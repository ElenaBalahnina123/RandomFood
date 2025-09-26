package com.slobozhaninova.randomfood.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Database(entities = [FoodDBEntity::class, CategoryDBEntity::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun categoryDao() : CategoryDao
}

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "food_database")
            .fallbackToDestructiveMigration()
            .build()

    }
    @Provides
    fun provideFoodsDao(db: AppDatabase): FoodDao {
        return db.foodDao()
    }

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao {
        return db.categoryDao()
    }

}

