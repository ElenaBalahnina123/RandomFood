package com.slobozhaninova.randomfood.listFood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slobozhaninova.randomfood.CategoryUtils
import com.slobozhaninova.randomfood.CategoryVM
import com.slobozhaninova.randomfood.FoodVM
import com.slobozhaninova.randomfood.FoodsRepository
import com.slobozhaninova.randomfood.toCategoryVM
import com.slobozhaninova.randomfood.toEntityFood
import com.slobozhaninova.randomfood.toFoodVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodListViewModel @Inject constructor(
    private val repository: FoodsRepository
) : ViewModel() {

    private val _stateCategory = MutableStateFlow<List<CategoryVM>>(emptyList())
    val stateCategory: StateFlow<List<CategoryVM>> = _stateCategory.asStateFlow()

    private val _allFoods = MutableStateFlow<List<FoodVM>>(emptyList())
    val allFoods: StateFlow<List<FoodVM>> = _allFoods.asStateFlow()

    private val _listState = MutableStateFlow(FoodListState())
    val listState: StateFlow<FoodListState> = _listState.asStateFlow()

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    init {
        loadAllFoods()
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getAllCategories().collect { categories ->
                val categoriesWithAll = listOf(
                    CategoryVM(categoryName = "Все", addByUser = false)
                ) + categories.map { toCategoryVM(it) }

                _stateCategory.value = categoriesWithAll

                // Автоматически выбираем первую категорию при первой загрузке
                if (categoriesWithAll.isNotEmpty() && _listState.value.selectedCategory.categoryName.isEmpty()) {
                    val firstCategory = categoriesWithAll.first()
                    _listState.value = _listState.value.copy(selectedCategory = firstCategory)
                    _selectedTabIndex.value = 0
                    updateListState()
                }
            }
        }
    }

    private fun loadAllFoods() {
        viewModelScope.launch {
            repository.getAllFoods()
                .map { entities -> entities.map { toFoodVM(it) } }
                .collect { foods ->
                    _allFoods.value = foods
                    updateListState()
                }
        }
    }

    private val _randomFood = MutableStateFlow<FoodVM?>(null)
    val randomFood: StateFlow<FoodVM?> = _randomFood.asStateFlow()

    fun generateRandomFood() {
        val foods = getCurrentFoods()
        _randomFood.value = if (foods.isNotEmpty()) {
            foods.random()
        } else {
            null
        }
    }

    fun generateRandomFoodFromAll() {
        val foods = _allFoods.value
        _randomFood.value = if (foods.isNotEmpty()) {
            foods.random()
        } else {
            null
        }
    }

    fun selectedCategory(category: CategoryVM) {
        _listState.value = _listState.value.copy(selectedCategory = category)

        // Обновляем индекс выбранной вкладки
        val index = _stateCategory.value.indexOfFirst { it.categoryName == category.categoryName }
        if (index >= 0) {
            _selectedTabIndex.value = index
        }

        updateListState()
    }

    fun selectCategoryByIndex(index: Int) {
        val categories = _stateCategory.value
        if (index in categories.indices) {
            val category = categories[index]
            _listState.value = _listState.value.copy(selectedCategory = category)
            _selectedTabIndex.value = index
            updateListState()
        }
    }

    fun query(query: String) {
        _listState.value = _listState.value.copy(searchQuery = query)
        updateListState()
    }

    fun deleteFood(food: FoodVM) {
        viewModelScope.launch {
            repository.deleteFood(toEntityFood(food))
            // После удаления обновляем список
            loadAllFoods()
        }
    }

    private fun updateListState() {
        val category = _listState.value.selectedCategory
        val query = _listState.value.searchQuery.lowercase()
        val allFoods = _allFoods.value

        var filtered = if (category.categoryName.isNotEmpty() && category.categoryName != "Все") {
            allFoods.filter { it.category == category.categoryName }
        } else {
            allFoods
        }

        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.name.lowercase().contains(query) ||
                        it.category.lowercase().contains(query)
            }
        }

        _listState.value = _listState.value.copy(filteredFoods = filtered)
    }

    private fun getCurrentFoods(): List<FoodVM> {
        return _listState.value.filteredFoods
    }

    // Функция для сброса фильтров
    fun resetFilters() {
        _listState.value = _listState.value.copy(
            selectedCategory = CategoryVM(categoryName = "Все", addByUser = false),
            searchQuery = ""
        )
        _selectedTabIndex.value = 0
        updateListState()
    }
}

data class FoodListState(
    val selectedCategory: CategoryVM = CategoryVM(categoryName = "Все", addByUser = false),
    val searchQuery: String = "",
    var filteredFoods: List<FoodVM> = emptyList()
)