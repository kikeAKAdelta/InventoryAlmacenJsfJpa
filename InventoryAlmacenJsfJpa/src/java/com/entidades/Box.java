/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Enrique
 */
@Entity
@Table(name = "box")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Box.findAll", query = "SELECT b FROM Box b")
    , @NamedQuery(name = "Box.findByIdBox", query = "SELECT b FROM Box b WHERE b.idBox = :idBox")
    , @NamedQuery(name = "Box.findByCreatedAt", query = "SELECT b FROM Box b WHERE b.createdAt = :createdAt")})
public class Box implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_box")
    private Integer idBox;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBox")
    private Collection<Sell> sellCollection;

    public Box() {
    }

    public Box(Integer idBox) {
        this.idBox = idBox;
    }

    public Box(Integer idBox, Date createdAt) {
        this.idBox = idBox;
        this.createdAt = createdAt;
    }

    public Integer getIdBox() {
        return idBox;
    }

    public void setIdBox(Integer idBox) {
        this.idBox = idBox;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @XmlTransient
    public Collection<Sell> getSellCollection() {
        return sellCollection;
    }

    public void setSellCollection(Collection<Sell> sellCollection) {
        this.sellCollection = sellCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBox != null ? idBox.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Box)) {
            return false;
        }
        Box other = (Box) object;
        if ((this.idBox == null && other.idBox != null) || (this.idBox != null && !this.idBox.equals(other.idBox))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return idBox +" " + createdAt;
    }
    
}
