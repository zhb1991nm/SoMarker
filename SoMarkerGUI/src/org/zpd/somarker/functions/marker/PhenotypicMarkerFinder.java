package org.zpd.somarker.functions.marker;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.zpd.foundation.Tool;
import org.zpd.jfxcommon.control.MultipleTipLabel;
import org.zpd.jfxcommon.model.JFXFunctionBase;
import org.zpd.somarker.db.entity.AbbreviationEntity;
import org.zpd.somarker.db.entity.PhenotypicMarkerEntity;
import org.zpd.somarker.db.entity.WormGeneEntity;
import org.zpd.somarker.functions.marker.service.AbbreviationService;
import org.zpd.somarker.functions.marker.service.PhenotypicMarkerFinderService;
import org.zpd.somarker.functions.marker.service.WormGeneService;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by zhb on 16/10/2.
 */
public class PhenotypicMarkerFinder extends JFXFunctionBase {

    private ComboBox<String> chromosomeComboBox;
    private TextField geneticPositionTextField;
    private TextField geneNameTextField;
    private TextField rfTextField;
    private Button searchButton, emptyButton;
    private TableView tableView;
    private PopOver popOver;
    private Label label_col_ES, label_col_ME, label_col_NA;
    private Tooltip tip_col_ES, tip_col_ME, tip_col_NA;

    private Node contentNode;


    private List<WormGeneEntity> wormGenes;
    private WormGeneEntity selectedWormGene;

    private PhenotypicMarkerFinderService searchBalancersService = new PhenotypicMarkerFinderService();

    private ObservableList<PhenotypicMarkerEntity> data = FXCollections.observableArrayList();

    public PhenotypicMarkerFinder() {
        wormGenes = WormGeneService.allWormGenes;
    }


    @Override
    public String getFunctionName() {
        return "Phenotypic Marker";
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
                rfTextField.setText("");
                data.clear();
            });
            Label rfLabel = new Label("Recombination frequency:");
            Label rfUnitLabel = new Label("%");
            rfTextField = new TextField();
            rfTextField.setDisable(true);
            rfTextField.setMaxWidth(80);
            HBox line1 = new HBox(geneNameLabel, geneNameTextField, chromosomeLabel, chromosomeComboBox, geneticPosition, geneticPositionTextField, searchButton, emptyButton);
            HBox line2 = new HBox(rfLabel, rfTextField, rfUnitLabel);
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
//                            rfTextField.setText("");
//                        });
//
//                    }
                    Platform.runLater(() -> {
                        chromosomeComboBox.setDisable(false);
                        chromosomeComboBox.getSelectionModel().clearSelection();
                        geneticPositionTextField.setDisable(false);
                        geneticPositionTextField.setText("");
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
                        Platform.runLater(() -> {
                            searchButton.fire();
                        });
                    } else {
                        chromosomeComboBox.setDisable(false);
                        chromosomeComboBox.getSelectionModel().clearSelection();
                        geneticPositionTextField.setDisable(false);
                        geneticPositionTextField.setText("");
                    }
                    rfTextField.setText("");
                }
            });

            tip_col_ES = new Tooltip("ES (Ease of Scoring):" +
                    "\n" +
                    "This indicates (very approximately) how easy it is to recognize a particular mutant phenotype. " +
                    "ES3 = easy to score; ES2 = hard to score (may become easier with practice); ES1 = very hard " +
                    "to score except by special means (such as enzyme assay or cell lineage analysis); ES0 = " +
                    "impossible to score, which may be the case for particular stages, sexes, or genetic background. " +
                    "In general, the ES score refers to ease of scoring at the stage when the mutation is maximally " +
                    "expressed. ES scores have only been included where relevant and known, and omitted for " +
                    "genes such as let genes.");
            label_col_ES = columnLabel("ES", tip_col_ES);

            tip_col_ME = new Tooltip("ME (Male mating Efficiency)" +
                    "\n" +
                    "This is recorded, where known, by an ME score, where ME0 = no successful mating, ME1 = " +
                    "rare successful mating, ME2 = poor mating, ME3 = fair-to-excellent mating.");
            label_col_ME = columnLabel("ME", tip_col_ME);

            tip_col_NA = new Tooltip("NA (Number of Alleles)" +
                    "\n" +
                    "The number of total alleles is listed by an NA score, followed by a list of some or all of these " +
                    "alleles, together with brief phenotypic descriptions for alleles with properties significantly " +
                    "different from that of the reference allele. Most mutations result in partial or complete loss of " +
                    "function and are recessive. Unusual alleles may exhibit gain-of-function properties, or " +
                    "dominant negative effects, and therefore lead to phenotypes very different from that of the " +
                    "reference allele. If only one allele is known, this is often indicated by NA1 (Number of " +
                    "Alleles 1).");
            label_col_NA = columnLabel("NA", tip_col_NA);


            TableView<PhenotypicMarkerEntity> tableView = new TableView<>();
            TableColumn<PhenotypicMarkerEntity, Void> col0 = new TableColumn<>("Index");
            col0.setPrefWidth(40);
            col0.setMinWidth(40);
            col0.setSortable(false);
            TableColumn<PhenotypicMarkerEntity, String> col1 = new TableColumn<>("Strain Name");
            col1.setSortable(false);
            col1.setMinWidth(150);
            col1.setSortable(false);
            TableColumn<PhenotypicMarkerEntity, String> col2 = new TableColumn<>("Chr");
            col2.setPrefWidth(40);
            col2.setMinWidth(40);
            col2.setSortable(false);
            TableColumn<PhenotypicMarkerEntity, String> col3 = new TableColumn<>("Phenotypic Marker");
            col3.setSortable(false);
            col3.setCellFactory(param -> new RTLabelCell());
            col3.setMinWidth(150);
