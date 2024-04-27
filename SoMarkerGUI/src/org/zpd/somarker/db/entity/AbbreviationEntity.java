package org.zpd.somarker.db.entity;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Column;
import org.orman.mapper.annotation.Entity;

/**
 * Created by zhb on 16/10/4.
 */

@Entity(table = "Abbreviation")
public class AbbreviationEntity extends Model<AbbreviationEntity>{

    @Column(name = "abbreviation")
    private String abbreviation;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "detail")
    private String detail;

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
