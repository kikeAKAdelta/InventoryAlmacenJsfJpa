package com.controllers;

import com.entidades.Product;
import com.controllers.util.JsfUtil;
import com.controllers.util.PaginationHelper;
import com.beans.ProductFacade;
import java.awt.event.ActionEvent;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("productController")
@RequestScoped
public class ProductController implements Serializable {

    private Product current;
    private DataModel items = null;
    private UIData productTable;
    @EJB
    private com.beans.ProductFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ProductController() {
    }
    
    

    public Product getSelected() {
        if (current == null) {
            current = new Product();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProductFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Product) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Product();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Product) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductUpdated"));
            return "List";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Product) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Product getProduct(java.lang.Integer id) {
        return ejbFacade.find(id);
    }
    
    /*-------------------------------Metodos propios Enrique--------------------------------*/
    
    //Eliminar un producto
    public void deleteProduct(ActionEvent event){
        current = (Product) this.productTable.getRowData();
        FacesMessage facesMessage = new FacesMessage("Se ha eliminado!! " + current.getIdProduct());
        
        facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
        
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        performDestroy();
    }

    //------------edita un client -----------
    public String editProduct(ActionEvent event){
        current = (Product) this.productTable.getRowData();
        
        return "/product/Edit";
    }
    
    //-----------elimina a un cliente -------------
    public void deletePerson(ActionEvent event){
        current = (Product) this.productTable.getRowData();
        FacesMessage facesMessage = new FacesMessage("Se ha eliminado!! " + current.getIdProduct());
        
        facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
        
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        performDestroy();
    }
    
    
    //Obtener nombre de la categoria
    public List<Product> obtenerNombreCategoria(){
        return ejbFacade.getNombreCategoria();
    }
    //Obtener nombre de la categoria
    public List<Product> getAllProducts(){
        return ejbFacade.getAllProducts();
    }
    
    @PostConstruct
    public void init(){
        this.current = new Product();
    }

    public Product getCurrent() {
        return current;
    }

    public void setCurrent(Product current) {
        this.current = current;
    }

    public UIData getProductTable() {
        return productTable;
    }

    public void setProductTable(UIData productTable) {
        this.productTable = productTable;
    }
    
    
    
    
    
    
    
    /*--------------fin------------------*/
    
    

    @FacesConverter(forClass = Product.class)
    public static class ProductControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductController controller = (ProductController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productController");
            return controller.getProduct(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Product) {
                Product o = (Product) object;
                return getStringKey(o.getIdProduct());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Product.class.getName());
            }
        }

    }

}
