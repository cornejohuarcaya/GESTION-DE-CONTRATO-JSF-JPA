/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author erick
 */
@ManagedBean
@RequestScoped
public class Prueba implements Serializable{

    List<String> lista=new ArrayList<String>();
    HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    
    /**
     * Creates a new instance of Prueba
     */
    String valor="";
    
    public Prueba() {
       session.setAttribute("user", "erick");

        lista.add("erick");
        lista.add("milton");
        lista.add("camiho");
        lista.add("milagros");
    }
    public Prueba(HttpServletRequest servletRequest) {
       
    }
    public String getValor()
    {
       
        valor=request.getParameter("id");
        valor=session.getAttribute("user").toString()+  valor + request.getContextPath() +"    "  +request.getLocalPort() + request.getRequestURL();
    return valor;
    }
    public List<String> getLista()
    {
        return lista;
    }
    public String getIdtexto()
    {
        return "txt1";
    }
}

