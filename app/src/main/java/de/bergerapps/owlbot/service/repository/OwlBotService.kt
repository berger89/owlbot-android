package de.bergerapps.owlbot.service.repository

import de.bergerapps.owlbot.service.model.OwlBotResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface OwlBotService {
    /**
     * @GET declares an HTTP GET request
     */
    @GET("dictionary/{dictionary}")
    fun getDictionary(@Path("dictionary") dictionary: String, @Header("Authorization") auth: String): Call<OwlBotResponse>
}