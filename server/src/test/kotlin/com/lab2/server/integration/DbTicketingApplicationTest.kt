package com.lab2.server.integration

import com.lab2.server.data.*
import com.lab2.server.dto.*
import com.lab2.server.repositories.*
import dasniko.testcontainers.keycloak.KeycloakContainer
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*
import kotlin.math.ceil
import kotlin.math.min


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DbTicketingApplicationTest {
    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")


        //@JsonIgnoreProperties(ignoreUnknown = true)
        @Container
        val keycloak: KeycloakContainer = KeycloakContainer()
            .withRealmImportFile(
                "/keycloak_settings/ticketing-realm.json"
            )

        // application properties used and defined specifically for tests
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate,ddl-auto") { "create-drop" }
            registry.add("spring.security.oauth2.resourceserver.jwt.url") {
                keycloak.authServerUrl.replace(
                    Regex("/$"),
                    ""
                )
            }
            registry.add("jwt.auth.converter.resource-id") { "ticketingclient" }
            registry.add("jwt.auth.converter.principal-attribute") { "preferred_username" }
            registry.add("keycloack.enabled") { true }
        }

    }

    // Used to force a random port
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

    @Autowired
    lateinit var managerRepository: ManagerRepository

    @Autowired
    lateinit var messageRepository: MessageRepository

    val NUMBEROFENTITIES = 4

    val expertises = mutableListOf<Expertise>()
    val experts = mutableListOf<Expert>()
    val managers = mutableListOf<Manager>()
    val products = mutableListOf<Product>()
    val profiles = mutableListOf<Profile>()
    val tickets = mutableListOf<Ticket>()

    val mainExpert = Expert("mastro.gesualdo@tickets.com", "mastro", "gesualdo")
    val mainManager = Manager("bigboss@tickets.admin.com", "big", "boss")
    val mainProfile =
        Profile("luigi.verdi@gmail.com", "luigi", "verdi", Address("address", "zipCode", "city", "country"))

    @BeforeEach
    @Transactional
    fun setUp() {
        ticketingRepository.deleteAll()
        profileRepository.deleteAll()
        productRepository.deleteAll()
        expertRepository.deleteAll()
        expertiseRepository.deleteAll()
        messageRepository.deleteAll()
        val requestFactory = SimpleClientHttpRequestFactory()
        requestFactory.setOutputStreaming(false)
        restTemplate.restTemplate.requestFactory = requestFactory


        for (i in 0 until NUMBEROFENTITIES) {
            val expertise = Expertise("expertise$i")
            expertises.add(expertise)
            expertiseRepository.save(expertise)
        }


        experts.add(mainExpert)
        expertises.subList(0, Random().nextInt(1, NUMBEROFENTITIES)).forEach { mainExpert.addExpertise(it) }
        managers.add(mainManager)
        profiles.add(mainProfile)

        expertRepository.save(mainExpert)
        managerRepository.save(mainManager)
        mainProfile.address.profile = mainProfile
        profileRepository.save(mainProfile)

        for (i in 0 until NUMBEROFENTITIES) {
            val expert = (Expert(
                "expertFake$i@expert.com",
                "nameExpert$i",
                "surnameExpert$i",
                expertises.subList(i, min(i + Random().nextInt(1, 3), NUMBEROFENTITIES - 1)).toMutableSet()
            ))
            experts.add(expert)
            expertRepository.save(expert)

        }

        for (i in 0 until NUMBEROFENTITIES) {
            val manager = Manager("manager$i@manager.com", "nameManager$i", "surnameManager$i")
            managers.add(manager)
            managerRepository.save(manager)
        }

        for (i in 0 until NUMBEROFENTITIES) {
            val product = Product("id$i", "product$i", "brand$i")
            products.add(product)
            productRepository.save(product)
        }

        for (i in 0 until NUMBEROFENTITIES) {
            val profile = Profile(
                "profile$i@profile.com",
                "nameProfile$i",
                "surnameProfile$i",
                Address("address$i", "zipCode$i", "city$i", "country$i")
            )
            profile.address.profile = profile
            profiles.add(profile)
            profileRepository.save(profile)
        }

        for ((expert, profile) in listOf(Pair(mainExpert, mainProfile), Pair(experts[1], profiles[1]))) {
            val statusChanges = listOf(
                Triple(TicketStatus(Status.OPEN, Date(System.currentTimeMillis())), Roles.PROFILE, null),
                Triple(
                    TicketStatus(Status.IN_PROGRESS, Date(System.currentTimeMillis().inc())),
                    Roles.MANAGER,
                    expert
                ),
                Triple(TicketStatus(Status.RESOLVED, Date(System.currentTimeMillis().plus(2))), Roles.EXPERT, null),
                Triple(TicketStatus(Status.CLOSED, Date(System.currentTimeMillis().plus(3))), Roles.PROFILE, null),
                Triple(TicketStatus(Status.REOPENED, Date(System.currentTimeMillis().plus(4))), Roles.PROFILE, null)
            )
            for (statusChange in statusChanges) {
                val product = products[0]

                val ticket = Ticket(
                    "obj",
                    expert.expertises.first(),
                    null,
                    profile,
                    null,
                    product
                )

                for (status in statusChanges) {
                    ticket.addStatus(statusChange.first, statusChange.second, statusChange.third)
                    if (status.first.status === Status.IN_PROGRESS) {
                        var time = System.currentTimeMillis()
                        for (j in 0 until NUMBEROFENTITIES) {
                            val attachment = "WnK3mmyVMFFMZInbdSP8wQ=="
                            val message = Message(
                                j,
                                Date(time),
                                "body${j}",
                                mutableListOf(Attachment(attachment, attachment.length.toLong(), "contentType"))
                            )
                            if (j % 2 == 0) {
                                ticket.addMessageFromProfile(message)
                            } else {
                                ticket.addMessageFromExpert(message)
                            }
                            time = time.inc()
                        }
                    }
                    if (status.first.status === statusChange.first.status) {
                        break
                    }
                }

                tickets.add(ticket)
                ticketingRepository.save(ticket)
            }
        }
    }

    fun getBearerToken(role: Roles): String {
        val username =
            if (role === Roles.MANAGER) "bigboss@tickets.admin.com" else if (role === Roles.EXPERT) "mastro.gesualdo@tickets.com" else "luigi.verdi@gmail.com"
        val requestEntity: HttpEntity<LoginDTO> = HttpEntity(LoginDTO(username, "password"))
        val resp = restTemplate.postForEntity(
            "/login",
            requestEntity,
            TokenDTO::class.java
        )
        return "Bearer " + resp.body?.access_token
    }

    private inline fun <reified T> getRequest(path: String, role: Roles? = null): ResponseEntity<T> {
        val headers = HttpHeaders()
        if (role !== null) {
            headers.set(
                "Authorization",
                getBearerToken(role)
            )
        }
        val getEntity: HttpEntity<String> = HttpEntity(null, headers)
        return restTemplate.exchange(
            path,
            HttpMethod.GET,
            getEntity,
            object : ParameterizedTypeReference<T>() {}
        )
    }

    private inline fun <R, reified T> postRequest(path: String, body: R?, role: Roles? = null): ResponseEntity<T> {
        val headers = HttpHeaders()
        if (role !== null) {
            headers.set(
                "Authorization",
                getBearerToken(role)
            )
        }
        headers.contentType = MediaType.APPLICATION_JSON
        val postEntity: HttpEntity<R> = HttpEntity(body, headers)
        return restTemplate.postForEntity(
            path,
            postEntity,
            T::class.java
        )
    }

    private inline fun <R, reified T> putRequest(path: String, body: R?, role: Roles? = null): ResponseEntity<T> {
        val headers = HttpHeaders()
        if (role !== null) {
            headers.set(
                "Authorization",
                getBearerToken(role)
            )
        }
        val postEntity: HttpEntity<R> = HttpEntity(body, headers)
        return restTemplate.exchange(
            path,
            HttpMethod.PUT,
            postEntity,
            T::class.java
        )
    }

    private inline fun <R, reified T> deleteRequest(path: String, body: R?, role: Roles? = null): ResponseEntity<T> {
        val headers = HttpHeaders()
        if (role !== null) {
            headers.set(
                "Authorization",
                getBearerToken(role)
            )
        }
        val deleteEntity: HttpEntity<R> = HttpEntity(body, headers)
        return restTemplate.exchange(
            path,
            HttpMethod.DELETE,
            deleteEntity,
            T::class.java,
        )
    }

    @Test
    fun `containers are up and running`() {
        assertTrue(postgres.isRunning)
        assertTrue(keycloak.isRunning)
    }


    // EXPERTS TEST

    @Test
    fun testGetAllExperts() {

        val response = getRequest<PagedDTO<ExpertDTO>>("/experts", Roles.MANAGER)


        val body = response.body!!

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(body)
        assertEquals(body.meta.currentPage, 1)
        assertEquals(body.meta.totalElements, experts.size)
        assertEquals(body.meta.totalPages, ceil(experts.size.toDouble() / 100).toInt())
        assertEquals(body.data.size, min(experts.size, 100))
    }

    @Test
    fun testGetAllExpertsPage2() {

        val response = getRequest<PagedDTO<ExpertDTO>>("/experts?page=2&offset=10", Roles.MANAGER)
        val body = response.body!!

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(body)
        assertEquals(body.meta.currentPage, 2)
        assertEquals(body.meta.totalElements, 0)
        assertEquals(body.meta.totalPages, ceil(experts.size.toDouble() / 10).toInt())
        assertEquals(body.data.size, 0)
    }

    @Test
    fun testGetExpertByEmail() {
        val expert = experts[1].toDTO()
        val response = getRequest<ExpertDTO>("/experts/${expert.email}", Roles.MANAGER)
        val body = response.body!!

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(body)
        assert(body == expert)
    }

    @Test
    fun testGetExpertByEmail_ExpertNotFound() {

        val response = getRequest<String>("/experts/notExistentExpert", Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testCreateExpert() {
        val expertToBeCreated = ExpertDTO(
            "newExpert@ticketing.com",
            "new",
            "expert",
            mutableSetOf(expertises[2].toDTO(), expertises[3].toDTO())
        )
        val response = postRequest<CreateExpertDTO, String>(
            "/expert",
            CreateExpertDTO(
                expertToBeCreated.email,
                "password",
                expertToBeCreated.name,
                expertToBeCreated.surname,
                expertToBeCreated.expertises.map { it.field }.toMutableSet()
            ),
            Roles.MANAGER
        )

        assertNotNull(response)
        assertEquals(HttpStatus.CREATED, response.statusCode)

        val response2 = getRequest<ExpertDTO>("/experts/${expertToBeCreated.email}", Roles.MANAGER)
        val body2 = response2.body!!

        assertEquals(response2.statusCode, HttpStatus.OK)
        assertNotNull(body2)
        assert(body2 == expertToBeCreated)

        val response3 =
            postRequest<LoginDTO, TokenDTO>("/login", LoginDTO(expertToBeCreated.email, "password"), Roles.MANAGER)
        assertEquals(response3.statusCode, HttpStatus.OK)
    }

    @Test
    fun testCreateExpert_NoBodyFailure() {

        val response = postRequest<CreateExpertDTO, String>(
            "/expert",
            null,
            Roles.MANAGER
        )

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testAddExpertiseToExpert() {
        val expert = experts[1].toDTO()
        val expertise = expertises.find { !expert.expertises.any { it2 -> it.field == it2.field } }!!.toDTO()
        val response = putRequest<ExpertiseDTO, String>("/experts/${expert.email}/expertise", expertise, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)

        val response2 = getRequest<ExpertDTO>("/experts/${expert.email}", Roles.MANAGER)
        val body = response2.body!!

        assertEquals(response2.statusCode, HttpStatus.OK)
        assertNotNull(body)
        assert(body.expertises.toList().any { it.field == expertise.field })
    }

    @Test
    fun testAddExpertiseToExpert_NoBodyFailure() {

        val expert = experts[1].toDTO()
        val response = putRequest<ExpertiseDTO, String>("/experts/${expert.email}/expertise", null, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testAddExpertiseToExpert_ExpertNotFoundFailure() {
        val expertise = ExpertiseDTO("reading")
        val response = putRequest<ExpertiseDTO, String>("/experts/notExistentEmail/expertise", expertise, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testDeleteExpertiseFromExpert() {
        val expert = experts[1].toDTO()

        val expertise = expert.expertises.first()
        val response =
            deleteRequest<ExpertiseDTO, String>("/experts/${expert.email}/expertise", expertise, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.statusCode)

        val response2 = getRequest<ExpertDTO>("/experts/${expert.email}", Roles.MANAGER)
        val body = response2.body!!

        assertEquals(response2.statusCode, HttpStatus.OK)
        assertNotNull(body)

        assert(!body.expertises.toList().any { it.field == expertise.field })
    }

    @Test
    fun testDeleteExpertiseFromExpert_NoBodyFailure() {

        val expert = experts[1].toDTO()
        val response = deleteRequest<ExpertiseDTO, String>("/experts/${expert.email}/expertise", null, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testDeleteExpertiseFromExpert_ExpertNotFoundFailure() {
        val expertise = ExpertiseDTO("reading")
        val response =
            deleteRequest<ExpertiseDTO, String>("/experts/notExistentEmail/expertise", expertise, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    // EXPERTISE TESTS

    @Test
    fun testGetAllExpertises() {
        val response = getRequest<PagedDTO<ExpertiseDTO>>("/expertises", Roles.PROFILE)

        val body = response.body!!

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(body)
        assertEquals(body.meta.currentPage, 1)
        assertEquals(body.meta.totalElements, expertises.size)
        assertEquals(body.meta.totalPages, ceil(expertises.size.toDouble() / 100).toInt())
        assertEquals(body.data.size, min(expertises.size, 100))
    }

    @Test
    fun testGetExpertsByExpertise() {
        val expertise = expertises[0]
        val experts = experts.filter { exp -> exp.expertises.any { it.field == expertise.field } }
        val response = getRequest<PagedDTO<ExpertDTO>>("/expertises/${expertise.field}/experts", Roles.MANAGER)

        val body = response.body!!

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(body)
        assertEquals(body.meta.currentPage, 1)
        assertEquals(body.meta.totalElements, experts.size)
        assertEquals(body.meta.totalPages, ceil(experts.size.toDouble() / 100).toInt())
        assertEquals(body.data.size, min(experts.size, 100))
    }

    @Test
    fun testGetExpertsByExpertise_ExpertiseNotFoundFailure() {
        val response = getRequest<String>("/expertises/notExistentExpertise/experts", Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testCreateExpertise() {
        val expertiseToBeCreated = ExpertiseDTO("newExpertise")
        val response = postRequest<ExpertiseDTO, String>("/expertises", expertiseToBeCreated, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.CREATED, response.statusCode)

        val response2 = getRequest<PagedDTO<ExpertiseDTO>>("/expertises", Roles.MANAGER)

        val body2 = response2.body!!

        assertEquals(response2.statusCode, HttpStatus.OK)
        assertNotNull(body2)
        assert(body2.data.any { it.field == expertiseToBeCreated.field })

    }

    @Test
    fun testCreateExpertise_NoBodyFailure() {
        val response = postRequest<ExpertiseDTO, String>("/expertises", null, Roles.MANAGER)


        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testDeleteExpertise() {
        val neverUsedExpertise = Expertise("neverUsed")
        expertiseRepository.save(neverUsedExpertise)
        val response = deleteRequest<String, String>("/expertises/${neverUsedExpertise.field}", null, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun testDeleteExpertise_ExpertiseNotFoundFailure() {
        val response = deleteRequest<String, String>("/expertises/notExistentExpertise", null, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    // MESSAGES TEST

    @Test
    fun testGetMessages() {
        val expert = mainExpert
        val ticket = tickets.find { it.expert?.email == expert.email }!!

        val response = getRequest<PagedDTO<MessageDTO>>("/tickets/${ticket.id}/messages", Roles.EXPERT)

        val body = response.body!!

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(body)
        assertEquals(body.meta.currentPage, 1)
        assertEquals(body.meta.totalElements, ticket.messages.size)
        assertEquals(body.meta.totalPages, ceil(ticket.messages.size.toDouble() / 100).toInt())
        assertEquals(body.data.size, min(ticket.messages.size, 100))
    }

    @Test
    fun testGetMessages_WrongExpert() {
        val expert = mainExpert
        val ticket = tickets.find { it.expert !== null && it.expert!!.email != expert.email }!!

        val response = getRequest<String>("/tickets/${ticket.id}/messages", Roles.EXPERT)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testGetMessages_WrongProfile() {
        val profile = mainProfile
        val ticket = tickets.find { it.profile.email != profile.email }!!

        val response = getRequest<String>("/tickets/${ticket.id}/messages", Roles.PROFILE)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testCreateMessage() {
        val expert = mainExpert
        val ticket = tickets.find { it.expert?.email == expert.email }!!
        val message = BodyMessageDTO(
            "HI",
            mutableListOf(AttachmentBodyDTO("mPIEG7nizgIuQdTt/fPDsQ==", "jpg"))
        )

        val response =
            postRequest<BodyMessageDTO, String>("/tickets/${ticket.id}/messages", message, Roles.EXPERT)

        assertNotNull(response)
        assertEquals(response.statusCode, HttpStatus.CREATED)

        val response2 = getRequest<PagedDTO<MessageDTO>>("/tickets/${ticket.id}/messages", Roles.EXPERT)

        val body = response2.body!!

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(body)
        assertEquals(body.meta.currentPage, 1)
        assertEquals(body.meta.totalElements, ticket.messages.size + 1)
        assertEquals(body.meta.totalPages, ceil((ticket.messages.size + 1).toDouble() / 100).toInt())
        assertEquals(body.data.size, min(ticket.messages.size + 1, 100))
        assertEquals(body.data[0].body, message.body)
        assertEquals(body.data[0].expert?.email, expert.email)
        assertEquals(body.data[0].index, ticket.messages.last().index + 1)
        assertEquals(body.data[0].attachments.size, 1)
    }

    @Test
    fun testCreateMessage_WrongExpert() {
        val expert = mainExpert
        val ticket = tickets.find { it.expert !== null && it.expert!!.email != expert.email }!!
        val message = BodyMessageDTO(
            "HI",
            mutableListOf()
        )

        val response =
            postRequest<BodyMessageDTO, String>("/tickets/${ticket.id}/messages", message, Roles.EXPERT)

        assertNotNull(response)
        assertEquals(response.statusCode, HttpStatus.NOT_FOUND)
    }

    @Test
    fun testAckMessage() {
        val expert = mainExpert
        val ticket = tickets.find { it.expert?.email == expert.email }!!
        val message = ticket.messages.last()

        val response = putRequest<String, String>(
            "/tickets/${ticket.id}/messages/${message.index}/ack",
            null,
            if (message.expert == null) Roles.EXPERT else Roles.PROFILE
        )

        assertNotNull(response)
        assertEquals(response.statusCode, HttpStatus.OK)

        val response2 =
            getRequest<TicketDTO>("/tickets/${ticket.id}", if (message.expert == null) Roles.EXPERT else Roles.PROFILE)

        val body2 = response2.body!!

        assertNotNull(body2)
        assertEquals(body2.lastReadMessageIndex, message.index)
    }

    // TEST PRODUCTS

    @Test
    fun testGetAllProducts() {

        val response = getRequest<PagedDTO<ProductDTO>>("/products", Roles.PROFILE)
        val body = response.body!!

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(body)
        assertEquals(body.meta.currentPage, 1)
        assertEquals(body.meta.totalElements, products.size)
        assertEquals(body.meta.totalPages, ceil(products.size.toDouble() / 100).toInt())
        assertEquals(body.data.size, min(products.size, 100))
    }

    @Test
    fun testGetProductById() {
        val product = products[1]
        val response = getRequest<ProductDTO>("/products/${product.id}", Roles.PROFILE)
        val body = response.body!!

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(body)
        assert(body == product.toDTO())
    }

    @Test
    fun testGetProductById_NotFound() {
        val response = getRequest<String>("/products/123987123987", Roles.PROFILE)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    // TEST PROFILE

    @Test
    fun testGetAllProfiles() {
        val response = getRequest<PagedDTO<ProfileDTO>>("/profiles", Roles.MANAGER)
        val body = response.body!!

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(body)
        assertEquals(body.meta.currentPage, 1)
        assertEquals(body.meta.totalElements, profiles.size)
        assertEquals(body.meta.totalPages, ceil(profiles.size.toDouble() / 100).toInt())
        assertEquals(body.data.size, min(profiles.size, 100))
    }

    @Test
    fun testGetProfileByEmail() {
        val profile = profiles[0]
        val response = getRequest<ProfileDTO>("/profiles/${profile.email}", Roles.MANAGER)
        val body = response.body!!

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(body)
        assert(body == profile.toDTO())
    }

    @Test
    fun testGetProfileByEmail_EmailNotFoundFailure() {
        val response = getRequest<String>("/profiles/notExistent@email.com", Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testSignup() {
        val address = AddressDTO(
            "newAddress",
            "newZipCode",
            "newCity",
            "newCountry",
        )
        val profile = CreateProfileDTO("newprofile@test.com", "password", "new", "profile", address)


        val response = postRequest<CreateProfileDTO, String>("/signup", profile)

        assertNotNull(response)
        assertEquals(HttpStatus.CREATED, response.statusCode)

        val response2 = getRequest<ProfileDTO>("/profiles/${profile.email}", Roles.MANAGER)
        val body2 = response2.body!!

        assertEquals(response2.statusCode, HttpStatus.OK)
        assertNotNull(body2)
        assert(body2 == ProfileDTO(profile.email, profile.name, profile.surname, profile.address))

        val response3 =
            postRequest<LoginDTO, TokenDTO>("/login", LoginDTO(profile.email, "password"), Roles.MANAGER)
        assertEquals(response3.statusCode, HttpStatus.OK)
    }

    @Test
    fun testSignup_NoBodyFailure() {
        val response = postRequest<CreateProfileDTO, String>("/signup", null)

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testSignup_DuplicatedFailure() {
        val profile = profiles[0]
        val profileCreate =
            CreateProfileDTO(profile.email, "password", profile.name, profile.surname, profile.address.toDTO())
        val response = postRequest<CreateProfileDTO, String>("/signup", profileCreate)

        assertNotNull(response)
        assertEquals(HttpStatus.CONFLICT, response.statusCode)
    }

    @Test
    fun testChangeProfileInfo() {
        val profile = mainProfile
        val profileChange =
            ChangeProfileInfoDTO("change1", "change2", AddressDTO("change2", "change3", "change4", "change5"))

        val response = putRequest<ChangeProfileInfoDTO, String>("/profiles/edit", profileChange, Roles.PROFILE)

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)

        val response2 = getRequest<ProfileDTO>("/profiles/${profile.email}", Roles.MANAGER)
        val body2 = response2.body!!

        assertEquals(response2.statusCode, HttpStatus.OK)
        assertNotNull(body2)
        assertEquals(body2.name, profileChange.name)
        assertEquals(body2.surname, profileChange.surname)
        assertEquals(body2.address, profileChange.address)
    }

    @Test
    fun testChangeProfileInfo_NoBodyFailure() {
        val response = putRequest<ChangeProfileInfoDTO, String>("/profiles/edit", null, Roles.PROFILE)

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testGetTicketsByEmail() {
        val profile = mainProfile
        val tickets = this.tickets.filter { it.profile.email == profile.email }
        val response = getRequest<PagedDTO<TicketDTO>>("/profiles/${profile.email}/tickets", Roles.PROFILE)

        val body = response.body!!

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(body)
        assertEquals(body.meta.currentPage, 1)
        assertEquals(body.meta.totalElements, tickets.size)
        assertEquals(body.meta.totalPages, ceil(tickets.size.toDouble() / 100).toInt())
        assertEquals(body.data.size, min(tickets.size, 100))
    }

    @Test
    fun testGetTicketsByEmail_EmailNotFoundFailure() {
        val response = getRequest<String>("/profiles/notexistent@email.com/tickets", Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testGetMeManager() {

        val response = getRequest<MeDTO>("/me", Roles.MANAGER)
        val body = response.body!!

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(body)
        assertEquals(body.email, mainManager.email)
        assertEquals(body.expertises, null)
        assertEquals(body.address, null)
        assertEquals(body.role, Roles.MANAGER)
    }

    @Test
    fun testGetMeExpert() {

        val response = getRequest<MeDTO>("/me", Roles.EXPERT)
        val body = response.body!!

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(body)
        assertEquals(body.email, mainExpert.email)
        assertEquals(body.expertises, mainExpert.expertises.map { it.toDTO() }.toMutableSet())
        assertEquals(body.address, null)
        assertEquals(body.role, Roles.EXPERT)
    }

    @Test
    fun testGetMeProfile() {
        val response = getRequest<MeDTO>("/me", Roles.PROFILE)
        val body = response.body!!

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(body)
        assertEquals(body.email, mainProfile.email)
        assertEquals(body.expertises, null)
        assertEquals(body.address, mainProfile.address.toDTO())
        assertEquals(body.role, Roles.PROFILE)
    }

    @Test
    fun testGetMe_NotLoggedFailure() {

        val response = getRequest<String>("/me")

        assertNotNull(response)
        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
    }

    // TEST TICKETS

    @Test
    fun testGetAllTicketsManager() {
        val response = getRequest<PagedDTO<TicketDTO>>("/tickets", Roles.MANAGER)
        val body = response.body!!

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(body)
        assertEquals(body.meta.currentPage, 1)
        assertEquals(body.meta.totalElements, tickets.size)
        assertEquals(body.meta.totalPages, ceil(tickets.size.toDouble() / 100).toInt())
        assertEquals(body.data.size, min(100, tickets.size))
    }

    @Test
    fun testGetAllTicketsProfile() {
        val profile = mainProfile
        val tickets = tickets.filter { it.profile.email == profile.email }
        val response = getRequest<PagedDTO<TicketDTO>>("/tickets", Roles.PROFILE)
        val body = response.body!!

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(body)
        assertEquals(body.meta.currentPage, 1)
        assertEquals(body.meta.totalElements, tickets.size)
        assertEquals(body.meta.totalPages, ceil(tickets.size.toDouble() / 100).toInt())
        assertEquals(body.data.size, min(tickets.size, 100))
    }

    @Test
    fun testGetAllTicketsExpert() {
        val expert = mainExpert
        val tickets = tickets.filter { it.expert?.email == expert.email }
        val response = getRequest<PagedDTO<TicketDTO>>("/tickets", Roles.EXPERT)
        val body = response.body!!

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(body)
        assertEquals(body.meta.currentPage, 1)
        assertEquals(body.meta.totalElements, tickets.size)
        assertEquals(body.meta.totalPages, ceil(tickets.size.toDouble() / 100).toInt())
        assertEquals(body.data.size, min(tickets.size, 100))
    }

    @Test
    fun testGetTicketById() {
        val ticket = tickets[0]
        val response = getRequest<TicketDTO>("/tickets/${ticket.id}", Roles.MANAGER)

        val body = response.body!!

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(body)
        assertEquals(body, ticket.toDTO(mainManager.email))
    }

    @Test
    fun testGetTicketById_TicketNotFound() {
        val response = getRequest<String>("/tickets/123456789", Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testCreateTicket() {
        val expertise = expertises[0]
        val product = products[0]
        val ticket = TicketCreateBodyDTO("newObj1234", expertise.field, "body", mutableListOf(), product.id)

        val response = postRequest<TicketCreateBodyDTO, String>("/tickets", ticket, Roles.PROFILE)

        assertNotNull(response)
        assertEquals(HttpStatus.CREATED, response.statusCode)

        val response2 = getRequest<PagedDTO<TicketDTO>>("/tickets", Roles.PROFILE)
        val body2 = response2.body!!

        assertEquals(response2.statusCode, HttpStatus.OK)
        assertNotNull(body2)
        assert(body2.data.any { it.obj == ticket.obj })
    }

    @Test
    fun testCreateTicket_NoBodyFailure() {
        val response = postRequest<TicketCreateBodyDTO, String>("/tickets", null, Roles.PROFILE)

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testCreateTicket_ProductNotFoundFailure() {
        val expertise = expertises[0]
        val ticket = TicketCreateBodyDTO("newObj1234", expertise.field, "body", mutableListOf(), "notExistentProduct")

        val response = postRequest<TicketCreateBodyDTO, String>("/tickets", ticket, Roles.PROFILE)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testOpenTicket() {
        val ticket = tickets.find { it.statusHistory.last().status === Status.IN_PROGRESS }!!
        val response = putRequest<String, String>("/tickets/${ticket.id}/open", null, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)

        val response2 = getRequest<TicketDTO>("/tickets/${ticket.id}", Roles.MANAGER)
        val body = response2.body!!

        assertEquals(body.status.status, Status.OPEN)
        assertEquals(body.expert, null)
    }

    @Test
    fun testOpenTicket_TicketNotFoundFailure() {
        val response = putRequest<String, String>("/tickets/12345678/open", null, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testCloseTicket() {
        val ticket = tickets[0]
        val response = putRequest<String, String>("/tickets/${ticket.id}/close", null, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)

        val response2 = getRequest<TicketDTO>("/tickets/${ticket.id}", Roles.MANAGER)
        val body = response2.body!!

        assertEquals(body.status.status, Status.CLOSED)
        assertEquals(body.expert, null)
    }

    @Test
    fun testCloseTicket_TicketNotFoundFailure() {
        val response = putRequest<String, String>("/tickets/1234567/close", null, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testReOpenTicket() {
        val ticket =
            tickets.find { it.statusHistory.last().status === Status.RESOLVED && it.profile.email == mainProfile.email }!!
        val response = putRequest<String, String>("/tickets/${ticket.id}/reopen", null, Roles.PROFILE)

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)

        val response2 = getRequest<TicketDTO>("/tickets/${ticket.id}", Roles.MANAGER)
        val body = response2.body!!

        assertEquals(body.status.status, Status.REOPENED)
        assertEquals(body.expert, null)
    }

    @Test
    fun testReOpenTicket_TicketNotFoundFailure() {
        val response = putRequest<String, String>("/tickets/123456/reopen", null, Roles.PROFILE)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testResolveTicket() {
        val ticket =
            tickets.find { it.statusHistory.last().status === Status.IN_PROGRESS && it.profile.email == mainProfile.email }!!
        val response = putRequest<String, String>("/tickets/${ticket.id}/resolved", null, Roles.PROFILE)

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)

        val response2 = getRequest<TicketDTO>("/tickets/${ticket.id}", Roles.MANAGER)
        val body = response2.body!!

        assertEquals(body.status.status, Status.RESOLVED)
        assertEquals(body.expert, null)
    }

    @Test
    fun testResolveTicket_TicketNotFoundFailure() {
        val response = putRequest<String, String>("/tickets/1234567/resolved", null, Roles.EXPERT)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testInProgressTicket() {
        val expert = mainExpert
        val ticket = tickets.find { it.statusHistory.last().status === Status.OPEN }!!
        val ticketInProgressBody = TicketInProgressBodyDTO(expert.email, Priority.HIGH)

        val response = putRequest<TicketInProgressBodyDTO, String>(
            "/tickets/${ticket.id}/inprogress",
            ticketInProgressBody,
            Roles.MANAGER
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)

        val response2 = getRequest<TicketDTO>("/tickets/${ticket.id}", Roles.MANAGER)
        val body = response2.body!!

        assertEquals(body.status.status, Status.IN_PROGRESS)
        assertEquals(body.expert, expert.email)
        assertEquals(body.priority, Priority.HIGH)
    }

    @Test
    fun testInProgressTicket_NoBodyFailure() {
        val ticket = tickets.find { it.statusHistory.last().status === Status.OPEN }!!

        val response =
            putRequest<TicketInProgressBodyDTO, String>("/tickets/${ticket.id}/inprogress", null, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testInProgressTicket_TicketNotFoundFailure() {
        val expert = mainExpert
        val ticketInProgressBody = TicketInProgressBodyDTO(expert.email, Priority.HIGH)
        val response = putRequest<TicketInProgressBodyDTO, String>(
            "/tickets/1234567/inprogress",
            ticketInProgressBody,
            Roles.MANAGER
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testInProgressTicket_ExpertNotFoundFailure() {
        val ticket = tickets.find { it.statusHistory.last().status === Status.OPEN }!!
        val ticketInProgressBody = TicketInProgressBodyDTO("notExistent@expert.com", Priority.HIGH)

        val response = putRequest<TicketInProgressBodyDTO, String>(
            "/tickets/${ticket.id}/inprogress",
            ticketInProgressBody,
            Roles.MANAGER
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testSetTicketStatus_IllegalStatusChangeFailure() {
        val expert = mainExpert
        val ticket = tickets.find { it.statusHistory.last().status === Status.RESOLVED }!!
        val ticketInProgressBody = TicketInProgressBodyDTO(expert.email, Priority.HIGH)

        val response = putRequest<TicketInProgressBodyDTO, String>(
            "/tickets/${ticket.id}/inprogress",
            ticketInProgressBody,
            Roles.MANAGER
        )

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testSetTicketStatus_IllegalPriorityFailure() {
        val expert = mainExpert
        val ticket = tickets.find { it.statusHistory.last().status === Status.OPEN }!!
        val ticketInProgressBody = TicketInProgressBodyDTO(expert.email, Priority.TOASSIGN)

        val response = putRequest<TicketInProgressBodyDTO, String>(
            "/tickets/${ticket.id}/inprogress",
            ticketInProgressBody,
            Roles.MANAGER
        )

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testSetTicketPriority() {
        val ticket = tickets.find { it.statusHistory.last().status === Status.IN_PROGRESS }!!
        val response = putRequest<String, String>("/tickets/${ticket.id}/priority/${Priority.LOW}", null, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)

        val response2 = getRequest<TicketDTO>("/tickets/${ticket.id}", Roles.MANAGER)
        val body = response2.body!!

        assertEquals(body.priority, Priority.LOW)
    }

    @Test
    fun testSetTicketPriority_TicketNotFoundFailure() {
        val response =
            putRequest<String, String>("/tickets/1234567/priority/${Priority.LOW}", null, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testSetTicketPriority_IllegalPriorityFailure() {
        val ticket = tickets.find { it.statusHistory.last().status === Status.IN_PROGRESS }!!
        val response = putRequest<String, String>("/tickets/${ticket.id}/priority/illegal", null, Roles.MANAGER)

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testSetTicketPriority_Unauthorized() {
        val ticket = tickets.find { it.statusHistory.last().status === Status.IN_PROGRESS }!!
        val response = putRequest<String, String>("/tickets/${ticket.id}/priority/${Priority.LOW}", null, Roles.PROFILE)

        assertNotNull(response)
        assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
    }
}