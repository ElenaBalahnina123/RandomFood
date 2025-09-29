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
import com.slobozhaninova.randomfood.addCategory.AddCategoryScreen
import com.slobozhaninova.randomfood.addCategory.AddCategoryViewModel
import com.slobozhaninova.randomfood.addFood.AddFoodScreen
import com.slobozhaninova.randomfood.addFood.AddFoodViewModel
import com.slobozhaninova.randomfood.listFood.FoodListScreen
import com.slobozhaninova.randomfood.listFood.FoodListViewModel
import com.slobozhaninova.randomfood.randomFood.RandomFoodScreen
import com.slobozhaninova.randomfood.randomFood.RandomViewModel
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
            val viewModel = hiltViewModel<RandomViewModel>()

            RandomFoodScreen(
                onEditFoodClick = { navController.navigate("list") },
                onAddFoodClick = { navController.navigate("add") },
                randomViewModel = viewModel

            )
        }
        composable("list") {
            val viewModel = hiltViewModel<FoodListViewModel>()
            val listState by viewModel.listState.collectAsState()
            val category by viewModel.categoriesWithAll.collectAsState()
            FoodListScreen(
                listState = listState,
                onBack = { navController.navigate("randomFood") },
                onAddFoodClick = { navController.navigate("add") },
                query = viewModel::query,
                selectedCategory = viewModel::selectedCategory,
                deleteFood = viewModel::deleteFood,
                onAddCategory = {navController.navigate("addCategories")},
                allCategory = category
            )
        }
        composable("add") {
            val viewModel = hiltViewModel<AddFoodViewModel>()
            val addFoodState by viewModel.addFoodState.collectAsState()
            val category by viewModel.stateCategory.collectAsState()
            AddFoodScreen(
                addFoodState = addFoodState,
                onBack = { navController.popBackStack() },
                addFoodName = viewModel::addFoodName,
                addFoodCategory = viewModel::addFoodCategory,
                onAddClick = {
                    viewModel.addFood()
                    navController.navigate("list")
                },
                categoryVM = category
            )
        }
        composable("addCategories") {
            val viewModel = hiltViewModel<AddCategoryViewModel>()
            val categoriesList by viewModel.stateCategory.collectAsState()
            AddCategoryScreen(
                onBackClick = {navController.navigate("list")},
                categoriesList = categoriesList,
                onDeleteClick = viewModel::deleteCategory,
                addCategory = viewModel::addCategory,
                canDeleteCategory = viewModel::canDeleteCategory
            )
        }
    }


}
