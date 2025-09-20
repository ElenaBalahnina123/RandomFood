package com.slobozhaninova.randomfood.listFood

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class FoodData(
    val food: String,
    val category : String
)

val tabTitles = listOf("Все", "Супы", "Салаты", "Гарниры", "Основные блюда", "Десерты")

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListScreen(
    foodData: FoodData = FoodData("котлета", category = "основное блюдо")
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Список блюд") }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it))
        {
            var selectedIndex by remember { mutableIntStateOf(0) }
            ScrollableTabRow(selectedTabIndex = selectedIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedIndex) {
                0 -> Text("Содержимое вкладки 1")
                1 -> Text("Содержимое вкладки 2")
                2 -> Text("Содержимое вкладки 3")
                3 -> Text("Содержимое вкладки 4")
                4 -> Text("Содержимое вкладки 5")
                5 -> Text("Содержимое вкладки 6")
            }
        }
    }
    }

    @Preview
    @Composable
    fun FoodItem(
        foodData: FoodData = FoodData("котлета", "основное блюдо")
    ) {
        Text(
            text = foodData.food,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 16.dp)
        )
    }