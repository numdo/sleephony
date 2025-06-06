package com.example.sleephony.ui.screen.statistics.month

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sleephony.R
import com.example.sleephony.data.model.StatisticData
import com.example.sleephony.ui.screen.statistics.components.AverageSleepScore
import com.example.sleephony.ui.screen.statistics.components.DetailSleep
import com.example.sleephony.ui.screen.statistics.components.SleepSummation
import com.example.sleephony.ui.screen.statistics.components.detail.Blue_text
import com.example.sleephony.ui.screen.statistics.components.detail.Comparison_text
import com.example.sleephony.ui.screen.statistics.components.detail.Gray_text
import com.example.sleephony.ui.screen.statistics.components.detail.White_text
import com.example.sleephony.ui.screen.statistics.viewmodel.StatisticsViewModel
import com.example.sleephony.ui.screen.statistics.week.StatisticsSleepHour
import com.example.sleephony.ui.screen.statistics.week.StatisticsTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Composable
fun MonthReport(
    modifier: Modifier,
    navController: NavController,
    statisticsViewModel: StatisticsViewModel
) {
    val today = LocalDate.now()
    val monthState = statisticsViewModel.selectedMonth
    val monthEnd = monthState.with(TemporalAdjusters.lastDayOfMonth())
    val month = monthState.format(DateTimeFormatter.ofPattern("M월").withLocale(Locale.KOREAN))

    val statistics = statisticsViewModel.statistics.collectAsState().value
    val statisticSummary = statisticsViewModel.statisticSummary.collectAsState().value
    val days = remember { listOf("1주","2주","3주","4주","5주") }
    val period = "MONTH"


    LaunchedEffect(Unit,monthState) {
        statisticsViewModel.loadStatistics(
            startDate = monthState.toString(),
            endDate = monthEnd.toString(),
            periodType = period
        )
        statisticsViewModel.loadStatisticSummary(
            startDate = monthState.toString(),
            endDate = monthEnd.toString(),
            periodType = period
        )
        Log.d("ssafy","summmary $statisticSummary")
    }


    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            MonthlyCalendar(
                modifier = modifier.
                padding(0.dp,5.dp),
                monthState = monthState,
                onChange = { newDate -> statisticsViewModel.selectedMonth = newDate }
            )
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                Box() {
                    Column {
                        White_text("${month} 한달 간")
                        Blue_text("평균 ${StatisticsTime(statistics?.sleepTime ?: emptyList())}")
                        Gray_text("꿀잠을 주무셨어요!!")
                    }
                }
                AverageSleepScore(
                    modifier = modifier,
                    averageSleepScore = statisticSummary?.averageSleepScore?.toInt() ?: 0,
                    averageSleepTimeMinutes = statisticSummary?.averageSleepTimeMinutes?.toInt() ?: 0,
                    averageSleepLatencyMinutes = statisticSummary?.averageSleepLatencyMinutes?.toInt() ?: 0
                    )
                SleepSummation(
                    modifier = modifier,
                    averageSleepLatencyMinutes = statisticSummary?.averageSleepLatencyMinutes ?: 0f,
                    averageRemSleepMinutes = statisticSummary?.averageRemSleepMinutes ?: 0f,
                    averageRemSleepPercentage = statisticSummary?.averageRemSleepPercentage?.toInt() ?: 0,
                    averageLightSleepMinutes = statisticSummary?.averageLightSleepMinutes ?: 0f,
                    averageLightSleepPercentage =statisticSummary?.averageLightSleepPercentage?.toInt() ?: 0,
                    averageDeepSleepMinutes = statisticSummary?.averageDeepSleepMinutes ?: 0f,
                    averageDeepSleepPercentage = statisticSummary?.averageDeepSleepPercentage?.toInt() ?: 0,
                    averageSleepCycleCount = statisticSummary?.averageSleepCycleCount?.toInt() ?: 0,
                )
                DetailSleep(
                    modifier = modifier,
                    navController = navController,
                    title = {
                        White_text(stringResource(R.string.sleep_time))
                    },
                    days = days,
                    sleepHours = statistics?.sleepTime?.map { StatisticsSleepHour(it.value.toInt()) } ?: emptyList(),
                    path = "sleep_time",
                    period = period
                )
                DetailSleep(
                    modifier = modifier,
                    navController = navController,
                    title={
                        White_text(stringResource(R.string.sleep_latency_time))
                        Comparison_text(blue_text = "${StatisticsTime(statistics?.sleepLatency ?: emptyList())}", white_text = "이에요" )
                    },
                    days = days,
                    sleepHours = statistics?.sleepLatency?.map { it.value.toFloat() } ?: emptyList(),
                    path = "sleep_latency",
                    period = period
                )
                DetailSleep(
                    modifier = modifier,
                    navController = navController,
                    title={
                        White_text(stringResource(R.string.average_REM_sleep))
                        Comparison_text(blue_text = "${ StatisticsTime(statistics?.remSleep ?: emptyList())}", white_text = "이에요")
                    },
                    days = days,
                    sleepHours = statistics?.remSleep?.map {StatisticsSleepHour(it.value.toInt()) } ?: emptyList(),
                    path = "sleep_REM",
                    period = period
                )
                DetailSleep(
                    modifier = modifier,
                    navController = navController,
                    title={
                        White_text(stringResource(R.string.average_light_sleep))
                        Comparison_text(blue_text = "${StatisticsTime(statistics?.lightSleep ?: emptyList())}", white_text = "이에요")
                    },
                    days = days,
                    sleepHours = statistics?.lightSleep?.map { StatisticsSleepHour(it.value.toInt()) } ?: emptyList(),
                    path = "sleep_light",
                    period = period
                )
                DetailSleep(
                    modifier = modifier,
                    navController = navController,
                    title={
                        White_text(stringResource(R.string.average_deep_sleep))
                        Comparison_text(blue_text = "${StatisticsTime(statistics?.deepSleep ?: emptyList())}", white_text = "이에요")
                    },
                    days = days,
                    sleepHours = statistics?.deepSleep?.map { StatisticsSleepHour(it.value.toInt()) } ?: emptyList(),
                    path = "sleep_deep",
                    period = period
                )
            }
        }
    }
}