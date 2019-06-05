package org.panta.misskey_nest.network

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class JSON{
    private val mapper = jacksonObjectMapper()
    fun<E> parseObject(json: String, classes: Class<E>):E{
        return mapper.readValue(json,classes)
    }

    fun<E> stringify(obj: E):String{
        return mapper.writeValueAsString(obj)
    }
}