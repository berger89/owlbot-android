package de.bergerapps.owlbot.service.repository

import de.bergerapps.owlbot.service.model.OwlBotResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface OwlBotService {
    /**
     * @GET declares an HTTP GET request
     */
    @Headers("Authorization: Token ")
    @GET("dictionary/{dictionary}")
    fun getDictionary(@Path("dictionary") dictionary: String): Call<OwlBotResponse>
}