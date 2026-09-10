/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.empresa_de_seguridad.jpacontroller;

import com.mycompany.empresa_de_seguridad.jpacontroller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import com.mycompany.empresa_de_seguridad.model.AgenteSeguridad;
import com.mycompany.empresa_de_seguridad.model.AsignacionAgente;
import com.mycompany.empresa_de_seguridad.model.Contrato;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

/**
 *
 * @author JOSUE
 */
public class AsignacionAgenteJpaController implements Serializable {

    public AsignacionAgenteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AsignacionAgente asignacionAgente) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AgenteSeguridad idAgente = asignacionAgente.getIdAgente();
            if (idAgente != null) {
                idAgente = em.getReference(idAgente.getClass(), idAgente.getIdAgente());
                asignacionAgente.setIdAgente(idAgente);
            }
            Contrato idContrato = asignacionAgente.getIdContrato();
            if (idContrato != null) {
                idContrato = em.getReference(idContrato.getClass(), idContrato.getIdContrato());
                asignacionAgente.setIdContrato(idContrato);
            }
            em.persist(asignacionAgente);
            if (idAgente != null) {
                idAgente.getAsignacionAgenteList().add(asignacionAgente);
                idAgente = em.merge(idAgente);
            }
            if (idContrato != null) {
                idContrato.getAsignacionAgenteList().add(asignacionAgente);
                idContrato = em.merge(idContrato);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AsignacionAgente asignacionAgente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AsignacionAgente persistentAsignacionAgente = em.find(AsignacionAgente.class, asignacionAgente.getIdAsignacion());
            AgenteSeguridad idAgenteOld = persistentAsignacionAgente.getIdAgente();
            AgenteSeguridad idAgenteNew = asignacionAgente.getIdAgente();
            Contrato idContratoOld = persistentAsignacionAgente.getIdContrato();
            Contrato idContratoNew = asignacionAgente.getIdContrato();
            if (idAgenteNew != null) {
                idAgenteNew = em.getReference(idAgenteNew.getClass(), idAgenteNew.getIdAgente());
                asignacionAgente.setIdAgente(idAgenteNew);
            }
            if (idContratoNew != null) {
                idContratoNew = em.getReference(idContratoNew.getClass(), idContratoNew.getIdContrato());
                asignacionAgente.setIdContrato(idContratoNew);
            }
            asignacionAgente = em.merge(asignacionAgente);
            if (idAgenteOld != null && !idAgenteOld.equals(idAgenteNew)) {
                idAgenteOld.getAsignacionAgenteList().remove(asignacionAgente);
                idAgenteOld = em.merge(idAgenteOld);
            }
            if (idAgenteNew != null && !idAgenteNew.equals(idAgenteOld)) {
                idAgenteNew.getAsignacionAgenteList().add(asignacionAgente);
                idAgenteNew = em.merge(idAgenteNew);
            }
            if (idContratoOld != null && !idContratoOld.equals(idContratoNew)) {
                idContratoOld.getAsignacionAgenteList().remove(asignacionAgente);
                idContratoOld = em.merge(idContratoOld);
            }
            if (idContratoNew != null && !idContratoNew.equals(idContratoOld)) {
                idContratoNew.getAsignacionAgenteList().add(asignacionAgente);
                idContratoNew = em.merge(idContratoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = asignacionAgente.getIdAsignacion();
                if (findAsignacionAgente(id) == null) {
                    throw new NonexistentEntityException("The asignacionAgente with id " + id + " no longer exists.");
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
            AsignacionAgente asignacionAgente;
            try {
                asignacionAgente = em.getReference(AsignacionAgente.class, id);
                asignacionAgente.getIdAsignacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asignacionAgente with id " + id + " no longer exists.", enfe);
            }
            AgenteSeguridad idAgente = asignacionAgente.getIdAgente();
            if (idAgente != null) {
                idAgente.getAsignacionAgenteList().remove(asignacionAgente);
                idAgente = em.merge(idAgente);
            }
            Contrato idContrato = asignacionAgente.getIdContrato();
            if (idContrato != null) {
                idContrato.getAsignacionAgenteList().remove(asignacionAgente);
                idContrato = em.merge(idContrato);
            }
            em.remove(asignacionAgente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AsignacionAgente> findAsignacionAgenteEntities() {
        return findAsignacionAgenteEntities(true, -1, -1);
    }

    public List<AsignacionAgente> findAsignacionAgenteEntities(int maxResults, int firstResult) {
        return findAsignacionAgenteEntities(false, maxResults, firstResult);
    }

    private List<AsignacionAgente> findAsignacionAgenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AsignacionAgente.class));
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

    public AsignacionAgente findAsignacionAgente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AsignacionAgente.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsignacionAgenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AsignacionAgente> rt = cq.from(AsignacionAgente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
