package org.zpd.somarker.functions.marker.service;


import org.zpd.somarker.db.entity.WormGeneEntity;

import java.io.File;
import java.util.List;

/**
 * Created by zhb on 16/10/3.
 */
public interface WormGeneInterface {

    List<WormGeneEntity> fetchAllWormGene();
    void deleteAllWormGenes();

    void bulkImportWormGenes(File sourceFile);

    void bulkExportWormGenes(File sourceFile);


}
