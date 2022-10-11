package com.marosseleng.demos.ideas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.marosseleng.demos.ideas.ui.Clock
import com.marosseleng.demos.ideas.ui.clockFlow
import com.marosseleng.demos.ideas.ui.theme.IdeasTheme
import java.time.LocalTime

@ExperimentalUnitApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val flowState by clockFlow.collectAsState(initial = LocalTime.of(0, 0))

            IdeasTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .scrollable(rememberScrollState(), Orientation.Vertical),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Clock(modifier = Modifier.padding(top = 16.dp), time = flowState)
                    }
                }
            }
        }
    }
}