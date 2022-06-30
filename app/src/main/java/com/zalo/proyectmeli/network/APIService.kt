package com.zalo.proyectmeli.network

import com.zalo.proyectmeli.utils.URL_BASE
import com.zalo.proyectmeli.utils.models.*
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("categories")
    fun getCategories(): Single<List<Categories>>

    @GET("search?")
    fun getCategoriesDetails(
        @Query("category") categories: String,
    ): Single<ProductDataResponse>

    @GET("search?")
    fun getItemsList(@Query("q") item: String): Single<ProductDataResponse>

    @GET("/items/{item}/description")
    fun getItemDescription(@Path("item") item: String): Single<DescriptionResponse>

    @GET("/items/{item}")
    fun getItemById(@Path("item") item: String): Single<ProductResponse>

}

object APIServiceImplements {
    fun getCategories(): Single<List<Categories>> {
        return service.getCategories()
    }

    fun getCategoriesDetail(categories: String): Single<ProductDataResponse> {
        return service.getCategoriesDetails(categories)
    }

    fun getItemsList(item: String): Single<ProductDataResponse> {
        return service.getItemsList(item)
    }

    fun getItemDescription(itemId: String): Single<DescriptionResponse> {
        return service.getItemDescription(itemId)
    }

    fun getItemById(itemId: String): Single<ProductResponse> {
        return service.getItemById(itemId)
    }

    private val service: APIService by lazy {
        Retrofit
            .Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(APIService::class.java)
    }
}