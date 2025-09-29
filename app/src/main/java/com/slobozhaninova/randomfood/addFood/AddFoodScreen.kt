package com.slobozhaninova.randomfood.addFood

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(
    addFoodState: AddFoodState,
    onBack: () -> Unit,
    onAddClick : ()-> Unit,
    addFoodCategory : (String) -> Unit,
    addFoodName : (String) -> Unit,
    categoryVM: List<CategoryVM>
) {
    var expanded by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Добавить блюдо") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.Companion
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = addFoodState.name,
                onValueChange = { addFoodName(it) },
                label = { Text("Название блюда") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = addFoodState.category,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Категория") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categoryVM.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.categoryName) },
                            onClick = {
                                addFoodCategory(category.categoryName)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = addFoodState.name.isNotBlank() && addFoodState.category.isNotBlank()
            ) {
                Text("Добавить блюдо")
            }
        }
    }
}