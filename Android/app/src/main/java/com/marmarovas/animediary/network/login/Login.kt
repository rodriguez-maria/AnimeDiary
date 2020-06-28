package com.marmarovas.animediary.network.login

data class Login (
    val success : Boolean,
    val data : LoginData?,
    val error: String?
)


