package fam.atlas.utils

import net.java.ao.Query
import java.util.*

/**
 * interface for chaining restrictions in [Q]
 */
interface QQ {
    fun eq(key: String, value: Any?): OO
    fun isIn(key: String, value: List<String>): OO
    fun eqNotNull(key: String, value: Any): OO
    fun eqIgnore(key: String, value: Any?): OO
    fun like(prefix: String, value: Any): OO
    fun start(): QQ
}

/**
 * interface for chaining actions in [Q]
 */
interface OO {
    fun or(): QQ
    fun and(): QQ
    fun end(): OO
    fun build(): Query
}

/**
 * Class provides chained Query builder for AO database services
 */
class Q private constructor(
        private var query: String = String(),
        private var params: MutableList<Any> = ArrayList()) : QQ, OO {

    companion object {
        fun query(): QQ = Q()
    }

    override fun eq(key: String, value: Any?): OO {
        query = "$query ${if (value == null) "$key IS NULL" else "$key = ?"}"
        value?.let { params.add(it) }
        return this
    }

    override fun isIn(key: String, value: List<String>): OO {
        query = "$query $key IN ( ${value.map { "'$it'" }.joinToString(separator = ", ")} )"
        return this
    }

    override fun eqNotNull(key: String, value: Any): OO {
        query = "$query $key = ?"
        params.add(value)
        return this
    }

    override fun eqIgnore(key: String, value: Any?): OO {
        value?.let {
            query = "$query $key = ?"
            params.add(it)
        }
        return this
    }

    override fun like(prefix: String, value: Any): OO {
        query = "$query $prefix LIKE ?%"
        params.add(value)
        return this
    }

    override fun or(): QQ {
        query = "$query OR "
        return this
    }

    override fun and(): QQ {
        query = "$query AND "
        return this
    }

    override fun start(): QQ {
        query = "$query ("
        return this
    }

    override fun end(): OO {
        query = "$query)"
        return this
    }

    override fun build(): Query {
        return Query.select().where(query, *params.toTypedArray())
    }

}