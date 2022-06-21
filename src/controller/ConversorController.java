package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.HorizontalAlignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Arquivo;
import model.ListaArquivos;
import model.TipoArquivo;

public class ConversorController {

	private ListaArquivos listaArquivos = new ListaArquivos();

	@FXML
	private HBox menuBar;

	@FXML
	private HBox boxImages;

	@FXML
	private Button btnImage;

	@FXML
	private Button btnGerar;

	@FXML
	private Button btnTelaDireita;

	@FXML
	private Button btnTelaEsquerda;

	@FXML
	private AnchorPane pnlPrincipal;

	@FXML
	private ScrollPane scPanel;

	@FXML
	void onBtnTelaDireita(ActionEvent event) {
		btnTelaDireita.hoverProperty().addListener(ev -> {
			System.out.println("Hover  funfando");
		});

	}

	@FXML
	void onBtnTelaEsquerda(ActionEvent event) {
		btnTelaEsquerda.hoverProperty().addListener(ev -> {
			System.out.println("Hover  funfando");
		});
	}

	@FXML
	void onClickBtnCarregar(ActionEvent event) throws IOException {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selecionar arquivos");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		fileChooser.getExtensionFilters().add(
				new ExtensionFilter("Pdf, Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.pdf", "*.PDF", "*.Pdf"));

		// Quando abre a localizador de arquivos esconde a tela do programa
		// Stage stage = (Stage) pnlPrincipal.getScene().getWindow();

		List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

