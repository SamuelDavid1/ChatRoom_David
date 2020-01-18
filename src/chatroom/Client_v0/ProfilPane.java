package chatroom.Client_v0;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.Event;


public class ProfilPane extends GridPane {
	private View view;
	
	Label lblName = new Label();
	TextField txtName = new TextField();
	Label lblPasswort = new Label();
	TextField txtPasswort = new TextField();
	Button btnCreateLogin = new Button();
	Button btnChangePasswort = new Button();
	Button btnDeleteAccount = new Button();
	Button btnAbbrechen = new Button();
	
	public ProfilPane() {
		this.getStylesheets().add(getClass().getResource("Client.css").toExternalForm());
		
		this.add(lblName, 1, 1);
		this.add(txtName, 2, 1);
		this.add(lblPasswort, 3, 1);
		this.add(txtPasswort, 4, 1);
		
		HBox btnBox = new HBox();
		btnBox.setId("btnBox");
		btnBox.getChildren().addAll(btnCreateLogin, btnChangePasswort, btnDeleteAccount, btnAbbrechen);
		
		this.add(btnBox, 5, 1);
		
		this.setId("startArea");
		this.setAlignment(Pos.TOP_LEFT);
		this.setHgap(20);
		this.setVgap(10);
		

        // Specifies the modality for new window.
		//stageProfil.initModality(Modality.WINDOW_MODAL);

    
	}
	

}
