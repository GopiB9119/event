package com.communityledger.app

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun distributionFlavorUsesExpectedUpdateChannel() {
    when (BuildConfig.FLAVOR) {
      "direct" -> assertTrue(BuildConfig.DIRECT_UPDATE_ENABLED)
      "play" -> assertFalse(BuildConfig.DIRECT_UPDATE_ENABLED)
      else -> fail("Unexpected distribution flavor: ${BuildConfig.FLAVOR}")
    }
  }
}
