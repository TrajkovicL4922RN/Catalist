package com.example.myapplication.second

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SingleView(
    onClickBack: ()->Unit,
    onWikiClick:(wiki: String)->Unit,
    state: SecondScreenState){
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Preview")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onClickBack()
                    }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Menu icon")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(255,176,95)
                )
            )
        },
        content = {

            if(state.loading){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }else if(state.error != null){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = state.error, color = Color.Red, fontSize = 25.sp)
                }
            }else if(state.cat != null){
                MakeCatView(
                    cat = state.cat,
                    it.calculateTopPadding(),
                    onWikiClick
                )
            }else{
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = "cat not found", color = Color.Red, fontSize = 25.sp)
                }
            }
        }
    )
}

@Composable
fun MakeCatView(cat: Cat, calculateTopPadding: Dp, onWikiClick: (wiki: String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .padding(top = calculateTopPadding)
            .verticalScroll(state = rememberScrollState())
            .fillMaxSize(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(255, 176, 95))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            val catName = if (!cat.alternativeName.isNullOrEmpty())
                "${cat.name} (${cat.alternativeName})"
            else
                cat.name

            Text(
                text = catName,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (cat.image?.url != null) {
                AsyncImage(
                    model = cat.image.url,
                    contentDescription = null,
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(12.dp)),
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
    Column (
        modifier = Modifier
            .padding(10.dp)
            .padding(vertical = 5.dp)
    ){
        Text(text = "Origin: "+cat.origin)
        Text(text = "Life span: "+cat.lifeSpan)
        Text(text = "Weight: "+cat.weight.metric)
        Text(text = "Rare: ${if(cat.rare>0) "Yes" else "No"}")
    }
}

@Composable
fun DisplayRating(cat: Cat){
    Column (
        modifier = Modifier
            .padding(10.dp)
    ){
        Text(text = "Adaptability")
        RatingBar(rating = cat.adaptability)
        Text(text = "Child friendly")
        RatingBar(rating = cat.childFriendly)
        Text(text = "Dog friendly")
        RatingBar(rating = cat.dogFriendly)
        Text(text = "Energy level")
        RatingBar(rating = cat.energyLevel)
        Text(text = "Intelligence")
        RatingBar(rating = cat.intelligence)
    }
}

@Composable
fun DisplayAllTemperaments(cat: Cat) {
    val list = cat.allTemperaments()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
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
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int = 0,
    stars: Int = 5,
    starsColor: Color = Color(237, 118, 14)
) {
    val filledStars = if (rating != 0) rating else 1
    Row(modifier = modifier.padding(vertical = 4.dp)) {
        repeat(filledStars) {
            Icon(Icons.Outlined.Star, contentDescription = null, tint = starsColor)
        }
        repeat(stars - filledStars) {
            Icon(Icons.Outlined.Star, contentDescription = null, tint = Color.LightGray)
        }
    }
}
