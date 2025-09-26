package com.slobozhaninova.randomfood.addCategory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slobozhaninova.randomfood.FoodsRepository
import com.slobozhaninova.randomfood.database.CategoryDBEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryVM(
    val categoryName: String = "",
    val addByUser : Boolean = true
)


@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val repository: FoodsRepository
) : ViewModel() {

    val mutableStateCategory = MutableStateFlow<List<CategoryVM>>(emptyList())
    val stateCategory = mutableStateCategory.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getAllCategories().collect { categories ->
                mutableStateCategory.value = categories.map { toCategoryVM(it) }
            }
            if (mutableStateCategory.value.isEmpty()) {
                initializeDefaultCategories()
            }
        }
    }

    private fun initializeDefaultCategories() {
               viewModelScope.launch {
                   repository.initializeDefaultCategories()

        }
    }

    fun addCategory(categoryName: String) {
        viewModelScope.launch {
                val trimmedName = categoryName.trim()
                if (trimmedName.isNotBlank()) {
                    val existingCategory = mutableStateCategory.value.find {
                        it.categoryName.equals(trimmedName, ignoreCase = true)
                    }
                    if (existingCategory == null) {
                        repository.insertCategory(CategoryDBEntity(trimmedName, addByUser = true))
                    } else {
                         Log.d("AddCategory","Категория \"$trimmedName\" уже существует")
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
                repository.deleteCategory(toEntity(category))
            }
        }
    }

    fun toCategoryVM(entity: CategoryDBEntity): CategoryVM {
        return CategoryVM(
            categoryName = entity.categoryName,
            addByUser = entity.addByUser
        )
    }

    fun toEntity(category: CategoryVM) : CategoryDBEntity {
        return CategoryDBEntity(
            categoryName = category.categoryName,
            addByUser = category.addByUser
        )
    }

}
