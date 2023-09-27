package com.example.newsapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newsapp.HomeViewModel
import com.example.newsapp.R
import com.example.newsapp.models.Articles
import com.example.newsapp.utility.MultiplePreviews
import com.example.newsapp.utility.loadRemoteImage


/**
 * Created by Abhishek Shah on 27 September 2023
 *
 * Thinkitive Technologies Pvt. Ltd.
 */

@Composable
fun HomeScreen(navigate: (String, Boolean) -> Unit) {

    val viewModel: HomeViewModel = viewModel()
    val context = LocalContext.current

    val newsHeadlines = viewModel.newsHeadlineList

    val listState = rememberLazyListState()

    var pageNumber by remember {
        mutableIntStateOf(1)
    }



    var scrollToPosition by remember {
        mutableIntStateOf(0)
    }

    scrollToPosition = when(pageNumber) {
        1 -> 0
        else -> {
            pageNumber.times(20).plus(1)
        }
    }

    LaunchedEffect(key1 = scrollToPosition, block = {
        listState.scrollToItem(scrollToPosition)
    })

    LaunchedEffect(key1 = pageNumber, block = {
        viewModel.getTopHeadlines(context = context, pageNumber)
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(color = Color.Black)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp),
            state = listState
        ) {
            if (listState.firstVisibleItemIndex == (pageNumber.times(20).minus(8))) {
                pageNumber++
            }
            items(newsHeadlines) {
                NewsItem(it, viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsItem(article: Articles, viewModel: HomeViewModel) {
    val url = article.urlToImage
    val context = LocalContext.current
    Card(
        onClick = { viewModel.openURL(context,article.url) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Column(modifier = Modifier) {
            Box(modifier = Modifier) {
                Image(
                    painter = loadRemoteImage(context = context, url = url?:""),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                Box(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .background(Color(0x8F000000))
                        .padding(5.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    Text(
                        text = article.source.name?:"",
                        color = Color.White,
                        maxLines = 1
                    )
                }
            }

            Column(
                Modifier
                    .background(
                        color = Color.White
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = article.title?:"",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 2,
                softWrap = true,
                overflow = TextOverflow.Ellipsis)

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = article.description?:"",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    maxLines = 3,
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis)

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = article.author?:"",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        softWrap = true,
                        overflow = TextOverflow.Ellipsis)
                    Text(text = article.publishedAt?:"",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        softWrap = true,
                        overflow = TextOverflow.Ellipsis)
                }
            }

        }
    }
}

@MultiplePreviews
@Composable
fun HomeScreenPreview() {
    HomeScreen(navigate = { _, _ -> })
}