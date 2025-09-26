package com.slobozhaninova.randomfood.addFood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slobozhaninova.randomfood.FoodsRepository
import com.slobozhaninova.randomfood.addCategory.CategoryVM
import com.slobozhaninova.randomfood.database.CategoryDBEntity
import com.slobozhaninova.randomfood.database.FoodDBEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddFoodState(
    val name: String = "",
    val category: String = "",
)

@HiltViewModel
class AddFoodViewModel @Inject constructor(
    private val repository: FoodsRepository
) : ViewModel() {

    private val _addFoodState = MutableStateFlow(AddFoodState())
    val addFoodState = _addFoodState.asStateFlow()

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
                repository.initializeDefaultCategories()
            }
        }
    }

    fun toCategoryVM(entity: CategoryDBEntity): CategoryVM {
        return CategoryVM(
            categoryName = entity.categoryName,
            addByUser = entity.addByUser
        )
    }

    fun addFoodName(name: String) {
        _addFoodState.value = _addFoodState.value.copy(
            name = name
        )
    }
    fun addFoodCategory(category: String) {
        _addFoodState.value = _addFoodState.value.copy(
            category = category
        )
    }

    fun addFood() {
        viewModelScope.launch {
            repository.insertFood(
                FoodDBEntity(
                    name = _addFoodState.value.name,
                    category = _addFoodState.value.category
                )
            )
        }
    }

}