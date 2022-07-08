package game.integration

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
class ProcessNextStepIntegrationTest {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private var mockMvc: MockMvc? = null

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun `nextStep should return gameStateResponse with correct data`() {
        val testStep = "1,0,0,0,0,0,0,0,0"
        val expectedResult = """{"gameState":"1,0,0,0,0,0,0,0,0","winner":0,"isGameOver":false}"""

        mockMvc!!.perform(
            MockMvcRequestBuilders
                .post(URI("/nextStep"))
                .contentType("text/plain")
                .content(testStep)
        ).andExpect(MockMvcResultMatchers.status().isOk)
        .andExpect { it.response.contentAsString.shouldBe(expectedResult) }
    }

    @Test
    fun `nextStep should return winner if game is won`() {
        val testStep = "-1,-1,-1,0,0,0,0,0,0"
        val expectedResult = """{"gameState":"-1,-1,-1,0,0,0,0,0,0","winner":-1,"isGameOver":true}"""
        mockMvc!!.perform(
            MockMvcRequestBuilders
                .post(URI("/nextStep"))
                .contentType("text/plain")
                .content(testStep)
        ).andExpect(MockMvcResultMatchers.status().isOk)
        .andExpect { it.response.contentAsString.shouldBe(expectedResult) }
    }

    @Test
    fun `nextStep should return gameEnd if game is over`() {
        val testStep = "-1,1,-1,-1,1,1,1,-1,1"
        val expectedResult = """{"gameState":"-1,1,-1,-1,1,1,1,-1,1","winner":0,"isGameOver":true}"""

        mockMvc!!.perform(
            MockMvcRequestBuilders
                .post(URI("/nextStep"))
                .contentType("text/plain")
                .content(testStep)
        ).andExpect(MockMvcResultMatchers.status().isOk)
        .andExpect { it.response.contentAsString.shouldBe(expectedResult) }
    }

    @Test
    fun `nextStep should return BAD_REQUEST with invalid input`() {
        val testStep = "testStep"

        mockMvc!!.perform(
            MockMvcRequestBuilders
                .post(URI("/nextStep"))
                .contentType("text/plain")
                .content(testStep)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}