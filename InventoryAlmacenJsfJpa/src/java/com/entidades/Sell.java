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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "sell")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sell.findAll", query = "SELECT s FROM Sell s")
    , @NamedQuery(name = "Sell.findByIdSell", query = "SELECT s FROM Sell s WHERE s.idSell = :idSell")
    , @NamedQuery(name = "Sell.findByTotal", query = "SELECT s FROM Sell s WHERE s.total = :total")
    , @NamedQuery(name = "Sell.findByCash", query = "SELECT s FROM Sell s WHERE s.cash = :cash")
    , @NamedQuery(name = "Sell.findByDiscount", query = "SELECT s FROM Sell s WHERE s.discount = :discount")
    , @NamedQuery(name = "Sell.findByCreatedAt", query = "SELECT s FROM Sell s WHERE s.createdAt = :createdAt")})
public class Sell implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_sell")
    private Integer idSell;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total")
    private double total;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cash")
    private double cash;
    @Basic(optional = false)
    @NotNull
    @Column(name = "discount")
    private double discount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @JoinColumn(name = "id_person", referencedColumnName = "id_person")
    @ManyToOne(optional = false)
    private Person idPerson;
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @ManyToOne(optional = false)
    private User idUser;
    @JoinColumn(name = "id_operation_type", referencedColumnName = "id_operation_type")
    @ManyToOne(optional = false)
    private OperationType idOperationType;
    @JoinColumn(name = "id_box", referencedColumnName = "id_box")
    @ManyToOne(optional = false)
    private Box idBox;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSell")
    private Collection<Operation> operationCollection;

    public Sell() {
    }

    public Sell(Integer idSell) {
        this.idSell = idSell;
    }

    public Sell(Integer idSell, double total, double cash, double discount, Date createdAt) {
        this.idSell = idSell;
        this.total = total;
        this.cash = cash;
        this.discount = discount;
        this.createdAt = createdAt;
    }

    public Integer getIdSell() {
        return idSell;
    }

    public void setIdSell(Integer idSell) {
        this.idSell = idSell;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Person getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Person idPerson) {
        this.idPerson = idPerson;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public OperationType getIdOperationType() {
        return idOperationType;
    }

    public void setIdOperationType(OperationType idOperationType) {
        this.idOperationType = idOperationType;
    }

    public Box getIdBox() {
        return idBox;
    }

    public void setIdBox(Box idBox) {
        this.idBox = idBox;
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
        hash += (idSell != null ? idSell.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sell)) {
            return false;
        }
        Sell other = (Sell) object;
        if ((this.idSell == null && other.idSell != null) || (this.idSell != null && !this.idSell.equals(other.idSell))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return idSell + " " ;
    }
    
}
