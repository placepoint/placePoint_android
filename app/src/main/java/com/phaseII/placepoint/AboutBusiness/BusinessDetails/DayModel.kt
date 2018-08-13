package com.phaseII.placepoint.AboutBusiness.BusinessDetails

class DayModel {
    lateinit var day:String
    lateinit var time:String

    override fun toString(): String {
        return "$day     $time"
    }
}