@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.myapplication.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.model.Cat

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    onClickHead: (String) -> Unit,
    eventPublisher: (MainScreenEvent) -> Unit,
    state: MainScreenState
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            Column {
                MainTopBar()
                MainSearchBar(
                    query = state.query,
                    onQueryChange = { eventPublisher(MainScreenEvent.FilterList(it)) },
                    onSearch = { keyboard?.hide() }
                )
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFCE4EC))
                    .padding(top = it.calculateTopPadding())
            ) {
                LoadingOrErrorSection(state = state)
                if (!state.loading && state.error == null) {
                    CatListSection(catList = state.listCats, onClickHead = onClickHead)
                }
            }
        }
    )
}

@Composable
fun MainTopBar() {
    TopAppBar(
        title = { Text(text = "Home", fontSize = 22.sp) },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.catalist),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFF80AB),
            scrolledContainerColor = Color(0xFFFF80AB)
        )
    )
}

@Composable
fun MainSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        placeholder = { Text(text = "Search here..") },
        onSearch = { onSearch() },
        active = false,
        onActiveChange = {},
        modifier = Modifier.padding(start = 15.dp)
    ) {}
}

@Composable
fun LoadingOrErrorSection(state: MainScreenState) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            state.error != null -> Text(
                text = state.error,
                color = Color.Red,
                fontSize = 22.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun CatListSection(catList: List<Cat>, onClickHead: (String) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(catList) { cat ->
            MakeCatCard(cat = cat, onClickHead = { onClickHead(cat.id) })
        }
    }
}

@Composable
fun MakeCatCard(cat: Cat, onClickHead: (String) -> Unit) {
    val catName = if (!cat.alternativeName.isNullOrEmpty())
        "${cat.name} (${cat.alternativeName})"
    else cat.name

    Card(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC1E3))
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = catName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (cat.description.length > 250) cat.description.take(250) + "..." else cat.description,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Justify
            )

            Spacer(modifier = Modifier.height(10.dp))

            DisplayTemperaments(cat = cat)

            Spacer(modifier = Modifier.height(10.dp))

            AssistChip(
                onClick = { onClickHead(cat.id) },
                label = { Text("Read more") },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.catalist),
                        contentDescription = null
                    )
                }
            )
        }
    }
}

fun findThreeRandom(cat: Cat): List<String> {
    val elements = cat.temperament.replace(" ", "").split(",")
    return if (elements.size >= 3) {
        listOf(elements[0], elements.last(), elements[elements.size / 2])
    } else elements
}

@Composable
fun DisplayTemperaments(cat: Cat) {
    val list = findThreeRandom(cat)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        list.forEach { temp ->
            Text(
                text = temp,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .background(Color.White, RoundedCornerShape(30.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}
