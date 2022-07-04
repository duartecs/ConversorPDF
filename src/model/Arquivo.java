package model;

import java.util.Objects;

public class Arquivo {

	private TipoArquivo tipoArquivo;
	private String pathArquivo;
	private String nome;
	private Integer id;

	public Arquivo(TipoArquivo tipoArquivo, String pathArquivo, String nome, Integer id) {
		this.tipoArquivo = tipoArquivo;
		this.pathArquivo = pathArquivo;
		this.nome = nome;
		this.id = id;
	}

	public TipoArquivo getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(TipoArquivo tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}

	public String getPathArquivo() {
		return pathArquivo;
	}

	public void setPathArquivo(String pathArquivo) {
		this.pathArquivo = pathArquivo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arquivo other = (Arquivo) obj;
		return Objects.equals(id, other.id);
	}

}
