package com.konkuk.medicarecall.ui.common.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExtensionsTest {

    @Test
    fun testLeapYearFebruary29IsValid() {
        assertTrue("20240229".isValidDate()) // 2024년은 윤년
        assertTrue("20200229".isValidDate()) // 2020년은 윤년
        assertTrue("20000229".isValidDate()) // 2000년은 400의 배수로 윤년
    }

    @Test
    fun testNonLeapYearFebruary29IsInvalid() {
        assertFalse("20230229".isValidDate()) // 2023년은 평년
        assertFalse("20190229".isValidDate()) // 2019년은 평년
        assertFalse("21000229".isValidDate()) // 2100년은 100의 배수이지만 400의 배수가 아님 (미래 날짜)
    }

    @Test
    fun testNonLeapYearFebruary28IsValid() {
        assertTrue("20230228".isValidDate())
        assertTrue("20190228".isValidDate())
        assertTrue("19000228".isValidDate())
    }

    @Test
    fun testLeapYearFebruary30IsInvalid() {
        assertFalse("20240230".isValidDate())
        assertFalse("20200230".isValidDate())
    }

    @Test
    fun testInvalidLengthReturnsInvalid() {
        assertFalse("2024022".isValidDate()) // 7자리
        assertFalse("202402299".isValidDate()) // 9자리
        assertFalse("".isValidDate())
    }

    @Test
    fun testDateBefore1900IsInvalid() {
        assertFalse("18991231".isValidDate())
        assertFalse("18000101".isValidDate())
    }

    @Test
    fun testFutureDateIsInvalid() {
        assertFalse("20991231".isValidDate())
        assertFalse("20300101".isValidDate())
    }

    @Test
    fun testInvalidDateFormat() {
        assertFalse("abcdefgh".isValidDate())
        assertFalse("2024abcd".isValidDate())
        assertFalse("20241301".isValidDate()) // 13월
        assertFalse("20240132".isValidDate()) // 32일
    }

    @Test
    fun testLastDayOfEachMonth() {
        assertTrue("20240131".isValidDate()) // 1월 31일
        assertTrue("20240331".isValidDate()) // 3월 31일
        assertTrue("20240430".isValidDate()) // 4월 30일
        assertTrue("20240531".isValidDate()) // 5월 31일
        assertTrue("20240630".isValidDate()) // 6월 30일
        assertTrue("20240731".isValidDate()) // 7월 31일
        assertTrue("20240831".isValidDate()) // 8월 31일
        assertTrue("20240930".isValidDate()) // 9월 30일
        assertTrue("20241031".isValidDate()) // 10월 31일
        assertTrue("20241130".isValidDate()) // 11월 30일
        assertTrue("20241231".isValidDate()) // 12월 31일
    }
}
