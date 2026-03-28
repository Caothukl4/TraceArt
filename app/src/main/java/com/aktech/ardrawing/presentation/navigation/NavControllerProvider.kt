package com.aktech.ardrawing.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("NavController not provided")
}

@Composable
fun <T> NavHostController.getParamsFromNavigate(
    onResult: (T) -> Unit
) {
    val prevEntry = previousBackStackEntry
    val savedStateHandle = prevEntry?.savedStateHandle

    // chỉ lắng nghe nếu previousBackStackEntry đúng route mong muốn
    val resultState = savedStateHandle
        ?.getStateFlow<T?>("params", null)
        ?.collectAsState()

    val result = resultState?.value
    LaunchedEffect(result) {
        result?.let {
            onResult(it)
            savedStateHandle.remove<T>("params")
        }
    }
}

@Composable
fun <T> NavHostController.getParamsFromBack(
    onResult: (T) -> Unit
) {
    val currentEntry = currentBackStackEntry
    val savedStateHandle = currentEntry?.savedStateHandle

    // chỉ lắng nghe nếu currentBackStackEntry đúng route mong muốn
    val resultState = savedStateHandle
        ?.getStateFlow<T?>("back_params", null)
        ?.collectAsState()

    val result = resultState?.value
    LaunchedEffect(result) {
        result?.let {
            onResult(it)
            savedStateHandle.remove<T>("back_params")
        }
    }
}

fun NavHostController.navigateWith(route: String, params: Any? = null, forceNewScreen: Boolean? = false) {
//    val exists = runCatching { getBackStackEntry(route) }.isSuccess
    val prefix = route.substringBefore("/") // ví dụ: "book_detail"

    val baseRoute = when (prefix) {
        Routes.BOOK_DETAIL -> Routes.BOOK_DETAIL_BY_ID
        Routes.SCAN_BOOK -> Routes.SCAN_BOOK
        else -> null
    }
    val exists = baseRoute?.let {
        runCatching { getBackStackEntry(it) }.isSuccess
    } ?: false

    if (exists && (forceNewScreen == null || !forceNewScreen)) {
        baseRoute?.let {
            getBackStackEntry(it).savedStateHandle["back_params"] = params
        }
        // Nếu route đã có trong stack thì remove màn hiện tại
        popBackStack()
        navigate(route) {
            launchSingleTop = true
        }
    } else {
        currentBackStackEntry?.savedStateHandle?.set("params", params)

        // Nếu chưa có thì push bình thường
        navigate(route) {
            launchSingleTop = true
        }
    }
}

//@Composable
//inline fun <reified T> NavHostController.getParams(
//    noinline onResult: (T) -> Unit
//) {
//    this.collectResult("params", onResult)
//}

@Composable
inline fun <reified VM : ViewModel> NavHostController.hiltGraphViewModel(route: String): VM {
    val parentEntry = getBackStackEntry(route)
    return hiltViewModel(parentEntry)
}