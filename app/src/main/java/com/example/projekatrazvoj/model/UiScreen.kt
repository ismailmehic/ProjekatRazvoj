package com.example.projekatrazvoj.model

sealed class UiScreen(val route: String) {
    object Splash : UiScreen("splash")
    object Onboarding : UiScreen("onboarding")
    object List : UiScreen("list")
    object Details : UiScreen("details/{id}") {
        fun createRoute(id: Int) = "details/$id"
    }
    object Favorites : UiScreen("favorites")
    object Chart : UiScreen("chart")
} 