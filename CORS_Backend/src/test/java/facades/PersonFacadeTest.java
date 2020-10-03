package facades;

import entities.Address;
import utils.EMF_Creator;
import entities.Person;
import entities.PersonDTO;
import entities.PersonsDTO;
import exception.PersonNotFoundException;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getFacadeExample(emf);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Person p").executeUpdate();
        em.createNativeQuery("ALTER TABLE `PERSON` AUTO_INCREMENT = 1").executeUpdate(); // Resetter auto increment tilbage til 1
        em.getTransaction().commit();
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        Person p1 = new Person("Peter", "Larsen", "75936593", new Date(), new Date());
        Address a1 = new Address("Street 1", 10331, "city 1");
        p1.setAddress(a1);

        Person p2 = new Person("Tina", "Larsen", "75937436", new Date(), new Date());
        Address a2 = new Address("Street2", 10332, "city 2 ");
        p2.setAddress(a2);

        Person p3 = new Person("Helle", "Larsen", "75939421", new Date(), new Date());
        Address a3 = new Address("Street3", 10333, "city 3");
        p3.setAddress(a3);

        Person p4 = new Person("Gunnar", "Larsen", "7596325", new Date(), new Date());
        Address a4 = new Address("Street4", 10334, "city 4");
        p4.setAddress(a4);

        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Person p").executeUpdate();
            em.createNativeQuery("ALTER TABLE `PERSON` AUTO_INCREMENT = 1").executeUpdate();
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        //em.createQuery("DELETE FROM Person p").executeUpdate();
        //em.createNativeQuery("ALTER TABLE `PERSON` AUTO_INCREMENT = 1").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testGetPersonById() throws PersonNotFoundException {
        // Arrange
        int id = 1;
        // Act
        PersonDTO result = facade.getPerson(id);
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetPersonByIdError() throws PersonNotFoundException {
        Assertions.assertThrows(PersonNotFoundException.class, () -> {
            int id = 178;
            PersonDTO result = facade.getPerson(id);
        });
    }

    @Test
    public void testAddPerson() {
        // Arrange
        String firstName = "Sanne";
        String lastName = "Sørensen";
        String phone = "75947594";
        String street = "Gadevang";
        int zip = 3245;
        String city = "Bakkedal";
        // Act
        PersonDTO result = facade.addPerson(firstName, lastName, phone, street, zip, city);
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetAllPersons() {
        // Arrange

        // Act
        PersonsDTO result = facade.getAllPersons();
        // Assert
        assertEquals(4, result.getAll().size());
    }

    @Test
    public void testDeletePerson() throws PersonNotFoundException {
        // Arrange
        int id = 2;
        // Act
        facade.deletePerson(id);
        // Assert
        assertTrue(true);
    }

    @Test
    public void testDeletePersonError() throws PersonNotFoundException {
        Assertions.assertThrows(PersonNotFoundException.class, () -> {
            int id = 178;
            PersonDTO result = facade.deletePerson(id);
        });
    }

    @Test
    public void testEditPerson() throws PersonNotFoundException {
        // Arrange
        PersonDTO p = new PersonDTO("Thomas", "Sørensen", "74837584", "Gadevang", 4500, "Bakkedal");
        p.setId(1);
        // Act
        PersonDTO result = facade.editPerson(p);
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testEditPersonError() throws PersonNotFoundException {
        Assertions.assertThrows(PersonNotFoundException.class, () -> {
            PersonDTO p = new PersonDTO("Thomas", "Sørensen", "74837584", "Gadevang", 4500, "Bakkedal");
            p.setId(154);
            PersonDTO result = facade.editPerson(p);
        });
    }

}
