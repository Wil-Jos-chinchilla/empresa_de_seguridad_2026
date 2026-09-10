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
import com.mycompany.empresa_de_seguridad.model.Contrato;
import com.mycompany.empresa_de_seguridad.model.Incidente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

/**
 *
 * @author JOSUE
 */
public class IncidenteJpaController implements Serializable {

    public IncidenteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Incidente incidente) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AgenteSeguridad idAgente = incidente.getIdAgente();
            if (idAgente != null) {
                idAgente = em.getReference(idAgente.getClass(), idAgente.getIdAgente());
                incidente.setIdAgente(idAgente);
            }
            Contrato idContrato = incidente.getIdContrato();
            if (idContrato != null) {
                idContrato = em.getReference(idContrato.getClass(), idContrato.getIdContrato());
                incidente.setIdContrato(idContrato);
            }
            em.persist(incidente);
            if (idAgente != null) {
                idAgente.getIncidenteList().add(incidente);
                idAgente = em.merge(idAgente);
            }
            if (idContrato != null) {
                idContrato.getIncidenteList().add(incidente);
                idContrato = em.merge(idContrato);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Incidente incidente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Incidente persistentIncidente = em.find(Incidente.class, incidente.getIdIncidente());
            AgenteSeguridad idAgenteOld = persistentIncidente.getIdAgente();
            AgenteSeguridad idAgenteNew = incidente.getIdAgente();
            Contrato idContratoOld = persistentIncidente.getIdContrato();
            Contrato idContratoNew = incidente.getIdContrato();
            if (idAgenteNew != null) {
                idAgenteNew = em.getReference(idAgenteNew.getClass(), idAgenteNew.getIdAgente());
                incidente.setIdAgente(idAgenteNew);
            }
            if (idContratoNew != null) {
                idContratoNew = em.getReference(idContratoNew.getClass(), idContratoNew.getIdContrato());
                incidente.setIdContrato(idContratoNew);
            }
            incidente = em.merge(incidente);
            if (idAgenteOld != null && !idAgenteOld.equals(idAgenteNew)) {
                idAgenteOld.getIncidenteList().remove(incidente);
                idAgenteOld = em.merge(idAgenteOld);
            }
            if (idAgenteNew != null && !idAgenteNew.equals(idAgenteOld)) {
                idAgenteNew.getIncidenteList().add(incidente);
                idAgenteNew = em.merge(idAgenteNew);
            }
            if (idContratoOld != null && !idContratoOld.equals(idContratoNew)) {
                idContratoOld.getIncidenteList().remove(incidente);
                idContratoOld = em.merge(idContratoOld);
            }
            if (idContratoNew != null && !idContratoNew.equals(idContratoOld)) {
                idContratoNew.getIncidenteList().add(incidente);
                idContratoNew = em.merge(idContratoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = incidente.getIdIncidente();
                if (findIncidente(id) == null) {
                    throw new NonexistentEntityException("The incidente with id " + id + " no longer exists.");
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
            Incidente incidente;
            try {
                incidente = em.getReference(Incidente.class, id);
                incidente.getIdIncidente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The incidente with id " + id + " no longer exists.", enfe);
            }
            AgenteSeguridad idAgente = incidente.getIdAgente();
            if (idAgente != null) {
                idAgente.getIncidenteList().remove(incidente);
                idAgente = em.merge(idAgente);
            }
            Contrato idContrato = incidente.getIdContrato();
            if (idContrato != null) {
                idContrato.getIncidenteList().remove(incidente);
                idContrato = em.merge(idContrato);
            }
            em.remove(incidente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Incidente> findIncidenteEntities() {
        return findIncidenteEntities(true, -1, -1);
    }

    public List<Incidente> findIncidenteEntities(int maxResults, int firstResult) {
        return findIncidenteEntities(false, maxResults, firstResult);
    }

    private List<Incidente> findIncidenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Incidente.class));
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

    public Incidente findIncidente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Incidente.class, id);
        } finally {
            em.close();
        }
    }

    public int getIncidenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Incidente> rt = cq.from(Incidente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
