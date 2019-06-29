package org.panta.misskeynest

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.repository.remote.FolderRepository

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {



    @Before
    fun createTestDateData(){
        //testDate = Date(Date().time -  60000)
    }

    @Test
    fun addition_isCorrect() {
        //val elapsed = ElapsedTimeFormatter()
        //val a = elapsed.formatTime(testDate!!)
        val connectionProperty = ConnectionProperty(domain = "https://misskey.io", i = ApplicationTestConstant.i, userPrimaryId = "7roinhytrr")
        //assertEquals(a, "1時間前")
        /*val fileRepository =FileRepository(connectionProperty)
        val response = fileRepository.getItems(null, null, null, null, null)
        println(response)*/

        val folderRepository = FolderRepository(connectionProperty)

        for(n in 0 until 30){
            //val res = folderRepository.create("hello$n", null)
            val result = folderRepository.find("hello$n", null)
            println(result)
            Assert.assertNotEquals(result, null)

            val id = result?.firstOrNull()?.id

            if(id != null){
                val res = folderRepository.delete(id)
                //Assert.assertEquals(res, true)
                println(res)
            }


        }

    }
}
