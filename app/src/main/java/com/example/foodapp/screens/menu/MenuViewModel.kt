package com.example.foodapp.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.db.CategoryEntity
import com.example.foodapp.data.db.RecipeEntity
import com.example.foodapp.data.repositories.CategoryRepository
import com.example.foodapp.data.repositories.RecipesRepository
import com.example.foodapp.utils.Constants.PREVIEW_PATH
import com.example.foodapp.utils.InternetConnection
import com.example.foodapp.utils.coroutineExceptionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val recipeRepo: RecipesRepository,
    private val categoryRepo: CategoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUIState())
    val uiState: StateFlow<MenuUIState> = _uiState

    private val eventChannel = Channel<MenuScreenEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    var getRecipeJob: Job? = null

    fun setInternetState(value: InternetConnection) {
        _uiState.value = uiState.value.copy(
            internetState = value
        )
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.value.internetState == InternetConnection.CONNECTED) {
                categoryRepo.cleanCategories()
                categoryRepo.getCategories().let {
                    val categories = uiState.value.categories.toMutableList()
                    it.categories.map {
                        categories.add(
                            Category(
                                name = it.name
                            )
                        )
                        categoryRepo.insertCategory(
                            CategoryEntity(
                                title = it.name
                            )
                        )
                        _uiState.value = uiState.value.copy(
                            categories = categories
                        )
                    }
                }
            } else {
                _uiState.value = uiState.value.copy(
                    categories = categoryRepo.getCategoriesFromDao().map {
                        Category(
                            name = it.title
                        )
                    }
                )
            }
            if (uiState.value.categories.isNotEmpty())
                selectCategory(uiState.value.categories[0].name, uiState.value.internetState)
        }
    }

    fun selectCategory(
        category: String,
        connection: InternetConnection
    ) {

        if (uiState.value.categories.find { it.selected }?.name == category) {
            sendEvent(MenuScreenEvent.ScrollToFirstItem())
            return
        }

        val categories = uiState.value.categories.toMutableList()

        val currentSelected = categories.find { it.selected }
        val indexSelected = categories.indexOf(currentSelected)

        val newSelected = categories.find { it.name == category }
        val newIndexSelected = categories.indexOf(newSelected)

        categories.apply {
            currentSelected?.let {
                removeAt(indexSelected)
                add(
                    indexSelected,
                    Category(
                        name = currentSelected.name,
                        selected = false
                    )
                )
            }
            removeAt(newIndexSelected)
            add(
                newIndexSelected,
                Category(
                    name = newSelected?.name ?: "",
                    selected = true
                )
            )
        }

        _uiState.value = uiState.value.copy(
            categories = categories,
            recipes = listOf(),
            internetState = connection
        )

        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.value.internetState == InternetConnection.CONNECTED) {
                getRecipeJob?.cancel()
                _uiState.value = uiState.value.copy(
                    recipes = listOf()
                )
                recipeRepo.getRecipesByCategory(category).let {
                    getRecipeJob =
                        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                            it.ids.forEach {
                                recipeRepo.getRecipeById(it.id).let { recInfo ->
                                    val list = uiState.value.recipes.toMutableList()
                                    Recipe(
                                        image = recInfo.meals.first().image + PREVIEW_PATH,
                                        name = recInfo.meals.first().name,
                                        category = recInfo.meals.first().category,
                                        description = recInfo.meals.first().description
                                    ).let {
                                        list.add(it)
                                        recipeRepo.insertRecipe(
                                            RecipeEntity(
                                                name = it.name,
                                                category = it.category,
                                                image = it.image,
                                                description = it.description,
                                                price = it.price
                                            )
                                        )
                                    }
                                    _uiState.value = uiState.value.copy(
                                        recipes = list
                                    )
                                }
                            }
                        }
                }
            } else {
                _uiState.value = uiState.value.copy(
                    recipes = listOf()
                )
                _uiState.value = uiState.value.copy(
                    recipes = recipeRepo.getRecipesDao(category = category).map {
                        Recipe(
                            image = it.image,
                            name = it.name,
                            category = it.category,
                            description = it.description
                        )
                    }
                )
            }
        }
    }

    fun setCity(city: String) {
        _uiState.value = uiState.value.copy(
            selectedCity = city
        )
    }

    private fun sendEvent(event: MenuScreenEvent) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }
}
