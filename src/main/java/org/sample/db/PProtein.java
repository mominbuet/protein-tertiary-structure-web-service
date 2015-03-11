/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sample.db;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author user
 */
@Entity
@Table(name = "p_protein")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PProtein.findAll", query = "SELECT p FROM PProtein p"),
    @NamedQuery(name = "PProtein.findByPid", query = "SELECT p FROM PProtein p WHERE p.pid = :pid"),
    @NamedQuery(name = "PProtein.findByPdbid", query = "SELECT p FROM PProtein p WHERE p.pdbid = :pdbid"),
    @NamedQuery(name = "PProtein.findByScopsid", query = "SELECT p FROM PProtein p WHERE p.scopsid = :scopsid"),
    @NamedQuery(name = "PProtein.findByName", query = "SELECT p FROM PProtein p WHERE p.name = :name"),
    @NamedQuery(name = "PProtein.findByDataset", query = "SELECT p FROM PProtein p WHERE p.dataset = :dataset")})
public class PProtein implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pid")
    private Integer pid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "pdbid")
    private String pdbid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "scopsid")
    private String scopsid;
    @Size(max = 250)
    @Column(name = "Name")
    private String name;
    @Column(name = "Dataset")
    private Integer dataset;
    @OneToMany(mappedBy = "pid")
    private Collection<Comogphogfeature> comogphogfeatureCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pid")
    private PDetails pDetails;

    public PProtein() {
    }

    public PProtein(Integer pid) {
        this.pid = pid;
    }

    public PProtein(Integer pid, String pdbid, String scopsid) {
        this.pid = pid;
        this.pdbid = pdbid;
        this.scopsid = scopsid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getPdbid() {
        return pdbid;
    }

    public void setPdbid(String pdbid) {
        this.pdbid = pdbid;
    }

    public String getScopsid() {
        return scopsid;
    }

    public void setScopsid(String scopsid) {
        this.scopsid = scopsid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDataset() {
        return dataset;
    }

    public void setDataset(Integer dataset) {
        this.dataset = dataset;
    }

    @XmlTransient
    public Collection<Comogphogfeature> getComogphogfeatureCollection() {
        return comogphogfeatureCollection;
    }

    public void setComogphogfeatureCollection(Collection<Comogphogfeature> comogphogfeatureCollection) {
        this.comogphogfeatureCollection = comogphogfeatureCollection;
    }

    public PDetails getPDetails() {
        return pDetails;
    }

    public void setPDetails(PDetails pDetails) {
        this.pDetails = pDetails;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pid != null ? pid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PProtein)) {
            return false;
        }
        PProtein other = (PProtein) object;
        if ((this.pid == null && other.pid != null) || (this.pid != null && !this.pid.equals(other.pid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sample.db.PProtein[ pid=" + pid + " ]";
    }
    
}
