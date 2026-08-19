package com.bloom.wellness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.bloom.wellness.navigation.BloomNavHost
import com.bloom.wellness.ui.theme.BloomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BloomTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BloomNavHost()
                }
            }
        }
    }
}
