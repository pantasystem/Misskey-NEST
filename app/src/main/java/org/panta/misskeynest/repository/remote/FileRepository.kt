package org.panta.misskeynest.repository.remote

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.network.OkHttpConnection
import org.panta.misskeynest.repository.IFileRepository
import java.io.File
import java.net.URL

class FileRepository(private val mConnectionProperty: ConnectionProperty) : IFileRepository{

    private val mConnection = OkHttpConnection()


    override fun create(folderId: String?, isSensitive: Boolean, force: Boolean, file: File): FileProperty? {
        val url = getURL("drive/files/create")
        val res = mConnection.postFile(url, mConnectionProperty.i, file, force = force, isSensitive = isSensitive,folderId = folderId)
            ?:return null
        return jacksonObjectMapper().readValue(res)
    }

    override fun delete(id: String): Boolean {
        val res = request("drive/files/delete",
            "i" to mConnectionProperty.i,
            "fileId" to id
            )
        return res != null
    }

    override fun getItems(
        limit: Int?,
        sinceId: String?,
        untilId: String?,
        folderId: String?,
        type: String?
    ): List<FileProperty>? {

        val res = request("drive/files",
            "i" to mConnectionProperty.i,
            "limit" to limit,
            "sinceId" to sinceId,
            "untilId" to untilId,
            "folderId" to folderId,
            "type" to type
        )?: return null

        return jacksonObjectMapper().readValue(res)

    }

    override fun show(id: String, url: URL): FileProperty? {


        val res = request("drive/files/show",
            "i" to mConnectionProperty.i ,
            "id" to id,
            "url" to url
            )
        res?: return null

        return jacksonObjectMapper().readValue(res)
    }

    override fun update(fileId: String, folderId: String?, fileName: String?, isSensitive: Boolean?): Boolean {
        val res = request("drive/files/update",
            "fileId" to fileId,
            "folderId" to folderId,
            "fileName" to fileName,
            "isSensitive" to isSensitive
        )
        return res != null
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



    private fun getURL(endPoint: String): URL{
        return URL("${mConnectionProperty.domain}/api/$endPoint")
    }
}