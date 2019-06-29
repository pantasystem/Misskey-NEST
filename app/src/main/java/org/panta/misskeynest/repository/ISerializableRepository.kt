package org.panta.misskeynest.repository

import java.io.Serializable

interface ISerializableRepository {
    fun load(key: String): Serializable?
    fun write(key: String, value: Serializable)

}