package com.slobozhaninova.randomfood

import com.slobozhaninova.randomfood.database.FoodDBEntity
import com.slobozhaninova.randomfood.database.FoodDao
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@Module
@InstallIn(SingletonComponent::class)
abstract class FoodsRepositoryModule {
    @Binds
    abstract fun bindFoodsRepository(impl: FoodsRepositoryImpl): FoodsRepository
}

interface FoodsRepository {
    fun getAllFoods(): Flow<List<FoodDBEntity>>

    fun getFoodsByCategory(category: String): Flow<List<FoodDBEntity>>
    fun getCategories(): Flow<List<String>>
    suspend fun insertFood(food: FoodDBEntity)

    suspend fun deleteFood(food: FoodDBEntity)
}

class FoodsRepositoryImpl @Inject constructor(
    private val foodDao: FoodDao
) : FoodsRepository {
    override fun getAllFoods(): Flow<List<FoodDBEntity>> = foodDao.getAllFoods()

    override fun getFoodsByCategory(category: String): Flow<List<FoodDBEntity>> =
        foodDao.getFoodByCategory(category)

    override fun getCategories(): Flow<List<String>> = foodDao.getCategories()

    override suspend fun insertFood(food: FoodDBEntity) = foodDao.insert(food)

    override suspend fun deleteFood(food: FoodDBEntity) = foodDao.delete(food)

}