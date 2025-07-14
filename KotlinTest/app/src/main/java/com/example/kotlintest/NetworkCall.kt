package com.example.kotlintest

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class NumberCall() {
    fun numberCall(): Flow<Int> {
        val numberFlow =  flow {
            while (true){
                val randomNumber = (1..100).random()
                emit(randomNumber)
                delay(2000)
            }
        }

        return numberFlow
    }

}