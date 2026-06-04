package no.nav.sikkerhetstjenesten.loggkamelproxy.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/blah/whatever")
class DemoController {

    @GetMapping("specific/endpoint")
    fun testEndpoint() : String {
        return "THIS IS ENDPOINT OUTPUT"
    }
}