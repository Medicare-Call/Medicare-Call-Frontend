package com.konkuk.medicarecall.ui.feature.login.carecall.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.feature.login.carecall.component.CallTimeBenefit
import com.konkuk.medicarecall.ui.feature.login.carecall.component.TimePickerBottomSheet
import com.konkuk.medicarecall.ui.feature.login.carecall.component.TimeSettingItem
import com.konkuk.medicarecall.ui.feature.login.carecall.viewmodel.CallTimeViewModel
import com.konkuk.medicarecall.ui.feature.login.info.component.LoginBackButton
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.EldersInfoViewModel
import com.konkuk.medicarecall.ui.model.CallTimes
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import com.konkuk.medicarecall.ui.type.TimeSettingType
import kotlinx.coroutines.launch

// helper: Triple을 "오전/오후 hh시 mm분" 형태로 바꿔주는 함수
fun Triple<Int, Int, Int>.toDisplayString(): String {
    val (amPm, h, m) = this
    val period = if (amPm == 0) "오전" else "오후"
    return "$period ${h.toString().padStart(2, '0')}시 ${m.toString().padStart(2, '0')}분"
}

@Composable
fun CallTimeScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    navigateToPayment: () -> Unit = {},
    eldersInfoViewModel: EldersInfoViewModel = hiltViewModel(),
    callTimeViewModel: CallTimeViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) { eldersInfoViewModel.ensureLoaded() }

    val isLoading = eldersInfoViewModel.isLoading.value
    val error = eldersInfoViewModel.error.value
    val nameIdList = eldersInfoViewModel.elderNameIdMapList

    when {
        isLoading -> {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MediCareCallTheme.colors.bg)
                    .systemBarsPadding(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    color = MediCareCallTheme.colors.main,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            return
        }

        error != null -> {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("어르신 정보를 불러오지 못했어요.\n잠시 후 다시 시도해 주세요.")
                Spacer(Modifier.height(12.dp))
                CTAButton(
                    type = CTAButtonType.GREEN,
                    text = "다시 시도",
                    onClick = { eldersInfoViewModel.refresh() },
                )
            }
            return
        }

        nameIdList.isEmpty() -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("등록된 어르신이 없습니다.")
            }
            return
        }
    }

    val scrollState = rememberScrollState() // 스크롤 상태
    var showBottomSheet by remember { mutableStateOf(false) } // 하단 시트 제어
    val elderNames = nameIdList.map { it.keys.first() } // 어르신 이름 리스트
    val elderIds = nameIdList.map { it.values.first() } // 어르신 아이디 리스트

    var selectedIndex by remember { mutableIntStateOf(0) } // 선택된 어르신 인덱스
    val selectedId = elderIds.getOrNull(selectedIndex) ?: 0 // 선택된 어르신 아이디
    val saved = callTimeViewModel.timeMap[selectedId] ?: CallTimes()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val allComplete = callTimeViewModel.isAllComplete(elderIds)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .padding(horizontal = 20.dp)
            .systemBarsPadding()
            .imePadding(),
    ) {
        LoginBackButton(onClick = onBack)
        Column(
            modifier = modifier.verticalScroll(scrollState),
        ) {
            Spacer(modifier = modifier.height(20.dp))
            Text(
                text = "케어콜 설정하기",
                style = MediCareCallTheme.typography.B_26,
                color = MediCareCallTheme.colors.gray8,
            )
            Spacer(modifier = modifier.height(20.dp))
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MediCareCallTheme.colors.white, shape = RoundedCornerShape(20.dp))
                    .border(
                        width = (1.2).dp,
                        color = MediCareCallTheme.colors.gray2,
                        shape = RoundedCornerShape(20.dp),
                    )
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Box(
                            modifier = modifier
                                .background(
                                    color = MediCareCallTheme.colors.g50,
                                    shape = RoundedCornerShape(10.dp),
                                )
                                .padding(vertical = 4.dp, horizontal = 8.dp),
                        ) {
                            Text(
                                text = "특별 할인",
                                style = MediCareCallTheme.typography.R_14,
                                color = MediCareCallTheme.colors.main,
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "프리미엄",
                            color = MediCareCallTheme.colors.gray7,
                            style = MediCareCallTheme.typography.B_26,
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "₩29,000/월",
                            color = MediCareCallTheme.colors.gray4,
                            style = MediCareCallTheme.typography.R_14,
                            textDecoration = TextDecoration.LineThrough,
                        )
                        Text(
                            text = "무료 체험",
                            color = MediCareCallTheme.colors.black,
                            style = MediCareCallTheme.typography.B_17,
                        )
                    }
                }

                HorizontalDivider(
                    modifier = modifier
                        .fillMaxWidth(),
                    thickness = 1.dp,
                    color = MediCareCallTheme.colors.gray2,
                )
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    CallTimeBenefit("매일 3회 케어콜 제공")
                    CallTimeBenefit("무제한 보호자 지정")
                }
            }
            Spacer(modifier = modifier.height(30.dp))

            // 전화 시간대 설정
            Text(
                text = "전화 시간대",
                style = MediCareCallTheme.typography.M_17,
                color = MediCareCallTheme.colors.gray8,
            )
            Spacer(modifier = modifier.height(10.dp))
            val listState = rememberLazyListState()
            val scope = rememberCoroutineScope()

            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                itemsIndexed(elderNames) { idx, name ->
                    Text(
                        text = name,
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(
                                width = if (idx == selectedIndex) 0.dp else (1.2).dp,
                                color = if (idx == selectedIndex) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray2,
                                shape = RoundedCornerShape(100.dp),
                            )
                            .background(
                                color = if (idx == selectedIndex) MediCareCallTheme.colors.main else Color.Transparent,
                                shape = RoundedCornerShape(100.dp),
                            )
                            .clickable {
                                selectedIndex = idx
                                scope.launch {
                                    listState.animateScrollToItem(idx)
                                }
                            }
                            .padding(vertical = 8.dp, horizontal = 24.dp),
                        color = if (idx == selectedIndex) MediCareCallTheme.colors.g50 else MediCareCallTheme.colors.gray5,
                        style = if (idx == selectedIndex) MediCareCallTheme.typography.SB_14 else MediCareCallTheme.typography.R_14,
                    )
                }
            }
            Spacer(modifier = modifier.height(30.dp))

            // 시간 설정 항목
