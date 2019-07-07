package com.example.fingerprintauth

import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory;



class RetrofitClient {

    companion object {
        private var retrofit: Retrofit? = null
        private const val BASE_URL = "http://192.168.100.8:8080/"
//        private const val BASE_URL = "https://attendance-web-service.herokuapp.com/"
        fun getInstance(): Retrofit? {
            if (retrofit == null) {
                retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }

}