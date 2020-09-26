/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entidades;

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
 * @author Enrique
 */
@Entity
@Table(name = "product_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductType.findAll", query = "SELECT p FROM ProductType p")
    , @NamedQuery(name = "ProductType.findByIdProductType", query = "SELECT p FROM ProductType p WHERE p.idProductType = :idProductType")
    , @NamedQuery(name = "ProductType.findByName", query = "SELECT p FROM ProductType p WHERE p.name = :name")})
public class ProductType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_product_type")
    private Integer idProductType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;

    public ProductType() {
    }

    public ProductType(Integer idProductType) {
        this.idProductType = idProductType;
    }

    public ProductType(Integer idProductType, String name) {
        this.idProductType = idProductType;
        this.name = name;
    }

    public Integer getIdProductType() {
        return idProductType;
    }

    public void setIdProductType(Integer idProductType) {
        this.idProductType = idProductType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProductType != null ? idProductType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductType)) {
            return false;
        }
        ProductType other = (ProductType) object;
        if ((this.idProductType == null && other.idProductType != null) || (this.idProductType != null && !this.idProductType.equals(other.idProductType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entidades.ProductType[ idProductType=" + idProductType + " ]";
    }
    
}
