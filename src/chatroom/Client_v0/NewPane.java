package chatroom.Client_v0;

import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.Event;


public class NewPane extends GridPane {
	private View view;
	
	private ScaleTransition scaleTransition;
	
	Label lblIPAdress = new Label("IP Address");
	TextField txtIPAdress = new TextField();
	Label lblPort = new Label("Port");
	TextField txtPort = new TextField();
	Button btnConnect = new Button("Connect");
	CheckBox cbSecurity = new CheckBox("Use Secure Sockets");
	
	public NewPane() {
		this.getStylesheets().add(getClass().getResource("Client.css").toExternalForm());
		
		this.add(lblIPAdress, 1, 1);
		this.add(txtIPAdress, 2, 1);
		this.add(lblPort, 3, 1);
		this.add(txtPort, 4, 1);
		this.add(cbSecurity, 5, 1);
		this.add(btnConnect, 6, 1);
		
		scaleTransition = new ScaleTransition(Duration.seconds(1), btnConnect);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.setCycleCount(Timeline.INDEFINITE);
        scaleTransition.setAutoReverse(true);
		
		
		this.setId("startArea");
		this.setAlignment(Pos.TOP_LEFT);
		this.setHgap(20);
		this.setVgap(10);
	}
	
	public void play() {
        scaleTransition.play();
    }
 
    public void stop() {
        scaleTransition.stop();
    }
	

}
