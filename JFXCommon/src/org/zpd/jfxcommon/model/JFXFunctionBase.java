package org.zpd.jfxcommon.model;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.zpd.jfxcommon.JFXFunction;

/**
 * Created by zhb on 16/10/2.
 */
public abstract class JFXFunctionBase extends Application implements JFXFunction {

    public JFXFunctionBase() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(this.getFunctionName());
        Scene scene = new Scene((Parent)buildFunction(this, primaryStage), 800.0D, 800.0D);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public String getFunctionName() {
        return "ZPD";
    }

    @Override
    public Node getPanel(Stage stage) {
        return null;
    }

    @Override
    public Node getControlPane() {
        return null;
    }

    @Override
    public String getControlStylesheetURL() {
        return null;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    public static Node buildFunction(JFXFunction function, Stage stage) {
        Node contentPanel = function.getPanel(stage);
        return contentPanel;
    }
}
