package org.zpd.jfxcommon.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import java.util.ArrayList;
import java.util.Collections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.zpd.jfxcommon.control.MouseEventLabel;

/**
 * Created by zhb on 16/10/4.
 */
public class MouseEventLabelSkin extends BehaviorSkinBase<MouseEventLabel,BehaviorBase<MouseEventLabel>> {
    private static final String HYPERLINK_START = "[";
    private static final String HYPERLINK_END = "]";
    private final TextFlow textFlow = new TextFlow();
    private final EventHandler<MouseEvent> enteredEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            EventHandler onMouseEnteredHandler = (MouseEventLabelSkin.this.getSkinnable()).getOnMouseEnteredAction();
            if (onMouseEnteredHandler != null){
                onMouseEnteredHandler.handle(event);
            }
        }
    };

    private final EventHandler<MouseEvent> exitedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            EventHandler onMouseExitedHandler = (MouseEventLabelSkin.this.getSkinnable()).getOnOnMouseExitedAction();
            if (onMouseExitedHandler != null){
                onMouseExitedHandler.handle(event);
            }
        }
    };

    public MouseEventLabelSkin(MouseEventLabel control) {
        super(control, new BehaviorBase(control, Collections.emptyList()));
        this.getChildren().add(this.textFlow);
        this.updateText();
        this.registerChangeListener(control.textProperty(), "TEXT");
    }

    protected void handleControlPropertyChanged(String p) {
        super.handleControlPropertyChanged(p);
        if(p == "TEXT") {
            this.updateText();
        }

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
