package com.aktech.ardrawing.presentation.navigation

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
import com.aktech.ardrawing.domain.model.Book
import com.aktech.ardrawing.presentation.screen.language.ChooseLanguageScreen
import com.aktech.ardrawing.presentation.screen.onboarding.OnboardingScreen
import com.aktech.ardrawing.presentation.screen.splash.SplashScreen

object Routes {
    const val SPLASH = "SplashScreen"
    const val MAIN = "MainTabNav"
    const val SCAN_BOOK = "ScanBookScreen"
    const val SCAN_BOOK_WITH_ID = "$SCAN_BOOK?bookId={bookId}"
    const val BOOK_DETAIL = "BookDetailScreen"
    const val BOOK_DETAIL_BY_ID = "$BOOK_DETAIL/{bookId}"
    const val PAGE_DETAIL = "PageDetailScreen"
    const val CHOOSE_VOICE = "ChooseVoiceScreen"
    const val CHOOSE_LANGUAGE = "ChooseLanguageScreen"
    const val BOOK_DETAIL_GRAPH = "book_detail_graph"
    const val HOME = "HomeScreen"
    const val SETTING = "SettingScreen"
    const val ONBOARDING = "OnboardingScreen"

    data class BookDetail(
        val bookId: String,
        val book: Book? = null,
    )

    fun bookDetailById(bookId: String): String {
        return buildString {
            append("$BOOK_DETAIL/$bookId")
        }
    }

    fun scanBookWithId(bookId: String): String = "$SCAN_BOOK?bookId=$bookId"
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
    }
}