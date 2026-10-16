package com.andreaszel.booklcalendar

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.andreaszel.booklcalendar.ui.AppNavHost
import com.andreaszel.booklcalendar.ui.theme.ComicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT), navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        setContent {
            ComicTheme {
                AppNavHost()
            }
        }
    }
}
