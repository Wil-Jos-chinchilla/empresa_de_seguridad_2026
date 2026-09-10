/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.empresa_de_seguridad.model;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author JOSUE
 */
@Entity
@Table(name = "AsignacionAgente", catalog = "EmpresaSeguridad", schema = "dbo")
@NamedQueries({
    @NamedQuery(name = "AsignacionAgente.findAll", query = "SELECT a FROM AsignacionAgente a"),
    @NamedQuery(name = "AsignacionAgente.findByIdAsignacion", query = "SELECT a FROM AsignacionAgente a WHERE a.idAsignacion = :idAsignacion"),
    @NamedQuery(name = "AsignacionAgente.findByFechaAsignacion", query = "SELECT a FROM AsignacionAgente a WHERE a.fechaAsignacion = :fechaAsignacion"),
    @NamedQuery(name = "AsignacionAgente.findByFechaFinAsignacion", query = "SELECT a FROM AsignacionAgente a WHERE a.fechaFinAsignacion = :fechaFinAsignacion")})
public class AsignacionAgente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdAsignacion", nullable = false)
    private Integer idAsignacion;
    @Basic(optional = false)
    @Column(name = "FechaAsignacion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaAsignacion;
    @Column(name = "FechaFinAsignacion")
    @Temporal(TemporalType.DATE)
    private Date fechaFinAsignacion;
    @JoinColumn(name = "IdAgente", referencedColumnName = "IdAgente", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AgenteSeguridad idAgente;
    @JoinColumn(name = "IdContrato", referencedColumnName = "IdContrato", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Contrato idContrato;

    public AsignacionAgente() {
    }

    public AsignacionAgente(Integer idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public AsignacionAgente(Integer idAsignacion, Date fechaAsignacion) {
        this.idAsignacion = idAsignacion;
        this.fechaAsignacion = fechaAsignacion;
    }

    public Integer getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(Integer idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Date getFechaFinAsignacion() {
        return fechaFinAsignacion;
    }

    public void setFechaFinAsignacion(Date fechaFinAsignacion) {
        this.fechaFinAsignacion = fechaFinAsignacion;
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
        hash += (idAsignacion != null ? idAsignacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsignacionAgente)) {
            return false;
        }
        AsignacionAgente other = (AsignacionAgente) object;
        if ((this.idAsignacion == null && other.idAsignacion != null) || (this.idAsignacion != null && !this.idAsignacion.equals(other.idAsignacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.empresa_de_seguridad.model.AsignacionAgente[ idAsignacion=" + idAsignacion + " ]";
    }
    
}
