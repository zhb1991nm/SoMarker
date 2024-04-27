package org.zpd.jfxcommon.model;

import javafx.scene.Node;

/**
 * Created by zhb on 16/10/2.
 */
public class WelcomePage {
    private String title;
    private Node content;

    public WelcomePage(String title, Node content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Node getContent() {
        return content;
    }

    public void setContent(Node content) {
        this.content = content;
    }
}
