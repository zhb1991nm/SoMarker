package org.zpd.somarker.functions.marker.service;

import org.zpd.somarker.db.entity.FluorescentMarkerEntity;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by zhb on 16/10/2.
 */
public interface FluorescentMarkerFinderInterface {

    List<FluorescentMarkerEntity> searchBalancers(Map<String,String> params);

    List<FluorescentMarkerEntity> fetchAllBalancers();

    void deleteAllBalancers();

    void bulkImportBalancers(File sourceFile);

    void bulkExportBalancers(File sourceFile);
}
