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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */
@Entity
@Table(name = "comogphogfeature_extended")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComogphogfeatureExtended.findAll", query = "SELECT c FROM ComogphogfeatureExtended c"),
    @NamedQuery(name = "ComogphogfeatureExtended.findByComogPhogID", query = "SELECT c FROM ComogphogfeatureExtended c WHERE c.comogPhogID = :comogPhogID"),
    @NamedQuery(name = "ComogphogfeatureExtended.findByScopid", query = "SELECT c FROM ComogphogfeatureExtended c WHERE c.scopid = :scopid"),
    @NamedQuery(name = "ComogphogfeatureExtended.findByFeatureVector", query = "SELECT c FROM ComogphogfeatureExtended c WHERE c.featureVector = :featureVector")})
public class ComogphogfeatureExtended implements Serializable {
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

    public ComogphogfeatureExtended() {
    }

    public ComogphogfeatureExtended(Integer comogPhogID) {
        this.comogPhogID = comogPhogID;
    }

    public ComogphogfeatureExtended(Integer comogPhogID, String scopid, String featureVector) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (comogPhogID != null ? comogPhogID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComogphogfeatureExtended)) {
            return false;
        }
        ComogphogfeatureExtended other = (ComogphogfeatureExtended) object;
        if ((this.comogPhogID == null && other.comogPhogID != null) || (this.comogPhogID != null && !this.comogPhogID.equals(other.comogPhogID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sample.db.ComogphogfeatureExtended[ comogPhogID=" + comogPhogID + " ]";
    }
    
}
