package com.slobozhaninova.randomfood

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.slobozhaninova.randomfood.listFood.FoodListScreen
import com.slobozhaninova.randomfood.randomFood.RandomFoodScreen
import com.slobozhaninova.randomfood.ui.theme.RandomFoodTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomFoodTheme {
                navigationContent()
            }
        }
    }
}

@Composable
fun navigationContent() {
    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = "randomFood",
    ) {
        composable("randomFood") {
            RandomFoodScreen(
                onEditFoodClick = { navController.navigate("addFood") }
            )
        }
        composable("addFood") {
            FoodListScreen()
        }
    }


}
