package org.panta.misskey_nest.network

import java.io.BufferedInputStream
import java.io.InputStream
import java.io.PrintStream
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class StreamHttpsConnection{
    fun get(url: URL): InputStream {

        val con = (url.openConnection() as HttpsURLConnection).apply{
            requestMethod = "GET"

            connect()

        }
        return BufferedInputStream(con.inputStream)
    }

    @Deprecated("OkHttpConnection#postStringに移行予定") fun post(url: URL, value: String): InputStream? {
        try{
            val con = (url.openConnection() as HttpsURLConnection).apply{
                requestMethod = "POST"
                instanceFollowRedirects = true
                doOutput = true
                connectTimeout = 3000

                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                val os = this.outputStream
                val ps = PrintStream(os)
                ps.print(value)
                ps.close()


            }
            return BufferedInputStream(con.inputStream)
        }catch(e: Exception){
            return null
        }

    }
}