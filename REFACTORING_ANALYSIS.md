# Medicare-Call-Android 프로젝트 종합 분석 보고서

> 분석일: 2025년 1월 14일
> 분석 대상: 293개 Kotlin 파일, 22,855줄

---

## 목차
1. [프로젝트 개요](#1-프로젝트-개요)
2. [Data Layer](#2-data-layer)
3. [Domain Layer](#3-domain-layer)
4. [UI Layer (공통)](#4-ui-layer-공통)
5. [Feature: Home](#5-feature-home)
6. [Feature: Login](#6-feature-login)
7. [Feature: Statistics](#7-feature-statistics)
8. [Feature: Settings](#8-feature-settings)
9. [Feature: HomeDetail](#9-feature-homedetail)
10. [Navigation](#10-navigation)
11. [우선순위별 작업 목록](#11-우선순위별-작업-목록)

---

## 1. 프로젝트 개요

### 1.1 프로젝트 규모
| 항목 | 수치 |
|------|------|
| 총 코드 라인 | 22,855줄 |
| Kotlin 파일 | 293개 |
| ViewModel | 18개 |
| Repository | 22개 |
| 테스트 코드 | 3개 (극히 부족) |

### 1.2 아키텍처 패턴
- **MVVM + Clean Architecture** 기반
- UI Layer: Jetpack Compose
- DI: Koin (KSP 기반 자동 생성)
- Network: Retrofit + Ktorfit (이중 사용 - 문제점)

### 1.3 패키지 구조
```
├── data
│   ├── api           # Retrofit/Ktorfit 서비스
│   ├── di            # Dependency Injection
│   ├── dto           # Request/Response DTO
│   ├── network       # Interceptor, Authenticator
│   ├── repository    # 인터페이스 (22개)
│   ├── repositoryimpl # 구현체 (22개)
│   └── util
├── domain
│   └── usecase
├── ui
│   ├── feature       # 화면별 모듈
│   │   ├── home
│   │   ├── login
│   │   ├── statistics
│   │   ├── settings
│   │   └── homedetail
│   ├── common        # 공통 컴포넌트
│   ├── navigation    # NavGraph
│   └── theme
```

---

## 2. Data Layer

### 2.1 이중 네트워크 라이브러리 🔴 Critical

**현재 상태:**
```
data/di/ApiModule.kt      → Retrofit
data/di/KtorApiModule.kt  → Ktorfit
```

**영향:**
- APK 크기 약 2MB 증가
- 유지보수 복잡성 증가

**권장:** Ktorfit으로 완전 통합

---

### 2.2 위험한 Null 처리

#### 2.2.1 ElderRegisterRepositoryImpl 🔴 Critical

**파일:** `data/repositoryimpl/ElderRegisterRepositoryImpl.kt`
**라인:** 34, 35, 53, 72, 73, 90, 99

```kotlin
RelationshipType.entries.find { it.displayName == relationship }!!
ElderResidenceType.entries.find { it.displayName == residence }!!
```

**개선:**
```kotlin
RelationshipType.entries.find { it.displayName == relationship }
    ?: RelationshipType.SPOUSE  // 기본값 설정
```

---

#### 2.2.2 AuthAuthenticator 🔴 Critical

**파일:** `data/network/AuthAuthenticator.kt:52`

```kotlin
refreshResponse.body()!!
```

**개선:**
```kotlin
refreshResponse.body()?.let { body ->
    // 처리
} ?: return null
```

---

#### 2.2.3 HealthRepositoryImpl 🟠 Medium

**파일:** `data/repositoryimpl/HealthRepositoryImpl.kt`

```kotlin
response.symptomList!!
```

**개선:** `?.isNotEmpty() == true`

---

### 2.3 Network 레이어 취약점 🟠 High

**파일:** `data/network/AuthInterceptor.kt:23`

```kotlin
// 문제: runBlocking 사용으로 스레드 차단
val accessToken = runBlocking {
    dataStoreRepository.getAccessToken()
} ?: ""
```

**해결책:** Ktor의 비동기 인터셉터 사용

---

### 2.4 중복되는 Enum 매핑 로직

**파일:**
- `data/repositoryimpl/ElderRegisterRepositoryImpl.kt`
- `data/repositoryimpl/EldersInfoRepositoryImpl.kt`

**문제:** `RelationshipType`, `ElderResidenceType`, `HealthIssueType` 매핑이 반복

**해결책:**
```kotlin
// data/mapper/TypeMapper.kt 생성
object TypeMapper {
    fun toRelationshipType(displayName: String): RelationshipType =
        RelationshipType.entries.find { it.displayName == displayName }
            ?: RelationshipType.SPOUSE
}
```

---

### 2.5 Repository 구조 개선

**현재:**
```
/data/repository/      ← 인터페이스만 (22개)
/data/repositoryimpl/  ← 구현체 (22개)
```

**개선:** 파일 통합 권장
```kotlin
// 하나의 파일에 interface + @Single class
@Single
class UserRepositoryImpl(...) : UserRepository { ... }
```

---

### 2.6 중복 의존성

**파일:** `app/build.gradle.kts`
```gradle
// Line 136-137 (중복)
implementation("de.jensklingenberg.ktorfit:ktorfit-lib:2.7.1")
// 이미 Line 114-115에 정의됨
```

---

### 2.7 Data Layer 작업 목록

| 우선순위 | 작업 | 파일 |
|---------|------|------|
| 🔴 Critical | `!!` 연산자 제거 | `ElderRegisterRepositoryImpl.kt` |
| 🔴 Critical | `!!` 연산자 제거 | `AuthAuthenticator.kt` |
| 🟠 High | runBlocking 제거 | `AuthInterceptor.kt` |
| 🟠 High | Retrofit 제거, Ktorfit 통합 | `ApiModule.kt`, `KtorApiModule.kt` |
| 🟡 Medium | Enum Mapper 추상화 | 신규 `TypeMapper.kt` 생성 |
| 🟡 Medium | Repository 구조 단순화 | 전체 repository 파일 |
| 🟢 Low | 중복 의존성 제거 | `build.gradle.kts` |

---

## 3. Domain Layer

### 3.1 현재 상태

Domain 레이어는 `usecase` 패키지만 존재하며, 비교적 단순한 구조

### 3.2 개선 사항

- UseCase 패턴이 일관되게 적용되지 않음
- 일부 비즈니스 로직이 Repository나 ViewModel에 혼재

---

## 4. UI Layer (공통)

### 4.1 Compose 리컴포지션 최적화 부재

**18개의 `by mutableStateOf` 선언 발견**

```kotlin
// Before
var elderInfo by mutableStateOf<ElderInfo?>(null)

// After
private val _elderInfo = MutableStateFlow<ElderInfo?>(null)
val elderInfo = _elderInfo.asStateFlow()

// Composable
val info by elderInfo.collectAsStateWithLifecycle()
```

---

### 4.2 공통 컴포넌트 문제

#### 4.2.1 MedInfoItem 🔴 Critical

**파일:** `ui/common/component/MedInfoItem.kt`

```kotlin
medsByPeriod[period]!!
```

**개선:** `getOrDefault()` 사용

---

#### 4.2.2 사용되지 않는 파라미터

| 파일 | 파라미터 |
|------|---------|
| `ui/feature/home/component/CareCallFloatingButton.kt:25` | `careCallOption: String` |
| `ui/feature/calendar/DateSelector.kt` | `onMonthClick` |

---

### 4.3 Suppress 주석 과다 (28개)

```kotlin
@SuppressLint("SetJavaScriptEnabled")  // NaverPayWebViewScreen.kt:52
@Suppress("DEPRECATION")               // MainActivity.kt:58-61
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")  // MainActivity.kt:40
```

---

### 4.4 테스트 현황 🔴 Critical

- 테스트 파일: **3개만 존재**
- 테스트 커버리지: **~1% 미만**

**필요한 테스트:**
```
├── Unit Tests (40-50개)
├── Integration Tests (10-15개)
└── UI Tests (5-10개)
```

---

### 4.5 UI 공통 작업 목록

| 우선순위 | 작업 | 파일 |
|---------|------|------|
| 🔴 Critical | `!!` 연산자 제거 | `MedInfoItem.kt` |
| 🔴 Critical | Unit Test 추가 (최소 20개) | 전체 |
| 🟠 High | StateFlow 최적화 | 전체 ViewModel |
| 🟡 Medium | 사용되지 않는 파라미터 정리 | 여러 컴포넌트 |
| 🟢 Low | Suppress 정리 | 전체 |

---

## 5. Feature: Home

### 5.1 HomeViewModel 문제점

#### 5.1.1 이중 로딩 플래그 설정 🔴 Critical

**파일:** `ui/feature/home/viewmodel/HomeViewModel.kt:140-204`

```kotlin
private fun fetchHomeSummaryForToday(elderId: Int) {
    viewModelScope.launch {
        _homeUiState.update { it.copy(isLoading = false) }  // 시작 직후 false? 오류!
        // ...
        finally {
            _homeUiState.update { it.copy(isLoading = false) }  // 중복
        }
    }
}
```

**개선:**
```kotlin
_homeUiState.update { it.copy(isLoading = true) }  // 시작 시 true
// ... API 호출 ...
finally {
    _homeUiState.update { it.copy(isLoading = false) }
}
```

---

#### 5.1.2 NPE 위험 🔴 Critical

**파일:** `ui/feature/home/viewmodel/HomeViewModel.kt:136-158`

```kotlin
fun callImmediate(careCallTimeOption: String) {
    viewModelScope.launch {
        homeRepository.requestImmediateCareCall(
            elderId = selectedElderId.value!!,  // NPE 위험!
        )
    }
}
```

**개선:**
```kotlin
fun callImmediate(careCallTimeOption: String) {
    val id = selectedElderId.value ?: return
    viewModelScope.launch {
        homeRepository.requestImmediateCareCall(elderId = id, ...)
    }
}
```

---

#### 5.1.3 SavedStateHandle 초기화 타이밍 🟠 Medium

**파일:** `ui/feature/home/viewmodel/HomeViewModel.kt:113-135`

`restoredId` 로직이 중복되어 있음

---

### 5.2 HomeScreen 문제점

#### 5.2.1 SavedStateHandle 부적절한 사용 🔴 Critical

**파일:** `ui/feature/home/screen/HomeScreen.kt:95-102`

```kotlin
@Composable
fun HomeScreen(...) {
    val updatedName by mainBackStackEntry.savedStateHandle
        .getStateFlow<String?>("ELDER_NAME_UPDATED", null)
        .collectAsStateWithLifecycle()

    LaunchedEffect(updatedName) {
        if (updatedName != null) {
            mainBackStackEntry.savedStateHandle.remove<String>("ELDER_NAME_UPDATED")
        }
    }
}
```

**개선:** ViewModel로 이동
```kotlin
// HomeViewModel.kt
class HomeViewModel(private val savedStateHandle: SavedStateHandle) {
    val elderNameUpdated = savedStateHandle.getStateFlow("ELDER_NAME_UPDATED", null)
    fun onNameUpdateHandled() { savedStateHandle["ELDER_NAME_UPDATED"] = null }
}
```

---

### 5.3 과도한 API 호출 🟠 Medium

**파일:** `ui/feature/home/viewmodel/HomeViewModel.kt:104-105`

화면 복귀 시마다 전체 데이터 새로고침 → 증분 업데이트 구현 권장

---

### 5.4 Home Feature 작업 목록

| 우선순위 | 작업 | 파일 |
|---------|------|------|
| 🔴 Critical | 로딩 플래그 로직 수정 | `HomeViewModel.kt:140-204` |
| 🔴 Critical | NPE 위험 제거 (`!!`) | `HomeViewModel.kt:136-158` |
| 🔴 Critical | SavedStateHandle → ViewModel | `HomeScreen.kt:95-102` |
| 🟠 High | 증분 업데이트 구현 | `HomeViewModel.kt` |
| 🟡 Medium | SavedStateHandle 초기화 정리 | `HomeViewModel.kt:113-135` |

---

## 6. Feature: Login

### 6.1 LoginElderViewModel 문제점

#### 6.1.1 인덱스 범위 검증 누락 🔴 Critical

**파일:** `ui/feature/login/senior/viewmodel/LoginElderViewModel.kt:94-107`

```kotlin
fun removeElder(index: Int) {
    _elderUiState.update { state ->
        state.copy(
            eldersList = state.eldersList.filterIndexed { i, _ -> i != index },
            // selectedIndex 조정 안 함!
        )
    }
}
```

**개선:**
```kotlin
fun removeElder(index: Int) {
    _elderUiState.update { state ->
        val newList = state.eldersList.filterIndexed { i, _ -> i != index }
        val newSelected = if (state.selectedIndex >= newList.size)
            maxOf(0, newList.size - 1) else state.selectedIndex
        state.copy(eldersList = newList, selectedIndex = newSelected)
    }
}
```

---

#### 6.1.2 건강정보 동기화 불일치 🟠 Medium

**파일:** `ui/feature/login/senior/viewmodel/LoginElderViewModel.kt:129-132, 263-305`

```kotlin
// eldersList와 elderHealthList가 독립적으로 관리됨
fun addElder() {
    _elderUiState.update { ... }
    // _elderHealthUiState 업데이트 누락!
}
```

**개선:** 어르신 추가/삭제 시 건강정보 리스트도 자동 동기화

---

### 6.2 LoginViewModel 문제점

#### 6.2.1 중복 요청 가능 🟠 Medium

**파일:** `ui/feature/login/info/viewmodel/LoginViewModel.kt:76-85`

```kotlin
fun postPhoneNumber(phone: String) {
    viewModelScope.launch {  // 로딩 체크 없음
        verificationRepository.requestCertificationCode(phone)
    }
}
```

**개선:**
```kotlin
private val _isLoading = MutableStateFlow(false)

fun postPhoneNumber(phone: String) {
    if (_isLoading.value) return
    viewModelScope.launch {
        _isLoading.value = true
        try { ... } finally { _isLoading.value = false }
    }
}
```

---

### 6.3 NaverPayWebViewScreen 문제점

#### 6.3.1 Null 처리 🔴 Critical

**파일:** `ui/feature/login/payment/screen/NaverPayWebViewScreen.kt:521`

```kotlin
popupWebView!!
```

---

#### 6.3.2 메모리 누수 가능성 🟠 High

**파일:** `ui/feature/login/payment/screen/NaverPayWebViewScreen.kt:101`

```kotlin
val webView = remember { WebView(context) }
// destroy 누락
```

**개선:**
```kotlin
DisposableEffect(Unit) {
    onDispose { webView?.destroy() }
}
```

---

#### 6.3.3 WebView 보안 🟠 High

**파일:** `ui/feature/login/payment/screen/NaverPayWebViewScreen.kt:52`

```kotlin
@SuppressLint("SetJavaScriptEnabled")
settings.javaScriptEnabled = true
```

**권장:** 신뢰 도메인만 JS 허용

---

#### 6.3.4 미완성 기능 🔴 Critical

| 라인 | TODO 내용 |
|------|----------|
| 82 | 임시 이동 버튼 (실배포 전 제거) |
| 499 | 실패 UI/토스트 구현 |

---

### 6.4 미완성 기능 (Login 전체)

| 파일 | TODO 내용 |
|------|----------|
| `LoginPhoneScreen.kt` | 서버 인증번호 요청 |
| `LoginVerificationScreen.kt` | 서버 인증번호 확인 |

---

### 6.5 Login Feature 작업 목록

| 우선순위 | 작업 | 파일 |
|---------|------|------|
| 🔴 Critical | 인덱스 범위 검증 추가 | `LoginElderViewModel.kt:94-107` |
| 🔴 Critical | `!!` 연산자 제거 | `NaverPayWebViewScreen.kt:521` |
| 🔴 Critical | TODO 완성 (임시 버튼 제거) | `NaverPayWebViewScreen.kt:82` |
| 🔴 Critical | TODO 완성 (실패 UI) | `NaverPayWebViewScreen.kt:499` |
| 🔴 Critical | TODO 완성 (인증번호) | `LoginPhoneScreen.kt`, `LoginVerificationScreen.kt` |
| 🟠 High | WebView 메모리 누수 수정 | `NaverPayWebViewScreen.kt:101` |
| 🟠 High | WebView 보안 강화 | `NaverPayWebViewScreen.kt:52` |
| 🟠 High | 중복 요청 방지 | `LoginViewModel.kt:76-85` |
| 🟠 High | 건강정보 동기화 | `LoginElderViewModel.kt` |

---

## 7. Feature: Statistics

### 7.1 StatisticsScreen 문제점

#### 7.1.1 SavedStateHandle 부적절한 사용 🔴 Critical

**파일:** `ui/feature/statistics/screen/StatisticsScreen.kt:108-117`

```kotlin
@Composable
fun StatisticsScreen(...) {
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val medsChanged by (savedStateHandle?.getStateFlow("medsChanged", false) ?: MutableStateFlow(false))
        .collectAsStateWithLifecycle()

    LaunchedEffect(medsChanged) {
        if (medsChanged) {
            savedStateHandle?.set("medsChanged", false)
        }
    }
}
```

**개선:** ViewModel로 이동
```kotlin
// StatisticsViewModel.kt
class StatisticsViewModel(private val savedStateHandle: SavedStateHandle) {
    val medsChanged = savedStateHandle.getStateFlow("medsChanged", false)
    fun onMedsChangeHandled() { savedStateHandle["medsChanged"] = false }
}
```

---

#### 7.1.2 LaunchedEffect 의존성 문제 🟠 Medium

**파일:** `ui/feature/statistics/screen/StatisticsScreen.kt:63`

```kotlin
LaunchedEffect(key1 = true)  // 항상 재실행
```

**개선:** 적절한 의존성 명시

---

### 7.2 StatisticsViewModel 문제점

#### 7.2.1 초기 상태 조기 종료 가능성 🟠 Medium

**파일:** `ui/feature/statistics/viewmodel/StatisticsViewModel.kt:127-189`

```kotlin
if (_uiState.value.isLoading && !ignoreLoadingGate) return  // 조기 반환
```

동시에 여러 어르신을 빠르게 전환할 때 데이터 로딩 누락 가능

---

### 7.3 Statistics Feature 작업 목록

| 우선순위 | 작업 | 파일 |
|---------|------|------|
| 🔴 Critical | SavedStateHandle → ViewModel | `StatisticsScreen.kt:108-117` |
| 🟠 High | LaunchedEffect 의존성 수정 | `StatisticsScreen.kt:63` |
| 🟡 Medium | 초기 상태 로딩 로직 개선 | `StatisticsViewModel.kt:127-189` |

---

## 8. Feature: Settings

### 8.1 ElderDetailScreen 문제점

#### 8.1.1 SavedStateHandle 부적절한 사용 🔴 Critical

**파일:** `ui/feature/settings/screen/ElderDetailScreen.kt:228`

```kotlin
navController.previousBackStackEntry?.savedStateHandle?.set("ELDER_NAME_UPDATED", name)
```

**개선:** Navigation Result API 패턴 사용

---

### 8.2 EldersInfoViewModel 문제점

#### 8.2.1 동기화 로직 누락 🟡 Medium

**파일:** `ui/feature/settings/viewmodel/EldersInfoViewModel.kt:43-80`

```kotlin
// NOTE: 중복 적재를 피하려면 ElderIdRepository에 replaceAll(...)을 추가하는 걸 추천.
```

ElderIdRepository에 `replaceAll()` 메소드 필요

---

### 8.3 미완성 기능 🔴 Critical

| 파일 | TODO 내용 |
|------|----------|
| `DeleteConfirmDialog.kt` | 삭제 동작 추가 |

---

### 8.4 Settings Feature 작업 목록

| 우선순위 | 작업 | 파일 |
|---------|------|------|
| 🔴 Critical | SavedStateHandle 패턴 수정 | `ElderDetailScreen.kt:228` |
| 🔴 Critical | TODO 완성 (삭제 동작) | `DeleteConfirmDialog.kt` |
| 🟡 Medium | ElderIdRepository replaceAll 추가 | `EldersInfoViewModel.kt` |

---

## 9. Feature: HomeDetail

### 9.1 GlucoseViewModel 문제점

#### 9.1.1 데이터 캐시 불일치 🔴 Critical

**파일:** `ui/feature/homedetail/glucoselevel/viewmodel/GlucoseViewModel.kt:32-66`

```kotlin
private var beforeMealData = mutableStateListOf<GraphDataPoint>()
private var afterMealData = mutableStateListOf<GraphDataPoint>()

fun getGlucoseData(..., type: GlucoseTiming, ...) {
    val updatedDataList = when (type) {
        GlucoseTiming.BEFORE_MEAL -> {
            if (isRefresh) beforeMealData.clear()
            beforeMealData.addAll(0, newData)
            beforeMealData
        }
    }
}
```

**문제:** 타이밍 전환 시 이전 데이터가 초기화되지 않음

---

### 9.2 MealViewModel, SleepViewModel 문제점

#### 9.2.1 상태 초기화 누락 🟢 Low

에러 발생 시 이전 데이터가 남아있을 수 있음

---

### 9.3 코드 중복 🟡 Medium

HomeDetail 하위 6개 ViewModel이 유사 패턴 반복:
- `GlucoseViewModel`, `SleepViewModel`, `HealthViewModel`
- `MentalViewModel`, `MedicineViewModel`, `MealViewModel`

**개선:** BaseViewModel 추상화
```kotlin
abstract class HealthDetailViewModel(
    protected val repository: EldersHealthInfoRepository
) : ViewModel() {
    protected fun fetchHealthData(elderId: Int, date: LocalDate) { ... }
}
```

---

### 9.4 HomeDetail Feature 작업 목록

| 우선순위 | 작업 | 파일 |
|---------|------|------|
| 🔴 Critical | 데이터 캐시 초기화 수정 | `GlucoseViewModel.kt:32-66` |
| 🟡 Medium | BaseViewModel 추상화 | 6개 ViewModel |
| 🟢 Low | 상태 초기화 개선 | `MealViewModel.kt`, `SleepViewModel.kt` |

---

## 10. Navigation

### 10.1 NavGraph 문제점

#### 10.1.1 콜백 이름과 동작 불일치 🟠 Medium

**파일:** `ui/navigation/NavGraph.kt:41-56`

```kotlin
composable<Route.Splash> {
    SplashScreen(
        navigateToLogin = { navController.navigate(Route.LoginStart) { ... } },
        navigateToStart = { navController.navigate(Route.LoginStart) },  // 중복!
        navigateToPurchase = { navController.navigateToMainAfterLogin() },
        navigateToHome = { navController.navigateToMainAfterLogin() },  // 동일 동작
    )
}
```

---

### 10.2 MainNavigator 문제점

#### 10.2.1 백스택 관리 비일관성 🟠 Medium

**파일:** `ui/navigation/MainNavigator.kt:59-72, 109-117, 209-214`

세 가지 다른 백스택 관리 방식 혼용:
1. `findStartDestination()` 사용
2. 특정 Route 명시
3. `MainTabRoute.Home` 명시

---

### 10.3 SavedStateHandle 사용 요약

| 파일 | 라인 | 위치 | 상태 |
|------|------|------|------|
| `StatisticsScreen.kt` | 108-117 | Screen | ❌ 부적절 |
| `HomeScreen.kt` | 95-102 | Screen | ❌ 부적절 |
| `ElderDetailScreen.kt` | 228 | Screen | ❌ 부적절 |
| `HomeViewModel.kt` | 31, 79, 90, 123 | ViewModel | ✅ 정상 |

---

### 10.4 Navigation 작업 목록

| 우선순위 | 작업 | 파일 |
|---------|------|------|
| 🟠 High | 콜백 이름/동작 정리 | `NavGraph.kt:41-56` |
| 🟠 High | 백스택 관리 일관성 확보 | `MainNavigator.kt` |

---

## 11. 우선순위별 작업 목록

### 🔴 Critical (즉시 해결)

#### Data Layer
- [ ] `!!` 연산자 제거 - `ElderRegisterRepositoryImpl.kt` (7개소)
- [ ] `!!` 연산자 제거 - `AuthAuthenticator.kt:52`

#### UI 공통
- [ ] `!!` 연산자 제거 - `MedInfoItem.kt`
- [ ] Unit Test 추가 (최소 20개)

#### Feature: Home
- [ ] 로딩 플래그 로직 수정 - `HomeViewModel.kt:140-204`
- [ ] NPE 위험 제거 - `HomeViewModel.kt:136-158`
- [ ] SavedStateHandle → ViewModel - `HomeScreen.kt:95-102`

#### Feature: Login
- [ ] 인덱스 범위 검증 추가 - `LoginElderViewModel.kt:94-107`
- [ ] `!!` 연산자 제거 - `NaverPayWebViewScreen.kt:521`
- [ ] TODO 완성 (임시 버튼 제거) - `NaverPayWebViewScreen.kt:82`
- [ ] TODO 완성 (실패 UI) - `NaverPayWebViewScreen.kt:499`
- [ ] TODO 완성 (인증번호) - `LoginPhoneScreen.kt`, `LoginVerificationScreen.kt`

#### Feature: Statistics
- [ ] SavedStateHandle → ViewModel - `StatisticsScreen.kt:108-117`

#### Feature: Settings
- [ ] SavedStateHandle 패턴 수정 - `ElderDetailScreen.kt:228`
- [ ] TODO 완성 (삭제 동작) - `DeleteConfirmDialog.kt`

#### Feature: HomeDetail
- [ ] 데이터 캐시 초기화 수정 - `GlucoseViewModel.kt:32-66`

---

### 🟠 High (다음 Sprint)

#### Data Layer
- [ ] runBlocking 제거 - `AuthInterceptor.kt`
- [ ] Retrofit 제거, Ktorfit 통합 - `ApiModule.kt`, `KtorApiModule.kt`

#### UI 공통
- [ ] StateFlow 최적화 - 전체 ViewModel

#### Feature: Home
- [ ] 증분 업데이트 구현 - `HomeViewModel.kt`

#### Feature: Login
- [ ] WebView 메모리 누수 수정 - `NaverPayWebViewScreen.kt:101`
- [ ] WebView 보안 강화 - `NaverPayWebViewScreen.kt:52`
- [ ] 중복 요청 방지 - `LoginViewModel.kt:76-85`
- [ ] 건강정보 동기화 - `LoginElderViewModel.kt`

#### Feature: Statistics
- [ ] LaunchedEffect 의존성 수정 - `StatisticsScreen.kt:63`

#### Navigation
- [ ] 콜백 이름/동작 정리 - `NavGraph.kt:41-56`
- [ ] 백스택 관리 일관성 확보 - `MainNavigator.kt`

---

### 🟡 Medium (향후 개선)

#### Data Layer
- [ ] Enum Mapper 추상화 - 신규 `TypeMapper.kt` 생성
- [ ] Repository 구조 단순화 - 전체 repository 파일

#### UI 공통
- [ ] 사용되지 않는 파라미터 정리 - 여러 컴포넌트

#### Feature: Home
- [ ] SavedStateHandle 초기화 정리 - `HomeViewModel.kt:113-135`

#### Feature: Statistics
- [ ] 초기 상태 로딩 로직 개선 - `StatisticsViewModel.kt:127-189`

#### Feature: Settings
- [ ] ElderIdRepository replaceAll 추가 - `EldersInfoViewModel.kt`

#### Feature: HomeDetail
- [ ] BaseViewModel 추상화 - 6개 ViewModel

---

### 🟢 Low (리팩토링)

#### Data Layer
- [ ] 중복 의존성 제거 - `build.gradle.kts`

#### UI 공통
- [ ] Suppress 정리 - 전체
- [ ] 로깅 일관성 (TAG 표준화)
- [ ] Kdoc 문서화 추가
- [ ] accompanist-webview 대체 라이브러리 검토

#### Feature: HomeDetail
- [ ] 상태 초기화 개선 - `MealViewModel.kt`, `SleepViewModel.kt`

---

## 최근 리팩토링 진행 상황

**진행 중인 작업:**
- ✅ Retrofit → Ktorfit 마이그레이션 (일부 완료)
- ✅ NavGraph 리팩토링 (PR #223)

**권장:** Retrofit 완전 제거 후 `ApiModule.kt`와 `KtorApiModule.kt` 통합
