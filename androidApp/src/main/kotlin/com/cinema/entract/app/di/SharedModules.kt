package com.cinema.entract.app.di

import com.cinema.entract.cache.repository.CinemaCacheRepo
import com.cinema.entract.cache.repository.CinemaUserPreferencesRepo
import com.cinema.entract.data.interactor.CinemaUseCase
import com.cinema.entract.data.interactor.NotifUseCase
import com.cinema.entract.data.interactor.TagUseCase
import com.cinema.entract.data.platform.DateUtils
import com.cinema.entract.data.platform.NetworkUtils
import com.cinema.entract.data.platform.PlatformUtils
import com.cinema.entract.data.repository.CacheRepo
import com.cinema.entract.data.repository.RemoteRepo
import com.cinema.entract.data.repository.UserPreferencesRepo
import com.cinema.entract.data.source.CinemaDataStore
import com.cinema.entract.data.source.DataStore
import com.cinema.entract.remote.CinemaRemoteRepo
import com.cinema.entract.remote.api.CinemaApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val remoteModule = module {
    single { CinemaApi() }
    factory<RemoteRepo> { CinemaRemoteRepo(get(), get()) }
}

val cacheModule = module {
    single<CacheRepo> { CinemaCacheRepo() }
    single<UserPreferencesRepo> { CinemaUserPreferencesRepo(androidContext()) }
}

val dataModule = module {
    single { CinemaUseCase(get(), get(), get(), get()) }
    single { TagUseCase(get(), get()) }
    single { NotifUseCase(get()) }

    single<DataStore> { CinemaDataStore(get(), get(), get()) }

    single { DateUtils() }
    single { PlatformUtils() }
    single { NetworkUtils(androidContext()) }
}
