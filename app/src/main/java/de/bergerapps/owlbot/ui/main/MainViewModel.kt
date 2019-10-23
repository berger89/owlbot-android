package de.bergerapps.owlbot.ui.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.bergerapps.owlbot.service.model.OwlBotResponse
import de.bergerapps.owlbot.service.repository.RestAPI

class MainViewModel : ViewModel() {
    var lastString = ""
    val owlBotLiveData = MutableLiveData<OwlBotResponse>()

    private val restAPI = RestAPI()

    fun getDictionary(context: Context, dictionary: String) {
        if (lastString == dictionary) {
            return
        }
        lastString = dictionary
        restAPI.getDictionary(context, owlBotLiveData, dictionary.toLowerCase())
    }
}
