package com.example.compose_responsive_ui_part_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookmarks.BookmarksScreen
import com.example.compose_responsive_ui_part_1.model.Screen
import com.example.compose_responsive_ui_part_1.ui.NavigationActions
import com.example.compose_responsive_ui_part_1.ui.theme.Compose_responsive_ui_part_1Theme
import com.example.movies.MoviesScreen
import com.example.settings.SettingsScreen
import kotlinx.coroutines.launch


private val menuItems = listOf(
    Screen.Movies,
    Screen.Bookmarks,
    Screen.Settings
)

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose_responsive_ui_part_1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val navigationActions = remember { NavigationActions(navController) }
                    val windowSizeClass = calculateWindowSizeClass(this)
                    MyApp(windowSizeClass, navigationActions)
                }
            }
        }
    }
}

@Composable
private fun MyApp(windowSizeClass: WindowSizeClass, navigationActions: NavigationActions) {

    when(windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            CompactScreen(navigationActions.navController, {
                navigationActions.navigate(it)
            }, {
                MyAppNavHost(navigationActions.navController, it)
            })
        }
        WindowWidthSizeClass.Medium -> {
            MediumScreen(navigationActions.navController, {
                navigationActions.navigate(it)
            }, {
                MyAppNavHost(navigationActions.navController, it)
            })
        }
        WindowWidthSizeClass.Expanded -> {
            ExpandedScreen(navigationActions.navController, {
                navigationActions.navigate(it)
            }, {
                MyAppNavHost(navigationActions.navController, it)
            })
        }
    }
}

@Composable
private fun MyAppNavHost(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(navController, startDestination = Screen.Movies.route, Modifier.padding(innerPadding)) {
        composable(Screen.Movies.route) { MoviesScreen() }
        composable(Screen.Bookmarks.route) { BookmarksScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}

@Composable
private fun CompactScreen(navController: NavHostController,
                          onClick: (route: String) -> Unit,
                          content: @Composable (innerPadding: PaddingValues) -> Unit) {
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                menuItems.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = { onClick(screen.route) }
                    )
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Composable
private fun MediumScreen(navController: NavHostController,
                         onClick: (route: String) -> Unit,
                         content: @Composable (innerPadding: PaddingValues) -> Unit) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail {
            menuItems.forEachIndexed { _, screen ->
                NavigationRailItem(
                    label = { Text(stringResource(screen.resourceId)) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = { onClick(screen.route) },
                    icon = { Icon(screen.icon, contentDescription = null) }
                )
            }
        }
        content(PaddingValues(0.dp))
    }
}


@Composable
private fun ExpandedScreen(navController: NavHostController,
                           onClick: (route: String) -> Unit,
                           content: @Composable (innerPadding: PaddingValues) -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Drawer title", modifier = Modifier.padding(16.dp))
                Divider()
                menuItems.forEachIndexed { _, screen ->
                    NavigationDrawerItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                            onClick(screen.route)
                        }
                    )
                }
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("Show drawer") },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            }
        ) { contentPadding ->
            content(contentPadding)
        }
    }
}

@Preview
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun PreviewCompactScreen() {
    val navController = rememberNavController()
    val navigationActions = remember { NavigationActions(navController) }
    val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 900.dp))
    MyApp(windowSizeClass, navigationActions)
}

@Preview(showBackground = true, widthDp = 700, heightDp = 500)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun PreviewMediumScreen() {
    val navController = rememberNavController()
    val navigationActions = remember { NavigationActions(navController) }
    val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(700.dp, 500.dp))
    MyApp(windowSizeClass, navigationActions)
}

@Preview(showBackground = true, widthDp = 1100, heightDp = 600)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun PreviewExtendedScreen() {
    val navController = rememberNavController()
    val navigationActions = remember { NavigationActions(navController) }
    val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1100.dp, 600.dp))
    MyApp(windowSizeClass, navigationActions)
}


