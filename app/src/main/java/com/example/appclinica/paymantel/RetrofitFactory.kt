package com.example.appclinica.paymantel

import com.example.appclinica.notification.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    companion object {

        private val retrofittest by lazy {
            Retrofit.Builder()
                    .baseUrl(Constants.BACKEND_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        val api by lazy {
            retrofittest.create(BackendService::class.java)
        }

    }
    /*var retrofit: Retrofit? = null
    var builder = OkHttpClient().newBuilder()


    fun getClient(): Retrofit? {
        if (retrofit == null) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
            builder.connectTimeout(3, TimeUnit.MINUTES)
            builder.readTimeout(3, TimeUnit.MINUTES)
            builder.writeTimeout(3, TimeUnit.MINUTES)
            val client = builder.build()
            retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BACKEND_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
        }
        return retrofit
    }*/
}