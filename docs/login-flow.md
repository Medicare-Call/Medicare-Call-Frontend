# 로그인 플로우 분석

## 전체 흐름

```
Splash → LoginStart → LoginPhone → LoginVerification
    → LoginMyInfo → LoginElderInfo → LoginElderMedInfo
    → CallTimeScreen → LoginFinish → Home
```

---

## UiState 존재 여부

| 패키지 | 화면 | ViewModel | UiState 파일 |
| ------ | ---- | --------- | ------------ |
| myinfo | LoginStartScreen | LoginInfoViewModel | LoginInfoUiState.kt (공유) |
| myinfo | LoginPhoneScreen | LoginInfoViewModel | LoginInfoUiState.kt (공유) |
| myinfo | LoginVerificationScreen | LoginInfoViewModel | LoginInfoUiState.kt (공유) |
| myinfo | LoginMyInfoScreen | LoginInfoViewModel | LoginInfoUiState.kt (공유) |
| elder | LoginElderInfoScreen | LoginElderViewModel | LoginElderUiState.kt |
| elder | LoginElderMedInfoScreen | LoginElderViewModel | LoginElderHealthUiState.kt |
| calltime | CallTimeScreen | CallTimeViewModel | CallTimeUiState.kt |
| payment | LoginFinishScreen | 없음 (stateless) | 없음 |

> 모든 화면에 UiState가 존재함. LoginFinishScreen은 상태 없는 완료 화면이라 해당 없음.

### UiState 외부에서 관리되는 값

#### LoginInfoViewModel

| 변수 | 타입 | 용도 |
| ---- | ---- | ---- |
| `debug` | `private val Boolean = false` | 디버그 모드 플래그 |
| `isVerified` | `var Boolean` (public) | 인증 성공 여부 (`confirmPhoneNumber` onSuccess에서 설정) |
| `token` | `var String` (private set) | 회원가입용 임시 토큰 (`confirmPhoneNumber`에서 저장 → `memberRegister`에서 사용) |
| `_events` | `MutableSharedFlow<LoginEvent>` | 일회성 이벤트 스트림 |

> `isVerified`, `token`은 UiState가 아닌 ViewModel 일반 프로퍼티로 관리됨.

#### LoginElderViewModel

UiState 외 값 없음. 모든 상태가 `_elderUiState`, `_elderHealthUiState`에 포함.

#### CallTimeViewModel

UiState 외 값 없음. 모든 상태가 `_uiState`에 포함.

---

## 패키지별 API 정리

### myinfo (LoginStart / LoginPhone / LoginVerification / LoginMyInfo)

| 화면 | Repository | 메서드 | API Service | 엔드포인트 |
| ---- | ---------- | ------ | ----------- | ---------- |
| LoginPhone | VerificationRepository | `requestCertificationCode(phone)` | AuthService | `POST /verifications` |
| LoginVerification | VerificationRepository | `confirmPhoneNumber(phone, code)` | AuthService | `POST /verifications/confirmation` |
| LoginVerification | DataStoreRepository | `saveAccessToken()` / `saveRefreshToken()` | (로컬 DataStore) | — |
| LoginVerification | FcmRepository | `validateAndRefreshTokenIfNeeded(jwt)` | FcmValidationService | `POST /notifications/validation-token` |
| LoginVerification | FcmRepository | (내부) | FcmUpdateService | (FCM 토큰 갱신) |
| LoginMyInfo | MemberRegisterRepository | `registerMember(token, name, birthDate, gender, fcmToken)` | MemberRegisterService | `POST /members` |
| LoginMyInfo | DataStoreRepository | `saveAccessToken()` / `saveRefreshToken()` | (로컬 DataStore) | — |
| LoginStart | CheckLoginStatusUseCase | `invoke()` | (아래 GET 3개 호출) | 상태 판별 |

#### CheckLoginStatusUseCase 내부 GET 호출

| 순서 | Repository | 메서드 | API Service | 엔드포인트 | 분기 |
| ---- | ---------- | ------ | ----------- | ---------- | ---- |
| 1 | EldersInfoRepository | `getElders()` | EldersInfoService | `GET /elders` | 비어있으면 → GoToRegisterElder |
| 2 | EldersInfoRepository | `getCareCallTimes(elderId)` | EldersInfoService | `GET /elders/{elderId}/care-call-setting` | 404 → GoToTimeSetting |
| 3 | EldersInfoRepository | `getSubscriptions()` | EldersInfoService | `GET /elders/subscriptions` | 비어있으면 → GoToPayment |

