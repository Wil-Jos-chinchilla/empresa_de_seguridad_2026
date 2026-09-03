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
@Table(name = "RegistroPuntoControl")
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
    @Column(name = "IdRegistro")
    private Integer idRegistro;
    @Basic(optional = false)
    @Column(name = "FechaHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Basic(optional = false)
    @Column(name = "MetodoRegistro")
    private String metodoRegistro;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Latitud")
    private BigDecimal latitud;
    @Column(name = "Longitud")
    private BigDecimal longitud;
    @Column(name = "Observacion")
    private String observacion;
    @JoinColumn(name = "IdPuntoControl", referencedColumnName = "IdPuntoControl")
    @ManyToOne(optional = false)
    private PuntoControl idPuntoControl;
    @JoinColumn(name = "IdRonda", referencedColumnName = "IdRonda")
    @ManyToOne(optional = false)
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
        return "com.mycompany.empresa_de_seguridad.RegistroPuntoControl[ idRegistro=" + idRegistro + " ]";
    }
    
}
