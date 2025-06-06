package com.example.sleephony.ui.screen.statistics.components.detail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import com.example.sleephony.R
import com.example.sleephony.data.model.StatisticMySummary
import com.example.sleephony.data.model.StatisticResults
import com.example.sleephony.data.model.StatisticSummaryData
import com.example.sleephony.ui.screen.statistics.components.detail.chart.ComparisonChart
import com.example.sleephony.ui.screen.statistics.components.detail.average.ComparisonAverage
import com.example.sleephony.ui.screen.statistics.week.StatisticsSleepHour

@Composable
fun SleepLatencyDetailScreen(
    modifier: Modifier,
    days:List<String>,
    statisticSummary : StatisticSummaryData?,
    statisticComparisonSummary : List<StatisticMySummary?>,
    statistics : StatisticResults?
) {
    val other = if (statisticComparisonSummary[0]?.gender == "M") "${statisticComparisonSummary[0]?.ageGroup} 남성" else "${statisticComparisonSummary[0]?.ageGroup} 여성"
    val mySleepLatencyAverage = statisticSummary?.averageSleepLatencyMinutes?.toInt() ?: 0
    val otherSleepLantencyAverage = statisticComparisonSummary[0]?.sleepLatencyMinutes?.toInt() ?: 0
    val pre = statisticSummary?.previousAverageSleepTimeMinutes?.toInt() ?: 0
    val myWakeUpTIme = statisticSummary?.averageWakeUpTime?.replace(":","")?.toIntOrNull() ?: 0
    val otherWakeUpTime =WakeTimeValue(statisticComparisonSummary[0]?.wakeupTime)
    Log.d("ssafy","myWakeUpTIme ${myWakeUpTIme}")
    Log.d("ssafy","otherWakeUpTime ${otherWakeUpTime}")
    Log.d("ssafy","mySleepLatencyAverage ${mySleepLatencyAverage}")
    Log.d("ssafy","otherSleepLantencyAverage ${otherSleepLantencyAverage}")
    Log.d("ssafy","pre ${pre}")

    LazyColumn(
        modifier = modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom =50.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                ComparisonAverage(
                    modifier = modifier,
                    days = days,
                    sleepHours = statistics?.sleepLatency?.map { StatisticsSleepHour(it.value.toInt()) } ?: emptyList(),
                    title = stringResource(R.string.non_sleep),
                    my_name = "내 평균",
                    my_value = mySleepLatencyAverage.toFloat(),
                    other_name = "${other} 평균",
                    other_value = otherSleepLantencyAverage.toFloat()
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="${other} 평균",
                    before_value= otherSleepLantencyAverage.toFloat(),
                    after_name = "내 평균",
                    after_value = mySleepLatencyAverage.toFloat(),
                    title = {
                        White_text("${other} 평균 취침 시간")
                        Comparison_text(blue_text = "${SummarTime(otherSleepLantencyAverage)}" , white_text = "보다")
                        Comparison_text(blue_text = "평균 ${SummarTime(Math.abs(mySleepLatencyAverage - otherSleepLantencyAverage))}", white_text = if (mySleepLatencyAverage > otherSleepLantencyAverage) "더 늦게 주무셨어요" else "더 일찍 주무셨어요")
                    }
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="저번주",
                    before_value= pre.toFloat(),
                    after_name = "이번주",
                    after_value = mySleepLatencyAverage.toFloat(),
                    title = {
                        White_text("${other} 평균 잠복기인")
                        Comparison_text(blue_text = "${SummarTime(otherSleepLantencyAverage)}", white_text = "범위 안에")
                        White_text("주무셨어요")
                    }
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="${other} 평균",
                    before_value= otherWakeUpTime.toFloat(),
                    after_name = "내 평균",
                    after_value = myWakeUpTIme.toFloat(),
                    title = {
                        White_text("${other} 평균 기상 시간")
                        Comparison_text(blue_text = "${SummarTime(otherWakeUpTime)}", white_text = "보다")
                        Comparison_text(blue_text = "평균 ${SummarTime(Math.abs(myWakeUpTIme - otherWakeUpTime))}", white_text = if (myWakeUpTIme > otherWakeUpTime) "더 주무셨어요" else "덜 주무셨어요")
                    }
                )
            }
        }
    }
}


fun WakeTimeValue(value: String?): Int {
    if (value == null) return 0
    val parts = value.split(":")
    if (parts.size < 2) return 0
    val hour = parts[0]
    val min = parts[1]
    val time = hour+min

    return time.toInt()
}

