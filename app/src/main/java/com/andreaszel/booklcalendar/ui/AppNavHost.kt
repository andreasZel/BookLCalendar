package com.andreaszel.booklcalendar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.andreaszel.booklcalendar.ui.add.AddBookScreen
import com.andreaszel.booklcalendar.ui.detail.BookDetailScreen
import com.andreaszel.booklcalendar.ui.shelf.ShelfScreen
import com.andreaszel.booklcalendar.ui.theme.Palette

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Routes.SHELF, modifier = Modifier.fillMaxSize().background(Palette.Paper)) {
        composable(Routes.SHELF) {
            ShelfScreen(onAdd = { nav.navigate(Routes.edit()) }, onOpen = { nav.navigate(Routes.detail(it)) })
        }
        composable(Routes.DETAIL, arguments = listOf(navArgument("id") { type = NavType.LongType })) { entry ->
            BookDetailScreen(bookId = entry.arguments?.getLong("id") ?: 0L, onBack = { nav.popBackStack() }, onEdit = { nav.navigate(Routes.edit(it)) })
        }
        composable(Routes.EDIT, arguments = listOf(navArgument("id") { type = NavType.LongType; defaultValue = 0L })) { entry ->
            AddBookScreen(bookId = entry.arguments?.getLong("id") ?: 0L, onDone = { nav.popBackStack() })
        }
    }
}

object Routes {
    const val SHELF = "shelf"
    const val DETAIL = "book/{id}"
    const val EDIT = "edit?id={id}"

    fun detail(id: Long) = "book/$id"
    fun edit(id: Long = 0L) = "edit?id=$id"
}
