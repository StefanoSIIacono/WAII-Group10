package com.lab2.server.ticketing

import com.lab2.server.data.*
import com.lab2.server.dto.*
import com.lab2.server.repositories.*
import dasniko.testcontainers.keycloak.KeycloakContainer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.*
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
            registry.add("spring.jpa.hibernate,ddl-auto") {"create-drop"}
            registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri")
            { keycloak.authServerUrl + "realms/ticketing" }
            registry.add("jwt.auth.converter.resource-id"){"ticketingclient"}
            registry.add("jwt.auth.converter.principal-attribute"){ "preferred_username"}
            registry.add("keycloack.enabled"){true}
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

    val priority = Priority.TOASSIGN

    // it is used to define collections of objects for testrestemplate REST methods
    private inline fun <reified T: Any> typeRef(): ParameterizedTypeReference<T> = object: ParameterizedTypeReference<T>(){}

    @BeforeEach
    fun setUp(){

        var product = Product(
                "1234567890123456",
                "product1",
                "p1"
        )

        var profile = Profile(
                "test1@test.com",
                "test1",
                "test",
                null
        )
        val address = Address(
                "c",
                "c",
                "z",
                "s",
                "h",
                profile,
                profile.email
        )

        profile.addAddress(address)

        product = productRepository.save(product)
        profile = profileRepository.save(profile)

        var expert = Expert(
                "expert",
                "expert"
        )
        val expertise = Expertise("expertise")

        expert.addExpertise(expertise)
        expertise.addExpert(expert)

        expertiseRepository.save(expertise)
        expert = expertRepository.save(expert)


        val ticket = Ticket(
                "obj",
                "arg",
                priority,
                profile,
                expert,
                product
        )
        val status = TicketStatus(
                Status.OPEN,
                Date(System.currentTimeMillis()),
                ticket
        )

        ticket.addStatus(status)
        ticket.addProduct(product)
        ticket.addProfile(profile)
        expert.addTicket(ticket)

        ticketingRepository.save(ticket)

    }
    fun getBearerToken(username: String, password: String): String {
        val keycloakAdminClient = KeycloakBuilder.builder()
            .serverUrl(keycloak.authServerUrl)
            .realm("ticketing")
            .clientId("ticketingclient")
            .username(username)
            .password(password)
            .build()
        return "Bearer " + keycloakAdminClient.tokenManager().accessToken.token
    }

    @AfterEach
    fun cleanUpRepositories(){
        ticketingRepository.deleteAll()
        profileRepository.deleteAll()
        productRepository.deleteAll()
        expertRepository.deleteAll()
        expertiseRepository.deleteAll()
    }

    // for authentication -> headers.setBasicAuth()

    @Test
    fun `containers are up and running`(){
        assertTrue(postgres.isRunning)
        assertTrue(keycloak.isRunning)
    }

    // PROFILE CONTROLLER TESTS
    @Test
    fun testGetAllProfiles() {

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        val getEntity: HttpEntity<String> = HttpEntity(null, headers)
        val resp: ResponseEntity<List<ProfileDTO>> = restTemplate.exchange(
                "/profiles/",
                HttpMethod.GET,
                getEntity,
                typeRef())
        val list: List<ProfileDTO> = resp.body!!

        assertNotNull(resp)
        assertEquals(HttpStatus.OK, resp.statusCode)
        assertEquals(1, list.size)
    }

    @Test
    fun testGetProfileByEmail(){

        val prof = profileRepository.findByIdOrNull("test1@test.com")!!.toDTO()
        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        val getEntity: HttpEntity<String> = HttpEntity(null, headers)
        val resp: ResponseEntity<ProfileDTO> = restTemplate.exchange(
            "/profiles/test1@test.com",
            HttpMethod.GET,
            getEntity,
            ProfileDTO::class.java
        )
        /*val resp: ResponseEntity<ProfileDTO> = restTemplate.getForEntity(
                "/profiles/test1@test.com",
                ProfileDTO::class.java
                )
         */

        assertNotNull(resp)
        assertEquals(HttpStatus.OK, resp.statusCode)
        assertEquals(prof, resp.body)
    }

    @Test
    fun testGetProfileByEmail_EmailNotFoundFailure(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        val getEntity: HttpEntity<String> = HttpEntity(null, headers)
        val resp: ResponseEntity<String> = restTemplate.exchange(
            "/profiles/notfound@test.com",
            HttpMethod.GET,
            getEntity,
            String::class.java
        )
        /*val resp: ResponseEntity<String> = restTemplate.getForEntity(
                "/profiles/notfound@test.com",
                String::class.java
        )
         */

        assertNotNull(resp)
        assertEquals(HttpStatus.NOT_FOUND, resp.statusCode)
    }

    @Test
    fun testGetTicketsByEmail(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )

        val getEntity: HttpEntity<MutableList<TicketDTO>> = HttpEntity(null, headers)

        val resp: ResponseEntity<MutableList<TicketDTO>> =
                restTemplate.exchange(
                    "/profiles/test1@test.com/tickets/",
                        HttpMethod.GET,
                        getEntity,
                        typeRef())

        val list: MutableList<TicketDTO> = resp.body!!

        assertNotNull(resp)
        assertEquals(HttpStatus.OK, resp.statusCode)
        assertEquals(1, list.size)
    }

    @Test
    fun testGetTicketsByEmail_EmailNotFoundFailure(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )

        val getEntity: HttpEntity<MutableList<TicketDTO>> = HttpEntity<MutableList<TicketDTO>>(headers)

        val resp: ResponseEntity<String> =
                restTemplate.exchange(
                    "/profiles/notfound@failure.com/tickets/",
                        HttpMethod.GET,
                        getEntity,
                        String::class.java
                )
        assertNotNull(resp)
        assertEquals(HttpStatus.NOT_FOUND, resp.statusCode)
    }

    @Test
    fun testCreateProfile(){

        val profile = ProfileDTO("insert@test.com", "test", "test", null)
        val address = GetAddressDTO(
                "c1",
                "c1",
                "z1",
                "s1",
                "h1"
        )
        profile.address = address

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        headers.contentType = MediaType(MediaType.APPLICATION_PROBLEM_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)


        val postEntity: HttpEntity<ProfileDTO> = HttpEntity(profile, headers)
        val resp = restTemplate.exchange(
                "/profiles/",
                HttpMethod.POST,
                postEntity,
                String::class.java )

        assertNotNull(resp)
        assertEquals(HttpStatus.CREATED, resp.statusCode)
    }

    @Test
    fun testCreateProfile_NoBodyFailure(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        headers.contentType = MediaType(MediaType.APPLICATION_PROBLEM_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)

        val postEntity: HttpEntity<ProfileDTO> = HttpEntity(null, headers)
        val resp = restTemplate.exchange(
                "/profiles/",
                HttpMethod.POST,
                postEntity,
                String::class.java )

        assertNotNull(resp)
        assertEquals(HttpStatus.BAD_REQUEST, resp.statusCode)
    }

    @Test
    fun testCreateProfile_DuplicatedFailure(){
        val profile = ProfileDTO(
                "test1@test.com",
                "test1",
                "test",
                null
        )
        val address = GetAddressDTO(
                "c",
                "c",
                "z",
                "s",
                "h"
        )

        profile.address = address

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )

        headers.contentType = MediaType(MediaType.APPLICATION_PROBLEM_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)

        val postEntity: HttpEntity<ProfileDTO> = HttpEntity(profile, headers)
        val resp = restTemplate.exchange(
                "/profiles/",
                HttpMethod.POST,
                postEntity,
                String::class.java )

        assertNotNull(resp)
        assertEquals(HttpStatus.CONFLICT, resp.statusCode)
    }

    @Test
    fun testChangeProfileInfo() {
        val profile = ChangeProfileInfoDTO("change", "change" )

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        val putEntity = HttpEntity<ChangeProfileInfoDTO>(profile, headers)
        val response = restTemplate.exchange(
                "/profiles/test1@test.com/newInfo",
                HttpMethod.PUT,
                putEntity,
                ChangeProfileInfoDTO::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun testChangeProfileInfo_NoBodyFailure() {

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )

        headers.contentType = MediaType.APPLICATION_PROBLEM_JSON
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)

        val putEntity = HttpEntity<ChangeProfileInfoDTO>(null, headers)
        val response = restTemplate.exchange(
                "/profiles/test1@test.com/newInfo",
                HttpMethod.PUT,
                putEntity,
                String::class.java
                )

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testChangeProfileAddress(){
        val address = GetAddressDTO(
                "c1",
                "c1",
                "z1",
                "s1",
                "h1"
        )

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )

        val putEntity = HttpEntity<GetAddressDTO> (address, headers)
        val response = restTemplate.exchange(
                "/profiles/test1@test.com/newAddress",
                HttpMethod.PUT,
                putEntity,
                GetAddressDTO::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun testChangeProfileAddress_NoBodyFailure(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )

        val putEntity = HttpEntity<GetAddressDTO> (null, headers)
        val response = restTemplate.exchange(
                "/profiles/test1@test.com/newAddress",
                HttpMethod.PUT,
                putEntity,
                String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    // PRODUCT CONTROLLER TESTS
    @Test
    fun testGetAllProducts () {

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        val getEntity: HttpEntity<String> = HttpEntity(null, headers)
        val resp: ResponseEntity<List<ProductDTO>> = restTemplate.exchange(
                "/products/",
                HttpMethod.GET,
                getEntity,
                typeRef())
        val list: List<ProductDTO> = resp.body!!

        assertNotNull(resp)
        assertEquals(HttpStatus.OK, resp.statusCode)
        assertEquals(1, list.size)
    }

    @Test
    fun testGetProductById(){

        val prod = productRepository.findByIdOrNull("1234567890123456")!!.toDTO()
        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        val getEntity: HttpEntity<String> = HttpEntity(null, headers)
        val resp: ResponseEntity<ProductDTO> = restTemplate.exchange(
            "/products/1234567890123456",
            HttpMethod.GET,
            getEntity,
            ProductDTO::class.java
        )
        /*val resp: ResponseEntity<ProductDTO> = restTemplate.getForEntity(
                "/products/1234567890123456",
                ProductDTO::class.java
                )
         */

        assertNotNull(resp)
        assertEquals(HttpStatus.OK, resp.statusCode)
        assertEquals(prod, resp.body)
    }

    @Test
    fun testGetProductById_NotFound(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        val getEntity: HttpEntity<String> = HttpEntity(null, headers)
        val resp: ResponseEntity<String> = restTemplate.exchange(
                "/products/productnotfound",
                HttpMethod.GET,
                getEntity,
                String::class.java
        )
        /*val resp: ResponseEntity<String> = restTemplate.getForEntity(
                "/products/productnotfound",
                String::class.java
        )*/

        assertNotNull(resp)
        assertEquals(HttpStatus.NOT_FOUND, resp.statusCode)
    }

    // EXPERT CONTROLLER TESTS

    @Test
    fun testGetAllExperts () {

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        val getEntity: HttpEntity<String> = HttpEntity(null, headers)
        val resp: ResponseEntity<MutableSet<ExpertDTO>> = restTemplate.exchange(
                "/experts/",
                HttpMethod.GET,
                getEntity,
                typeRef())
        val set: MutableSet<ExpertDTO> = resp.body!!

        assertNotNull(resp)
        assertEquals(HttpStatus.OK, resp.statusCode)
        assertEquals(1, set.size)
    }

    @Test
    fun testGetExpertById(){

        val expert = expertRepository.save(Expert("n", "s")).toDTO()
        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        val getEntity: HttpEntity<String> = HttpEntity(null, headers)
        val resp: ResponseEntity<ExpertDTO> = restTemplate.exchange(
                "/experts/" + expert.id,
                HttpMethod.GET,
                getEntity,
                ExpertDTO::class.java
        )
        /*val resp: ResponseEntity<ExpertDTO> = restTemplate.getForEntity(
                "/experts/" + expert.id,
                ExpertDTO::class.java
        )*/

        assertNotNull(resp)
        assertEquals(HttpStatus.OK, resp.statusCode)
        assertEquals(expert, resp.body)
    }

    @Test
    fun testGetExpertById_ExpertNotFound(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        val getEntity: HttpEntity<String> = HttpEntity(null, headers)
        val resp: ResponseEntity<String> = restTemplate.exchange(
                "/products/productnotfound",
                HttpMethod.GET,
                getEntity,
                String::class.java
        )
        /*val resp: ResponseEntity<String> = restTemplate.getForEntity(
                "/products/productnotfound",
                String::class.java
        )*/

        assertNotNull(resp)
        assertEquals(HttpStatus.NOT_FOUND, resp.statusCode)
    }

    @Test
    fun testCreateExpert(){

        val exSet: MutableSet<ExpertiseDTO> = mutableSetOf(ExpertiseDTO(1, "e1"))
        val expert = ExpertDTO(null, "e1", "e2", exSet)

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        headers.contentType = MediaType(MediaType.APPLICATION_PROBLEM_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)


        val postEntity: HttpEntity<ExpertDTO> = HttpEntity(expert, headers)
        val resp = restTemplate.exchange(
                "/experts/",
                HttpMethod.POST,
                postEntity,
                String::class.java )

        assertNotNull(resp)
        assertEquals(HttpStatus.CREATED, resp.statusCode)
    }

    @Test
    fun testCreateExpert_NoBodyFailure(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )

        headers.contentType = MediaType(MediaType.APPLICATION_PROBLEM_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)

        val postEntity: HttpEntity<ExpertDTO> = HttpEntity(null, headers)
        val resp = restTemplate.exchange(
                "/experts/",
                HttpMethod.POST,
                postEntity,
                String::class.java )

        assertNotNull(resp)
        assertEquals(HttpStatus.BAD_REQUEST, resp.statusCode)
    }

    @Test
    fun testAddExpertiseToExpert(){

        val expert = expertRepository.save(Expert("e", "e"))
        val expertise = expertiseRepository.save(Expertise("e1"))

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )

        val putEntity = HttpEntity<ExpertiseDTO> (expertise.toDTO(), headers)
        val response = restTemplate.exchange(
                "/experts/" + expert.id + "/addExpertise",
                HttpMethod.PUT,
                putEntity,
                ExpertiseDTO::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun testAddExpertiseToExpert_NoBodyFailure(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )

        val putEntity = HttpEntity<ExpertiseDTO> (null, headers)
        val response = restTemplate.exchange(
                "/experts/1/addExpertise",
                HttpMethod.PUT,
                putEntity,
                String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testAddExpertiseToExpert_ExpertNotFoundFailure(){

        val expertise = expertiseRepository.save(Expertise("e1"))

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )

        val putEntity = HttpEntity<ExpertiseDTO> (expertise.toDTO(), headers)
        val response = restTemplate.exchange(
                "/experts/100000/addExpertise",
                HttpMethod.PUT,
                putEntity,
                String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testAddExpertiseToExpert_ExpertiseNotFoundFailure(){

        val expertise = ExpertiseDTO(null, "notfoundfailure")

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )

        val putEntity = HttpEntity<ExpertiseDTO> (expertise, headers)
        val response = restTemplate.exchange(
                "/experts/1/addExpertise",
                HttpMethod.PUT,
                putEntity,
                String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    //EXPERTISE CONTROLLER TESTS

    @Test
    fun testGetAllExpertises () {

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        val getEntity: HttpEntity<String> = HttpEntity(null, headers)
        val resp: ResponseEntity<List<ExpertiseDTO>> = restTemplate.exchange(
                "/expertises/",
                HttpMethod.GET,
                getEntity,
                typeRef())

        val list: List<ExpertiseDTO> = resp.body!!

        assertNotNull(resp)
        assertEquals(HttpStatus.OK, resp.statusCode)
        assertEquals(1, list.size)
    }

    @Test
    fun testGetExpertiseByField(){

        val expertise = expertiseRepository.save(Expertise("test"))
        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        val getEntity: HttpEntity<ExpertiseDTO> = HttpEntity(null, headers)
        val resp: ResponseEntity<ExpertiseDTO> = restTemplate.exchange(
            "/expertises/" + expertise.field,
            HttpMethod.GET,
            getEntity,
            ExpertiseDTO::class.java
        )
        /*val resp: ResponseEntity<ExpertiseDTO> = restTemplate.getForEntity(
                "/expertises/" + expertise.field,
                ExpertiseDTO::class.java)
*/
        assertNotNull(resp)
        assertEquals(HttpStatus.OK, resp.statusCode)
        assertEquals(expertise.toDTO(), resp.body)
    }

    @Test
    fun testGetExpertiseByField_ExpertiseNotFound(){

        val expertise = ExpertiseDTO(null, "test")

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        val getEntity: HttpEntity<String> = HttpEntity(null, headers)
        val resp: ResponseEntity<String> = restTemplate.exchange(
            "/expertises/" + expertise.field,
            HttpMethod.GET,
            getEntity,
            String::class.java
        )/*
        val resp: ResponseEntity<String> = restTemplate.getForEntity(
                "/expertises/" + expertise.field,
                String::class.java)
*/
        assertNotNull(resp)
        assertEquals(HttpStatus.NOT_FOUND, resp.statusCode)
    }


    @Test
    fun testGetExpertsByExpertise(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        val expertise = expertiseRepository.findByField("expertise")

        val getEntity: HttpEntity<String> = HttpEntity(null, headers)
        val resp: ResponseEntity<List<ExpertDTO>> = restTemplate.exchange(
                "/expertises/" + expertise!!.field + "/experts/",
                HttpMethod.GET,
                getEntity,
                typeRef())

        val list: List<ExpertDTO> = resp.body!!

        assertNotNull(resp)
        assertEquals(HttpStatus.OK, resp.statusCode)
        assertEquals(1, list.size)
    }

    @Test
    fun testGetExpertsByExpertise_ExpertiseNotFoundFailure(){
        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        val getEntity: HttpEntity<String> = HttpEntity(null, headers)
        val resp: ResponseEntity<String> = restTemplate.exchange(
            "/expertises/notfoundfailure/experts/",
            HttpMethod.GET,
            getEntity,
            String::class.java
        )
        assertNotNull(resp)
        assertEquals(HttpStatus.NOT_FOUND, resp.statusCode)
    }

    @Test
    fun testCreateExpertise(){

        val expertise = ExpertiseDTO(null, "test")

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        headers.contentType = MediaType(MediaType.APPLICATION_PROBLEM_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)


        val postEntity: HttpEntity<ExpertiseDTO> = HttpEntity(expertise, headers)
        val resp = restTemplate.exchange(
                "/expertises/",
                HttpMethod.POST,
                postEntity,
                ExpertiseDTO::class.java )

        assertNotNull(resp)
        assertEquals(HttpStatus.CREATED, resp.statusCode)
    }

    @Test
    fun testCreateExpertise_NoBodyFailure(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        headers.contentType = MediaType(MediaType.APPLICATION_PROBLEM_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)

        val postEntity: HttpEntity<ExpertiseDTO> = HttpEntity(null, headers)
        val resp = restTemplate.exchange(
                "/expertises/",
                HttpMethod.POST,
                postEntity,
                String::class.java )

        assertNotNull(resp)
        assertEquals(HttpStatus.BAD_REQUEST, resp.statusCode)
    }

    @Test
    fun testDeleteExpertise(){

        val expertise = expertiseRepository.save(Expertise("test"))

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        headers.contentType = MediaType(MediaType.APPLICATION_PROBLEM_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)

        val deleteEntity: HttpEntity<ExpertiseDTO> = HttpEntity(null, headers)
        val resp = restTemplate.exchange(
                "/expertises/" + expertise.field,
                HttpMethod.DELETE,
                deleteEntity,
                String::class.java
        )

        assertNotNull(resp)
        assertEquals(HttpStatus.NO_CONTENT, resp.statusCode)
    }

    @Test
    fun testDeleteExpertise_ExpertiseNotFoundFailure(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        headers.contentType = MediaType(MediaType.APPLICATION_PROBLEM_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)

        val deleteEntity: HttpEntity<ExpertiseDTO> = HttpEntity(null, headers)
        val resp = restTemplate.exchange(
                "/expertises/notfoundfailure",
                HttpMethod.DELETE,
                deleteEntity,
                String::class.java
        )

        assertNotNull(resp)
        assertEquals(HttpStatus.NOT_FOUND, resp.statusCode)
    }

    @Test
    fun testGetAllTickets(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        val getEntity: HttpEntity<String> = HttpEntity(null, headers)
        val resp: ResponseEntity<List<TicketDTO>> = restTemplate.exchange(
                "/tickets/",
                HttpMethod.GET,
                getEntity,
                typeRef()
        )

        val list: List<TicketDTO> = resp.body!!

        assertNotNull(resp)
        assertEquals(HttpStatus.OK, resp.statusCode)
        assertEquals(1, list.size)
    }

    @Test
    fun testGetTicketById(){

        val ticket = Ticket(
            "o",
            "a",
            Priority.TOASSIGN,
            profileRepository.findByIdOrNull("test1@test.com")!!,
            null,
            productRepository.findByIdOrNull("1234567890123456")!!
        )
        val status = TicketStatus(Status.OPEN, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)
        ticketingRepository.save(ticket)

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        val getEntity: HttpEntity<TicketDTO> = HttpEntity(null, headers)
        val resp: ResponseEntity<TicketDTO> = restTemplate.exchange(
            "/tickets/" + ticket.id,
            HttpMethod.GET,
            getEntity,
            TicketDTO::class.java
        )
        /*
        val resp: ResponseEntity<TicketDTO> = restTemplate.getForEntity(
                "/tickets/" + ticket.id,
                TicketDTO::class.java
                )*/

        assertNotNull(resp)
        assertEquals(HttpStatus.OK, resp.statusCode)
    }

    @Test
    fun testGetTicketById_TicketNotFound(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        val getEntity: HttpEntity<TicketDTO> = HttpEntity(null, headers)
        val resp: ResponseEntity<String> = restTemplate.exchange(
            "/tickets/1000",
            HttpMethod.GET,
            getEntity,
            String::class.java
        )
        /*
        val resp: ResponseEntity<String> = restTemplate.getForEntity(
                "/tickets/1000",
                String::class.java
        )*/

        assertNotNull(resp)
        assertEquals(HttpStatus.NOT_FOUND, resp.statusCode)
    }

    @Test
    fun testCreateTicket(){

        val ticket = TicketCreateBodyDTO("o", "a", "test1@test.com", "1234567890123456")

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        headers.contentType = MediaType(MediaType.APPLICATION_PROBLEM_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)

        val postEntity: HttpEntity<TicketCreateBodyDTO> = HttpEntity(ticket, headers)
        val resp = restTemplate.exchange(
                "/tickets/",
                HttpMethod.POST,
                postEntity,
                TicketCreateBodyDTO::class.java )

        assertNotNull(resp)
        assertEquals(HttpStatus.CREATED, resp.statusCode)
    }

    @Test
    fun testCreateTicket_NoBodyFailure(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        headers.contentType = MediaType(MediaType.APPLICATION_PROBLEM_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)

        val postEntity: HttpEntity<TicketCreateBodyDTO> = HttpEntity(null, headers)

        val resp = restTemplate.exchange(
                "/tickets/",
                HttpMethod.POST,
                postEntity,
                String::class.java
        )

        assertNotNull(resp)
        assertEquals(HttpStatus.BAD_REQUEST, resp.statusCode)
    }

    @Test
    fun testCreateTicket_ProfileNotFoundFailure(){

        val ticket = TicketCreateBodyDTO("o", "a", "notfound@failure.com", "1234567890123456")

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        headers.contentType = MediaType(MediaType.APPLICATION_PROBLEM_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)


        val postEntity: HttpEntity<TicketCreateBodyDTO> = HttpEntity(ticket, headers)
        val resp = restTemplate.exchange(
                "/tickets/",
                HttpMethod.POST,
                postEntity,
                String::class.java
        )

        assertNotNull(resp)
        assertEquals(HttpStatus.NOT_FOUND, resp.statusCode)
    }

    @Test
    fun testCreateTicket_ProductNotFoundFailure(){

        val ticket = TicketCreateBodyDTO("o", "a", "test1@test.com", "notfoundfailure")

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        headers.contentType = MediaType(MediaType.APPLICATION_PROBLEM_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)

        val postEntity: HttpEntity<TicketCreateBodyDTO> = HttpEntity(ticket, headers)
        val resp = restTemplate.exchange(
                "/tickets/",
                HttpMethod.POST,
                postEntity,
                String::class.java )

        assertNotNull(resp)
        assertEquals(HttpStatus.NOT_FOUND, resp.statusCode)
    }

    @Test
    fun testOpenTicket() {

        val ticket = Ticket(
                "o",
                "a",
                Priority.TOASSIGN,
                profileRepository.findByIdOrNull("test1@test.com")!!,
                null,
                productRepository.findByIdOrNull("1234567890123456")!!
        )
        val status = TicketStatus(Status.IN_PROGRESS, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)
        ticketingRepository.save(ticket)

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )

        val putEntity = HttpEntity<String>(null, headers)
        val response = restTemplate.exchange(
                "/tickets/" + ticket.id + "/open",
                HttpMethod.PUT,
                putEntity,
                String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun testOpenTicket_TicketNotFoundFailure() {

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )

        val putEntity = HttpEntity<String>(null, headers)
        val response = restTemplate.exchange(
                "/tickets/100000/open",
                HttpMethod.PUT,
                putEntity,
                String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testCloseTicket() {

        val ticket = Ticket(
            "o",
            "a",
            Priority.TOASSIGN,
            profileRepository.findByIdOrNull("test1@test.com")!!,
            null,
            productRepository.findByIdOrNull("1234567890123456")!!
        )
        val status = TicketStatus(Status.IN_PROGRESS, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)
        ticketingRepository.save(ticket)

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        val putEntity = HttpEntity<String>(null, headers)
        val response = restTemplate.exchange(
            "/tickets/" + ticket.id + "/close",
            HttpMethod.PUT,
            putEntity,
            String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun testCloseTicket_TicketNotFoundFailure() {

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )

        val putEntity = HttpEntity<String>(null, headers)
        val response = restTemplate.exchange(
                "/tickets/10000/close",
                HttpMethod.PUT,
                putEntity,
                String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testReOpenTicket() {

        val ticket = Ticket(
                "o",
                "a",
                Priority.TOASSIGN,
                profileRepository.findByIdOrNull("test1@test.com")!!,
                null,
                productRepository.findByIdOrNull("1234567890123456")!!,
        )

        val status = TicketStatus(Status.CLOSED, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)
        ticketingRepository.save(ticket)

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )

        val putEntity = HttpEntity<String>(null, headers)
        val response = restTemplate.exchange(
                "/tickets/" + ticket.id + "/reopen",
                HttpMethod.PUT,
                putEntity,
                String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun testReOpenTicket_TicketNotFoundFailure() {

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )

        val putEntity = HttpEntity<String>(null, headers)
        val response = restTemplate.exchange(
                "/tickets/100000/reopen",
                HttpMethod.PUT,
                putEntity,
                String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testResolveTicket() {

        val ticket = Ticket(
            "o",
            "a",
            Priority.TOASSIGN,
            profileRepository.findByIdOrNull("test1@test.com")!!,
            null,
            productRepository.findByIdOrNull("1234567890123456")!!
        )
        val status = TicketStatus(Status.IN_PROGRESS, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)
        ticketingRepository.save(ticket)

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )
        val putEntity = HttpEntity<String>(null, headers)
        val response = restTemplate.exchange(
            "/tickets/" + ticket.id + "/resolved",
            HttpMethod.PUT,
            putEntity,
            String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun testResolveTicket_TicketNotFoundFailure() {

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("profile", "password")
        )

        val putEntity = HttpEntity<String>(null, headers)
        val response = restTemplate.exchange(
                "/tickets/100000/resolved",
                HttpMethod.PUT,
                putEntity,
                String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testInProgressTicket() {

        val expert = expertRepository.save(Expert("e", "e"))

        val ticket = Ticket(
            "o",
            "a",
            Priority.TOASSIGN,
            profileRepository.findByIdOrNull("test1@test.com")!!,
            null,
            productRepository.findByIdOrNull("1234567890123456")!!
        )
        val status = TicketStatus(Status.OPEN, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)
        ticketingRepository.save(ticket)

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )

        val ticketInProgress = TicketInProgressBodyDTO(expert.id!!, Priority.HIGH)

        val putEntity = HttpEntity<TicketInProgressBodyDTO>(ticketInProgress, headers)
        val response = restTemplate.exchange(
                "/tickets/" + ticket.id + "/inprogress",
                HttpMethod.PUT,
                putEntity,
                String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun testInProgressTicket_NoBodyFailure() {

        val ticket = Ticket(
            "o",
            "a",
            Priority.TOASSIGN,
            profileRepository.findByIdOrNull("test1@test.com")!!,
            null,
            productRepository.findByIdOrNull("1234567890123456")!!
        )
        val status = TicketStatus(Status.IN_PROGRESS, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)
        ticketingRepository.save(ticket)

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )

        val putEntity = HttpEntity<TicketInProgressBodyDTO>(null, headers)
        val response = restTemplate.exchange(
                "/tickets/" + ticket.id + "/inprogress",
                HttpMethod.PUT,
                putEntity,
                String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testInProgressTicket_TicketNotFoundFailure() {

        val expert = expertRepository.save(Expert("e", "e"))

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )

        val ticketInProgress = TicketInProgressBodyDTO(expert.id!!, Priority.HIGH)

        val putEntity = HttpEntity<TicketInProgressBodyDTO>(ticketInProgress, headers)
        val response = restTemplate.exchange(
                "/tickets/100000/inprogress",
                HttpMethod.PUT,
                putEntity,
                String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testInProgressTicket_ExpertNotFoundFailure() {
        val ticket = Ticket(
            "o",
            "a",
            Priority.TOASSIGN,
            profileRepository.findByIdOrNull("test1@test.com")!!,
            null,
            productRepository.findByIdOrNull("1234567890123456")!!
        )
        val status = TicketStatus(Status.IN_PROGRESS, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)
        ticketingRepository.save(ticket)

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )

        val ticketInProgress = TicketInProgressBodyDTO(10000, Priority.HIGH)

        val putEntity = HttpEntity<TicketInProgressBodyDTO>(ticketInProgress, headers)
        val response = restTemplate.exchange(
                "/tickets/" + ticket.id + "/inprogress",
                HttpMethod.PUT,
                putEntity,
                String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testSetTicketStatus_IllegalStatusChangeFailure(){

        val expert = expertRepository.save(Expert("e", "e"))

        val ticket = Ticket(
            "o",
            "a",
            Priority.TOASSIGN,
            profileRepository.findByIdOrNull("test1@test.com")!!,
            null,
            productRepository.findByIdOrNull("1234567890123456")!!
        )
        val status = TicketStatus(Status.RESOLVED, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)
        ticketingRepository.save(ticket)

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )

        val ticketInProgress = TicketInProgressBodyDTO(expert.id!!, Priority.HIGH)

        val putEntity = HttpEntity<TicketInProgressBodyDTO>(ticketInProgress, headers)
        val response = restTemplate.exchange(
            "/tickets/" + ticket.id + "/inprogress",
            HttpMethod.PUT,
            putEntity,
            String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testSetTicketStatus_IllegalPriorityFailure(){

        val expert = expertRepository.save(Expert("e", "e"))

        val ticket = Ticket(
            "o",
            "a",
            Priority.TOASSIGN,
            profileRepository.findByIdOrNull("test1@test.com")!!,
            null,
            productRepository.findByIdOrNull("1234567890123456")!!
        )
        val status = TicketStatus(Status.OPEN, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)
        ticketingRepository.save(ticket)

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )

        val ticketInProgress = TicketInProgressBodyDTO(expert.id!!, Priority.TOASSIGN)

        val putEntity = HttpEntity<TicketInProgressBodyDTO>(ticketInProgress, headers)
        val response = restTemplate.exchange(
            "/tickets/" + ticket.id + "/inprogress",
            HttpMethod.PUT,
            putEntity,
            String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun testSetTicketPriority(){

        val ticket = Ticket(
            "o",
            "a",
            Priority.TOASSIGN,
            profileRepository.findByIdOrNull("test1@test.com")!!,
            null,
            productRepository.findByIdOrNull("1234567890123456")!!
        )
        val status = TicketStatus(Status.OPEN, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)
        ticketingRepository.save(ticket)

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        val putEntity = HttpEntity<String>(null, headers)
        val response = restTemplate.exchange(
            "/tickets/" + ticket.id + "/set_priority/high",
            HttpMethod.PUT,
            putEntity,
            String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun testSetTicketPriority_TicketNotFoundFailure(){

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        val putEntity = HttpEntity<String>(null, headers)
        val response = restTemplate.exchange(
            "/tickets/10000/set_priority/high",
            HttpMethod.PUT,
            putEntity,
            String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testSetTicketPriority_IllegalPriorityFailure(){
        val ticket = Ticket(
            "o",
            "a",
            Priority.TOASSIGN,
            profileRepository.findByIdOrNull("test1@test.com")!!,
            null,
            productRepository.findByIdOrNull("1234567890123456")!!
        )
        val status = TicketStatus(Status.OPEN, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)
        ticketingRepository.save(ticket)

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("manager", "password")
        )
        val putEntity = HttpEntity<String>(null, headers)
        val response = restTemplate.exchange(
            "/tickets/" + ticket.id + "/set_priority/illegal",
            HttpMethod.PUT,
            putEntity,
            String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }
    @Test
    fun testLogin(){

        val logindto = LoginDTO("expert", "password")

        val headers = HttpHeaders()
        headers.contentType = MediaType(MediaType.APPLICATION_PROBLEM_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)

        val postEntity: HttpEntity<LoginDTO> = HttpEntity(logindto, headers)
        val resp = restTemplate.exchange(
            "/login/",
            HttpMethod.POST,
            postEntity,
            TokenDTO::class.java )

        assertNotNull(resp)
        assertEquals(HttpStatus.CREATED, resp.statusCode)
    }

    @Test
    fun testSetTicketPriority_Unauthorized(){

        val ticket = Ticket(
            "o",
            "a",
            Priority.TOASSIGN,
            profileRepository.findByIdOrNull("test1@test.com")!!,
            null,
            productRepository.findByIdOrNull("1234567890123456")!!
        )
        val status = TicketStatus(Status.OPEN, Date(System.currentTimeMillis()), ticket)

        ticket.addStatus(status)
        ticketingRepository.save(ticket)

        val headers = HttpHeaders()
        headers.set(
            "Authorization",
            getBearerToken("expert", "password")
        )

        val putEntity = HttpEntity<String>(null, headers)
        val response = restTemplate.exchange(
            "/tickets/" + ticket.id + "/set_priority/high",
            HttpMethod.PUT,
            putEntity,
            String::class.java
        )

        assertNotNull(response)
        assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
    }

    @Test
    fun testProva(){

        val headers = HttpHeaders()
        headers.set(
                "Authorization",
                getBearerToken("manager", "password")
        )
        headers.contentType = MediaType(MediaType.APPLICATION_PROBLEM_JSON)
        headers.accept = Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON)

        val getEntity: HttpEntity<ExpertiseDTO> = HttpEntity(null, headers)
        val resp = restTemplate.exchange(
                "/prova",
                HttpMethod.GET,
                getEntity,
                String::class.java )

        assertNotNull(resp)
        assertEquals(HttpStatus.OK, resp.statusCode)
        assertEquals("manager", resp.body)
    }
}