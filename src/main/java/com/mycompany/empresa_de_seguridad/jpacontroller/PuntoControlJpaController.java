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
import com.mycompany.empresa_de_seguridad.model.PuestoServicio;
import com.mycompany.empresa_de_seguridad.model.PuntoControl;
import com.mycompany.empresa_de_seguridad.model.RegistroPuntoControl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JOSUE
 */
public class PuntoControlJpaController implements Serializable {

    public PuntoControlJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PuntoControl puntoControl) {
        if (puntoControl.getRegistroPuntoControlList() == null) {
            puntoControl.setRegistroPuntoControlList(new ArrayList<RegistroPuntoControl>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PuestoServicio idPuesto = puntoControl.getIdPuesto();
            if (idPuesto != null) {
                idPuesto = em.getReference(idPuesto.getClass(), idPuesto.getIdPuesto());
                puntoControl.setIdPuesto(idPuesto);
            }
            List<RegistroPuntoControl> attachedRegistroPuntoControlList = new ArrayList<RegistroPuntoControl>();
            for (RegistroPuntoControl registroPuntoControlListRegistroPuntoControlToAttach : puntoControl.getRegistroPuntoControlList()) {
                registroPuntoControlListRegistroPuntoControlToAttach = em.getReference(registroPuntoControlListRegistroPuntoControlToAttach.getClass(), registroPuntoControlListRegistroPuntoControlToAttach.getIdRegistro());
                attachedRegistroPuntoControlList.add(registroPuntoControlListRegistroPuntoControlToAttach);
            }
            puntoControl.setRegistroPuntoControlList(attachedRegistroPuntoControlList);
            em.persist(puntoControl);
            if (idPuesto != null) {
                idPuesto.getPuntoControlList().add(puntoControl);
                idPuesto = em.merge(idPuesto);
            }
            for (RegistroPuntoControl registroPuntoControlListRegistroPuntoControl : puntoControl.getRegistroPuntoControlList()) {
                PuntoControl oldIdPuntoControlOfRegistroPuntoControlListRegistroPuntoControl = registroPuntoControlListRegistroPuntoControl.getIdPuntoControl();
                registroPuntoControlListRegistroPuntoControl.setIdPuntoControl(puntoControl);
                registroPuntoControlListRegistroPuntoControl = em.merge(registroPuntoControlListRegistroPuntoControl);
                if (oldIdPuntoControlOfRegistroPuntoControlListRegistroPuntoControl != null) {
                    oldIdPuntoControlOfRegistroPuntoControlListRegistroPuntoControl.getRegistroPuntoControlList().remove(registroPuntoControlListRegistroPuntoControl);
                    oldIdPuntoControlOfRegistroPuntoControlListRegistroPuntoControl = em.merge(oldIdPuntoControlOfRegistroPuntoControlListRegistroPuntoControl);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PuntoControl puntoControl) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PuntoControl persistentPuntoControl = em.find(PuntoControl.class, puntoControl.getIdPuntoControl());
            PuestoServicio idPuestoOld = persistentPuntoControl.getIdPuesto();
            PuestoServicio idPuestoNew = puntoControl.getIdPuesto();
            List<RegistroPuntoControl> registroPuntoControlListOld = persistentPuntoControl.getRegistroPuntoControlList();
            List<RegistroPuntoControl> registroPuntoControlListNew = puntoControl.getRegistroPuntoControlList();
            List<String> illegalOrphanMessages = null;
            for (RegistroPuntoControl registroPuntoControlListOldRegistroPuntoControl : registroPuntoControlListOld) {
                if (!registroPuntoControlListNew.contains(registroPuntoControlListOldRegistroPuntoControl)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RegistroPuntoControl " + registroPuntoControlListOldRegistroPuntoControl + " since its idPuntoControl field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPuestoNew != null) {
                idPuestoNew = em.getReference(idPuestoNew.getClass(), idPuestoNew.getIdPuesto());
                puntoControl.setIdPuesto(idPuestoNew);
            }
            List<RegistroPuntoControl> attachedRegistroPuntoControlListNew = new ArrayList<RegistroPuntoControl>();
            for (RegistroPuntoControl registroPuntoControlListNewRegistroPuntoControlToAttach : registroPuntoControlListNew) {
                registroPuntoControlListNewRegistroPuntoControlToAttach = em.getReference(registroPuntoControlListNewRegistroPuntoControlToAttach.getClass(), registroPuntoControlListNewRegistroPuntoControlToAttach.getIdRegistro());
                attachedRegistroPuntoControlListNew.add(registroPuntoControlListNewRegistroPuntoControlToAttach);
            }
            registroPuntoControlListNew = attachedRegistroPuntoControlListNew;
            puntoControl.setRegistroPuntoControlList(registroPuntoControlListNew);
            puntoControl = em.merge(puntoControl);
            if (idPuestoOld != null && !idPuestoOld.equals(idPuestoNew)) {
                idPuestoOld.getPuntoControlList().remove(puntoControl);
                idPuestoOld = em.merge(idPuestoOld);
            }
            if (idPuestoNew != null && !idPuestoNew.equals(idPuestoOld)) {
                idPuestoNew.getPuntoControlList().add(puntoControl);
                idPuestoNew = em.merge(idPuestoNew);
            }
            for (RegistroPuntoControl registroPuntoControlListNewRegistroPuntoControl : registroPuntoControlListNew) {
                if (!registroPuntoControlListOld.contains(registroPuntoControlListNewRegistroPuntoControl)) {
                    PuntoControl oldIdPuntoControlOfRegistroPuntoControlListNewRegistroPuntoControl = registroPuntoControlListNewRegistroPuntoControl.getIdPuntoControl();
                    registroPuntoControlListNewRegistroPuntoControl.setIdPuntoControl(puntoControl);
                    registroPuntoControlListNewRegistroPuntoControl = em.merge(registroPuntoControlListNewRegistroPuntoControl);
                    if (oldIdPuntoControlOfRegistroPuntoControlListNewRegistroPuntoControl != null && !oldIdPuntoControlOfRegistroPuntoControlListNewRegistroPuntoControl.equals(puntoControl)) {
                        oldIdPuntoControlOfRegistroPuntoControlListNewRegistroPuntoControl.getRegistroPuntoControlList().remove(registroPuntoControlListNewRegistroPuntoControl);
                        oldIdPuntoControlOfRegistroPuntoControlListNewRegistroPuntoControl = em.merge(oldIdPuntoControlOfRegistroPuntoControlListNewRegistroPuntoControl);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = puntoControl.getIdPuntoControl();
                if (findPuntoControl(id) == null) {
                    throw new NonexistentEntityException("The puntoControl with id " + id + " no longer exists.");
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
            PuntoControl puntoControl;
            try {
                puntoControl = em.getReference(PuntoControl.class, id);
                puntoControl.getIdPuntoControl();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The puntoControl with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RegistroPuntoControl> registroPuntoControlListOrphanCheck = puntoControl.getRegistroPuntoControlList();
            for (RegistroPuntoControl registroPuntoControlListOrphanCheckRegistroPuntoControl : registroPuntoControlListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PuntoControl (" + puntoControl + ") cannot be destroyed since the RegistroPuntoControl " + registroPuntoControlListOrphanCheckRegistroPuntoControl + " in its registroPuntoControlList field has a non-nullable idPuntoControl field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            PuestoServicio idPuesto = puntoControl.getIdPuesto();
            if (idPuesto != null) {
                idPuesto.getPuntoControlList().remove(puntoControl);
                idPuesto = em.merge(idPuesto);
            }
            em.remove(puntoControl);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PuntoControl> findPuntoControlEntities() {
        return findPuntoControlEntities(true, -1, -1);
    }

    public List<PuntoControl> findPuntoControlEntities(int maxResults, int firstResult) {
        return findPuntoControlEntities(false, maxResults, firstResult);
    }

    private List<PuntoControl> findPuntoControlEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PuntoControl.class));
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

    public PuntoControl findPuntoControl(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PuntoControl.class, id);
        } finally {
            em.close();
        }
    }

    public int getPuntoControlCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PuntoControl> rt = cq.from(PuntoControl.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
