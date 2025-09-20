package com.slobozhaninova.randomfood.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Query("SELECT * FROM foods")
    fun getAllFoods(): Flow<List<FoodDBEntity>>

    @Query("SELECT * FROM foods WHERE category = :category")
    fun getFoodByCategory(category: String): Flow<List<FoodDBEntity>>

    @Query("SELECT DISTINCT category FROM foods")
    fun getCategories(): Flow<List<String>>

    @Insert
    suspend fun insert(foodDBEntity: FoodDBEntity)

    @Delete
    suspend fun delete(food: FoodDBEntity)
}