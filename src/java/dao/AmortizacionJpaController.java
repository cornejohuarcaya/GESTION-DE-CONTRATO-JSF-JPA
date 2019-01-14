/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Amortizacion;
import modelo.Contrato;
import modelo.Usuario;

/**
 *
 * @author erick
 */
public class AmortizacionJpaController implements Serializable {

    public AmortizacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Amortizacion amortizacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contrato codigocontraro = amortizacion.getCodigocontraro();
            if (codigocontraro != null) {
                codigocontraro = em.getReference(codigocontraro.getClass(), codigocontraro.getCodigo());
                amortizacion.setCodigocontraro(codigocontraro);
            }
            Usuario codigousuario = amortizacion.getCodigousuario();
            if (codigousuario != null) {
                codigousuario = em.getReference(codigousuario.getClass(), codigousuario.getCodigo());
                amortizacion.setCodigousuario(codigousuario);
            }
            em.persist(amortizacion);
            if (codigocontraro != null) {
                codigocontraro.getAmortizacionList().add(amortizacion);
                codigocontraro = em.merge(codigocontraro);
            }
            if (codigousuario != null) {
                codigousuario.getAmortizacionList().add(amortizacion);
                codigousuario = em.merge(codigousuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Amortizacion amortizacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Amortizacion persistentAmortizacion = em.find(Amortizacion.class, amortizacion.getCodigo());
            Contrato codigocontraroOld = persistentAmortizacion.getCodigocontraro();
            Contrato codigocontraroNew = amortizacion.getCodigocontraro();
            Usuario codigousuarioOld = persistentAmortizacion.getCodigousuario();
            Usuario codigousuarioNew = amortizacion.getCodigousuario();
            if (codigocontraroNew != null) {
                codigocontraroNew = em.getReference(codigocontraroNew.getClass(), codigocontraroNew.getCodigo());
                amortizacion.setCodigocontraro(codigocontraroNew);
            }
            if (codigousuarioNew != null) {
                codigousuarioNew = em.getReference(codigousuarioNew.getClass(), codigousuarioNew.getCodigo());
                amortizacion.setCodigousuario(codigousuarioNew);
            }
            amortizacion = em.merge(amortizacion);
            if (codigocontraroOld != null && !codigocontraroOld.equals(codigocontraroNew)) {
                codigocontraroOld.getAmortizacionList().remove(amortizacion);
                codigocontraroOld = em.merge(codigocontraroOld);
            }
            if (codigocontraroNew != null && !codigocontraroNew.equals(codigocontraroOld)) {
                codigocontraroNew.getAmortizacionList().add(amortizacion);
                codigocontraroNew = em.merge(codigocontraroNew);
            }
            if (codigousuarioOld != null && !codigousuarioOld.equals(codigousuarioNew)) {
                codigousuarioOld.getAmortizacionList().remove(amortizacion);
                codigousuarioOld = em.merge(codigousuarioOld);
            }
            if (codigousuarioNew != null && !codigousuarioNew.equals(codigousuarioOld)) {
                codigousuarioNew.getAmortizacionList().add(amortizacion);
                codigousuarioNew = em.merge(codigousuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = amortizacion.getCodigo();
                if (findAmortizacion(id) == null) {
                    throw new NonexistentEntityException("The amortizacion with id " + id + " no longer exists.");
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
            Amortizacion amortizacion;
            try {
                amortizacion = em.getReference(Amortizacion.class, id);
                amortizacion.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The amortizacion with id " + id + " no longer exists.", enfe);
            }
            Contrato codigocontraro = amortizacion.getCodigocontraro();
            if (codigocontraro != null) {
                codigocontraro.getAmortizacionList().remove(amortizacion);
                codigocontraro = em.merge(codigocontraro);
            }
            Usuario codigousuario = amortizacion.getCodigousuario();
            if (codigousuario != null) {
                codigousuario.getAmortizacionList().remove(amortizacion);
                codigousuario = em.merge(codigousuario);
            }
            em.remove(amortizacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Amortizacion> findAmortizacionEntities() {
        return findAmortizacionEntities(true, -1, -1);
    }

    public List<Amortizacion> findAmortizacionEntities(int maxResults, int firstResult) {
        return findAmortizacionEntities(false, maxResults, firstResult);
    }

    private List<Amortizacion> findAmortizacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Amortizacion.class));
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

    public Amortizacion findAmortizacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Amortizacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getAmortizacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Amortizacion> rt = cq.from(Amortizacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
