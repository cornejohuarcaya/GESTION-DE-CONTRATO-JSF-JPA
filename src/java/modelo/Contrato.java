/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author erick
 */
@Entity
@Table(name = "contrato")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contrato.findAll", query = "SELECT c FROM Contrato c")})
public class Contrato implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private double total;
    @Column(name = "pagado")
    private Boolean pagado;
    @Column(name = "saldo")
    private double saldo;
    @Column(name = "detalle")
    private String detalle;
    @Column(name = "numfactura")
    private String numfactura;
    @OneToMany(mappedBy = "codigocontrato")
    private List<Detallecontrato> detallecontratoList;
    @JoinColumn(name = "codigocliente", referencedColumnName = "codigo")
    @ManyToOne
    private Cliente codigocliente;
    @JoinColumn(name = "codigousuario", referencedColumnName = "codigo")
    @ManyToOne
    private Usuario codigousuario;
    @OneToMany(mappedBy = "codigocontraro")
    private List<Amortizacion> amortizacionList;

    public Contrato() {
    }

    public Contrato(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Boolean getPagado() {
        return pagado;
    }
public String getPagadocadena() {
       String cadena="NO";
       try {
        if (pagado)
            cadena="SI";
               
    } catch (Exception e) {
         cadena="NO";
    }
               return cadena;
    }
    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getNumfactura() {
        return numfactura;
    }

    public void setNumfactura(String numfactura) {
        this.numfactura = numfactura;
    }

    @XmlTransient
    public List<Detallecontrato> getDetallecontratoList() {
        return detallecontratoList;
    }

    public void setDetallecontratoList(List<Detallecontrato> detallecontratoList) {
        this.detallecontratoList = detallecontratoList;
    }

    public Cliente getCodigocliente() {
        return codigocliente;
    }

    public void setCodigocliente(Cliente codigocliente) {
        this.codigocliente = codigocliente;
    }

    public Usuario getCodigousuario() {
        return codigousuario;
    }

    public void setCodigousuario(Usuario codigousuario) {
        this.codigousuario = codigousuario;
    }

    @XmlTransient
    public List<Amortizacion> getAmortizacionList() {
        return amortizacionList;
    }

    public void setAmortizacionList(List<Amortizacion> amortizacionList) {
        this.amortizacionList = amortizacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contrato)) {
            return false;
        }
        Contrato other = (Contrato) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Contrato[ codigo=" + codigo + " ]";
    }

    public void setTotal(Integer Valor1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
