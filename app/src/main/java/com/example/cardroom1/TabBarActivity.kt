package com.example.cardroom1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cardroom1.ui.theme.CardRoom1Theme

var isUserLoggedIn = mutableStateOf(false)


class TabBarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        MainNavigationBar(navController)
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = ScreenPage.Login.route,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable(ScreenPage.Login.route) {
                            Box(
                                modifier = Modifier.padding(paddingValues)
                            ) {
                                LoginApp(navController)
                            }
                        }
                        composable(ScreenPage.Reservation.route) {
                            Box(
                                modifier = Modifier.padding(paddingValues)
                            ) {
                                IndexApp(navController)
                            }
                        }
                        composable(ScreenPage.List.route) {
                            Box(
                                modifier = Modifier.padding(paddingValues)
                            ) {
                                SituationApp(navController)
                            }
                        }
                        composable(ScreenPage.Room.route) {
                            Box(
                                modifier = Modifier.padding(paddingValues)
                            ) {
                                RoomApp()
                            }
                        }
                        composable(ScreenPage.Own.route) {
                            Box(
                                modifier = Modifier.padding(paddingValues)
                            ) {
                                OwnApp()
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MainNavigationBar(navController: NavController) {
    val items = listOf(
        ScreenPage.Login,
        ScreenPage.Reservation,
        ScreenPage.List,
        ScreenPage.Room,
        ScreenPage.Own,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.LightGray
    ) {
        items.forEach { screenPage ->
            val isSelected =
                currentDestination?.hierarchy?.any { it.route == screenPage.route } == true

            val iconRes = if (isSelected) {
                screenPage.iconSelect
            } else {
                screenPage.iconUnselect
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(screenPage.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null
                    )
                },
                label = if (screenPage.isShowText) {
                    {
                        Text(
                            text = screenPage.route,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    null
                }
            )
        }
    }
}