/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.empresa_de_seguridad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author JOSUE
 */
@Entity
@Table(name = "Incidente")
@NamedQueries({
    @NamedQuery(name = "Incidente.findAll", query = "SELECT i FROM Incidente i"),
    @NamedQuery(name = "Incidente.findByIdIncidente", query = "SELECT i FROM Incidente i WHERE i.idIncidente = :idIncidente"),
    @NamedQuery(name = "Incidente.findByFechaHora", query = "SELECT i FROM Incidente i WHERE i.fechaHora = :fechaHora"),
    @NamedQuery(name = "Incidente.findByTipoIncidente", query = "SELECT i FROM Incidente i WHERE i.tipoIncidente = :tipoIncidente"),
    @NamedQuery(name = "Incidente.findByDescripcion", query = "SELECT i FROM Incidente i WHERE i.descripcion = :descripcion"),
    @NamedQuery(name = "Incidente.findByNivelUrgencia", query = "SELECT i FROM Incidente i WHERE i.nivelUrgencia = :nivelUrgencia"),
    @NamedQuery(name = "Incidente.findByLatitud", query = "SELECT i FROM Incidente i WHERE i.latitud = :latitud"),
    @NamedQuery(name = "Incidente.findByLongitud", query = "SELECT i FROM Incidente i WHERE i.longitud = :longitud"),
    @NamedQuery(name = "Incidente.findByEstado", query = "SELECT i FROM Incidente i WHERE i.estado = :estado"),
    @NamedQuery(name = "Incidente.findByFechaCierre", query = "SELECT i FROM Incidente i WHERE i.fechaCierre = :fechaCierre"),
    @NamedQuery(name = "Incidente.findByObservacionCierre", query = "SELECT i FROM Incidente i WHERE i.observacionCierre = :observacionCierre")})
public class Incidente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdIncidente")
    private Integer idIncidente;
    @Basic(optional = false)
    @Column(name = "FechaHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Basic(optional = false)
    @Column(name = "TipoIncidente")
    private String tipoIncidente;
    @Basic(optional = false)
    @Column(name = "Descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "NivelUrgencia")
    private String nivelUrgencia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Latitud")
    private BigDecimal latitud;
    @Column(name = "Longitud")
    private BigDecimal longitud;
    @Basic(optional = false)
    @Column(name = "Estado")
    private String estado;
    @Column(name = "FechaCierre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCierre;
    @Column(name = "ObservacionCierre")
    private String observacionCierre;
    @JoinColumn(name = "IdAgente", referencedColumnName = "IdAgente")
    @ManyToOne(optional = false)
    private AgenteSeguridad idAgente;
    @JoinColumn(name = "IdContrato", referencedColumnName = "IdContrato")
    @ManyToOne(optional = false)
    private Contrato idContrato;

    public Incidente() {
    }

    public Incidente(Integer idIncidente) {
        this.idIncidente = idIncidente;
    }

    public Incidente(Integer idIncidente, Date fechaHora, String tipoIncidente, String descripcion, String nivelUrgencia, String estado) {
        this.idIncidente = idIncidente;
        this.fechaHora = fechaHora;
        this.tipoIncidente = tipoIncidente;
        this.descripcion = descripcion;
        this.nivelUrgencia = nivelUrgencia;
        this.estado = estado;
    }

    public Integer getIdIncidente() {
        return idIncidente;
    }

    public void setIdIncidente(Integer idIncidente) {
        this.idIncidente = idIncidente;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getTipoIncidente() {
        return tipoIncidente;
    }

    public void setTipoIncidente(String tipoIncidente) {
        this.tipoIncidente = tipoIncidente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNivelUrgencia() {
        return nivelUrgencia;
    }

    public void setNivelUrgencia(String nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getObservacionCierre() {
        return observacionCierre;
    }

    public void setObservacionCierre(String observacionCierre) {
        this.observacionCierre = observacionCierre;
    }

    public AgenteSeguridad getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(AgenteSeguridad idAgente) {
        this.idAgente = idAgente;
    }

    public Contrato getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Contrato idContrato) {
        this.idContrato = idContrato;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idIncidente != null ? idIncidente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Incidente)) {
            return false;
        }
        Incidente other = (Incidente) object;
        if ((this.idIncidente == null && other.idIncidente != null) || (this.idIncidente != null && !this.idIncidente.equals(other.idIncidente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.empresa_de_seguridad.Incidente[ idIncidente=" + idIncidente + " ]";
    }
    
}
