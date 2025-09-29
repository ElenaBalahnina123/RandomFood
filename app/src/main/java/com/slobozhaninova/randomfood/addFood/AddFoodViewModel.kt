package com.slobozhaninova.randomfood.addFood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slobozhaninova.randomfood.CategoryVM
import com.slobozhaninova.randomfood.CategoryViewModel
import com.slobozhaninova.randomfood.FoodsRepository
import com.slobozhaninova.randomfood.database.FoodDBEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val categoryHandler = CategoryViewModel(repository)

    init {
        categoryHandler.loadCategories()
    }

    val stateCategory: StateFlow<List<CategoryVM>> = categoryHandler.stateCategory

    private val _addFoodState = MutableStateFlow(AddFoodState())
    val addFoodState = _addFoodState.asStateFlow()

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