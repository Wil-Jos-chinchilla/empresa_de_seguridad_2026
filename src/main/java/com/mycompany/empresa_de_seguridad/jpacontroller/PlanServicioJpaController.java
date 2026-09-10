/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.empresa_de_seguridad.jpacontroller;

import com.mycompany.empresa_de_seguridad.jpacontroller.exceptions.IllegalOrphanException;
import com.mycompany.empresa_de_seguridad.jpacontroller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import com.mycompany.empresa_de_seguridad.model.Contrato;
import com.mycompany.empresa_de_seguridad.model.PlanServicio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JOSUE
 */
public class PlanServicioJpaController implements Serializable {

    public PlanServicioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PlanServicio planServicio) {
        if (planServicio.getContratoList() == null) {
            planServicio.setContratoList(new ArrayList<Contrato>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Contrato> attachedContratoList = new ArrayList<Contrato>();
            for (Contrato contratoListContratoToAttach : planServicio.getContratoList()) {
                contratoListContratoToAttach = em.getReference(contratoListContratoToAttach.getClass(), contratoListContratoToAttach.getIdContrato());
                attachedContratoList.add(contratoListContratoToAttach);
            }
            planServicio.setContratoList(attachedContratoList);
            em.persist(planServicio);
            for (Contrato contratoListContrato : planServicio.getContratoList()) {
                PlanServicio oldIdPlanOfContratoListContrato = contratoListContrato.getIdPlan();
                contratoListContrato.setIdPlan(planServicio);
                contratoListContrato = em.merge(contratoListContrato);
                if (oldIdPlanOfContratoListContrato != null) {
                    oldIdPlanOfContratoListContrato.getContratoList().remove(contratoListContrato);
                    oldIdPlanOfContratoListContrato = em.merge(oldIdPlanOfContratoListContrato);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PlanServicio planServicio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PlanServicio persistentPlanServicio = em.find(PlanServicio.class, planServicio.getIdPlan());
            List<Contrato> contratoListOld = persistentPlanServicio.getContratoList();
            List<Contrato> contratoListNew = planServicio.getContratoList();
            List<String> illegalOrphanMessages = null;
            for (Contrato contratoListOldContrato : contratoListOld) {
                if (!contratoListNew.contains(contratoListOldContrato)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contrato " + contratoListOldContrato + " since its idPlan field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Contrato> attachedContratoListNew = new ArrayList<Contrato>();
            for (Contrato contratoListNewContratoToAttach : contratoListNew) {
                contratoListNewContratoToAttach = em.getReference(contratoListNewContratoToAttach.getClass(), contratoListNewContratoToAttach.getIdContrato());
                attachedContratoListNew.add(contratoListNewContratoToAttach);
            }
            contratoListNew = attachedContratoListNew;
            planServicio.setContratoList(contratoListNew);
            planServicio = em.merge(planServicio);
            for (Contrato contratoListNewContrato : contratoListNew) {
                if (!contratoListOld.contains(contratoListNewContrato)) {
                    PlanServicio oldIdPlanOfContratoListNewContrato = contratoListNewContrato.getIdPlan();
                    contratoListNewContrato.setIdPlan(planServicio);
                    contratoListNewContrato = em.merge(contratoListNewContrato);
                    if (oldIdPlanOfContratoListNewContrato != null && !oldIdPlanOfContratoListNewContrato.equals(planServicio)) {
                        oldIdPlanOfContratoListNewContrato.getContratoList().remove(contratoListNewContrato);
                        oldIdPlanOfContratoListNewContrato = em.merge(oldIdPlanOfContratoListNewContrato);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = planServicio.getIdPlan();
                if (findPlanServicio(id) == null) {
                    throw new NonexistentEntityException("The planServicio with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PlanServicio planServicio;
            try {
                planServicio = em.getReference(PlanServicio.class, id);
                planServicio.getIdPlan();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The planServicio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Contrato> contratoListOrphanCheck = planServicio.getContratoList();
            for (Contrato contratoListOrphanCheckContrato : contratoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PlanServicio (" + planServicio + ") cannot be destroyed since the Contrato " + contratoListOrphanCheckContrato + " in its contratoList field has a non-nullable idPlan field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(planServicio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PlanServicio> findPlanServicioEntities() {
        return findPlanServicioEntities(true, -1, -1);
    }

    public List<PlanServicio> findPlanServicioEntities(int maxResults, int firstResult) {
        return findPlanServicioEntities(false, maxResults, firstResult);
    }

    private List<PlanServicio> findPlanServicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PlanServicio.class));
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

    public PlanServicio findPlanServicio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlanServicio.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlanServicioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PlanServicio> rt = cq.from(PlanServicio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