//        col2.setCellFactory(param -> new HLabelTableCell(new HLabelTableCellHandler() {
//            @Override
//            public void onMouseEntered(Hyperlink hyperlink) {
//                GeneticMarkerFinder.this.showPopOver(hyperlink);
//            }
//
//            @Override
//            public void onMouseExited() {
//                GeneticMarkerFinder.this.hidePopOver();
//            }
//        }));


            TableColumn<PhenotypicMarkerEntity, String> col4 = new TableColumn<>("Genetic Position");
            col4.setSortable(false);
            col4.setMinWidth(150);

            TableColumn<PhenotypicMarkerEntity, String> col5 = new TableColumn<>("");
            col5.setGraphic(label_col_ES);
            col5.setSortable(false);
            col5.setMinWidth(50);
            col5.setCellFactory(param -> new RTLabelCell());

            TableColumn<PhenotypicMarkerEntity, String> col6 = new TableColumn<>("");
            col6.setGraphic(label_col_ME);
            col6.setSortable(false);
            col6.setMinWidth(50);
            col6.setCellFactory(param -> new RTLabelCell());

            TableColumn<PhenotypicMarkerEntity, String> col7 = new TableColumn<>("");
            col7.setGraphic(label_col_NA);
            col7.setSortable(false);
            col7.setMinWidth(50);
            col7.setCellFactory(param -> new RTLabelCell());


            TableColumn<PhenotypicMarkerEntity, String> col8 = new TableColumn<>("Information");
            col8.setSortable(false);
            col8.setCellFactory(param -> new PopOverTableCell());
            col8.setMinWidth(200);

            col0.setCellFactory(col -> new TableCell<PhenotypicMarkerEntity, Void>() {
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
            col1.setCellValueFactory(new PropertyValueFactory<PhenotypicMarkerEntity, String>("strainName"));
            col2.setCellValueFactory(new PropertyValueFactory<PhenotypicMarkerEntity, String>("chromosome"));
            col3.setCellValueFactory(new PropertyValueFactory<PhenotypicMarkerEntity, String>("geneticMarker"));
            col4.setCellValueFactory(new PropertyValueFactory<PhenotypicMarkerEntity, String>("geneticPosition"));
            col5.setCellValueFactory(new PropertyValueFactory<PhenotypicMarkerEntity, String>("ES"));
            col6.setCellValueFactory(new PropertyValueFactory<PhenotypicMarkerEntity, String>("ME"));
            col7.setCellValueFactory(new PropertyValueFactory<PhenotypicMarkerEntity, String>("NA"));
            col8.setCellValueFactory(new PropertyValueFactory<PhenotypicMarkerEntity, String>("info"));
            tableView.getColumns().addAll(col0, col1, col2, col3, col4, col5, col6, col7, col8);
            tableView.setItems(data);
            tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    if (newValue != null) {
                        if (selectedWormGene != null) {
                            double rf = Math.abs(selectedWormGene.getGeneticPosition() - ((PhenotypicMarkerEntity) newValue).getGeneticPosition());
                            DecimalFormat df = new DecimalFormat("######0.00");
                            rfTextField.setText(df.format(rf) + "");
                            return;
                        } else{
                            if (!geneticPositionTextField.getText().isEmpty()){
                                String inputGeneticPosition = geneticPositionTextField.getText();
                                double rf = Math.abs(Double.parseDouble(inputGeneticPosition) - ((PhenotypicMarkerEntity) newValue).getGeneticPosition());
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

        Map<String, String> param = new HashMap<>();
//        String chromosome = getSelectedChromesome();
        String chromosome = chromosomeComboBox.getSelectionModel().getSelectedItem();
        String position = geneticPositionTextField.getText();
        String name = Tool.instance().getString(geneNameTextField.getText());
        param.put("name", name);
        param.put("position", position);
        param.put("chromosome", chromosome);
        List<PhenotypicMarkerEntity> results = searchBalancersService.searchBalancers(param);
        if (results != null && results.size() > 0) {
            Platform.runLater(() -> {
                data.clear();
                data.addAll(results);
            });
        }
    }
//    private void showPopOver(Hyperlink hyperlink){
//        if (popOver == null){
//            popOver = new PopOver();
//            popOver.setHideOnEscape(true);
//        }
//        popOver.setContentNode(new Label(hyperlink.getText()));
//        popOver.show(hyperlink,-10);
//    }
//
//    private void hidePopOver(){
//        if (popOver != null && popOver.isShowing()){
//            popOver.hide(new Duration(0));
//        }
//    }

    private Label columnLabel(String title, Tooltip tooltip) {
        Label label = new Label(title);
        label.setTextFill(Color.SKYBLUE);
//        label.setUnderline(true);
//        Tooltip tooltip = new Tooltip(tip);
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(300);
//        label.setTooltip(tooltip);
        label.setCursor(Cursor.HAND);
//        label.setOnMouseEntered(event -> {
//            // do nothing
//        });
//        label.setOnMouseMoved(event -> {
//            // do nothing
//        });
//        label.setOnMouseExited(event -> {
//            // do nothing
//        });
        label.setOnMouseClicked(event -> {
            if (tooltip.isShowing()) {
                tooltip.hide();
            } else {
                Bounds bounds = label.localToScreen(label.getBoundsInLocal());
                double x = bounds.getMinX() + bounds.getWidth() / 2;
                double y = bounds.getMaxY() + 5; // 5 pixels be
                tooltip.show(label, x, y);
            }
        });
        label.setOnMouseExited(event -> {
            tooltip.hide();
        });
        return label;
    }

//    class TextAreaTableCell extends TableCell<GeneticMarkerEntity,String>{
//        final TextArea textArea = new TextArea();
//
//        @Override
//        protected void updateItem(String item, boolean empty) {
//            if (item != null){
//                textArea.setText(item);
//                textArea.setEditable(false);
//                textArea.setWrapText(true);
//            }
//            setGraphic(textArea);
//        }
//    }

    class PopOverTableCell extends TableCell<PhenotypicMarkerEntity, String> {
        final Label label = new Label();

        @Override
        protected void updateItem(String item, boolean empty) {
            if (item != null) {
                label.setText(item);
                label.setWrapText(true);
//                Tooltip tooltip = new Tooltip(item);
//                tooltip.setMaxWidth(200);
//                tooltip.setWrapText(true);
//                label.setTooltip(tooltip);
                label.setUnderline(true);
                label.setTextFill(Color.SKYBLUE);
                label.setCursor(Cursor.HAND);
                label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Label popOverLabel = new Label(item);
                        popOverLabel.setTextFill(Color.BLACK);
                        popOverLabel.setMaxWidth(200);
                        popOverLabel.setWrapText(true);
                        VBox vBox = new VBox(popOverLabel);
                        vBox.setPadding(new Insets(10));

                        if (popOver != null) {
                            if (popOver.isShowing()) {
                                if (popOver.getOwnerNode() == label) {
                                    return;
                                }
                            }
                        }
                        popOver = new PopOver();
                        popOver.setArrowSize(0);
                        popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);
                        popOver.setHeaderAlwaysVisible(true);
                        popOver.setHideOnEscape(true);
                        popOver.setContentNode(vBox);
                        popOver.show(label);
                    }
                });
            }else {
                label.setText(null);
                setText(null);
            }
            setGraphic(label);
        }
    }

    class RTLabelCell extends TableCell<PhenotypicMarkerEntity, String> {
        private final TextFlow textFlow = new TextFlow();

        private final MultipleTipLabel label = new MultipleTipLabel("");

        public RTLabelCell() {
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            if (item != null) {
                label.setTipHandler(new MultipleTipLabel.MultipleTipLabelHandler() {
                    @Override
                    public String tipForText(String text) {
                        AbbreviationEntity abbreviationEntity = AbbreviationService.findAbbreviation(text);
                        String tip = "";
                        if (abbreviationEntity != null) {
                            tip += abbreviationEntity.getAbbreviation();
                            tip += "(" + abbreviationEntity.getFullname() + ")";
                        } else {
                            tip += "Not found";
                        }
                        return tip;
                    }
                });
                label.setText(item);
            }else {
                label.setText(null);
                setText(null);
            }
            setGraphic(label);
//            textFlow.getChildren().clear();
//            if (item != null) {
//                String text = item;
//                if(text != null && !text.isEmpty()) {
//                    ArrayList nodes = new ArrayList();
//                    int start = 0;
//
//                    int endPos;
//                    for(int textLength = text.length(); start != -1 && start < textLength; start = endPos + 1) {
//                        int startPos = text.indexOf("[", start);
//                        endPos = text.indexOf("]", startPos);
//                        if((startPos == -1 || endPos == -1) && textLength > start) {
//                            Label label1 = new Label(text.substring(start));
//                            nodes.add(label1);
//                            break;
//                        }
//
//                        Text label = new Text(text.substring(start, startPos));
//                        nodes.add(label);
//
//                        String title = text.substring(startPos + 1, endPos);
//                        Label toolTipLabel = new Label(title);
//                        toolTipLabel.setTextFill(Color.SKYBLUE);
//                        toolTipLabel.setUnderline(true);
//                        toolTipLabel.setCursor(Cursor.HAND);
//                        AbbreviationEntity abbreviationEntity = AbbreviationService.findAbbreviation(title);
//                        if (abbreviationEntity != null){
//                            String tip = "";
//                            tip += abbreviationEntity.getAbbreviation();
//                            tip += "(" + abbreviationEntity.getFullname() + ")";
//                            toolTipLabel.setTooltip(new Tooltip(tip));
//                        }
//                        nodes.add(toolTipLabel);
//                    }
//
//                    this.textFlow.getChildren().setAll(nodes);
//                } else {
//                    this.textFlow.getChildren().clear();
//                }
//            }
//            setGraphic(textFlow);
        }
    }

