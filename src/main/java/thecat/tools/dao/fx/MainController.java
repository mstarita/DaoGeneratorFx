package thecat.tools.dao.fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import thecat.tools.dao.fx.screens.ControlledScreen;
import thecat.tools.dao.fx.screens.ScreenEnum;
import thecat.tools.dao.fx.screens.ScreensController;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * Created by thecat on 09/10/16.
 */
public class MainController implements Initializable, ControlledScreen {

    private static String[] ACCEPT_DRAGGED_WITH_FILE_EXTENSION = { "class" };

    private ScreensController screenController;

    @FXML
    private Pane mainPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainPane.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                // Collect the files and check the extensions
                List<File> files = event.getDragboard().getFiles();
                for (File file: files) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
            }
        });

        mainPane.setOnDragDropped( event -> {
            if (event.getDragboard().hasFiles()) {
                List<File> files = event.getDragboard().getFiles();

                for (File file : files) {
                    String ext = getExtension(file.getName());
                    Predicate<String> currentExtPredicate = it -> it.equals(ext);

                    if (Arrays.asList(ACCEPT_DRAGGED_WITH_FILE_EXTENSION).stream().anyMatch(currentExtPredicate)) {
                        System.out.println("the extension matches... go ahead with file " + file.getName());
                        screenController.setScreen(ScreenEnum.DAO_OPTIONS.getId(), file.getAbsoluteFile().toString());
                    } else {
                        System.out.println("the extension " + ext + " is not valid!!!");
                    }
                }
            }
        });
    }

    private String getExtension(String fileName){
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }

    @Override
    public void setScreenParent(ScreensController screenParent, Object params) {
        screenController = screenParent;
    }
}
