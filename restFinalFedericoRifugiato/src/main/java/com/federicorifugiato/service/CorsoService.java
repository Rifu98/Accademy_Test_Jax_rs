package com.federicorifugiato.service;

import java.util.List;

import com.federicorifugiato.dto.UtenteCorsoDto;
import com.federicorifugiato.model.Corso;

public interface CorsoService {

	void registraCorso(Corso corso);
	Corso getCorsoById(int id);
	List<UtenteCorsoDto> getCorsi();
	void cancellaCorso(int id);
}
