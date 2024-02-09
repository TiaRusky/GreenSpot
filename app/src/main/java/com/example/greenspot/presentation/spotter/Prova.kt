package com.example.greenspot.presentation.spotter

import android.content.Context
import android.widget.Toast

class Prova(val context: Context) {
    fun stampa() {
        Toast.makeText(
            context,
            "SHAKER",
            Toast.LENGTH_LONG
        ).show()
    }
}