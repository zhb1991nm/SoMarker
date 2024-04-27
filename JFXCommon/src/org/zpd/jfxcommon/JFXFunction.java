package org.zpd.jfxcommon;

import javafx.scene.Node;
import javafx.stage.Stage;


/**
 * Created by zhb on 16/10/2.
 */
public interface JFXFunction {

    String getFunctionName();

    Node getPanel(Stage stage);

    Node getControlPane();

    String getControlStylesheetURL();

    boolean isVisible();

}
