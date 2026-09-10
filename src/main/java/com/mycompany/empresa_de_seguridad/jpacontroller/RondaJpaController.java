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
import com.mycompany.empresa_de_seguridad.model.AgenteSeguridad;
import com.mycompany.empresa_de_seguridad.model.PuestoServicio;
import com.mycompany.empresa_de_seguridad.model.RegistroPuntoControl;
import com.mycompany.empresa_de_seguridad.model.Ronda;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JOSUE
 */
public class RondaJpaController implements Serializable {

    public RondaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ronda ronda) {
        if (ronda.getRegistroPuntoControlList() == null) {
            ronda.setRegistroPuntoControlList(new ArrayList<RegistroPuntoControl>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AgenteSeguridad idAgente = ronda.getIdAgente();
            if (idAgente != null) {
                idAgente = em.getReference(idAgente.getClass(), idAgente.getIdAgente());
                ronda.setIdAgente(idAgente);
            }
            PuestoServicio idPuesto = ronda.getIdPuesto();
            if (idPuesto != null) {
                idPuesto = em.getReference(idPuesto.getClass(), idPuesto.getIdPuesto());
                ronda.setIdPuesto(idPuesto);
            }
            List<RegistroPuntoControl> attachedRegistroPuntoControlList = new ArrayList<RegistroPuntoControl>();
            for (RegistroPuntoControl registroPuntoControlListRegistroPuntoControlToAttach : ronda.getRegistroPuntoControlList()) {
                registroPuntoControlListRegistroPuntoControlToAttach = em.getReference(registroPuntoControlListRegistroPuntoControlToAttach.getClass(), registroPuntoControlListRegistroPuntoControlToAttach.getIdRegistro());
                attachedRegistroPuntoControlList.add(registroPuntoControlListRegistroPuntoControlToAttach);
            }
            ronda.setRegistroPuntoControlList(attachedRegistroPuntoControlList);
            em.persist(ronda);
            if (idAgente != null) {
                idAgente.getRondaList().add(ronda);
                idAgente = em.merge(idAgente);
            }
            if (idPuesto != null) {
                idPuesto.getRondaList().add(ronda);
                idPuesto = em.merge(idPuesto);
            }
            for (RegistroPuntoControl registroPuntoControlListRegistroPuntoControl : ronda.getRegistroPuntoControlList()) {
                Ronda oldIdRondaOfRegistroPuntoControlListRegistroPuntoControl = registroPuntoControlListRegistroPuntoControl.getIdRonda();
                registroPuntoControlListRegistroPuntoControl.setIdRonda(ronda);
                registroPuntoControlListRegistroPuntoControl = em.merge(registroPuntoControlListRegistroPuntoControl);
                if (oldIdRondaOfRegistroPuntoControlListRegistroPuntoControl != null) {
                    oldIdRondaOfRegistroPuntoControlListRegistroPuntoControl.getRegistroPuntoControlList().remove(registroPuntoControlListRegistroPuntoControl);
                    oldIdRondaOfRegistroPuntoControlListRegistroPuntoControl = em.merge(oldIdRondaOfRegistroPuntoControlListRegistroPuntoControl);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ronda ronda) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ronda persistentRonda = em.find(Ronda.class, ronda.getIdRonda());
            AgenteSeguridad idAgenteOld = persistentRonda.getIdAgente();
            AgenteSeguridad idAgenteNew = ronda.getIdAgente();
            PuestoServicio idPuestoOld = persistentRonda.getIdPuesto();
            PuestoServicio idPuestoNew = ronda.getIdPuesto();
            List<RegistroPuntoControl> registroPuntoControlListOld = persistentRonda.getRegistroPuntoControlList();
            List<RegistroPuntoControl> registroPuntoControlListNew = ronda.getRegistroPuntoControlList();
            List<String> illegalOrphanMessages = null;
            for (RegistroPuntoControl registroPuntoControlListOldRegistroPuntoControl : registroPuntoControlListOld) {
                if (!registroPuntoControlListNew.contains(registroPuntoControlListOldRegistroPuntoControl)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RegistroPuntoControl " + registroPuntoControlListOldRegistroPuntoControl + " since its idRonda field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idAgenteNew != null) {
                idAgenteNew = em.getReference(idAgenteNew.getClass(), idAgenteNew.getIdAgente());
                ronda.setIdAgente(idAgenteNew);
            }
            if (idPuestoNew != null) {
                idPuestoNew = em.getReference(idPuestoNew.getClass(), idPuestoNew.getIdPuesto());
                ronda.setIdPuesto(idPuestoNew);
            }
            List<RegistroPuntoControl> attachedRegistroPuntoControlListNew = new ArrayList<RegistroPuntoControl>();
            for (RegistroPuntoControl registroPuntoControlListNewRegistroPuntoControlToAttach : registroPuntoControlListNew) {
                registroPuntoControlListNewRegistroPuntoControlToAttach = em.getReference(registroPuntoControlListNewRegistroPuntoControlToAttach.getClass(), registroPuntoControlListNewRegistroPuntoControlToAttach.getIdRegistro());
                attachedRegistroPuntoControlListNew.add(registroPuntoControlListNewRegistroPuntoControlToAttach);
            }
            registroPuntoControlListNew = attachedRegistroPuntoControlListNew;
            ronda.setRegistroPuntoControlList(registroPuntoControlListNew);
            ronda = em.merge(ronda);
            if (idAgenteOld != null && !idAgenteOld.equals(idAgenteNew)) {
                idAgenteOld.getRondaList().remove(ronda);
                idAgenteOld = em.merge(idAgenteOld);
            }
            if (idAgenteNew != null && !idAgenteNew.equals(idAgenteOld)) {
                idAgenteNew.getRondaList().add(ronda);
                idAgenteNew = em.merge(idAgenteNew);
            }
            if (idPuestoOld != null && !idPuestoOld.equals(idPuestoNew)) {
                idPuestoOld.getRondaList().remove(ronda);
                idPuestoOld = em.merge(idPuestoOld);
            }
            if (idPuestoNew != null && !idPuestoNew.equals(idPuestoOld)) {
                idPuestoNew.getRondaList().add(ronda);
                idPuestoNew = em.merge(idPuestoNew);
            }
            for (RegistroPuntoControl registroPuntoControlListNewRegistroPuntoControl : registroPuntoControlListNew) {
                if (!registroPuntoControlListOld.contains(registroPuntoControlListNewRegistroPuntoControl)) {
                    Ronda oldIdRondaOfRegistroPuntoControlListNewRegistroPuntoControl = registroPuntoControlListNewRegistroPuntoControl.getIdRonda();
                    registroPuntoControlListNewRegistroPuntoControl.setIdRonda(ronda);
                    registroPuntoControlListNewRegistroPuntoControl = em.merge(registroPuntoControlListNewRegistroPuntoControl);
                    if (oldIdRondaOfRegistroPuntoControlListNewRegistroPuntoControl != null && !oldIdRondaOfRegistroPuntoControlListNewRegistroPuntoControl.equals(ronda)) {
                        oldIdRondaOfRegistroPuntoControlListNewRegistroPuntoControl.getRegistroPuntoControlList().remove(registroPuntoControlListNewRegistroPuntoControl);
                        oldIdRondaOfRegistroPuntoControlListNewRegistroPuntoControl = em.merge(oldIdRondaOfRegistroPuntoControlListNewRegistroPuntoControl);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ronda.getIdRonda();
                if (findRonda(id) == null) {
                    throw new NonexistentEntityException("The ronda with id " + id + " no longer exists.");
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
            Ronda ronda;
            try {
                ronda = em.getReference(Ronda.class, id);
                ronda.getIdRonda();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ronda with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RegistroPuntoControl> registroPuntoControlListOrphanCheck = ronda.getRegistroPuntoControlList();
            for (RegistroPuntoControl registroPuntoControlListOrphanCheckRegistroPuntoControl : registroPuntoControlListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ronda (" + ronda + ") cannot be destroyed since the RegistroPuntoControl " + registroPuntoControlListOrphanCheckRegistroPuntoControl + " in its registroPuntoControlList field has a non-nullable idRonda field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            AgenteSeguridad idAgente = ronda.getIdAgente();
            if (idAgente != null) {
                idAgente.getRondaList().remove(ronda);
                idAgente = em.merge(idAgente);
            }
            PuestoServicio idPuesto = ronda.getIdPuesto();
            if (idPuesto != null) {
                idPuesto.getRondaList().remove(ronda);
                idPuesto = em.merge(idPuesto);
            }
            em.remove(ronda);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ronda> findRondaEntities() {
        return findRondaEntities(true, -1, -1);
    }

    public List<Ronda> findRondaEntities(int maxResults, int firstResult) {
        return findRondaEntities(false, maxResults, firstResult);
    }

    private List<Ronda> findRondaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ronda.class));
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

    public Ronda findRonda(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ronda.class, id);
        } finally {
            em.close();
        }
    }

    public int getRondaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ronda> rt = cq.from(Ronda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
