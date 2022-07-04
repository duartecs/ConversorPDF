package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Arquivo;
import model.TipoArquivo;

public class ConversorController {

	private List<Arquivo> listaArquivos = new LinkedList<>();

	private List<File> arquivosInvalidos = new ArrayList<>();

	private Instant instant;

	private Integer sequencial = 0;

	private final String formatArchive = ".pdf";

	@FXML
	private HBox menuBar;

	@FXML
	private HBox boxImages;

	@FXML
	private Button btnImage;

	@FXML
	private Button btnGerar;

	@FXML
	private AnchorPane pnlPrincipal;

	@FXML
	private ScrollPane scPanel;

	@FXML
	public void initialize() {

		this.instant = Instant.now();

		scPanel.setOnScroll(event -> {
			if (event.getDeltaX() == 0 && event.getDeltaY() != 0) {
				scPanel.setHvalue(scPanel.getHvalue() - event.getDeltaY() / boxImages.getWidth());
			}
		});

		pnlPrincipal.heightProperty().addListener(event -> {
			if (Instant.now().isAfter(this.instant.plusMillis(500))) {
				this.instant = Instant.now();
				listarArquivos();
			}
		});
	}

	@FXML
	void handleDragOver(DragEvent event) {
		if (event.getDragboard().hasFiles()) {
			event.acceptTransferModes(TransferMode.ANY);
		}
	}

	@FXML
	void handleDragDrop(DragEvent event) {
		List<File> files = event.getDragboard().getFiles();

		adicionarLista(files);

		listarArquivos();
	}

	@FXML
	void onClickBtnCarregar(ActionEvent event) throws IOException {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selecionar arquivos");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		fileChooser.getExtensionFilters().add(
				new ExtensionFilter("Pdf, Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.pdf", "*.PDF", "*.Pdf"));

		List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

