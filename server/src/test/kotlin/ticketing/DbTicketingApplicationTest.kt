package com.lab2.server.ticketing

import com.lab2.server.data.Priority
import com.lab2.server.data.Ticket
import com.lab2.server.data.toProduct
import com.lab2.server.data.toProfile
import com.lab2.server.dto.ProductDTO
import com.lab2.server.dto.ProfileDTO
import com.lab2.server.repositories.ProductRepository
import com.lab2.server.repositories.ProfileRepository
import com.lab2.server.repositories.TicketingRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

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


    @BeforeEach
    fun populateRepositories(){
        val product = ProductDTO("1234567890123456", "product1", "p1")
        val profile = ProfileDTO("test1@test.com", "test1", "test")

        ProfileRepository.save(profile.toProfile())
        ProductRepository.save(product.toProduct())
    }

    @AfterEach
    fun deleteRepositories(){
        TicketingRepository.deleteAll()
        ProfileRepository.deleteAll()
        ProductRepository.deleteAll()
    }

    @Test
    fun testGetTicketID() {
        val prod = ProductRepository.findByIdOrNull("1234567890123456")!!
        val prof = ProfileRepository.findByIdOrNull("test1@test.com")!!
        var actualTicket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)

        actualTicket = TicketingRepository.save(actualTicket)
        assertEquals((actualTicket.id), 1)
    }

    @Test
    fun testTicketCount() {
        val prod = ProductRepository.findByIdOrNull("1234567890123456")!!
        val prof = ProfileRepository.findByIdOrNull("test1@test.com")!!
        val actualTicket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)

        TicketingRepository.save(actualTicket)
        assertEquals((TicketingRepository.count()), 1)
    }

    @Test
    fun testTicketGetAllCount() {
        val product = ProductDTO("1234567890123457", "product2", "p2")
        val profile = ProfileDTO("test2@test.com", "test2", "test2")

        ProfileRepository.save(profile.toProfile())
        ProductRepository.save(product.toProduct())

        val prod = ProductRepository.findByIdOrNull("1234567890123456")!!
        val prof = ProfileRepository.findByIdOrNull("test1@test.com")!!

        val actualTicket1 = Ticket("", "", Priority.TOASSIGN, prof, null, prod)
        val actualTicket2 = Ticket("", "", Priority.TOASSIGN, profile.toProfile(), null, product.toProduct())
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

        var prod2 = ProductRepository.findByIdOrNull("1234567890123456")!!
        val prof2 = ProfileRepository.findByIdOrNull("test1@test.com")!!
        var actualTicket = Ticket("", "", Priority.TOASSIGN, prof2, null, prod2)

        actualTicket.addProduct(prod2)
        actualTicket = TicketingRepository.save(actualTicket)
        ProductRepository.save(prod2)
        prod2 = ProductRepository.findByIdOrNull("1234567890123456")!!
        assertEquals((prod2.tickets.contains(actualTicket)), true)
    }

    @Test
    fun testCheckProfileContainsTicket() {
        val prod = ProductRepository.findByIdOrNull("1234567890123456")!!
        val prof = ProfileRepository.findByIdOrNull("test1@test.com")!!
        var actualTicket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)
        actualTicket.addProfile(prof)

        actualTicket = TicketingRepository.save(actualTicket)
        assertEquals((prof.tickets.contains(actualTicket)), true)
    }
    /*
    testInsertTicket -> check whether the ticket contains the other entities
    X testCheckProfileContainsTicket
    testTicketGetAllCount -> getAll
    X testProductInsertInTicket
    testCheckProductContainsTicket
    testCheckProfileContainsTicket
    test ticket doesn't exist
    test product e profile don't exist when you try to add them to a ticket
    get ticket by ID
    change status to a ticket
    add expert to a ticket and check if the expert contains the ticket
    change priority

     */

}