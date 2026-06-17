package com.salu.project.api

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/api/"
    private var retrofit: Retrofit? = null

    private fun getRetrofit(context: Context): Retrofit {
        if (retrofit == null) {
            val sessionManager = SessionManager(context.applicationContext)

            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                    sessionManager.fetchAuthToken()?.let {
                        requestBuilder.addHeader("Authorization", "Bearer $it")
                    }
                    chain.proceed(requestBuilder.build())
                }
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        return retrofit!!
    }

    fun getUserApi(context: Context): UserApi = getRetrofit(context).create(UserApi::class.java)
    fun getProductApi(context: Context): ProductApi = getRetrofit(context).create(ProductApi::class.java)
    fun getCartApi(context: Context): CartApi = getRetrofit(context).create(CartApi::class.java)
}
