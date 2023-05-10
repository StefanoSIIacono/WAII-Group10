package com.lab2.server.ticketing

import com.lab2.server.dto.ProductDTO
import com.lab2.server.data.Priority
import com.lab2.server.data.Ticket
import com.lab2.server.data.toProduct
import com.lab2.server.dto.ProfileDTO
import com.lab2.server.repositories.ProfileRepository
import com.lab2.server.data.toProfile
import com.lab2.server.repositories.ProductRepository
import com.lab2.server.repositories.TicketingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.junit.Ignore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Testcontainers
@SpringBootTest (webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class DbTicketingApplicationTest {
    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate,ddl-auto") {"create-drop"}
        }

    }
    @LocalServerPort
    protected var port: Int = 0
    @Autowired
    lateinit var restTemplate: TestRestTemplate
    @Autowired
    lateinit var TicketingRepository: TicketingRepository
    @Autowired
    lateinit var ProductRepository: ProductRepository
    @Autowired
    lateinit var ProfileRepository: ProfileRepository

    @Test
    @Ignore
    fun `when record is saved then the id is populated`() {
        val product = ProductDTO("1234567890123456", "product1", "p1")
        var profile = ProfileDTO("test1@test.com", "test1", "test")

        var prof = profile.toProfile()
        ProfileRepository.save(prof)

        var prod = product.toProduct()
        ProductRepository.save(prod)

        var actualTicket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)
        //actualTicket.addProduct(prod)
        prof.addTicket(actualTicket)

        actualTicket = TicketingRepository.save(actualTicket)
        assertEquals((actualTicket.getId()), 1)
        //assertEquals((TicketingRepository.count()), 1)
    }

    @Test
    fun `when record is saved then it's returned successfully`() {
        val product = ProductDTO("1234567890123456", "product1", "p1")
        var profile = ProfileDTO("test1@test.com", "test1", "test")

        var prof = profile.toProfile()
        ProfileRepository.save(prof)

        var prod = product.toProduct()
        ProductRepository.save(prod)

        var actualTicket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)
        //actualTicket.addProduct(prod)
        prof.addTicket(actualTicket)

        actualTicket = TicketingRepository.save(actualTicket)
        println(prod)
        println(actualTicket.product)
        assertEquals((actualTicket.product), prod)
        //assertEquals((TicketingRepository.count()), 1)
    }

    /* TO BE IMPLEMENTED
    @Test
    @Ignore
    fun `create a message and then map it to the ticket` () {

        val service = TicketServiceImpl(TicketingRepository)

        val product = ProductDTO("1234567890123456", "product1", "p1")
        val profile = ProfileDTO("test1@test.com", "test1", "test")

        var prof = profile.toProfile()
        ProfileRepository.save(prof)

        var prod = product.toProduct()
        ProductRepository.save(prod)

        var actualTicket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)
        //actualTicket.addProduct(prod)
        prod.addTicket(actualTicket)
        prof.addTicket(actualTicket)

        val message = Message (Date(System.currentTimeMillis()), "Message", "chatter", actualTicket)
        actualTicket.addMessage(message)
        actualTicket = TicketingRepository.save(actualTicket)

        val messages = mutableListOf<Message>().add(message)
        assertEquals((service.getMessagesByTicketId(actualTicket.getId())), messages)
    }*/
}