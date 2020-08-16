package com.example.organize.helper

import android.util.Base64

class Base64Custom {
    companion object {
        fun crypto64(text: String?): String {
            return Base64.encodeToString(text!!.toByteArray(), Base64.NO_WRAP)
        }
        fun deCrypto64(cryptoText: String): String {
            return String(Base64.decode(cryptoText, Base64.DEFAULT))
        }
    }

}