/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.empresa_de_seguridad.jpacontroller;

import com.mycompany.empresa_de_seguridad.jpacontroller.exceptions.IllegalOrphanException;
import com.mycompany.empresa_de_seguridad.jpacontroller.exceptions.NonexistentEntityException;
import com.mycompany.empresa_de_seguridad.model.Cliente;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import com.mycompany.empresa_de_seguridad.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.empresa_de_seguridad.model.Contrato;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 *
 * @author JOSUE
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) {
        if (cliente.getUsuarioList() == null) {
            cliente.setUsuarioList(new ArrayList<Usuario>());
        }
        if (cliente.getContratoList() == null) {
            cliente.setContratoList(new ArrayList<Contrato>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : cliente.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdUsuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            cliente.setUsuarioList(attachedUsuarioList);
            List<Contrato> attachedContratoList = new ArrayList<Contrato>();
            for (Contrato contratoListContratoToAttach : cliente.getContratoList()) {
                contratoListContratoToAttach = em.getReference(contratoListContratoToAttach.getClass(), contratoListContratoToAttach.getIdContrato());
                attachedContratoList.add(contratoListContratoToAttach);
            }
            cliente.setContratoList(attachedContratoList);
            em.persist(cliente);
            for (Usuario usuarioListUsuario : cliente.getUsuarioList()) {
                Cliente oldIdClienteOfUsuarioListUsuario = usuarioListUsuario.getIdCliente();
                usuarioListUsuario.setIdCliente(cliente);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldIdClienteOfUsuarioListUsuario != null) {
                    oldIdClienteOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldIdClienteOfUsuarioListUsuario = em.merge(oldIdClienteOfUsuarioListUsuario);
                }
            }
            for (Contrato contratoListContrato : cliente.getContratoList()) {
                Cliente oldIdClienteOfContratoListContrato = contratoListContrato.getIdCliente();
                contratoListContrato.setIdCliente(cliente);
                contratoListContrato = em.merge(contratoListContrato);
                if (oldIdClienteOfContratoListContrato != null) {
                    oldIdClienteOfContratoListContrato.getContratoList().remove(contratoListContrato);
                    oldIdClienteOfContratoListContrato = em.merge(oldIdClienteOfContratoListContrato);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getIdCliente());
            List<Usuario> usuarioListOld = persistentCliente.getUsuarioList();
            List<Usuario> usuarioListNew = cliente.getUsuarioList();
            List<Contrato> contratoListOld = persistentCliente.getContratoList();
            List<Contrato> contratoListNew = cliente.getContratoList();
            List<String> illegalOrphanMessages = null;
            for (Contrato contratoListOldContrato : contratoListOld) {
                if (!contratoListNew.contains(contratoListOldContrato)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contrato " + contratoListOldContrato + " since its idCliente field is not nullable.");
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
            cliente.setUsuarioList(usuarioListNew);
            List<Contrato> attachedContratoListNew = new ArrayList<Contrato>();
            for (Contrato contratoListNewContratoToAttach : contratoListNew) {
                contratoListNewContratoToAttach = em.getReference(contratoListNewContratoToAttach.getClass(), contratoListNewContratoToAttach.getIdContrato());
                attachedContratoListNew.add(contratoListNewContratoToAttach);
            }
            contratoListNew = attachedContratoListNew;
            cliente.setContratoList(contratoListNew);
            cliente = em.merge(cliente);
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    usuarioListOldUsuario.setIdCliente(null);
                    usuarioListOldUsuario = em.merge(usuarioListOldUsuario);
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Cliente oldIdClienteOfUsuarioListNewUsuario = usuarioListNewUsuario.getIdCliente();
                    usuarioListNewUsuario.setIdCliente(cliente);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldIdClienteOfUsuarioListNewUsuario != null && !oldIdClienteOfUsuarioListNewUsuario.equals(cliente)) {
                        oldIdClienteOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldIdClienteOfUsuarioListNewUsuario = em.merge(oldIdClienteOfUsuarioListNewUsuario);
                    }
                }
            }
            for (Contrato contratoListNewContrato : contratoListNew) {
                if (!contratoListOld.contains(contratoListNewContrato)) {
                    Cliente oldIdClienteOfContratoListNewContrato = contratoListNewContrato.getIdCliente();
                    contratoListNewContrato.setIdCliente(cliente);
                    contratoListNewContrato = em.merge(contratoListNewContrato);
                    if (oldIdClienteOfContratoListNewContrato != null && !oldIdClienteOfContratoListNewContrato.equals(cliente)) {
                        oldIdClienteOfContratoListNewContrato.getContratoList().remove(contratoListNewContrato);
                        oldIdClienteOfContratoListNewContrato = em.merge(oldIdClienteOfContratoListNewContrato);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getIdCliente();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getIdCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Contrato> contratoListOrphanCheck = cliente.getContratoList();
            for (Contrato contratoListOrphanCheckContrato : contratoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Contrato " + contratoListOrphanCheckContrato + " in its contratoList field has a non-nullable idCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Usuario> usuarioList = cliente.getUsuarioList();
            for (Usuario usuarioListUsuario : usuarioList) {
                usuarioListUsuario.setIdCliente(null);
                usuarioListUsuario = em.merge(usuarioListUsuario);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
