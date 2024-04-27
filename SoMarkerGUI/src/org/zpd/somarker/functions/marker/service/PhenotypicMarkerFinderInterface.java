package org.zpd.somarker.functions.marker.service;

import org.zpd.somarker.db.entity.PhenotypicMarkerEntity;
import org.zpd.somarker.db.entity.WormGeneEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by zhb on 16/10/2.
 */
public interface PhenotypicMarkerFinderInterface {
    List<PhenotypicMarkerEntity> searchBalancers(Map<String,String> params);

    List<WormGeneEntity> searchWormGene(String name);

}
