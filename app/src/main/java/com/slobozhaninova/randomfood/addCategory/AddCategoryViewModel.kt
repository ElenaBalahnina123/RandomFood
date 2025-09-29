package com.slobozhaninova.randomfood.addCategory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slobozhaninova.randomfood.CategoryVM
import com.slobozhaninova.randomfood.CategoryViewModel
import com.slobozhaninova.randomfood.FoodsRepository
import com.slobozhaninova.randomfood.database.CategoryDBEntity
import com.slobozhaninova.randomfood.toEntityCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val repository: FoodsRepository
) : ViewModel() {


    private val categoryHandler = CategoryViewModel(repository)

    init {
        categoryHandler.loadCategories()
    }

    val stateCategory: StateFlow<List<CategoryVM>> = categoryHandler.stateCategory


    fun addCategory(categoryName: String) {
        viewModelScope.launch {
            val trimmedName = categoryName.trim()
            if (trimmedName.isNotBlank()) {
                val existingCategory = stateCategory.value.find {
                    it.categoryName.equals(trimmedName, ignoreCase = true)
                }
                if (existingCategory == null) {
                    repository.insertCategory(CategoryDBEntity(trimmedName, addByUser = true))
                } else {
                    Log.d("AddCategory", "Категория \"$trimmedName\" уже существует")
                }
            }
        }
    }

    fun canDeleteCategory(category: CategoryVM): Boolean {
        return category.addByUser
    }

    fun deleteCategory(category: CategoryVM) {
        viewModelScope.launch {
            if (category.addByUser) {
                repository.deleteCategory(toEntityCategory(category))
            }
        }
    }

}
