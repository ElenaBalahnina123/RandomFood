package com.slobozhaninova.randomfood

import android.util.Log
import com.slobozhaninova.randomfood.database.CategoryDBEntity
import com.slobozhaninova.randomfood.database.CategoryDao
import com.slobozhaninova.randomfood.database.FoodDBEntity
import com.slobozhaninova.randomfood.database.FoodDao
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject


@Module
@InstallIn(SingletonComponent::class)
abstract class FoodsRepositoryModule {
    @Binds
    abstract fun bindFoodsRepository(impl: FoodsRepositoryImpl): FoodsRepository
}

interface FoodsRepository {

    fun getAllFoods(): Flow<List<FoodDBEntity>>
    suspend fun insertFood(food: FoodDBEntity)

    suspend fun deleteFood(food: FoodDBEntity)

    fun getAllCategories(): Flow<List<CategoryDBEntity>>

    suspend fun insertCategory(category: CategoryDBEntity)

    suspend fun deleteCategory(category: CategoryDBEntity)

    suspend fun initializeDefaultCategories()
}

class FoodsRepositoryImpl @Inject constructor(
    private val foodDao: FoodDao,
    private val categoryDao: CategoryDao
) : FoodsRepository {

    override fun getAllFoods(): Flow<List<FoodDBEntity>> = foodDao.getAllFoods()

    override suspend fun insertFood(food: FoodDBEntity) = foodDao.insert(food)

    override suspend fun deleteFood(food: FoodDBEntity) = foodDao.delete(food)

    override fun getAllCategories(): Flow<List<CategoryDBEntity>> = categoryDao.getAllCategories()

    override suspend fun insertCategory(category: CategoryDBEntity) = categoryDao.insert(category)

    override suspend fun deleteCategory(category: CategoryDBEntity) = categoryDao.delete(category)

    override suspend fun initializeDefaultCategories() {
        val defaultCategories = listOf(
            CategoryDBEntity("Салаты", addByUser = false),
            CategoryDBEntity("Супы", addByUser = false),
            CategoryDBEntity("Гарниры", addByUser = false),
            CategoryDBEntity("Главное блюдо", addByUser = false),
            CategoryDBEntity("Десерты", addByUser = false)
        )

        val existingCategories = categoryDao.getAllCategories().first()

        defaultCategories.forEach { category ->
            val exists = existingCategories.any { it.categoryName == category.categoryName }
            if (!exists) {
                categoryDao.insert(category)
                Log.d("FoodsRepository", "Added default category: ${category.categoryName}")
            } else {
                Log.d("FoodsRepository", "Category already exists: ${category.categoryName}")
            }
        }
    }
}