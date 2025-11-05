package com.konkuk.medicarecall.ui.feature.login.payment.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.feature.login.info.component.LoginBackButton
import com.konkuk.medicarecall.ui.feature.login.payment.component.PaymentPriceItem
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.EldersInfoViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PaymentScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToNaverPay: () -> Unit = {},
    elderInfoViewModel: EldersInfoViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    var isClicked by remember { mutableStateOf(false) }
    val elders = elderInfoViewModel.eldersInfoList.map { it.name }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .systemBarsPadding()
            .imePadding(),
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 20.dp),
        ) {
            LoginBackButton(onBack)
            Spacer(modifier = modifier.height(20.dp))
            Text(
                text = "결제하기",
                style = MediCareCallTheme.typography.B_26,
                color = MediCareCallTheme.colors.black,
            )
            Spacer(modifier = modifier.height(10.dp))
        }

        Column(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
                .padding(vertical = 20.dp),
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "선택한 어르신",
                    style = MediCareCallTheme.typography.B_17,
                    color = MediCareCallTheme.colors.black,
                )
                Spacer(modifier = modifier.width(8.dp))
                Text(
                    text = "${elders.size}명",
                    style = MediCareCallTheme.typography.R_14,
                    color = MediCareCallTheme.colors.gray5,
                )
            }
            Spacer(modifier = modifier.height(20.dp))
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MediCareCallTheme.colors.gray3,
                        shape = RoundedCornerShape(14.dp),
                    )
                    .background(
                        MediCareCallTheme.colors.white,
                        shape = RoundedCornerShape(14.dp),
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                elders.forEach { elder ->
                    PaymentPriceItem(elder, "29,000")
                }
            }
            Spacer(modifier = modifier.height(50.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "결제 금액",
                    style = MediCareCallTheme.typography.B_17,
                    color = MediCareCallTheme.colors.black,
                )
                Spacer(modifier = modifier.weight(1f))
                val totalAmount =
                    elders.size * 29000 // Assuming 29,000 is the monthly fee per elder
                val formatted = NumberFormat.getNumberInstance(Locale.KOREA).format(totalAmount)
                val displayText = "₩$formatted/월"
                Text(
                    text = displayText,
                    style = MediCareCallTheme.typography.SB_14,
                    color = MediCareCallTheme.colors.main,
                )
            }
            Spacer(modifier = modifier.height(20.dp))
            Button(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                onClick = { if (isClicked) isClicked = false else isClicked = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MediCareCallTheme.colors.bg,
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    2.dp,
                    color = if (isClicked) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray3,
                ),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_naver_pay),
                    contentDescription = "네이버페이 아이콘",
                    modifier = modifier
                        .height(18.dp)
                        .width(61.dp),
                )
            }
            Spacer(modifier = modifier.weight(1f))
            CTAButton(
                type = if (isClicked) CTAButtonType.GREEN else CTAButtonType.DISABLED,
                text = "결제하기",
                onClick = { if (isClicked) navigateToNaverPay() },
            )
        }
    }
}
