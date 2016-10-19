package thecat.tools.dao.fx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import thecat.tools.dao.fx.screens.ScreenEnum;
import thecat.tools.dao.fx.screens.ScreensController;

/**
 * Created by thecat on 09/10/16.
 */
public class Main extends Application {

    public void start(Stage primaryStage) throws Exception{
        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(ScreenEnum.MAIN.getId(), ScreenEnum.MAIN.getFxml());
        mainContainer.loadScreen(ScreenEnum.DAO_OPTIONS.getId(), ScreenEnum.DAO_OPTIONS.getFxml());

        mainContainer.setScreen(ScreenEnum.MAIN.getId());

        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        primaryStage.setTitle("DaoGeneratorFx");
        primaryStage.setScene(scene);
        primaryStage.getScene().getStylesheets().add
                (Main.class.getClassLoader().getResource("css/style.css").toExternalForm());
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
    }

    public static void main(String[] args) {
        launch(args);
    }
}
