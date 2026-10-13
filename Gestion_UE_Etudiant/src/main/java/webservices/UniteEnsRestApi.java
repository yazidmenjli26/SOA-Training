package webservices;

import entities.UniteEnseignement;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ue")
public class UniteEnsRestApi {
    UniteEnseignementBusiness helper = new UniteEnseignementBusiness();

    // Get list of UEs
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListUEs() {
        return Response
                .status(200)
                .entity(helper.getListeUE())
                .build();
    }

    // Add an UniteEnseignement
    @Path("/new")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createUE(UniteEnseignement ue) {
        if (ue == null) {
            return Response.status(400).entity("UE obligatoire").build();
        }
        if (helper.addUniteEnseignement(ue)) {
            return Response.status(201).entity("added successfully").build();
        }
        return Response.status(400).entity("didn't get added").build();
    }
}