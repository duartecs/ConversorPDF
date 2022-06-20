package model;

public class Teste {

	public static void main(String[] args) {

		ListaArquivos lista = new ListaArquivos();

		Arquivo arquivo1 = new Arquivo(TipoArquivo.IMAGEM, "/home/doc/arquivo1.jpeg", "Imagem 01", 1);
		Arquivo arquivo2 = new Arquivo(TipoArquivo.PDF, "/home/doc/arquivo2.pdf", "PDF 01", 2);
		Arquivo arquivo3 = new Arquivo(TipoArquivo.IMAGEM, "/home/doc/arquivo3.jpeg", "Imagem 02", 3);
		Arquivo arquivo4 = new Arquivo(TipoArquivo.IMAGEM, "/home/doc/arquivo4.jpeg", "Imagem 03", 4);

		System.out.println(lista);

		System.out.println("Adicionando primeiro elemento");
		lista.adicionar(arquivo1);
		System.out.println(lista);

		System.out.println("Adicionando segundo elemento");
		lista.adicionar(arquivo2);
		System.out.println(lista);

		System.out.println("Adicionando terceiro elemento");
		lista.adicionar(arquivo3);
		System.out.println(lista);

		System.out.println("Adicionando terceiro elemento");
		lista.adicionar(arquivo4);
		System.out.println(lista);

		System.out.println("-----------------------------------------------------------------------");
		System.out.println("Contador = " + lista.getContador());
		System.out.println("-----------------------------------------------------------------------");

//		System.out.println("Buscando id = 2 " + lista.getById(2).toString());

//		System.out.println("-----------------------------------------------------------------------");
//		System.out.println("Remover id = 2 ");
//		System.out.println(lista.removerById(2));
//		System.out.println("Contador = " + lista.getContador());
//		System.out.println(lista);
//		System.out.println("-----------------------------------------------------------------------");

		System.out.println("Trocando de lugar 2 com o 1");

		System.out.println("-----------------------------------------------------------------------");
		lista.trocarProximoById(1);
		System.out.println(lista);
		System.out.println("-----------------------------------------------------------------------");

	}

}
