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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author JOSUE
 */
@Entity
@Table(name = "PlanServicio", catalog = "EmpresaSeguridad", schema = "dbo")
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
    @Column(name = "IdPlan", nullable = false)
    private Integer idPlan;
    @Basic(optional = false)
    @Column(name = "NombrePlan", nullable = false, length = 100)
    private String nombrePlan;
    @Column(name = "Descripcion", length = 300)
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "PrecioMensual", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioMensual;
    @Basic(optional = false)
    @Column(name = "Estado", nullable = false)
    private boolean estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPlan", fetch = FetchType.LAZY)
    private List<Contrato> contratoList;

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

    public List<Contrato> getContratoList() {
        return contratoList;
    }

    public void setContratoList(List<Contrato> contratoList) {
        this.contratoList = contratoList;
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
        return "com.mycompany.empresa_de_seguridad.model.PlanServicio[ idPlan=" + idPlan + " ]";
    }
    
}
