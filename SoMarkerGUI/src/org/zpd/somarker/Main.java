package org.zpd.somarker;

import javafx.application.Application;
import javafx.beans.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.zpd.somarker.db.ORMDatabaseHelper;
import org.zpd.somarker.functions.reference.Reference;
import org.zpd.somarker.functions.marker.FluorescentMarkerFinder;
import org.zpd.somarker.functions.marker.PhenotypicMarkerFinder;
import org.zpd.jfxcommon.JFXFunction;
import org.zpd.jfxcommon.model.EmptyFunction;
import org.zpd.jfxcommon.model.FuctionTree;
import org.zpd.jfxcommon.model.Project;

import java.util.*;

/**
 * Created by zhb on 16/10/2.
 */
public class Main extends Application{
    private static String version = "v0.1.0";
    private Map<String, Project> projectsMap;
    private Stage stage;
    private GridPane grid;
    private JFXFunction selectedFunction;
    private TreeView<JFXFunction> functionsTreeView;
    private TreeItem<JFXFunction> root;
    private StackPane contentPane;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        ORMDatabaseHelper.sharedInstance().startSession();

        this.projectsMap = this.discoverFunctions();
        this.stage = primaryStage;
        this.buildFunctionTree(null);
        this.grid = new GridPane();
        this.grid.setPadding(new Insets(5.0D, 10.0D, 10.0D, 10.0D));
        this.grid.setHgap(10.0D);
        this.grid.setVgap(10.0D);
        final TextField searchBox = new TextField();
        searchBox.setPromptText("Search");
        searchBox.getStyleClass().add("search-box");
        searchBox.textProperty().addListener(new InvalidationListener() {
            public void invalidated(javafx.beans.Observable o) {
                Main.this.buildFunctionTree(searchBox.getText());
            }
        });
        GridPane.setMargin(searchBox, new Insets(5.0D, 0.0D, 0.0D, 0.0D));
        this.grid.add(searchBox, 0, 0);
        this.functionsTreeView = new TreeView<>(this.root);
        this.functionsTreeView.setShowRoot(false);
        this.functionsTreeView.getStyleClass().add("samples-tree");
        this.functionsTreeView.setMinWidth(200.0D);
        this.functionsTreeView.setMaxWidth(200.0D);
        this.functionsTreeView.setCellFactory(param ->
            new TreeCell<JFXFunction>(){
                @Override
                protected void updateItem(JFXFunction item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        this.setText("");
                    } else {
                        this.setText(item.getFunctionName());
                    }
                }
            }
        );
        this.functionsTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<JFXFunction>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<JFXFunction>> observable, TreeItem<JFXFunction> oldValue, TreeItem<JFXFunction> newValue) {
                if (newValue != null){
                    if (newValue.getValue() instanceof EmptyFunction) {
//                        JFXFunction selectedFunction = newValue.getValue();

                    }else {
                        Main.this.selectedFunction = newValue.getValue();
                        Main.this.changeFunction();
                    }
                }
            }
        });
        GridPane.setVgrow(this.functionsTreeView, Priority.ALWAYS);
        this.grid.add(this.functionsTreeView,0,1);

        this.contentPane = new StackPane();
        GridPane.setHgrow(this.contentPane,Priority.ALWAYS);
        GridPane.setVgrow(this.contentPane,Priority.ALWAYS);
        this.grid.add(this.contentPane,1,0,1,2);


//        ObservableList projects = this.functionsTreeView.getRoot().getChildren();
//        if(!projects.isEmpty()) {
//            TreeItem scene = (TreeItem)projects.get(0);
//            this.functionsTreeView.getSelectionModel().select(scene);
//        }

        Scene scene = new Scene(new StackPane(this.grid));
        scene.getStylesheets().add("styles/styles.css");
//        scene1.getStylesheets().add(this.getClass().getResource("fxsampler.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1200.0D);
        primaryStage.setMinHeight(600.0D);
        Rectangle2D screenBounds1 = Screen.getPrimary().getVisualBounds();
        primaryStage.setWidth(screenBounds1.getWidth() * 0.5D);
        primaryStage.setHeight(screenBounds1.getHeight() * 0.5D);
        primaryStage.setTitle(String.format("SoMarker (%s)", version));
        primaryStage.show();
        this.functionsTreeView.requestFocus();
    }

    protected void changeFunction() {
        if(this.selectedFunction != null) {
            this.contentPane.getChildren().removeAll(this.contentPane.getChildren());
            this.contentPane.getChildren().add(this.selectedFunction.getPanel(stage));
        }
    }

    private boolean pruneFunctionTree(TreeItem<JFXFunction> treeItem, String searchText) {
        if(searchText == null) {
            return true;
        } else if(treeItem.isLeaf()) {
            return (treeItem.getValue()).getFunctionName().toUpperCase().contains(searchText.toUpperCase());
        } else {
            ArrayList toRemove = new ArrayList();
            Iterator var4 = treeItem.getChildren().iterator();

            while(var4.hasNext()) {
                TreeItem child = (TreeItem)var4.next();
                boolean keep = this.pruneFunctionTree(child, searchText);
                if(!keep) {
                    toRemove.add(child);
                }
            }

            treeItem.getChildren().removeAll(toRemove);
            return !treeItem.getChildren().isEmpty();
        }
    }

    private void sort(TreeItem<JFXFunction> node, Comparator<TreeItem<JFXFunction>> comparator) {
        node.getChildren().sort(comparator);
        Iterator var3 = node.getChildren().iterator();

        while(var3.hasNext()) {
            TreeItem child = (TreeItem)var3.next();
            this.sort(child, comparator);
        }

    }

    protected Map<String,Project> discoverFunctions(){
        Map<String,Project> ret = new HashMap<>();
        Project project = new Project("Marker","org.zpd.dai.functions.marker");
        project.addFunction("org.zpd.dai.functions.marker",new PhenotypicMarkerFinder());
        project.addFunction("org.zpd.dai.functions.marker",new FluorescentMarkerFinder());

        Project project1 = new Project("Reference","org.zpd.dai.functions.reference");
        project1.addFunction("org.zpd.dai.functions.reference",new Reference());

        ret.put("Marker",project);
        ret.put("Reference",project1);
        return ret;
    }

    protected void buildFunctionTree(String searchText) {

        this.root = new TreeItem(new EmptyFunction("Functions"));
        this.root.setExpanded(true);
        Iterator var2 = this.projectsMap.keySet().iterator();

        while(var2.hasNext()) {
            String projectName = (String)var2.next();
            Project project = this.projectsMap.get(projectName);
            if(project != null) {
                FuctionTree.TreeNode n = project.getFuctionTree().getRoot();
                this.root.getChildren().add(n.createTreeItem());
            }
        }

        if(searchText != null) {
            this.pruneFunctionTree(this.root, searchText);
            this.functionsTreeView.setRoot(null);
            this.functionsTreeView.setRoot(this.root);
        }

        this.sort(this.root, (o1, o2) -> {

            String o1Name = o1.getValue().getFunctionName();
            String o2Name = o2.getValue().getFunctionName();
            if (o1Name.equals("Reference")){
                return 1;
            }else if (o2Name.equals("Reference")){
                return -1;
            }else {
                return (o1.getValue()).getFunctionName().compareTo((o2.getValue()).getFunctionName());
            }
        });

    }
}
