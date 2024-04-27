package org.zpd.somarker.functions.marker.service;

import org.orman.mapper.C;
import org.orman.mapper.Model;
import org.orman.mapper.ModelQuery;
import org.orman.sql.Query;
import org.zpd.somarker.db.entity.PhenotypicMarkerEntity;
import org.zpd.somarker.db.entity.WormGeneEntity;
import org.zpd.foundation.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhb on 16/10/2.
 */
public class PhenotypicMarkerFinderService implements PhenotypicMarkerFinderInterface {

    @Override
    public List<PhenotypicMarkerEntity> searchBalancers(Map<String, String> params) {
//        String name = Tool.instance().getString(params.get("name"));
        String chromosome = Tool.instance().getString(params.get("chromosome"));
        String position = params.get("position");
        if (chromosome.length() == 0 && position.length() == 0){
            return new ArrayList<>();
        }
        String[] chromosomes = chromosome.split(",");
        if (chromosome != null){
            Float _position = 0F;
            if (position != null && !"".equals(position)){
                _position = Tool.instance().getFloat(position);
            }
            String sql = "select * from PhenotypicMarker ";
            if (chromosomes.length > 0){
                sql += "where Chromosome in (";
                for (String str : chromosomes){
                    sql += "'" + str + "'";
                    if (str != chromosomes[chromosomes.length - 1]){
                        sql += ",";
                    }
                }
                sql += ") ";
            }
            sql += " order by (GeneticPosition - " + _position + ") * (GeneticPosition - " + _position + ") asc LIMIT 20";
              return Model.fetchQuery(new Query(sql),PhenotypicMarkerEntity.class);
        }
        return new ArrayList<>();
    }

    @Override
    public List<WormGeneEntity> searchWormGene(String name) {
        if (name == null || name.equals("")){
            return null;
        }
        List<WormGeneEntity> result = Model.fetchQuery(ModelQuery.select().from(WormGeneEntity.class).where(C.like("Gene",name)).getQuery(),WormGeneEntity.class);
        if (result.size() > 0){
            return result;
        }
        return null;
    }

}