//    public interface HLabelTableCellHandler{
//        void onMouseEntered(Hyperlink hyperlink);
//        void onMouseExited();
//    }
//
//    class HLabelTableCell extends TableCell<GeneticMarkerEntity,String>{
//        final MouseEventLabel label = new MouseEventLabel();
//        final HLabelTableCellHandler handler;
////        final HyperlinkLabel label = new HyperlinkLabel();
//
//        public HLabelTableCell(HLabelTableCellHandler handler){
//            this.handler = handler;
//        }
//
//        @Override
//        protected void updateItem(String item, boolean empty) {
//            if (item != null){
//                label.setText(item);
//                label.setOnMouseEnteredAction(new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
//                        System.out.println("in");
//                        Hyperlink link = (Hyperlink)event.getSource();
//                        handler.onMouseEntered(link);
//                    }
//                });
//
//                label.setOnMouseExitedAction(new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
//                        System.out.println("out");
//                        handler.onMouseExited();
//                    }
//                });
////                label.setOnAction(new EventHandler<ActionEvent>() {
////                    @Override
////                    public void handle(ActionEvent event) {
////                        Hyperlink link = (Hyperlink)event.getSource();
////                    }
////                });
//            }
//            setGraphic(label);
//        }
//    }

    public static void main(String[] args) {
        launch(args);
    }
}
