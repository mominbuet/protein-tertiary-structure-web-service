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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gun2sh
 */
@Entity
@Table(name = "p_camat32")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PCamat32.findAll", query = "SELECT p FROM PCamat32 p"),
    @NamedQuery(name = "PCamat32.findByCid", query = "SELECT p FROM PCamat32 p WHERE p.cid = :cid"),
    @NamedQuery(name = "PCamat32.findByCol1", query = "SELECT p FROM PCamat32 p WHERE p.col1 = :col1"),
    @NamedQuery(name = "PCamat32.findByCol2", query = "SELECT p FROM PCamat32 p WHERE p.col2 = :col2"),
    @NamedQuery(name = "PCamat32.findByCol3", query = "SELECT p FROM PCamat32 p WHERE p.col3 = :col3"),
    @NamedQuery(name = "PCamat32.findByCol4", query = "SELECT p FROM PCamat32 p WHERE p.col4 = :col4"),
    @NamedQuery(name = "PCamat32.findByCol5", query = "SELECT p FROM PCamat32 p WHERE p.col5 = :col5"),
    @NamedQuery(name = "PCamat32.findByCol6", query = "SELECT p FROM PCamat32 p WHERE p.col6 = :col6"),
    @NamedQuery(name = "PCamat32.findByCol7", query = "SELECT p FROM PCamat32 p WHERE p.col7 = :col7"),
    @NamedQuery(name = "PCamat32.findByCol8", query = "SELECT p FROM PCamat32 p WHERE p.col8 = :col8"),
    @NamedQuery(name = "PCamat32.findByRowNo", query = "SELECT p FROM PCamat32 p WHERE p.rowNo = :rowNo"),
    @NamedQuery(name = "PCamat32.findByColNo", query = "SELECT p FROM PCamat32 p WHERE p.colNo = :colNo")})
public class PCamat32 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cid")
    private Integer cid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "col1")
    private int col1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "col2")
    private int col2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "col3")
    private int col3;
    @Basic(optional = false)
    @NotNull
    @Column(name = "col4")
    private int col4;
    @Basic(optional = false)
    @NotNull
    @Column(name = "col5")
    private int col5;
    @Basic(optional = false)
    @NotNull
    @Column(name = "col6")
    private int col6;
    @Basic(optional = false)
    @NotNull
    @Column(name = "col7")
    private int col7;
    @Basic(optional = false)
    @NotNull
    @Column(name = "col8")
    private int col8;
    @Basic(optional = false)
    @NotNull
    @Column(name = "row_no")
    private int rowNo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "col_no")
    private int colNo;
    @JoinColumn(name = "pid", referencedColumnName = "pid")
    @ManyToOne(optional = false)
    private PProtein pid;

    public PCamat32() {
    }

    public PCamat32(Integer cid) {
        this.cid = cid;
    }

    public PCamat32(Integer cid, int col1, int col2, int col3, int col4, int col5, int col6, int col7, int col8, int rowNo, int colNo) {
        this.cid = cid;
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
        this.col4 = col4;
        this.col5 = col5;
        this.col6 = col6;
        this.col7 = col7;
        this.col8 = col8;
        this.rowNo = rowNo;
        this.colNo = colNo;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public int getCol1() {
        return col1;
    }

    public void setCol1(int col1) {
        this.col1 = col1;
    }

    public int getCol2() {
        return col2;
    }

    public void setCol2(int col2) {
        this.col2 = col2;
    }

    public int getCol3() {
        return col3;
    }

    public void setCol3(int col3) {
        this.col3 = col3;
    }

    public int getCol4() {
        return col4;
    }

    public void setCol4(int col4) {
        this.col4 = col4;
    }

    public int getCol5() {
        return col5;
    }

    public void setCol5(int col5) {
        this.col5 = col5;
    }

    public int getCol6() {
        return col6;
    }

    public void setCol6(int col6) {
        this.col6 = col6;
    }

    public int getCol7() {
        return col7;
    }

    public void setCol7(int col7) {
        this.col7 = col7;
    }

    public int getCol8() {
        return col8;
    }

    public void setCol8(int col8) {
        this.col8 = col8;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public int getColNo() {
        return colNo;
    }

    public void setColNo(int colNo) {
        this.colNo = colNo;
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
        hash += (cid != null ? cid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PCamat32)) {
            return false;
        }
        PCamat32 other = (PCamat32) object;
        if ((this.cid == null && other.cid != null) || (this.cid != null && !this.cid.equals(other.cid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sample.db.PCamat32[ cid=" + cid + " ]";
    }
    
}
