package com.slobozhaninova.randomfood.listFood

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slobozhaninova.randomfood.CategoryVM
import com.slobozhaninova.randomfood.FoodVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListScreen(
    selectedTabIndex : Int,
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                   MinimalDropdownMenu(
                       addFood = onAddFoodClick,
                       addCategory = onAddCategory
                   )
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

            if(allCategory.isNotEmpty()) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth(),
                    edgePadding = 0.dp
                ) {
                    allCategory.forEachIndexed { index, category ->
                        Tab(
                            text = {
                                Text(
                                    text = category.categoryName,
                                    maxLines = 1,
                                )
                            },
                            selected = selectedTabIndex == index,
                            onClick = {
                                selectedCategory(category)
                            }
                        )
                    }
                }
            } else {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                )
            }
            LazyColumn {
                items(listState.filteredFoods,  key = { it.id }) { food ->
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


@Composable
fun MinimalDropdownMenu(
    addFood : () -> Unit,
    addCategory : () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.Add, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Добавить блюдо") },
                onClick = addFood
            )
            DropdownMenuItem(
                text = { Text("Добавить категорию") },
                onClick = addCategory
            )
        }
    }
}