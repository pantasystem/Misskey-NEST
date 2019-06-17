package org.panta.misskeynest

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.panta.misskeynest.util.parseRGBv11
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    var testDate: Date? = null

    @Before
    fun createTestDateData(){
        //testDate = Date(Date().time -  60000)
    }

    @Test
    fun addition_isCorrect() {
        //val elapsed = ElapsedTimeFormatter()
        //val a = elapsed.formatTime(testDate!!)
        //assertEquals(a, "1時間前")
        val rgbList = parseRGBv11("RGB(255,100,255)")
        Assert.assertEquals(rgbList, listOf(255, 100, 255))
    }
}
