package org.zpd.somarker.db.entity;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Column;
import org.orman.mapper.annotation.Entity;

/**
 * Created by zhb on 16/10/2.
 */
@Entity(table = "PhenotypicMarker")
public class PhenotypicMarkerEntity extends Model<PhenotypicMarkerEntity>{

    @Column(name = "Chromosome")
    private String chromosome;

    @Column(name = "GeneticMarker")
    private String geneticMarker;

    @Column(name = "GeneticPosition")
    private double geneticPosition;

    @Column(name = "StrainName")
    private String strainName;

    @Column(name = "Information")
    private String info;

    @Column(name = "ES")
    private String ES;

    @Column(name = "ME")
    private String ME;

    @Column(name = "NA")
    private String NA;

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getGeneticMarker() {
        return geneticMarker;
    }

    public void setGeneticMarker(String geneticMarker) {
        this.geneticMarker = geneticMarker;
    }

    public double getGeneticPosition() {
        return geneticPosition;
    }

    public void setGeneticPosition(double geneticPosition) {
        this.geneticPosition = geneticPosition;
    }

    public String getStrainName() {
        return strainName;
    }

    public void setStrainName(String strainName) {
        this.strainName = strainName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getES() {
        return ES;
    }

    public void setES(String ES) {
        this.ES = ES;
    }

    public String getME() {
        return ME;
    }

    public void setME(String ME) {
        this.ME = ME;
    }

    public String getNA() {
        return NA;
    }

    public void setNA(String NA) {
        this.NA = NA;
    }
}
