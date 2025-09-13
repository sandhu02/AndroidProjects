package com.example.myclock

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.glance.LocalContext
import androidx.glance.text.FontFamily

class ClockWidget : GlanceAppWidget() {

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(
            DpSize(120.dp, 60.dp),
            DpSize(150.dp, 100.dp),
            DpSize(180.dp, 120.dp)
        )
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        scheduleNextUpdate(context)

        provideContent {
            val size = LocalSize.current
            ClockWidgetContent()
        }
    }
}

@Composable
fun ClockWidgetContent() {
    val context = LocalContext.current
    val currentTime = getCurrentTime()
    val currentDate = getCurrentDate()
    val battery = getBatteryPercentage(context)

    val font = FontFamily.Monospace

    Column(
        modifier = GlanceModifier
            .background(ColorProvider(day = Color(0xFFf0f0f0), night = Color(0xFF3d3d3d)))
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Header Row
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "{}  ",
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xFF03adf0),
                        night = Color(0xFFFFFF00)
                    ),
                    fontFamily = font
                )

            )
            Text(
                text = "stats.json",
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xffef85ef),
                        night = Color(0xFF107A10)
                    ),
                    fontFamily = font
                )
            )
        }

        Spacer(modifier = GlanceModifier.height(4.dp))
        Text(
            text = "{",
            style = TextStyle(
                color = ColorProvider(
                    day = Color(0xFF03adf0),
                    night = Color(0xFFFFFF00)
                ),
                fontFamily = font
            )
        )

        // NOW block
        Row(modifier = GlanceModifier.padding(start = 8.dp)) {
            Text(
                text = "\"now\"",
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xffff0000),
                        night = Color(0xFF00FFFF)
                    ),
                    fontFamily = font
                )
            )
            Text(
                text = ":",
                modifier = GlanceModifier.padding(end = 4.dp),
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xFF000000),
                        night = Color(0xFFFFFFFF)
                    ),
                    fontFamily = font
                )
            )
            Text(
                text = "{",
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xff3dda72),
                        night = Color(0xFFc2258d)
                    ),
                    fontFamily = font
                )
            )
        }

        Row(modifier = GlanceModifier.padding(start = 16.dp)) {
            Text(
                text = "\"time\"",
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xffff0000),
                        night = Color(0xFF00FFFF)
                    ),
                    fontFamily = font
                )
            )
            Text(
                text = ":",
                modifier = GlanceModifier.padding(end = 4.dp),
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xFF000000),
                        night = Color(0xFFFFFFFF)
                    ),
                    fontFamily = font
                )
            )
            Text(
                text = "\"$currentTime\"",
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xffef85ef),
                        night = Color(0xFF107A10)
                    ),
                    fontFamily = font
                )
            )
            Text(
                text = ",",
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xFF000000),
                        night = Color(0xFFFFFFFF)
                    ),
                    fontFamily = font
                )
            )
        }

        Row(modifier = GlanceModifier.padding(start = 16.dp)) {
            Text(
                text = "\"date\"",
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xffff0000),
                        night = Color(0xFF00FFFF)
                    ),
                    fontFamily = font
                )
            )
            Text(
                text = ":",
                modifier = GlanceModifier.padding(end = 4.dp),
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xFF000000),
                        night = Color(0xFFFFFFFF)
                    ),
                    fontFamily = font
                )
            )
            Text(
                text = "\"$currentDate\"",
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xFF000000),
                        night = Color(0xFFFFFFFF)
                    ),
                    fontFamily = font
                )
            )
        }

        Row(modifier = GlanceModifier.padding(start = 8.dp)) {
            Text(
                text = "}",
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xff3dda72),
                        night = Color(0xFFc2258d)
                    ),
                    fontFamily = font
                )
            )
            Text(
                text = ",",
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xFF000000),
                        night = Color(0xFFFFFFFF)
                    ),
                    fontFamily = font
                )
            )
        }

        // Battery
        Row(modifier = GlanceModifier.padding(start = 8.dp)) {
            Text(
                text = "\"battery\"",
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xffff0000),
                        night = Color(0xFF00FFFF)
                    ),
                    fontFamily = font
                )
            )
            Text(
                text = ":",
                modifier = GlanceModifier.padding(end = 4.dp),
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xFF000000),
                        night = Color(0xFFFFFFFF)
                    ),
                    fontFamily = font
                )
            )
            Text(
                text = "$battery",
                style = TextStyle(
                    color = ColorProvider(
                        day = Color(0xff5b9c29),
                        night = Color(0xFFa463d6)
                    )
                    ,
                    fontFamily = font
                )
            )
        }

        Text(
            text = "}",
            style = TextStyle(
                color = ColorProvider(
                    day = Color(0xFF03adf0),
                    night = Color(0xFFFFFF00)
                ),
                fontFamily = font
            )
        )
    }
}


/** Helper to get current time formatted */
fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date())
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return sdf.format(Date())
}

fun getBatteryPercentage(context: Context): Int {
    val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    val batteryStatus = context.registerReceiver(null, intentFilter)

    val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

    return if (level >= 0 && scale > 0) {
        (level * 100) / scale
    } else {
        -1 // could not read battery
    }
}
