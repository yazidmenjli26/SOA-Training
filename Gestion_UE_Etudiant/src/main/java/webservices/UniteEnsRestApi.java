package webservices;

import entities.Module;
import entities.UniteEnseignement;
import metiers.ModuleBusiness;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/module")
public class UniteEnsRestApi {

    // helper : permet de manipuler les données (CRUD)
    ModuleBusiness helper = new ModuleBusiness();
    UniteEnseignementBusiness ueHelper = new UniteEnseignementBusiness();

    // Liste de tous les modules
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListModules() {
        return Response
                .status(200)
                .entity(helper.getAllModules())
                .build();
    }

    // Ajouter un module
    @Path("/new")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createModule(Module module) {
        if (module.getUniteEnseignement() == null) {
            return Response.status(400).entity("UE obligatoire").build();
        }
        if (helper.addModule(module)) {
            return Response.status(201).entity("added successfully").build();
        }
        return Response.status(400).entity("didn't get added").build();
    }

    // Recherche par matricule
    @Path("/search/{matricule}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByMatricule(@PathParam("matricule") String matricule) {
        Module m = helper.getModuleByMatricule(matricule);
        if (m == null) {
            return Response.status(404).entity("module not found").build();
        }
        return Response.ok(m).build();
    }

    // Recherche par type : /module?type=PROFESSIONNEL
    @Path("")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByType(@QueryParam("type") String type) {
        try {
            Module.TypeModule t = Module.TypeModule.valueOf(type.toUpperCase());
            return Response.ok(helper.getModulesByType(t)).build();
        } catch (IllegalArgumentException | NullPointerException e) {
            return Response.status(400).entity("type invalide").build();
        }
    }

    // Modules d'une UE : /module/ue/1
    @Path("/ue/{code}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModulesByUE(@PathParam("code") int code) {
        UniteEnseignement ue = ueHelper.getUEByCode(code);
        if (ue == null) {
            return Response.status(404).entity("UE not found").build();
        }
        return Response.ok(helper.getModulesByUE(ue)).build();
    }

    // Mise à jour : PUT /module/update/M101
    @Path("/update/{matricule}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateModule(@PathParam("matricule") String matricule, Module module) {
        if (helper.updateModule(matricule, module)) {
            return Response.status(200).entity("updated successfully").build();
        }
        return Response.status(404).entity("module not found").build();
    }

    // Suppression : DELETE /module/delete?matricule=M101
    @Path("/delete")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteModule(@QueryParam("matricule") String matricule) {
        return Response.ok(helper.deleteModule(matricule)).build();
    }
}