package com.example.newsapp.constants


/**
 * Created by Abhishek Shah on 27 September 2023
 *
 * Thinkitive Technologies Pvt. Ltd.
 */
sealed class NavConstants(val id: String,val title: String,val route: String, val icon: Int = 0) {

    object HomeNavigation: NavConstants(
        id = "home",
        title = "Home",
        route = Navigation.HomeScreenNav.name
    )

}