package com.slobozhaninova.randomfood.listFood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slobozhaninova.randomfood.CategoryVM
import com.slobozhaninova.randomfood.CategoryViewModel
import com.slobozhaninova.randomfood.FoodVM
import com.slobozhaninova.randomfood.FoodsRepository
import com.slobozhaninova.randomfood.database.CategoryDBEntity
import com.slobozhaninova.randomfood.database.FoodDBEntity
import com.slobozhaninova.randomfood.toEntityFood
import com.slobozhaninova.randomfood.toFoodVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FoodListViewModel @Inject constructor(
    private val repository: FoodsRepository
) : ViewModel() {

    private val categoryHandler = CategoryViewModel(repository)

    val mutableState = MutableStateFlow<List<FoodVM>>(emptyList())
    val allFood: StateFlow<List<FoodVM>> = mutableState.asStateFlow()

    private val _listState = MutableStateFlow(FoodListState())
    val listState: StateFlow<FoodListState> = _listState.asStateFlow()

    init {
        loadAllFoods()
        categoryHandler.loadCategories()

    }

    private fun loadAllFoods() {
        viewModelScope.launch {
            repository.getAllFoods().map { entities ->
                entities.map {
                    toFoodVM(it)
                }
            }.collect { foods ->
                mutableState.value = foods
                updateListState()
            }
        }
    }

    private val _randomFood = MutableStateFlow<FoodVM?>(null)
    val randomFood: StateFlow<FoodVM?> = _randomFood.asStateFlow()

    fun generateRandomFood() {
        if (mutableState.value.isEmpty()) {
            _randomFood.value = null
            return
        }
        _randomFood.value = if (mutableState.value.isNotEmpty()) {
            mutableState.value.random()
        } else {
            null
        }
    }

    fun selectedCategory(category: CategoryVM) {
        _listState.value = _listState.value.copy(selectedCategory = category)
        updateListState()
    }

    fun query(query: String) {
        _listState.value = _listState.value.copy(searchQuery = query)
        updateListState()
    }

    fun deleteFood(food: FoodVM) {
        viewModelScope.launch {
            repository.deleteFood(toEntityFood(foodVm = FoodVM(food.id, food.name, food.category)))
        }
    }

    private fun updateListState() {
        val category = _listState.value.selectedCategory
        val query = _listState.value.searchQuery.lowercase()

        var filtered = if (category.categoryName.isNotEmpty() && category.categoryName != "Все") {
            mutableState.value.filter { it.category == category.categoryName }
        } else {
            mutableState.value
        }

        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.name.lowercase().contains(query) ||
                        it.category.lowercase().contains(query)
            }
        }

        _listState.value = _listState.value.copy(filteredFoods = filtered)
    }

    val categoriesWithAll: StateFlow<List<CategoryVM>> =
        categoryHandler.stateCategory.map { categories ->
            listOf(CategoryVM(categoryName = "Все", addByUser = false)) + categories
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

}

data class FoodListState(
    val selectedCategory: CategoryVM = CategoryVM(categoryName = "Все", addByUser = false),
    val searchQuery: String = "",
    val filteredFoods: List<FoodVM> = emptyList()
)