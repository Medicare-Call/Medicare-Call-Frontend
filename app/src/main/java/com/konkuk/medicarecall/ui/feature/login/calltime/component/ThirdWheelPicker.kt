package com.konkuk.medicarecall.ui.feature.login.calltime.component

import android.annotation.SuppressLint
import android.graphics.Color
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
fun ThirdTimeWheelPicker(
    modifier: Modifier = Modifier,
    initialHour: Int = 5,
    initialMinute: Int = 0,
    onTimeChange: (hour: Int, minute: Int) -> Unit = { _, _ -> },
) {
    var hour by remember { mutableIntStateOf(initialHour) }
    var minute by remember { androidx.compose.runtime.mutableIntStateOf(initialMinute) }

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
                        displayedValues = arrayOf("오후")
                        wrapSelectorWheel = false
//                        setOnValueChangedListener { _, _, newVal ->
//                            onTimeChange(hour, minute)
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
                        minValue = 5; maxValue = 11
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
    ThirdTimeWheelPicker(
        modifier = Modifier
            .fillMaxWidth()
            .height(206.dp),
    )
}
