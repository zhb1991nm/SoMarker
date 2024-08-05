package org.zpd.somarker.db;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.orman.dbms.Database;
import org.orman.dbms.sqlite.SQLite;
import org.orman.mapper.MappingSession;
import org.zpd.foundation.utils.FileUtils;
import org.zpd.foundation.utils.SystemUtils;
import org.zpd.somarker.db.entity.AbbreviationEntity;
import org.zpd.somarker.db.entity.FluorescentMarkerEntity;
import org.zpd.somarker.db.entity.PhenotypicMarkerEntity;
import org.zpd.somarker.db.entity.WormGeneEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Created by zhb on 16/6/18.
 */
public class ORMDatabaseHelper {

    private static ORMDatabaseHelper ormDatabaseHelper = null;

    public static ORMDatabaseHelper sharedInstance(){
        if (ormDatabaseHelper == null){
            ormDatabaseHelper = new ORMDatabaseHelper();
        }
        return ormDatabaseHelper;
    }

    private ORMDatabaseHelper(){

    }

    public void startSession(){
        loadDynamicLinkLibrary(FileUtils.getINSTANCE().getAPP_PATH());
        Database db = new SQLite(FileUtils.getINSTANCE().getDB_PATH());
        MappingSession.registerDatabase(db);
        MappingSession.registerEntity(FluorescentMarkerEntity.class);
        MappingSession.registerEntity(PhenotypicMarkerEntity.class);
        MappingSession.registerEntity(WormGeneEntity.class);
        MappingSession.registerEntity(AbbreviationEntity.class);
        MappingSession.start();
    }


    private void loadDynamicLinkLibrary(String appPath){
        File dbFile = new File(FileUtils.getINSTANCE().getDB_PATH());
        if (!dbFile.exists()){
            FileUtils.copyFile(Objects.requireNonNull(getClass().getResourceAsStream("/db/SoMarker.db")),dbFile);
        }
        Path imagePath = Paths.get(FileUtils.getINSTANCE().getIMAGE_PATH());
        if (!Files.exists(imagePath)){
            try {
                Files.createDirectories(imagePath);
                File targetFile;
                targetFile = new File(imagePath.resolve("Peft-GFP.jpg").toUri());
                FileUtils.copyFile(Objects.requireNonNull(getClass().getResourceAsStream("/image/Peft-GFP.jpg")), targetFile);
                targetFile = new File(imagePath.resolve("Peft-GFP-H2B.jpg").toUri());
                FileUtils.copyFile(Objects.requireNonNull(getClass().getResourceAsStream("/image/Peft-GFP-H2B.jpg")), targetFile);
                targetFile = new File(imagePath.resolve("Peft-GFP-H2B hsp-peel-1 NeoR.jpg").toUri());
                FileUtils.copyFile(Objects.requireNonNull(getClass().getResourceAsStream("/image/Peft-GFP-H2B hsp-peel-1 NeoR.jpg")), targetFile);
                targetFile = new File(imagePath.resolve("Peft-mCherry.jpg").toUri());
                FileUtils.copyFile(Objects.requireNonNull(getClass().getResourceAsStream("/image/Peft-mCherry.jpg")), targetFile);
                targetFile = new File(imagePath.resolve("Peft-tdTomato-H2B.jpg").toUri());
                FileUtils.copyFile(Objects.requireNonNull(getClass().getResourceAsStream("/image/Peft-tdTomato-H2B.jpg")), targetFile);
                targetFile = new File(imagePath.resolve("Peft-tdTomato-H2B gaIs283.jpg").toUri());
                FileUtils.copyFile(Objects.requireNonNull(getClass().getResourceAsStream("/image/Peft-tdTomato-H2B gaIs283.jpg")), targetFile);
            } catch (IOException e){
                System.err.println("创建目录时发生错误: " + e.getMessage());
            }

        }


//        FileUtils.copyFile(getClass().getResourceAsStream("/db/SoMarker.db"),dbFile);
        try {
            if (Objects.equals(SystemUtils.currentSystem(), SystemUtils.Windows)){
                File targetFile = new File(appPath + "sqlite4java-win32-x64.dll");
                if (!targetFile.exists()){
                    FileUtils.copyFile(Objects.requireNonNull(getClass().getResourceAsStream("/sqlite4java-win32-x64.dll")),targetFile);
                }
                targetFile = new File(appPath + "sqlite4java-win32-x86.dll");
                if (!targetFile.exists()){
                    FileUtils.copyFile(Objects.requireNonNull(getClass().getResourceAsStream("/sqlite4java-win32-x86.dll")),targetFile);
                }
            }else if (Objects.equals(SystemUtils.currentSystem(), SystemUtils.MacOS)){
                File targetFile = new File(appPath + "libsqlite4java-osx-amd64.dylib");
                if (!targetFile.exists()){
                    FileUtils.copyFile(Objects.requireNonNull(getClass().getResourceAsStream("/libsqlite4java-osx-amd64.dylib")),targetFile);
                }
            }else if (Objects.equals(SystemUtils.currentSystem(), SystemUtils.Linux)){
                File targetFile = new File(appPath + "libsqlite4java-linux-amd64.so");
                if (!targetFile.exists()){
                    FileUtils.copyFile(Objects.requireNonNull(getClass().getResourceAsStream("/libsqlite4java-linux-amd64.so")),targetFile);
                }
                targetFile = new File(appPath + "libsqlite4java-linux-i386.so");
                if (!targetFile.exists()){
                    FileUtils.copyFile(Objects.requireNonNull(getClass().getResourceAsStream("/libsqlite4java-linux-i386.so")),targetFile);
                }
            }
            com.almworks.sqlite4java.SQLite.setLibraryPath(appPath);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.CLOSE);
            alert.show();
        }
    }

}
