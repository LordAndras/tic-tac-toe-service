package com.example.game.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ObjectMapperConfig {

    @Bean
    open fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.registerKotlinModule()
        return mapper
    }
}