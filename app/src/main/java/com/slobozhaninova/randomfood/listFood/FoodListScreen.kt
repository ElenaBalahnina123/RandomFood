package com.slobozhaninova.randomfood.listFood

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slobozhaninova.randomfood.CategoryVM
import com.slobozhaninova.randomfood.FoodVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListScreen(
    listState: FoodListState,
    onBack: () -> Unit,
    onAddFoodClick: () -> Unit,
    query : (String) -> Unit,
    selectedCategory : (CategoryVM) -> Unit,
    deleteFood : (FoodVM) -> Unit,
    onAddCategory : () -> Unit,
    allCategory : List<CategoryVM>
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Список блюд") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = onAddFoodClick) {
                        Icon(Icons.Default.Create, contentDescription = null)
                    }
                    IconButton(onClick = onAddCategory) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            OutlinedTextField(
                value = listState.searchQuery,
                onValueChange = { query(it) },
                label = { Text("Поиск блюд") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                trailingIcon = {
                    if (listState.searchQuery.isNotBlank()) {
                        IconButton(onClick = { query("") }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                }
            )

            if (allCategory.isNotEmpty()) {
                val selectedIndex = allCategory.indexOfFirst {it.categoryName == listState.selectedCategory.categoryName }
                    .coerceAtLeast(0)

                ScrollableTabRow(selectedTabIndex = selectedIndex) {
                    allCategory.forEach { category ->
                        Tab(
                            selected = listState.selectedCategory.categoryName == category.categoryName,
                            onClick = { selectedCategory(category) },
                            text = { Text(category.categoryName) }
                        )
                    }
                }
            }
            LazyColumn {
                items(listState.filteredFoods) { food ->
                    FoodItem(
                        foodVM = food,
                        onDelete = { deleteFood(food) }
                    )
                }

                if (listState.filteredFoods.isEmpty()) {
                    item {
                        Text(
                            text = if (listState.searchQuery.isNotBlank()) {
                                "Ничего не найдено"
                            } else if (allCategory.isEmpty()) {
                                "Нет категорий"
                            } else {
                                "Нет блюд в этой категории"
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally),
                        )
                    }
                }
            }
        }
    }
}