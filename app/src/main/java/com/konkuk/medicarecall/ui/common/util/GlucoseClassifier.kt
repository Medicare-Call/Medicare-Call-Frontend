package com.konkuk.medicarecall.ui.common.util

import com.konkuk.medicarecall.ui.model.GlucoseTiming

enum class GlucoseLevel { LOW, NORMAL, HIGH }

private data class Range(val lowUpper: Float, val normalUpper: Float)

// 65세 이상 건강노인 기준 혈당 상태 구분
private fun rangeFor(timing: GlucoseTiming): Range = when (timing) {
    GlucoseTiming.BEFORE_MEAL -> Range(lowUpper = 90f, normalUpper = 130f) // 식전: <90 / 90~130 / >130
    GlucoseTiming.AFTER_MEAL -> Range(lowUpper = 90f, normalUpper = 150f) // 식후: <90 / 90~150 / >150
}

/** x: mg/dL 값 */
fun classifyGlucose(x: Float, timing: GlucoseTiming): GlucoseLevel {
    val r = rangeFor(timing)
    return when {
        x < r.lowUpper -> GlucoseLevel.LOW
        x <= r.normalUpper -> GlucoseLevel.NORMAL // 경계 포함
        else -> GlucoseLevel.HIGH
    }
}
