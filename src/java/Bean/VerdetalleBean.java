/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import dao.ContratoJpaController;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import modelo.Contrato;
import modelo.Detallecontrato;

/**
 *
 * @author erick
 */
@ManagedBean
@RequestScoped
public class VerdetalleBean implements Serializable{
    HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    int id=0;
     EntityManagerFactory emf = Persistence.createEntityManagerFactory("BiasterPU");
 Contrato contrato=new Contrato();

    List<Detallecontrato> lstdetalle=new ArrayList<>();

    public List<Detallecontrato> getLstdetalle() {
        try {
         id=Integer.parseInt(request.getParameter("id"));
         System.out.println("ID DETALLE" +id);
         contrato=controlador.findContrato(id);
         lstdetalle=contrato.getDetallecontratoList();
         System.out.println("total detalle " +lstdetalle.size());
       } catch (Exception e) {
           int id=0;
           System.out.println("error al cargar detalle " +e.getMessage());
       }
        return lstdetalle;
    }

    public void setLstdetalle(List<Detallecontrato> lstdetalle) {
        this.lstdetalle = lstdetalle;
    }
    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
 
 ContratoJpaController controlador=new ContratoJpaController(emf);
    /**
     * Creates a new instance of VerdetalleBean
     */
    public VerdetalleBean() {
        lstdetalle=new ArrayList<>();
    }
        public void buscardetalle()
    {
        try {
         id=Integer.parseInt(request.getParameter("id"));
         System.out.println("ID DETALLE" +id);
         contrato=controlador.findContrato(id);
         lstdetalle=contrato.getDetallecontratoList();
         System.out.println("total detalle " +lstdetalle.size());
       } catch (Exception e) {
           int id=0;
           System.out.println("error al cargar detalle " +e.getMessage());
       }
    }
         public void persist(Object object) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
