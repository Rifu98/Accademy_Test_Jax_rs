package com.federicorifugiato.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.federicorifugiato.dto.UtenteCorsoDto;
import com.federicorifugiato.jwt.JWTTokenNeeded;
import com.federicorifugiato.jwt.Secured;
import com.federicorifugiato.service.CorsoService;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


//Mancano alcuni metodi ma almeno uno per verificare il filtro sono riuscito ad inserirlo.
// n.b. c'è una modifica nel filtro, leggere il commento in loco

@JWTTokenNeeded
@Path("/corsi")
public class CorsoController {
	
	@Autowired
	private CorsoService corsoService;
	
	@GET
	@Path("/all")
	@Secured (role = "admin")
    @Produces(MediaType.APPLICATION_JSON)
	public Response getAllUtenti () {
		
		List<UtenteCorsoDto> corsi = corsoService.getCorsi();
		
		return Response.status(Status.OK).entity(corsi).build();
	}


}
