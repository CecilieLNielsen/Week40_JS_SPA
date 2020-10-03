package rest;

import entities.Address;
import entities.Person;
import entities.PersonDTO;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person p1, p2;
    private static Address a1, a2;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Person("Kim", "Hansen", "65748390", new Date(), new Date());
        a1 = new Address("Street", 4312, "City");
        p1.setAddress(a1);
        p2 = new Person("Sonny", "Hansen", "64537390", new Date(), new Date());
        a2 = new Address("Street2", 543222, "City3");
        p2.setAddress(a2);
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Person p").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/person").then().statusCode(200);
    }

    //This test assumes the database contains two rows
    @Test
    public void testDummyMsg() throws Exception {
        given()
                .contentType("application/json")
                .get("/person/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("Hello World"));
    }

    @Test
    public void testCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/person/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(2));
    }

    @Test
    public void getAllPersons() throws Exception {
        List<PersonDTO> personsDtos;

        personsDtos = given()
                .contentType("application/json")
                .when()
                .get("/person/all")
                .then()
                .extract().body().jsonPath().getList("all", PersonDTO.class);

        PersonDTO p1DTO = new PersonDTO(p1);
        PersonDTO p2DTO = new PersonDTO(p2);

        assertThat(personsDtos, containsInAnyOrder(p1DTO, p2DTO));

    }

    @Test
    public void addPerson() throws Exception {
        given()
                .contentType("application/json")
                .body(new PersonDTO("Ib", "Poulsen", "86749374", "Gadevang", 5647, "Bakkedal"))
                .when()
                .post("/person/add")
                .then()
                .body("firstname", equalTo("Ib"))
                .body("lastname", equalTo("Poulsen"))
                .body("id", notNullValue());

    }

    @Test
    public void getPerson() throws Exception {
        PersonDTO person;

        person = given()
                .contentType("application/json")
                .when()
                .get("/person/" + p1.getId())
                .then()
                .extract().body().jsonPath().getObject("", PersonDTO.class);

        assertThat("Person is the right one", p1.getId() == person.getId());
    }

    @Test
    public void getPersonError() throws Exception {
        int code = given()
                .contentType("application/json")
                .when()
                .get("/person/4324")
                .then()
                .extract().body().jsonPath().get("code");

        assertThat("Person do not exist", code == 404);
    }

    @Test
    public void deletePerson() throws Exception {
        PersonDTO person;

        person = given()
                .contentType("application/json")
                .when()
                .delete("/person/" + p1.getId())
                .then()
                .extract().body().jsonPath().getObject("", PersonDTO.class);

        assertThat("Person is deleted", p1.getId() == person.getId());

    }

    @Test
    public void deletePersonError() throws Exception {
        int code = given()
                .contentType("application/json")
                .when()
                .delete("/person/4324")
                .then()
                .extract().body().jsonPath().get("code");

        assertThat("Person do not exist", code == 404);
    }

    @Test
    public void editPerson() throws Exception {
        PersonDTO person;

        person = given()
                .contentType("application/json")
                .body(new PersonDTO("Ib", "Poulsen", "86749374", "Gadevang", 5647, "Bakkedal"))
                .when()
                .put("/person/" + p1.getId())
                .then()
                .extract().body().jsonPath().getObject("", PersonDTO.class);

        assertThat("Person is edited", p1.getId() == person.getId() && p1.getFirstname() != person.getFirstname());

    }

    @Test
    public void editPersonError() throws Exception {
        int code = given()
                .contentType("application/json")
                .body(new PersonDTO("Ib", "Poulsen", "86749374", "Gadevang", 5647, "Bakkedal"))
                .when()
                .put("/person/4324")
                .then()
                .extract().body().jsonPath().get("code");

        assertThat("Person do not exist", code == 404);
    }

}
