/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.empresa_de_seguridad.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "RegistroPuntoControl", catalog = "EmpresaSeguridad", schema = "dbo")
@NamedQueries({
    @NamedQuery(name = "RegistroPuntoControl.findAll", query = "SELECT r FROM RegistroPuntoControl r"),
    @NamedQuery(name = "RegistroPuntoControl.findByIdRegistro", query = "SELECT r FROM RegistroPuntoControl r WHERE r.idRegistro = :idRegistro"),
    @NamedQuery(name = "RegistroPuntoControl.findByFechaHora", query = "SELECT r FROM RegistroPuntoControl r WHERE r.fechaHora = :fechaHora"),
    @NamedQuery(name = "RegistroPuntoControl.findByMetodoRegistro", query = "SELECT r FROM RegistroPuntoControl r WHERE r.metodoRegistro = :metodoRegistro"),
    @NamedQuery(name = "RegistroPuntoControl.findByLatitud", query = "SELECT r FROM RegistroPuntoControl r WHERE r.latitud = :latitud"),
    @NamedQuery(name = "RegistroPuntoControl.findByLongitud", query = "SELECT r FROM RegistroPuntoControl r WHERE r.longitud = :longitud"),
    @NamedQuery(name = "RegistroPuntoControl.findByObservacion", query = "SELECT r FROM RegistroPuntoControl r WHERE r.observacion = :observacion")})
public class RegistroPuntoControl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdRegistro", nullable = false)
    private Integer idRegistro;
    @Basic(optional = false)
    @Column(name = "FechaHora", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Basic(optional = false)
    @Column(name = "MetodoRegistro", nullable = false, length = 20)
    private String metodoRegistro;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Latitud", precision = 10, scale = 7)
    private BigDecimal latitud;
    @Column(name = "Longitud", precision = 10, scale = 7)
    private BigDecimal longitud;
    @Column(name = "Observacion", length = 250)
    private String observacion;
    @JoinColumn(name = "IdPuntoControl", referencedColumnName = "IdPuntoControl", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PuntoControl idPuntoControl;
    @JoinColumn(name = "IdRonda", referencedColumnName = "IdRonda", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Ronda idRonda;

    public RegistroPuntoControl() {
    }

    public RegistroPuntoControl(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public RegistroPuntoControl(Integer idRegistro, Date fechaHora, String metodoRegistro) {
        this.idRegistro = idRegistro;
        this.fechaHora = fechaHora;
        this.metodoRegistro = metodoRegistro;
    }

    public Integer getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMetodoRegistro() {
        return metodoRegistro;
    }

    public void setMetodoRegistro(String metodoRegistro) {
        this.metodoRegistro = metodoRegistro;
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public PuntoControl getIdPuntoControl() {
        return idPuntoControl;
    }

    public void setIdPuntoControl(PuntoControl idPuntoControl) {
        this.idPuntoControl = idPuntoControl;
    }

    public Ronda getIdRonda() {
        return idRonda;
    }

    public void setIdRonda(Ronda idRonda) {
        this.idRonda = idRonda;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRegistro != null ? idRegistro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegistroPuntoControl)) {
            return false;
        }
        RegistroPuntoControl other = (RegistroPuntoControl) object;
        if ((this.idRegistro == null && other.idRegistro != null) || (this.idRegistro != null && !this.idRegistro.equals(other.idRegistro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.empresa_de_seguridad.model.RegistroPuntoControl[ idRegistro=" + idRegistro + " ]";
    }
    
}
