package com.example.contacthelper.models

import java.io.Serializable

class Contacts : Serializable {
    var name:String? = null
    var number:String? = null

    constructor(name: String?, number: String?) {
        this.name = name
        this.number = number
    }
}