package com.ewa.mikulska.listoffriends.retrofit

import com.ewa.mikulska.listoffriends.data.BackgroundPhoto
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface PicsumService {

    @GET("v2/list")
    fun getBackgroundAsync(): Deferred<Response<BackgroundPhoto>>
}