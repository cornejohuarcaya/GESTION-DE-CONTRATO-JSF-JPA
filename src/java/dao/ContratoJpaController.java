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
import modelo.Cliente;
import modelo.Usuario;
import modelo.Detallecontrato;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Amortizacion;
import modelo.Contrato;

/**
 *
 * @author erick
 */
public class ContratoJpaController implements Serializable {

    public ContratoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contrato contrato) {
        if (contrato.getDetallecontratoList() == null) {
            contrato.setDetallecontratoList(new ArrayList<Detallecontrato>());
        }
        if (contrato.getAmortizacionList() == null) {
            contrato.setAmortizacionList(new ArrayList<Amortizacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente codigocliente = contrato.getCodigocliente();
            if (codigocliente != null) {
                codigocliente = em.getReference(codigocliente.getClass(), codigocliente.getCodigo());
                contrato.setCodigocliente(codigocliente);
            }
            Usuario codigousuario = contrato.getCodigousuario();
            if (codigousuario != null) {
                codigousuario = em.getReference(codigousuario.getClass(), codigousuario.getCodigo());
                contrato.setCodigousuario(codigousuario);
            }
            List<Detallecontrato> attachedDetallecontratoList = new ArrayList<Detallecontrato>();
            for (Detallecontrato detallecontratoListDetallecontratoToAttach : contrato.getDetallecontratoList()) {
                detallecontratoListDetallecontratoToAttach = em.getReference(detallecontratoListDetallecontratoToAttach.getClass(), detallecontratoListDetallecontratoToAttach.getCodigo());
                attachedDetallecontratoList.add(detallecontratoListDetallecontratoToAttach);
            }
            contrato.setDetallecontratoList(attachedDetallecontratoList);
            List<Amortizacion> attachedAmortizacionList = new ArrayList<Amortizacion>();
            for (Amortizacion amortizacionListAmortizacionToAttach : contrato.getAmortizacionList()) {
                amortizacionListAmortizacionToAttach = em.getReference(amortizacionListAmortizacionToAttach.getClass(), amortizacionListAmortizacionToAttach.getCodigo());
                attachedAmortizacionList.add(amortizacionListAmortizacionToAttach);
            }
            contrato.setAmortizacionList(attachedAmortizacionList);
            em.persist(contrato);
            if (codigocliente != null) {
                codigocliente.getContratoList().add(contrato);
                codigocliente = em.merge(codigocliente);
            }
            if (codigousuario != null) {
                codigousuario.getContratoList().add(contrato);
                codigousuario = em.merge(codigousuario);
            }
            for (Detallecontrato detallecontratoListDetallecontrato : contrato.getDetallecontratoList()) {
                Contrato oldCodigocontratoOfDetallecontratoListDetallecontrato = detallecontratoListDetallecontrato.getCodigocontrato();
                detallecontratoListDetallecontrato.setCodigocontrato(contrato);
                detallecontratoListDetallecontrato = em.merge(detallecontratoListDetallecontrato);
                if (oldCodigocontratoOfDetallecontratoListDetallecontrato != null) {
                    oldCodigocontratoOfDetallecontratoListDetallecontrato.getDetallecontratoList().remove(detallecontratoListDetallecontrato);
                    oldCodigocontratoOfDetallecontratoListDetallecontrato = em.merge(oldCodigocontratoOfDetallecontratoListDetallecontrato);
                }
            }
            for (Amortizacion amortizacionListAmortizacion : contrato.getAmortizacionList()) {
                Contrato oldCodigocontraroOfAmortizacionListAmortizacion = amortizacionListAmortizacion.getCodigocontraro();
                amortizacionListAmortizacion.setCodigocontraro(contrato);
                amortizacionListAmortizacion = em.merge(amortizacionListAmortizacion);
                if (oldCodigocontraroOfAmortizacionListAmortizacion != null) {
                    oldCodigocontraroOfAmortizacionListAmortizacion.getAmortizacionList().remove(amortizacionListAmortizacion);
                    oldCodigocontraroOfAmortizacionListAmortizacion = em.merge(oldCodigocontraroOfAmortizacionListAmortizacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contrato contrato) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contrato persistentContrato = em.find(Contrato.class, contrato.getCodigo());
            Cliente codigoclienteOld = persistentContrato.getCodigocliente();
            Cliente codigoclienteNew = contrato.getCodigocliente();
            Usuario codigousuarioOld = persistentContrato.getCodigousuario();
            Usuario codigousuarioNew = contrato.getCodigousuario();
            List<Detallecontrato> detallecontratoListOld = persistentContrato.getDetallecontratoList();
            List<Detallecontrato> detallecontratoListNew = contrato.getDetallecontratoList();
            List<Amortizacion> amortizacionListOld = persistentContrato.getAmortizacionList();
            List<Amortizacion> amortizacionListNew = contrato.getAmortizacionList();
            if (codigoclienteNew != null) {
                codigoclienteNew = em.getReference(codigoclienteNew.getClass(), codigoclienteNew.getCodigo());
                contrato.setCodigocliente(codigoclienteNew);
            }
            if (codigousuarioNew != null) {
                codigousuarioNew = em.getReference(codigousuarioNew.getClass(), codigousuarioNew.getCodigo());
                contrato.setCodigousuario(codigousuarioNew);
            }
            List<Detallecontrato> attachedDetallecontratoListNew = new ArrayList<Detallecontrato>();
            for (Detallecontrato detallecontratoListNewDetallecontratoToAttach : detallecontratoListNew) {
                detallecontratoListNewDetallecontratoToAttach = em.getReference(detallecontratoListNewDetallecontratoToAttach.getClass(), detallecontratoListNewDetallecontratoToAttach.getCodigo());
                attachedDetallecontratoListNew.add(detallecontratoListNewDetallecontratoToAttach);
            }
            detallecontratoListNew = attachedDetallecontratoListNew;
            contrato.setDetallecontratoList(detallecontratoListNew);
            List<Amortizacion> attachedAmortizacionListNew = new ArrayList<Amortizacion>();
            for (Amortizacion amortizacionListNewAmortizacionToAttach : amortizacionListNew) {
                amortizacionListNewAmortizacionToAttach = em.getReference(amortizacionListNewAmortizacionToAttach.getClass(), amortizacionListNewAmortizacionToAttach.getCodigo());
                attachedAmortizacionListNew.add(amortizacionListNewAmortizacionToAttach);
            }
            amortizacionListNew = attachedAmortizacionListNew;
            contrato.setAmortizacionList(amortizacionListNew);
            contrato = em.merge(contrato);
            if (codigoclienteOld != null && !codigoclienteOld.equals(codigoclienteNew)) {
                codigoclienteOld.getContratoList().remove(contrato);
                codigoclienteOld = em.merge(codigoclienteOld);
            }
            if (codigoclienteNew != null && !codigoclienteNew.equals(codigoclienteOld)) {
                codigoclienteNew.getContratoList().add(contrato);
                codigoclienteNew = em.merge(codigoclienteNew);
            }
            if (codigousuarioOld != null && !codigousuarioOld.equals(codigousuarioNew)) {
                codigousuarioOld.getContratoList().remove(contrato);
                codigousuarioOld = em.merge(codigousuarioOld);
            }
            if (codigousuarioNew != null && !codigousuarioNew.equals(codigousuarioOld)) {
                codigousuarioNew.getContratoList().add(contrato);
                codigousuarioNew = em.merge(codigousuarioNew);
            }
            for (Detallecontrato detallecontratoListOldDetallecontrato : detallecontratoListOld) {
                if (!detallecontratoListNew.contains(detallecontratoListOldDetallecontrato)) {
                    detallecontratoListOldDetallecontrato.setCodigocontrato(null);
                    detallecontratoListOldDetallecontrato = em.merge(detallecontratoListOldDetallecontrato);
                }
            }
            for (Detallecontrato detallecontratoListNewDetallecontrato : detallecontratoListNew) {
                if (!detallecontratoListOld.contains(detallecontratoListNewDetallecontrato)) {
                    Contrato oldCodigocontratoOfDetallecontratoListNewDetallecontrato = detallecontratoListNewDetallecontrato.getCodigocontrato();
                    detallecontratoListNewDetallecontrato.setCodigocontrato(contrato);
                    detallecontratoListNewDetallecontrato = em.merge(detallecontratoListNewDetallecontrato);
                    if (oldCodigocontratoOfDetallecontratoListNewDetallecontrato != null && !oldCodigocontratoOfDetallecontratoListNewDetallecontrato.equals(contrato)) {
                        oldCodigocontratoOfDetallecontratoListNewDetallecontrato.getDetallecontratoList().remove(detallecontratoListNewDetallecontrato);
                        oldCodigocontratoOfDetallecontratoListNewDetallecontrato = em.merge(oldCodigocontratoOfDetallecontratoListNewDetallecontrato);
                    }
                }
            }
            for (Amortizacion amortizacionListOldAmortizacion : amortizacionListOld) {
                if (!amortizacionListNew.contains(amortizacionListOldAmortizacion)) {
                    amortizacionListOldAmortizacion.setCodigocontraro(null);
                    amortizacionListOldAmortizacion = em.merge(amortizacionListOldAmortizacion);
                }
            }
            for (Amortizacion amortizacionListNewAmortizacion : amortizacionListNew) {
                if (!amortizacionListOld.contains(amortizacionListNewAmortizacion)) {
                    Contrato oldCodigocontraroOfAmortizacionListNewAmortizacion = amortizacionListNewAmortizacion.getCodigocontraro();
                    amortizacionListNewAmortizacion.setCodigocontraro(contrato);
                    amortizacionListNewAmortizacion = em.merge(amortizacionListNewAmortizacion);
                    if (oldCodigocontraroOfAmortizacionListNewAmortizacion != null && !oldCodigocontraroOfAmortizacionListNewAmortizacion.equals(contrato)) {
                        oldCodigocontraroOfAmortizacionListNewAmortizacion.getAmortizacionList().remove(amortizacionListNewAmortizacion);
                        oldCodigocontraroOfAmortizacionListNewAmortizacion = em.merge(oldCodigocontraroOfAmortizacionListNewAmortizacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = contrato.getCodigo();
                if (findContrato(id) == null) {
                    throw new NonexistentEntityException("The contrato with id " + id + " no longer exists.");
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
            Contrato contrato;
            try {
                contrato = em.getReference(Contrato.class, id);
                contrato.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contrato with id " + id + " no longer exists.", enfe);
            }
            Cliente codigocliente = contrato.getCodigocliente();
            if (codigocliente != null) {
                codigocliente.getContratoList().remove(contrato);
                codigocliente = em.merge(codigocliente);
            }
            Usuario codigousuario = contrato.getCodigousuario();
            if (codigousuario != null) {
                codigousuario.getContratoList().remove(contrato);
                codigousuario = em.merge(codigousuario);
            }
            List<Detallecontrato> detallecontratoList = contrato.getDetallecontratoList();
            for (Detallecontrato detallecontratoListDetallecontrato : detallecontratoList) {
                detallecontratoListDetallecontrato.setCodigocontrato(null);
                detallecontratoListDetallecontrato = em.merge(detallecontratoListDetallecontrato);
            }
            List<Amortizacion> amortizacionList = contrato.getAmortizacionList();
            for (Amortizacion amortizacionListAmortizacion : amortizacionList) {
                amortizacionListAmortizacion.setCodigocontraro(null);
                amortizacionListAmortizacion = em.merge(amortizacionListAmortizacion);
            }
            em.remove(contrato);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Contrato> findContratoEntities() {
        return findContratoEntities(true, -1, -1);
    }

    public List<Contrato> findContratoEntities(int maxResults, int firstResult) {
        return findContratoEntities(false, maxResults, firstResult);
    }

    private List<Contrato> findContratoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contrato.class));
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

    public Contrato findContrato(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contrato.class, id);
        } finally {
            em.close();
        }
    }

    public int getContratoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contrato> rt = cq.from(Contrato.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
