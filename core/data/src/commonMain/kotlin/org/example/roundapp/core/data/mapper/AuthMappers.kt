package org.example.roundapp.core.data.mapper

import org.example.roundapp.core.data.dto.AuthResponseDto
import org.example.roundapp.core.data.dto.AuthTokensDto
import org.example.roundapp.core.data.dto.UserDto
import org.example.roundapp.core.domain.auth.AuthResult
import org.example.roundapp.core.domain.dto.AuthTokens
import org.example.roundapp.core.domain.dto.User

fun AuthTokensDto.toDomain(): AuthTokens = AuthTokens(access = access, refresh = refresh)

fun UserDto.toDomain(): User = User(id = id, email = email, displayName = displayName)

fun AuthResponseDto.toDomain(): AuthResult.Authenticated =
    AuthResult.Authenticated(tokens = tokens.toDomain(), user = user.toDomain())
