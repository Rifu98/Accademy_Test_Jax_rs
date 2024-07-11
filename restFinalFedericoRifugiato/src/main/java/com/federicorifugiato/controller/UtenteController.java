package com.federicorifugiato.controller;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import com.federicorifugiato.dto.UtenteDto;
import com.federicorifugiato.dto.UtenteLoginDto;
import com.federicorifugiato.dto.UtenteLoginResponseDto;
import com.federicorifugiato.dto.UtenteRegistrationDto;
import com.federicorifugiato.dto.UtenteUpdateDto;
import com.federicorifugiato.model.Ruolo;
import com.federicorifugiato.model.Utente;
import com.federicorifugiato.service.UtenteService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/utenti")
public class UtenteController {
	
	@Autowired
	private UtenteService utenteService;
	
	@GET
	@Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
	public Response getAllUtenti () {
		
		List<UtenteDto> utenti = utenteService.getUtenti();
		
		return Response.status(Status.OK).entity(utenti).build();
	}

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUtente(@Valid UtenteRegistrationDto utenteDto) {
		try {
			if(utenteService.existsUtenteByMail(utenteDto.getEmail())) {
				return Response.status(Status.BAD_REQUEST).entity("già registrato").build();
			}

	        String hashedPassword = DigestUtils.sha256Hex(utenteDto.getPassword());
	        Utente utente = new Utente ();
	        utente.setCognome(utenteDto.getCognome());
	        utente.setNome(utenteDto.getNome());
	        utente.setPassword(hashedPassword);
	        utente.setEmail(utenteDto.getEmail());
	        
	        utenteService.registraUtente(utente);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity("errore").build();
		}
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUtente(UtenteLoginDto utenteDto) {
        String hashedPassword = DigestUtils.sha256Hex(utenteDto.getPassword());
        utenteDto.setPassword(hashedPassword);
        Utente utente = utenteService.loginUtente(utenteDto);
        if (utente != null) {
            UtenteLoginResponseDto token = issueToken(utenteDto.getEmail());
            return Response.status(Status.OK).entity(token).build();
        }
        return Response.status(Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/get/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUtenteByEmail(@PathParam("email") String email) {
    	if(email == null || email == "") {
            return Response.status(Status.BAD_REQUEST).build();
    	}
        UtenteDto utente = utenteService.getUtenteDtoByEmail(email);
        if (utente != null) {
            return Response.status(Status.OK).entity(utente).build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }

	
	@DELETE
	@Path("/delete/{email}")
	public Response deleteUser (@PathParam("email") String email) {
		try {
			utenteService.cancellaUtenteByEmail(email);
			return Response.status(Status.OK)
					.build();
			} catch (Exception e) {
				return Response.status(Status.BAD_REQUEST)
						.build();
			}
	}
	
	@PUT
	@Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser (@Valid @RequestBody UtenteUpdateDto user) {
		try {
			if(!utenteService.existsUtenteByMail(user.getEmail())) {
				return Response.status(Status.BAD_REQUEST).entity("non trovato").build();
			}

	        String hashedPassword = DigestUtils.sha256Hex(user.getPassword());
	        user.setPassword(hashedPassword);
			utenteService.updateUtente(user);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity("errore").build();
		}
	}
    
	public UtenteLoginResponseDto issueToken (String email) {
	    
	      byte[] secret = "33trentinientraronoatrentotuttie33trotterellando1234567890".getBytes();
	      Key key = Keys.hmacShaKeyFor(secret);
	      
	      Utente informazioniUtente = utenteService.getUtenteByEmail(email);
	      Map<String, Object> map = new HashMap<>();
	      map.put("nome", informazioniUtente.getNome());
	      map.put("cognome", informazioniUtente.getCognome());
	      map.put("email", email);
	      
	      List <String> ruoli = new ArrayList<>();
	      for(Ruolo ruolo : informazioniUtente.getRuoli()) {
	    	  ruoli.add(ruolo.getTipologia().name());
	      }

	      map.put("ruoli", ruoli);

	      Date creation = new Date();
	      Date end = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(15L));
	      
	      String tokenJwts = Jwts.builder()
	    		  .setClaims(map)
	    		  .setIssuer("http://localhost:8080")
	    		  .setIssuedAt(creation)
	    		  .setExpiration(end)
	    		  .signWith(key)
	    		  .compact();
	      
	      UtenteLoginResponseDto token = new UtenteLoginResponseDto();
	      
	      token.setToken(tokenJwts);
	      token.setTct(creation);
	      token.setTtl(end);
	      
	      return token;
		
	}
}
