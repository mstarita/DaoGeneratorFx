package thecat.tools.dao.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import javafx.util.Pair;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import sun.security.tools.keytool.Resources_sv;
import thecat.tools.dao.fx.screens.ControlledScreen;
import thecat.tools.dao.fx.screens.ScreenEnum;
import thecat.tools.dao.fx.screens.ScreensController;
import thecat.tools.dao.utils.DaoGeneratorLauncher;
import thecat.tools.dao.utils.ExecuteWithDelay;
import thecat.tools.dao.utils.PackageUtils;
import thecat.util.clazz.ClazzUtils;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by thecat on 09/10/16.
 */
public class OptionsController implements Initializable, ControlledScreen {

    private static final String BASE_OUTPUT_FOLDER = "output-";

    private ScreensController screenController;
    private String selectedFile;
    private String classPath;
    private String className;
    private String ormType = "ormlite";

    private ResourceBundle resources;

    @FXML
    private Label fqcn;
    @FXML
    private ComboBox<String> cbKeyField;
    @FXML
    private Label lblFieldCount;
    @FXML
    private TextField txtOutputPackage;
    @FXML
    private Button btnGenerate;
    @FXML
    private TextArea txtOutput;
    @FXML
    private CheckBox cbVerbose;
    @FXML
    private CheckBox cbGenerateDaoBaseClasses;
    @FXML
    private Button btnOpenOutputFolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    @Override
    public void setScreenParent(ScreensController screenController, Object params) {
        this.screenController = screenController;
        if (params != null) {
            this.selectedFile = (String) params;

            Pair<String, String> cpAndFqcn = ClazzUtils.calculatePackageAndCP(selectedFile);

            if (cpAndFqcn != null) {
                classPath = cpAndFqcn.getKey();
                className = cpAndFqcn.getValue();

                init();
            }
        }
    }

    private void init() {
        fqcn.setText(className);

        // setup field list
        try {
            List<Method> methodList = ClazzUtils.getClassMethods(className, false, new String[]{classPath});
            cbKeyField.getItems().clear();
            for (Method method: methodList) {
                String fieldName = StringUtils.uncapitalize(method.getName().substring(3));
                cbKeyField.getItems().addAll(fieldName);
            }
            lblFieldCount.setText("" + methodList.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // setup output package
        txtOutputPackage.setText((String) PackageUtils.calculateDaoPackage(className));
    }

    @FXML
    public void onGenerate(ActionEvent actionEvent) {

        if (cbKeyField.getValue() != null &&
                !txtOutputPackage.getText().isEmpty()) {
            List<String> params = new ArrayList<>();
            params.add("-fq-class-name=" + className);
            params.add("-key-field-name=" + cbKeyField.getValue());
            params.add("-output-package=" + txtOutputPackage.getText());
            params.add("-use-orm=ormlite");

            if (cbVerbose.isSelected()) {
                params.add("-verbose");
            }

            if (cbGenerateDaoBaseClasses.isSelected()) {
                params.add("-generate-dao-base-classes");
            }

            PrintStream originalStdOut = System.out;
            try {
                List<String> classPathList = new ArrayList<>();
                classPathList.add(classPath);

                OutputStream outputStream = new ByteArrayOutputStream();
                PrintStream capturedOutput = new PrintStream(outputStream);
                System.setOut(capturedOutput);

                DaoGeneratorLauncher.exec(classPathList, params);

                txtOutput.setText(outputStream.toString());
                txtOutput.positionCaret(Integer.MAX_VALUE);
            } catch (Exception e) {
                e.printStackTrace();
                txtOutput.setText(ExceptionUtils.getStackTrace(e));
            } finally {
                System.setOut(originalStdOut);
            }
        } else {
            showToolTip(resources.getString("msg.specify-key-and-output-package"), btnGenerate);
        }
    }

    private void showToolTip(String message, Control parent) {
        Tooltip tooltip = new Tooltip();
        tooltip.setText(message);
        tooltip.setStyle("-fx-background-color: #7f7f7f; -fx-font-size: 24px; -fx-Text-fill: #fbfbfb;");
        tooltip.show(parent.getScene().getWindow());

        ExecuteWithDelay executeWithDelay = new ExecuteWithDelay(
                Duration.seconds(2),
                event -> {
                    tooltip.hide();
                }
        );
        executeWithDelay.play();
    }

    @FXML
    public void onOpenOutputFolder(ActionEvent actionEvent) {

        System.out.println("opening folder " + BASE_OUTPUT_FOLDER + ormType);

        new Thread(() -> {
            Desktop desktop = Desktop.getDesktop();
            File dirToOpen = null;
            try {
                dirToOpen = new File(BASE_OUTPUT_FOLDER + ormType);
                desktop.open(dirToOpen);
            } catch (IllegalArgumentException iae) {
                System.out.println("File Not Found");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    public void onBack(ActionEvent actionEvent) {
        screenController.setScreen(ScreenEnum.MAIN.getId());
    }
}
