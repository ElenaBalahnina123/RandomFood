package com.slobozhaninova.randomfood.listFood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slobozhaninova.randomfood.FoodsRepository
import com.slobozhaninova.randomfood.database.FoodDBEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

val allCategory = listOf(
    "Все",
    "Салаты",
    "Супы",
    "Гарниры",
    "Главное блюдо",
    "Десерты"
)

data class FoodVM(
    val id: Long = 0L,
    val name: String,
    val category: String
)

@HiltViewModel
class FoodListViewModel @Inject constructor(
    private val repository: FoodsRepository
) : ViewModel() {

    val mutableState = MutableStateFlow<List<FoodVM>>(emptyList())
    val allFood: StateFlow<List<FoodVM>> = mutableState.asStateFlow()

    private val _listState = MutableStateFlow(FoodListState())
    val listState: StateFlow<FoodListState> = _listState.asStateFlow()

    init {
        loadAllFoods()

    }

    private fun loadAllFoods() {
        viewModelScope.launch {
            repository.getAllFoods().map { entities ->
                entities.map {
                    toFoodVM(it)
                }
            }.collect { foods ->
                mutableState.value = foods
                generateRandomFood()
                updateListState()
            }
        }
    }

    private val _randomFood = MutableStateFlow<FoodVM?>(null)
    val randomFood: StateFlow<FoodVM?> = _randomFood.asStateFlow()

    fun generateRandomFood() {
        _randomFood.value = if (mutableState.value.isNotEmpty()) {
            mutableState.value.random()
        } else {
            null
        }
    }

    fun selectedCategory(category: String) {
        _listState.value = _listState.value.copy(selectedCategory = category)
        updateListState()
    }

    fun query(query: String) {
        _listState.value = _listState.value.copy(searchQuery = query)
        updateListState()
    }

    fun deleteFood(food: FoodVM) {
        viewModelScope.launch {
            repository.deleteFood(toEntity(foodVm = FoodVM(food.id, food.name, food.category)))
        }
    }

    private fun updateListState() {
        val category = _listState.value.selectedCategory
        val query = _listState.value.searchQuery.lowercase()

        var filtered = if (category == allCategory.first()) {
            mutableState.value
        } else {
            mutableState.value.filter { it.category == category }
        }

        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.name.lowercase().contains(query) ||
                        it.category.lowercase().contains(query)
            }
        }

        _listState.value = _listState.value.copy(filteredFoods = filtered)
    }
}


fun toEntity(foodVm: FoodVM): FoodDBEntity {
    return FoodDBEntity(
        id = foodVm.id,
        name = foodVm.name,
        category = foodVm.category
    )
}

fun toFoodVM(entity: FoodDBEntity): FoodVM {
    return FoodVM(
        id = entity.id,
        name = entity.name,
        category = entity.category
    )
}

data class FoodListState(
    val selectedCategory: String = "Все",
    val searchQuery: String = "",
    val filteredFoods: List<FoodVM> = emptyList()
)