package chatroom.Client_v0;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;

public class Controller {
	private Model model;
	private View view;
	public int i = 0;
	public ProfilPane profilview;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	
	
    public Controller(Model model, View view) {
		
		this.model = model;
		this.view = view;
		
		model.messageTextProperty().addListener((observable, oldValue, newValue) -> {
			view.txtTexts.appendText(newValue + "\n");
		});
		
		view.btnCreateChatroom.setDisable(true);
		view.btnJoinChatroom.setDisable(true);
		view.btnSend.setDisable(true);
		view.btnLogin.setDisable(false);
		view.btnDeleteChatroom.setDisable(true);
		view.btnLeaveChatroom.setDisable(true);
		view.btnLogout.setDisable(true);
		
		view.txtName.setDisable(false);
		view.listView.setDisable(true);
		view.txtMessage.setDisable(true);
		view.txtPasswort.setDisable(false);
		view.txtTexts.setDisable(true);
		view.cbPublic.setDisable(true);
		view.txtChatroom.setDisable(true);
	
		
		view.btnLogin.setOnAction(e -> {
			String username = view.txtName.getText();
        	String password = view.txtPasswort.getText();
        	String result = model.Login(username, password);
        	view.btnCreateChatroom.setDisable(false);
        	view.btnLogin.setDisable(true);
        	view.txtChatroom.setDisable(false);
        	view.cbPublic.setDisable(false);
        	view.btnLogout.setDisable(false);
        	view.txtName.setDisable(true);
        	view.txtPasswort.setDisable(true);
        	
        	
        });
		
		view.btnCreateChatroom.setOnAction(e -> {
		
			String token = model.getToken();
        	String name = view.txtChatroom.getText();
        	boolean isPublic = view.cbPublic.isSelected();
        	String result = model.createChatroom(token, name, isPublic);
        	view.listView.setDisable(false);
        	model.addNewElement(name);
   
        });
		
		view.btnLeaveChatroom.setOnAction(e -> {
			
			String username = view.txtName.getText();
			String token = model.getToken();
        	String name = view.selectedChatroom.getText();
        	String result = model.leaveChatroom(token, name, username);
        	view.txtTexts.setText("");
        	view.btnSend.setDisable(true);
        	view.txtMessage.setDisable(true);
        	view.txtTexts.setDisable(true);
        	view.btnLeaveChatroom.setDisable(true);
   
        });
		
		view.btnDeleteChatroom.setOnAction(e -> {
			
			String token = model.getToken();
        	String name = view.selectedChatroom.getText();
        	String result = model.deleteChatroom(token, name);
        	view.btnSend.setDisable(true);
        	view.txtMessage.setDisable(true);
        	view.txtTexts.setDisable(true);
        	model.removeElement(name);
        	view.txtMessage.setText("");
        });
	
		
		model.getElements().addListener((ListChangeListener<String>) c -> {
			while (c.next()) {
				view.listView.scrollTo(c.getFrom());
			}
		});
		
		view.listView.getSelectionModel().selectedItemProperty().addListener(
	            new ChangeListener<String>() {
	                public void changed(ObservableValue<? extends String> ov, 
	                    String old_val, String new_val) {
	                	view.selectedChatroom.setText(new_val);
	                	view.btnJoinChatroom.setDisable(false);
	                	view.btnDeleteChatroom.setDisable(false);
	            }
	        });
		
		view.btnJoinChatroom.setOnAction(e -> {
			String username = view.txtName.getText();
			String token = model.getToken();
        	String name = view.selectedChatroom.getText();
        	String result = model.joinChatroom(token, name, username);
        	view.btnSend.setDisable(false);
        	view.txtMessage.setDisable(false);
        	view.btnLeaveChatroom.setDisable(false);
        	view.txtMessage.setText("");
        	
        });
		
		view.btnSend.setOnAction(e -> {
			String username = view.txtName.getText();
			String token = model.getToken();
			String destination = view.selectedChatroom.getText();
        	String message = view.txtMessage.getText();
        	String result = model.sendMessage(token, destination, message);
        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        	view.txtTexts.appendText(" (Sent from: " + username + " at " + sdf.format(timestamp) + ")" + "\n");
        	view.txtTexts.setDisable(false);
        	view.txtMessage.setText("");
        });
		
		view.btnLogout.setOnAction(e -> {
			String result = model.Logout();
        	view.btnCreateChatroom.setDisable(true);
        	view.btnDeleteChatroom.setDisable(true);
        	view.btnLeaveChatroom.setDisable(true);
        	view.btnJoinChatroom.setDisable(true);
        	view.listView.setDisable(true);
        	view.txtTexts.setDisable(true);
        	view.btnSend.setDisable(true);
        	view.txtMessage.setDisable(true);
        	view.btnLogin.setDisable(false);
        	view.txtChatroom.setDisable(true);
        	view.cbPublic.setDisable(true);
        	view.btnLogout.setDisable(true);
        	view.txtName.setDisable(false);
        	view.txtPasswort.setDisable(false);
        	view.txtMessage.setText("");
        	
        });
		
		
		view.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                view.stop();
            }
        });
		
    }
        

}
