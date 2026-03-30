package com.tuananh.traceart.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tuananh.traceart.domain.model.Book
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.tuananh.traceart.presentation.screen.drawing.DrawingScreen
import com.tuananh.traceart.presentation.screen.home.HomeScreen
import com.tuananh.traceart.presentation.screen.language.ChooseLanguageScreen
import com.tuananh.traceart.presentation.screen.onboarding.OnboardingScreen
import com.tuananh.traceart.presentation.screen.result.ResultScreen
import com.tuananh.traceart.presentation.screen.splash.SplashScreen

object Routes {
    const val SPLASH = "SplashScreen"
    const val MAIN = "MainTabNav"
    const val HOME = "HomeScreen"
    const val DRAWING = "DrawingScreen"
    const val RESULT = "ResultScreen"
    const val SETTING = "SettingScreen"
    const val ONBOARDING = "OnboardingScreen"
    const val CHOOSE_LANGUAGE = "ChooseLanguageScreen"
    // Helper to pass image uri to drawing screen
    fun drawing(imageUri: String) = "$DRAWING?imageUri=$imageUri"
    const val DRAW_WITH_URI = "$DRAWING?imageUri={imageUri}"
    
    fun result(score: Int) = "$RESULT?score=$score"
    const val RESULT_WITH_SCORE = "$RESULT?score={score}"
}

@Composable
fun AppNavHost(navController: NavHostController) {
    val analyticsViewModel: AnalyticsViewModel = hiltViewModel()

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            destination.route?.let { route ->
                analyticsViewModel.trackScreens(route)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(250)
            ) + fadeIn(animationSpec = tween(250))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(250)
            ) + fadeOut(animationSpec = tween(250))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(250)
            ) + fadeIn(animationSpec = tween(250))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(250)
            ) + fadeOut(animationSpec = tween(250))
        }
    ) {
        composable(Routes.SPLASH) { SplashScreen() }
        composable(Routes.ONBOARDING) { OnboardingScreen() }
        composable(Routes.CHOOSE_LANGUAGE) { ChooseLanguageScreen() }
        composable(Routes.MAIN) { MainTabNav(navController) }
        composable(Routes.HOME) { HomeScreen() }
        composable(
            route = Routes.DRAW_WITH_URI,
            arguments = listOf(
                navArgument("imageUri") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri") ?: ""
            DrawingScreen(imageUri = imageUri)
        }
        composable(
            route = Routes.RESULT_WITH_SCORE,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            ResultScreen(score = score)
        }
    }
}