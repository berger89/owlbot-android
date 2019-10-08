package de.bergerapps.owlbot.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.bergerapps.owlbot.rest.OwlBotResponse
import de.bergerapps.owlbot.rest.RestAPI

class MainViewModel : ViewModel() {
    val owlBotLiveData = MutableLiveData<OwlBotResponse>()

    private val restAPI = RestAPI()

    fun getDictionary(dictionary: String) {
        restAPI.getDictionary(owlBotLiveData, dictionary.toLowerCase())
    }
}