> 1→2→3 순서대로 호출. 모두 통과하면 `GoToHome`. 에러 발생 시 `GoToLogin`.
> 1번 성공 시 어르신 ID를 `ElderIdRepository.updateElderIds()`로 로컬 저장.

### elder (LoginElderInfo / LoginElderMedInfo)

| 화면 | Repository | 메서드 | API Service | 엔드포인트 |
| ---- | ---------- | ------ | ----------- | ---------- |
| LoginElderInfo | ElderRegisterRepository | `postElderBulk(elderList)` | ElderRegisterService | `POST /elders/bulk` |
| LoginElderMedInfo | ElderRegisterRepository | `postElderHealthInfoBulk(elderHealthList)` | ElderRegisterService | `POST /elders/health-info/bulk` |

### calltime (CallTimeScreen)

| 화면 | Repository | 메서드 | API Service | 엔드포인트 |
| ---- | ---------- | ------ | ----------- | ---------- |
| CallTimeScreen | ElderIdRepository | `getElderIds()` | (로컬 DataStore) | — |
| CallTimeScreen | SetCallRepository | `saveForElder(elderId, times)` | SetCallService | `POST /elders/{elderId}/care-call-setting` |

### finish (LoginFinishScreen)

| 화면 | Repository | 메서드 | API Service | 엔드포인트 |
| ---- | ---------- | ------ | ----------- | ---------- |
| — | — | — | — | API 호출 없음 (stateless UI) |

---

## Response DTO

### VerificationResponseDto (인증번호 확인 응답)

```kotlin
@Serializable
data class VerificationResponseDto(
    val verified: Boolean,
    val message: String,
    val memberStatus: String,       // "EXISTING_MEMBER" | "NEW_MEMBER"
    val token: String?,             // 회원가입용 임시 토큰
    val accessToken: String?,
    val refreshToken: String?,
)
```

### MemberTokenResponseDto (회원가입 응답)

```kotlin
@Serializable
data class MemberTokenResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Long,
)
```

### ElderBulkRegisterResponseDto (어르신 일괄등록 응답)

```kotlin
typealias ElderBulkRegisterResponseDto = List<ElderInfo>

@Serializable
data class ElderInfo(
    val id: Int,
    val name: String,
    val birthDate: String,
    val phone: String,
    val gender: String,
    val relationship: String,
    val residenceType: String,
    val guardianId: Int,
    val guardianName: String,
)
```

### CallTimeResponseDto (돌봄전화 시간 응답)

```kotlin
@Serializable
data class CallTimeResponseDto(
    val firstCallTime: String,
    val secondCallTime: String,
    val thirdCallTime: String,
)
```

---

## Request DTO

### CertificationCodeRequestDto

```kotlin
@Serializable
data class CertificationCodeRequestDto(
    val phone: String,
)
```

### PhoneNumberConfirmRequestDto

```kotlin
@Serializable
data class PhoneNumberConfirmRequestDto(
    val phone: String,
    val certificationCode: String,
)
```

### MemberRegisterRequestDto

```kotlin
@Serializable
data class MemberRegisterRequestDto(
    val name: String,
    val birthDate: String,
    val gender: GenderType,    // MALE, FEMALE
    val fcmToken: String,
)
```

### ElderBulkRegisterRequestDto

```kotlin
@Serializable
data class ElderBulkRegisterRequestDto(
    val elders: List<ElderInfo>,
) {
    @Serializable
    data class ElderInfo(
        val name: String,
        val birthDate: String,
        val gender: String,
        val phone: String,
        val relationship: String,
        val residenceType: String,
    )
}
```

### ElderBulkHealthInfoRequestDto

```kotlin
@Serializable
data class ElderBulkHealthInfoRequestDto(
    val healthInfos: List<HealthInfo>,
) {
    @Serializable
    data class HealthInfo(
        val elderId: Int,
        val diseaseNames: List<String>,
        val medicationSchedules: List<MedicationSchedule>,
        val notes: List<String>,
    ) {
        @Serializable
        data class MedicationSchedule(
            val medicationName: String,
            val scheduleTimes: List<String>,
        )
    }
}
```

### SetCallTimeRequestDto

```kotlin
@Serializable
data class SetCallTimeRequestDto(
    val firstCallTime: String,   // "HH:mm" (24시간)
    val secondCallTime: String,
    val thirdCallTime: String,
)
```

---

## ViewModel onSuccess 블록 상세

### LoginInfoViewModel

#### 1. postPhoneNumber() → `requestCertificationCode`

