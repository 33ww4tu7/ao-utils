package fam.atlas.utils.service

import fam.atlas.utils.ao.PluginParameter
import fam.atlas.utils.ao.PluginParameterExt
import net.java.ao.EntityManager
import net.java.ao.test.jdbc.DatabaseUpdater

/**
 * Created on 04.06.2017.
 */
class DBUpdater : DatabaseUpdater {

    @Throws(Exception::class)
    override fun update(entityManager: EntityManager) {
        entityManager.migrate(PluginParameter::class.java, PluginParameterExt::class.java)
    }

}
