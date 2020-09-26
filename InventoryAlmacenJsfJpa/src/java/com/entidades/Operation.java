/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Enrique
 */
@Entity
@Table(name = "operation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Operation.findAll", query = "SELECT o FROM Operation o")
    , @NamedQuery(name = "Operation.findByIdOperation", query = "SELECT o FROM Operation o WHERE o.idOperation = :idOperation")
    , @NamedQuery(name = "Operation.findByQuantity", query = "SELECT o FROM Operation o WHERE o.quantity = :quantity")
    , @NamedQuery(name = "Operation.findByCreatedAt", query = "SELECT o FROM Operation o WHERE o.createdAt = :createdAt")})
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_operation")
    private Integer idOperation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private float quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @JoinColumn(name = "id_product", referencedColumnName = "id_product")
    @ManyToOne(optional = false)
    private Product idProduct;
    @JoinColumn(name = "id_operation_type", referencedColumnName = "id_operation_type")
    @ManyToOne(optional = false)
    private OperationType idOperationType;
    @JoinColumn(name = "id_sell", referencedColumnName = "id_sell")
    @ManyToOne(optional = false)
    private Sell idSell;

    public Operation() {
    }

    public Operation(Integer idOperation) {
        this.idOperation = idOperation;
    }

    public Operation(Integer idOperation, float quantity, Date createdAt) {
        this.idOperation = idOperation;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public Integer getIdOperation() {
        return idOperation;
    }

    public void setIdOperation(Integer idOperation) {
        this.idOperation = idOperation;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Product getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Product idProduct) {
        this.idProduct = idProduct;
    }

    public OperationType getIdOperationType() {
        return idOperationType;
    }

    public void setIdOperationType(OperationType idOperationType) {
        this.idOperationType = idOperationType;
    }

    public Sell getIdSell() {
        return idSell;
    }

    public void setIdSell(Sell idSell) {
        this.idSell = idSell;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOperation != null ? idOperation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Operation)) {
            return false;
        }
        Operation other = (Operation) object;
        if ((this.idOperation == null && other.idOperation != null) || (this.idOperation != null && !this.idOperation.equals(other.idOperation))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entidades.Operation[ idOperation=" + idOperation + " ]";
    }
    
}
