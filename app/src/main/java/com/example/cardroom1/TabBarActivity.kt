package com.example.cardroom1

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cardroom1.ui.theme.CardRoom1Theme

var isUserLoggedIn = mutableStateOf(false)

data class MenuItem(
    val text: String,
    val onClick: (() -> Unit)? = null
)

class TabBarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
                TapBarApp()
            }
        }
    }
}

@Composable
fun TapBarApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            when (currentRoute) {
                ScreenPage.Login.route-> {
                    TopBar(navController, title = "用户登录", showBackButton = false, showMenu = false)
                }
                ScreenPage.Own.route -> {
                    TopBar(navController, title = "个人中心", showBackButton = true, showMenu = true, menuItems = listOf(
                        MenuItem("设置") { navController.navigate(ScreenPage.Setting.route) },
                        MenuItem("布局") { logout(navController) }
                    ))
                }
                ScreenPage.Index.route->{
                    TopBar(navController, title = "房间预约", showBackButton = false,showMenu = true)
                }
                ScreenPage.List.route->{
                    TopBar(navController, title = "预约情况", showBackButton = false, showMenu = true,menuItems = listOf(
                        MenuItem("搜索记录") {navController.navigate(ScreenPage.Search.route)},
                    ))
                }
                ScreenPage.Room.route->{
                    TopBar(navController, title = "房间", showBackButton = true, showMenu = true, menuItems = listOf(
                        MenuItem("设置") { navController.navigate(ScreenPage.Setting.route) },
                    ))
                }
                else -> {
                    TopBar(navController, title = currentRoute ?: "", showBackButton = true, showMenu = true)
                }
            }
        },
        bottomBar = {
            if (currentRoute !in listOf(ScreenPage.Login.route, ScreenPage.Forget.route, ScreenPage.Register.route)) {
                MainNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = ScreenPage.Login.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(ScreenPage.Login.route) { LoginApp(navController) }
            composable(ScreenPage.Index.route) {
                val viewModel: ReservationViewModel = viewModel()
                val reservations by viewModel.reservations.collectAsState(emptyList())
                IndexApp(navController,viewModel,reservations)
            }
            composable(ScreenPage.List.route) { SituationApp(navController) }
            composable(ScreenPage.Room.route) { RoomApp(navController) }
            composable(ScreenPage.Own.route) { OwnApp() }
            composable(ScreenPage.Forget.route) { ForgetApp(navController) }
            composable(ScreenPage.Register.route) { RegisterApp(navController) }
            composable("${ScreenPage.Reservation.route}/{reservationId}") { backStackEntry ->
                val reservationId = backStackEntry.arguments?.getString("reservationId")?.toLongOrNull() ?: 0L
                val viewModel: ReservationViewModel = viewModel()
                ReservationApp(navController, viewModel, reservationId)
            }
            composable(ScreenPage.Search.route){ SearchApp(navController) }
            composable(ScreenPage.Setting.route){ SettingApp() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    title: String,
    showBackButton: Boolean,
    showMenu: Boolean,
    menuItems: List<MenuItem> = emptyList()
) {
    val openMenu = remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = { Text(text = title, fontSize = 40.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = {
                    navController.popBackStack()
                    println("返回")
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                }
            }
        },
        actions = {
            if (showMenu) {
                IconButton(onClick = { openMenu.value = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Menu")
                }
                DropdownMenu(
                    expanded = openMenu.value,
                    onDismissRequest = { openMenu.value = false }
                ) {
                    menuItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.text) },
                            onClick = {
                                openMenu.value = false
                                item.onClick?.invoke()
                            }
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    )
}

private fun logout(navController: NavController) {
    isUserLoggedIn.value = false
    Log.d("TabBarActivity", "User logged out")
    navController.navigate(ScreenPage.Login.route) {
        popUpTo(navController.graph.startDestinationId) {
            inclusive = true
            saveState = false
        }
        launchSingleTop = true
        restoreState = false
    }
}

@Composable
fun MainNavigationBar(navController: NavController) {
    val items = listOf(
        ScreenPage.Index,
        ScreenPage.List,
        ScreenPage.Room,
        ScreenPage.Own
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val context = LocalContext.current

    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.LightGray
    ) {
        items.forEach { screenPage ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screenPage.route } == true
            val iconRes = if (isSelected) screenPage.iconSelect else screenPage.iconUnselect

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isUserLoggedIn.value && screenPage.route != ScreenPage.Login.route) {
                        navController.navigate(ScreenPage.Login.route)
                        Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
                    } else {
                        navController.navigate(screenPage.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(painter = painterResource(id = iconRes), contentDescription = null)
                },
                label = if (screenPage.isShowText) {
                    { Text(text = screenPage.route, fontSize = 12.sp) }
                } else {
                    null
                }
            )
        }
    }
}