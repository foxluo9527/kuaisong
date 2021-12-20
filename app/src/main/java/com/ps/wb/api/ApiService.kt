package com.ps.wb.api

import retrofit2.http.*


interface ApiService {
    /**
     * 登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return
     */
    @POST("new_war_exploded/bysj/getLogin")
    suspend fun loginNew(
        @Query("phone") phone: String?,
        @Query("pwd") password: String?
    ): String?

    /**
     * 账号注册
     *
     * @param phone
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("new_war_exploded/bysj/getRegister")
    suspend fun register(
        @Field("phone") phone: String?,
        @Field("pwd") password: String?
    ): String?

    /**
     * 找回密码/修改密码
     *
     * @param phone       手机号
     * @param newPassword 新密码
     * @return
     */
    @FormUrlEncoded
    @POST("new_war_exploded/bysj/getForgetPwd")
    suspend fun resetPassword(
        @Field("phone") phone: String?,
        @Field("pwd") newPassword: String?
    ): String?

    /**
     * 注销
     *
     * @param phone
     * @return
     */
    @FormUrlEncoded
    @POST("new_war_exploded/bysj/getCancellation")
    suspend fun logout(@Field("phone") phone: String?): String?

    /**
     * 请求WebURL
     *
     * @return
     */
    @GET("http://mock-api.com/lzjqeWK4.mock/appconfig")
    suspend fun webURL(): String?
}