package chatroom.Client_v0;

import javafx.geometry.Pos;

import java.util.Locale;
import java.util.logging.Logger;

import chatroom.commons.Translator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class View  {
	ServiceLocator sl = ServiceLocator.getServiceLocator();
	Logger logger = sl.getLogger();
	protected Stage primaryStage;
	private Model model;
	Scene scene, startScene, profilScene;
	ProfilPane profilLayout = new ProfilPane();
	NewPane startLayout = new NewPane();
	Menu menuFileLanguage = new Menu();
	
	
	
	Menu menuProfil = new Menu();
	MenuItem menuGoTo = new MenuItem();
	
    Label lblIPAdress = new Label();
    TextField txtIPAdress = new TextField();
    Label lblPort = new Label();
    TextField txtPort = new TextField();
    Label lblName = new Label();
    TextField txtName = new TextField();
    Label lblPasswort = new Label();
    TextField txtPasswort = new TextField();
    Button btnConnect = new Button();
    Button btnLogin = new Button();
    Button btnLogout = new Button();
	
    TextArea txtTexts = new TextArea();
	
    TextField txtMessage = new TextField();
    Button btnSend = new Button();
	
    TextField txtChatroom = new TextField();
    
    CheckBox cbPublic = new CheckBox();   
    
    Button btnCreateChatroom = new Button();
    Button btnJoinChatroom = new Button();
    Button btnLeaveChatroom = new Button ();
    Button btnDeleteChatroom = new Button ();

    ListView<String> listView;
    final Label selectedChatroom = new Label();
	
	public View(Stage primaryStage, Model model) {
		this.primaryStage = primaryStage;
		this.model = model;
		
		lblName.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		lblPasswort.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		btnSend.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		btnCreateChatroom.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		btnJoinChatroom.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		
		txtName.setMinWidth(200); txtName.setPrefWidth(200);
		txtPasswort.setMinWidth(200); txtPasswort.setPrefWidth(200);
		txtTexts.setMinHeight(700); txtTexts.setPrefHeight(700);		
		
		
		startLayout.play();
		startLayout.setId("root");
		
		startLayout.txtIPAdress.textProperty().addListener(
				(observable, oldValue, newValue) -> validateIpAddress(newValue)
		);
		
		startLayout.txtPort.textProperty().addListener(
				(observable, oldValue, newValue) -> validatePortNumber(newValue)
		);
		
		//startLayout.btnConnect.disableProperty().bind(startLayout.txtIPAdress.textProperty().isEmpty());
		//startLayout.btnConnect.disableProperty().bind(startLayout.txtPort.textProperty().isEmpty());
		startLayout.btnConnect.setDisable(true);
		startLayout.btnConnect.setOnAction(e ->{
			String ipAddress = startLayout.txtIPAdress.getText();
			int port = Integer.parseInt(startLayout.txtPort.getText());
			boolean secure = startLayout.cbSecurity.isSelected();
			model.connect(ipAddress, port, secure);
			
			primaryStage.setScene(scene);
		});
        startScene = new Scene(startLayout, 1500, 900);
        
        
        profilLayout.setId("root");
        profilLayout.btnCreateLogin.disableProperty().bind(profilLayout.txtName.textProperty().isEmpty());
        profilLayout.btnCreateLogin.disableProperty().bind(profilLayout.txtPasswort.textProperty().isEmpty());
        profilLayout.btnCreateLogin.setOnAction(e -> {
        	String username = profilLayout.txtName.getText();
        	String password = profilLayout.txtPasswort.getText();
        	String result = model.createLogin(username, password);
        	String receiveResult = model.getReceiveResult();
        	
        		primaryStage.setScene(scene);
            	
            	this.btnLogin.setDisable(false);
            	this.txtName.setDisable(false);
            	this.txtPasswort.setDisable(false);
            	profilLayout.btnChangePasswort.setDisable(false);
            	profilLayout.btnDeleteAccount.setDisable(false);
        	
        	
        });
        profilLayout.btnChangePasswort.setOnAction(e -> {
        	String token = model.getToken();
        	String password = profilLayout.txtPasswort.getText();
        	String result = model.changePasswort(token, password);
        	String receiveResult = model.getReceiveResult();
        	
        		primaryStage.setScene(scene);
            	
            	this.btnCreateChatroom.setDisable(true);
            	this.btnDeleteChatroom.setDisable(true);
            	this.btnLeaveChatroom.setDisable(true);
            	this.btnJoinChatroom.setDisable(true);
            	this.listView.setDisable(true);
            	this.txtTexts.setDisable(true);
            	this.btnSend.setDisable(true);
            	this.txtMessage.setDisable(true);
            	this.btnLogin.setDisable(false);
            	this.txtChatroom.setDisable(true);
            	this.cbPublic.setDisable(true);
            	this.btnLogout.setDisable(true);
            	this.txtName.setDisable(false);
            	this.txtPasswort.setDisable(false);
        	
        	
        });
        profilLayout.btnDeleteAccount.setOnAction(e -> {
        	String token = model.getToken();
        	String result = model.deleteLogin(token);
        	
        	this.btnCreateChatroom.setDisable(true);
        	this.btnDeleteChatroom.setDisable(true);
        	this.btnLeaveChatroom.setDisable(true);
        	this.btnJoinChatroom.setDisable(true);
        	this.listView.setDisable(true);
        	this.txtTexts.setDisable(true);
        	this.btnSend.setDisable(true);
        	this.txtMessage.setDisable(true);
        	this.btnLogin.setDisable(false);
        	this.txtChatroom.setDisable(true);
        	this.cbPublic.setDisable(true);
        	this.btnLogout.setDisable(true);
        	this.txtName.setDisable(false);
        	this.txtPasswort.setDisable(false);
        	String receiveResult = model.getReceiveResult();
        	
        		
            	
            	profilLayout.btnChangePasswort.setDisable(true);
            	profilLayout.btnDeleteAccount.setDisable(true);
        	
        	
        });
        profilLayout.btnAbbrechen.setOnAction(e -> {
        	primaryStage.setScene(scene);
        });
        profilLayout.btnChangePasswort.setDisable(true);
        profilLayout.btnDeleteAccount.setDisable(true);
        profilScene = new Scene(profilLayout, 1500, 900);
        
        for (Locale locale : sl.getLocales()) {
            MenuItem language = new MenuItem(locale.getLanguage());
            menuFileLanguage.getItems().add(language);
            language.setOnAction( event -> {
    				sl.getConfiguration().setLocalOption("Language", locale.getLanguage());
                 sl.setTranslator(new Translator(locale.getLanguage()));
                 updateTexts();
             });
         }
	    
	    MenuBar menuBar = new MenuBar();
       	menuProfil.getItems().addAll(menuGoTo);
       	menuGoTo.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	primaryStage.setScene(profilScene);
            }
        });
	    menuBar.getMenus().addAll(menuFileLanguage, menuProfil);
		
	    BorderPane root = new BorderPane();
	    root.setId("root");
		root.setTop(menuBar);
 
		HBox middleBox = new HBox();
        middleBox.setId("Middle Box");
		
        VBox mainBox = new VBox();
        mainBox.setId("MainBox");
		
        HBox topBox = new HBox();
        topBox.setId("TopBox");
        topBox.getChildren().addAll(lblName, txtName, lblPasswort, txtPasswort, btnLogin, btnLogout);
        txtIPAdress.setId("IP");
        txtPort.setId("Port");
		
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
        scrollPane.setContent(txtTexts);
        txtTexts.setWrapText(true);	
        scrollPane.setId("ScrollPane");
		
        HBox bottomBox = new HBox();
        bottomBox.setId("BottomBox");
        bottomBox.getChildren().addAll(txtMessage, btnSend);
        HBox.setHgrow(txtMessage, Priority.ALWAYS);	
        
        mainBox.getChildren().addAll(topBox, scrollPane, bottomBox);
        root.setBottom(mainBox);
		
        VBox chatroomBox = new VBox();
        chatroomBox.setId("ChatroomBox");
		
        HBox centerBox = new HBox();
        centerBox.setId("CenterBox");
        centerBox.getChildren().addAll(txtChatroom, cbPublic, btnCreateChatroom, btnJoinChatroom, btnLeaveChatroom, btnDeleteChatroom);   
        
        listView = new ListView<>(model.getElements());
        listView.setId("List");
		
        chatroomBox.getChildren().addAll(centerBox, listView, selectedChatroom);
		
        middleBox.getChildren().addAll(mainBox, chatroomBox);
		
        root.setCenter(middleBox);
        
		scene = new Scene(root, 1500, 900);
		scene.getStylesheets().add(
                getClass().getResource("Client.css").toExternalForm());
		
		primaryStage.setScene(startScene);
		primaryStage.setTitle("Client");
	}
	
	public void start() {
		primaryStage.show();
		primaryStage.setMinWidth(primaryStage.getWidth());
		primaryStage.setMinHeight(primaryStage.getHeight());
		updateTexts();
	}
	
	public void stop() {
		primaryStage.hide();
	}
	
	public Stage getStage() {
	    return primaryStage;
	}
	
	protected void updateTexts() {
		Translator t = ServiceLocator.getServiceLocator().getTranslator();
	        
	    // The menu entries
	    this.menuFileLanguage.setText(t.getString("program.menu.file.language"));
	    this.menuProfil.setText(t.getString("menu.profil"));
	    this.menuGoTo.setText(t.getString("menuitem.goto"));
	    
	    // Text
	    startLayout.lblIPAdress.setText(t.getString("label.IPAdress"));
	    startLayout.lblPort.setText(t.getString("label.port"));
	    
	    this.lblName.setText(t.getString("label.name"));
	    this.lblPasswort.setText(t.getString("label.password"));
	    
	    profilLayout.lblName.setText(t.getString("label.name"));
	    profilLayout.lblPasswort.setText(t.getString("label.password"));
	    
	        
	    // Other controls
        startLayout.btnConnect.setText(t.getString("button.connect"));
        startLayout.cbSecurity.setText(t.getString("checkbox.security"));
        
        this.btnSend.setText(t.getString("button.send"));
        this.btnLogin.setText(t.getString("button.login"));
        this.btnLogout.setText(t.getString("button.logout"));
        this.btnCreateChatroom.setText(t.getString("button.createchatroom"));
        this.btnJoinChatroom.setText(t.getString("button.joinchatroom"));
        this.btnLeaveChatroom.setText(t.getString("button.leavechatroom"));
        this.btnDeleteChatroom.setText(t.getString("button.deletechatroom"));
        this.cbPublic.setText(t.getString("checkbox.public"));
        
        profilLayout.btnCreateLogin.setText(t.getString("button.createlogin"));
        profilLayout.btnChangePasswort.setText(t.getString("button.changepassword"));
        profilLayout.btnDeleteAccount.setText(t.getString("button.deleteaccount"));
        profilLayout.btnAbbrechen.setText(t.getString("button.abbrechen"));
	    }
	private void validateIpAddress(String ipAddress) {
		boolean formatOK = false;
		// Check for validity (not complete, but not bad)
		String ipPieces[] = ipAddress.split("\\."); // Must escape (see
													// documentation)
		// Must have 4 parts
		if (ipPieces.length == 4) {
			// Each part must be an integer 0 to 255
			formatOK = true; // set to false on the first error
			int byteValue = -1;
			for (String s : ipPieces) {
				byteValue = Integer.parseInt(s); // may throw
													// NumberFormatException
				if (byteValue < 0 | byteValue > 255) formatOK = false;
			}
		}
		
		if (formatOK) {
			startLayout.txtIPAdress.setStyle("-fx-text-inner-color: green;");
        } else {
        	startLayout.txtIPAdress.setStyle("-fx-text-inner-color: red;");
        }
		
	}

	private void validatePortNumber(String portText) {
		boolean formatOK = false;
		try {
			int portNumber = Integer.parseInt(portText);
			if (portNumber >= 1024 & portNumber <= 65535) {
				formatOK = true;
			}
		} catch (NumberFormatException e) {
		}

		if (formatOK) {
			startLayout.btnConnect.setDisable(false);
			startLayout.txtPort.setStyle("-fx-text-inner-color: green;");
        } else {
        	startLayout.btnConnect.setDisable(true);
        	startLayout.txtPort.setStyle("-fx-text-inner-color: red;");
        }

	}
}
