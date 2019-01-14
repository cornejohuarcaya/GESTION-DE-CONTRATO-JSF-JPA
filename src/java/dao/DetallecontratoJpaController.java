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
import modelo.Contrato;
import modelo.Detallecontrato;

/**
 *
 * @author erick
 */
public class DetallecontratoJpaController implements Serializable {

    public DetallecontratoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallecontrato detallecontrato) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contrato codigocontrato = detallecontrato.getCodigocontrato();
            if (codigocontrato != null) {
                codigocontrato = em.getReference(codigocontrato.getClass(), codigocontrato.getCodigo());
                detallecontrato.setCodigocontrato(codigocontrato);
            }
            em.persist(detallecontrato);
            if (codigocontrato != null) {
                codigocontrato.getDetallecontratoList().add(detallecontrato);
                codigocontrato = em.merge(codigocontrato);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detallecontrato detallecontrato) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detallecontrato persistentDetallecontrato = em.find(Detallecontrato.class, detallecontrato.getCodigo());
            Contrato codigocontratoOld = persistentDetallecontrato.getCodigocontrato();
            Contrato codigocontratoNew = detallecontrato.getCodigocontrato();
            if (codigocontratoNew != null) {
                codigocontratoNew = em.getReference(codigocontratoNew.getClass(), codigocontratoNew.getCodigo());
                detallecontrato.setCodigocontrato(codigocontratoNew);
            }
            detallecontrato = em.merge(detallecontrato);
            if (codigocontratoOld != null && !codigocontratoOld.equals(codigocontratoNew)) {
                codigocontratoOld.getDetallecontratoList().remove(detallecontrato);
                codigocontratoOld = em.merge(codigocontratoOld);
            }
            if (codigocontratoNew != null && !codigocontratoNew.equals(codigocontratoOld)) {
                codigocontratoNew.getDetallecontratoList().add(detallecontrato);
                codigocontratoNew = em.merge(codigocontratoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detallecontrato.getCodigo();
                if (findDetallecontrato(id) == null) {
                    throw new NonexistentEntityException("The detallecontrato with id " + id + " no longer exists.");
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
            Detallecontrato detallecontrato;
            try {
                detallecontrato = em.getReference(Detallecontrato.class, id);
                detallecontrato.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallecontrato with id " + id + " no longer exists.", enfe);
            }
            Contrato codigocontrato = detallecontrato.getCodigocontrato();
            if (codigocontrato != null) {
                codigocontrato.getDetallecontratoList().remove(detallecontrato);
                codigocontrato = em.merge(codigocontrato);
            }
            em.remove(detallecontrato);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detallecontrato> findDetallecontratoEntities() {
        return findDetallecontratoEntities(true, -1, -1);
    }

    public List<Detallecontrato> findDetallecontratoEntities(int maxResults, int firstResult) {
        return findDetallecontratoEntities(false, maxResults, firstResult);
    }

    private List<Detallecontrato> findDetallecontratoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallecontrato.class));
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

    public Detallecontrato findDetallecontrato(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallecontrato.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallecontratoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallecontrato> rt = cq.from(Detallecontrato.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
