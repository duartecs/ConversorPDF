package model;

import java.util.Objects;

public class ListaArquivos {

	private Arquivo primeiro;
	private Integer contador;

	public ListaArquivos() {
		this.primeiro = null;
		this.contador = 0;
	}

	public Integer getContador() {
		return contador;
	}

	public void adicionar(Arquivo arquivo) {
		if (listaVazia()) {
			this.primeiro = arquivo;
			contador++;
			return;
		}
		Arquivo ultimoArquivo = getUltimoElemento();
		ultimoArquivo.setArquivoProximo(arquivo);
		arquivo.setArquivoAnterior(ultimoArquivo);
		contador++;
	}

	private Arquivo getUltimoElemento() {
		if (listaVazia()) {
			return null;
		}
		if (this.primeiro.getArquivoProximo() == null) {
			return this.primeiro;
		}
		return getUltimoElemento(this.primeiro.getArquivoProximo());

	}

	private Arquivo getUltimoElemento(Arquivo arquivo) {
		if (arquivo.getArquivoProximo() == null) {
			return arquivo;
		}
		return getUltimoElemento(arquivo.getArquivoProximo());
	}

	public Boolean listaVazia() {
		return this.primeiro == null;
	}

	public Arquivo getById(Integer id) {
		if (listaVazia()) {
			return null;
		}
		if (this.primeiro.getId().equals(id)) {
			return this.primeiro;
		}

		Arquivo arquivo = this.primeiro.getArquivoProximo();

		while (!arquivo.getId().equals(id)) {
			if (arquivo.getArquivoProximo() == null) {
				return null;
			}
			arquivo = arquivo.getArquivoProximo();
		}

		return arquivo;
	}

	public Arquivo removerById(Integer id) {
		Arquivo vitima = getById(id);

		vitima.getArquivoAnterior().setArquivoProximo(vitima.getArquivoProximo());
		vitima.getArquivoProximo().setArquivoAnterior(vitima.getArquivoAnterior());
		vitima.setArquivoAnterior(null);
		vitima.setArquivoProximo(null);
		contador--;

		return vitima;
	}

	public void trocarAnteriorById(Integer id) {
		Arquivo arquivoPrincipal = getById(id);
		if (isPrimeiro(arquivoPrincipal) || arquivoPrincipal == null)
			return;

		Arquivo vitima = arquivoPrincipal.getArquivoAnterior();
		Arquivo vizinhoEsquerda = null;

		if (isPrimeiro(vitima)) {
			vitima = this.primeiro;
			this.primeiro = arquivoPrincipal;
		} else {
			vizinhoEsquerda = vitima.getArquivoAnterior();
			vizinhoEsquerda.setArquivoProximo(arquivoPrincipal);
		}

		Arquivo vizinhoDireita = arquivoPrincipal.getArquivoProximo();

		arquivoPrincipal.setArquivoAnterior(vizinhoEsquerda);
		arquivoPrincipal.setArquivoProximo(vitima);
		vitima.setArquivoAnterior(arquivoPrincipal);
		vitima.setArquivoProximo(vizinhoDireita);
		if (vizinhoDireita != null) {
			vizinhoDireita.setArquivoAnterior(vitima);
		}

	}

	public void trocarProximoById(Integer id) {
		Arquivo arquivoPrincipal = getById(id);
		if (isUltimo(arquivoPrincipal) || arquivoPrincipal == null)
			return;

		Arquivo vitima = arquivoPrincipal.getArquivoProximo();
		Arquivo vizinhoEsquerda = null;

		if (isPrimeiro(arquivoPrincipal)) {
			this.primeiro = vitima;
		} else {
			vizinhoEsquerda = arquivoPrincipal.getArquivoAnterior();
			vizinhoEsquerda.setArquivoProximo(vitima);
		}

		Arquivo vizinhoDireita = vitima.getArquivoProximo();

		vitima.setArquivoAnterior(vizinhoEsquerda);
		vitima.setArquivoProximo(arquivoPrincipal);
		arquivoPrincipal.setArquivoAnterior(vitima);
		arquivoPrincipal.setArquivoProximo(vizinhoDireita);
		if (vizinhoDireita != null) {
			vizinhoDireita.setArquivoAnterior(arquivoPrincipal);
		}

	}

	private Boolean isPrimeiro(Arquivo arquivo) {
		return this.primeiro.equals(arquivo);
	}

	private Boolean isUltimo(Arquivo arquivo) {
		return getUltimoElemento().equals(arquivo);
	}

	public Arquivo getPrimeiro() {
		return this.primeiro;
	}

	public Arquivo getProximo(Arquivo arquivo) {
		return arquivo.getArquivoProximo();
	}

	public Boolean hasNext(Arquivo arquivo) {
		return arquivo.getArquivoProximo() != null ? true : false;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");

		Arquivo arquivo = this.primeiro;
		while (arquivo != null) {
			sb.append(arquivo.toString() + " -> ");
			arquivo = arquivo.getArquivoProximo();
		}

		sb.append("]");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(primeiro);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListaArquivos other = (ListaArquivos) obj;
		return Objects.equals(primeiro, other.primeiro);
	}

}
