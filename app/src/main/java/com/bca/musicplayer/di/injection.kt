package com.bca.musicplayer.di

import com.bca.musicplayer.BuildConfig
import com.bca.musicplayer.data.repository.MusicRepository
import com.bca.musicplayer.data.source.remote.source.MusicRemoteDataSource
import com.bca.musicplayer.domain.repository.IMusicRepository
import com.bca.musicplayer.domain.usecase.MusicPlayerIterator
import com.bca.musicplayer.domain.usecase.MusicPlayerUsecase
import com.bca.musicplayer.ui.music.MusicViewModel
import com.dicoding.core.data.source.remote.network.ApiService
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module{
    single { MusicRemoteDataSource(get()) }

    single<IMusicRepository> {
        MusicRepository(get())
    }
}

val useCaseModule = module{
    single<MusicPlayerUsecase>{
        MusicPlayerIterator(
            get()
        )
    }
}
val viewModelModule = module{
    viewModel {
         MusicViewModel(get())
    }
}