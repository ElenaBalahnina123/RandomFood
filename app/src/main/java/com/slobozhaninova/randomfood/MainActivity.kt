package com.slobozhaninova.randomfood

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.slobozhaninova.randomfood.addFood.AddFoodScreen
import com.slobozhaninova.randomfood.addFood.AddFoodViewModel
import com.slobozhaninova.randomfood.listFood.FoodListScreen
import com.slobozhaninova.randomfood.listFood.FoodListViewModel
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
            val viewModel = hiltViewModel<FoodListViewModel>()
            val randomFood by viewModel.randomFood.collectAsState()
            val allFoods by viewModel.allFood.collectAsState()
            RandomFoodScreen(
                randomFood = randomFood,
                allFoods = allFoods,
                onClickRandomFood = viewModel::generateRandomFood,
                onEditFoodClick = { navController.navigate("list") },
                onAddFoodClick = { navController.navigate("add") }
            )
        }
        composable("list") {
            val viewModel = hiltViewModel<FoodListViewModel>()
            val listState by viewModel.listState.collectAsState()
            FoodListScreen(
                listState = listState,
                onBack = { navController.navigate("randomFood") },
                onAddFoodClick = { navController.navigate("add") },
                query = viewModel::query,
                selectedCategory = viewModel::selectedCategory,
                deleteFood = viewModel::deleteFood
            )
        }
        composable("add") {
            val viewModel = hiltViewModel<AddFoodViewModel>()
            val addFoodState by viewModel.addFoodState.collectAsState()
            AddFoodScreen(
                addFoodState = addFoodState,
                onBack = { navController.navigate("list") },
                setAddFoodName = viewModel::setAddFoodName,
                setAddFoodCategory = viewModel::setAddFoodCategory,
                onAddClick = {
                    viewModel.addFood()
                    navController.navigate("list")
                }
            )
        }
    }


}
