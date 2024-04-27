package org.zpd.somarker.db.entity;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Column;
import org.orman.mapper.annotation.Entity;

/**
 * Created by zhb on 16/10/2.
 */
@Entity(table = "WormGene")
public class WormGeneEntity extends Model<WormGeneEntity>{
    @Column(name = "Gene")
    private String gene;

    @Column(name = "Chromosome")
    private String chromosome;

    @Column(name = "GeneticPosition")
    private double geneticPosition;

    @Column(name = "PPFrom")
    private double ppFrom;

    @Column(name = "PPTo")
    private double ppTo;

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

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

    public double getPpFrom() {
        return ppFrom;
    }

    public void setPpFrom(double ppFrom) {
        this.ppFrom = ppFrom;
    }

    public double getPpTo() {
        return ppTo;
    }

    public void setPpTo(double ppTo) {
        this.ppTo = ppTo;
    }
}
