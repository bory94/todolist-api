package com.bory.todolistapi

import com.bory.todolistapi.entity.UserEntity
import com.bory.todolistapi.repository.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

@SpringBootApplication
class TodolistApiApplication(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Bean
    fun dataLoader(): ApplicationRunner = ApplicationRunner {
        val entity = UserEntity(
            username = "user",
            password = passwordEncoder.encode("user!@#"),
            enabled = true,
            createdTime = LocalDateTime.now(),
            modifiedTime = LocalDateTime.now()
        )

        userRepository.save(entity)
    }

}

fun main(args: Array<String>) {
    runApplication<TodolistApiApplication>(*args)
}
