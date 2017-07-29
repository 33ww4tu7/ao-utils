package fam.atlas.utils.service

import com.atlassian.activeobjects.external.ActiveObjects
import com.atlassian.activeobjects.test.TestActiveObjects
import fam.atlas.utils.Q
import fam.atlas.utils.ao.PluginParameterExt
import net.java.ao.EntityManager
import net.java.ao.test.jdbc.Data
import net.java.ao.test.junit.ActiveObjectsJUnitRunner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Created on 04.06.2017.
 */
@SuppressWarnings("ALL")
@RunWith(ActiveObjectsJUnitRunner::class)
@Data(DBUpdater::class)
class ParameterExtServiceImplTest {

    internal var entityManager: EntityManager? = null
    internal var params: ParameterExtService? = null
    internal var ao: ActiveObjects? = null
    @Before
    fun init() {
        assertNotNull(entityManager)
        ao = TestActiveObjects(entityManager)
        params = ParameterExtServiceImpl(ao!!)
        params?.set("key1", "val1", null) ?: assert(false)
        params?.set("key2", "val2", "ext2") ?: assert(false)
        params?.set("key3", "val3", "ext3") ?: assert(false)
    }

    @Test
    fun set() {
        params?.set("key4", "val2", "ext4") ?: assert(false)
    }

    @Test
    fun get() {
        params!!["key1", null] ?: assert(false)

        params!!["key2", "ext2"] ?: assert(false)
        if (params!!["key", null] != null) assert(false)
        if (params!!["key", "ext2"] != null) assert(false)
    }

    @Test
    fun getIn() {
        val arr = ao!!.find(PluginParameterExt::class.java, Q.query().isIn("KEY", listOf("key1", "key2")).build())
        assertFalse { arr.isEmpty() }
    }

    @Test
    fun getByKey() {
        assertTrue { params!!.getByKey("key1").isNotEmpty() }
        assertTrue { params!!.getByKey("key").isEmpty() }
    }

    @Test
    fun getByExt() {
        assertTrue { params!!.getByExt("ext2").isNotEmpty() }
        assertTrue { params!!.getByExt(null).isNotEmpty() }
        assertTrue { params!!.getByExt("key").isEmpty() }
    }

    @Test
    fun delete() {
        params!!["key1", null] ?: assert(false)
        params!!["key2", "ext2"] ?: assert(false)
        params!!.delete("key2", "ext2")
        params!!.delete("key1", null)
        if (params!!["key1", null] != null) assert(false)
        if (params!!["key2", "ext2"] != null) assert(false)
    }

    @Test
    fun deleteByKey() {
        assertTrue { params!!.getByKey("key1").isNotEmpty() }
        params!!.deleteByKey("key1")
        assertTrue { params!!.getByKey("key1").isEmpty() }
    }

    @Test
    fun deleteByExt() {
        assertTrue { params!!.getByExt("ext2").isNotEmpty() }
        params!!.deleteByExt("ext2")
        assertTrue { params!!.getByExt("ext2").isEmpty() }
        assertTrue { params!!.getByExt(null).isNotEmpty() }
        params!!.deleteByExt(null)
        assertTrue { params!!.getByExt("null").isEmpty() }
    }

}