package org.zpd.somarker.functions.marker.service;

import org.orman.mapper.Model;
import org.zpd.somarker.db.entity.AbbreviationEntity;

import java.util.List;

/**
 * Created by zhb on 16/10/4.
 */
public class AbbreviationService {

    public static List<AbbreviationEntity> abbreviationEntities;

    static{
        abbreviationEntities = Model.fetchAll(AbbreviationEntity.class);
    }

    public static AbbreviationEntity findAbbreviation(String abbreviation){
        AbbreviationEntity target = null;
        if (abbreviation.equals("N/A")){
            return null;
        }
        for (AbbreviationEntity entity: abbreviationEntities){
            if (entity.getAbbreviation().equals(abbreviation)){
                target = entity;
            }
        }
        return target;
    }
}
