package chatroom.Client_v0;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Main mainProgram; // singleton
	private ServiceLocator serviceLocator; // resources, after initialization


	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
    public void init() {
        if (mainProgram == null) {
            mainProgram = this;
        } else {
            Platform.exit();
        }
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		Model model = new Model();
		View view = new View(primaryStage, model);
		Controller controller = new Controller(model, view);
		model.initialize();
		serviceLocator = ServiceLocator.getServiceLocator();
		view.start();

	}
	
	public void stop() {
        serviceLocator.getConfiguration().save();

        serviceLocator.getLogger().info("Application terminated");
    }
	
	protected static Main getMainProgram() {
        return mainProgram;
    }

}
