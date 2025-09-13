package com.example.roomtest

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.roomtest.ui.theme.RoomTestTheme

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.roomtest", appContext.packageName)
    }

//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun uiTest(){
//        composeTestRule.setContent {
//            RoomTestTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    MainScreen(
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
//
//        composeTestRule.onNodeWithText("Count value is 0").assertExists("no node with this string")
//        composeTestRule.onNodeWithTag("Increase Counter").performClick()
//        composeTestRule.onNodeWithText("Count value is 1").assertExists("no node with this string")
//        composeTestRule.onNodeWithTag("Reset Counter").performClick()
//        composeTestRule.onNodeWithText("Count value is 0").assertExists("no node with this string")
//    }
}