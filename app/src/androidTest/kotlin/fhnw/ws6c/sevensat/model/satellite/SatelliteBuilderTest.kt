package fhnw.ws6c.sevensat.model.satellite

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import fhnw.ws6c.R

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 *  ACHTUNG: Auf Android arbeiten wir (noch) mit JUNIT 4
 */
@RunWith(AndroidJUnit4::class)
class SatelliteBuilderTest {
    @Test
    fun useAppContext() {
        // given
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val prefs   = appContext.getSharedPreferences(appContext.getString(R.string.tle_preferences), Context.MODE_PRIVATE)
        val tleData = prefs.getString("25544", "")
        val lines = tleData?.split(";")
        val tleLine1 = lines?.get(1) ?: ""
        val tleLine2 = lines?.get(2) ?: ""

        // when
        val satellite = SatelliteBuilder().withPlainTextTleData(appContext, 25544).build()

        // then
        assertEquals(satellite.tleLine1, tleLine1)
        assertEquals(satellite.tleLine2, tleLine2)
    }
}