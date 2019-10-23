package de.bergerapps.owlbot.service.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import de.bergerapps.owlbot.service.model.OwlBotResponse
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class RestAPI {

    private val owlBotService: OwlBotService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://owlbot.info/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        owlBotService = retrofit.create(OwlBotService::class.java)
    }

    fun getDictionary(
        context: Context,
        data: MutableLiveData<OwlBotResponse>,
        dictionary: String
    ) {
        val properties = Properties()
        properties.load(context.assets.open("auth.properties"))
        val token = properties.getProperty("token")
        doAsync {

            owlBotService.getDictionary(dictionary, token)
                .enqueue(object : Callback<OwlBotResponse> {
                    override fun onFailure(call: Call<OwlBotResponse>?, t: Throwable?) {
                        Log.v("retrofit", "call failed")
                    }

                    override fun onResponse(
                        call: Call<OwlBotResponse>?,
                        response: Response<OwlBotResponse>?
                    ) {
                        if (response?.body() != null)
                            data.value = response!!.body()!!
                        else
                            data.value = null
                    }

                })
        }
    }
}