package org.zpd.somarker.functions.data;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.zpd.foundation.utils.FileUtils;
import org.zpd.jfxcommon.model.JFXFunctionBase;
import org.zpd.somarker.functions.marker.service.FluorescentMarkerFinderService;
import org.zpd.somarker.functions.marker.service.PhenotypicMarkerFinderService;
import org.zpd.somarker.functions.marker.service.WormGeneService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class DataTool extends JFXFunctionBase {

    private final WormGeneService wormGeneService = new WormGeneService();
    private final FluorescentMarkerFinderService fluorescentMarkerFinderService = new FluorescentMarkerFinderService();

    private final PhenotypicMarkerFinderService phenotypicMarkerFinderService = new PhenotypicMarkerFinderService();


    @Override
    public String getFunctionName() {
        return "Import/Export Tool";
    }

    @Override
    public Node getPanel(Stage stage) {
        // 创建进度条

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(8);
        gridPane.setVgap(10);

        // 创建Label和Button
        Label wormGeneImportLabel = new Label("Import Worm Gene Data:");
        Button wormGeneImportButton = new Button("Choose XLSX File");
        wormGeneImportButton.setOnAction(event -> wormGeneImport(stage));

        Label wormGeneExportLabel = new Label("Export Worm Gene Data:");
        Button wormGeneExportButton = new Button("Export XLSX Data");
        wormGeneExportButton.setOnAction(event -> wormGeneExport(stage));

        Label fluorescentImportLabel = new Label("Import Fluorescent Marker Data:");
        Button fluorescentImportButton = new Button("Choose XLSX File");
        fluorescentImportButton.setOnAction(event -> fluorescentImport(stage));

        Label fluorescentExportLabel = new Label("Export Fluorescent Marker Data:");
        Button fluorescentExportButton = new Button("Export XLSX Data");
        fluorescentExportButton.setOnAction(event -> fluorescentExport(stage));

        Label phenotypicImportLabel = new Label("Import Phenotypic Marker Data:");
        Button phenotypicImportButton = new Button("Choose XLSX File");
        phenotypicImportButton.setOnAction(event -> phenotypicImport(stage));

        Label phenotypicExportLabel = new Label("Export Phenotypic Marker Data:");
        Button phenotypicExportButton = new Button("Export XLSX Data");
        phenotypicExportButton.setOnAction(event -> phenotypicExport(stage));

        Label imageFolderLabel = new Label("Open Image Folder:");
        Button imageFolderOpenButton = new Button("Open");
        imageFolderOpenButton.setOnAction(event -> openFile(FileUtils.getINSTANCE().getIMAGE_PATH()));


        // 将控件添加到GridPane
        gridPane.add(wormGeneImportLabel, 0, 0);
        gridPane.add(wormGeneImportButton, 1, 0);
        gridPane.add(wormGeneExportLabel, 0, 1);
        gridPane.add(wormGeneExportButton, 1, 1);
        gridPane.add(fluorescentImportLabel, 0, 2);
        gridPane.add(fluorescentImportButton, 1, 2);
        gridPane.add(fluorescentExportLabel, 0, 3);
        gridPane.add(fluorescentExportButton, 1, 3);
        gridPane.add(phenotypicImportLabel, 0, 4);
        gridPane.add(phenotypicImportButton, 1, 4);
        gridPane.add(phenotypicExportLabel, 0, 5);
        gridPane.add(phenotypicExportButton, 1, 5);
        gridPane.add(imageFolderLabel, 0, 6);
        gridPane.add(imageFolderOpenButton, 1, 6);



        // 使用BorderPane来管理布局，使进度条始终在底部
        BorderPane root = new BorderPane();
        root.setTop(gridPane); // GridPane位于顶部
        return root;
    }

    public void wormGeneImport(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to import the data? Importing data will overwrite existing data.",
                new ButtonType[]{ButtonType.YES, ButtonType.NO});
        // 设置对话框的标题和头部文本
        alert.setTitle("Import Confirmation");
        alert.setHeaderText("Please confirm the import operation");
        // 显示对话框并获取用户选择
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx")
            );
            fileChooser.setTitle("Choose xlsx file");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    wormGeneService.bulkImportWormGenes(file);
                    showSuccessAlert("Import Completed", "Data import is complete!");
                } catch (RuntimeException e) {
                    showErrorAlert("Import Failed", e.getMessage());
                }
            }
        }
    }

    public void wormGeneExport(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx")
        );
        fileChooser.setInitialFileName("WormGene.xlsx");
        fileChooser.setTitle("Export File");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                wormGeneService.bulkExportWormGenes(file);
                showSuccessAlert("Export Completed", "Data export is complete! File saved to: " + file.getPath());
            } catch (RuntimeException e) {
                showErrorAlert("Export Failed", e.getMessage());
            }
        }
    }

    public void fluorescentImport(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to import the data? Importing data will overwrite existing data.",
                new ButtonType[]{ButtonType.YES, ButtonType.NO});
        // 设置对话框的标题和头部文本
        alert.setTitle("Import Confirmation");
        alert.setHeaderText("Please confirm the import operation");
        // 显示对话框并获取用户选择
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx")
            );
            fileChooser.setTitle("Choose xlsx file");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    fluorescentMarkerFinderService.bulkImportBalancers(file);
                    showSuccessAlert("Import Completed", "Data import is complete!");
                } catch (RuntimeException e) {
                    showErrorAlert("Import Failed", e.getMessage());
                }
            }
        }
    }

    public void fluorescentExport(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx")
        );
        fileChooser.setInitialFileName("FluorescentMarker.xlsx");
        fileChooser.setTitle("Export File");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                fluorescentMarkerFinderService.bulkExportBalancers(file);
                showSuccessAlert("Export Completed", "Data export is complete! File saved to: " + file.getPath());
            } catch (RuntimeException e) {
                showErrorAlert("Export Failed", e.getMessage());
            }
        }
    }

    public void phenotypicImport(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to import the data? Importing data will overwrite existing data.",
                new ButtonType[]{ButtonType.YES, ButtonType.NO});
        // 设置对话框的标题和头部文本
        alert.setTitle("Import Confirmation");
        alert.setHeaderText("Please confirm the import operation");
        // 显示对话框并获取用户选择
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx")
            );
            fileChooser.setTitle("Choose xlsx file");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    phenotypicMarkerFinderService.bulkImportBalancers(file);
                    showSuccessAlert("Import Completed", "Data import is complete!");
                } catch (RuntimeException e) {
                    showErrorAlert("Import Failed", e.getMessage());
                }
            }
        }

    }

    public void phenotypicExport(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx")
        );
        fileChooser.setInitialFileName("PhenotypicMarker.xlsx");
        fileChooser.setTitle("Export File");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                phenotypicMarkerFinderService.bulkExportBalancers(file);
                showSuccessAlert("Export Completed", "Data export is complete! File saved to: " + file.getPath());
            } catch (RuntimeException e) {
                showErrorAlert("Export Failed", e.getMessage());
            }
        }
    }

    private void openFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("文件不存在: " + filePath);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

}
