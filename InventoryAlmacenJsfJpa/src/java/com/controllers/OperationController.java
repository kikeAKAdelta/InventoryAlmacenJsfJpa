package com.controllers;

import com.entidades.Operation;
import com.controllers.util.JsfUtil;
import com.controllers.util.PaginationHelper;
import com.beans.OperationFacade;
import com.beans.SellFacade;
import com.entidades.Box;
import com.entidades.OperationType;
import com.entidades.Person;
import com.entidades.Product;
import com.entidades.Sell;
import com.entidades.User;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("operationController")
@RequestScoped
public class OperationController implements Serializable {

    private Operation current;
    private Product currentProduct;
    private Person currentPerson;
    private User currentUser;
    private Sell currentSell;
    private Box currentBox;
    private UIData operationTable;
    private DataModel items = null;
    @EJB
    private com.beans.OperationFacade ejbFacade;
    @EJB
    private com.beans.SellFacade ejbFacadeSell;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public OperationController() {
    }

    public Operation getSelected() {
        if (current == null) {
            current = new Operation();
            selectedItemIndex = -1;
        }
        return current;
    }

    private OperationFacade getFacade() {
        return ejbFacade;
    }
    
    private SellFacade getFacadeSell(){
        return ejbFacadeSell;
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
        current = (Operation) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Operation();
        selectedItemIndex = -1;
        return "Create";
    }
    
//    public String createSell(){
//        //currentSell.setIdSell(3);
//            currentSell.setCreatedAt(current.getCreatedAt());
//            currentSell.setIdPerson(currentPerson);
//            currentSell.setIdUser(currentUser);
//            currentSell.setIdOperationType(current.getIdOperationType());
//            currentSell.setIdBox(currentBox);
//            getFacadeSell().create(currentSell);
//            
//            return "Elemento guardado";
//    }

    public String create() {
        try {
            
            //current.getIdSell().setIdUser(currentUser);
            //current.getIdSell().setIdPerson(currentPerson);
            Date fecha = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("yyy-MM-dd");
            String cadenaDate = formato.format(fecha);
            Date dateNow = formato.parse(cadenaDate);
            OperationType operacion = (OperationType) getFacade().getTypeOperation(1);
            
            currentSell.setCreatedAt(dateNow);
            currentSell.setIdPerson(currentPerson);
            currentSell.setIdUser(currentUser);
            currentSell.setIdOperationType(current.getIdOperationType());
            currentSell.setIdBox(currentBox);
            currentSell.setIdOperationType(operacion);
            //getFacadeSell().create(currentSell);
            
            Sell newSell = (Sell) getFacadeSell().maxIdObjectSell();
            
            //Save compras
            
            
            //Save Operation
            getProductDataVoid();
            current.setCreatedAt(dateNow);
            current.setIdOperationType(operacion);
            current.setIdProduct(currentProduct);
            current.setIdSell(currentSell);
            
                        
            
            getFacade().create(current);
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OperationCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Operation) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OperationUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Operation) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OperationDeleted"));
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

    public Operation getOperation(java.lang.Integer id) {
        return ejbFacade.find(id);
    }
    
    /*-----------Metodos propdios Enrique-----------*/
    
    //-----------elimina una operacion -------------
    public void deletePerson(ActionEvent event){
        current = (Operation) this.operationTable.getRowData();
        FacesMessage facesMessage = new FacesMessage("Se ha eliminado!! " + current.getIdOperation());
        
        facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
        
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        performDestroy();
    }

    //------------edita una operacion -----------
    public String editPerson(ActionEvent event, int tipoPerson){
        current = (Operation) this.operationTable.getRowData();
        
        return "/operation/Edit";
    }
    
    //Obtener inventario
    public List<Operation> getAllInventario(){
        return ejbFacade.getAllIventario();
    }
    private UIInput inputText;

    public UIInput getInputText() {
        return inputText;
    }

    public void setInputText(UIInput inputText) {
        this.inputText = inputText;
    }
    
    
    //Obtener producto por medio de codigo de barra
    public String getProductData(){
 
        String barcode = currentProduct.getBarcode();
        //String idUser2 = currentUser.getIdUser()+"";
        if(barcode !=  null){
            FacesMessage facesMessage = new FacesMessage("Tiene algo? "+ barcode);
            facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
        
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            
            currentProduct = (Product) ejbFacade.getProductData(barcode);
        }       
        
        return "Create";
        
    }
    
    //Obtener producto por medio de codigo de barra
    public void getProductDataVoid(){
 
        String barcode = currentProduct.getBarcode();
        //String idUser2 = currentUser.getIdUser()+"";
        if(barcode !=  null){
            FacesMessage facesMessage = new FacesMessage("Tiene algo? "+ barcode);
            facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
        
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            
            currentProduct = (Product) ejbFacade.getProductData(barcode);
        }
        
    }
    
    @PostConstruct
    public void init(){
        this.current = new Operation();
        this.currentProduct = new Product();
        this.currentPerson = new Person();
        this.currentUser = new User();
        this.currentBox = new Box();
        this.currentSell = new Sell();
    }

    public Operation getCurrent() {
        return current;
    }

    public void setCurrent(Operation current) {
        this.current = current;
    }

    public UIData getOperationTable() {
        return operationTable;
    }

    public void setOperationTable(UIData operationTable) {
        this.operationTable = operationTable;
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(Product currentProduct) {
        this.currentProduct = currentProduct;
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Box getCurrentBox() {
        return currentBox;
    }

    public void setCurrentBox(Box currentBox) {
        this.currentBox = currentBox;
    }

    public Sell getCurrentSell() {
        return currentSell;
    }

    public void setCurrentSell(Sell currentSell) {
        this.currentSell = currentSell;
    }
    
    
    
    
    
    
    
    
    /*------------------------Fin-----------------------*/

    @FacesConverter(forClass = Operation.class)
    public static class OperationControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            OperationController controller = (OperationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "operationController");
            return controller.getOperation(getKey(value));
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
            if (object instanceof Operation) {
                Operation o = (Operation) object;
                return getStringKey(o.getIdOperation());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Operation.class.getName());
            }
        }

    }

}
