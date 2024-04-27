package org.zpd.jfxcommon.control;

import com.sun.javafx.event.EventHandlerManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;
import org.zpd.jfxcommon.skin.MouseEventLabelSkin;

/**
 * Created by zhb on 16/10/4.
 */
public class MouseEventLabel extends Control implements EventTarget {
    private final EventHandlerManager eventHandlerManager;
    private final StringProperty text;
    private ObjectProperty<EventHandler<MouseEvent>> onMouseEnteredAction;
    private ObjectProperty<EventHandler<MouseEvent>> onMouseExitedAction;
    public MouseEventLabel() {
        this(null);
    }

    public MouseEventLabel(String text){
        this.eventHandlerManager = new EventHandlerManager(this);
        this.text = new SimpleStringProperty(this,text);
        this.setText(text);
    }

    protected Skin<?> createDefaultSkin() {
        return new MouseEventLabelSkin(this);
    }

    public final StringProperty textProperty() {
        return this.text;
    }

    public final String getText() {
        return this.text.get();
    }

    public final void setText(String value) {
        this.text.set(value);
    }

    public final ObjectProperty<EventHandler<MouseEvent>> onMouseEnteredActionProperty() {
        if(this.onMouseEnteredAction == null) {
            this.onMouseEnteredAction = new SimpleObjectProperty(this, "onMouseEnteredAction") {
                protected void invalidated() {
                    MouseEventLabel.this.eventHandlerManager.setEventHandler(MouseEvent.MOUSE_ENTERED, (EventHandler)this.get());
                }
            };
        }

        return this.onMouseEnteredAction;
    }

    public final ObjectProperty<EventHandler<MouseEvent>> onMouseExitedActionProperty() {
        if(this.onMouseExitedAction == null) {
            this.onMouseExitedAction = new SimpleObjectProperty(this, "onMouseExitedAction") {
                protected void invalidated() {
                    MouseEventLabel.this.eventHandlerManager.setEventHandler(MouseEvent.MOUSE_EXITED, (EventHandler)this.get());
                }
            };
        }

        return this.onMouseExitedAction;
    }

    public final void setOnMouseEnteredAction(EventHandler<MouseEvent> value) {
        this.onMouseEnteredActionProperty().set(value);
    }

    public final EventHandler<MouseEvent> getOnMouseEnteredAction() {
        return this.onMouseEnteredAction == null?null:(EventHandler)this.onMouseEnteredAction.get();
    }

    public final void setOnMouseExitedAction(EventHandler<MouseEvent> value) {
        this.onMouseExitedActionProperty().set(value);
    }

    public final EventHandler<MouseEvent> getOnOnMouseExitedAction() {
        return this.onMouseExitedAction == null?null:(EventHandler)this.onMouseExitedAction.get();
    }


}
