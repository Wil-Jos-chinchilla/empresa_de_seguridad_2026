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
import com.mycompany.empresa_de_seguridad.model.Cliente;
import com.mycompany.empresa_de_seguridad.model.PlanServicio;
import com.mycompany.empresa_de_seguridad.model.PuestoServicio;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.empresa_de_seguridad.model.Turno;
import com.mycompany.empresa_de_seguridad.model.Incidente;
import com.mycompany.empresa_de_seguridad.model.Factura;
import com.mycompany.empresa_de_seguridad.model.AsignacionAgente;
import com.mycompany.empresa_de_seguridad.model.Contrato;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 *
 * @author JOSUE
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
        if (contrato.getPuestoServicioList() == null) {
            contrato.setPuestoServicioList(new ArrayList<PuestoServicio>());
        }
        if (contrato.getTurnoList() == null) {
            contrato.setTurnoList(new ArrayList<Turno>());
        }
        if (contrato.getIncidenteList() == null) {
            contrato.setIncidenteList(new ArrayList<Incidente>());
        }
        if (contrato.getFacturaList() == null) {
            contrato.setFacturaList(new ArrayList<Factura>());
        }
        if (contrato.getAsignacionAgenteList() == null) {
            contrato.setAsignacionAgenteList(new ArrayList<AsignacionAgente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente idCliente = contrato.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                contrato.setIdCliente(idCliente);
            }
            PlanServicio idPlan = contrato.getIdPlan();
            if (idPlan != null) {
                idPlan = em.getReference(idPlan.getClass(), idPlan.getIdPlan());
                contrato.setIdPlan(idPlan);
            }
            List<PuestoServicio> attachedPuestoServicioList = new ArrayList<PuestoServicio>();
            for (PuestoServicio puestoServicioListPuestoServicioToAttach : contrato.getPuestoServicioList()) {
                puestoServicioListPuestoServicioToAttach = em.getReference(puestoServicioListPuestoServicioToAttach.getClass(), puestoServicioListPuestoServicioToAttach.getIdPuesto());
                attachedPuestoServicioList.add(puestoServicioListPuestoServicioToAttach);
            }
            contrato.setPuestoServicioList(attachedPuestoServicioList);
            List<Turno> attachedTurnoList = new ArrayList<Turno>();
            for (Turno turnoListTurnoToAttach : contrato.getTurnoList()) {
                turnoListTurnoToAttach = em.getReference(turnoListTurnoToAttach.getClass(), turnoListTurnoToAttach.getIdTurno());
                attachedTurnoList.add(turnoListTurnoToAttach);
            }
            contrato.setTurnoList(attachedTurnoList);
            List<Incidente> attachedIncidenteList = new ArrayList<Incidente>();
            for (Incidente incidenteListIncidenteToAttach : contrato.getIncidenteList()) {
                incidenteListIncidenteToAttach = em.getReference(incidenteListIncidenteToAttach.getClass(), incidenteListIncidenteToAttach.getIdIncidente());
                attachedIncidenteList.add(incidenteListIncidenteToAttach);
            }
            contrato.setIncidenteList(attachedIncidenteList);
            List<Factura> attachedFacturaList = new ArrayList<Factura>();
            for (Factura facturaListFacturaToAttach : contrato.getFacturaList()) {
                facturaListFacturaToAttach = em.getReference(facturaListFacturaToAttach.getClass(), facturaListFacturaToAttach.getIdFactura());
                attachedFacturaList.add(facturaListFacturaToAttach);
            }
            contrato.setFacturaList(attachedFacturaList);
            List<AsignacionAgente> attachedAsignacionAgenteList = new ArrayList<AsignacionAgente>();
            for (AsignacionAgente asignacionAgenteListAsignacionAgenteToAttach : contrato.getAsignacionAgenteList()) {
                asignacionAgenteListAsignacionAgenteToAttach = em.getReference(asignacionAgenteListAsignacionAgenteToAttach.getClass(), asignacionAgenteListAsignacionAgenteToAttach.getIdAsignacion());
                attachedAsignacionAgenteList.add(asignacionAgenteListAsignacionAgenteToAttach);
            }
            contrato.setAsignacionAgenteList(attachedAsignacionAgenteList);
            em.persist(contrato);
            if (idCliente != null) {
                idCliente.getContratoList().add(contrato);
                idCliente = em.merge(idCliente);
            }
            if (idPlan != null) {
                idPlan.getContratoList().add(contrato);
                idPlan = em.merge(idPlan);
            }
            for (PuestoServicio puestoServicioListPuestoServicio : contrato.getPuestoServicioList()) {
                Contrato oldIdContratoOfPuestoServicioListPuestoServicio = puestoServicioListPuestoServicio.getIdContrato();
                puestoServicioListPuestoServicio.setIdContrato(contrato);
                puestoServicioListPuestoServicio = em.merge(puestoServicioListPuestoServicio);
                if (oldIdContratoOfPuestoServicioListPuestoServicio != null) {
                    oldIdContratoOfPuestoServicioListPuestoServicio.getPuestoServicioList().remove(puestoServicioListPuestoServicio);
                    oldIdContratoOfPuestoServicioListPuestoServicio = em.merge(oldIdContratoOfPuestoServicioListPuestoServicio);
                }
            }
            for (Turno turnoListTurno : contrato.getTurnoList()) {
                Contrato oldIdContratoOfTurnoListTurno = turnoListTurno.getIdContrato();
                turnoListTurno.setIdContrato(contrato);
                turnoListTurno = em.merge(turnoListTurno);
                if (oldIdContratoOfTurnoListTurno != null) {
                    oldIdContratoOfTurnoListTurno.getTurnoList().remove(turnoListTurno);
                    oldIdContratoOfTurnoListTurno = em.merge(oldIdContratoOfTurnoListTurno);
                }
            }
            for (Incidente incidenteListIncidente : contrato.getIncidenteList()) {
                Contrato oldIdContratoOfIncidenteListIncidente = incidenteListIncidente.getIdContrato();
                incidenteListIncidente.setIdContrato(contrato);
                incidenteListIncidente = em.merge(incidenteListIncidente);
                if (oldIdContratoOfIncidenteListIncidente != null) {
                    oldIdContratoOfIncidenteListIncidente.getIncidenteList().remove(incidenteListIncidente);
                    oldIdContratoOfIncidenteListIncidente = em.merge(oldIdContratoOfIncidenteListIncidente);
                }
            }
            for (Factura facturaListFactura : contrato.getFacturaList()) {
                Contrato oldIdContratoOfFacturaListFactura = facturaListFactura.getIdContrato();
                facturaListFactura.setIdContrato(contrato);
                facturaListFactura = em.merge(facturaListFactura);
                if (oldIdContratoOfFacturaListFactura != null) {
                    oldIdContratoOfFacturaListFactura.getFacturaList().remove(facturaListFactura);
                    oldIdContratoOfFacturaListFactura = em.merge(oldIdContratoOfFacturaListFactura);
                }
            }
            for (AsignacionAgente asignacionAgenteListAsignacionAgente : contrato.getAsignacionAgenteList()) {
                Contrato oldIdContratoOfAsignacionAgenteListAsignacionAgente = asignacionAgenteListAsignacionAgente.getIdContrato();
                asignacionAgenteListAsignacionAgente.setIdContrato(contrato);
                asignacionAgenteListAsignacionAgente = em.merge(asignacionAgenteListAsignacionAgente);
                if (oldIdContratoOfAsignacionAgenteListAsignacionAgente != null) {
                    oldIdContratoOfAsignacionAgenteListAsignacionAgente.getAsignacionAgenteList().remove(asignacionAgenteListAsignacionAgente);
                    oldIdContratoOfAsignacionAgenteListAsignacionAgente = em.merge(oldIdContratoOfAsignacionAgenteListAsignacionAgente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contrato contrato) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contrato persistentContrato = em.find(Contrato.class, contrato.getIdContrato());
            Cliente idClienteOld = persistentContrato.getIdCliente();
            Cliente idClienteNew = contrato.getIdCliente();
            PlanServicio idPlanOld = persistentContrato.getIdPlan();
            PlanServicio idPlanNew = contrato.getIdPlan();
            List<PuestoServicio> puestoServicioListOld = persistentContrato.getPuestoServicioList();
            List<PuestoServicio> puestoServicioListNew = contrato.getPuestoServicioList();
            List<Turno> turnoListOld = persistentContrato.getTurnoList();
            List<Turno> turnoListNew = contrato.getTurnoList();
            List<Incidente> incidenteListOld = persistentContrato.getIncidenteList();
            List<Incidente> incidenteListNew = contrato.getIncidenteList();
            List<Factura> facturaListOld = persistentContrato.getFacturaList();
            List<Factura> facturaListNew = contrato.getFacturaList();
            List<AsignacionAgente> asignacionAgenteListOld = persistentContrato.getAsignacionAgenteList();
            List<AsignacionAgente> asignacionAgenteListNew = contrato.getAsignacionAgenteList();
            List<String> illegalOrphanMessages = null;
            for (PuestoServicio puestoServicioListOldPuestoServicio : puestoServicioListOld) {
                if (!puestoServicioListNew.contains(puestoServicioListOldPuestoServicio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PuestoServicio " + puestoServicioListOldPuestoServicio + " since its idContrato field is not nullable.");
                }
            }
            for (Turno turnoListOldTurno : turnoListOld) {
                if (!turnoListNew.contains(turnoListOldTurno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Turno " + turnoListOldTurno + " since its idContrato field is not nullable.");
                }
            }
            for (Incidente incidenteListOldIncidente : incidenteListOld) {
                if (!incidenteListNew.contains(incidenteListOldIncidente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Incidente " + incidenteListOldIncidente + " since its idContrato field is not nullable.");
                }
            }
            for (Factura facturaListOldFactura : facturaListOld) {
                if (!facturaListNew.contains(facturaListOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaListOldFactura + " since its idContrato field is not nullable.");
                }
            }
            for (AsignacionAgente asignacionAgenteListOldAsignacionAgente : asignacionAgenteListOld) {
                if (!asignacionAgenteListNew.contains(asignacionAgenteListOldAsignacionAgente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AsignacionAgente " + asignacionAgenteListOldAsignacionAgente + " since its idContrato field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                contrato.setIdCliente(idClienteNew);
            }
            if (idPlanNew != null) {
                idPlanNew = em.getReference(idPlanNew.getClass(), idPlanNew.getIdPlan());
                contrato.setIdPlan(idPlanNew);
            }
            List<PuestoServicio> attachedPuestoServicioListNew = new ArrayList<PuestoServicio>();
            for (PuestoServicio puestoServicioListNewPuestoServicioToAttach : puestoServicioListNew) {
                puestoServicioListNewPuestoServicioToAttach = em.getReference(puestoServicioListNewPuestoServicioToAttach.getClass(), puestoServicioListNewPuestoServicioToAttach.getIdPuesto());
                attachedPuestoServicioListNew.add(puestoServicioListNewPuestoServicioToAttach);
            }
            puestoServicioListNew = attachedPuestoServicioListNew;
            contrato.setPuestoServicioList(puestoServicioListNew);
            List<Turno> attachedTurnoListNew = new ArrayList<Turno>();
            for (Turno turnoListNewTurnoToAttach : turnoListNew) {
                turnoListNewTurnoToAttach = em.getReference(turnoListNewTurnoToAttach.getClass(), turnoListNewTurnoToAttach.getIdTurno());
                attachedTurnoListNew.add(turnoListNewTurnoToAttach);
            }
            turnoListNew = attachedTurnoListNew;
            contrato.setTurnoList(turnoListNew);
            List<Incidente> attachedIncidenteListNew = new ArrayList<Incidente>();
            for (Incidente incidenteListNewIncidenteToAttach : incidenteListNew) {
                incidenteListNewIncidenteToAttach = em.getReference(incidenteListNewIncidenteToAttach.getClass(), incidenteListNewIncidenteToAttach.getIdIncidente());
                attachedIncidenteListNew.add(incidenteListNewIncidenteToAttach);
            }
            incidenteListNew = attachedIncidenteListNew;
            contrato.setIncidenteList(incidenteListNew);
            List<Factura> attachedFacturaListNew = new ArrayList<Factura>();
            for (Factura facturaListNewFacturaToAttach : facturaListNew) {
                facturaListNewFacturaToAttach = em.getReference(facturaListNewFacturaToAttach.getClass(), facturaListNewFacturaToAttach.getIdFactura());
                attachedFacturaListNew.add(facturaListNewFacturaToAttach);
            }
            facturaListNew = attachedFacturaListNew;
            contrato.setFacturaList(facturaListNew);
            List<AsignacionAgente> attachedAsignacionAgenteListNew = new ArrayList<AsignacionAgente>();
            for (AsignacionAgente asignacionAgenteListNewAsignacionAgenteToAttach : asignacionAgenteListNew) {
                asignacionAgenteListNewAsignacionAgenteToAttach = em.getReference(asignacionAgenteListNewAsignacionAgenteToAttach.getClass(), asignacionAgenteListNewAsignacionAgenteToAttach.getIdAsignacion());
                attachedAsignacionAgenteListNew.add(asignacionAgenteListNewAsignacionAgenteToAttach);
            }
            asignacionAgenteListNew = attachedAsignacionAgenteListNew;
            contrato.setAsignacionAgenteList(asignacionAgenteListNew);
            contrato = em.merge(contrato);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getContratoList().remove(contrato);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getContratoList().add(contrato);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idPlanOld != null && !idPlanOld.equals(idPlanNew)) {
                idPlanOld.getContratoList().remove(contrato);
                idPlanOld = em.merge(idPlanOld);
            }
            if (idPlanNew != null && !idPlanNew.equals(idPlanOld)) {
                idPlanNew.getContratoList().add(contrato);
                idPlanNew = em.merge(idPlanNew);
            }
            for (PuestoServicio puestoServicioListNewPuestoServicio : puestoServicioListNew) {
                if (!puestoServicioListOld.contains(puestoServicioListNewPuestoServicio)) {
                    Contrato oldIdContratoOfPuestoServicioListNewPuestoServicio = puestoServicioListNewPuestoServicio.getIdContrato();
                    puestoServicioListNewPuestoServicio.setIdContrato(contrato);
                    puestoServicioListNewPuestoServicio = em.merge(puestoServicioListNewPuestoServicio);
                    if (oldIdContratoOfPuestoServicioListNewPuestoServicio != null && !oldIdContratoOfPuestoServicioListNewPuestoServicio.equals(contrato)) {
                        oldIdContratoOfPuestoServicioListNewPuestoServicio.getPuestoServicioList().remove(puestoServicioListNewPuestoServicio);
                        oldIdContratoOfPuestoServicioListNewPuestoServicio = em.merge(oldIdContratoOfPuestoServicioListNewPuestoServicio);
                    }
                }
            }
            for (Turno turnoListNewTurno : turnoListNew) {
                if (!turnoListOld.contains(turnoListNewTurno)) {
                    Contrato oldIdContratoOfTurnoListNewTurno = turnoListNewTurno.getIdContrato();
                    turnoListNewTurno.setIdContrato(contrato);
                    turnoListNewTurno = em.merge(turnoListNewTurno);
                    if (oldIdContratoOfTurnoListNewTurno != null && !oldIdContratoOfTurnoListNewTurno.equals(contrato)) {
                        oldIdContratoOfTurnoListNewTurno.getTurnoList().remove(turnoListNewTurno);
                        oldIdContratoOfTurnoListNewTurno = em.merge(oldIdContratoOfTurnoListNewTurno);
                    }
                }
            }
            for (Incidente incidenteListNewIncidente : incidenteListNew) {
                if (!incidenteListOld.contains(incidenteListNewIncidente)) {
                    Contrato oldIdContratoOfIncidenteListNewIncidente = incidenteListNewIncidente.getIdContrato();
                    incidenteListNewIncidente.setIdContrato(contrato);
                    incidenteListNewIncidente = em.merge(incidenteListNewIncidente);
                    if (oldIdContratoOfIncidenteListNewIncidente != null && !oldIdContratoOfIncidenteListNewIncidente.equals(contrato)) {
                        oldIdContratoOfIncidenteListNewIncidente.getIncidenteList().remove(incidenteListNewIncidente);
                        oldIdContratoOfIncidenteListNewIncidente = em.merge(oldIdContratoOfIncidenteListNewIncidente);
                    }
                }
            }
            for (Factura facturaListNewFactura : facturaListNew) {
                if (!facturaListOld.contains(facturaListNewFactura)) {
                    Contrato oldIdContratoOfFacturaListNewFactura = facturaListNewFactura.getIdContrato();
                    facturaListNewFactura.setIdContrato(contrato);
                    facturaListNewFactura = em.merge(facturaListNewFactura);
                    if (oldIdContratoOfFacturaListNewFactura != null && !oldIdContratoOfFacturaListNewFactura.equals(contrato)) {
                        oldIdContratoOfFacturaListNewFactura.getFacturaList().remove(facturaListNewFactura);
                        oldIdContratoOfFacturaListNewFactura = em.merge(oldIdContratoOfFacturaListNewFactura);
                    }
                }
            }
            for (AsignacionAgente asignacionAgenteListNewAsignacionAgente : asignacionAgenteListNew) {
                if (!asignacionAgenteListOld.contains(asignacionAgenteListNewAsignacionAgente)) {
                    Contrato oldIdContratoOfAsignacionAgenteListNewAsignacionAgente = asignacionAgenteListNewAsignacionAgente.getIdContrato();
                    asignacionAgenteListNewAsignacionAgente.setIdContrato(contrato);
                    asignacionAgenteListNewAsignacionAgente = em.merge(asignacionAgenteListNewAsignacionAgente);
                    if (oldIdContratoOfAsignacionAgenteListNewAsignacionAgente != null && !oldIdContratoOfAsignacionAgenteListNewAsignacionAgente.equals(contrato)) {
                        oldIdContratoOfAsignacionAgenteListNewAsignacionAgente.getAsignacionAgenteList().remove(asignacionAgenteListNewAsignacionAgente);
                        oldIdContratoOfAsignacionAgenteListNewAsignacionAgente = em.merge(oldIdContratoOfAsignacionAgenteListNewAsignacionAgente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = contrato.getIdContrato();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contrato contrato;
            try {
                contrato = em.getReference(Contrato.class, id);
                contrato.getIdContrato();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contrato with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PuestoServicio> puestoServicioListOrphanCheck = contrato.getPuestoServicioList();
            for (PuestoServicio puestoServicioListOrphanCheckPuestoServicio : puestoServicioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contrato (" + contrato + ") cannot be destroyed since the PuestoServicio " + puestoServicioListOrphanCheckPuestoServicio + " in its puestoServicioList field has a non-nullable idContrato field.");
            }
            List<Turno> turnoListOrphanCheck = contrato.getTurnoList();
            for (Turno turnoListOrphanCheckTurno : turnoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contrato (" + contrato + ") cannot be destroyed since the Turno " + turnoListOrphanCheckTurno + " in its turnoList field has a non-nullable idContrato field.");
            }
            List<Incidente> incidenteListOrphanCheck = contrato.getIncidenteList();
            for (Incidente incidenteListOrphanCheckIncidente : incidenteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contrato (" + contrato + ") cannot be destroyed since the Incidente " + incidenteListOrphanCheckIncidente + " in its incidenteList field has a non-nullable idContrato field.");
            }
            List<Factura> facturaListOrphanCheck = contrato.getFacturaList();
            for (Factura facturaListOrphanCheckFactura : facturaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contrato (" + contrato + ") cannot be destroyed since the Factura " + facturaListOrphanCheckFactura + " in its facturaList field has a non-nullable idContrato field.");
            }
            List<AsignacionAgente> asignacionAgenteListOrphanCheck = contrato.getAsignacionAgenteList();
            for (AsignacionAgente asignacionAgenteListOrphanCheckAsignacionAgente : asignacionAgenteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contrato (" + contrato + ") cannot be destroyed since the AsignacionAgente " + asignacionAgenteListOrphanCheckAsignacionAgente + " in its asignacionAgenteList field has a non-nullable idContrato field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cliente idCliente = contrato.getIdCliente();
            if (idCliente != null) {
                idCliente.getContratoList().remove(contrato);
                idCliente = em.merge(idCliente);
            }
            PlanServicio idPlan = contrato.getIdPlan();
            if (idPlan != null) {
                idPlan.getContratoList().remove(contrato);
                idPlan = em.merge(idPlan);
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
