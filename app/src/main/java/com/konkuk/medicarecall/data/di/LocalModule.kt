package com.konkuk.medicarecall.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.konkuk.medicarecall.data.model.ElderIds
import com.konkuk.medicarecall.data.model.Token
import com.konkuk.medicarecall.data.util.ElderIdsSerializer
import com.konkuk.medicarecall.data.util.TokenSerializer
import org.koin.core.annotation.Module
import org.koin.core.annotation.Qualifier
import org.koin.core.annotation.Single

val Context.tokenDataStore by dataStore(
    fileName = "tokens",
    serializer = TokenSerializer,
)

val Context.elderIdDataStore by dataStore(
    fileName = "elderIds",
    serializer = ElderIdsSerializer,
)

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ElderIdDataStore

@Module
class LocalModule {

    @TokenDataStore
    @Single
    fun provideTokenDataStore(context: Context): DataStore<Token> {
        return context.tokenDataStore
    }

    @ElderIdDataStore
    @Single
    fun provideElderIdDataStore(context: Context): DataStore<ElderIds> {
        return context.elderIdDataStore
    }
}