//            val callTimes = timeMap[selectedName]!!

            if (saved.first == null) {
                TimeSettingItem(
                    category = "1차",
                    timeType = TimeSettingType.FIRST,
                    timeText = null,
                    modifier = Modifier.clickable {
                        showBottomSheet = true
                        selectedTabIndex = 0
                    },
                )
            } else {
                TimeSettingItem(
                    category = "1차",
                    timeType = TimeSettingType.FIRST,
                    timeText = saved.first.toDisplayString(),
                    modifier = Modifier.clickable {
                        showBottomSheet = true
                        selectedTabIndex = 0
                    },
                )
                Spacer(modifier = modifier.height(20.dp))
                TimeSettingItem(
                    category = "2차",
                    timeType = TimeSettingType.SECOND,
                    timeText = saved.second?.toDisplayString(),
                    modifier = Modifier.clickable {
                        showBottomSheet = true
                        selectedTabIndex = 1
                    },
                )
                Spacer(modifier = modifier.height(20.dp))
                TimeSettingItem(
                    category = "3차",
                    timeType = TimeSettingType.THIRD,
                    timeText = saved.third?.toDisplayString(),
                    modifier = Modifier.clickable {
                        showBottomSheet = true
                        selectedTabIndex = 2
                    },
                )
            }
            Spacer(modifier = modifier.height(30.dp))

            // 안내 사항
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(
                        color = MediCareCallTheme.colors.gray1,
                        shape = RoundedCornerShape(20.dp),
                    )
                    .padding(all = 20.dp),
            ) {
                Text(
                    text = "안내사항",
                    style = MediCareCallTheme.typography.B_17,
                    color = MediCareCallTheme.colors.gray8,
                )
                Spacer(modifier = modifier.height(12.dp))
                Column(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "AI 특성상 인식 오류가 있을 수 있습니다",
                        style = MediCareCallTheme.typography.R_15,
                        color = MediCareCallTheme.colors.gray8,
                    )
                    Text(
                        text = "케어콜 번호를 어르신 휴대전화 주소록에 저장해주세요",
                        style = MediCareCallTheme.typography.R_15,
                        color = MediCareCallTheme.colors.gray8,
                    )
                }
            }
            Spacer(modifier = modifier.height(30.dp))
            CTAButton(
                if (allComplete) CTAButtonType.GREEN else CTAButtonType.DISABLED,
                text = "확인",
                onClick = {
                    if (!allComplete) return@CTAButton
                    callTimeViewModel.submitAllByIds(
                        elderIds = elderIds,
                        onSuccess = {
                            navigateToPayment()
                            Log.d("SetCallScreen", "콜 시간 설정 완료")
                            Log.d("SetCallScreen", "시간 : ${callTimeViewModel.timeMap}")
                        },
                        onError = { t ->
                            Log.e("SetCallScreen", "콜 시간 설정 실패: $t")
                        },
                    )
                },
                modifier = Modifier.padding(bottom = 20.dp),
            ) // 입력여부에 따라 Type 바뀌도록 수정 필요
            if (showBottomSheet) {
                TimePickerBottomSheet(
                    visible = true,
                    initialTabIndex = selectedTabIndex,
                    // 기존에 선택됐던 값을 다시 초기값으로 넘겨주면 UX가 매끄러워집니다.
                    initialFirstHour = saved.first?.second ?: 9,
                    initialFirstMinute = saved.first?.third ?: 0,
                    initialSecondHour = saved.second?.second ?: 12,
                    initialSecondMinute = saved.second?.third ?: 0,
                    initialThirdHour = saved.third?.second ?: 5,
                    initialThirdMinute = saved.third?.third ?: 0,
                    onDismiss = { showBottomSheet = false },
                    onConfirm = { fH, fM, sH, sM, tH, tM ->
                        callTimeViewModel.setTimes(
                            selectedId,
                            CallTimes(
                                first = Triple(0, fH, fM),
                                second = Triple(1, sH, sM),
                                third = Triple(1, tH, tM),
                            ),
                        )
                        showBottomSheet = false
                    },
                )
            }
        }
    }
}
