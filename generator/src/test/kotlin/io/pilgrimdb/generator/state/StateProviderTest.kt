package io.pilgrimdb.generator.state

import io.pilgrimdb.common.annotations.PilgrimModel
import io.pilgrimdb.common.model.ModelState
import io.pilgrimdb.common.model.ProjectModelEntry
import io.pilgrimdb.generator.state.adapters.StateAdapter
import kotlin.reflect.KClass
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldHaveKey
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@PilgrimModel
object TestClass

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StateProviderTest {

    @Test
    fun testGetState() {
        val dummyModel = ModelState("TestClass")

        val stateAdapter = object : StateAdapter {
            override fun convertModel(klass: KClass<out Any>): ModelState {
                return dummyModel
            }

            override fun isSupported(klass: KClass<out Any>): Boolean {
                return true
            }
        }

        val provider = StateProvider("io.pilgrimdb.generator.state", listOf(stateAdapter))
        val result = provider.getState()

        result.models.size shouldEqual 1
        result.models shouldHaveKey ProjectModelEntry("io.pilgrimdb.generator.state", "TestClass")
    }
}