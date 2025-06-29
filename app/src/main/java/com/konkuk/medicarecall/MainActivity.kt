package com.konkuk.medicarecall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.konkuk.medicarecall.navigation.BottomNavItem
import com.konkuk.medicarecall.navigation.NavGraph
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediCareCallTheme {
                val navController = rememberNavController()

                val navBarItems = listOf(
                    BottomNavItem(
                        label = "홈",
                        route = "home",
                        selectedIcon = R.drawable.ic_home_selected,
                        unselectedIcon = R.drawable.ic_home_unselected
                    ),
                    BottomNavItem(
                        label = "주간 통계",
                        route = "statistics",
                        selectedIcon = R.drawable.ic_statistics_selected,
                        unselectedIcon = R.drawable.ic_statistics_unselected
                    ),
                    BottomNavItem(
                        label = "설정",
                        route = "settings",
                        selectedIcon = R.drawable.ic_settings_selected,
                        unselectedIcon = R.drawable.ic_settings_unselected,
                    )
                )

                var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

                Scaffold(
                    modifier = Modifier
                        .systemBarsPadding(),
                    contentWindowInsets = WindowInsets.safeDrawing,
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier
                                .drawBehind {
                                    val strokeWidth = 1.dp.toPx()
                                    drawLine(
                                        color = Color(0xFFECECEC), // NavigationBar의 상단 테두리
                                        start = Offset(0f, 0f),
                                        end = Offset(size.width, 0f),
                                        strokeWidth = strokeWidth,
                                    )
                                },
                            containerColor = Color.White
                        )
                        {
                            navBarItems.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = selectedIndex == index,
                                    alwaysShowLabel = true,
                                    label = {
                                        Text(
                                            text = item.label,
                                            style = MediCareCallTheme.typography.R_14
                                        )
                                    },
                                    onClick = {
                                        selectedIndex = index
                                        navController.navigate(item.route)
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(
                                                if (index == selectedIndex) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon
                                            ),
                                            contentDescription = item.label
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = Color.Transparent,
                                        selectedIconColor = MediCareCallTheme.colors.main , // 선택된 아이콘 색상
                                        unselectedIconColor = Color.Black, // 선택되지 않은 아이콘 색상
                                        selectedTextColor = MediCareCallTheme.colors.main, // 선택된 텍스트 색상
                                        unselectedTextColor = Color.Black // 선택되지 않은 텍스트 색상
                                    )
                                )
                            }
                        }
                    }
                ) {
                    innerPadding ->
                    NavGraph(
                        navController = navController,
                        modifier = Modifier
                            .padding(innerPadding)
                    )

                }
            }
        }
    }
}