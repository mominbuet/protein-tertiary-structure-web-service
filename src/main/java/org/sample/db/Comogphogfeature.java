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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gun2sh
 */
@Entity
@Table(name = "comogphogfeature")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comogphogfeature.findAll", query = "SELECT c FROM Comogphogfeature c"),
    @NamedQuery(name = "Comogphogfeature.findByComogPhogID", query = "SELECT c FROM Comogphogfeature c WHERE c.comogPhogID = :comogPhogID"),
    @NamedQuery(name = "Comogphogfeature.findByScopid", query = "SELECT c FROM Comogphogfeature c WHERE c.scopid = :scopid"),
    @NamedQuery(name = "Comogphogfeature.findByFeatureVector", query = "SELECT c FROM Comogphogfeature c WHERE c.featureVector = :featureVector")})
public class Comogphogfeature implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ComogPhogID")
    private Integer comogPhogID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "scopid")
    private String scopid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12000)
    @Column(name = "FeatureVector")
    private String featureVector;
    @JoinColumn(name = "pid", referencedColumnName = "pid")
    @ManyToOne
    private PProtein pid;

    public Comogphogfeature() {
    }

    public Comogphogfeature(Integer comogPhogID) {
        this.comogPhogID = comogPhogID;
    }

    public Comogphogfeature(Integer comogPhogID, String scopid, String featureVector) {
        this.comogPhogID = comogPhogID;
        this.scopid = scopid;
        this.featureVector = featureVector;
    }

    public Integer getComogPhogID() {
        return comogPhogID;
    }

    public void setComogPhogID(Integer comogPhogID) {
        this.comogPhogID = comogPhogID;
    }

    public String getScopid() {
        return scopid;
    }

    public void setScopid(String scopid) {
        this.scopid = scopid;
    }

    public String getFeatureVector() {
        return featureVector;
    }

    public void setFeatureVector(String featureVector) {
        this.featureVector = featureVector;
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
        hash += (comogPhogID != null ? comogPhogID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comogphogfeature)) {
            return false;
        }
        Comogphogfeature other = (Comogphogfeature) object;
        if ((this.comogPhogID == null && other.comogPhogID != null) || (this.comogPhogID != null && !this.comogPhogID.equals(other.comogPhogID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sample.db.Comogphogfeature[ comogPhogID=" + comogPhogID + " ]";
    }
    
}
