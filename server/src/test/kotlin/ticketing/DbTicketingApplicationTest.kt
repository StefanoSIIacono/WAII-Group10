package com.lab2.server.ticketing

import com.lab2.server.dto.*
import com.lab2.server.data.*
import com.lab2.server.repositories.*
import com.lab2.server.serviceImpl.*
import com.lab2.server.services.*
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
    fun testGetTicketID() {
        TicketingRepository.deleteAll()
        ProfileRepository.deleteAll()
        ProductRepository.deleteAll()


        val product = ProductDTO("1234567890123456", "product1", "p1")
        val profile = ProfileDTO("test1@test.com", "test1", "test")

        val prof = profile.toProfile()
        ProfileRepository.save(prof)

        val prod = product.toProduct()
        ProductRepository.save(prod)

        var actualTicket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)
        //actualTicket.addProduct(prod)
        //prof.addTicket(actualTicket)

        actualTicket = TicketingRepository.save(actualTicket)
        assertEquals((actualTicket.id), 1)
    }

    @Test
    fun testTicketCount() {
        TicketingRepository.deleteAll()
        ProfileRepository.deleteAll()
        ProductRepository.deleteAll()

        val product = ProductDTO("1234567890123457", "product2", "p2")
        val profile = ProfileDTO("test2@test.com", "test2", "test2")

        val prof = profile.toProfile()
        ProfileRepository.save(prof)

        val prod = product.toProduct()
        ProductRepository.save(prod)

        val actualTicket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)

        TicketingRepository.save(actualTicket)
        assertEquals((TicketingRepository.count()), 1)
    }

    @Test
    fun testTicketGetAllCount() {
        TicketingRepository.deleteAll()
        ProfileRepository.deleteAll()
        ProductRepository.deleteAll()

        val product1 = ProductDTO("1234567890123456", "product1", "p1")
        val product2 = ProductDTO("1234567890123457", "product2", "p2")
        val profile1 = ProfileDTO("test1@test.com", "test1", "test1")
        val profile2 = ProfileDTO("test2@test.com", "test2", "test2")

        val prof1 = profile1.toProfile()
        val prof2 = profile2.toProfile()
        ProfileRepository.save(prof1)
        ProfileRepository.save(prof2)

        val prod1 = product1.toProduct()
        val prod2 = product2.toProduct()
        ProductRepository.save(prod1)
        ProductRepository.save(prod2)


        val actualTicket1 = Ticket("", "", Priority.TOASSIGN, prof1, null, prod1)
        val actualTicket2 = Ticket("", "", Priority.TOASSIGN, prof2, null, prod2)
        TicketingRepository.save(actualTicket1)
        TicketingRepository.save(actualTicket2)

        assertEquals((TicketingRepository.count()), 2)
    }

    @Test
    fun testProductInsertInTicket() {
        TicketingRepository.deleteAll()
        ProfileRepository.deleteAll()
        ProductRepository.deleteAll()

        val product = ProductDTO("1234567890123456", "product1", "p1")
        val profile = ProfileDTO("test1@test.com", "test1", "test")

        val prof = profile.toProfile()
        ProfileRepository.save(prof)

        val prod = product.toProduct()
        ProductRepository.save(prod)

        var actualTicket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)

        actualTicket = TicketingRepository.save(actualTicket)
        assertEquals((actualTicket.product), prod)
    }

    @Test
    fun testCheckProductContainsTicket() {
        TicketingRepository.deleteAll()
        ProfileRepository.deleteAll()
        ProductRepository.deleteAll()

        val product = ProductDTO("1234567890123456", "product1", "p1")
        val profile = ProfileDTO("test1@test.com", "test1", "test")

        val prof = profile.toProfile()
        ProfileRepository.save(prof)

        val prod = product.toProduct()
        ProductRepository.save(prod)

        var actualTicket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)
        actualTicket.addProduct(prod)

        actualTicket = TicketingRepository.save(actualTicket)
        assertEquals((prod.tickets.contains(actualTicket)), true)
    }

    @Test
    fun testCheckProfileContainsTicket() {
        TicketingRepository.deleteAll()
        ProfileRepository.deleteAll()
        ProductRepository.deleteAll()

        val product = ProductDTO("1234567890123456", "product1", "p1")
        val profile = ProfileDTO("test1@test.com", "test1", "test")

        val prof = profile.toProfile()
        ProfileRepository.save(prof)

        val prod = product.toProduct()
        ProductRepository.save(prod)

        var actualTicket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)
        actualTicket.addProfile(prof)

        actualTicket = TicketingRepository.save(actualTicket)
        assertEquals((prof.tickets.contains(actualTicket)), true)
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