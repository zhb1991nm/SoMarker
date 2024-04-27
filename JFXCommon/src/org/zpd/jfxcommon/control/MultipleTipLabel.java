package org.zpd.jfxcommon.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.zpd.jfxcommon.skin.MultipleTipLabelSkin;

/**
 * Created by zhb on 16/10/5.
 */
public class MultipleTipLabel extends Control{

    public interface MultipleTipLabelHandler{
        String tipForText(String text);
    }

    private final StringProperty text;
    private ObjectProperty<MultipleTipLabelHandler> tipHandler;

    public MultipleTipLabel(String text){
        this.text = new SimpleStringProperty(this,text);
        this.setText(text);
    }

    protected Skin<?> createDefaultSkin() {
        return new MultipleTipLabelSkin(this);
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

    public final ObjectProperty<MultipleTipLabelHandler> tipHandlerProperty(){
        if (this.tipHandler == null){
            this.tipHandler = new SimpleObjectProperty<>(this,"tipHandler");
        }
        return this.tipHandler;
    }

    public MultipleTipLabelHandler getTipHandler() {
        return tipHandler == null ? null : tipHandler.get();
    }

    public void setTipHandler(MultipleTipLabelHandler tipHandler) {
        this.tipHandlerProperty().set(tipHandler);
    }
}
