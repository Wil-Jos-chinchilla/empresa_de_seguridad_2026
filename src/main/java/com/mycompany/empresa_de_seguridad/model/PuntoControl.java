/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.empresa_de_seguridad.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 *
 * @author JOSUE
 */
@Entity
@Table(name = "PuntoControl", catalog = "EmpresaSeguridad", schema = "dbo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"CodigoQR"})})
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
    @Column(name = "IdPuntoControl", nullable = false)
    private Integer idPuntoControl;
    @Basic(optional = false)
    @Column(name = "NombrePunto", nullable = false, length = 100)
    private String nombrePunto;
    @Column(name = "Descripcion", length = 250)
    private String descripcion;
    @Column(name = "CodigoQR", length = 150)
    private String codigoQR;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Latitud", precision = 10, scale = 7)
    private BigDecimal latitud;
    @Column(name = "Longitud", precision = 10, scale = 7)
    private BigDecimal longitud;
    @Basic(optional = false)
    @Column(name = "Estado", nullable = false)
    private boolean estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPuntoControl", fetch = FetchType.LAZY)
    private List<RegistroPuntoControl> registroPuntoControlList;
    @JoinColumn(name = "IdPuesto", referencedColumnName = "IdPuesto", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
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

    public List<RegistroPuntoControl> getRegistroPuntoControlList() {
        return registroPuntoControlList;
    }

    public void setRegistroPuntoControlList(List<RegistroPuntoControl> registroPuntoControlList) {
        this.registroPuntoControlList = registroPuntoControlList;
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
        return "com.mycompany.empresa_de_seguridad.model.PuntoControl[ idPuntoControl=" + idPuntoControl + " ]";
    }
    
}
