package com.salu.project.api

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        const val IS_LOGGED_IN = "is_logged_in"
        const val USER_NAME = "user_name"
        const val USER_EMAIL = "user_email"
        const val AUTH_TOKEN = "auth_token"
    }

    fun setLoginStatus(isLoggedIn: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(IS_LOGGED_IN, false)
    }

    fun saveUser(name: String, email: String) {
        val editor = prefs.edit()
        editor.putString(USER_NAME, name)
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(AUTH_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(AUTH_TOKEN, null)
    }

    fun logout() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
