package com.slobozhaninova.randomfood.listFood

import androidx.lifecycle.ViewModel
import com.slobozhaninova.randomfood.FoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FoodVM(
    val id : Long,
    val name : String,
    val category : String
)

@HiltViewModel
class FoodListViewModel(
    private val repository: FoodsRepository
) : ViewModel() {

    val mutableState = MutableStateFlow<List<FoodVM>>(emptyList())
    val stateFlow = mutableState.asStateFlow()

}