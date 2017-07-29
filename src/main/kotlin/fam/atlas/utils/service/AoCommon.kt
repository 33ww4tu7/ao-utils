@file:Suppress("unused")

package fam.atlas.utils.service

import com.atlassian.activeobjects.external.ActiveObjects
import net.java.ao.DBParam
import net.java.ao.Entity
import net.java.ao.Query

/**
 * Abstract service class contains most commonly used ao operations.
 *
 * All methods are executed the simple way as transaction does not work properly.
 */
abstract class AbstractAoService<T : Entity> protected constructor(val ao: ActiveObjects, val aClass: Class<T>) {

    /**
     * Saves the AO object in transaction
     * @param t object to save
     * @return saved object
     */
    fun save(t: T): T {
        t.save()
        ao.flush(t)
        return t
    }

    /**
     * Searches for the **first object** for specified query. If none found will create new object in database.
     * @param update function to update found or created instance before saving
     * @param query  query to search for the object
     * @param params optional parameters for query
     * @return created or updated instance of an object
     */
    inline fun saveOrUpdate(update: (T) -> Unit, query: String, vararg params: String): T =
            saveOrUpdate(update, Query.select().where(query, *params))

    /**
     * Searches for the **first object** for specified query. If none found will create new object in database.
     * @param update function to update found or created instance before saving
     * @param query  query to search for the object
     * @return created or updated instance of an object
     */
    inline fun saveOrUpdate(update: (T) -> Unit, query: Query): T {
        val o = ao.find(aClass, query).firstOrNull() ?: ao.create(aClass)
        update(o)
        o.save()
        return o
    }

    /**
     * Searches for the **first object** for specified query. If none found will create new object in database.
     * @param defaults specifies default parameters
     * @param update   function to update found or created instance before saving
     * @param query    query to search for the object
     * @param params   optional parameters for query
     * @return created or updated instance of an object
     */
    fun saveOrUpdate(defaults: () -> Array<DBParam>, update: (T) -> Unit, query: String, vararg params: Any): T =
            saveOrUpdate(defaults, update, Query.select().where(query, *params))

    /**
     * Searches for the **first object** for specified query. If none found will create new object in database.
     * @param defaults specifies default parameters
     * @param update   function to update found or created instance before saving
     * @param query    query to search for the object
     * @return created or updated instance of an object
     */
    fun saveOrUpdate(defaults: () -> Array<DBParam>, update: (T) -> Unit, query: Query): T {
        val o = ao.find(aClass, query).firstOrNull() ?: ao.create(aClass, *defaults())
        update(o)
        o.save()
        return o
    }

    /**
     * Get AO Entity by ID
     * @param id id of an entity
     * @return entity or NULL
     */
    operator fun get(id: Int): T? = ao.get(aClass, id)

    /**
     * Get single Optional of AO object by query with optional params
     * @param query  query to execute
     * @param params optional parameters
     * @return Optional of single result
     */
    operator fun get(query: String, vararg params: Any): T? = ao.find(aClass, Query.select().where(query, *params)).firstOrNull()

    /**
     * Get list of results for the following query
     * @param query  query to execute
     * @param params optional parameters
     * @return list of results
     */
    fun getList(query: String, vararg params: Any): List<T> = getList(Query.select().where(query, *params))

    /**
     * Get single Optional of AO object by query
     * @param query query object
     * @return Optional of single result
     */
    operator fun get(query: Query): T? = ao.find(aClass, query).firstOrNull()

    /**
     * Get list of results for the following query
     * @param query query to execute
     * @return list of results
     */
    fun getList(query: Query): List<T> = ao.find(aClass, query).toList()

    /**
     * Returns a [List] containing all elements.
     */
    fun getAll(): List<T> = ao.find(aClass).toList()

    /**
     * Deletes all objects matching query
     * @param query  query to search objects
     * @param params optional parameters
     */
    fun delete(query: String, vararg params: Any) = delete(Query.select().where(query, *params))

    /**
     * Deletes all objects matching query
     * @param query query to search objects
     */
    fun delete(query: Query) = ao.delete(*ao.find(aClass, query))

    /**
     * Delete specific entity

     * @param entity entity to delete
     */
    @SafeVarargs
    fun delete(vararg entity: T) = ao.delete(*entity)

    /**
     * Delete all entities of type
     */
    fun deleteAll() = delete("ID > ?", 0)

}
