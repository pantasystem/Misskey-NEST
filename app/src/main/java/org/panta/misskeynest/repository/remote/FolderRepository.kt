package org.panta.misskeynest.repository.remote

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.FolderProperty
import org.panta.misskeynest.network.OkHttpConnection
import org.panta.misskeynest.repository.IFolderRepository
import java.net.URL

class FolderRepository(private val mConnectionProperty: ConnectionProperty) : IFolderRepository{

    private val mConnection = OkHttpConnection()

    override fun create(name: String, parentId: String?): Boolean {
        val res = request(
            "drive/folders/create",
            "i" to mConnectionProperty.i,
            "name" to name,
            "parentId" to parentId
        )
        return res != null
    }

    override fun delete(folderId: String): Boolean {
        val res = request(
            "drive/folders/delete",
            "i" to mConnectionProperty.i,
            "folderId" to folderId
        )
        return res != null
    }

    override fun find(name: String, parentId: String?): List<FolderProperty>? {
        val res = request(
            "drive/folders/find",
            "i" to mConnectionProperty.i,
            "name" to name,
            "parentId" to parentId
        )?: return null
        return jacksonObjectMapper().readValue(res)
    }

    override fun getItems(limit: Int, sinceId: String?, untilId: String?, folderId: String?): List<FolderProperty>? {
       val res = request(
           "drive/folders",
           "i" to mConnectionProperty.i,
           "limit" to limit,
           "sinceId" to sinceId,
           "untilId" to untilId,
           "folderId" to folderId
       )?: return null
        return jacksonObjectMapper().readValue(res)
    }

    override fun show(folderId: String): FolderProperty? {
        val res = request(
            "drive/folders/show",
            "i" to mConnectionProperty.i,
            "folderId" to folderId
            )?: return null

        return jacksonObjectMapper().readValue(res)
    }

    override fun update(folderId: String, name: String, parentId: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun request(endPoint: String, vararg value: Pair<String, Any?>): String?{
        val map = HashMap<String, Any>()
        for( p in value){
            if( p.second != null){
                map[p.first] = p.second!!
            }
        }
        val json = jacksonObjectMapper().writeValueAsString(map)
        return mConnection.postString(getURL(endPoint), json)
    }

    private fun getURL(endPoint: String): URL {
        return URL("${mConnectionProperty.domain}/api/$endPoint")
    }
}