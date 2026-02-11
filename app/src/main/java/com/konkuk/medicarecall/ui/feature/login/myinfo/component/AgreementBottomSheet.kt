package com.konkuk.medicarecall.ui.feature.login.myinfo.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgreementBottomSheet(
    sheetState: SheetState,
    checkedStates: List<Boolean>,
    allAgreeCheckState: Boolean,
    onDismissRequest: () -> Unit,
    onAllAgreeClick: () -> Unit,
    onCheckedChange: (Int, Boolean) -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val itemList = listOf(
        "서비스 이용약관",
        "개인정보 수집 및 이용 동의",
    )
    val isCheckedAll = checkedStates.all { it }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MediCareCallTheme.colors.bg,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        modifier = modifier,
    ) {
        Column {
            Text(
                "회원가입을 위해\n약관 동의가 필요합니다",
                color = MediCareCallTheme.colors.black,
                style = MediCareCallTheme.typography.B_20,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp),
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = onAllAgreeClick,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painterResource(R.drawable.ic_check_box),
                    contentDescription = "체크박스",
                    tint = if (allAgreeCheckState) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray2,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "전체 동의하기",
                    color = MediCareCallTheme.colors.black,
                    style = MediCareCallTheme.typography.SB_16,
                )
            }
        }
        HorizontalDivider(
            thickness = 1.4.dp,
            color = MediCareCallTheme.colors.gray2,
        )
        Spacer(Modifier.height(12.dp))

        itemList.forEachIndexed { index, title ->
            AgreementItem(
                title,
                isChecked = checkedStates[index],
                onCheckedChange = { onCheckedChange(index, !checkedStates[index]) },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            )
        }

        CTAButton(
            type = if (isCheckedAll) CTAButtonType.GREEN else CTAButtonType.DISABLED,
            text = "다음",
            onClick = onNextClick,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 30.dp, top = 20.dp),
        )
    }
}
