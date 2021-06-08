package com.example.appclinica.paymantel.modelo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Transaction(var status: String = "",
        var payment_date:String = "",
        var amoun:Double = 0.00,
        var authorization_code:String = "",
        var installments:Int = 0,
        var devReference:String = "",
        var message:String = "",
        var error_code:String = "",
        var id:String = "",
        var status_detail:Int = 0
) {
}
