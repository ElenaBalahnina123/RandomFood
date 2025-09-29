package com.slobozhaninova.randomfood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object CategoryUtils {

    fun loadCategories(
        repository: FoodsRepository,
        scope: CoroutineScope,
        onCategoriesLoaded: (List<CategoryVM>) -> Unit
    ) {
        scope.launch {
            repository.getAllCategories().collect { categories ->
                val categoryVMs = categories.map { toCategoryVM(it) }
                onCategoriesLoaded(categoryVMs)

                // Если категорий нет, инициализируем дефолтные
                if (categoryVMs.isEmpty()) {
                    repository.initializeDefaultCategories()
                }
            }
        }
    }
}