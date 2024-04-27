package org.zpd.somarker.db.entity;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Column;
import org.orman.mapper.annotation.Entity;

/**
 * Created by zhb on 16/10/2.
 */

@Entity(table = "FluorescentMarker")
public class FluorescentMarkerEntity extends Model<FluorescentMarkerEntity>{

    @Column(name = "Chromosome")
    private String chromosome;

    @Column(name = "GeneticPosition")
    private double geneticPosition;

    @Column(name = "Promoter")
    private String promoter;

    @Column(name = "Fluorophore")
    private String fluorophore;

    @Column(name = "Allele")
    private String allele;

    @Column(name = "StrainName")
    private String strainName;

    @Column(name = "InsertionMarker")
    private String insertionMarker;

    @Column(name = "Genotype")
    private String genotype;

    @Column(name = "Image")
    private String image;

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public double getGeneticPosition() {
        return geneticPosition;
    }

    public void setGeneticPosition(double geneticPosition) {
        this.geneticPosition = geneticPosition;
    }

    public String getPromoter() {
        return promoter;
    }

    public void setPromoter(String promoter) {
        this.promoter = promoter;
    }

    public String getFluorophore() {
        return fluorophore;
    }

    public void setFluorophore(String fluorophore) {
        this.fluorophore = fluorophore;
    }

    public String getAllele() {
        return allele;
    }

    public void setAllele(String allele) {
        this.allele = allele;
    }

    public String getStrainName() {
        return strainName;
    }

    public void setStrainName(String strainName) {
        this.strainName = strainName;
    }

    public String getInsertionMarker() {
        return insertionMarker;
    }

    public void setInsertionMarker(String insertionMarker) {
        this.insertionMarker = insertionMarker;
    }

    public String getGenotype() {
        return genotype;
    }

    public void setGenotype(String genotype) {
        this.genotype = genotype;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
