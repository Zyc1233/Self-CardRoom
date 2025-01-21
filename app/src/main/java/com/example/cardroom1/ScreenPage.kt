package com.example.cardroom1

sealed class ScreenPage(
    val route: String,
    val iconSelect: Int,
    val iconUnselect: Int,
    val isShowText: Boolean = true
) {
    object Index : ScreenPage(
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
        route = "房间/{reservationId}",
        iconSelect = R.drawable.home,
        iconUnselect = R.drawable.home
    )

    object Forget : ScreenPage(
        route = "忘记密码",
        iconUnselect = R.drawable.own,
        iconSelect =R.drawable.own
    )

    object Register : ScreenPage(
        route = "注册",
        iconUnselect = R.drawable.own,
        iconSelect =R.drawable.own
    )

    object Reservation : ScreenPage(
        route = "预约信息/{reservationId}",
        iconSelect = R.drawable.own,
        iconUnselect = R.drawable.own
    )

    object Search : ScreenPage(
        route = "搜索记录",
        iconUnselect = R.drawable.own,
        iconSelect = R.drawable.own
    )

    object Setting : ScreenPage(
        route = "设置",
        iconUnselect = R.drawable.own,
        iconSelect = R.drawable.own
    )

    object About : ScreenPage(
        route = "关于",
        iconUnselect = R.drawable.own,
        iconSelect = R.drawable.own
    )
}