		if (selectedFiles != null) {

			int count = 1;
			for (File file : selectedFiles) {

				Arquivo arquivo = new Arquivo(getTipo(file.getAbsolutePath()), file.getAbsolutePath(), file.getName(),
						count);
				listaArquivos.adicionar(arquivo);

				count++;
			}
			listarArquivos(listaArquivos);

		}

	}

	private void listarArquivos(ListaArquivos listaArquivos) {
		limparTela();
		Arquivo arquivo = listaArquivos.getPrimeiro();
		while (arquivo != null) {
			boxImages.getChildren().add(gerarCard(arquivo));
			arquivo = listaArquivos.getProximo(arquivo);
		}
	}

	private void limparTela() {
		boxImages.getChildren().removeAll(boxImages.getChildren());
	}

	@FXML
	void onClickBtnGerar(ActionEvent event) {
		if (!listaArquivos.listaVazia()) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Salvar");
			fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
			fileChooser.getExtensionFilters().add(new ExtensionFilter("PDF", "*.pdf"));
			fileChooser.setInitialFileName("Arquivo");
			File pathSave = fileChooser.showSaveDialog(null);
			gerarPDF(pathSave.getAbsolutePath() + ".pdf");
			carregarTelaSucesso(pathSave.getAbsolutePath());
		}

	}

	public void carregarTelaSucesso(String pathSave) {
		this.listaArquivos = new ListaArquivos();
		limparTela();
		boxImages.getChildren().add(gerarSucesso(pathSave));
	}

	public void gerarPDF(String pathSave) {

		try {
			PdfDocument pdf = new PdfDocument(new PdfWriter(pathSave));
			Document document = new Document(pdf, PageSize.A4);
			document.setMargins(20f, 20f, 20f, 20f);

			Arquivo arquivo = listaArquivos.getPrimeiro();
			while (arquivo != null) {

				if (arquivo.getTipoArquivo().equals(TipoArquivo.IMAGEM)) {
					if (pdf.getNumberOfPages() != 0) {
						document.add(new AreaBreak(AreaBreakType.LAST_PAGE));
						document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
					}
					ImageData data = ImageDataFactory.create(arquivo.getPathArquivo());
					com.itextpdf.layout.element.Image imagePdf = new com.itextpdf.layout.element.Image(data);
					imagePdf.setHorizontalAlignment(HorizontalAlignment.CENTER);
					document.add(imagePdf);
				} else {
					PdfDocument pdf2 = new PdfDocument(new PdfReader(arquivo.getPathArquivo()));

					for (int i = 1; i <= pdf2.getNumberOfPages(); i++) {
						PdfPage page = pdf2.getPage(i).copyTo(pdf);
						pdf.addPage(page);
					}
					pdf2.close();
				}

				arquivo = listaArquivos.getProximo(arquivo);
			}
			document.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private TipoArquivo getTipo(String path) {
		String extensao = path.substring(path.lastIndexOf(".") + 1);

		if (extensao.equals("pdf")) {
			return TipoArquivo.PDF;
		}
		if (extensao.equals("jpg") || extensao.equals("jpeg") || extensao.equals("png") || extensao.equals("gif")) {
			return TipoArquivo.IMAGEM;
		}

		System.out.println("Extensao invalida");
		return null;
	}

	private VBox gerarCard(Arquivo arquivo) {
		VBox card = new VBox();
		card.setSpacing(10);
		card.setAlignment(Pos.CENTER);
		card.setStyle("-fx-border-color: grey;" + "-fx-border-radius: 15px;" + "-fx-border-insets: 10 0 0 10;"
				+ "-fx-padding: 10");

		HBox botoes = new HBox();
		botoes.setSpacing(25);
		botoes.setAlignment(Pos.CENTER);

		Button btnEsquerda = new Button("<--");
		btnEsquerda.setUserData(arquivo.getId().toString());
		btnEsquerda.setOnAction(evento -> {
			int id = Integer.parseInt((String) ((Node) evento.getSource()).getUserData());
			listaArquivos.trocarAnteriorById(id);
			listarArquivos(listaArquivos);
		});

		Button btnDireita = new Button("-->");
		btnDireita.setUserData(arquivo.getId().toString());
		btnDireita.setOnAction(evento -> {
			int id = Integer.parseInt((String) ((Node) evento.getSource()).getUserData());
			listaArquivos.trocarProximoById(id);
			listarArquivos(listaArquivos);
		});

		Button btnExcluir = new Button("Excluir");
		btnExcluir.setUserData(arquivo.getId().toString());
		btnExcluir.setOnAction(evento -> {
			for (Node node : boxImages.getChildren()) {
				if (node.getId().equals(((Node) evento.getSource()).getUserData())) {
					boxImages.getChildren().remove(node);
					listaArquivos.removerById(Integer.parseInt(node.getId()));
					return;
				}
			}
		});

		botoes.getChildren().addAll(btnEsquerda, btnExcluir, btnDireita);

		Label label = new Label(arquivo.getNome());
		label.setMaxWidth(250);

		ImageView imagem = null;
		if (arquivo.getTipoArquivo().equals(TipoArquivo.PDF)) {
			imagem = new ImageView(new Image(getClass().getResourceAsStream("/resources/imagePDF.png")));
		} else {
			try {
				InputStream streamImage = new FileInputStream(arquivo.getPathArquivo());
				imagem = new ImageView(new Image(streamImage));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		imagem.setFitHeight(400);
		imagem.setFitWidth(250);
		card.setId(arquivo.getId().toString());

		card.getChildren().addAll(label, botoes, imagem);

		return card;
	}

	public VBox gerarSucesso(String pathArquivo) {
		VBox card = new VBox();
		card.setSpacing(15);
		card.setAlignment(Pos.CENTER);

		Label label = new Label("Arquivo gerado em: " + pathArquivo);

		ImageView imagem = new ImageView(new Image(getClass().getResourceAsStream("/resources/check.png")));
		imagem.setFitHeight(300);
		imagem.setFitWidth(300);

		card.getChildren().addAll(imagem, label);

		return card;
	}

	public boolean onCloseQuery() {
		Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
		alerta.setTitle("Pergunta");
		alerta.setHeaderText("Deseja sair do sistema?");

		ButtonType botaoNao = ButtonType.NO;
		ButtonType botaoSim = ButtonType.YES;

		alerta.getButtonTypes().setAll(botaoSim, botaoNao);

		Optional<ButtonType> opcaoClicada = alerta.showAndWait();

		return opcaoClicada.get() == botaoSim ? true : false;
	}

	@FXML
	public void initialize() {
		btnTelaEsquerda.hoverProperty().addListener(ev -> {
			scPanel.setHvalue(scPanel.getHvalue() - 0.02);
		});

		btnTelaDireita.hoverProperty().addListener(ev -> {
			scPanel.setHvalue(scPanel.getHvalue() + 0.02);
		});
	}

}
