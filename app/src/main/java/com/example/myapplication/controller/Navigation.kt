package com.example.myapplication.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavGraphBuilder
import com.example.myapplication.main.MainScreen
import com.example.myapplication.main.MainViewModel
import com.example.myapplication.second.SecondScreenViewModel
import com.example.myapplication.second.SingleView

// Sealed class for screen routes
sealed class Screen(val route: String) {
    object MainScreen : Screen("main")
    object DetailScreen : Screen("detail") {
        fun withArgs(id: String): String = "$route/$id"
    }
}

// Main navigation composable
@Composable
fun Navigation(startDestination: String = Screen.MainScreen.route) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        addMainScreen(navController)
        addDetailScreen(navController)
    }
}

// Extension function for MainScreen
fun NavGraphBuilder.addMainScreen(navController: NavHostController) {
    composable(route = Screen.MainScreen.route) {
        val viewModel = hiltViewModel<MainViewModel>()
        val state by viewModel.state.collectAsState()

        MainScreen(
            onClickHead = { id ->
                navController.navigate(Screen.DetailScreen.withArgs(id))
            },
            eventPublisher = { viewModel.setEvent(it) },
            state = state
        )
    }
}

// Extension function for DetailScreen
fun NavGraphBuilder.addDetailScreen(navController: NavHostController) {
    composable(
        route = Screen.DetailScreen.route + "/{id}",
        arguments = listOf(
            navArgument("id") {
                type = NavType.StringType
                nullable = false
            }
        )
    ) {
        val viewModel = hiltViewModel<SecondScreenViewModel>()
        val state by viewModel.state.collectAsState()
        val uriHandler = LocalUriHandler.current

        SingleView(
            onClickBack = {
                navController.navigateUp()
            },
            onWikiClick = { wiki ->
                uriHandler.openUri(wiki)
            },
            state = state
        )
    }
}
