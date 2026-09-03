/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.empresa_de_seguridad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author JOSUE
 */
@Entity
@Table(name = "PuntoControl")
@NamedQueries({
    @NamedQuery(name = "PuntoControl.findAll", query = "SELECT p FROM PuntoControl p"),
    @NamedQuery(name = "PuntoControl.findByIdPuntoControl", query = "SELECT p FROM PuntoControl p WHERE p.idPuntoControl = :idPuntoControl"),
    @NamedQuery(name = "PuntoControl.findByNombrePunto", query = "SELECT p FROM PuntoControl p WHERE p.nombrePunto = :nombrePunto"),
    @NamedQuery(name = "PuntoControl.findByDescripcion", query = "SELECT p FROM PuntoControl p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "PuntoControl.findByCodigoQR", query = "SELECT p FROM PuntoControl p WHERE p.codigoQR = :codigoQR"),
    @NamedQuery(name = "PuntoControl.findByLatitud", query = "SELECT p FROM PuntoControl p WHERE p.latitud = :latitud"),
    @NamedQuery(name = "PuntoControl.findByLongitud", query = "SELECT p FROM PuntoControl p WHERE p.longitud = :longitud"),
    @NamedQuery(name = "PuntoControl.findByEstado", query = "SELECT p FROM PuntoControl p WHERE p.estado = :estado")})
public class PuntoControl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdPuntoControl")
    private Integer idPuntoControl;
    @Basic(optional = false)
    @Column(name = "NombrePunto")
    private String nombrePunto;
    @Column(name = "Descripcion")
    private String descripcion;
    @Column(name = "CodigoQR")
    private String codigoQR;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Latitud")
    private BigDecimal latitud;
    @Column(name = "Longitud")
    private BigDecimal longitud;
    @Basic(optional = false)
    @Column(name = "Estado")
    private boolean estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPuntoControl")
    private Collection<RegistroPuntoControl> registroPuntoControlCollection;
    @JoinColumn(name = "IdPuesto", referencedColumnName = "IdPuesto")
    @ManyToOne(optional = false)
    private PuestoServicio idPuesto;

    public PuntoControl() {
    }

    public PuntoControl(Integer idPuntoControl) {
        this.idPuntoControl = idPuntoControl;
    }

    public PuntoControl(Integer idPuntoControl, String nombrePunto, boolean estado) {
        this.idPuntoControl = idPuntoControl;
        this.nombrePunto = nombrePunto;
        this.estado = estado;
    }

    public Integer getIdPuntoControl() {
        return idPuntoControl;
    }

    public void setIdPuntoControl(Integer idPuntoControl) {
        this.idPuntoControl = idPuntoControl;
    }

    public String getNombrePunto() {
        return nombrePunto;
    }

    public void setNombrePunto(String nombrePunto) {
        this.nombrePunto = nombrePunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigoQR() {
        return codigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        this.codigoQR = codigoQR;
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

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Collection<RegistroPuntoControl> getRegistroPuntoControlCollection() {
        return registroPuntoControlCollection;
    }

    public void setRegistroPuntoControlCollection(Collection<RegistroPuntoControl> registroPuntoControlCollection) {
        this.registroPuntoControlCollection = registroPuntoControlCollection;
    }

    public PuestoServicio getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(PuestoServicio idPuesto) {
        this.idPuesto = idPuesto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPuntoControl != null ? idPuntoControl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PuntoControl)) {
            return false;
        }
        PuntoControl other = (PuntoControl) object;
        if ((this.idPuntoControl == null && other.idPuntoControl != null) || (this.idPuntoControl != null && !this.idPuntoControl.equals(other.idPuntoControl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.empresa_de_seguridad.PuntoControl[ idPuntoControl=" + idPuntoControl + " ]";
    }
    
}
