package com.slobozhaninova.randomfood.addFood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slobozhaninova.randomfood.FoodsRepository
import com.slobozhaninova.randomfood.database.FoodDBEntity
import com.slobozhaninova.randomfood.listFood.allCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.category

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

    fun setAddFoodName(name: String) {
        _addFoodState.value = _addFoodState.value.copy(
            name = name
        )
    }

    fun setAddFoodCategory(category: String) {
        _addFoodState.value = _addFoodState.value.copy(
            category = category
        )
    }

    fun addFood() {
        val state = _addFoodState.value
        val name = state.name.trim()
        val category = state.category.trim()

        viewModelScope.launch {
            repository.insertFood(FoodDBEntity(name = name, category = category))
            _addFoodState.value = AddFoodState(
                name = "",
                category = allCategory.first()
            )
        }
    }


}