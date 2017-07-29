package fam.atlas.utils.service

import com.atlassian.activeobjects.external.ActiveObjects
import com.atlassian.activeobjects.tx.Transactional
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport
import fam.atlas.utils.Q
import fam.atlas.utils.ao.PluginParameter
import fam.atlas.utils.ao.PluginParameterExt
import net.java.ao.DBParam
import org.springframework.stereotype.Component
import javax.inject.Inject

/**
 * Created on 04.06.2017.
 */

@Transactional
interface ParameterService {
    fun get(key: String): PluginParameter?
    fun set(key: String, value: String): PluginParameter
    fun getAll(): List<PluginParameter>

    fun like(prefix: String): List<PluginParameter>
    fun delete(key: String)
    fun deleteLike(prefix: String)
    fun deleteAll()
}

@Transactional
interface ParameterExtService {
    operator fun set(key: String, value: String, ext: String?): PluginParameterExt

    operator fun get(key: String, ext: String?): PluginParameterExt?

    fun getByKey(key: String): List<PluginParameterExt>
    fun getByExt(ext: String?): List<PluginParameterExt>
    fun delete(key: String, ext: String?)
    fun deleteByKey(key: String)
    fun deleteByExt(ext: String?)
}

@Suppress("unused")
@Component
open class ParameterServiceImpl @Inject constructor(@ComponentImport ao: ActiveObjects)
    : AbstractAoService<PluginParameter>(ao, PluginParameter::class.java), ParameterService {
    override fun get(key: String): PluginParameter? = get("KEY = ?", key.toLowerCase())

    override fun set(key: String, value: String): PluginParameter {
        return saveOrUpdate({ arrayOf(DBParam("KEY", key.toLowerCase())) },
                {
                    it.key = key
                    it.value = value
                }, Q.query().eq("KEY", key).build())
    }

    override fun like(prefix: String): List<PluginParameter> = getList(Q.query().like("KEY", prefix).build())

    override fun delete(key: String) = delete(Q.query().eq("KEY", key).build())

    override fun deleteLike(prefix: String) = delete(Q.query().like("KEY", prefix).build())

}

@Suppress("unused")
@Component
open class ParameterExtServiceImpl @Inject constructor(@ComponentImport ao: ActiveObjects) :
        AbstractAoService<PluginParameterExt>(ao, PluginParameterExt::class.java), ParameterExtService {

    override fun set(key: String, value: String, ext: String?): PluginParameterExt = saveOrUpdate({
        it.key = key
        it.ext = ext
        it.value = value
    }, Q.query().eq("KEY", key).and().eq("EXT", ext).build())

    override fun get(key: String, ext: String?): PluginParameterExt? = get(Q.query().eq("KEY", key).and().eq("EXT", ext).build())

    override fun getByKey(key: String): List<PluginParameterExt> = getList(Q.query().eq("KEY", key).build())

    override fun getByExt(ext: String?): List<PluginParameterExt> = getList(Q.query().eq("EXT", ext).build())

    override fun delete(key: String, ext: String?) = delete(Q.query().eq("KEY", key).and().eq("EXT", ext).build())

    override fun deleteByKey(key: String) = delete(Q.query().eq("KEY", key).build())

    override fun deleteByExt(ext: String?) = delete(Q.query().eq("EXT", ext).build())

}

