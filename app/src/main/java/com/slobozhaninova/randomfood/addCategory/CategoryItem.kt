package com.slobozhaninova.randomfood.addCategory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun CategoryItem(
    category: CategoryVM = CategoryVM("гарнир"),
    onDeleteClick: () -> Unit = {},
    canDeleteCategory : Boolean = false
) {
    Row (modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = category.categoryName
        )

        if (canDeleteCategory) {
            IconButton(
                onClick = onDeleteClick,
                enabled = true
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить категорию")
            }
        }
    }
}

