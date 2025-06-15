@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication.second

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.model.Cat

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SingleView(
    onClickBack: () -> Unit,
    onWikiClick: (String) -> Unit,
    state: SecondScreenState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preview") },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFB05F))
            )
        },
        content = {
            when {
                state.loading -> SingleScreenStatus("Loading...")
                state.error != null -> SingleScreenStatus(state.error, isError = true)
                state.cat != null -> CatDetailsCard(cat = state.cat, it.calculateTopPadding(), onWikiClick)
                else -> SingleScreenStatus("Cat not found", isError = true)
            }
        }
    )
}

@Composable
fun SingleScreenStatus(message: String, isError: Boolean = false) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (message == "Loading...") {
            CircularProgressIndicator()
        } else {
            Text(text = message, color = if (isError) Color.Red else Color.Black, fontSize = 25.sp)
        }
    }
}

@Composable
fun CatDetailsCard(cat: Cat, calculateTopPadding: Dp, onWikiClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .padding(top = calculateTopPadding)
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFB05F))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
            val catName = if (!cat.alternativeName.isNullOrEmpty())
                "${cat.name} (${cat.alternativeName})" else cat.name

            Text(
                text = catName,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            cat.image?.url?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier.size(220.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.catalist)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Text(
                text = cat.description,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Justify,
                fontSize = 16.sp,
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            DisplayAllTemperaments(cat)

            Spacer(modifier = Modifier.height(12.dp))
            DisplayOther(cat)

            Spacer(modifier = Modifier.height(12.dp))
            DisplayRating(cat)

            Spacer(modifier = Modifier.height(16.dp))

            cat.wikipediaUrl?.let {
                AssistChip(
                    onClick = { onWikiClick(it) },
                    label = { Text("Read more on Wikipedia!") },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun DisplayOther(cat: Cat) {
    Column(modifier = Modifier.padding(10.dp)) {
        Text("Origin: ${cat.origin}")
        Text("Life span: ${cat.lifeSpan}")
        Text("Weight: ${cat.weight.metric}")
        Text("Rare: ${if (cat.rare > 0) "Yes" else "No"}")
    }
}

@Composable
fun DisplayRating(cat: Cat) {
    Column(modifier = Modifier.padding(10.dp)) {
        Text("Adaptability")
        RatingBar(cat.adaptability)
        Text("Child friendly")
        RatingBar(cat.childFriendly)
        Text("Dog friendly")
        RatingBar(cat.dogFriendly)
        Text("Energy level")
        RatingBar(cat.energyLevel)
        Text("Intelligence")
        RatingBar(cat.intelligence)
    }
}

@Composable
fun DisplayAllTemperaments(cat: Cat) {
    val list = cat.allTemperaments()
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        list.forEach { tmp ->
            Text(
                text = tmp,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun RatingBar(rating: Int = 0, stars: Int = 5, starsColor: Color = Color(0xFFED760E)) {
    val filledStars = if (rating != 0) rating else 1
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        repeat(filledStars) {
            Icon(Icons.Outlined.Star, contentDescription = null, tint = starsColor)
        }
        repeat(stars - filledStars) {
            Icon(Icons.Outlined.Star, contentDescription = null, tint = Color.LightGray)
        }
    }
}
