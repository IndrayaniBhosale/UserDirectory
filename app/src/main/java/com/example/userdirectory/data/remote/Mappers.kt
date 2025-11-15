package com.example.userdirectory.data.remote

import com.example.userdirectory.data.local.UserEntity

fun UserDto.toEntity() = UserEntity(
    id = id,
    name = name,
    email = email,
    phone = phone
)
