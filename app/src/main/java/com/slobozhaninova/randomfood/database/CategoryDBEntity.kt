package com.slobozhaninova.randomfood.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "categories")
data class CategoryDBEntity (
    @PrimaryKey
    val categoryName : String,
    val addByUser : Boolean
)