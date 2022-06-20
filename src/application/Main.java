package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Conversor PDF");
			// primaryStage.setResizable(false);

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Conversor.fxml"));
			Parent arquivoXML = loader.load();

			Scene conversorCena = new Scene(arquivoXML);
			primaryStage.setScene(conversorCena);

			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
