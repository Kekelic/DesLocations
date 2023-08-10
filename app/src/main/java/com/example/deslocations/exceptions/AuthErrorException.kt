package com.example.deslocations.exceptions

import android.content.Context
import android.text.TextUtils
import com.example.deslocations.R

class AuthErrorException(errorCode: String, context: Context) {

    var emailErrorMessage: String =
        when (errorCode) {
            "ERROR_INVALID_EMAIL" -> context.getString(R.string.error_invalid_email)
            "ERROR_EMAIL_ALREADY_IN_USE" -> context.getString(R.string.error_email_already_in_use)
            "ERROR_USER_NOT_FOUND" -> context.getString(R.string.error_user_not_found)
            else -> ""
        }

    var passwordErrorMessage: String =
        when (errorCode) {
            "ERROR_WRONG_PASSWORD" -> context.getString(R.string.error_wrong_password)
            "ERROR_WEAK_PASSWORD" -> context.getString(R.string.error_weak_password)
            else -> ""
        }

    var specificErrorMessage: String =
        if (TextUtils.isEmpty(passwordErrorMessage) && TextUtils.isEmpty(emailErrorMessage)) {
            when (errorCode) {
                "ERROR_USER_DISABLED" -> context.getString(R.string.error_user_disabled)
                "ERROR_USER_TOKEN_EXPIRED" -> context.getString(R.string.error_user_token_expired)
                else -> context.getString(R.string.error_something_went_wrong)
            }
        } else ""


}