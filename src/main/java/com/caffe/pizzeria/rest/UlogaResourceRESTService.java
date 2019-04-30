package com.caffe.pizzeria.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.caffe.pizzeria.data.UlogaRepo;
import com.caffe.pizzeria.model.Uloga;

@Path("/uloge")
@RequestScoped
public class UlogaResourceRESTService {

	@Inject
    private Validator validator;

    @Inject
    private UlogaRepo repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Uloga> listAllUloge() {
        return repository.findAll();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uloga lookupUlogaById(@PathParam("id") long id) {
        Uloga uloga = repository.findById(id);
        if (uloga == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return uloga;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUloga(Uloga uloga) {

        Response.ResponseBuilder builder = null;

        try {
            validateUloga(uloga);
            repository.Dodaj(uloga);
            builder = Response.ok();
        } catch (ConstraintViolationException ce) {
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("naziv", "Naziv taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    private void validateUloga(Uloga uloga) throws ConstraintViolationException, ValidationException {
        Set<ConstraintViolation<Uloga>> violations = validator.validate(uloga);

        if (!violations.isEmpty()) {
        	System.out.println("V1");
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
        
        if (nazivAlreadyExists(uloga.getNaziv())) {
        	System.out.println("V2");
            throw new ValidationException("Unique Naziv Violation");
        }
    }

    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        Map<String, String> responseObj = new HashMap<String, String>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

    public boolean nazivAlreadyExists(String naziv) {
        Uloga uloga = null;
        try {
            uloga = repository.findByNaziv(naziv);
        } catch (NoResultException e) {
        }
        return uloga != null;
    }
}
