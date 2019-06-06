package io.pilgrimdb.common.migrations

import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import io.pilgrimdb.common.migrations.operations.Migration
import io.pilgrimdb.common.migrations.operations.MigrationIndex
import io.pilgrimdb.common.model.ProjectState
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldHaveKey
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GraphTest {

    var graph = Graph()
    val migration = Migration("package", "name")

    @BeforeEach
    fun setup() {
        graph = Graph()
    }

    @Test
    fun testAddNode() {
        graph.addNode(migration)

        graph.nodes shouldHaveKey migration.index
        graph.nodes[migration.index] shouldEqual migration
        graph.nodesMap shouldHaveKey migration.index
        graph.nodesMap[migration.index] shouldBeInstanceOf Node::class
    }

    @Test
    fun testAddDummyNode() {
        graph.addDummyNode(MigrationIndex("test", "test"), migration, "error")

        graph.nodes shouldHaveKey MigrationIndex("test", "test")
        graph.nodes[MigrationIndex("test", "test")].shouldBeNull()
        graph.nodesMap shouldHaveKey MigrationIndex("test", "test")
        graph.nodesMap[MigrationIndex("test", "test")] shouldBeInstanceOf DummyNode::class
    }

    @Nested
    inner class AddDependencyTest {

        @Test
        fun `Adding dependency should link the 2 nodes`() {
            graph.addNode(Migration("package1", "migration1"))
            graph.addNode(Migration("package2", "migration2"))

            graph.addDependency(
                graph.nodes[MigrationIndex("package2", "migration2")]!!,
                MigrationIndex("package2", "migration2"),
                MigrationIndex("package1", "migration1")
            )

            graph.nodesMap[MigrationIndex("package2", "migration2")]!!.parents.size shouldEqual 1
            graph.nodesMap[
                MigrationIndex(
                    "package2",
                    "migration2"
                )
            ]!!.parents[0].key shouldEqual MigrationIndex("package1", "migration1")
            graph.nodesMap[MigrationIndex("package1", "migration1")]!!.children.size shouldEqual 1
            graph.nodesMap[
                MigrationIndex(
                    "package1",
                    "migration1"
                )
            ]!!.children[0].key shouldEqual MigrationIndex("package2", "migration2")
        }

        @Test
        fun `Adding dependency to a node that doesnt exist should create a dummy node`() {
            graph.addNode(Migration("package1", "migration1"))

            graph.addDependency(
                graph.nodes[MigrationIndex("package1", "migration1")]!!,
                MigrationIndex("package1", "migration1"),
                MigrationIndex("doesntexist", "doesntexist"),
                true
            )

            graph.nodesMap[MigrationIndex("package1", "migration1")]!!.parents.size shouldEqual 1
            graph.nodesMap[
                MigrationIndex(
                    "package1",
                    "migration1"
                )
            ]!!.parents[0].key shouldEqual MigrationIndex("doesntexist", "doesntexist")

            graph.nodesMap[MigrationIndex("doesntexist", "doesntexist")]!! shouldBeInstanceOf DummyNode::class
            (
                graph.nodesMap[
                    MigrationIndex(
                        "doesntexist",
                        "doesntexist"
                    )
                ]!! as DummyNode
                ).errorMessage shouldEqual "Migration(packageName=package1, migrationName=migration1, dependencies=[], operations=[]) dependencies reference nonexistent parent node MigrationIndex(packageName=doesntexist, migrationName=doesntexist)"
        }

        @Test
        fun `Adding dependency from a node that doesnt exist should create a dummy node`() {
            graph.addNode(Migration("package1", "migration1"))

            graph.addDependency(
                graph.nodes[MigrationIndex("package1", "migration1")]!!,
                MigrationIndex("doesntexist", "doesntexist"),
                MigrationIndex("package1", "migration1"),
                true
            )

            graph.nodesMap[MigrationIndex("package1", "migration1")]!!.children.size shouldEqual 1
            graph.nodesMap[
                MigrationIndex(
                    "package1",
                    "migration1"
                )
            ]!!.children[0].key shouldEqual MigrationIndex("doesntexist", "doesntexist")
            graph.nodesMap[MigrationIndex("doesntexist", "doesntexist")]!! shouldBeInstanceOf DummyNode::class
            (
                graph.nodesMap[
                    MigrationIndex(
                        "doesntexist",
                        "doesntexist"
                    )
                ]!! as DummyNode
                ).errorMessage shouldEqual "Migration(packageName=package1, migrationName=migration1, dependencies=[], operations=[]) dependencies reference nonexistent child node MigrationIndex(packageName=doesntexist, migrationName=doesntexist)"
        }
    }

    @Nested
    inner class ForwardsPlanTest {

        @Test
        fun `should return the dependencies first (one package)`() {
            val migration1 = Migration("package", "1")
            val migration2 = Migration("package", "2")
            val migration3 = Migration("package", "3")

            with(graph) {
                addNode(migration1)
                addNode(migration2)
                addNode(migration3)
                addDependency(migration3, migration3.index, migration2.index)
                addDependency(migration2, migration2.index, migration1.index)
            }

            val result = graph.forwardsPlan(migration3.index)

            result shouldEqual mutableListOf(migration1.index, migration2.index, migration3.index)
        }

        @Test
        fun `should return the dependencies first (multiple packages)`() {
            val migrations = listOf(
                Migration("package1", "1"),
                Migration("package1", "2"),
                Migration("package1", "3"),
                Migration("package2", "1"),
                Migration("package2", "2")
            )

            with(graph) {
                migrations.forEach { addNode(it) }
                addDependency(migrations[1], migrations[1].index, migrations[0].index)
                addDependency(migrations[2], migrations[2].index, migrations[1].index)
                addDependency(migrations[4], migrations[4].index, migrations[3].index)
                addDependency(migrations[4], migrations[4].index, migrations[1].index)
            }

            // migration package2, 2 depends on [(package2, 1), (package1, 2), (package1, 1)]
            graph.forwardsPlan(migrations[4].index) shouldEqual listOf(
                migrations[3].index,
                migrations[0].index,
                migrations[1].index,
                migrations[4].index
            )
        }
    }

    @Nested
    inner class MakeStateTest {

        @Test
        fun `should return empty state if no nodes`() {
            val result = graph.makeState()

            result.models.shouldBeEmpty()
        }

        @Test
        fun `should call mutate state on migrations`() {
            val migration = mockk<Migration>()
            every { migration.mutateState(any()) } returns ProjectState()
            every { migration.index } returns MigrationIndex("test", "test")
            graph.addNode(migration)

            graph.makeState()

            verify { migration.mutateState(any()) }
        }

        @Test
        fun `should call mutate state on migrations in correct order`() {
            val migration1 = mockk<Migration>()
            every { migration1.mutateState(any()) } returns ProjectState()
            every { migration1.index } returns MigrationIndex("test", "test1")
            every { migration1.packageName } returns "test"
            graph.addNode(migration1)

            val migration2 = mockk<Migration>()
            every { migration2.mutateState(any()) } returns ProjectState()
            every { migration2.index } returns MigrationIndex("test", "test2")
            every { migration2.packageName } returns "test"

            graph.addNode(migration2)
            graph.addDependency(migration2, migration2.index, migration1.index)

            graph.makeState()

            verifyOrder {
                migration1.mutateState(any())
                migration2.mutateState(any())
            }
        }

        @Test
        fun `should not run the last migration if atEnd is false`() {
            val migration1 = mockk<Migration>()
            every { migration1.mutateState(any()) } returns ProjectState()
            every { migration1.index } returns MigrationIndex("test", "test1")
            every { migration1.packageName } returns "test"
            graph.addNode(migration1)

            val migration2 = mockk<Migration>()
            every { migration2.mutateState(any()) } returns ProjectState()
            every { migration2.index } returns MigrationIndex("test", "test2")
            every { migration2.packageName } returns "test"

            graph.addNode(migration2)
            graph.addDependency(migration2, migration2.index, migration1.index)

            graph.makeState(nodes = listOf(migration2.index), atEnd = false)

            verify { migration1.mutateState(any()) }
            verify { migration2.mutateState(any()) wasNot Called }
        }
    }
}