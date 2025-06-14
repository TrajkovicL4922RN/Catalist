package com.example.myapplication.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.main.MainScreen
import com.example.myapplication.main.MainViewModel
import com.example.myapplication.second.SecondScreenViewModel
import com.example.myapplication.second.SingleView

@Composable
fun Navigation(startDestination: String = Screen.MainScreen.route){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination ){
        composable (route = Screen.MainScreen.route) {
            val mainViewModel = hiltViewModel<MainViewModel>()
            val state by mainViewModel.state.collectAsState()

            MainScreen(
                onClickHead = { id ->
                    val x = Screen.DetailScreen.withArgs(id)
                    navController.navigate(x)
                },
                eventPublisher = { mainViewModel.setEvent(it) },
                state = state
            )
        }
        composable(route = Screen.DetailScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { entry ->
            // Jednostavno koristi hiltViewModel() - SavedStateHandle će automatski imati "id" argument
            val secondScreenViewModel = hiltViewModel<SecondScreenViewModel>()
            val state by secondScreenViewModel.state.collectAsState()
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
}