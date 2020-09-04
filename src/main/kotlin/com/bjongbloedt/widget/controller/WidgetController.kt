package com.bjongbloedt.widget.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/widgets")
internal class WidgetController {

    @GetMapping
    fun getWidgets() = listOf(mapOf("name" to "Frank"), mapOf("name" to "Larry_too"))
}