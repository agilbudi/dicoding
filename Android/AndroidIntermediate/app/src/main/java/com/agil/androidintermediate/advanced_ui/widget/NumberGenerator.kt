package com.agil.androidintermediate.advanced_ui.widget

import kotlin.random.Random

//this is class helper
internal object NumberGenerator {
    fun generate(max: Int): Int{
        val random = Random
        return random.nextInt(max)
    }
}