```kotlin
.onSuccess { Log.d("httplog", "인증번호 요청 성공, ${it.message}") }
.onFailure { Log.e("httplog", "인증번호 요청 실패: ${it.message}") }
```

- 사용 필드: `it.message` (로그만)

#### 2. confirmPhoneNumber() → `confirmPhoneNumber`

```kotlin
.onSuccess {
    isVerified = it.verified
    if (isVerified) {
        token = it.token ?: ""
        dataStoreRepository.saveAccessToken(it.accessToken ?: "")
        dataStoreRepository.saveRefreshToken(it.refreshToken ?: "")

        it.accessToken?.let { jwt ->
            fcmRepository.validateAndRefreshTokenIfNeeded(jwt)
        }
    }

    if (it.memberStatus == "EXISTING_MEMBER")
        _events.emit(LoginEvent.VerificationSuccessExisting)
    else
        _events.emit(LoginEvent.VerificationSuccessNew)
}
.onFailure {
    _events.emit(LoginEvent.VerificationFailure)
}
```

- 사용 필드: `verified`, `token`, `accessToken`, `refreshToken`, `memberStatus`
- 부수효과: DataStore 토큰 저장, FCM 토큰 검증, 이벤트 발행

#### 3. memberRegister() → `registerMember`

```kotlin
.onSuccess {
    dataStoreRepository.saveAccessToken(it.accessToken)
    dataStoreRepository.saveRefreshToken(it.refreshToken)
    _events.emit(LoginEvent.MemberRegisterSuccess)
}
.onFailure {
    _events.emit(LoginEvent.MemberRegisterFailure)
}
```

- 사용 필드: `accessToken`, `refreshToken`
- 부수효과: DataStore 토큰 저장, 이벤트 발행

---

### LoginElderViewModel

#### 1. postElderBulk() → `postElderBulk`

```kotlin
.onSuccess { response ->
    _elderUiState.update { state ->
        state.copy(
            eldersList = state.eldersList.mapIndexed { index, elderData ->
                elderData.copy(id = response[index].id)
            },
        )
    }
    _elderHealthUiState.update { state ->
        state.copy(
            elderHealthList = state.elderHealthList.mapIndexed { index, healthData ->
                healthData.copy(id = response[index].id)
            },
        )
    }
}
.onFailure { exception ->
    when (exception) {
        is HttpException -> Log.e(...)
    }
}
```

- 사용 필드: `response[index].id` (각 어르신에 할당된 서버 ID)
- 부수효과: `eldersList`와 `elderHealthList` 모두 ID 업데이트

#### 2. postElderHealthInfoBulk() → `postElderHealthInfoBulk`

```kotlin
.onSuccess { Log.d("elderHealthRegister", "Success") }
.onFailure { exception ->
    when (exception) {
        is HttpException -> Log.e(...)
    }
}
```

- 사용 필드: 없음 (Unit 반환)

---

### CallTimeViewModel

#### 1. submitAllByIds() → `saveForElder` (어르신별 병렬)

```kotlin
// 병렬 요청
val jobs = elderIds.map { id ->
    val times = uiState.value.timeMap[id] ?: error("...")
    async {
        setCallRepository.saveForElder(id, times).getOrThrow()
    }
}
jobs.awaitAll()
onSuccess()   // 콜백
```

- 사용 필드: 없음 (Unit 반환, getOrThrow로 에러 전파)
- 부수효과: `isLoading` 상태 토글, `error` 상태 업데이트, 콜백 호출

---

## 기존 사용자 분기 로직 (CheckLoginStatusUseCase)

| 상태 | 이동 대상 |
| ---- | --------- |
| 어르신 미등록 | LoginElderInfo |
| 돌봄전화 미설정 (404) | CallTimeScreen |
| 구독 미완료 | LoginFinish |
| 모두 완료 | Home |
| 에러 | LoginStart |

---

## 입력 검증 규칙

| 필드 | 규칙 |
| ---- | ---- |
| 전화번호 | `010` 시작, 숫자만, 11자리 |
| 이름 | 한글(`가-힣`) 또는 영문(`a-zA-Z`)만 |
| 생년월일 | `YYYYMMDD` 형식, 유효 날짜 체크 |
| 인증번호 | 숫자만, 정확히 6자리 |

---

## Navigation Routes

```kotlin
Route.LoginStart
Route.LoginPhone
Route.LoginVerification
Route.LoginRegisterUserInfo
Route.LoginRegisterElder
Route.LoginRegisterElderHealth
Route.LoginCareCallSetting
Route.LoginFinish
```

