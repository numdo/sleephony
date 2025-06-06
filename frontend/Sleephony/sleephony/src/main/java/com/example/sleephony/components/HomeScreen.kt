package com.example.sleephony.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Scaffold
import com.example.sleephony.components.mypage.MyPageScreen
import com.example.sleephony.presentation.components.alarm.AlarmScreen
import com.example.sleephony.presentation.components.hitory.HistoryScreen
import com.example.sleephony.presentation.components.step.StepIndicator
import com.example.sleephony.viewmodel.AlarmViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: AlarmViewModel
) {
    val pageState = rememberPagerState()
    val stepNum = remember { mutableStateOf(1) }

    LaunchedEffect(pageState.currentPage) {
        stepNum.value = pageState.currentPage + 1
    }

    Scaffold {
        Column(modifier = modifier.fillMaxSize()
            .padding(bottom = 10.dp)) {
            StepIndicator(step = stepNum.value)

            HorizontalPager(
                count = 3,
                state = pageState,
                modifier = modifier.fillMaxSize()
            ) { page ->
                when(page) {
                    0 -> AlarmScreen(modifier = modifier, navController = navController, viewModel = viewModel)
                    1 -> HistoryScreen(modifier = modifier, navController = navController)
                    2 -> MyPageScreen(modifier = modifier, navController = navController)
                }
            }
        }
    }
}