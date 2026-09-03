/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.empresa_de_seguridad;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author JOSUE
 */
@Entity
@Table(name = "AgenteSeguridad")
@NamedQueries({
    @NamedQuery(name = "AgenteSeguridad.findAll", query = "SELECT a FROM AgenteSeguridad a"),
    @NamedQuery(name = "AgenteSeguridad.findByIdAgente", query = "SELECT a FROM AgenteSeguridad a WHERE a.idAgente = :idAgente"),
    @NamedQuery(name = "AgenteSeguridad.findByNombre", query = "SELECT a FROM AgenteSeguridad a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "AgenteSeguridad.findByApellido", query = "SELECT a FROM AgenteSeguridad a WHERE a.apellido = :apellido"),
    @NamedQuery(name = "AgenteSeguridad.findByDpi", query = "SELECT a FROM AgenteSeguridad a WHERE a.dpi = :dpi"),
    @NamedQuery(name = "AgenteSeguridad.findByTelefono", query = "SELECT a FROM AgenteSeguridad a WHERE a.telefono = :telefono"),
    @NamedQuery(name = "AgenteSeguridad.findByDireccion", query = "SELECT a FROM AgenteSeguridad a WHERE a.direccion = :direccion"),
    @NamedQuery(name = "AgenteSeguridad.findByFechaIngreso", query = "SELECT a FROM AgenteSeguridad a WHERE a.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "AgenteSeguridad.findByEstado", query = "SELECT a FROM AgenteSeguridad a WHERE a.estado = :estado")})
public class AgenteSeguridad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdAgente")
    private Integer idAgente;
    @Basic(optional = false)
    @Column(name = "Nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "Apellido")
    private String apellido;
    @Basic(optional = false)
    @Column(name = "DPI")
    private String dpi;
    @Column(name = "Telefono")
    private String telefono;
    @Column(name = "Direccion")
    private String direccion;
    @Basic(optional = false)
    @Column(name = "FechaIngreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Basic(optional = false)
    @Column(name = "Estado")
    private String estado;
    @OneToMany(mappedBy = "idAgente")
    private Collection<Usuario> usuarioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAgente")
    private Collection<Turno> turnoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAgente")
    private Collection<Incidente> incidenteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAgente")
    private Collection<Ronda> rondaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAgente")
    private Collection<AsignacionAgente> asignacionAgenteCollection;

    public AgenteSeguridad() {
    }

    public AgenteSeguridad(Integer idAgente) {
        this.idAgente = idAgente;
    }

    public AgenteSeguridad(Integer idAgente, String nombre, String apellido, String dpi, Date fechaIngreso, String estado) {
        this.idAgente = idAgente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dpi = dpi;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
    }

    public Integer getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(Integer idAgente) {
        this.idAgente = idAgente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    public Collection<Turno> getTurnoCollection() {
        return turnoCollection;
    }

    public void setTurnoCollection(Collection<Turno> turnoCollection) {
        this.turnoCollection = turnoCollection;
    }

    public Collection<Incidente> getIncidenteCollection() {
        return incidenteCollection;
    }

    public void setIncidenteCollection(Collection<Incidente> incidenteCollection) {
        this.incidenteCollection = incidenteCollection;
    }

    public Collection<Ronda> getRondaCollection() {
        return rondaCollection;
    }

    public void setRondaCollection(Collection<Ronda> rondaCollection) {
        this.rondaCollection = rondaCollection;
    }

    public Collection<AsignacionAgente> getAsignacionAgenteCollection() {
        return asignacionAgenteCollection;
    }

    public void setAsignacionAgenteCollection(Collection<AsignacionAgente> asignacionAgenteCollection) {
        this.asignacionAgenteCollection = asignacionAgenteCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAgente != null ? idAgente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AgenteSeguridad)) {
            return false;
        }
        AgenteSeguridad other = (AgenteSeguridad) object;
        if ((this.idAgente == null && other.idAgente != null) || (this.idAgente != null && !this.idAgente.equals(other.idAgente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.empresa_de_seguridad.AgenteSeguridad[ idAgente=" + idAgente + " ]";
    }
    
}
