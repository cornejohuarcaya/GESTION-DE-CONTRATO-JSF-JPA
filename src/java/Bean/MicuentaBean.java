/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import dao.UsuarioJpaController;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;
import modelo.Usuario;
import net.bootsfaces.utils.FacesMessages;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author erick
 */
@ManagedBean
@RequestScoped
public class MicuentaBean implements Serializable{
    String confirmacionpassword="";

    public String getConfirmacionpassword() {
        return confirmacionpassword;
    }

    public void setConfirmacionpassword(String confirmacionpassword) {
        this.confirmacionpassword = confirmacionpassword;
    }
HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("BiasterPU");
    UsuarioJpaController controlador=new UsuarioJpaController(emf);
    Usuario usuario=new Usuario();

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public void actualizar()
    {
        if (confirmacionpassword.equals(usuario.getPassword()))
        {
    try {
        String usu=DigestUtils.shaHex(usuario.getUsuario());
        String pass=DigestUtils.shaHex(usuario.getPassword());
        usuario.setUsuario(usu);
        usuario.setPassword(pass);
        controlador.edit(usuario);
        usuario.setUsuario("");
        usuario.setPassword("");
        FacesMessages.info("Se actualizo correctamente","");
          usuario.setUsuario("");
        usuario.setPassword("");
        confirmacionpassword="";
    } catch (Exception ex) {
         FacesMessages.info("lo sentimos se ha producido un error","");
         System.out.println("error :" + ex.getMessage());
    }
        }else
        {
            FacesMessages.error("los campos de las claves deben ser iguales","");
        }
    
    }
    public MicuentaBean() {
        usuario=(Usuario)session.getAttribute("usuario");
        usuario.setUsuario("");
        usuario.setPassword("");
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
