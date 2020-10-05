package com.controllers;

import com.entidades.Person;
import com.controllers.util.JsfUtil;
import com.controllers.util.PaginationHelper;
import com.beans.PersonFacade;
import java.awt.event.ActionEvent;

import java.io.Serializable;
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
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("personController")
@RequestScoped
public class PersonController implements Serializable {

    private Person current;
    private DataModel items = null;
    @EJB
    private com.beans.PersonFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private UIData personTable;

    public PersonController() {
    }

    public Person getSelected() {
        if (current == null) {
            current = new Person();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PersonFacade getFacade() {
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
        current = (Person) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Person();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create(int tipoPerson) {
        try {
            current.setIdPerson(this.getMaxIdPerson());
            if (tipoPerson==1) {
                current.setKind(1);
            }else if(tipoPerson == 2){
                current.setKind(2);
            }
            
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonCreated"));
            //cleanClientTxt();
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Person) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/client/Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonUpdated"));
            return "List?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Person) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonDeleted"));
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

    public Person getPerson(java.lang.Integer id) {
        return ejbFacade.find(id);
    }
    
    //-------------------- metodos propios ENRIQUE ---------------------------
    
    //Obtiene conteo de clientes y proveedores
    public Object getCountPerson(){
        return ejbFacade.countPerson();
    }
    
    //Obtiene conteo de clientes
    public Object getCountPersonClient(){
        return ejbFacade.countPersonClient();
    }
    
    //obtiene id maximo de persona
    public Integer getMaxIdPerson(){
        return ejbFacade.maxIdPerson()+1;
    }
    
    //Obtiene conteo de clientes
    public Object getCountPersonProvider(){
        return ejbFacade.countPersonProvider();
    }
    
    //Obtiene lista de clientes
    public List<Person> getPersonClients(){
        return ejbFacade.getPersonClients();
    }
    
    //Obtiene lista de proveedores
    public List<Person> getPersonProvider(){
        return ejbFacade.getPersonProvider();
    }
    
    //metodo de limpieza de datos cuando se agrega un nuevo liente
    public void cleanClientTxt(){
        current.setIdPerson(0);
        current.setName("");
        current.setLastname("");
        current.setCompany("");
        current.setAddress1("");
        current.setAddress2("");
        current.setEmail1("");
        current.setEmail2("");
        current.setPhone1("");
        current.setPhone2("");
        current.setKind(0);
        current.setCreatedAt(new Date());
        
        FacesMessage msg = new FacesMessage("Fue Agregado correctamente");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    //-----------elimina a un cliente -------------
    public void deletePerson(ActionEvent event){
        current = (Person) this.personTable.getRowData();
        FacesMessage facesMessage = new FacesMessage("Se ha eliminado!! " + current.getIdPerson());
        
        facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
        
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        performDestroy();
    }

    //------------edita un client -----------
    public String editPerson(ActionEvent event, int tipoPerson){
        current = (Person) this.personTable.getRowData();
                
        if (tipoPerson == 1) {
            return "/client/Edit";
        }else if(tipoPerson == 2){
            return "/provider/Edit";
        }
        
        return "";
    }
    
    @PostConstruct
    public void init(){
        this.current = new Person();
    }

    
    public UIData getPersonTable() {
        return personTable;
    }

    public void setPersonTable(UIData personTable) {
        this.personTable = personTable;
    }

    public Person getCurrent() {
        return current;
    }

    public void setCurrent(Person current) {
        this.current = current;
    }
    
    
    
    
    
    

    //------------------fin----------------------------------
    
    
    @FacesConverter(forClass = Person.class)
    public static class PersonControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PersonController controller = (PersonController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "personController");
            return controller.getPerson(getKey(value));
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
            if (object instanceof Person) {
                Person o = (Person) object;
                return getStringKey(o.getIdPerson());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Person.class.getName());
            }
        }

    }

}
