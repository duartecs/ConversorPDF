package controller;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class SomarController implements Initializable {

	@FXML
	private Button btnLimpar;

	@FXML
	private Button btnSomar;

	@FXML
	private Label lblNumero1;

	@FXML
	private Label lblNumero2;

	@FXML
	private Label lblResultado;

	@FXML
	private Label lblResultadoSoma;

	@FXML
	private GridPane pnlGrid;

	@FXML
	private AnchorPane pnlPrincipal;

	@FXML
	private TextField txtNumero1;

	@FXML
	private TextField txtNumero2;

	@FXML
	void onClickBtnLimpar(ActionEvent event) {
		this.txtNumero1.setText(new String());
		this.txtNumero2.setText(new String());
		this.lblResultadoSoma.setText(new String("0"));
		this.txtNumero1.requestFocus();

	}

	@FXML
	void onClickBtnSomar(ActionEvent event) {
//		try {
//
//			double numero1 = Double.parseDouble(this.txtNumero1.getText());
//			double numero2 = Double.parseDouble(this.txtNumero2.getText());
//
//			numero1 = numero1 + numero2;
//
//			this.lblResultadoSoma.setText(new Double(numero1).toString());
//
//		} catch (NumberFormatException e) {
//			StringWriter sw = new StringWriter();
//			PrintWriter pw = new PrintWriter(sw);
//			e.printStackTrace(pw);
//			String textoErro = sw.toString();
//
//			Alert alerta = new Alert(AlertType.ERROR);
//			alerta.setTitle("Erro!");
//			alerta.setHeaderText("Aconteceu um erro de convesão numerica.");
//
//			Label label = new Label("Segue a pilha de exceção:");
//
//			TextArea textArea = new TextArea(textoErro);
//			textArea.setEditable(false);
//			textArea.setWrapText(true);
//
//			textArea.setMaxWidth(Double.MAX_VALUE);
//			textArea.setMaxHeight(Double.MAX_VALUE);
//			GridPane.setVgrow(textArea, Priority.ALWAYS);
//			GridPane.setHgrow(textArea, Priority.ALWAYS);
//
//			GridPane expConteudo = new GridPane();
//			expConteudo.setMaxWidth(Double.MAX_VALUE);
//			expConteudo.setMaxHeight(Double.MAX_VALUE);
//			expConteudo.add(label, 0, 0);
//			expConteudo.add(textArea, 0, 1);
//			alerta.getDialogPane().setExpandableContent(expConteudo);
//			alerta.showAndWait();
//
//		}

		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selecionar a imagem");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			System.out.println(selectedFile.getAbsolutePath());
		}

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.txtNumero1.requestFocus();

	}

}
