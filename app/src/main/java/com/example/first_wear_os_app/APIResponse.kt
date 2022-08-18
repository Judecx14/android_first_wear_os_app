package com.example.first_wear_os_app

data class APIResponse(
    var status: Boolean,
    var data: List<String>,
    var message: String
)
