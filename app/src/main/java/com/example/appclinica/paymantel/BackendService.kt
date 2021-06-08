package com.example.appclinica.paymantel

import com.example.appclinica.paymantel.modelo.CreateChargeResponse
import com.example.appclinica.paymantel.modelo.DeleteCardResponse
import com.example.appclinica.paymantel.modelo.GetCardsResponse
import retrofit2.Call
import retrofit2.http.*

interface BackendService {

    @FormUrlEncoded
    @POST("/create-charge")
    fun createCharge(@Field("uid") uid: String?, @Field("session_id") session_id: String?,
                     @Field("token") token: String?, @Field("amount") amount: Double,
                     @Field("dev_reference") dev_reference: String?, @Field("description") description: String?): Call<CreateChargeResponse?>?

    @FormUrlEncoded
    @POST("/delete-card")
    fun deleteCard(@Field("uid") uid: String?, @Field("token") token: String?): Call<DeleteCardResponse?>?

    @GET("/get-cards")
    fun getCards(@Query("uid") uid: String?): Call<GetCardsResponse?>?

    /*@GET("/get-cards")
    fun getCards(@Query("uid") uid: String?): Call<GetCardsResponse?>?

    @FormUrlEncoded
    @POST("/delete-card")
    fun deleteCard(@Field("uid") uid: String?, @Field("token") token: String?): Call<DeleteCardResponse?>?

    @FormUrlEncoded
    @POST("/verify-transaction")
    fun verifyTransaction(@Field("uid") uid: String?, @Field("transaction_id") transaction_id: String?,
                          @Field("type") type: String?, @Field("value") value: String?): Call<VerifyResponse?>?*/
}