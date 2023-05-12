package com.lab2.server.ticketing

import com.lab2.server.data.*import com.lab2.server.repositories.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
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
import java.util.*

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
    lateinit var ticketingRepository: TicketingRepository
    @Autowired
    lateinit var productRepository: ProductRepository
    @Autowired
    lateinit var profileRepository: ProfileRepository
    @Autowired
    lateinit var expertRepository: ExpertRepository
    @Autowired
    lateinit var expertiseRepository: ExpertiseRepository

    val priority = Priority.MEDIUM
    val statusChanger = StatusChanger.PROFILE

    @BeforeEach
    fun populateRepositories(){
        val product = Product("1234567890123456", "product1", "p1")
        val profile = Profile("test1@test.com", "test1", "test")
        val expert = Expert("expert", "expert")
        val expertise = Expertise("expertise")

        profileRepository.save(profile)
        productRepository.save(product)
        expertRepository.save(expert)
        expertiseRepository.save(expertise)

        val ticket = Ticket("obj", "arg", priority, profile, expert, product)
        val status = TicketStatus(Status.OPEN, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)
        ticketingRepository.save(ticket)
    }

    @AfterEach
    fun deleteRepositories(){
        ticketingRepository.deleteAll()
        profileRepository.deleteAll()
        productRepository.deleteAll()
        expertRepository.deleteAll()
        expertiseRepository.deleteAll()
    }

    @Test
    fun `creating a ticket maps the other entities correctly`() {

        var expert = Expert("expert", "expert")
        expert = expertRepository.save(expert)

        expert = expertRepository.findByIdOrNull(expert.id)!!

        val product = productRepository.findByIdOrNull("1234567890123456")!!
        val profile = profileRepository.findByIdOrNull("test1@test.com")!!

        val ticket = Ticket("obj", "arg", priority, profile, expert, product)
        val status = TicketStatus(Status.OPEN, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)

        val actualTicket = ticketingRepository.save(ticket)

        assertNotNull(actualTicket.id)
        assertEquals((actualTicket.product), product)
        assertEquals((actualTicket.profile), profile)
        assertEquals((actualTicket.expert), expert)
        assertEquals((actualTicket.statusHistory.size), 1)
        assertEquals((actualTicket.statusHistory[0]), status)
    }

    /*@Test
    fun testTicketCount() {
        val prod = ProductRepository.findByIdOrNull("1234567890123456")!!
        val prof = ProfileRepository.findByIdOrNull("test1@test.com")!!
        val actualTicket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)

        TicketingRepository.save(actualTicket)
        assertEquals((TicketingRepository.count()), 1)
    }*/

    @Test
    fun `after inserting 2 more tickets the count is correct`() {

        val prod = productRepository.findByIdOrNull("1234567890123456")!!
        val prof = profileRepository.findByIdOrNull("test1@test.com")!!

        val actualTicket1 = Ticket("", "", Priority.TOASSIGN, prof, null, prod)
        val actualTicket2 = Ticket("", "", Priority.TOASSIGN, prof, null, prod)
        ticketingRepository.save(actualTicket1)
        ticketingRepository.save(actualTicket2)

        assertEquals((ticketingRepository.count()), 3)
    }

    /*@Test
    fun `inserting a product and profile into the ticket maps the ticket to them`() {
        var prod = ProductRepository.findByIdOrNull("1234567890123456")!!
        var prof = ProfileRepository.findByIdOrNull("test1@test.com")!!

        var actualTicket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)

        actualTicket = TicketingRepository.save(actualTicket)

        prod = ProductRepository.findByIdOrNull("1234567890123456")!!
        prof = ProfileRepository.findByIdOrNull("test1@test.com")!!
        println(prod.tickets[1])
        println(prof.tickets[1])
        //val ticketsProd = prod.tickets.contains(actualTicket)
        //val ticketsProf = prof.tickets.contains(actualTicket)

        //assertEquals(ticketsProd, true)
        //assertEquals(ticketsProf, true)
        //assertEquals(ticketsProd[1], actualTicket)
        //assertEquals(ticketsProf[1], actualTicket)

    }*/

    @Test
    fun `when the ticket isn't in the repository returns null`() {
        val actualTicket = ticketingRepository.findByIdOrNull(10000000)
        assertNull(actualTicket)
    }

    @Test
    fun `when the ticket is already in the repository the id doesn't change`() {
        val prod = productRepository.findByIdOrNull("1234567890123456")!!
        val prof = profileRepository.findByIdOrNull("test1@test.com")!!

        val actualTicket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)

        val pastTicket = ticketingRepository.save(actualTicket)

        val product = Product("1234567890123456", "product1", "p1")
        productRepository.save(product)
        actualTicket.product = product
        val newTicket = ticketingRepository.save(actualTicket)

        assertEquals(newTicket.id, pastTicket.id)
    }
    /*
    (TODO) testCheckProductContainsTicket
    (TODO) testCheckProfileContainsTicket
    test product e profile don't exist when you try to add them to a ticket
    get ticket by ID
    change status to a ticket
    add expert to a ticket and check if the expert contains the ticket
    change priority

     */
}