/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import dao.ClienteJpaController;
import dao.ContratoJpaController;
import dao.DetallecontratoJpaController;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;
import modelo.Cliente;
import modelo.Contrato;
import modelo.Detallecontrato;
import modelo.Usuario;
import net.bootsfaces.utils.FacesMessages;

/**
 *
 * @author erick
 */
@ManagedBean
@SessionScoped
public class ContratoBean {
    double Valor1=0;

    public double getValor1() {
        return Valor1;
    }

    public void setValor1(double Valor1) {
        this.Valor1 = Valor1;
    }
HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    Cliente cliente;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    Contrato contrato ;
    Detallecontrato detallecontrato;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("BiasterPU");
    ContratoJpaController controlador=new ContratoJpaController(emf);
    DetallecontratoJpaController controladordet=new DetallecontratoJpaController(emf);
    ClienteJpaController controladorCliente=new ClienteJpaController(emf);
    ArrayList<Detallecontrato> lstdetalle=new ArrayList<Detallecontrato>();
    
    public ContratoBean() {
        contrato=new Contrato();
//        lstdetalle=new ArrayList<Detallecontrato>();
        detallecontrato=new Detallecontrato();
        cliente=new Cliente();
    }

    public Contrato getContrato() {
        return contrato;
    }
    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
    public Detallecontrato getDetallecontrato() {
        return detallecontrato;
    }
    public void setDetallecontrato(Detallecontrato detallecontrato) {
        this.detallecontrato = detallecontrato;
    }
    public ArrayList<Detallecontrato> getLstdetalle() {
        return lstdetalle;
    }
    public void setLstdetalle(ArrayList<Detallecontrato> lstdetalle) {
        this.lstdetalle = lstdetalle;
    }
    
    
    public void registrar()
    {
        try {
            Date fecha=new Date();
            contrato.setFecha(fecha);
            contrato.setCodigocliente(cliente);
            contrato.setCodigousuario((Usuario) session.getAttribute("usuario"));
            controlador.create(contrato);
            for (Detallecontrato detallecontrato1 : lstdetalle) {
                 detallecontrato1.setCodigo(null);
                 detallecontrato1.setCodigocontrato(contrato);
                 
                controladordet.create(detallecontrato1);
            }
            FacesMessages.info("El contrato fue registrado correctamente","");
            
            lstdetalle=new ArrayList<>();
            contrato=new Contrato();
            cliente=new Cliente();
        } catch (Exception e) {
            FacesMessages.error("Verifique los datos","");
            System.out.println("error al ingresar" + e.getMessage());
        }
    }
    
    public void agregar()
    {
        lstdetalle.add(detallecontrato);
        colocarindice();
        detallecontrato=new Detallecontrato();
    }
    public void quitar(Detallecontrato seleccionado)
    {
        lstdetalle.remove(seleccionado.getCodigo().intValue());
          colocarindice();
          System.out.println("quitando "+seleccionado.getCodigo());
    }
    public void seleccionarcliente(Cliente cli)
    {
        cliente=cli;
    }
       
  public void colocarindice()
  {
      double Valor1 = 0;
      int indice=0;
      for (Detallecontrato pedidodetalle1 : lstdetalle) {
          pedidodetalle1.setCodigo(indice);
          Valor1= Valor1+(pedidodetalle1.getTotal());
          indice+=1;
      }
      contrato.setTotal(Valor1);
      contrato.setSaldo(Valor1);
//      pedidotrabajo.setPreciototal(Valor1);
      System.out.println("total " + Valor1);
  }

}
  
