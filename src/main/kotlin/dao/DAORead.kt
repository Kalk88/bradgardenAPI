package dao

import kotlin.collections.ArrayList

/**
 * Created by kalk on 8/12/17.
 * Basic implementation of the DAOReadInterface
 */
class DAORead: DAOReadInterface {

    override fun get(limit: Int, offset: Int): ArrayList<Any> {
        return ArrayList<Any>()
    }

    override fun getDetailed(id: Int): Any {
        return  Any()
    }
}