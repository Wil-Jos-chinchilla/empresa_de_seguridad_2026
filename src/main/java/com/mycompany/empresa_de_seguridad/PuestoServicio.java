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
@Table(name = "PuestoServicio")
@NamedQueries({
    @NamedQuery(name = "PuestoServicio.findAll", query = "SELECT p FROM PuestoServicio p"),
    @NamedQuery(name = "PuestoServicio.findByIdPuesto", query = "SELECT p FROM PuestoServicio p WHERE p.idPuesto = :idPuesto"),
    @NamedQuery(name = "PuestoServicio.findByNombrePuesto", query = "SELECT p FROM PuestoServicio p WHERE p.nombrePuesto = :nombrePuesto"),
    @NamedQuery(name = "PuestoServicio.findByDescripcion", query = "SELECT p FROM PuestoServicio p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "PuestoServicio.findByDireccion", query = "SELECT p FROM PuestoServicio p WHERE p.direccion = :direccion"),
    @NamedQuery(name = "PuestoServicio.findByLatitud", query = "SELECT p FROM PuestoServicio p WHERE p.latitud = :latitud"),
    @NamedQuery(name = "PuestoServicio.findByLongitud", query = "SELECT p FROM PuestoServicio p WHERE p.longitud = :longitud"),
    @NamedQuery(name = "PuestoServicio.findByEstado", query = "SELECT p FROM PuestoServicio p WHERE p.estado = :estado")})
public class PuestoServicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdPuesto")
    private Integer idPuesto;
    @Basic(optional = false)
    @Column(name = "NombrePuesto")
    private String nombrePuesto;
    @Column(name = "Descripcion")
    private String descripcion;
    @Column(name = "Direccion")
    private String direccion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Latitud")
    private BigDecimal latitud;
    @Column(name = "Longitud")
    private BigDecimal longitud;
    @Basic(optional = false)
    @Column(name = "Estado")
    private String estado;
    @JoinColumn(name = "IdContrato", referencedColumnName = "IdContrato")
    @ManyToOne(optional = false)
    private Contrato idContrato;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPuesto")
    private Collection<PuntoControl> puntoControlCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPuesto")
    private Collection<Ronda> rondaCollection;

    public PuestoServicio() {
    }

    public PuestoServicio(Integer idPuesto) {
        this.idPuesto = idPuesto;
    }

    public PuestoServicio(Integer idPuesto, String nombrePuesto, String estado) {
        this.idPuesto = idPuesto;
        this.nombrePuesto = nombrePuesto;
        this.estado = estado;
    }

    public Integer getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Integer idPuesto) {
        this.idPuesto = idPuesto;
    }

    public String getNombrePuesto() {
        return nombrePuesto;
    }

    public void setNombrePuesto(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public Contrato getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Contrato idContrato) {
        this.idContrato = idContrato;
    }

    public Collection<PuntoControl> getPuntoControlCollection() {
        return puntoControlCollection;
    }

    public void setPuntoControlCollection(Collection<PuntoControl> puntoControlCollection) {
        this.puntoControlCollection = puntoControlCollection;
    }

    public Collection<Ronda> getRondaCollection() {
        return rondaCollection;
    }

    public void setRondaCollection(Collection<Ronda> rondaCollection) {
        this.rondaCollection = rondaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPuesto != null ? idPuesto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PuestoServicio)) {
            return false;
        }
        PuestoServicio other = (PuestoServicio) object;
        if ((this.idPuesto == null && other.idPuesto != null) || (this.idPuesto != null && !this.idPuesto.equals(other.idPuesto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.empresa_de_seguridad.PuestoServicio[ idPuesto=" + idPuesto + " ]";
    }
    
}
