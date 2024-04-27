package org.zpd.jfxcommon.model;

import javafx.scene.Node;
import javafx.stage.Stage;
import org.zpd.jfxcommon.JFXFunction;


/**
 * Created by zhb on 16/10/2.
 */
public class EmptyFunction implements JFXFunction {

    private final String name;
    public EmptyFunction(String name){
        this.name = name;
    }

    @Override
    public String getFunctionName() {
        return this.name;
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
}
