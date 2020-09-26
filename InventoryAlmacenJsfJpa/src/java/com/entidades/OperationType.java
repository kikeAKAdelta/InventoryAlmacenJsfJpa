/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entidades;

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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Enrique
 */
@Entity
@Table(name = "operation_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OperationType.findAll", query = "SELECT o FROM OperationType o")
    , @NamedQuery(name = "OperationType.findByIdOperationType", query = "SELECT o FROM OperationType o WHERE o.idOperationType = :idOperationType")
    , @NamedQuery(name = "OperationType.findByName", query = "SELECT o FROM OperationType o WHERE o.name = :name")})
public class OperationType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_operation_type")
    private Integer idOperationType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOperationType")
    private Collection<Sell> sellCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOperationType")
    private Collection<Operation> operationCollection;

    public OperationType() {
    }

    public OperationType(Integer idOperationType) {
        this.idOperationType = idOperationType;
    }

    public OperationType(Integer idOperationType, String name) {
        this.idOperationType = idOperationType;
        this.name = name;
    }

    public Integer getIdOperationType() {
        return idOperationType;
    }

    public void setIdOperationType(Integer idOperationType) {
        this.idOperationType = idOperationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<Sell> getSellCollection() {
        return sellCollection;
    }

    public void setSellCollection(Collection<Sell> sellCollection) {
        this.sellCollection = sellCollection;
    }

    @XmlTransient
    public Collection<Operation> getOperationCollection() {
        return operationCollection;
    }

    public void setOperationCollection(Collection<Operation> operationCollection) {
        this.operationCollection = operationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOperationType != null ? idOperationType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OperationType)) {
            return false;
        }
        OperationType other = (OperationType) object;
        if ((this.idOperationType == null && other.idOperationType != null) || (this.idOperationType != null && !this.idOperationType.equals(other.idOperationType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entidades.OperationType[ idOperationType=" + idOperationType + " ]";
    }
    
}