---

# 참고: Sleep 기능 구조 (HomeDetail)

> 아래는 UiState → ViewModel → Repository → API → Mapper 구조의 참고 예시

### 데이터 흐름

```
SleepService (API)
  ↓ Response<SleepResponseDto>
handleResponse() — HTTP 검증
  ↓ SleepResponseDto
toModel() — DTO → Domain 매핑
  ↓ Sleep
SleepRepositoryImpl — Result<Sleep> 래핑
  ↓
SleepViewModel — StateFlow<SleepUiState> 갱신
  ↓
SleepDetailScreen / SleepDetailCard — UI 렌더링
```

### UiState

```kotlin
// SleepUiState.kt
data class SleepUiState(
    val date: String = "",
    val totalSleepHours: Int? = null,
    val totalSleepMinutes: Int? = null,
    val bedTime: String? = null,
    val wakeUpTime: String? = null,
    val isRecorded: Boolean = bedTime != null && wakeUpTime != null,
)
```

### ViewModel

```kotlin
// SleepViewModel.kt
@KoinViewModel
class SleepViewModel(
    private val sleepRepository: SleepRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _uiState = MutableStateFlow(SleepUiState())
    val uiState: StateFlow<SleepUiState> = _uiState.asStateFlow()

    private val elderId = savedStateHandle.toRoute<Route.SleepDetail>().elderId

    fun selectDate(date: LocalDate) { _selectedDate.value = date }
    fun resetToToday() { _selectedDate.value = LocalDate.now() }

    fun loadSleepDataForDate(date: LocalDate) {
        viewModelScope.launch {
            sleepRepository.getSleepData(elderId, date)
                .onSuccess { data ->
                    _uiState.update {
                        SleepUiState(
                            date = data.date,
                            totalSleepHours = data.totalSleepHours,
                            totalSleepMinutes = data.totalSleepMinutes,
                            bedTime = data.bedTime,
                            wakeUpTime = data.wakeUpTime,
                        )
                    }
                }
                .onFailure { Log.e("SleepViewModel", "Error", it) }
        }
    }
}
```

### Repository

```kotlin
// SleepRepository.kt (인터페이스)
interface SleepRepository {
    suspend fun getSleepData(elderId: Int, date: LocalDate): Result<Sleep>
}

// SleepRepositoryImpl.kt (구현)
@Single
class SleepRepositoryImpl(
    private val sleepService: SleepService,
) : SleepRepository {
    override suspend fun getSleepData(elderId: Int, date: LocalDate): Result<Sleep> =
        runCatching {
            sleepService.getDailySleep(elderId, date.toString())
                .handleResponse()
                .toModel()
        }
}
```

### API Service

```kotlin
// SleepService.kt
interface SleepService {
    @GET("elders/{elderId}/sleep")
    suspend fun getDailySleep(
        @Path("elderId") elderId: Int,
        @Query("date") date: String,
    ): Response<SleepResponseDto>
}
```

### DTO

```kotlin
// SleepResponseDto.kt
@Serializable
data class SleepResponseDto(
    val date: String,
    val totalSleep: TotalSleepDto?,
    val sleepTime: String?,
    val wakeTime: String?,
)

@Serializable
data class TotalSleepDto(
    val hours: Int?,
    val minutes: Int?,
)
```

### Domain Model

```kotlin
// Sleep.kt
data class Sleep(
    val date: String = "",
    val totalSleepHours: Int? = null,
    val totalSleepMinutes: Int? = null,
    val bedTime: String? = null,
    val wakeUpTime: String? = null,
)
```

### Mapper

```kotlin
// HomeMapper.kt
fun SleepResponseDto.toModel() = Sleep(
    date = this.date,
    totalSleepHours = this.totalSleep?.hours,
    totalSleepMinutes = this.totalSleep?.minutes,
    bedTime = formatTime(this.sleepTime),
    wakeUpTime = formatTime(this.wakeTime),
)

private fun formatTime(timeStr: String?): String {
    if (timeStr.isNullOrBlank() || !timeStr.contains(":")) return ""
    return try {
        val parsedTime = LocalTime.parse(timeStr)
        parsedTime.format(DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREAN))
    } catch (e: DateTimeParseException) {
        ""
    }
}
```

### ResponseHandler 유틸

```kotlin
// ResponseHandler.kt
fun <T> Response<T>.handleResponse(): T {
    if (isSuccessful) return body() ?: error("응답값이 null 입니다.")
    else throw HttpException(this)
}
```