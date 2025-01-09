package com.example.cardroom1

sealed class ScreenPage(
    val route: String,
    val iconSelect: Int,
    val iconUnselect: Int,
    val isShowText: Boolean = true
) {
    object Reservation : ScreenPage(
        route = "房间预约",
        iconSelect = R.drawable.reservation,
        iconUnselect = R.drawable.reservation
    )

    object Login : ScreenPage(
        route = "登录",
        iconSelect = R.drawable.login,
        iconUnselect = R.drawable.login
    )

    object List : ScreenPage(
        route = "预约情况",
        iconSelect = R.drawable.list,
        iconUnselect = R.drawable.list,
        isShowText = true
    )

    object Own : ScreenPage(
        route = "我的",
        iconSelect = R.drawable.own,
        iconUnselect = R.drawable.own
    )

    object Room : ScreenPage(
        route = "房间",
        iconSelect = R.drawable.home,
        iconUnselect = R.drawable.home
    )
}