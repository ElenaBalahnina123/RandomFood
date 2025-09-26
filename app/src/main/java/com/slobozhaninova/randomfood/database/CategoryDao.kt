package com.slobozhaninova.randomfood.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY categoryName")
    fun getAllCategories(): Flow<List<CategoryDBEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: CategoryDBEntity)

    @Delete
    suspend fun delete(category: CategoryDBEntity)
}