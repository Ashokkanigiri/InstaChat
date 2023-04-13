package com.example.instachat.utils

import com.example.instachat.services.models.dummyjson.User
import java.util.regex.Pattern


object ValidationUtils {
    val emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
    val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\S+\$).{6,}\$"

    fun shouldRegisterUser(
        user: User, reEnteredPassword: String
    ) =
        isValidEmail(user.email) && isValidPassword(user.password) && isValidFirstName(user.firstName) && user.password.equals(reEnteredPassword)

    fun isValidEmail(email: String): Boolean {
        return isValidLoginEmail(email) && Pattern.matches(emailPattern, email)
    }

    fun isValidPassword(password: String) :Boolean{
        return Pattern.matches(passwordPattern, password)
    }

    fun isValidLoginEmail(email: String?): Boolean{
        return !email.isNullOrEmpty()
    }

    fun isValidLoginPassword(password: String?): Boolean{
        return !password.isNullOrEmpty()
    }

    fun isValidFirstName(firstName: String) = firstName.isNotEmpty()

}