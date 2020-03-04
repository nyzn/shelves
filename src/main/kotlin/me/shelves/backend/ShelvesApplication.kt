package me.shelves.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShelvesApplication

fun main(args: Array<String>) {
	runApplication<ShelvesApplication>(*args)
}
