package io.pilgrimdb.common.migrations

import io.pilgrimdb.common.migrations.operations.Migration
import io.pilgrimdb.common.migrations.operations.MigrationIndex
import io.pilgrimdb.common.model.ProjectState
import java.util.Stack
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

open class Node(
    val key: MigrationIndex,
    val parents: MutableList<Node> = mutableListOf(),
    val children: MutableList<Node> = mutableListOf()
)

class DummyNode(
    key: MigrationIndex,
    val migration: Migration,
    val errorMessage: String,
    parents: MutableList<Node> = mutableListOf(),
    children: MutableList<Node> = mutableListOf()
) : Node(key, parents, children) {

    fun raiseError() {
        throw NodeNotFoundException(errorMessage)
    }
}

class Graph {

    val nodesMap: MutableMap<MigrationIndex, Node> = mutableMapOf()
    val nodes: MutableMap<MigrationIndex, Migration?> = mutableMapOf()

    fun addNode(migration: Migration) {
        assert(!nodesMap.containsKey(migration.index))
        val node = Node(migration.index)
        nodesMap[migration.index] = node
        nodes[migration.index] = migration
    }

    fun addDummyNode(key: MigrationIndex, migration: Migration, errorMessage: String) {
        val node = DummyNode(key, migration, errorMessage)
        nodesMap[key] = node
        nodes[key] = null
    }

    fun addDependency(migration: Migration, child: MigrationIndex, parent: MigrationIndex, skipValidation: Boolean = false) {
        if (!nodes.containsKey(child)) {
            addDummyNode(child, migration, "$migration dependencies reference nonexistent child node $child")
        }
        if (!nodes.containsKey(parent)) {
            addDummyNode(parent, migration, "$migration dependencies reference nonexistent parent node $parent")
        }
        nodesMap[parent]?.let { nodesMap[child]?.parents?.add(it) }
        nodesMap[child]?.let { nodesMap[parent]?.children?.add(it) }

        if (!skipValidation) {
            validateConsistency()
        }
    }

    fun validateConsistency() = nodesMap.values.filter { it is DummyNode }.map { it as DummyNode }.forEach {
        it.raiseError()
    }

    /**
     * Given a node, return a list of which previous nodes (dependencies) must be applied,
     * ending with the node itself. This is the list you would follow if applying the migrations to a database
     */
    fun forwardsPlan(target: MigrationIndex): MutableList<MigrationIndex> {
        if (!nodes.containsKey(target)) {
            throw NodeNotFoundException("$target is not a valid node")
        }
        return iterativeDfs(nodesMap[target]!!)
    }

    fun rootNodes() =
        nodes.asSequence()
            .filter { node ->
                nodesMap[node.key]!!.parents.map { it.key.packageName }.all { node.value?.packageName == it }
            }.map { it.key }.distinct().toList()

    fun leafNodes() =
        nodes.asSequence()
            .filter { node ->
                nodesMap[node.key]!!.children.map { it.key.packageName }.all { node.value?.packageName == it }
            }.map { it.key }.distinct().toList()

    /**
     * Given a migration nodes, return a complete ProjectState for it.
     * If [atEnd] is False, return the state before the migration has run
     * If no [nodes] are provided, return the overall most current project state
     */
    fun makeState(nodes: List<MigrationIndex> = leafNodes(), atEnd: Boolean = true): ProjectState {
        if (nodes.isEmpty()) {
            return ProjectState()
        }
        val plan = generatePlan(nodes, atEnd)
        var projectState = ProjectState()
        for (node in plan) {
            projectState = this.nodes[node]!!.mutateState(projectState)
        }

        return projectState
    }

    /**
     * Depth first search for finding dependencies
     */
    private fun iterativeDfs(start: Node, forwards: Boolean = true): MutableList<MigrationIndex> {
        val visited = mutableListOf<MigrationIndex>()
        val visitedSet = mutableSetOf<Node>()
        val stack = Stack<Pair<Node, Boolean>>()

        stack.push(Pair(start, false))

        while (!stack.empty()) {
            val (node, processed) = stack.pop()
            if (visitedSet.contains(node)) {
                continue
            } else if (processed) {
                visitedSet.add(node)
                visited.add(node.key)
            } else {
                stack.push(Pair(node, true))
                if (forwards) {
                    node.parents
                } else {
                    node.children
                }.asSequence().sortedWith(compareBy({ it.key.packageName }, { it.key.packageName }))
                    .forEach {
                        stack.push(Pair(it, false))
                    }
            }
        }
        return visited
    }

    private fun generatePlan(nodes: List<MigrationIndex>, atEnd: Boolean) =
        nodes.flatMap { forwardsPlan(it) }.distinct().takeWhile { atEnd or !nodes.contains(it) }.toList()
}