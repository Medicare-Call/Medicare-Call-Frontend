package com.konkuk.medicarecall.ui.feature.login.senior.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.common.component.DefaultDropdown
import com.konkuk.medicarecall.ui.common.component.DefaultTextField
import com.konkuk.medicarecall.ui.common.component.GenderToggleButton
import com.konkuk.medicarecall.ui.common.util.DateOfBirthVisualTransformation
import com.konkuk.medicarecall.ui.common.util.PhoneNumberVisualTransformation
import com.konkuk.medicarecall.ui.model.ElderData
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.ElderResidenceType
import com.konkuk.medicarecall.ui.type.RelationshipType

@Composable
fun ElderInputForm(
    scrollState: ScrollState,
    elderData: ElderData,
    onNameChanged: (String) -> Unit,
    onBirthDateChanged: (String) -> Unit,
    onGenderChanged: (Boolean) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onRelationshipChange: (String) -> Unit,
    onLivingTypeChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    nameFocusRequester: FocusRequester? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        DefaultTextField(
            value = elderData.name,
            onValueChange = { input ->
                onNameChanged(input)
            },
            category = "이름",
            placeHolder = "성함을 입력해주세요",
            textFieldModifier = if (nameFocusRequester != null)
                Modifier.focusRequester(nameFocusRequester)
            else Modifier,
        )
        Spacer(Modifier.height(20.dp))
        DefaultTextField(
            elderData.birthDate,
            { input ->
                val filtered = input.filter { it.isDigit() }.take(8)
                onBirthDateChanged(filtered)
            },
            category = "생년월일",
            placeHolder = "0000 / 00 / 00",
            keyboardType = KeyboardType.Number,
            visualTransformation = DateOfBirthVisualTransformation(),
            maxLength = 8,
        )
        Spacer(Modifier.height(20.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                "성별",
                color = MediCareCallTheme.colors.gray7,
                style = MediCareCallTheme.typography.M_17,
            )

            GenderToggleButton(elderData.gender) {
                onGenderChanged(it)
            }
        }
        Spacer(Modifier.height(20.dp))
        DefaultTextField(
            elderData.phoneNumber,
            { input ->
                val filtered = input.filter { it.isDigit() }.take(11)
                onPhoneNumberChanged(filtered)
            },
            category = "휴대폰 번호",
            placeHolder = "010-1111-1111",
            keyboardType = KeyboardType.Number,
            visualTransformation = PhoneNumberVisualTransformation(),
            maxLength = 11,
        )
        Spacer(Modifier.height(20.dp))

        DefaultDropdown(
            enumList = RelationshipType.entries.map { it.displayName }
                .toList(),
            placeHolder = "어르신과의 관계를 선택해주세요",
            category = "어르신과의 관계",
            scrollState,
            { onRelationshipChange(it) },
            elderData.relationship,
        )

        Spacer(Modifier.height(20.dp))

        DefaultDropdown(
            enumList = ElderResidenceType.entries.map { it.displayName }
                .toList(),
            placeHolder = "어르신의 거주방식을 선택해주세요",
            category = "어르신 거주 방식",
            scrollState,
            { onLivingTypeChanged(it) },
            elderData.livingType,
        )

        Spacer(Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ElderInputFormPreview() {
    ElderInputForm(
        scrollState = rememberScrollState(),
        elderData = ElderData(
            name = "김어르신",
            birthDate = "19450101",
            gender = true,
            phoneNumber = "01012345678",
            relationship = "자녀",
            livingType = "혼자 거주",
        ),
        onNameChanged = {},
        onBirthDateChanged = {},
        onGenderChanged = {},
        onPhoneNumberChanged = {},
        onRelationshipChange = {},
        onLivingTypeChanged = {},
    )
}
