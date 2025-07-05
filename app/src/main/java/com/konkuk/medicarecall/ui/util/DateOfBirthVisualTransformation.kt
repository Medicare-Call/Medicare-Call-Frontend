package com.konkuk.medicarecall.ui.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class DateOfBirthVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // 입력 문자열에서 8자리까지만 추출
        val digits = text.text.filter { it.isDigit() }.take(8)

        // YYYY / MM / DD 형태로 포맷팅
        val formatted = buildString {
            digits.forEachIndexed { index, c ->
                if (index == 4 || index == 6) {
                    append(' ')
                    append('/')
                    append(' ')
                }

                append(c)
            }
        }

        // 원본 문자열 인덱스 -> 포맷팅 문자열 인덱스 매핑
        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // offset은 원본 문자열(digits)의 커서 위치 (0 ~ digits.length)

                // 삽입된 구분자의 개수를 계산 (커서 위치에 따라 달라짐)
                var transformedOffset = offset

                // YYYY 뒤에 ' / '가 추가되는 경우
                if (offset >= 5) { // 원본 4번째 문자(인덱스 3)를 지나서 커서가 4 이상일 때
                    transformedOffset += 3 // ' / ' (3칸) 추가
                }
                // MM 뒤에 ' / '가 추가되는 경우
                if (offset >= 7) { // 원본 6번째 문자(인덱스 5)를 지나서 커서가 6 이상일 때
                    transformedOffset += 3 // ' / ' (3칸) 추가
                }

                // 변환된 문자열의 최대 길이를 넘지 않도록
                return transformedOffset.coerceAtMost(formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                // offset은 변환된 문자열(formatted)의 커서 위치 (0 ~ formatted.length)

                // 변환된 문자열에서 구분자를 역으로 제거
                var originalOffset = offset

                // 두 번째 ' / '를 제거
                if (offset >= 9) { // YYYY / MM / 의 ' / ' (두 번째) 시작 위치 (인덱스 9) 이후
                    originalOffset -= 3 // ' / ' (3칸) 제거
                }
                // 첫 번째 ' / '를 제거
                if (offset >= 5) { // YYYY / 의 ' / ' (첫 번째) 시작 위치 (인덱스 5) 이후
                    originalOffset -= 3 // ' / ' (3칸) 제거
                }

                // 원본 문자열의 최대 길이를 넘지 않도록
                return originalOffset.coerceAtMost(digits.length)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetTranslator)
    }

}