package com.example.game.integration

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class NewGameIntegrationTest {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private var mockMvc: MockMvc? = null

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun `newGame should return game state with only zeros`() {
        val expectedResult = """{"gameState":"0,0,0,0,0,0,0,0,0","winner":0,"gameOver":false}"""

        mockMvc!!.perform(
                MockMvcRequestBuilders
                    .get(URI("/newGame"))
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect { it.response.contentAsString.shouldBe(expectedResult) }
    }
}