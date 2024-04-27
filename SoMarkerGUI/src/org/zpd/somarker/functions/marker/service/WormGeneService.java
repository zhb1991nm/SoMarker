package org.zpd.somarker.functions.marker.service;

import org.orman.mapper.Model;
import org.zpd.somarker.db.entity.WormGeneEntity;

import java.util.List;

/**
 * Created by zhb on 16/10/3.
 */
public class WormGeneService {

    public static List<WormGeneEntity>allWormGenes;

    static{
        allWormGenes = Model.fetchAll(WormGeneEntity.class);
    }

}
