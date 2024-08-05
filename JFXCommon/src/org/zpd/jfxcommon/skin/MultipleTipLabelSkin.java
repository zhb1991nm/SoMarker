package org.zpd.jfxcommon.skin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.zpd.jfxcommon.control.MultipleTipLabel;

import java.util.ArrayList;

/**
 * Created by zhb on 16/10/5.
 */
public class MultipleTipLabelSkin extends SkinBase<MultipleTipLabel> {

    private static final String HYPERLINK_START = "[";
    private static final String HYPERLINK_END = "]";
    private final TextFlow textFlow = new TextFlow();


    public MultipleTipLabelSkin(MultipleTipLabel control) {
        super(control);
        this.getChildren().add(this.textFlow);
        this.updateText();
        control.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateText();
            }
        });
    }


    private void updateText() {
        String text = (this.getSkinnable()).getText();
        if(text != null && !text.isEmpty()) {
            ArrayList nodes = new ArrayList();
            int start = 0;

            int endPos;
            for(int textLength = text.length(); start != -1 && start < textLength; start = endPos + 1) {
                int startPos = text.indexOf("[", start);
                endPos = text.indexOf("]", startPos);
                if((startPos == -1 || endPos == -1) && textLength > start) {
                    Label label1 = new Label(text.substring(start));
                    nodes.add(label1);
                    break;
                }

                Text label = new Text(text.substring(start, startPos));
                nodes.add(label);


                Label tipLabel = new Label(text.substring(startPos + 1, endPos));
//                tipLabel.setUnderline(true);
                tipLabel.setCursor(Cursor.HAND);
                tipLabel.setTextFill(Color.SKYBLUE);
                tipLabel.setPadding(new Insets(0.0D, 0.0D, 0.0D, 0.0D));
                tipLabel.setTooltip(new Tooltip(MultipleTipLabelSkin.this.getSkinnable().getTipHandler().tipForText(tipLabel.getText())));
                nodes.add(tipLabel);
            }

            this.textFlow.getChildren().setAll(nodes);
        } else {
            this.textFlow.getChildren().clear();
        }
    }

}
