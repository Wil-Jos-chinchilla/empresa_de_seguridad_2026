/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.empresa_de_seguridad.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;

/**
 *
 * @author JOSUE
 */
@Entity
@Table(name = "AgenteSeguridad", catalog = "EmpresaSeguridad", schema = "dbo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"DPI"})})
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
    @Column(name = "IdAgente", nullable = false)
    private Integer idAgente;
    @Basic(optional = false)
    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;
    @Basic(optional = false)
    @Column(name = "Apellido", nullable = false, length = 100)
    private String apellido;
    @Basic(optional = false)
    @Column(name = "DPI", nullable = false, length = 20)
    private String dpi;
    @Column(name = "Telefono", length = 20)
    private String telefono;
    @Column(name = "Direccion", length = 250)
    private String direccion;
    @Basic(optional = false)
    @Column(name = "FechaIngreso", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Basic(optional = false)
    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;
    @OneToMany(mappedBy = "idAgente", fetch = FetchType.LAZY)
    private List<Usuario> usuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAgente", fetch = FetchType.LAZY)
    private List<Turno> turnoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAgente", fetch = FetchType.LAZY)
    private List<Incidente> incidenteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAgente", fetch = FetchType.LAZY)
    private List<Ronda> rondaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAgente", fetch = FetchType.LAZY)
    private List<AsignacionAgente> asignacionAgenteList;

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

    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public List<Turno> getTurnoList() {
        return turnoList;
    }

    public void setTurnoList(List<Turno> turnoList) {
        this.turnoList = turnoList;
    }

    public List<Incidente> getIncidenteList() {
        return incidenteList;
    }

    public void setIncidenteList(List<Incidente> incidenteList) {
        this.incidenteList = incidenteList;
    }

    public List<Ronda> getRondaList() {
        return rondaList;
    }

    public void setRondaList(List<Ronda> rondaList) {
        this.rondaList = rondaList;
    }

    public List<AsignacionAgente> getAsignacionAgenteList() {
        return asignacionAgenteList;
    }

    public void setAsignacionAgenteList(List<AsignacionAgente> asignacionAgenteList) {
        this.asignacionAgenteList = asignacionAgenteList;
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
        return "com.mycompany.empresa_de_seguridad.model.AgenteSeguridad[ idAgente=" + idAgente + " ]";
    }
    
}
