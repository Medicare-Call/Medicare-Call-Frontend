package com.konkuk.medicarecall.domain.model

import com.konkuk.medicarecall.ui.type.GenderType

data class Elder(
    val id: Long = 0L,
    val name: String = "",
    val birthDate: String = "",
    val gender: GenderType = GenderType.MALE,
    val phoneNumber: String = "",
    val relationship: RelationshipType = RelationshipType.CHILD,
    val residenceType: ElderResidenceType = ElderResidenceType.ALONE,
    val diseases: List<String> = emptyList(),
    val medication: List<Medication> = emptyList(),
    val notes: List<ElderNote> = emptyList(),
) {
    // 자식, 손자, 형제, 친척, 지인
    enum class RelationshipType(val displayName: String) {
        CHILD("자식"),
        GRANDCHILD("손자"),
        SIBLING("형제"),
        RELATIVE("친척"),
        ACQUAINTANCE("지인"),
        ;

        companion object {
            fun fromString(value: String): RelationshipType {
                return RelationshipType.entries.find { it.displayName == value || it.name == value } ?: CHILD
            }
        }
    }

    // 혼자계세요, 가족과 함께 살아요
    enum class ElderResidenceType(val displayName: String) {
        ALONE("혼자 계세요"),
        WITH_FAMILY("가족과 함께 살아요"),
        ;
        companion object {
            fun fromString(value: String): ElderResidenceType {
                return ElderResidenceType.entries.find { it.displayName == value || it.name == value } ?: ALONE
            }
        }
    }

    data class Medication(
        val medicine: String = "",
        val time: MedicationTime = MedicationTime.BREAKFAST,
    )

    // 아침, 점심, 저녁 enum
    enum class MedicationTime(val displayName: String) {
        BREAKFAST("아침"),
        LUNCH("점심"),
        DINNER("저녁"),
    }

    // 특이사항 enum
    enum class ElderNote(val displayName: String) {
        INSOMNIA("불면증 / 수면장애"),
        FORGETS_MEDICATION("약 자주 잊음"),
        MOBILITY_ISSUE("보행 불편"),
        HEARING_LOSS("청력 저하"),
        COGNITIVE_DECLINE("인지저하 의심"),
        MOOD_SWINGS("감정기복"),
        RECENT_SPOUSE_DEATH("최근 배우자 사망"),
        SMOKING("흡연"),
        DRINKING("음주"),
        ALCOHOLISM("알콜중독"),
        VISUAL_IMPAIRMENT("시각장애"),
    }
}
