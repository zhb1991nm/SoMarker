package org.zpd.somarker.functions.marker;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.zpd.foundation.Tool;
import org.zpd.foundation.utils.FileUtils;
import org.zpd.jfxcommon.model.JFXFunctionBase;
import org.zpd.somarker.db.entity.FluorescentMarkerEntity;
import org.zpd.somarker.db.entity.WormGeneEntity;
import org.zpd.somarker.functions.marker.service.FluorescentMarkerFinderService;
import org.zpd.somarker.functions.marker.service.WormGeneService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by zhb on 16/10/2.
 */
public class FluorescentMarkerFinder extends JFXFunctionBase {

    private ComboBox<String> chromosomeComboBox;
    private TextField geneticPositionTextField;
    private TextField geneNameTextField;
    private Button searchButton, emptyButton, addButton;
    private TextField ppFromTextField, ppToTextField, rfTextField;
    private Node contentNode;

    private List<WormGeneEntity> wormGenes;
    private WormGeneEntity selectedWormGene;

    private FluorescentMarkerFinderService searchBalancersService = new FluorescentMarkerFinderService();

    private ObservableList<FluorescentMarkerEntity> data = FXCollections.observableArrayList();

    public FluorescentMarkerFinder() {
        wormGenes = WormGeneService.allWormGenes;
    }

    @Override
    public String getFunctionName() {
        return "Fluorescent Marker";
    }

    @Override
    public Node getPanel(Stage stage) {
        if (contentNode == null) {
            VBox searchBar = new VBox();
            searchBar.setPrefHeight(80);
            searchBar.setSpacing(0);
            geneNameTextField = new TextField();
            geneNameTextField.setMaxWidth(100);
            chromosomeComboBox = new ComboBox<>(FXCollections.observableArrayList("I", "II", "III", "IV", "V", "X"));
            geneticPositionTextField = new TextField();
            geneticPositionTextField.setMaxWidth(80);
            geneticPositionTextField.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> {
                String newText = change.getControlNewText();
                if (newText.equals("") || newText.equals("-")) {
                    return change;
                }
                try {
                    Float.parseFloat(newText.toString());
                    return change;
                } catch (Exception e) {
                    return null;
                }
            }));
            Label geneNameLabel = new Label("Gene");
            Label chromosomeLabel = new Label("Chromosome");
            Label geneticPosition = new Label("Genetic Position");
            searchButton = new Button("search");
            searchButton.setOnAction((e) -> searchBalancers());
            searchButton.setDefaultButton(true);
            emptyButton = new Button("empty");
            emptyButton.setOnAction((e) -> {
                selectedWormGene = null;
                geneNameTextField.setText("");
                chromosomeComboBox.setDisable(false);
//            chromosomeComboBox.getCheckModel().clearChecks();
                chromosomeComboBox.getSelectionModel().clearSelection();
                geneticPositionTextField.setDisable(false);
                geneticPositionTextField.setText("");
                ppFromTextField.setText("");
                ppToTextField.setText("");
                rfTextField.setText("");
                data.clear();
            });

//            addButton = new Button("add marker");
//            addButton.setOnAction(event -> showAddItemDialog(stage));

