/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.empresa_de_seguridad.jpacontroller;

import com.mycompany.empresa_de_seguridad.jpacontroller.exceptions.IllegalOrphanException;
import com.mycompany.empresa_de_seguridad.jpacontroller.exceptions.NonexistentEntityException;
import com.mycompany.empresa_de_seguridad.model.AgenteSeguridad;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import com.mycompany.empresa_de_seguridad.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.empresa_de_seguridad.model.Turno;
import com.mycompany.empresa_de_seguridad.model.Incidente;
import com.mycompany.empresa_de_seguridad.model.Ronda;
import com.mycompany.empresa_de_seguridad.model.AsignacionAgente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 *
 * @author JOSUE
 */
public class AgenteSeguridadJpaController implements Serializable {

    public AgenteSeguridadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AgenteSeguridad agenteSeguridad) {
        if (agenteSeguridad.getUsuarioList() == null) {
            agenteSeguridad.setUsuarioList(new ArrayList<Usuario>());
        }
        if (agenteSeguridad.getTurnoList() == null) {
            agenteSeguridad.setTurnoList(new ArrayList<Turno>());
        }
        if (agenteSeguridad.getIncidenteList() == null) {
            agenteSeguridad.setIncidenteList(new ArrayList<Incidente>());
        }
        if (agenteSeguridad.getRondaList() == null) {
            agenteSeguridad.setRondaList(new ArrayList<Ronda>());
        }
        if (agenteSeguridad.getAsignacionAgenteList() == null) {
            agenteSeguridad.setAsignacionAgenteList(new ArrayList<AsignacionAgente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : agenteSeguridad.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdUsuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            agenteSeguridad.setUsuarioList(attachedUsuarioList);
            List<Turno> attachedTurnoList = new ArrayList<Turno>();
            for (Turno turnoListTurnoToAttach : agenteSeguridad.getTurnoList()) {
                turnoListTurnoToAttach = em.getReference(turnoListTurnoToAttach.getClass(), turnoListTurnoToAttach.getIdTurno());
                attachedTurnoList.add(turnoListTurnoToAttach);
            }
            agenteSeguridad.setTurnoList(attachedTurnoList);
            List<Incidente> attachedIncidenteList = new ArrayList<Incidente>();
            for (Incidente incidenteListIncidenteToAttach : agenteSeguridad.getIncidenteList()) {
                incidenteListIncidenteToAttach = em.getReference(incidenteListIncidenteToAttach.getClass(), incidenteListIncidenteToAttach.getIdIncidente());
                attachedIncidenteList.add(incidenteListIncidenteToAttach);
            }
            agenteSeguridad.setIncidenteList(attachedIncidenteList);
            List<Ronda> attachedRondaList = new ArrayList<Ronda>();
            for (Ronda rondaListRondaToAttach : agenteSeguridad.getRondaList()) {
                rondaListRondaToAttach = em.getReference(rondaListRondaToAttach.getClass(), rondaListRondaToAttach.getIdRonda());
                attachedRondaList.add(rondaListRondaToAttach);
            }
            agenteSeguridad.setRondaList(attachedRondaList);
            List<AsignacionAgente> attachedAsignacionAgenteList = new ArrayList<AsignacionAgente>();
            for (AsignacionAgente asignacionAgenteListAsignacionAgenteToAttach : agenteSeguridad.getAsignacionAgenteList()) {
                asignacionAgenteListAsignacionAgenteToAttach = em.getReference(asignacionAgenteListAsignacionAgenteToAttach.getClass(), asignacionAgenteListAsignacionAgenteToAttach.getIdAsignacion());
                attachedAsignacionAgenteList.add(asignacionAgenteListAsignacionAgenteToAttach);
            }
            agenteSeguridad.setAsignacionAgenteList(attachedAsignacionAgenteList);
            em.persist(agenteSeguridad);
            for (Usuario usuarioListUsuario : agenteSeguridad.getUsuarioList()) {
                AgenteSeguridad oldIdAgenteOfUsuarioListUsuario = usuarioListUsuario.getIdAgente();
                usuarioListUsuario.setIdAgente(agenteSeguridad);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldIdAgenteOfUsuarioListUsuario != null) {
                    oldIdAgenteOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldIdAgenteOfUsuarioListUsuario = em.merge(oldIdAgenteOfUsuarioListUsuario);
                }
            }
            for (Turno turnoListTurno : agenteSeguridad.getTurnoList()) {
                AgenteSeguridad oldIdAgenteOfTurnoListTurno = turnoListTurno.getIdAgente();
                turnoListTurno.setIdAgente(agenteSeguridad);
                turnoListTurno = em.merge(turnoListTurno);
                if (oldIdAgenteOfTurnoListTurno != null) {
                    oldIdAgenteOfTurnoListTurno.getTurnoList().remove(turnoListTurno);
                    oldIdAgenteOfTurnoListTurno = em.merge(oldIdAgenteOfTurnoListTurno);
                }
            }
            for (Incidente incidenteListIncidente : agenteSeguridad.getIncidenteList()) {
                AgenteSeguridad oldIdAgenteOfIncidenteListIncidente = incidenteListIncidente.getIdAgente();
                incidenteListIncidente.setIdAgente(agenteSeguridad);
                incidenteListIncidente = em.merge(incidenteListIncidente);
                if (oldIdAgenteOfIncidenteListIncidente != null) {
                    oldIdAgenteOfIncidenteListIncidente.getIncidenteList().remove(incidenteListIncidente);
                    oldIdAgenteOfIncidenteListIncidente = em.merge(oldIdAgenteOfIncidenteListIncidente);
                }
            }
            for (Ronda rondaListRonda : agenteSeguridad.getRondaList()) {
                AgenteSeguridad oldIdAgenteOfRondaListRonda = rondaListRonda.getIdAgente();
                rondaListRonda.setIdAgente(agenteSeguridad);
                rondaListRonda = em.merge(rondaListRonda);
                if (oldIdAgenteOfRondaListRonda != null) {
                    oldIdAgenteOfRondaListRonda.getRondaList().remove(rondaListRonda);
                    oldIdAgenteOfRondaListRonda = em.merge(oldIdAgenteOfRondaListRonda);
                }
            }
            for (AsignacionAgente asignacionAgenteListAsignacionAgente : agenteSeguridad.getAsignacionAgenteList()) {
                AgenteSeguridad oldIdAgenteOfAsignacionAgenteListAsignacionAgente = asignacionAgenteListAsignacionAgente.getIdAgente();
                asignacionAgenteListAsignacionAgente.setIdAgente(agenteSeguridad);
                asignacionAgenteListAsignacionAgente = em.merge(asignacionAgenteListAsignacionAgente);
                if (oldIdAgenteOfAsignacionAgenteListAsignacionAgente != null) {
                    oldIdAgenteOfAsignacionAgenteListAsignacionAgente.getAsignacionAgenteList().remove(asignacionAgenteListAsignacionAgente);
                    oldIdAgenteOfAsignacionAgenteListAsignacionAgente = em.merge(oldIdAgenteOfAsignacionAgenteListAsignacionAgente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AgenteSeguridad agenteSeguridad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AgenteSeguridad persistentAgenteSeguridad = em.find(AgenteSeguridad.class, agenteSeguridad.getIdAgente());
            List<Usuario> usuarioListOld = persistentAgenteSeguridad.getUsuarioList();
            List<Usuario> usuarioListNew = agenteSeguridad.getUsuarioList();
            List<Turno> turnoListOld = persistentAgenteSeguridad.getTurnoList();
            List<Turno> turnoListNew = agenteSeguridad.getTurnoList();
            List<Incidente> incidenteListOld = persistentAgenteSeguridad.getIncidenteList();
            List<Incidente> incidenteListNew = agenteSeguridad.getIncidenteList();
            List<Ronda> rondaListOld = persistentAgenteSeguridad.getRondaList();
            List<Ronda> rondaListNew = agenteSeguridad.getRondaList();
            List<AsignacionAgente> asignacionAgenteListOld = persistentAgenteSeguridad.getAsignacionAgenteList();
            List<AsignacionAgente> asignacionAgenteListNew = agenteSeguridad.getAsignacionAgenteList();
            List<String> illegalOrphanMessages = null;
            for (Turno turnoListOldTurno : turnoListOld) {
                if (!turnoListNew.contains(turnoListOldTurno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Turno " + turnoListOldTurno + " since its idAgente field is not nullable.");
                }
            }
            for (Incidente incidenteListOldIncidente : incidenteListOld) {
                if (!incidenteListNew.contains(incidenteListOldIncidente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Incidente " + incidenteListOldIncidente + " since its idAgente field is not nullable.");
                }
            }
            for (Ronda rondaListOldRonda : rondaListOld) {
                if (!rondaListNew.contains(rondaListOldRonda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ronda " + rondaListOldRonda + " since its idAgente field is not nullable.");
                }
            }
            for (AsignacionAgente asignacionAgenteListOldAsignacionAgente : asignacionAgenteListOld) {
                if (!asignacionAgenteListNew.contains(asignacionAgenteListOldAsignacionAgente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AsignacionAgente " + asignacionAgenteListOldAsignacionAgente + " since its idAgente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            agenteSeguridad.setUsuarioList(usuarioListNew);
            List<Turno> attachedTurnoListNew = new ArrayList<Turno>();
            for (Turno turnoListNewTurnoToAttach : turnoListNew) {
                turnoListNewTurnoToAttach = em.getReference(turnoListNewTurnoToAttach.getClass(), turnoListNewTurnoToAttach.getIdTurno());
                attachedTurnoListNew.add(turnoListNewTurnoToAttach);
            }
            turnoListNew = attachedTurnoListNew;
            agenteSeguridad.setTurnoList(turnoListNew);
            List<Incidente> attachedIncidenteListNew = new ArrayList<Incidente>();
            for (Incidente incidenteListNewIncidenteToAttach : incidenteListNew) {
                incidenteListNewIncidenteToAttach = em.getReference(incidenteListNewIncidenteToAttach.getClass(), incidenteListNewIncidenteToAttach.getIdIncidente());
                attachedIncidenteListNew.add(incidenteListNewIncidenteToAttach);
            }
            incidenteListNew = attachedIncidenteListNew;
            agenteSeguridad.setIncidenteList(incidenteListNew);
            List<Ronda> attachedRondaListNew = new ArrayList<Ronda>();
            for (Ronda rondaListNewRondaToAttach : rondaListNew) {
                rondaListNewRondaToAttach = em.getReference(rondaListNewRondaToAttach.getClass(), rondaListNewRondaToAttach.getIdRonda());
                attachedRondaListNew.add(rondaListNewRondaToAttach);
            }
            rondaListNew = attachedRondaListNew;
            agenteSeguridad.setRondaList(rondaListNew);
            List<AsignacionAgente> attachedAsignacionAgenteListNew = new ArrayList<AsignacionAgente>();
            for (AsignacionAgente asignacionAgenteListNewAsignacionAgenteToAttach : asignacionAgenteListNew) {
                asignacionAgenteListNewAsignacionAgenteToAttach = em.getReference(asignacionAgenteListNewAsignacionAgenteToAttach.getClass(), asignacionAgenteListNewAsignacionAgenteToAttach.getIdAsignacion());
                attachedAsignacionAgenteListNew.add(asignacionAgenteListNewAsignacionAgenteToAttach);
            }
            asignacionAgenteListNew = attachedAsignacionAgenteListNew;
            agenteSeguridad.setAsignacionAgenteList(asignacionAgenteListNew);
            agenteSeguridad = em.merge(agenteSeguridad);
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    usuarioListOldUsuario.setIdAgente(null);
                    usuarioListOldUsuario = em.merge(usuarioListOldUsuario);
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    AgenteSeguridad oldIdAgenteOfUsuarioListNewUsuario = usuarioListNewUsuario.getIdAgente();
                    usuarioListNewUsuario.setIdAgente(agenteSeguridad);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldIdAgenteOfUsuarioListNewUsuario != null && !oldIdAgenteOfUsuarioListNewUsuario.equals(agenteSeguridad)) {
                        oldIdAgenteOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldIdAgenteOfUsuarioListNewUsuario = em.merge(oldIdAgenteOfUsuarioListNewUsuario);
                    }
                }
            }
            for (Turno turnoListNewTurno : turnoListNew) {
                if (!turnoListOld.contains(turnoListNewTurno)) {
                    AgenteSeguridad oldIdAgenteOfTurnoListNewTurno = turnoListNewTurno.getIdAgente();
                    turnoListNewTurno.setIdAgente(agenteSeguridad);
                    turnoListNewTurno = em.merge(turnoListNewTurno);
                    if (oldIdAgenteOfTurnoListNewTurno != null && !oldIdAgenteOfTurnoListNewTurno.equals(agenteSeguridad)) {
                        oldIdAgenteOfTurnoListNewTurno.getTurnoList().remove(turnoListNewTurno);
                        oldIdAgenteOfTurnoListNewTurno = em.merge(oldIdAgenteOfTurnoListNewTurno);
                    }
                }
            }
            for (Incidente incidenteListNewIncidente : incidenteListNew) {
                if (!incidenteListOld.contains(incidenteListNewIncidente)) {
                    AgenteSeguridad oldIdAgenteOfIncidenteListNewIncidente = incidenteListNewIncidente.getIdAgente();
                    incidenteListNewIncidente.setIdAgente(agenteSeguridad);
                    incidenteListNewIncidente = em.merge(incidenteListNewIncidente);
                    if (oldIdAgenteOfIncidenteListNewIncidente != null && !oldIdAgenteOfIncidenteListNewIncidente.equals(agenteSeguridad)) {
                        oldIdAgenteOfIncidenteListNewIncidente.getIncidenteList().remove(incidenteListNewIncidente);
                        oldIdAgenteOfIncidenteListNewIncidente = em.merge(oldIdAgenteOfIncidenteListNewIncidente);
                    }
                }
            }
            for (Ronda rondaListNewRonda : rondaListNew) {
                if (!rondaListOld.contains(rondaListNewRonda)) {
                    AgenteSeguridad oldIdAgenteOfRondaListNewRonda = rondaListNewRonda.getIdAgente();
                    rondaListNewRonda.setIdAgente(agenteSeguridad);
                    rondaListNewRonda = em.merge(rondaListNewRonda);
                    if (oldIdAgenteOfRondaListNewRonda != null && !oldIdAgenteOfRondaListNewRonda.equals(agenteSeguridad)) {
                        oldIdAgenteOfRondaListNewRonda.getRondaList().remove(rondaListNewRonda);
                        oldIdAgenteOfRondaListNewRonda = em.merge(oldIdAgenteOfRondaListNewRonda);
                    }
                }
            }
            for (AsignacionAgente asignacionAgenteListNewAsignacionAgente : asignacionAgenteListNew) {
                if (!asignacionAgenteListOld.contains(asignacionAgenteListNewAsignacionAgente)) {
                    AgenteSeguridad oldIdAgenteOfAsignacionAgenteListNewAsignacionAgente = asignacionAgenteListNewAsignacionAgente.getIdAgente();
                    asignacionAgenteListNewAsignacionAgente.setIdAgente(agenteSeguridad);
                    asignacionAgenteListNewAsignacionAgente = em.merge(asignacionAgenteListNewAsignacionAgente);
                    if (oldIdAgenteOfAsignacionAgenteListNewAsignacionAgente != null && !oldIdAgenteOfAsignacionAgenteListNewAsignacionAgente.equals(agenteSeguridad)) {
                        oldIdAgenteOfAsignacionAgenteListNewAsignacionAgente.getAsignacionAgenteList().remove(asignacionAgenteListNewAsignacionAgente);
                        oldIdAgenteOfAsignacionAgenteListNewAsignacionAgente = em.merge(oldIdAgenteOfAsignacionAgenteListNewAsignacionAgente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = agenteSeguridad.getIdAgente();
                if (findAgenteSeguridad(id) == null) {
                    throw new NonexistentEntityException("The agenteSeguridad with id " + id + " no longer exists.");
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
            AgenteSeguridad agenteSeguridad;
            try {
                agenteSeguridad = em.getReference(AgenteSeguridad.class, id);
                agenteSeguridad.getIdAgente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The agenteSeguridad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Turno> turnoListOrphanCheck = agenteSeguridad.getTurnoList();
            for (Turno turnoListOrphanCheckTurno : turnoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AgenteSeguridad (" + agenteSeguridad + ") cannot be destroyed since the Turno " + turnoListOrphanCheckTurno + " in its turnoList field has a non-nullable idAgente field.");
            }
            List<Incidente> incidenteListOrphanCheck = agenteSeguridad.getIncidenteList();
            for (Incidente incidenteListOrphanCheckIncidente : incidenteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AgenteSeguridad (" + agenteSeguridad + ") cannot be destroyed since the Incidente " + incidenteListOrphanCheckIncidente + " in its incidenteList field has a non-nullable idAgente field.");
            }
            List<Ronda> rondaListOrphanCheck = agenteSeguridad.getRondaList();
            for (Ronda rondaListOrphanCheckRonda : rondaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AgenteSeguridad (" + agenteSeguridad + ") cannot be destroyed since the Ronda " + rondaListOrphanCheckRonda + " in its rondaList field has a non-nullable idAgente field.");
            }
            List<AsignacionAgente> asignacionAgenteListOrphanCheck = agenteSeguridad.getAsignacionAgenteList();
            for (AsignacionAgente asignacionAgenteListOrphanCheckAsignacionAgente : asignacionAgenteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AgenteSeguridad (" + agenteSeguridad + ") cannot be destroyed since the AsignacionAgente " + asignacionAgenteListOrphanCheckAsignacionAgente + " in its asignacionAgenteList field has a non-nullable idAgente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Usuario> usuarioList = agenteSeguridad.getUsuarioList();
            for (Usuario usuarioListUsuario : usuarioList) {
                usuarioListUsuario.setIdAgente(null);
                usuarioListUsuario = em.merge(usuarioListUsuario);
            }
            em.remove(agenteSeguridad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AgenteSeguridad> findAgenteSeguridadEntities() {
        return findAgenteSeguridadEntities(true, -1, -1);
    }

    public List<AgenteSeguridad> findAgenteSeguridadEntities(int maxResults, int firstResult) {
        return findAgenteSeguridadEntities(false, maxResults, firstResult);
    }

    private List<AgenteSeguridad> findAgenteSeguridadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AgenteSeguridad.class));
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

    public AgenteSeguridad findAgenteSeguridad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AgenteSeguridad.class, id);
        } finally {
            em.close();
        }
    }

    public int getAgenteSeguridadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AgenteSeguridad> rt = cq.from(AgenteSeguridad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
