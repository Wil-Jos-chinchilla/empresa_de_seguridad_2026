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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author JOSUE
 */
@Entity
@Table(name = "PlanServicio")
@NamedQueries({
    @NamedQuery(name = "PlanServicio.findAll", query = "SELECT p FROM PlanServicio p"),
    @NamedQuery(name = "PlanServicio.findByIdPlan", query = "SELECT p FROM PlanServicio p WHERE p.idPlan = :idPlan"),
    @NamedQuery(name = "PlanServicio.findByNombrePlan", query = "SELECT p FROM PlanServicio p WHERE p.nombrePlan = :nombrePlan"),
    @NamedQuery(name = "PlanServicio.findByDescripcion", query = "SELECT p FROM PlanServicio p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "PlanServicio.findByPrecioMensual", query = "SELECT p FROM PlanServicio p WHERE p.precioMensual = :precioMensual"),
    @NamedQuery(name = "PlanServicio.findByEstado", query = "SELECT p FROM PlanServicio p WHERE p.estado = :estado")})
public class PlanServicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdPlan")
    private Integer idPlan;
    @Basic(optional = false)
    @Column(name = "NombrePlan")
    private String nombrePlan;
    @Column(name = "Descripcion")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "PrecioMensual")
    private BigDecimal precioMensual;
    @Basic(optional = false)
    @Column(name = "Estado")
    private boolean estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPlan")
    private Collection<Contrato> contratoCollection;

    public PlanServicio() {
    }

    public PlanServicio(Integer idPlan) {
        this.idPlan = idPlan;
    }

    public PlanServicio(Integer idPlan, String nombrePlan, BigDecimal precioMensual, boolean estado) {
        this.idPlan = idPlan;
        this.nombrePlan = nombrePlan;
        this.precioMensual = precioMensual;
        this.estado = estado;
    }

    public Integer getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Integer idPlan) {
        this.idPlan = idPlan;
    }

    public String getNombrePlan() {
        return nombrePlan;
    }

    public void setNombrePlan(String nombrePlan) {
        this.nombrePlan = nombrePlan;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioMensual() {
        return precioMensual;
    }

    public void setPrecioMensual(BigDecimal precioMensual) {
        this.precioMensual = precioMensual;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Collection<Contrato> getContratoCollection() {
        return contratoCollection;
    }

    public void setContratoCollection(Collection<Contrato> contratoCollection) {
        this.contratoCollection = contratoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlan != null ? idPlan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanServicio)) {
            return false;
        }
        PlanServicio other = (PlanServicio) object;
        if ((this.idPlan == null && other.idPlan != null) || (this.idPlan != null && !this.idPlan.equals(other.idPlan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.empresa_de_seguridad.PlanServicio[ idPlan=" + idPlan + " ]";
    }
    
}
