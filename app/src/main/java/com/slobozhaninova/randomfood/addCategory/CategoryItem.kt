package com.slobozhaninova.randomfood.addCategory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slobozhaninova.randomfood.CategoryVM

@Preview
@Composable
fun CategoryItem(
    category: CategoryVM = CategoryVM("гарнир"),
    onDeleteClick: () -> Unit = {},
    canDeleteCategory: Boolean = false
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = category.categoryName,
            )

            if (canDeleteCategory) {
                IconButton(
                    onClick = onDeleteClick,
                    enabled = true
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                }
            } else {
                IconButton(
                    onClick = onDeleteClick,
                    enabled = false
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                }
            }
        }
    }
}


