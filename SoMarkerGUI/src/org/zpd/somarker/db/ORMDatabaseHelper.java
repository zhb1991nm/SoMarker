package org.zpd.somarker.db;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.orman.dbms.Database;
import org.orman.dbms.sqlite.SQLite;
import org.orman.mapper.MappingSession;
import org.zpd.somarker.db.entity.AbbreviationEntity;
import org.zpd.somarker.db.entity.FluorescentMarkerEntity;
import org.zpd.somarker.db.entity.PhenotypicMarkerEntity;
import org.zpd.somarker.db.entity.WormGeneEntity;
import org.zpd.foundation.utils.FileUtils;
import org.zpd.foundation.utils.SystemUtils;


import java.io.File;

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
//        if (!dbFile.exists()){
//            FileUtils.copyFile(getClass().getResourceAsStream("/db/EZX.db"),dbFile);
//        }
        FileUtils.copyFile(getClass().getResourceAsStream("/db/EZX.db"),dbFile);
        try {
            if (SystemUtils.currentSystem() == SystemUtils.Windows){
                File targetFile = new File(appPath + "sqlite4java-win32-x64.dll");
                if (!targetFile.exists()){
                    FileUtils.copyFile(getClass().getResourceAsStream("/sqlite4java-win32-x64.dll"),targetFile);
                }
                targetFile = new File(appPath + "sqlite4java-win32-x86.dll");
                if (!targetFile.exists()){
                    FileUtils.copyFile(getClass().getResourceAsStream("/sqlite4java-win32-x86.dll"),targetFile);
                }
            }else if (SystemUtils.currentSystem() == SystemUtils.MacOS){
                File targetFile = new File(appPath + "libsqlite4java-osx-amd64.dylib");
                if (!targetFile.exists()){
                    FileUtils.copyFile(getClass().getResourceAsStream("/libsqlite4java-osx-amd64.dylib"),targetFile);
                }
            }else if (SystemUtils.currentSystem() == SystemUtils.Linux){
                File targetFile = new File(appPath + "libsqlite4java-linux-amd64.so");
                if (!targetFile.exists()){
                    FileUtils.copyFile(getClass().getResourceAsStream("/libsqlite4java-linux-amd64.so"),targetFile);
                }
                targetFile = new File(appPath + "libsqlite4java-linux-i386.so");
                if (!targetFile.exists()){
                    FileUtils.copyFile(getClass().getResourceAsStream("/libsqlite4java-linux-i386.so"),targetFile);
                }
            }
            com.almworks.sqlite4java.SQLite.setLibraryPath(appPath);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.CLOSE);
            alert.show();
        }
    }

}
