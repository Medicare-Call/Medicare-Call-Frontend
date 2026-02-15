package com.konkuk.medicarecall.ui.feature.login.calltime.component

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.util.TypedValue
import android.widget.EditText
import android.widget.NumberPicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toDrawable
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun FirstTimeWheelPicker(
    modifier: Modifier = Modifier,
    initialHour: Int = 9,
    initialMinute: Int = 0,
    onTimeChange: (hour: Int, minute: Int) -> Unit = { _, _ -> },
) {
    var hour by remember { mutableIntStateOf(initialHour) }
    var minute by remember { mutableIntStateOf(initialMinute) }

    val mainColor = MediCareCallTheme.colors.main.toArgb()
    val minuteOptions = arrayOf("00", "10", "20", "30", "40", "50")

    val style = MediCareCallTheme.typography.M_20

    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 오전 / 오후
            AndroidView(
                factory = { ctx ->
                    NumberPicker(ctx).apply {
                        displayedValues = arrayOf("오전")
                        wrapSelectorWheel = false
//                        setOnValueChangedListener { _, _, newVal ->
//                            amPm = newVal
//                            onTimeChange(amPm, hour, minute)
//
//                            //setPickerTextStyle(mainColor, 20f, Typeface.DEFAULT_BOLD)
//                        }
                        setPickerTextColor(mainColor)
                        //                        for (i in 0 until childCount) {
//                            val child = getChildAt(i)
//                            if (child is EditText) {
//                                child.textSize = 20f
//                                child.typeface = Typeface.DEFAULT_BOLD
//                            }
//                        }
                        @SuppressLint("DiscouragedPrivateApi", "SoonBlockedPrivateApi", "UseKtx")
                        fun hideDividers() {
                            try {
                                // mSelectionDivider(드로어블)를 투명 Drawable 로 교체
                                val dividerField = NumberPicker::class.java
                                    .getDeclaredField("mSelectionDivider")
                                    .apply { isAccessible = true }
                                dividerField.set(
                                    this,
                                    Color.TRANSPARENT.toDrawable(),
                                )

                                // mSelectionDividerHeight(높이)를 0 으로
                                val heightField = NumberPicker::class.java
                                    .getDeclaredField("mSelectionDividerHeight")
                                    .apply { isAccessible = true }
                                heightField.setInt(this, 0)
                            } catch (e: Exception) {
                            }
                        }

                        hideDividers()
//                        for (i in 0 until childCount) {
//                            (getChildAt(i) as? EditText)?.apply {
//                                // Typeface.DEFAULT_BOLD 로 Bold 체 강제
//                                setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD)
//                            }
//                        }
                    }
                },
                update = { picker ->
                    @SuppressLint("DiscouragedPrivateApi", "SoonBlockedPrivateApi", "UseKtx")
                    fun hideDividers() {
                        try {
                            // mSelectionDivider(드로어블)를 투명 Drawable 로 교체
                            val dividerField = NumberPicker::class.java
                                .getDeclaredField("mSelectionDivider")
                                .apply { isAccessible = true }
                            dividerField.set(
                                this,
                                Color.TRANSPARENT.toDrawable(),
                            )

                            // mSelectionDividerHeight(높이)를 0 으로
                            val heightField = NumberPicker::class.java
                                .getDeclaredField("mSelectionDividerHeight")
                                .apply { isAccessible = true }
                            heightField.setInt(this, 0)
                        } catch (e: Exception) {
                        }
                    }
                    hideDividers()
//                    for (i in 0 until picker.childCount) {
//                        val child = picker.getChildAt(i)
//                        if (child is EditText) {
//                            child.textSize = 20f
//                            child.typeface = Typeface.DEFAULT_BOLD
//                        }
//                    }
//                    picker.setPickerTextStyle(mainColor, 20f, Typeface.DEFAULT_BOLD)
                },
            )
            Spacer(Modifier.width(32.dp))

            AndroidView(
                factory = { ctx ->
                    NumberPicker(ctx).apply {
                        minValue = 1; maxValue = 12
                        wrapSelectorWheel = false
                        setOnValueChangedListener { _, _, newVal ->
                            hour = newVal
                            onTimeChange(hour, minute)
//                            setPickerTextStyle(mainColor, 20f, Typeface.DEFAULT_BOLD)
                        }
                        setPickerTextColor(mainColor) // 텍스트 색상 변경
                        //                        for (i in 0 until childCount) {
//                            val child = getChildAt(i)
//                            if (child is EditText) {
//                                child.textSize = 20f
//                                child.typeface = Typeface.DEFAULT_BOLD
//                            }
//                        }
                        @SuppressLint("DiscouragedPrivateApi", "SoonBlockedPrivateApi")
                        fun hideDividers() {
                            try {
                                // mSelectionDivider(드로어블)를 투명 Drawable 로 교체
                                val dividerField = NumberPicker::class.java
                                    .getDeclaredField("mSelectionDivider")
                                    .apply { isAccessible = true }
                                dividerField.set(
                                    this,
                                    Color.TRANSPARENT.toDrawable(),
                                )

                                // mSelectionDividerHeight(높이)를 0 으로
                                val heightField = NumberPicker::class.java
                                    .getDeclaredField("mSelectionDividerHeight")
                                    .apply { isAccessible = true }
                                heightField.setInt(this, 0)
                            } catch (e: Exception) {
                            }
                        }
                        hideDividers()
//                        for (i in 0 until childCount) {
//                            (getChildAt(i) as? EditText)?.apply {
//                                // Typeface.DEFAULT_BOLD 로 Bold 체 강제
//                                setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD)
//                            }
//                        }
                    }
                },
                update = { picker ->
                    picker.value = hour
                    @SuppressLint("DiscouragedPrivateApi", "SoonBlockedPrivateApi", "UseKtx")
                    fun hideDividers() {
                        try {
                            // mSelectionDivider(드로어블)를 투명 Drawable 로 교체
                            val dividerField = NumberPicker::class.java
                                .getDeclaredField("mSelectionDivider")
                                .apply { isAccessible = true }
                            dividerField.set(
                                this,
                                Color.TRANSPARENT.toDrawable(),
                            )

                            // mSelectionDividerHeight(높이)를 0 으로
                            val heightField = NumberPicker::class.java
                                .getDeclaredField("mSelectionDividerHeight")
                                .apply { isAccessible = true }
                            heightField.setInt(this, 0)
                        } catch (e: Exception) {
                        }
                    }
                    hideDividers()
//                    for (i in 0 until picker.childCount) {
//                        val child = picker.getChildAt(i)
//                        if (child is EditText) {
//                            child.textSize = 20f
//                            child.typeface = Typeface.DEFAULT_BOLD
//                        }
//                    }
//                    picker.setPickerTextStyle(mainColor, 20f, Typeface.DEFAULT_BOLD)
                },
            )
            Spacer(modifier = Modifier.width(18.dp))
            Text(
                ":",
                style = MediCareCallTheme.typography.M_20,
                color = MediCareCallTheme.colors.main,
            )
            Spacer(modifier = Modifier.width(18.dp))

            // 분 (00, 10, 20, 30, 40, 50)
            AndroidView(
                factory = { ctx ->
                    NumberPicker(ctx).apply {
                        minValue = 0
                        maxValue = minuteOptions.lastIndex
                        displayedValues = minuteOptions
                        wrapSelectorWheel = false
                        setOnValueChangedListener { _, _, newValue ->
                            minute = newValue * 10 // Convert index to actual minute value
                            onTimeChange(hour, minute)
//                            setPickerTextStyle(mainColor, 20f, Typeface.DEFAULT_BOLD)
                        }
                        setPickerTextColor(mainColor)
                        //                        for (i in 0 until childCount) {
//                            val child = getChildAt(i)
//                            if (child is EditText) {
//                                child.textSize = 20f
//                                child.typeface = Typeface.DEFAULT_BOLD
//                            }
//                        }
                        @SuppressLint("DiscouragedPrivateApi", "SoonBlockedPrivateApi")
                        fun hideDividers() {
                            try {
                                // mSelectionDivider(드로어블)를 투명 Drawable 로 교체
                                val dividerField = NumberPicker::class.java
                                    .getDeclaredField("mSelectionDivider")
                                    .apply { isAccessible = true }
                                dividerField.set(
                                    this,
                                    Color.TRANSPARENT.toDrawable(),
                                )

                                // mSelectionDividerHeight(높이)를 0 으로
                                val heightField = NumberPicker::class.java
                                    .getDeclaredField("mSelectionDividerHeight")
                                    .apply { isAccessible = true }
                                heightField.setInt(this, 0)
                            } catch (e: Exception) {
                            }
                        }
                        hideDividers()
//                        for (i in 0 until childCount) {
//                            (getChildAt(i) as? EditText)?.apply {
//                                // Typeface.DEFAULT_BOLD 로 Bold 체 강제
//                                setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD)
//                            }
//                        }
                    }
                },
                update = { picker ->
                    picker.value = minuteOptions.indexOf(minute.toString().padStart(2, '0'))
                    @SuppressLint("DiscouragedPrivateApi", "SoonBlockedPrivateApi", "UseKtx")
                    fun hideDividers() {
                        try {
                            // mSelectionDivider(드로어블)를 투명 Drawable 로 교체
                            val dividerField = NumberPicker::class.java
                                .getDeclaredField("mSelectionDivider")
                                .apply { isAccessible = true }
                            dividerField.set(
                                this,
                                Color.TRANSPARENT.toDrawable(),
                            )

                            // mSelectionDividerHeight(높이)를 0 으로
                            val heightField = NumberPicker::class.java
                                .getDeclaredField("mSelectionDividerHeight")
                                .apply { isAccessible = true }
                            heightField.setInt(this, 0)
                        } catch (e: Exception) {
                        }
                    }
                    hideDividers()
//                    for (i in 0 until picker.childCount) {
//                        val child = picker.getChildAt(i)
//                        if (child is EditText) {
//                            child.textSize = 20f
//                            child.typeface = Typeface.DEFAULT_BOLD
//                        }
//                    }
//                    picker.setPickerTextStyle(mainColor, 20f, Typeface.DEFAULT_BOLD)
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeWheelPreview() {
    FirstTimeWheelPicker(
        modifier = Modifier
            .fillMaxWidth()
            .height(206.dp),
    )
}

@SuppressLint("DiscouragedPrivateApi")
fun NumberPicker.setPickerTextColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // API 29 이상에서는 공식 setTextColor() 사용
        this.setTextColor(color)
    } else {
        // API 28 이하에서는 private field 접근
        try {
            // mSelectorWheelPaint 페인트 색 변경
            val selectorWheelPaintField = NumberPicker::class.java
                .getDeclaredField("mSelectorWheelPaint")
                .apply { isAccessible = true }

            val paint = selectorWheelPaintField.get(this) as Paint
            paint.color = color

            // 내부 EditText 들의 텍스트 색도 변경
            for (i in 0 until childCount) {
                (getChildAt(i) as? EditText)?.setTextColor(color)
            }

            // 다시 그리기
            invalidate()
        } catch (e: Exception) {
            // 리플렉션 실패 시 무시
        }
    }
}

@SuppressLint("DiscouragedPrivateApi")
fun NumberPicker.setPickerTextStyle(color: Int, textSizeSp: Float, typeface: Typeface) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this.setTextColor(color)
    } else {
        try {
            // mSelectorWheelPaint 설정
            val selectorWheelPaintField = NumberPicker::class.java
                .getDeclaredField("mSelectorWheelPaint")
                .apply { isAccessible = true }

            val paint = selectorWheelPaintField.get(this) as Paint
            paint.color = color
            paint.textSize = textSizeSp * context.resources.displayMetrics.scaledDensity
            paint.typeface = typeface

            // 내부 EditText 설정
            for (i in 0 until childCount) {
                (getChildAt(i) as? EditText)?.apply {
                    setTextColor(color)
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSp)
                    setTypeface(typeface)
                }
            }

            invalidate()
        } catch (e: Exception) {
            // 리플렉션 실패 시 무시
        }
    }
}
