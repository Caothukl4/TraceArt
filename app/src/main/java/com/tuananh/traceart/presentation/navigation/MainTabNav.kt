package com.tuananh.traceart.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tuananh.traceart.R
import com.tuananh.traceart.presentation.screen.home.HomeScreen
import com.tuananh.traceart.presentation.screen.setting.SettingScreen
import com.tuananh.traceart.presentation.theme.AppTypography
import com.tuananh.traceart.presentation.theme.Colors
import com.tuananh.traceart.utils.customBorder

@Composable
fun MainTabNav(navController: NavHostController) {
    val tabNav = rememberNavController()
    val analyticsViewModel: AnalyticsViewModel = hiltViewModel()
    LaunchedEffect(tabNav) {
        tabNav.addOnDestinationChangedListener { _, destination, _ ->
            destination.route?.let { route ->
                analyticsViewModel.trackScreens(route)
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = tabNav, startDestination = Routes.HOME,
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
            composable(Routes.HOME) { HomeScreen() }
            composable(Routes.SETTING) { SettingScreen() }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .customBorder(color = Colors.surfaceVariant, strokeWidth = 1.dp, top = true)
                .align(Alignment.BottomCenter)
        ) {

            NavigationBar(
                modifier = Modifier.height(130.dp),
                containerColor = Colors.surface,
            ) {
                TabBarItem(
                    modifier = Modifier.weight(1f),
                    route = Routes.HOME,
                    label = stringResource(R.string.tab_home),
//                    iconResource = R.drawable.ic_home,
                    imageVector = Icons.Filled.Home,
                    tabNav = tabNav
                )
//                ActionButton(onClick = {
//                    navController.navigateWith(Routes.SCAN_BOOK)
//                })
                TabBarItem(
                    modifier = Modifier.weight(1f),
                    route = Routes.SETTING,
                    label = stringResource(R.string.tab_setting),
//                    iconResource = R.drawable.ic_setting,
                    imageVector = Icons.Filled.Settings,
                    tabNav = tabNav
                )
            }
        }
    }
}


@Composable
fun TabBarItem(
    modifier: Modifier = Modifier,
    route: String,
    label: String,
//    iconResource: Int,
    imageVector: ImageVector,
    tabNav: NavHostController
) {
    val currentBackStack by tabNav.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val selected = currentRoute == route
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                tabNav.navigate(route) {
                    popUpTo(tabNav.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(26.dp),
//            painter = painterResource(iconResource),
            imageVector = imageVector,
            contentDescription = "previous",
            tint = if (selected) Colors.primary else Colors.onBackground
        )
        Text(
            text = label,
            style = AppTypography.labelMedium,
            modifier = Modifier.padding(top = 4.dp),
            color = if (selected) Colors.primary else Colors.onBackground
        )
    }
}

