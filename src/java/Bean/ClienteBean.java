/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import dao.ClienteJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Cliente;
import net.bootsfaces.utils.FacesMessages;

/**
 *
 * @author erick
 */
@ManagedBean
@ApplicationScoped
public class ClienteBean implements Serializable{

    Cliente cliente ;
List<Cliente>lstcliente;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("BiasterPU");
    ClienteJpaController controlador=new ClienteJpaController(emf);
    
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Cliente> getLstcliente() {
        lstcliente=controlador.findClienteEntities();
        return lstcliente;
    }

    public void setLstcliente(List<Cliente> lstcliente) {
        this.lstcliente = lstcliente;
    }
    
    public ClienteBean() {
        cliente=new  Cliente();
    }
    
    public void registrar()
    {
        if (cliente.getCodigo()==null)
        {
            try {
                controlador.create(cliente);
            FacesMessages.info( "Cliente Registrado Correctamente","");
            cliente=new Cliente();
            } catch (Exception e) {
                FacesMessages.error("Verifique sus datos","");
            }
            
        }
        else if(cliente.getCodigo()>0)
        {
             try {
                controlador.edit(cliente);
            FacesMessages.info("Actualizado Correctamente","");
            cliente=new Cliente();
            } catch (Exception e) {
                FacesMessages.error("Verifique sus datos","");
            }
            
        }
        
    }
    public void eliminar(int id)
    {
        try {
            controlador.destroy(id);
            FacesMessages.info("Eliminado Correctamente","");
            } catch (Exception e) {
                FacesMessages.error("ha ocurrido un error","");
            }
    }
    public void seleccionar(Cliente seleccionado)
    {
        cliente=seleccionado;
        lstcliente=new ArrayList<>();
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
