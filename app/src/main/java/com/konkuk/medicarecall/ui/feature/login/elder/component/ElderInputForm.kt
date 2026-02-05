package com.konkuk.medicarecall.ui.feature.login.elder.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.type.ElderResidenceType
import com.konkuk.medicarecall.ui.type.RelationshipType
import com.konkuk.medicarecall.ui.common.component.DefaultDropdown
import com.konkuk.medicarecall.ui.common.component.DefaultTextField
import com.konkuk.medicarecall.ui.common.component.GenderToggleButton
import com.konkuk.medicarecall.ui.feature.login.elder.viewmodel.LoginElderData
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.GenderType

@Composable
fun ElderInputForm(
    scrollState: ScrollState,
    elderData: LoginElderData,
    onGenderChanged: (GenderType) -> Unit,
    onRelationshipChange: (RelationshipType) -> Unit,
    onLivingTypeChanged: (ElderResidenceType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        DefaultTextField(
            textFieldState = elderData.nameState,
            title = "이름",
            placeHolder = "성함을 입력해주세요",
        )
        Spacer(Modifier.height(20.dp))
        DefaultTextField(
            textFieldState = elderData.birthDateState,
            title = "생년월일",
            placeHolder = "0000 / 00 / 00",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            inputTransformation = InputTransformation.maxLength(8),
            outputTransformation = {
                val digits = asCharSequence().filter { it.isDigit() }.take(8).toString()

                if (digits.length > 4) {
                    insert(4, " / ")
                }
                if (digits.length > 6) {
                    insert(9, " / ") // 4 + 3(" / ") + 2 = 9
                }
            },
        )
        Spacer(Modifier.height(20.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                "성별",
                color = MediCareCallTheme.colors.gray7,
                style = MediCareCallTheme.typography.M_17,
            )
            GenderToggleButton(
                selectedGenderType = elderData.gender,
                onGenderChange = onGenderChanged,
            )
        }
        Spacer(Modifier.height(20.dp))
        DefaultTextField(
            textFieldState = elderData.phoneNumberState,
            title = "휴대폰 번호",
            placeHolder = "010-1111-1111",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            inputTransformation = InputTransformation.maxLength(11),
            outputTransformation = {
                val digits = asCharSequence().filter { it.isDigit() }.take(11).toString()

                if (digits.length > 3) {
                    insert(3, "-")
                }
                if (digits.length > 7) {
                    insert(8, "-") // 3 + 1("-") + 4 = 8
                }
            },
        )
        Spacer(Modifier.height(20.dp))

        DefaultDropdown(
            enumList = RelationshipType.entries.map { it.displayName },
            placeHolder = "어르신과의 관계를 선택해주세요",
            category = "어르신과의 관계",
            scrollState = scrollState,
            onOptionSelect = { displayName ->
                RelationshipType.entries.find { it.displayName == displayName }
                    ?.let { onRelationshipChange(it) }
            },
            value = elderData.relationship?.displayName ?: "",
        )

        Spacer(Modifier.height(20.dp))

        DefaultDropdown(
            enumList = ElderResidenceType.entries.map { it.displayName },
            placeHolder = "어르신의 거주방식을 선택해주세요",
            category = "어르신 거주 방식",
            scrollState = scrollState,
            onOptionSelect = { displayName ->
                ElderResidenceType.entries.find { it.displayName == displayName }
                    ?.let { onLivingTypeChanged(it) }
            },
            value = elderData.livingType?.displayName ?: "",
        )

        Spacer(Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun ElderInputFormPreview() {
    MediCareCallTheme {
        ElderInputForm(
            scrollState = rememberScrollState(),
            elderData = LoginElderData(
                nameState = TextFieldState("김어르신"),
                birthDateState = TextFieldState("19450101"),
                gender = GenderType.FEMALE,
                phoneNumberState = TextFieldState("01012345678"),
                relationship = RelationshipType.CHILD,
                livingType = ElderResidenceType.ALONE,
            ),
            onGenderChanged = {},
            onRelationshipChange = {},
            onLivingTypeChanged = {},
        )
    }
}
