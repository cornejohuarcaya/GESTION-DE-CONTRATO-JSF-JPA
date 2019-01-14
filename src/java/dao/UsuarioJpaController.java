/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Contrato;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Amortizacion;
import modelo.Usuario;

/**
 *
 * @author erick
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getContratoList() == null) {
            usuario.setContratoList(new ArrayList<Contrato>());
        }
        if (usuario.getAmortizacionList() == null) {
            usuario.setAmortizacionList(new ArrayList<Amortizacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Contrato> attachedContratoList = new ArrayList<Contrato>();
            for (Contrato contratoListContratoToAttach : usuario.getContratoList()) {
                contratoListContratoToAttach = em.getReference(contratoListContratoToAttach.getClass(), contratoListContratoToAttach.getCodigo());
                attachedContratoList.add(contratoListContratoToAttach);
            }
            usuario.setContratoList(attachedContratoList);
            List<Amortizacion> attachedAmortizacionList = new ArrayList<Amortizacion>();
            for (Amortizacion amortizacionListAmortizacionToAttach : usuario.getAmortizacionList()) {
                amortizacionListAmortizacionToAttach = em.getReference(amortizacionListAmortizacionToAttach.getClass(), amortizacionListAmortizacionToAttach.getCodigo());
                attachedAmortizacionList.add(amortizacionListAmortizacionToAttach);
            }
            usuario.setAmortizacionList(attachedAmortizacionList);
            em.persist(usuario);
            for (Contrato contratoListContrato : usuario.getContratoList()) {
                Usuario oldCodigousuarioOfContratoListContrato = contratoListContrato.getCodigousuario();
                contratoListContrato.setCodigousuario(usuario);
                contratoListContrato = em.merge(contratoListContrato);
                if (oldCodigousuarioOfContratoListContrato != null) {
                    oldCodigousuarioOfContratoListContrato.getContratoList().remove(contratoListContrato);
                    oldCodigousuarioOfContratoListContrato = em.merge(oldCodigousuarioOfContratoListContrato);
                }
            }
            for (Amortizacion amortizacionListAmortizacion : usuario.getAmortizacionList()) {
                Usuario oldCodigousuarioOfAmortizacionListAmortizacion = amortizacionListAmortizacion.getCodigousuario();
                amortizacionListAmortizacion.setCodigousuario(usuario);
                amortizacionListAmortizacion = em.merge(amortizacionListAmortizacion);
                if (oldCodigousuarioOfAmortizacionListAmortizacion != null) {
                    oldCodigousuarioOfAmortizacionListAmortizacion.getAmortizacionList().remove(amortizacionListAmortizacion);
                    oldCodigousuarioOfAmortizacionListAmortizacion = em.merge(oldCodigousuarioOfAmortizacionListAmortizacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getCodigo());
            List<Contrato> contratoListOld = persistentUsuario.getContratoList();
            List<Contrato> contratoListNew = usuario.getContratoList();
            List<Amortizacion> amortizacionListOld = persistentUsuario.getAmortizacionList();
            List<Amortizacion> amortizacionListNew = usuario.getAmortizacionList();
            List<Contrato> attachedContratoListNew = new ArrayList<Contrato>();
            for (Contrato contratoListNewContratoToAttach : contratoListNew) {
                contratoListNewContratoToAttach = em.getReference(contratoListNewContratoToAttach.getClass(), contratoListNewContratoToAttach.getCodigo());
                attachedContratoListNew.add(contratoListNewContratoToAttach);
            }
            contratoListNew = attachedContratoListNew;
            usuario.setContratoList(contratoListNew);
            List<Amortizacion> attachedAmortizacionListNew = new ArrayList<Amortizacion>();
            for (Amortizacion amortizacionListNewAmortizacionToAttach : amortizacionListNew) {
                amortizacionListNewAmortizacionToAttach = em.getReference(amortizacionListNewAmortizacionToAttach.getClass(), amortizacionListNewAmortizacionToAttach.getCodigo());
                attachedAmortizacionListNew.add(amortizacionListNewAmortizacionToAttach);
            }
            amortizacionListNew = attachedAmortizacionListNew;
            usuario.setAmortizacionList(amortizacionListNew);
            usuario = em.merge(usuario);
            for (Contrato contratoListOldContrato : contratoListOld) {
                if (!contratoListNew.contains(contratoListOldContrato)) {
                    contratoListOldContrato.setCodigousuario(null);
                    contratoListOldContrato = em.merge(contratoListOldContrato);
                }
            }
            for (Contrato contratoListNewContrato : contratoListNew) {
                if (!contratoListOld.contains(contratoListNewContrato)) {
                    Usuario oldCodigousuarioOfContratoListNewContrato = contratoListNewContrato.getCodigousuario();
                    contratoListNewContrato.setCodigousuario(usuario);
                    contratoListNewContrato = em.merge(contratoListNewContrato);
                    if (oldCodigousuarioOfContratoListNewContrato != null && !oldCodigousuarioOfContratoListNewContrato.equals(usuario)) {
                        oldCodigousuarioOfContratoListNewContrato.getContratoList().remove(contratoListNewContrato);
                        oldCodigousuarioOfContratoListNewContrato = em.merge(oldCodigousuarioOfContratoListNewContrato);
                    }
                }
            }
            for (Amortizacion amortizacionListOldAmortizacion : amortizacionListOld) {
                if (!amortizacionListNew.contains(amortizacionListOldAmortizacion)) {
                    amortizacionListOldAmortizacion.setCodigousuario(null);
                    amortizacionListOldAmortizacion = em.merge(amortizacionListOldAmortizacion);
                }
            }
            for (Amortizacion amortizacionListNewAmortizacion : amortizacionListNew) {
                if (!amortizacionListOld.contains(amortizacionListNewAmortizacion)) {
                    Usuario oldCodigousuarioOfAmortizacionListNewAmortizacion = amortizacionListNewAmortizacion.getCodigousuario();
                    amortizacionListNewAmortizacion.setCodigousuario(usuario);
                    amortizacionListNewAmortizacion = em.merge(amortizacionListNewAmortizacion);
                    if (oldCodigousuarioOfAmortizacionListNewAmortizacion != null && !oldCodigousuarioOfAmortizacionListNewAmortizacion.equals(usuario)) {
                        oldCodigousuarioOfAmortizacionListNewAmortizacion.getAmortizacionList().remove(amortizacionListNewAmortizacion);
                        oldCodigousuarioOfAmortizacionListNewAmortizacion = em.merge(oldCodigousuarioOfAmortizacionListNewAmortizacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getCodigo();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<Contrato> contratoList = usuario.getContratoList();
            for (Contrato contratoListContrato : contratoList) {
                contratoListContrato.setCodigousuario(null);
                contratoListContrato = em.merge(contratoListContrato);
            }
            List<Amortizacion> amortizacionList = usuario.getAmortizacionList();
            for (Amortizacion amortizacionListAmortizacion : amortizacionList) {
                amortizacionListAmortizacion.setCodigousuario(null);
                amortizacionListAmortizacion = em.merge(amortizacionListAmortizacion);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
   public Usuario loguin(String usu, String pass ) {
        EntityManager em = getEntityManager();
        
        Query query = em.createNamedQuery("Usuario.loguin");
        query.setParameter("usuario", usu);
        query.setParameter("password", pass);
      
      Usuario usuario;
      usuario=new Usuario();
      
        try {
          usuario=(Usuario)query.getSingleResult();
        } catch (Exception e) {
            usuario =new Usuario();
            usuario.setCodigo(0);
            usuario.setApellidosnombres("");
        }
      
      return usuario;
    } 
}
