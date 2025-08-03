package com.github.radlance.kanbanboards.core

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.github.radlance.kanbanboards.navigation.core.NavGraph
import com.github.radlance.kanbanboards.uikit.KanbanBoardsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KanbanBoardsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    NavGraph(navHostController = rememberNavController())
                }
            }
        }
    }
}