		if (selectedFiles != null) {

			adicionarLista(selectedFiles);

			listarArquivos();
		}
	}

	@FXML
	void onClickBtnGerar(ActionEvent event) {

		if (!listaArquivos.isEmpty()) {

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Salvar");
			fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
			fileChooser.getExtensionFilters().add(new ExtensionFilter("PDF", "*.pdf"));
			fileChooser.setInitialFileName("Arquivo");

			File pathSave = fileChooser.showSaveDialog(null);

			String pathSaveFormated = pathSave.getAbsolutePath().toLowerCase().contains(formatArchive)
					? pathSave.getAbsolutePath()
					: pathSave.getAbsolutePath() + formatArchive;

			gerarPDF(pathSaveFormated);

			carregarTelaSucesso(pathSave.getAbsolutePath());
		}
	}

	private void adicionarLista(List<File> arquivosSelecionados) {
		List<File> arquivosOrdenados = arquivosSelecionados.stream()
				.sorted((a, b) -> a.getName().compareTo(b.getName())).collect(Collectors.toList());

		for (File file : arquivosOrdenados) {

			Arquivo arquivo = new Arquivo(getTipo(file.getAbsolutePath()), file.getAbsolutePath(), file.getName(),
					sequencial++);

			if (arquivo.getTipoArquivo() == null) {
				arquivosInvalidos.add(file);
			} else {
				listaArquivos.add(arquivo);
			}

		}
	}

	private void listarArquivos() {

		limparTela();

		for (Arquivo arquivo : listaArquivos) {
			boxImages.getChildren().add(gerarCard(arquivo));
		}

		if (!this.arquivosInvalidos.isEmpty()) {
			StringBuilder sb = new StringBuilder();

			for (File file : this.arquivosInvalidos) {
				sb.append(file.getName());
				sb.append("\n");
			}
			String erros = sb.toString();
			carregarAlerta("Atenção!", "Estes arquivos não foram adicionados por não serem do tipo imagem ou pdf: ",
					erros);
			this.arquivosInvalidos.clear();
		}

	}

	private void limparTela() {
		boxImages.getChildren().removeAll(boxImages.getChildren());
	}

	public void carregarTelaSucesso(String pathSave) {

		this.listaArquivos.clear();
		this.sequencial = 0;

		limparTela();

		boxImages.getChildren().add(gerarSucesso(pathSave));
	}

	private void carregarAlerta(String titulo, String mensagem, String descricao) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titulo);
		alert.setHeaderText(mensagem);
		alert.setContentText(descricao);
		alert.showAndWait();
	}

	public void gerarPDF(String pathSave) {

		try {
			PdfDocument pdf = new PdfDocument(new PdfWriter(pathSave));
			Document document = new Document(pdf, PageSize.A4);

			float documentWidth = PageSize.A4.getWidth() - 50.00f;
			float documentHeight = PageSize.A4.getHeight() - 50.00f;

			for (Arquivo arquivo : listaArquivos) {

				if (arquivo.getTipoArquivo().equals(TipoArquivo.IMAGEM)) {
					if (pdf.getNumberOfPages() != 0) {
						document.add(new AreaBreak(AreaBreakType.LAST_PAGE));
						document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
					}
					ImageData data = ImageDataFactory.create(arquivo.getPathArquivo());
					com.itextpdf.layout.element.Image imagePdf = new com.itextpdf.layout.element.Image(data);
					imagePdf.setHorizontalAlignment(HorizontalAlignment.CENTER);
					imagePdf.scaleToFit(documentWidth, documentHeight);
					document.add(imagePdf);
				} else {
					PdfDocument pdf2 = new PdfDocument(new PdfReader(arquivo.getPathArquivo()));

					for (int i = 1; i <= pdf2.getNumberOfPages(); i++) {
						PdfPage page = pdf2.getPage(i).copyTo(pdf);
						pdf.addPage(page);
					}
					pdf2.close();
				}
			}

			document.close();

		} catch (FileNotFoundException e) {
			carregarAlerta("Erro", "Problemas ao gerar o arquivo PDF: Arquivo não encontrado", e.getMessage());
			e.printStackTrace();
		} catch (MalformedURLException e) {
			carregarAlerta("Erro", "Problemas ao gerar o arquivo PDF: URL não encontrada", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			carregarAlerta("Erro", "Problemas ao gerar o arquivo PDF: IOException", e.getMessage());
			e.printStackTrace();
		}

	}

	private TipoArquivo getTipo(String path) {
		String extensao = path.substring(path.lastIndexOf(".") + 1).toLowerCase();

		if (extensao.equals("pdf")) {
			return TipoArquivo.PDF;
		}
		if (extensao.equals("jpg") || extensao.equals("jpeg") || extensao.equals("png") || extensao.equals("gif")
				|| extensao.equals("bmp")) {
			return TipoArquivo.IMAGEM;
		}
		return null;
	}

	private VBox gerarCard(Arquivo arquivo) {

		double width = pnlPrincipal.getWidth() / 5;
		double heigth = pnlPrincipal.getHeight() / 1.5;

		VBox card = new VBox();
		card.setSpacing(10);
		card.setAlignment(Pos.CENTER);
		card.setStyle("-fx-border-color: grey;" + "-fx-border-radius: 15px;" + "-fx-border-insets: 10 0 0 10;"
				+ "-fx-padding: 10");

		HBox botoes = new HBox();
		botoes.setSpacing(15);
		botoes.setAlignment(Pos.CENTER);
		botoes.setMaxWidth(width);

		Button btnEsquerda = new Button("<--");
		btnEsquerda.setStyle(getBtnStyle("PADRAO"));
		btnEsquerda.setUserData(arquivo.getId().toString());
		btnEsquerda.setOnMouseEntered(e -> {
			btnEsquerda.setStyle(getBtnStyle("PADRAO:HOVER"));
		});
		btnEsquerda.setOnMouseExited(e -> {
			btnEsquerda.setStyle(getBtnStyle("PADRAO"));
		});
		btnEsquerda.setOnAction(evento -> {
			int id = Integer.parseInt((String) ((Node) evento.getSource()).getUserData());

			Arquivo arquivo2 = findById(id);
			int index = listaArquivos.indexOf(arquivo2);

			if (index > 0) {
				listaArquivos.remove(index);
				listaArquivos.add(index - 1, arquivo2);
			}

			listarArquivos();
			scPanel.setHvalue(scPanel.getHvalue() - getValor());
		});

		Button btnDireita = new Button("-->");
		btnDireita.setStyle(getBtnStyle("PADRAO"));
		btnDireita.setUserData(arquivo.getId().toString());
		btnDireita.setOnMouseEntered(e -> {
			btnDireita.setStyle(getBtnStyle("PADRAO:HOVER"));
		});
		btnDireita.setOnMouseExited(e -> {
			btnDireita.setStyle(getBtnStyle("PADRAO"));
		});
		btnDireita.setOnAction(evento -> {
			int id = Integer.parseInt((String) ((Node) evento.getSource()).getUserData());

			Arquivo arquivo2 = findById(id);
			int index = listaArquivos.indexOf(arquivo2);

			if (index < listaArquivos.size() - 1) {
				listaArquivos.remove(index);
				listaArquivos.add(index + 1, arquivo2);
			}

			listarArquivos();
			scPanel.setHvalue(scPanel.getHvalue() + getValor());
		});

		Button btnExcluir = new Button("Excluir");
		btnExcluir.setStyle(getBtnStyle("EXCLUIR"));
		btnExcluir.setUserData(arquivo.getId().toString());
		btnExcluir.setOnMouseEntered(e -> {
			btnExcluir.setStyle(getBtnStyle("EXCLUIR:HOVER"));
		});
		btnExcluir.setOnMouseExited(e -> {
			btnExcluir.setStyle(getBtnStyle("EXCLUIR"));
		});
		btnExcluir.setOnAction(evento -> {
			for (Node node : boxImages.getChildren()) {
				if (node.getId().equals(((Node) evento.getSource()).getUserData())) {
					boxImages.getChildren().remove(node);
					int id = Integer.parseInt(node.getId());
					Arquivo arquivo2 = findById(id);
					listaArquivos.remove(arquivo2);
					return;
				}
			}
		});

		botoes.getChildren().addAll(btnEsquerda, btnExcluir, btnDireita);

		Label label = new Label(arquivo.getNome());
		label.setAlignment(Pos.CENTER);
		label.setMaxWidth(width);

		ImageView imagem = null;
		if (arquivo.getTipoArquivo().equals(TipoArquivo.PDF)) {
			imagem = new ImageView(new Image(getClass().getResourceAsStream("/resources/imagePDF.png")));
		} else {
			try {
				InputStream streamImage = new FileInputStream(arquivo.getPathArquivo());
				imagem = new ImageView(new Image(streamImage));
			} catch (FileNotFoundException e) {
				carregarAlerta("Erro", "Problemas ao encontrar a imagem padrão do arquivo PDF", e.getMessage());
				e.printStackTrace();
			}

		}

		imagem.setFitHeight(heigth);
		imagem.setFitWidth(width);

		card.setId(arquivo.getId().toString());
		card.getChildren().addAll(label, botoes, imagem);

		return card;
	}

	private VBox gerarSucesso(String pathArquivo) {
		VBox card = new VBox();
		card.setSpacing(15);
		card.setAlignment(Pos.CENTER);
		card.setMinWidth(pnlPrincipal.getWidth());
		card.setPadding(new Insets(25, 0, 0, 0));

		Label label = new Label("Arquivo gerado em: " + pathArquivo);

		ImageView imagem = new ImageView(new Image(getClass().getResourceAsStream("/resources/check.png")));

		imagem.setFitHeight(300);
		imagem.setFitWidth(300);

		card.getChildren().addAll(imagem, label);

		return card;
	}

	private Double getValor() {
		return 1.0 / listaArquivos.size();
	}

	private String getBtnStyle(String tipo) {

		String corPrimaria = null;
		String corSecundaria = null;
		boolean toogle = true;

		switch (tipo) {
		case "PADRAO":
			corPrimaria = "#1e55fa";
			break;

		case "PADRAO:HOVER":
			corPrimaria = "#1e55fa";
			corSecundaria = "#0b02ba";
			toogle = false;
			break;

		case "EXCLUIR":
			corPrimaria = "#bd005e";
			break;

		case "EXCLUIR:HOVER":
			corPrimaria = "#bd005e";
			corSecundaria = "#6e0202";
			toogle = false;
			break;

		default:
			break;
		}

		String padrao = "-fx-padding: 10 10 10 10;" + "-fx-background-color: " + "transparent," + "-fx-inner-border;"
				+ "-fx-border-color: " + corPrimaria + ";" + "-fx-border-radius: 6, 5;"
				+ "    -fx-background-radius: 6, 5;" + "    -fx-background-insets: 0, 1;"
				+ "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );"
				+ "    -fx-text-fill: " + corPrimaria + ";";

		String padraoHover = "-fx-padding: 10 10 10 10;" + "-fx-background-color:" + "        linear-gradient("
				+ corPrimaria + ", " + corSecundaria + ")," + "        radial-gradient(center 50% -40%, radius 200%, "
				+ corPrimaria + " 45%, " + corSecundaria + " 50%);" + "    -fx-background-radius: 6, 5;"
				+ "    -fx-background-insets: 0, 1;"
				+ "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );"
				+ "    -fx-text-fill: #FFFFFF;";

		return toogle ? padrao : padraoHover;
	}

	private Arquivo findById(int id) {
		for (Arquivo arquivo : listaArquivos) {
			if (arquivo.getId().equals(id)) {
				return arquivo;
			}

		}
		return null;
	}

}
