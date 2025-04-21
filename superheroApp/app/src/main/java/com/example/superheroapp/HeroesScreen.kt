package com.example.superheroapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.superheroapp.model.Hero
import com.example.superheroapp.model.HeroesRepository.heroes
import com.example.superheroapp.ui.theme.SuperheroAppTheme

@Composable
fun HeroesItem(hero: Hero , modifier: Modifier = Modifier){
    Card (elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) ,
        modifier = modifier
            
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .sizeIn(minHeight = 72.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(id = hero.nameRes) , style = MaterialTheme.typography.displaySmall )
                Text(text = stringResource(id = hero.descriptionRes) , style = MaterialTheme.typography.bodyLarge )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box (modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .size(72.dp)
            ) {
                Image(
                    painter = painterResource(id = hero.imageRes),
                    contentDescription = stringResource(id = hero.nameRes),
                    alignment = Alignment.TopCenter,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

@Composable
fun ItemList(modifier: Modifier = Modifier){
    LazyColumn (modifier = modifier){
        items(heroes) { item ->
            HeroesItem(hero = item , modifier = Modifier.padding(top = 8.dp , bottom = 8.dp , start = 16.dp , end = 16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun itemPreview() {
    SuperheroAppTheme (darkTheme = false){
        ItemList()
    }
}