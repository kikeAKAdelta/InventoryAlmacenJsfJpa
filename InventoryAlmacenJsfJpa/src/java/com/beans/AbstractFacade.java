/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Enrique
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    
   //--------metodos propios Enrique------------------
    
   /*Conteo de clientes y proveedores*/
   public Object countPerson(){
       Query query = getEntityManager().createQuery("SELECT count(0) FROM Person e");
       return query.getSingleResult();
   }
   
   /*Conteo de clientes*/
   public Object countPersonClient(){
       Query query = getEntityManager().createQuery("SELECT count(0) FROM Person e WHERE e.kind = 1");
       return query.getSingleResult();
   }
   
   /*Conteo de proveedores*/
   public Object countPersonProvider(){
       Query query = getEntityManager().createQuery("SELECT count(0) FROM Person e WHERE e.kind = 2");
       return query.getSingleResult();
   }
   
   
   /*Obtiene lista de Clientes*/
   public List<T> getPersonClients(){
       Query query = getEntityManager().createQuery("SELECT e FROM Person e WHERE e.kind = 1");
       return query.getResultList();
   }
   
   /*Obtiene lista de Proveedores*/
   public List<T> getPersonProvider(){
       Query query = getEntityManager().createQuery("SELECT e FROM Person e WHERE e.kind = 2");
       return query.getResultList();
   }
   
   public Integer maxIdPerson(){
       Query query = getEntityManager().createQuery("SELECT max(e.idPerson) FROM Person e");
       
       return Integer.parseInt(query.getSingleResult().toString());
   }
   
   public Object maxIdObjectSell(){
       Query query = getEntityManager().createQuery("select p from Sell p where p.idSell=(\n" +
        "SELECT max(e.idSell) FROM Sell e)");
       
       return query.getSingleResult();
   }
   
   
   //obtener nombre de id categoria
   public List<T> getNombreCategoria(){
       Query query = getEntityManager().createQuery("Select p.idProduct, p.name, p.description,p.inventaryMin,p.priceIn, p.priceOut, p.unit, p.createdAt, c.name, u.name\n" +
        "from Product p \n" +
        "JOIN p.idCategory c\n" +
        "JOIN p.idUser u");
       
       return query.getResultList();
   }
   
   //obtener nombre de id categoria
   public List<T> getAllProducts(){
       Query query = getEntityManager().createQuery("Select p from Product p");
       return query.getResultList();
   }
   
   
   //---Obtener inventario--------------
   public List<T> getAllIventario(){
       Query query = getEntityManager().createQuery("select e from Operation e \n" +
        "join e.idOperationType c\n" +
        "where c.idOperationType = 1");
       
       return query.getResultList();
   }
   
   //----Obtener producto por codigo de barra----------
   public Object getProductData(String barcode){
       Query query = getEntityManager().createQuery("select p from Product p\n" +
        "where p.barcode = '"+ barcode + "'");
       
       return query.getSingleResult();
   }
   
   //Obtener el tipo de operation
   public Object getTypeOperation(int operacion){
       Query query = getEntityManager().createQuery("select e from OperationType e\n" +
        "where e.idOperationType = '"+operacion+"'");
       
       return query.getSingleResult();
   }
   
   //-------------- fin metodos propios
    
}
