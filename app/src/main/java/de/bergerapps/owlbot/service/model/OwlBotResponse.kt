package de.bergerapps.owlbot.service.model

class OwlBotResponse {
    var definitions = ArrayList<OwlBotDefinitionResponse>()
    var word = ""
    var pronunciation = ""

    class OwlBotDefinitionResponse {
        var type = ""
        var definition = ""
        var example = ""
        var image_url = ""
    }
}