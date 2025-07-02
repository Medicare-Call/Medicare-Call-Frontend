package com.konkuk.medicarecall.ui.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // 입력 문자열에서 숫자만 추출
        val digits = text.text.filter { it.isDigit() }.take(11)

        // 3-4-4 형태로 포맷팅
        val formatted = buildString {
            digits.forEachIndexed { index, c ->
                if (index == 3 || index == 7)
                    append('-')
                append(c)
            }
        }

        // 원본 문자열 인덱스 -> 포맷팅 문자열 인덱스 매핑
        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    offset <= 7 -> offset + 1
                    offset <= 11 -> offset + 2
                    else -> formatted.length
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    offset <= 8 -> offset - 1
                    offset <= 13 -> offset - 2
                    else -> digits.length
                }
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetTranslator)
    }
}
