package com.example.sleephony.domain.repository

import android.app.Activity
import com.example.sleephony.data.model.auth.SocialLoginResult
import com.example.sleephony.data.model.user.UserProfileRequest

interface AuthRepository {
    suspend fun isLoggedIn(): Boolean
    suspend fun loginWithKakao(activity: Activity): Result<SocialLoginResult>
    suspend fun loginWithGoogle(activity: Activity): Result<SocialLoginResult>
    suspend fun submitProfile(request: UserProfileRequest): Result<Unit>
}