package com.hiz.skina.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiz.skina.R
import kotlinx.serialization.Serializable

@Serializable
object LandingRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPage(navigateToForm: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(

                title = {
                    Text("Daily Skincare", fontSize = 25.sp, fontWeight = FontWeight.W500)
                },
                modifier = Modifier.shadow(2.dp),
                actions = {
                    IconButton(onClick = navigateToForm) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Routine",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            )
        },
    )
    { padding ->
        EmptyState(Modifier.padding(padding))
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.empty_state),
            contentDescription = "Empty state",
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.5f)
        )
        Text(text = "No skincare routine found", fontSize = 22.sp)
    }
}