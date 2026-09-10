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
import com.mycompany.empresa_de_seguridad.model.PuestoServicio;
import com.mycompany.empresa_de_seguridad.model.PuntoControl;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.empresa_de_seguridad.model.Ronda;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 *
 * @author JOSUE
 */
public class PuestoServicioJpaController implements Serializable {

    public PuestoServicioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PuestoServicio puestoServicio) {
        if (puestoServicio.getPuntoControlList() == null) {
            puestoServicio.setPuntoControlList(new ArrayList<PuntoControl>());
        }
        if (puestoServicio.getRondaList() == null) {
            puestoServicio.setRondaList(new ArrayList<Ronda>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contrato idContrato = puestoServicio.getIdContrato();
            if (idContrato != null) {
                idContrato = em.getReference(idContrato.getClass(), idContrato.getIdContrato());
                puestoServicio.setIdContrato(idContrato);
            }
            List<PuntoControl> attachedPuntoControlList = new ArrayList<PuntoControl>();
            for (PuntoControl puntoControlListPuntoControlToAttach : puestoServicio.getPuntoControlList()) {
                puntoControlListPuntoControlToAttach = em.getReference(puntoControlListPuntoControlToAttach.getClass(), puntoControlListPuntoControlToAttach.getIdPuntoControl());
                attachedPuntoControlList.add(puntoControlListPuntoControlToAttach);
            }
            puestoServicio.setPuntoControlList(attachedPuntoControlList);
            List<Ronda> attachedRondaList = new ArrayList<Ronda>();
            for (Ronda rondaListRondaToAttach : puestoServicio.getRondaList()) {
                rondaListRondaToAttach = em.getReference(rondaListRondaToAttach.getClass(), rondaListRondaToAttach.getIdRonda());
                attachedRondaList.add(rondaListRondaToAttach);
            }
            puestoServicio.setRondaList(attachedRondaList);
            em.persist(puestoServicio);
            if (idContrato != null) {
                idContrato.getPuestoServicioList().add(puestoServicio);
                idContrato = em.merge(idContrato);
            }
            for (PuntoControl puntoControlListPuntoControl : puestoServicio.getPuntoControlList()) {
                PuestoServicio oldIdPuestoOfPuntoControlListPuntoControl = puntoControlListPuntoControl.getIdPuesto();
                puntoControlListPuntoControl.setIdPuesto(puestoServicio);
                puntoControlListPuntoControl = em.merge(puntoControlListPuntoControl);
                if (oldIdPuestoOfPuntoControlListPuntoControl != null) {
                    oldIdPuestoOfPuntoControlListPuntoControl.getPuntoControlList().remove(puntoControlListPuntoControl);
                    oldIdPuestoOfPuntoControlListPuntoControl = em.merge(oldIdPuestoOfPuntoControlListPuntoControl);
                }
            }
            for (Ronda rondaListRonda : puestoServicio.getRondaList()) {
                PuestoServicio oldIdPuestoOfRondaListRonda = rondaListRonda.getIdPuesto();
                rondaListRonda.setIdPuesto(puestoServicio);
                rondaListRonda = em.merge(rondaListRonda);
                if (oldIdPuestoOfRondaListRonda != null) {
                    oldIdPuestoOfRondaListRonda.getRondaList().remove(rondaListRonda);
                    oldIdPuestoOfRondaListRonda = em.merge(oldIdPuestoOfRondaListRonda);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PuestoServicio puestoServicio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PuestoServicio persistentPuestoServicio = em.find(PuestoServicio.class, puestoServicio.getIdPuesto());
            Contrato idContratoOld = persistentPuestoServicio.getIdContrato();
            Contrato idContratoNew = puestoServicio.getIdContrato();
            List<PuntoControl> puntoControlListOld = persistentPuestoServicio.getPuntoControlList();
            List<PuntoControl> puntoControlListNew = puestoServicio.getPuntoControlList();
            List<Ronda> rondaListOld = persistentPuestoServicio.getRondaList();
            List<Ronda> rondaListNew = puestoServicio.getRondaList();
            List<String> illegalOrphanMessages = null;
            for (PuntoControl puntoControlListOldPuntoControl : puntoControlListOld) {
                if (!puntoControlListNew.contains(puntoControlListOldPuntoControl)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PuntoControl " + puntoControlListOldPuntoControl + " since its idPuesto field is not nullable.");
                }
            }
            for (Ronda rondaListOldRonda : rondaListOld) {
                if (!rondaListNew.contains(rondaListOldRonda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ronda " + rondaListOldRonda + " since its idPuesto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idContratoNew != null) {
                idContratoNew = em.getReference(idContratoNew.getClass(), idContratoNew.getIdContrato());
                puestoServicio.setIdContrato(idContratoNew);
            }
            List<PuntoControl> attachedPuntoControlListNew = new ArrayList<PuntoControl>();
            for (PuntoControl puntoControlListNewPuntoControlToAttach : puntoControlListNew) {
                puntoControlListNewPuntoControlToAttach = em.getReference(puntoControlListNewPuntoControlToAttach.getClass(), puntoControlListNewPuntoControlToAttach.getIdPuntoControl());
                attachedPuntoControlListNew.add(puntoControlListNewPuntoControlToAttach);
            }
            puntoControlListNew = attachedPuntoControlListNew;
            puestoServicio.setPuntoControlList(puntoControlListNew);
            List<Ronda> attachedRondaListNew = new ArrayList<Ronda>();
            for (Ronda rondaListNewRondaToAttach : rondaListNew) {
                rondaListNewRondaToAttach = em.getReference(rondaListNewRondaToAttach.getClass(), rondaListNewRondaToAttach.getIdRonda());
                attachedRondaListNew.add(rondaListNewRondaToAttach);
            }
            rondaListNew = attachedRondaListNew;
            puestoServicio.setRondaList(rondaListNew);
            puestoServicio = em.merge(puestoServicio);
            if (idContratoOld != null && !idContratoOld.equals(idContratoNew)) {
                idContratoOld.getPuestoServicioList().remove(puestoServicio);
                idContratoOld = em.merge(idContratoOld);
            }
            if (idContratoNew != null && !idContratoNew.equals(idContratoOld)) {
                idContratoNew.getPuestoServicioList().add(puestoServicio);
                idContratoNew = em.merge(idContratoNew);
            }
            for (PuntoControl puntoControlListNewPuntoControl : puntoControlListNew) {
                if (!puntoControlListOld.contains(puntoControlListNewPuntoControl)) {
                    PuestoServicio oldIdPuestoOfPuntoControlListNewPuntoControl = puntoControlListNewPuntoControl.getIdPuesto();
                    puntoControlListNewPuntoControl.setIdPuesto(puestoServicio);
                    puntoControlListNewPuntoControl = em.merge(puntoControlListNewPuntoControl);
                    if (oldIdPuestoOfPuntoControlListNewPuntoControl != null && !oldIdPuestoOfPuntoControlListNewPuntoControl.equals(puestoServicio)) {
                        oldIdPuestoOfPuntoControlListNewPuntoControl.getPuntoControlList().remove(puntoControlListNewPuntoControl);
                        oldIdPuestoOfPuntoControlListNewPuntoControl = em.merge(oldIdPuestoOfPuntoControlListNewPuntoControl);
                    }
                }
            }
            for (Ronda rondaListNewRonda : rondaListNew) {
                if (!rondaListOld.contains(rondaListNewRonda)) {
                    PuestoServicio oldIdPuestoOfRondaListNewRonda = rondaListNewRonda.getIdPuesto();
                    rondaListNewRonda.setIdPuesto(puestoServicio);
                    rondaListNewRonda = em.merge(rondaListNewRonda);
                    if (oldIdPuestoOfRondaListNewRonda != null && !oldIdPuestoOfRondaListNewRonda.equals(puestoServicio)) {
                        oldIdPuestoOfRondaListNewRonda.getRondaList().remove(rondaListNewRonda);
                        oldIdPuestoOfRondaListNewRonda = em.merge(oldIdPuestoOfRondaListNewRonda);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = puestoServicio.getIdPuesto();
                if (findPuestoServicio(id) == null) {
                    throw new NonexistentEntityException("The puestoServicio with id " + id + " no longer exists.");
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
            PuestoServicio puestoServicio;
            try {
                puestoServicio = em.getReference(PuestoServicio.class, id);
                puestoServicio.getIdPuesto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The puestoServicio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PuntoControl> puntoControlListOrphanCheck = puestoServicio.getPuntoControlList();
            for (PuntoControl puntoControlListOrphanCheckPuntoControl : puntoControlListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PuestoServicio (" + puestoServicio + ") cannot be destroyed since the PuntoControl " + puntoControlListOrphanCheckPuntoControl + " in its puntoControlList field has a non-nullable idPuesto field.");
            }
            List<Ronda> rondaListOrphanCheck = puestoServicio.getRondaList();
            for (Ronda rondaListOrphanCheckRonda : rondaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PuestoServicio (" + puestoServicio + ") cannot be destroyed since the Ronda " + rondaListOrphanCheckRonda + " in its rondaList field has a non-nullable idPuesto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Contrato idContrato = puestoServicio.getIdContrato();
            if (idContrato != null) {
                idContrato.getPuestoServicioList().remove(puestoServicio);
                idContrato = em.merge(idContrato);
            }
            em.remove(puestoServicio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PuestoServicio> findPuestoServicioEntities() {
        return findPuestoServicioEntities(true, -1, -1);
    }

    public List<PuestoServicio> findPuestoServicioEntities(int maxResults, int firstResult) {
        return findPuestoServicioEntities(false, maxResults, firstResult);
    }

    private List<PuestoServicio> findPuestoServicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PuestoServicio.class));
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

    public PuestoServicio findPuestoServicio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PuestoServicio.class, id);
        } finally {
            em.close();
        }
    }

    public int getPuestoServicioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PuestoServicio> rt = cq.from(PuestoServicio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
