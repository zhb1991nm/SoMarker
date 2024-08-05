package org.zpd.jfxcommon.skin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.zpd.jfxcommon.control.MouseEventLabel;

import java.util.ArrayList;

/**
 * Created by zhb on 16/10/4.
 */
public class MouseEventLabelSkin extends SkinBase<MouseEventLabel> {
    private static final String HYPERLINK_START = "[";
    private static final String HYPERLINK_END = "]";
    private final TextFlow textFlow = new TextFlow();
    private final EventHandler<MouseEvent> enteredEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            EventHandler<MouseEvent> onMouseEnteredHandler = (MouseEventLabelSkin.this.getSkinnable()).getOnMouseEnteredAction();
            if (onMouseEnteredHandler != null){
                onMouseEnteredHandler.handle(event);
            }
        }
    };

    private final EventHandler<MouseEvent> exitedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            EventHandler<MouseEvent> onMouseExitedHandler = (MouseEventLabelSkin.this.getSkinnable()).getOnOnMouseExitedAction();
            if (onMouseExitedHandler != null){
                onMouseExitedHandler.handle(event);
            }
        }
    };

    public MouseEventLabelSkin(MouseEventLabel control) {
        super(control);
        getChildren().add(textFlow);
        updateText();
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
            ArrayList<Node> nodes = new ArrayList<>();
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


                Hyperlink hyperlink = new Hyperlink(text.substring(startPos + 1, endPos));
                hyperlink.setPadding(new Insets(0.0D, 0.0D, 0.0D, 0.0D));
                hyperlink.setOnMouseEntered(this.enteredEventHandler);
                hyperlink.setOnMouseExited(this.exitedEventHandler);
                nodes.add(hyperlink);
            }

            this.textFlow.getChildren().setAll(nodes);
        } else {
            this.textFlow.getChildren().clear();
        }
    }
}
