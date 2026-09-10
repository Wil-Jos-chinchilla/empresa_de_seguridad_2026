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
import com.mycompany.empresa_de_seguridad.model.PuntoControl;
import com.mycompany.empresa_de_seguridad.model.RegistroPuntoControl;
import com.mycompany.empresa_de_seguridad.model.Ronda;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

/**
 *
 * @author JOSUE
 */
public class RegistroPuntoControlJpaController implements Serializable {

    public RegistroPuntoControlJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RegistroPuntoControl registroPuntoControl) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PuntoControl idPuntoControl = registroPuntoControl.getIdPuntoControl();
            if (idPuntoControl != null) {
                idPuntoControl = em.getReference(idPuntoControl.getClass(), idPuntoControl.getIdPuntoControl());
                registroPuntoControl.setIdPuntoControl(idPuntoControl);
            }
            Ronda idRonda = registroPuntoControl.getIdRonda();
            if (idRonda != null) {
                idRonda = em.getReference(idRonda.getClass(), idRonda.getIdRonda());
                registroPuntoControl.setIdRonda(idRonda);
            }
            em.persist(registroPuntoControl);
            if (idPuntoControl != null) {
                idPuntoControl.getRegistroPuntoControlList().add(registroPuntoControl);
                idPuntoControl = em.merge(idPuntoControl);
            }
            if (idRonda != null) {
                idRonda.getRegistroPuntoControlList().add(registroPuntoControl);
                idRonda = em.merge(idRonda);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RegistroPuntoControl registroPuntoControl) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RegistroPuntoControl persistentRegistroPuntoControl = em.find(RegistroPuntoControl.class, registroPuntoControl.getIdRegistro());
            PuntoControl idPuntoControlOld = persistentRegistroPuntoControl.getIdPuntoControl();
            PuntoControl idPuntoControlNew = registroPuntoControl.getIdPuntoControl();
            Ronda idRondaOld = persistentRegistroPuntoControl.getIdRonda();
            Ronda idRondaNew = registroPuntoControl.getIdRonda();
            if (idPuntoControlNew != null) {
                idPuntoControlNew = em.getReference(idPuntoControlNew.getClass(), idPuntoControlNew.getIdPuntoControl());
                registroPuntoControl.setIdPuntoControl(idPuntoControlNew);
            }
            if (idRondaNew != null) {
                idRondaNew = em.getReference(idRondaNew.getClass(), idRondaNew.getIdRonda());
                registroPuntoControl.setIdRonda(idRondaNew);
            }
            registroPuntoControl = em.merge(registroPuntoControl);
            if (idPuntoControlOld != null && !idPuntoControlOld.equals(idPuntoControlNew)) {
                idPuntoControlOld.getRegistroPuntoControlList().remove(registroPuntoControl);
                idPuntoControlOld = em.merge(idPuntoControlOld);
            }
            if (idPuntoControlNew != null && !idPuntoControlNew.equals(idPuntoControlOld)) {
                idPuntoControlNew.getRegistroPuntoControlList().add(registroPuntoControl);
                idPuntoControlNew = em.merge(idPuntoControlNew);
            }
            if (idRondaOld != null && !idRondaOld.equals(idRondaNew)) {
                idRondaOld.getRegistroPuntoControlList().remove(registroPuntoControl);
                idRondaOld = em.merge(idRondaOld);
            }
            if (idRondaNew != null && !idRondaNew.equals(idRondaOld)) {
                idRondaNew.getRegistroPuntoControlList().add(registroPuntoControl);
                idRondaNew = em.merge(idRondaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = registroPuntoControl.getIdRegistro();
                if (findRegistroPuntoControl(id) == null) {
                    throw new NonexistentEntityException("The registroPuntoControl with id " + id + " no longer exists.");
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
            RegistroPuntoControl registroPuntoControl;
            try {
                registroPuntoControl = em.getReference(RegistroPuntoControl.class, id);
                registroPuntoControl.getIdRegistro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The registroPuntoControl with id " + id + " no longer exists.", enfe);
            }
            PuntoControl idPuntoControl = registroPuntoControl.getIdPuntoControl();
            if (idPuntoControl != null) {
                idPuntoControl.getRegistroPuntoControlList().remove(registroPuntoControl);
                idPuntoControl = em.merge(idPuntoControl);
            }
            Ronda idRonda = registroPuntoControl.getIdRonda();
            if (idRonda != null) {
                idRonda.getRegistroPuntoControlList().remove(registroPuntoControl);
                idRonda = em.merge(idRonda);
            }
            em.remove(registroPuntoControl);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RegistroPuntoControl> findRegistroPuntoControlEntities() {
        return findRegistroPuntoControlEntities(true, -1, -1);
    }

    public List<RegistroPuntoControl> findRegistroPuntoControlEntities(int maxResults, int firstResult) {
        return findRegistroPuntoControlEntities(false, maxResults, firstResult);
    }

    private List<RegistroPuntoControl> findRegistroPuntoControlEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RegistroPuntoControl.class));
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

    public RegistroPuntoControl findRegistroPuntoControl(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RegistroPuntoControl.class, id);
        } finally {
            em.close();
        }
    }

    public int getRegistroPuntoControlCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RegistroPuntoControl> rt = cq.from(RegistroPuntoControl.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
