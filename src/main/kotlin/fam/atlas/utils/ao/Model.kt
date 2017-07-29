@file:Suppress("unused")

package fam.atlas.utils.ao

import net.java.ao.Entity
import net.java.ao.Preload
import net.java.ao.schema.*

/**
 *
 * Created on 04.06.2017.
 */
@Preload
@Table(value = "plugin_parameter")
interface PluginParameter : Entity {
    @get:Unique
    @get:StringLength(256)
    @get:Indexed
    var key: String
    @get:StringLength(StringLength.UNLIMITED)
    var value: String
}

@Preload
@Table(value = "plugin_parameter_ext")
@Indexes(Index(name = "key", methodNames = arrayOf("getKey")),
        Index(name = "ext", methodNames = arrayOf("getExt")),
        Index(name = "key_ext", methodNames = arrayOf("getKey", "getExt")))
interface PluginParameterExt : Entity {
    @get:StringLength(256)
    var key: String
    @get:StringLength(256)
    var ext: String?
    @get:StringLength(StringLength.UNLIMITED)
    var value: String
}