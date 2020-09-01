package com.bjongbloedt.widget

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class WidgetApplication

fun main(args: Array<String>) {
    runApplication<WidgetApplication>(*args)
}

@RestController
@RequestMapping("api/v1/widgets")
internal class WidgetController {

    @GetMapping
    fun getWidgets() = listOf(mapOf("name" to "Frank"), mapOf("name" to "Larry_too"))
}