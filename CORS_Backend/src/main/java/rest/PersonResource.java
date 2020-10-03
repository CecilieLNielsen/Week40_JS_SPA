package rest;

import exception.PersonNotFoundException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Person;
import entities.PersonDTO;
import exception.PersonNotFoundExceptionMapper;
import utils.EMF_Creator;
import facades.PersonFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Todo Remove or change relevant parts before ACTUAL use
@Path("person")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    //An alternative way to get the EntityManagerFactory, whithout having to type the details all over the code
    //EMF = EMF_Creator.createEntityManagerFactory(DbSelector.DEV, Strategy.CREATE);
    private static final PersonFacade FACADE = PersonFacade.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final PersonNotFoundExceptionMapper pnfem = new PersonNotFoundExceptionMapper();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getRenameMeCount() {
        long count = FACADE.getRenameMeCount();
        //System.out.println("--------------->"+count);
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
    }

    @Path("/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPersonById(@PathParam("id") int id) {
        try {
            return Response.ok().entity(GSON.toJson(FACADE.getPerson(id))).build();
        } catch (PersonNotFoundException ex) {
            return pnfem.toResponse(ex);
        } catch (RuntimeException ex) {
            return Response.ok().entity("{\"code\": 500, \"message\": \"Internal Server Problem. We are sorry for the inconvenience\"}").build();
        }
    }

    @Path("/all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllPersons() {
        try {
            return Response.ok().entity(GSON.toJson(FACADE.getAllPersons())).build();
        } catch (RuntimeException ex) {
            return Response.ok().entity("{\"code\": 500, \"message\": \"Internal Server Problem. We are sorry for the inconvenience\"}").build();
        }

    }

    @Path("/add")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response addPerson(String person) {
        try {
            PersonDTO personDTO = GSON.fromJson(person, PersonDTO.class);
            return Response.ok().entity(GSON.toJson(FACADE.addPerson(personDTO.getFirstname(), personDTO.getLastname(), personDTO.getPhone(), personDTO.getStreet(), personDTO.getZip(), personDTO.getCity()))).build();
        } catch (RuntimeException ex) {
            return Response.ok().entity("{\"code\": 500, \"message\": \"Internal Server Problem. We are sorry for the inconvenience\"}").build();
        }
    }

    @Path("/{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public Response deletePerson(@PathParam("id") int id) {
        try {
            return Response.ok().entity(GSON.toJson(FACADE.deletePerson(id))).build();
        } catch (PersonNotFoundException ex) {
            return pnfem.toResponse(ex);
        } catch (RuntimeException ex) {
            return Response.ok().entity("{\"code\": 500, \"message\": \"Internal Server Problem. We are sorry for the inconvenience\"}").build();
        }
    }

    @PUT
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response editPerson(@PathParam("id") int id, String person) {
        try {
            PersonDTO p = GSON.fromJson(person, PersonDTO.class);
            p.setId(id);
            return Response.ok().entity(GSON.toJson(FACADE.editPerson(p))).build();
        } catch (PersonNotFoundException ex) {
            return pnfem.toResponse(ex);
        } catch (RuntimeException ex) {
            return Response.ok().entity("{\"code\": 500, \"message\": \"Internal Server Problem. We are sorry for the inconvenience\"}").build();
        }
    }

}
