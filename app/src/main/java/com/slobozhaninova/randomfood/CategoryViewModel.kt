package com.slobozhaninova.randomfood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class CategoryViewModel(
    protected val repository: FoodsRepository
) : ViewModel() {

    val mutableStateCategory = MutableStateFlow<List<CategoryVM>>(emptyList())
    val stateCategory = mutableStateCategory.asStateFlow()

    fun loadCategories() {
        viewModelScope.launch {
            repository.getAllCategories().collect { categories ->
                mutableStateCategory.value = categories.map { toCategoryVM(it) }
            }
            if (mutableStateCategory.value.isEmpty()) {
                repository.initializeDefaultCategories()
            }
        }
    }
}