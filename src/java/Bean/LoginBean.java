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
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.Usuario;
import org.apache.commons.codec.digest.DigestUtils;

@ManagedBean
@ViewScoped
public class LoginBean  implements Serializable{
HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
   
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("BiasterPU");
    UsuarioJpaController controlador=new UsuarioJpaController(emf);
    public LoginBean() {
    }
    String valor = "irmenu";

    public String getValor() {
        return valor;
    }
    

    public void setValor(String valor) {
        this.valor = valor;
    }
    String usuario = "";

    public String getUsuario() {
        return usuario;
    }

    public String getPassword() {
        return password;
    }
    String password = "";

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String loguear() {
        //probando encriptacion
      
        String encriptusuario=DigestUtils.shaHex(usuario);
        String encriptpass=DigestUtils.shaHex(password);
        usuario=encriptusuario;
        password=encriptpass;
       
       
       
       
        Usuario objusuario;
        objusuario=new Usuario();
        objusuario=controlador.loguin(usuario, password);
        
       System.out.println("Bienvenido: " + objusuario.getApellidosnombres()); 
       
        if (objusuario.getCodigo()>0 ) {
            valor = "menu?faces-redirect=true";
            session.setAttribute("usuario", objusuario);
             
        } else {
            valor = "salir";
            System.out.println("Error de validacion de usuario");
        }
       
        return valor;
       
    }
    
    public String salir() {
        session.removeAttribute("usuario");
        return "login?faces-redirect=true";
    }
    
       public String volvererror() {

       return "menu?faces-redirect=true";
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
    public String apellidosusuario (){
        String v = "";
        try {
            v = ((Usuario) session.getAttribute("usuario")).getApellidosnombres();
        } catch (Exception e) {
        }
    return v;
        
    }
    
}
