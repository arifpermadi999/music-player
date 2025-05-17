package com.bca.musicplayer

import com.bca.musicplayer.data.repository.MusicRepository
import com.bca.musicplayer.data.source.remote.source.MusicRemoteDataSource
import com.bca.musicplayer.domain.usecase.MusicPlayerUsecase
import com.dicoding.core.data.source.remote.network.ApiService
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.mockito.Mockito.mock
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
class ModuleTest : AutoCloseKoinTest() {
    //======= NETWORK ==========
    private val mockOkHttpClient: OkHttpClient = mock()
    private val mockRetrofit: Retrofit = mock()
    private val apiService: ApiService by inject()
    //=============================
    private val mockRemoteSource: MusicRemoteDataSource = mock()
    private val musicRemoteDataSource: MusicRemoteDataSource by inject()

    private val mockRepository: MusicRepository = mock()
    private val repository: MusicRepository by inject()


    private val moduleNetwork = module {
        single { mockOkHttpClient }
        single { mockRetrofit }
        single { mock<ApiService>() }
    }
    private val moduleRepository = module{
        single { mockRemoteSource }
        single{ mockRepository }
    }
    @Test
    fun `test network module dependencies`() {
        startKoin {
            modules(
                moduleNetwork
            )
        }
        val injectedOkHttpClient: OkHttpClient by inject()
        val injectedRetrofit: Retrofit by inject()
        assertNotNull(injectedOkHttpClient)
        assertNotNull(injectedRetrofit)
        assert(apiService is ApiService)
    }
    @Test
    fun `test repository module dependencies`() {
        startKoin {
            modules(
                moduleNetwork,
                moduleRepository
            )
        }
        assertNotNull(musicRemoteDataSource)
        assertNotNull(repository)
        assert(repository is MusicRepository)
    }


}