package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.data.repository.DataStoreRepository
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.repository.ElderRegisterRepository
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.data.repository.MemberRegisterRepository
import com.konkuk.medicarecall.data.repository.NaverPayRepository
import com.konkuk.medicarecall.data.repository.NoticeRepository
import com.konkuk.medicarecall.data.repository.SetCallRepository
import com.konkuk.medicarecall.data.repository.SubscribeRepository
import com.konkuk.medicarecall.data.repository.UpdateElderInfoRepository
import com.konkuk.medicarecall.data.repository.UserRepository
import com.konkuk.medicarecall.data.repository.VerificationRepository
import com.konkuk.medicarecall.data.repositoryimpl.DataStoreRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.ElderIdRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.ElderRegisterRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.EldersHealthInfoRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.EldersInfoRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.MemberRegisterRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.NaverPayRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.NoticeRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.SetCallRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.SubscribeRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.UpdateElderInfoRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.UserRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.VerificationRepositoryImpl
import com.konkuk.medicarecall.data.repository.HomeRepository
import com.konkuk.medicarecall.data.repositoryimpl.HomeRepositoryImpl
import com.konkuk.medicarecall.data.repository.GlucoseRepository
import com.konkuk.medicarecall.data.repositoryimpl.GlucoseRepositoryImpl
import com.konkuk.medicarecall.data.repository.MealRepository
import com.konkuk.medicarecall.data.repositoryimpl.MealRepositoryImpl
import com.konkuk.medicarecall.data.repository.MedicineRepository
import com.konkuk.medicarecall.data.repositoryimpl.MedicineRepositoryImpl
import com.konkuk.medicarecall.data.repository.SleepRepository
import com.konkuk.medicarecall.data.repositoryimpl.SleepRepositoryImpl
import com.konkuk.medicarecall.data.repository.HealthRepository
import com.konkuk.medicarecall.data.repositoryimpl.HealthRepositoryImpl
import com.konkuk.medicarecall.data.repository.MentalRepository
import com.konkuk.medicarecall.data.repositoryimpl.MentalRepositoryImpl
import com.konkuk.medicarecall.data.repository.StatisticsRepository
import com.konkuk.medicarecall.data.repositoryimpl.StatisticsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDataStoreRepository(dataStoreRepositoryImpl: DataStoreRepositoryImpl): DataStoreRepository

    @Binds
    @Singleton
    abstract fun bindEldersInfoRepository(eldersInfoRepositoryImpl: EldersInfoRepositoryImpl): EldersInfoRepository

    @Binds
    @Singleton
    abstract fun bindVerificationRepository(verificationRepositoryImpl: VerificationRepositoryImpl): VerificationRepository

    @Binds
    @Singleton
    abstract fun bindMemberRegisterRepository(memberRegisterRepositoryImpl: MemberRegisterRepositoryImpl): MemberRegisterRepository

    @Binds
    @Singleton
    abstract fun bindNoticeRepository(noticeRepositoryImpl: NoticeRepositoryImpl): NoticeRepository

    @Binds
    @Singleton
    abstract fun bindSetCallRepository(setCallRepositoryImpl: SetCallRepositoryImpl): SetCallRepository

    @Binds
    @Singleton
    abstract fun bindSubscribeRepository(subscribeRepositoryImpl: SubscribeRepositoryImpl): SubscribeRepository

    @Binds
    @Singleton
    abstract fun bindEldersHealthInfoRepository(eldersHealthInfoRepositoryImpl: EldersHealthInfoRepositoryImpl): EldersHealthInfoRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindUpdateElderInfoRepository(updateElderInfoRepositoryImpl: UpdateElderInfoRepositoryImpl): UpdateElderInfoRepository

    @Binds
    @Singleton
    abstract fun bindNaverPayRepository(naverPayRepositoryImpl: NaverPayRepositoryImpl): NaverPayRepository

    @Binds
    @Singleton
    abstract fun bindElderRegisterRepository(elderRegisterRepositoryImpl: ElderRegisterRepositoryImpl): ElderRegisterRepository

    @Binds
    @Singleton
    abstract fun bindElderIdRepository(elderIdRepositoryImpl: ElderIdRepositoryImpl): ElderIdRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    @Binds
    @Singleton
    abstract fun bindGlucoseRepository(glucoseRepositoryImpl: GlucoseRepositoryImpl): GlucoseRepository

    @Binds
    @Singleton
    abstract fun bindMealRepository(mealRepositoryImpl: MealRepositoryImpl): MealRepository

    @Binds
    @Singleton
    abstract fun bindMedicineRepository(medicineRepositoryImpl: MedicineRepositoryImpl): MedicineRepository

    @Binds
    @Singleton
    abstract fun bindSleepRepository(sleepRepositoryImpl: SleepRepositoryImpl): SleepRepository

    @Binds
    @Singleton
    abstract fun bindHealthRepository(healthRepositoryImpl: HealthRepositoryImpl): HealthRepository

    @Binds
    @Singleton
    abstract fun bindMentalRepository(mentalRepositoryImpl: MentalRepositoryImpl): MentalRepository

    @Binds
    @Singleton
    abstract fun bindStatisticsRepository(statisticsRepositoryImpl: StatisticsRepositoryImpl): StatisticsRepository
}
