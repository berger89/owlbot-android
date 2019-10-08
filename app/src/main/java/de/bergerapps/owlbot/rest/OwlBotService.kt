package de.bergerapps.owlbot.rest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface OwlBotService {
    /**
     * @GET declares an HTTP GET request
     */
    @Headers("Authorization: Token de25a306d7fd5cd7b45235cbd25628268087eeca")
    @GET("dictionary/{dictionary}")
    fun getDictionary(@Path("dictionary") dictionary: String): Call<OwlBotResponse>
}