            Label ppFromLabel = new Label("Physical Position From:");
            Label ppToLabel = new Label("To:");
            ppFromTextField = new TextField();
            ppToTextField = new TextField();
            ppFromTextField.setMaxWidth(80);
            ppFromTextField.setDisable(true);
            ppToTextField.setMaxWidth(80);
            ppToTextField.setDisable(true);
            Label rfLabel = new Label("Recombination frequency:");
            Label rfUnitLabel = new Label("%");
            rfTextField = new TextField();
            rfTextField.setDisable(true);
            rfTextField.setMaxWidth(80);
            HBox line1 = new HBox(geneNameLabel, geneNameTextField, chromosomeLabel, chromosomeComboBox, geneticPosition, geneticPositionTextField, searchButton, emptyButton);
            HBox line2 = new HBox(ppFromLabel, ppFromTextField, ppToLabel, ppToTextField, rfLabel, rfTextField, rfUnitLabel);
            line1.setPrefHeight(40);
            line1.setSpacing(10);
            line1.setAlignment(Pos.CENTER_LEFT);
            line2.setPrefHeight(40);
            line2.setSpacing(10);
            line2.setAlignment(Pos.CENTER_LEFT);
            searchBar.getChildren().addAll(line1, line2);
            geneNameTextField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    chromosomeComboBox.setDisable(false);
//                chromosomeComboBox.getCheckModel().clearChecks();
                    chromosomeComboBox.getSelectionModel().clearSelection();
                    geneticPositionTextField.setDisable(false);
                    geneticPositionTextField.clear();
                }
            });
            AutoCompletionBinding<WormGeneEntity> acb = TextFields.bindAutoCompletion(geneNameTextField, new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<WormGeneEntity>>() {
                @Override
                public Collection<WormGeneEntity> call(AutoCompletionBinding.ISuggestionRequest param) {
                    selectedWormGene = null;
                    data.clear();
                    String userText = param.getUserText().trim().toLowerCase();
                    List<WormGeneEntity> results = new ArrayList<>();
                    if (!userText.isEmpty()) {
                        for (WormGeneEntity wormGene : wormGenes) {
                            if (wormGene.getGene().toLowerCase().contains(userText)) {
                                results.add(wormGene);
                            }
                        }
                    }
//                    if (!results.isEmpty()) {
//                        selectedWormGene = results.get(0);
//                        Platform.runLater(() -> {
//                            chromosomeComboBox.getSelectionModel().select(selectedWormGene.getChromosome());
//                            chromosomeComboBox.setDisable(true);
//                            geneticPositionTextField.setText(Tool.instance().getString(selectedWormGene.getGeneticPosition()));
//                            geneticPositionTextField.setDisable(true);
//                            ppFromTextField.setText(Tool.instance().getString(selectedWormGene.getPpFrom()));
//                            ppToTextField.setText(Tool.instance().getString(selectedWormGene.getPpTo()));
//                            rfTextField.setText("");
//                        });
//
//                    }
                    Platform.runLater(() -> {
                        chromosomeComboBox.setDisable(false);
                        chromosomeComboBox.getSelectionModel().clearSelection();
                        geneticPositionTextField.setDisable(false);
                        geneticPositionTextField.setText("");
                        ppFromTextField.setText("");
                        ppToTextField.setText("");
                    });
                    return results;
                }
            }, new StringConverter<WormGeneEntity>() {
                @Override
                public String toString(WormGeneEntity object) {
                    return object.getGene();
                }

                @Override
                public WormGeneEntity fromString(String string) {
                    return null;
                }
            });
            acb.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<WormGeneEntity>>() {
                @Override
                public void handle(AutoCompletionBinding.AutoCompletionEvent<WormGeneEntity> event) {
                    selectedWormGene = event.getCompletion();
                    if (selectedWormGene != null) {
                        chromosomeComboBox.getSelectionModel().select(selectedWormGene.getChromosome());
                        chromosomeComboBox.setDisable(true);
                        geneticPositionTextField.setText(Tool.instance().getString(selectedWormGene.getGeneticPosition()));
                        geneticPositionTextField.setDisable(true);
                        ppFromTextField.setText(Tool.instance().getString(selectedWormGene.getPpFrom()));
                        ppToTextField.setText(Tool.instance().getString(selectedWormGene.getPpTo()));
                        Platform.runLater(() -> {
                            searchButton.fire();
                        });
                    } else {
                        chromosomeComboBox.setDisable(false);
                        chromosomeComboBox.getSelectionModel().clearSelection();
                        geneticPositionTextField.setDisable(false);
                        geneticPositionTextField.setText("");
                        ppFromTextField.setText("");
                        ppToTextField.setText("");
                    }
                    rfTextField.setText("");
                }
            });


            TableView tableView = new TableView<>();
            TableColumn<FluorescentMarkerEntity, Void> col0 = new TableColumn("Index");
            col0.setSortable(false);
            col0.setPrefWidth(40);
            col0.setMinWidth(40);
            TableColumn<FluorescentMarkerEntity, String> col1 = new TableColumn("Strain Name");
            col1.setSortable(false);
            col1.setMinWidth(150);
            TableColumn<FluorescentMarkerEntity, String> col2 = new TableColumn("Chr");
            col2.setSortable(false);
            col2.setPrefWidth(40);
            col2.setMinWidth(40);
            TableColumn<FluorescentMarkerEntity, String> col3 = new TableColumn("Genetic Position");
            col3.setSortable(false);
            col3.setMinWidth(150);
            TableColumn<FluorescentMarkerEntity, String> col4 = new TableColumn("Fluorescent Marker");
            col4.setSortable(false);
            col4.setMinWidth(150);
            TableColumn<FluorescentMarkerEntity, String> col5 = new TableColumn("Insertion Marker");
            col5.setSortable(false);
            col5.setMinWidth(150);

            TableColumn<FluorescentMarkerEntity, String> col6 = new TableColumn<>("Image");
            col6.setSortable(false);
            col6.setCellFactory(param -> new ImageTableCell(new ImageTabelCellHandler() {
                @Override
                public void onClickImage(Image image, Bounds bounds) {
                    Parent parent = stage.getScene().getRoot();
                    FluorescentMarkerFinder.this.showImage(parent, image, bounds);
                }
            }));
            col6.setMinWidth(100);

            col0.setCellFactory(col -> new TableCell<FluorescentMarkerEntity, Void>() {
                @Override
                public void updateIndex(int index) {
                    super.updateIndex(index);
                    if (isEmpty() || index < 0) {
                        setText(null);
                    } else {
                        setText(Integer.toString(index + 1));
                    }
                }
            });
            col1.setCellValueFactory(new PropertyValueFactory<FluorescentMarkerEntity, String>("strainName"));
            col2.setCellValueFactory(new PropertyValueFactory<FluorescentMarkerEntity, String>("chromosome"));
            col3.setCellValueFactory(new PropertyValueFactory<FluorescentMarkerEntity, String>("geneticPosition"));
            col4.setCellValueFactory(new PropertyValueFactory<FluorescentMarkerEntity, String>("fluorescentMarker"));
            col5.setCellValueFactory(new PropertyValueFactory<FluorescentMarkerEntity, String>("insertionMarker"));
            col6.setCellValueFactory(new PropertyValueFactory<FluorescentMarkerEntity, String>("image"));
            tableView.getColumns().addAll(col0, col1, col2, col3, col4, col5, col6);
            tableView.setItems(data);
            tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    if (newValue != null) {
                        if (selectedWormGene != null) {
                            double rf = Math.abs(selectedWormGene.getGeneticPosition() - ((FluorescentMarkerEntity) newValue).getGeneticPosition());
                            DecimalFormat df = new DecimalFormat("######0.00");
                            rfTextField.setText(df.format(rf) + "");
                            return;
                        } else {
                            if (!geneticPositionTextField.getText().isEmpty()) {
                                String inputGeneticPosition = geneticPositionTextField.getText();
                                double rf = Math.abs(Double.parseDouble(inputGeneticPosition) - ((FluorescentMarkerEntity) newValue).getGeneticPosition());
                                DecimalFormat df = new DecimalFormat("######0.00");
                                rfTextField.setText(df.format(rf) + "");
                                return;
                            }
                        }
                    }
                    rfTextField.setText("");
                }
            });
            AnchorPane anchorPane = new AnchorPane(searchBar, tableView);
            AnchorPane.setTopAnchor(searchBar, 0D);
            AnchorPane.setLeftAnchor(searchBar, 0D);
            AnchorPane.setRightAnchor(searchBar, 0D);

            AnchorPane.setTopAnchor(tableView, 80D);
            AnchorPane.setLeftAnchor(tableView, 0D);
            AnchorPane.setBottomAnchor(tableView, 0D);
            AnchorPane.setRightAnchor(tableView, 0D);

            HBox hBox = new HBox();
            hBox.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.5), null, null)));

            StackPane stackPane = new StackPane(anchorPane, hBox);
            hBox.setVisible(false);
            contentNode = stackPane;
        }
        return contentNode;
    }

    private void searchBalancers() {
        Platform.runLater(() -> {
            data.clear();
        });
        Map<String, String> param = new HashMap<>();
        String chromosome = chromosomeComboBox.getSelectionModel().getSelectedItem();
        String position = geneticPositionTextField.getText();
        String name = Tool.instance().getString(geneNameTextField.getText());
        param.put("name", name);
        param.put("position", position);
        param.put("chromosome", chromosome);
        List<FluorescentMarkerEntity> results = searchBalancersService.searchBalancers(param);
        if (results != null && results.size() > 0) {
            Platform.runLater(() -> {
                data.addAll(results);
            });

        }

    }

    private void showImage(Parent parent, Image image, Bounds bounds) {
        if (parent instanceof StackPane) {
            StackPane stackPane = (StackPane) parent;
            StackPane imagePane = new StackPane();
            imagePane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.5), null, null)));
            ImageView imageView = new ImageView();
            imageView.setFitWidth(image.getWidth());
            imageView.setFitHeight(image.getHeight());
            imagePane.getChildren().add(imageView);
            imagePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stackPane.getChildren().remove(imagePane);
                }
            });
            imagePane.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode().equals(KeyCode.ESCAPE)) {
                        stackPane.getChildren().remove(imagePane);
                    }
                }
            });
            imageView.setImage(image);
            stackPane.getChildren().add(imagePane);
//            stackPane.widthProperty().ad
        }
    }

    public interface ImageTabelCellHandler {
        void onClickImage(Image image, Bounds bounds);
    }

    class ImageTableCell extends TableCell<FluorescentMarkerEntity, String> {
        final ImageView imageView = new ImageView();

        final ImageTabelCellHandler handler;

        public ImageTableCell(ImageTabelCellHandler handler) {
            this.handler = handler;
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            if (item != null) {
//                Image image = new Image(Main.class.getResourceAsStream("/image/" + item));
                Image image = null;
                try {
                    image = new Image(new FileInputStream(FileUtils.getINSTANCE().getIMAGE_PATH() + item));
                    imageView.setImage(image);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(image.getHeight() * 100 / image.getWidth());
                    imageView.setOnMouseReleased(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            handler.onClickImage(imageView.getImage(), imageView.localToScene(imageView.getBoundsInLocal()));
//                        System.out.println(imageView.localToScene(imageView.getBoundsInLocal()));
                        }
                    });
                } catch (FileNotFoundException e) {
                    System.err.println("获取图片发生错误: " + e.getMessage());
                }

            } else {
                imageView.setImage(null);
            }
            setGraphic(new VBox(imageView));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
