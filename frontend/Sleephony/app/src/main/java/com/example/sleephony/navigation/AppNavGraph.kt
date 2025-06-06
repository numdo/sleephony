package com.example.sleephony.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.sleephony.ui.common.components.BottomNavBar
import com.example.sleephony.ui.screen.auth.ProfileSetupScreen
import com.example.sleephony.ui.screen.auth.ProfileViewModel
import com.example.sleephony.ui.screen.auth.SocialLoginScreen
import com.example.sleephony.ui.screen.report.AiReportScreen
import com.example.sleephony.ui.screen.report.ReportScreen
import com.example.sleephony.ui.screen.settings.SettingViewModel
import com.example.sleephony.ui.screen.report.viewmodel.ReportViewModel
import com.example.sleephony.ui.screen.settings.SettingsGuideScreen
import com.example.sleephony.ui.screen.statistics.components.detail.SleepDetailScreen
import com.example.sleephony.ui.screen.settings.SettingsHomeScreen
import com.example.sleephony.ui.screen.settings.SettingsUserProfileScreen
import com.example.sleephony.ui.screen.settings.SettingsUserProfileUpdateScreen
import com.example.sleephony.ui.screen.settings.SettingsWatchScreen
import com.example.sleephony.ui.screen.sleep.SleepMeasurementScreen
import com.example.sleephony.ui.screen.sleep.SleepSettingScreen
import com.example.sleephony.ui.screen.sleep.SleepUiState
import com.example.sleephony.ui.screen.sleep.SleepViewModel
import com.example.sleephony.ui.screen.splash.SplashScreen
import com.example.sleephony.ui.screen.splash.SplashViewModel
import com.example.sleephony.ui.screen.statistics.StatisticsScreen
import com.example.sleephony.ui.screen.statistics.viewmodel.StatisticsViewModel
import java.time.LocalDate
import com.example.sleephony.utils.WearMessageUtils
import com.google.android.gms.wearable.WearableListenerService

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    vm:SleepViewModel
) {
    val statisticsViewModel: StatisticsViewModel = hiltViewModel()
    val reportViewModel: ReportViewModel = hiltViewModel()
    val splashVm: SplashViewModel = hiltViewModel()
    val profileVm: ProfileViewModel = hiltViewModel()
    val settingVm: SettingViewModel = hiltViewModel()
    val context = LocalContext.current

    // 현재 경로 가져오기
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    // 바텀바를 보여줄 라우트 목록
    val bottomRoutes = listOf(
        "sleep_setting",
        "report",
        "statistics",
        "settings"
    )
    val showBottomBar = currentRoute in bottomRoutes

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            composable("splash") {
                SplashScreen(
                    navController = navController,
                    viewModel = splashVm,
                    statisticsViewModel = statisticsViewModel
                )
            }

            composable("login") {
                SocialLoginScreen(
                    onNeedsProfile = {
                        navController.navigate("profile_setup") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onLoginSuccess = {
                        navController.navigate("sleep_setting") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            navigation(
                startDestination = ProfileStep.NICKNAME.route,  // 첫 화면: 닉네임
                route = "profile_setup"
            ) {
                ProfileStep.entries.forEach { step ->
                    composable(step.route) { backStackEntry ->
                        val parentEntry = remember(backStackEntry) {
                            navController.getBackStackEntry("profile_setup")
                        }

                        ProfileSetupScreen(
                            step = step,
                            viewModel = profileVm,
                            onNext = {
                                ProfileStep.entries.getOrNull(step.ordinal + 1)?.let { next ->
                                    navController.navigate(next.route)
                                }
                                // 마지막 단계: ViewModel 에 제출 요청
                                    ?: profileVm.submitProfile()
                            },
                            onComplete = {
                                navController.navigate("sleep_setting") {
                                    popUpTo("profile_setup") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
            // 수면 측정 관련
            composable("sleep_setting") {

                SleepSettingScreen(
                    viewModel = vm,
                    onStart = {
                        vm.onStartClicked()
                        WearMessageUtils.SendMessage(
                            context = context,
                            mode = "alarm",
                            data = mapOf(
                                "hour" to vm.settingData.value.hour,
                                "minute" to vm.settingData.value.minute,
                                "isAm" to if (vm.settingData.value.isAm) "오전" else "오후",
                                "alarmType" to vm.settingData.value.mode,
                            )
                        )
                        navController.navigate("sleep_measurement") {
                            popUpTo("sleep_setting") { inclusive = false }
                        }
                    },
                    navController = navController
                )
            }

            composable("sleep_measurement") {
                val uiState by vm.uiState.collectAsState()

                LaunchedEffect(uiState) {
                    if(uiState is SleepUiState.Setting) {
                        navController.navigate("sleep_setting") {
                            popUpTo("sleep_measurement") { inclusive = true }
                        }
                    }
                }

                SleepMeasurementScreen(
                    onStop = {
                        vm.onStopClicked()
                        navController.navigate("sleep_setting") {
                            popUpTo("sleep_measurement") { inclusive = false }
                        }
                    },
                    viewModel = vm
                )
            }

            composable("report") {
                ReportScreen(navController = navController, reportViewModel = reportViewModel)
            }

            composable(
                "ai_report/{selectedDate}",
                arguments = listOf(navArgument("selectedDate") { type = NavType.StringType })
            ) { backStackEntry ->
                val selectedDate = backStackEntry.arguments?.getString("selectedDate")?.let {
                    LocalDate.parse(it)
                } ?: LocalDate.now() // Fallback to current date if not found

                AiReportScreen(navController = navController, selectedDate = selectedDate)
            }

            composable("statistics") {
                StatisticsScreen(modifier = Modifier, navController = navController, statisticsViewModel = statisticsViewModel)
            }

            composable("settings") {
                SettingsHomeScreen(
                    viewModel = settingVm,
                    logout = {
                        navController.navigate("login") {
                            popUpTo("settings") { inclusive = true}
                        }
                    },
                    goUserProfile = {
                      navController.navigate("settings_profile")
                    },
                    goWearable = {
                        navController.navigate("settings_wearable")
                    },
                    goGuide = {
                        navController.navigate("settings_guide")
                    }
                )
            }

            composable("settings_profile") {
                SettingsUserProfileScreen(
                    viewModel = settingVm,
                    backSettingHome = {
                        navController.navigate("settings") {
                            popUpTo("settings_profile") { inclusive = true }
                        }
                    },
                    deleteUser = {
                        navController.navigate("login") {
                            popUpTo("settings_profile") { inclusive = true }
                        }
                    },
                    goUpdateScreen = { key ->
                        navController.navigate("settings_profile/$key") {
                            popUpTo("settings_profile") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(
                route = "settings_profile/{key}",
                arguments = listOf(
                    navArgument("key") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val key =  backStackEntry.arguments?.getString("key")!!
                SettingsUserProfileUpdateScreen(
                    viewModel = settingVm,
                    key = key,
                    updateProfile = {
                        navController.popBackStack()
                    }
                )
            }

            composable("settings_wearable"){
                SettingsWatchScreen(
                    viewModel = settingVm,
                    backSettingHome = {
                        navController.navigate("settings") {
                            popUpTo("settings_wearable") { inclusive = true }
                        }
                    },
                )
            }

            composable("settings_guide"){
                SettingsGuideScreen(
                    backSettingHome = {
                        navController.navigate("settings") {
                            popUpTo("settings_guide") { inclusive = true }
                        }
                    },
                )
            }


            composable("detail/{page}/{period}") { it ->
                val page = it.arguments?.getString("page") ?: ""
                val period = it.arguments?.getString("period") ?: ""
                val days = when (period) {
                    "week" -> listOf("월", "화", "수", "목", "금", "토", "일")
                    "month" -> listOf("1주", "2주", "3주", "4주", "5주")
                    "year" -> listOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")
                    else -> emptyList()
                }
                SleepDetailScreen(
                    page = page,
                    navController = navController,
                    days = days,
                    statisticsViewModel = statisticsViewModel
                )
            }
        }
    }
}