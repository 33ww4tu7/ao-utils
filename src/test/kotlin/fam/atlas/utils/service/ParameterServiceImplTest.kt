package fam.atlas.utils.service

import com.atlassian.activeobjects.external.ActiveObjects
import com.atlassian.activeobjects.test.TestActiveObjects
import net.java.ao.EntityManager
import net.java.ao.test.jdbc.Data
import net.java.ao.test.junit.ActiveObjectsJUnitRunner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Created on 04.06.2017.
 */
@SuppressWarnings("ALL")
@RunWith(ActiveObjectsJUnitRunner::class)
@Data(DBUpdater::class)
class ParameterServiceImplTest {

    internal var entityManager: EntityManager? = null
    internal var params: ParameterService? = null
    internal var ao: ActiveObjects? = null
    @Before
    fun init() {
        assertNotNull(entityManager)
        ao = TestActiveObjects(entityManager)
        params = ParameterServiceImpl(ao!!)
        params?.set("key1", "val1") ?: assert(false)
        params?.set("key2", "val2") ?: assert(false)
        params?.set("key3", "val3") ?: assert(false)
    }

    @Test
    fun get() {
        params?.get("key1").let { assertNotNull(it) }
    }

    @Test
    fun set() {
        params?.set("key4", "val4") ?: assert(false)
        params?.get("key4").let { assertNotNull(it); assertTrue(it?.value == "val4") }
    }

    @Test
    fun like() {
        params?.like("key").let { assertTrue(it?.size == 3) }
    }

    @Test
    fun delete() {
        assertTrue { params?.get("key2") != null }
        params?.delete("key2")
        assertTrue { params?.get("key2") == null }
    }

    @Test
    fun deleteLike() {
        assertTrue { params?.like("key")?.size == 3 }
        params?.deleteLike("key")
        assertTrue {params?.like("key")?.size == 0 }
    }

}