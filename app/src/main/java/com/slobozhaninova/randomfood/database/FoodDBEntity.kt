package com.slobozhaninova.randomfood.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class FoodDBEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val name : String,
    val category : String
)