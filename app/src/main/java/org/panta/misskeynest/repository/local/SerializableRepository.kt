package org.panta.misskeynest.repository.local

import android.content.Context
import android.util.Log
import org.panta.misskeynest.repository.ISerializableRepository
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class SerializableRepository (private val context: Context): ISerializableRepository{

    override fun load(key: String): Serializable? {
        try{
            val input = context.openFileInput(key)
            ObjectInputStream(input).use{
                val a = it.readObject()
                return a as Serializable
            }
        }catch(e: Exception){
            Log.e("SerializableRepository", "error", e)
            return null
        }
    }

    override fun write(key: String, value: Serializable) {
        try{
            val os = context.openFileOutput(key, Context.MODE_PRIVATE)
            ObjectOutputStream(os).use{
                it.writeObject(value)

            }

        }catch (e: Exception){
            Log.e("SerializableRepository", "error", e)
        }

    }
}