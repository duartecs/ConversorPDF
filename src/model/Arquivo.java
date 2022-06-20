package model;

import java.util.Objects;

public class Arquivo {

	private TipoArquivo tipoArquivo;
	private String pathArquivo;
	private String nome;
	private Integer id;
	private Arquivo arquivoAnterior;
	private Arquivo arquivoProximo;

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

	protected Arquivo getArquivoAnterior() {
		return arquivoAnterior;
	}

	protected void setArquivoAnterior(Arquivo arquivoAnterior) {
		this.arquivoAnterior = arquivoAnterior;
	}

	protected Arquivo getArquivoProximo() {
		return arquivoProximo;
	}

	protected void setArquivoProximo(Arquivo arquivoProximo) {
		this.arquivoProximo = arquivoProximo;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		if (getArquivoAnterior() == null) {
			sb.append("Anterior = []");
		} else {
			sb.append("Anterior id = " + getArquivoAnterior().getId());
		}

		sb.append(" | Atual id = " + getId());

		if (getArquivoProximo() == null) {
			sb.append(" | Proximo = []");
		} else {
			sb.append(" | Proximo id = " + getArquivoProximo().getId());
		}

		sb.append("}");
		return sb.toString();
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
