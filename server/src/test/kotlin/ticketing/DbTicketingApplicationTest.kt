package com.lab2.server.ticketing

import com.lab2.server.controllers.ExpertController
import com.lab2.server.data.*import com.lab2.server.repositories.*
import jakarta.transaction.Transactional
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
    //val statusChanger = StatusChanger.PROFILE

    @BeforeEach
    fun populateRepositories(){
        var product = Product("1234567890123456", "product1", "p1")
        var profile = Profile("test1@test.com", "test1", "test")
        var expert = Expert("expert", "expert")
        var expertise = Expertise("expertise")

        profile = profileRepository.save(profile)
        product = productRepository.save(product)
        expert = expertRepository.save(expert)
        expertise=expertiseRepository.save(expertise)

        val ticket = Ticket("obj", "arg", priority, profile, expert, product)
        val status = TicketStatus(Status.OPEN, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)
        ticket.addProduct(product)
        ticket.addProfile(profile)
        expert.addTicket(ticket)

        profileRepository.save(profile)
        productRepository.save(product)
        expertRepository.save(expert)
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
    fun `the ticket is returned using the id correctly`() {
        val product = productRepository.findByIdOrNull("1234567890123456")!!
        val profile = profileRepository.findByIdOrNull("test1@test.com")!!

        val ticket = Ticket("obj", "arg", priority, profile, null, product)
        val actualTicket = ticketingRepository.save(ticket)

        val returnedTicket = ticketingRepository.findByIdOrNull(actualTicket.id)
        assertEquals(returnedTicket?.id, actualTicket.id)
    }

    @Test
    fun `creating a ticket maps the other entities to the ticket correctly`() {

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
        assertEquals(product, (actualTicket.product))
        assertEquals(profile, (actualTicket.profile))
        assertEquals(expert, (actualTicket.expert))
        assertEquals(1, (actualTicket.statusHistory.size))
        assertEquals(status, (actualTicket.statusHistory[0]))
    }

    @Test
    fun `after inserting 2 more tickets the count is correct`() {

        val prod = productRepository.findByIdOrNull("1234567890123456")!!
        val prof = profileRepository.findByIdOrNull("test1@test.com")!!

        val actualTicket1 = Ticket("", "", Priority.TOASSIGN, prof, null, prod)
        val actualTicket2 = Ticket("", "", Priority.TOASSIGN, prof, null, prod)
        ticketingRepository.save(actualTicket1)
        ticketingRepository.save(actualTicket2)

        assertEquals(3, (ticketingRepository.count()))
    }

    @Test
    fun `when the ticket isn't in the repository returns null`() {
        val actualTicket = ticketingRepository.findByIdOrNull(10000000)
        assertNull(actualTicket)
    }

    @Test
    fun `when the ticket is already in the repository the id doesn't change`() {
        val prod = productRepository.findByIdOrNull("1234567890123456")!!
        val prof = profileRepository.findByIdOrNull("test1@test.com")!!

        val ticket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)

        val pastTicket = ticketingRepository.save(ticket)

        val product = Product("1234567890123456", "newProduct", "np")
        productRepository.save(product)
        pastTicket.product = product
        val newTicket = ticketingRepository.save(pastTicket)

        assertEquals(newTicket.id, pastTicket.id)
    }

    @Test
    @Transactional
    fun `inserting a product and a profile into the ticket maps the ticket to them`() {
        var prod = productRepository.findByIdOrNull("1234567890123456")!!
        var prof = profileRepository.findByIdOrNull("test1@test.com")!!

        var actualTicket = Ticket("", "", Priority.TOASSIGN, prof, null, prod)
        actualTicket.addProduct(prod)
        actualTicket.addProfile(prof)
        actualTicket = ticketingRepository.save(actualTicket)

        prod = productRepository.findByIdOrNull("1234567890123456")!!
        prof = profileRepository.findByIdOrNull("test1@test.com")!!
        val ticketsProd = prod.tickets.contains(actualTicket)
        val ticketsProf = prof.tickets.contains(actualTicket)


        assertEquals(true, ticketsProd)
        assertEquals(true, ticketsProf)
    }
    @Test
    @Transactional
    fun `mapping a expert to an expertise and viceversa`(){
        var expertise=expertiseRepository.findByField("expertise")
        var expert=expertRepository.findByIdOrNull(1)
        //expert?.addExpertise(expertise!!)
        expertise?.addExpert(expert!!)
        //expert?.expertises?.add(expertise!!)
        //expertise?.experts?.add(expert!!)
        expertRepository.save(expert!!)
        expertiseRepository.save(expertise!!)
        var expertisee=expertiseRepository.findByField("expertise")
        var expertt=expertRepository.findByIdOrNull(1)
        assertEquals(1,expertt?.expertises?.size)
        assertEquals(1, expertisee?.experts?.size )

    }
}