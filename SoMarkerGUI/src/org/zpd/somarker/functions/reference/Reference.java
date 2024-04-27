package org.zpd.somarker.functions.reference;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.zpd.jfxcommon.model.JFXFunctionBase;


/**
 * Created by zhb on 16/10/2.
 */
public class Reference extends JFXFunctionBase{


    @Override
    public String getFunctionName() {
        return "Reference";
    }

    @Override
    public Node getPanel(Stage stage) {
        return new StackPane(new Label("Reference: \n" +
                "\n" +
                "\n" +
                "\n" +
                "1. Riddle DL, Blumenthal T, Meyer BJ, et al.,\n" +
                "editors. C. elegans II. 2nd edition. Cold Spring Harbor (NY): Cold Spring \n" +
                "Harbor Laboratory Press; 1997. Part D, List of Characterized Genes. Available \n" +
                "from: https://www.ncbi.nlm.nih.gov/books/NBK20064/\n" +
                "2. Frøkjær-Jensen, Christian, et al. \"Random and targeted transgene insertion \n" +
                "in Caenorhabditis elegans using a modified Mos1 transposon.\" \n" +
                "Nature methods 11.5 (2014): 529-534."));
    }
    public static void main(String[] args) {
        launch(args);
    }

}
