package com.federicorifugiato.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.federicorifugiato.dao.CorsoDao;
import com.federicorifugiato.dto.UtenteCorsoDto;
import com.federicorifugiato.model.Corso;

@Service
public class CorsoServiceImpl implements CorsoService{

	@Autowired
	private CorsoDao corsoDao;
	
	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public void registraCorso(Corso corso) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Corso getCorsoById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UtenteCorsoDto> getCorsi() {
	
		List<Corso> corsi = (List<Corso>) corsoDao.findAll();
		List<UtenteCorsoDto> corsiDto = new ArrayList<>();
		
		corsi.forEach(c -> corsiDto.add(modelMapper.map(c, UtenteCorsoDto.class)));
		
		return corsiDto;
	}

	@Override
	public void cancellaCorso(int id) {
		// TODO Auto-generated method stub
		
	}

}
