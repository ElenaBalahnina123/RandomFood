package com.slobozhaninova.randomfood.addCategory

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.slobozhaninova.randomfood.CategoryVM


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AddCategoryScreen(
    onBackClick: () -> Unit = {},
    categoriesList: List<CategoryVM> = listOf(CategoryVM("гарнир"), CategoryVM("Супы")),
    onDeleteClick: (CategoryVM) -> Unit = {},
    addCategory: (String) -> Unit = { "" },
    canDeleteCategory: (CategoryVM) -> Boolean = { true }
) {

    var showAlertDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Управление категориями") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { showAlertDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxWidth()) {
            LazyColumn {
                items(categoriesList) { category ->
                    CategoryItem(
                        category = category,
                        onDeleteClick = { onDeleteClick(category) },
                        canDeleteCategory = canDeleteCategory(category)
                    )
                }
            }
        }

        if (showAlertDialog) {
            AlertDialog(
                onDismissRequest = { !showAlertDialog },
                title = { Text("Добавить категорию") },
                text = {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Название категории") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val trimmedName = newCategoryName.trim()
                            when {
                                trimmedName.isBlank() -> {
                                    Toast.makeText(context, "Категория пустая", Toast.LENGTH_SHORT).show()
                                }

                                categoriesList.any { it.categoryName.equals(trimmedName, ignoreCase = true) } -> {
                                    Toast.makeText(context, "Категория уже существует", Toast.LENGTH_SHORT).show()
                                }

                                else -> {
                                    addCategory(trimmedName)
                                    newCategoryName = ""
                                    showAlertDialog = false
                                    Toast.makeText(context, "Категория добавлена", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    ) {
                        Text("Добавить")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showAlertDialog = false
                            newCategoryName = ""
                        }) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}

