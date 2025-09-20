package com.slobozhaninova.randomfood.randomFood

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class RandomFoodData(
    val food : String
)

@Preview
@Composable
fun RandomFoodScreen(
    randomFoodData: RandomFoodData = RandomFoodData("Котлета с пюре"),
    onEditFoodClick : () -> Unit = {},
    onAgainClick : () -> Unit = {}
) {
    Scaffold(
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(it).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = onEditFoodClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Редактировать блюдо")
            }
            Text(
                text = randomFoodData.food,
                modifier = Modifier.padding(vertical = 48.dp),
                fontSize = 24.sp
            )
            Button(
                onClick = onAgainClick,
               modifier = Modifier.size(150.dp).clip(CircleShape)
            ) {
                Text("Заново")
            }
        }
    }
}