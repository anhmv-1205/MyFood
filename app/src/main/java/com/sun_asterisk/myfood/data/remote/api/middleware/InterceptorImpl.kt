package com.sun_asterisk.myfood.data.remote.api.middleware

import com.sun_asterisk.myfood.data.repositories.TokenRepository
import com.sun_asterisk.myfood.utils.extension.notNull
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response
import org.koin.core.KoinComponent
import java.net.HttpURLConnection

class InterceptorImpl(private val tokenRepository: TokenRepository?) : Interceptor, KoinComponent {

    private val isRefreshToken: Boolean = false

    override fun intercept(chain: Chain): Response {
        val builder = initializeHeader(chain)
        val request = builder.build()
        var response = chain.proceed(request)

        if (!isRefreshToken && response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            builder.removeHeader(KEY_TOKEN)

            tokenRepository?.getToken()?.let {
                builder.addHeader(KEY_TOKEN, it)
                response = chain.proceed(request)
            }
        }

        return response
    }

    private fun initializeHeader(chain: Chain): Request.Builder {
        val originRequest = chain.request()
        val url = originRequest.url.newBuilder().build()
        val builder = originRequest.newBuilder().header("Accept", "application/json")
            .addHeader("Cache-Control", "no-cache")
            .addHeader("Cache-Control", "no-store")
            .url(url)
            .method(originRequest.method, originRequest.body)
        tokenRepository?.getToken().notNull {
            builder.addHeader(KEY_TOKEN, it)
        }

        return builder
    }

    companion object {
        private const val KEY_TOKEN = "Authorization"
    }
}
