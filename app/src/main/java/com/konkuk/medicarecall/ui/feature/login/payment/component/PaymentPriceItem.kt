package com.konkuk.medicarecall.ui.feature.login.payment.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun PaymentPriceItem(name: String, price: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "$name 님",
            style = MediCareCallTheme.typography.M_17,
            color = MediCareCallTheme.colors.gray8,
        )
        Spacer(modifier = modifier.height(8.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(color = MediCareCallTheme.colors.bg, shape = RoundedCornerShape(6.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
        ) {
            Text(
                text = "주문 금액",
                style = MediCareCallTheme.typography.R_14,
                color = MediCareCallTheme.colors.gray8,
            )
            Spacer(modifier = modifier.weight(1f))
            Text(
                text = "₩$price/월",
                style = MediCareCallTheme.typography.R_14,
                color = MediCareCallTheme.colors.gray8,
            )
        }
    }
}
