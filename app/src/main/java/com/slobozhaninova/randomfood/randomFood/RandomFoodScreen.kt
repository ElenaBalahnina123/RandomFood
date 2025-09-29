package com.slobozhaninova.randomfood.randomFood

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomFoodScreen(
    randomViewModel: RandomViewModel,
    onEditFoodClick: () -> Unit,
    onAddFoodClick: () -> Unit
) {
    val randomFood by randomViewModel.randomFood.collectAsState()
    val allFoods by randomViewModel.allFoods.collectAsState()
    val isLoading by randomViewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Рандомайзер блюд") },
                actions = {
                    IconButton(onClick = onAddFoodClick) {
                        Icon(Icons.Default.Add, contentDescription = "Добавить блюдо")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = onEditFoodClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Редактировать список блюд")
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else if (randomFood != null) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = randomFood!!.name,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = randomFood!!.category
                    )
                }
            } else {
                Text(
                    text = "Нет блюд для выбора",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 32.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { randomViewModel.generateRandomFood() },
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                enabled = allFoods.isNotEmpty() && !isLoading
            ) {
                Text("Заново", textAlign = TextAlign.Center)
            }

            if (allFoods.isEmpty() && !isLoading) {
                Text(
                    text = "Добавьте блюда",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
