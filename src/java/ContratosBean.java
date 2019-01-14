/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dao.ContratoJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import modelo.Contrato;
import net.bootsfaces.utils.FacesMessages;

/**
 *
 * @author erick
 */
@ManagedBean
@ApplicationScoped
public class ContratosBean implements Serializable{
    HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    int id=0;
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("BiasterPU");
 Contrato contrato=new Contrato();
 
   public ContratosBean() {
    contrato=new Contrato();
     
       
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
    ContratoJpaController controlador=new ContratoJpaController(emf);
    public List<Contrato> getLstcontrato() {
        lstcontrato=controlador.findContratoEntities();
        return lstcontrato;
    }
   

    public void setLstcontrato(List<Contrato> lstcontrato) {
        this.lstcontrato = lstcontrato;
    }


    List<Contrato> lstcontrato=new  ArrayList<>();
 
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
    
    public void actualizar()
    {
     try {
         controlador.edit(contrato);
         FacesMessages.info("Actualizado Correctamente", "");
         contrato=new Contrato();
     } catch (Exception ex) {
         Logger.getLogger(ContratosBean.class.getName()).log(Level.SEVERE, null, ex);
         FacesMessages.error("Verifique la informacion consgmada", "");
     }
    }
    public void seleccionar(Contrato cont)
    {
        contrato=new Contrato();
        contrato=cont;
    }
  

}
