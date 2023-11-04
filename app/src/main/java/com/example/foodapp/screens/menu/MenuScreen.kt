package com.example.foodapp.screens.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.AsyncImage
import com.example.foodapp.R
import com.example.foodapp.ui.theme.DividerColor
import com.example.foodapp.utils.Constants.CONTENT_DESCRIPTION
import com.example.foodapp.utils.InternetConnection
import com.example.foodapp.utils.isInternetAvailable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun MenuScreen() {

    val context = LocalContext.current
    val viewModel: MenuViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CitiesDropDownMenu(
                            uiState.value.selectedCity,
                            uiState.value.cityList,
                            setSelectedCity = viewModel::setCity
                        )
                        AsyncImage(
                            modifier = Modifier.size(24.dp),
                            model = R.drawable.ic_qr_code,
                            contentDescription = CONTENT_DESCRIPTION
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { contentPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            state = listState
        ) {
            item() {
                LazyRow(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.value.banners) { item ->
                        BannerItem(item)
                    }
                }
            }
            stickyHeader {
                LazyRow(
                    modifier = Modifier
                        .background(
                            color = (MaterialTheme.colorScheme.background)
                        )
                        .padding(bottom = 16.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.value.categories) { item ->
                        CategoryCard(
                            item,
                            cardClick = {
                                viewModel.selectCategory(
                                    it,
                                    if (isInternetAvailable(context)) InternetConnection.CONNECTED
                                    else InternetConnection.NOT_CONNECTED
                                )
                            },
                        )
                    }
                }
            }
            val list = uiState.value.recipes
            if (list.isNotEmpty())
                itemsIndexed(uiState.value.recipes) { index, item ->
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 8.dp),
                        thickness = 1.dp,
                        color = DividerColor
                    )
                    RecipeItemCard(item)
                    if (index == uiState.value.recipes.size - 1)
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            thickness = 1.dp,
                            color = DividerColor
                        )
                }
            else {
                item {
                    Text(
                        text = ""
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.eventsFlow.collectLatest { value ->
            when (value) {
                is MenuScreenEvent.ScrollToFirstItem -> {
                    coroutineScope.launch {
                        listState.animateScrollToItem(index = 0)
                    }
                }
            }
        }
    }

    val lifeCycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.setInternetState(
                    if (isInternetAvailable(context)) InternetConnection.CONNECTED
                    else InternetConnection.NOT_CONNECTED
                )
                viewModel.getCategories()
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun CitiesDropDownMenu(
    selectedCity: String,
    citiesList: List<String>,
    setSelectedCity: (city: String) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .clickable {
                expanded.value = !expanded.value
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier
                .padding(top = 15.dp, bottom = 19.dp),
            text = selectedCity,
            fontSize = 16.sp,
            lineHeight = 18.75.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Medium
        )
        AsyncImage(
            modifier = Modifier.size(24.dp),
            model = R.drawable.ic_drop_down_arrow,
            contentDescription = CONTENT_DESCRIPTION
        )
        DropdownMenu(modifier = Modifier
            .background(Color.White),
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }) {
            citiesList.forEach { city ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = city,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp
                        )
                    },
                    onClick = {
                        setSelectedCity(city)
                        expanded.value = false
                    }
                )
            }
        }
    }
}

@Composable
fun BannerItem(banner: Int) {
    Card(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .size(width = 312.dp, height = 112.dp),
        shape = MaterialTheme.shapes.small
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = banner,
            contentDescription = CONTENT_DESCRIPTION,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun CategoryCard(
    item: Category,
    cardClick: (name: String) -> Unit
) {
    Card(
        modifier = Modifier
            .clickable(
                onClick = { cardClick(item.name) }
            ),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Text(
            modifier = Modifier
                .background(
                    color = if (item.selected) MaterialTheme.colorScheme.tertiary else
                        MaterialTheme.colorScheme.secondary
                )
                .padding(vertical = 8.dp, horizontal = 18.dp),
            text = item.name,
            color = if (item.selected) MaterialTheme.colorScheme.onTertiary else
                MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
fun RecipeItemCard(item: Recipe) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(135.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Card(
                modifier = Modifier
                    .size(135.dp),
                shape = MaterialTheme.shapes.small
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    model = item.image,
                    error = painterResource(id = R.drawable.ic_base_meal),
                    contentDescription = CONTENT_DESCRIPTION,
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.75.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = item.description,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 16.71.sp,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    CartBtn(item.price)
                }
            }
        }
    }
}

@Composable
fun CartBtn(price: String) {
    Card(
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.tertiary),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 18.dp),
            text = stringResource(id = R.string.price_label, price),
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 15.51.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onTertiary
        )
    }
}