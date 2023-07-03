package com.marketbook.controller

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("admin")
class AdminController() {

    @GetMapping("/reports")
    fun report(): String = "This is a Report. Only Admin can see it"

}