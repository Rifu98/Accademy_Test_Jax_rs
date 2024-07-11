package com.federicorifugiato.dto;

import com.federicorifugiato.enums.NomeCategoria;

public class CategoriaDto {
    private int id;
    private NomeCategoria nomeCategoria;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public NomeCategoria getNomeCategoria() {
		return nomeCategoria;
	}

	public void setNomeCategoria(NomeCategoria nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}
    
}
