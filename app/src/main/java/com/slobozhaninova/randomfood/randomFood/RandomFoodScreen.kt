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
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slobozhaninova.randomfood.FoodVM
import com.slobozhaninova.randomfood.ui.theme.RandomFoodTheme

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RandomFoodScreen(
    onEditFoodClick: () -> Unit = {},
    onAddFoodClick: () -> Unit = {},
    randomFood : FoodVM? = FoodVM(0L, "пюре с аоаоаоаоаоао аоаоао", "гарниры"),
    allFoods : List<FoodVM> = listOf(FoodVM(0L, "пюре", "гарниры"),FoodVM(0L, "пюре", "гарниры")),
    generateRandomFood : () -> Unit = {}
) {
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

                if (randomFood != null) {
                        Text(
                            text = randomFood.name,
                            fontSize = 30.sp,
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                else {
                    Text(
                        text = "...",
                        fontSize = 30.sp,
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }


                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = generateRandomFood,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape),
                    enabled = allFoods.isNotEmpty()
                ) {
                    Text("Заново", textAlign = TextAlign.Center,
                        fontSize = 20.sp)
                }

                if (allFoods.isEmpty()) {
                    Text(
                        text = "Добавьте блюда",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
