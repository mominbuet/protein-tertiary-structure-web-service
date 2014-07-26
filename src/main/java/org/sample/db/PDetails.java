/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sample.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@Table(name = "p_details")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PDetails.findAll", query = "SELECT p FROM PDetails p"),
    @NamedQuery(name = "PDetails.findByDetid", query = "SELECT p FROM PDetails p WHERE p.detid = :detid"),
    //@NamedQuery(name = "PDetails.findByScopID", query = "SELECT p FROM PDetails p JOIN p. pd WHERE pd.pid=p and pd.scopsid= :scopid"),
    @NamedQuery(name = "PDetails.findByClass1", query = "SELECT p FROM PDetails p WHERE p.class1 = :class1"),
    @NamedQuery(name = "PDetails.findByFold", query = "SELECT p FROM PDetails p WHERE p.fold = :fold"),
    @NamedQuery(name = "PDetails.findBySuperfamily", query = "SELECT p FROM PDetails p WHERE p.superfamily = :superfamily"),
    @NamedQuery(name = "PDetails.findByFamily", query = "SELECT p FROM PDetails p WHERE p.family = :family"),
    @NamedQuery(name = "PDetails.findByProtein", query = "SELECT p FROM PDetails p WHERE p.protein = :protein"),
    @NamedQuery(name = "PDetails.FindByPid", query = "SELECT p FROM PDetails p WHERE p.pid.pid = :pid"),
    @NamedQuery(name = "PDetails.findBySpecies", query = "SELECT p FROM PDetails p WHERE p.species = :species"),
    @NamedQuery(name = "PDetails.findBySunid", query = "SELECT p FROM PDetails p WHERE p.sunid = :sunid")})
public class PDetails implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "detid")
    private Integer detid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "class")
    private String class1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "fold")
    private String fold;
    @Size(max = 255)
    @Column(name = "superfamily")
    private String superfamily;
    @Size(max = 255)
    @Column(name = "family")
    private String family;
    @Size(max = 255)
    @Column(name = "protein")
    private String protein;
    @Size(max = 255)
    @Column(name = "species")
    private String species;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sunid")
    private String sunid;
    @JoinColumn(name = "pid", referencedColumnName = "pid")
    @OneToOne(optional = false)
    private PProtein pid;

    public PDetails() {
    }

    public PDetails(Integer detid) {
        this.detid = detid;
    }

    public PDetails(Integer detid, String class1, String fold, String sunid) {
        this.detid = detid;
        this.class1 = class1;
        this.fold = fold;
        this.sunid = sunid;
    }

    public Integer getDetid() {
        return detid;
    }

    public void setDetid(Integer detid) {
        this.detid = detid;
    }

    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    public String getFold() {
        return fold;
    }

    public void setFold(String fold) {
        this.fold = fold;
    }

    public String getSuperfamily() {
        return superfamily;
    }

    public void setSuperfamily(String superfamily) {
        this.superfamily = superfamily;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getSunid() {
        return sunid;
    }

    public void setSunid(String sunid) {
        this.sunid = sunid;
    }

    public PProtein getPid() {
        return pid;
    }

    public void setPid(PProtein pid) {
        this.pid = pid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detid != null ? detid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PDetails)) {
            return false;
        }
        PDetails other = (PDetails) object;
        if ((this.detid == null && other.detid != null) || (this.detid != null && !this.detid.equals(other.detid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sample.db.PDetails[ detid=" + detid + " ]";
    }
    
}
