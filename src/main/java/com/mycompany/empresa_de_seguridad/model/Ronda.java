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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author JOSUE
 */
@Entity
@Table(name = "Ronda", catalog = "EmpresaSeguridad", schema = "dbo")
@NamedQueries({
    @NamedQuery(name = "Ronda.findAll", query = "SELECT r FROM Ronda r"),
    @NamedQuery(name = "Ronda.findByIdRonda", query = "SELECT r FROM Ronda r WHERE r.idRonda = :idRonda"),
    @NamedQuery(name = "Ronda.findByFecha", query = "SELECT r FROM Ronda r WHERE r.fecha = :fecha"),
    @NamedQuery(name = "Ronda.findByHoraInicio", query = "SELECT r FROM Ronda r WHERE r.horaInicio = :horaInicio"),
    @NamedQuery(name = "Ronda.findByHoraFin", query = "SELECT r FROM Ronda r WHERE r.horaFin = :horaFin"),
    @NamedQuery(name = "Ronda.findByEstado", query = "SELECT r FROM Ronda r WHERE r.estado = :estado")})
public class Ronda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdRonda", nullable = false)
    private Integer idRonda;
    @Basic(optional = false)
    @Column(name = "Fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "HoraInicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaInicio;
    @Column(name = "HoraFin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaFin;
    @Basic(optional = false)
    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRonda", fetch = FetchType.LAZY)
    private List<RegistroPuntoControl> registroPuntoControlList;
    @JoinColumn(name = "IdAgente", referencedColumnName = "IdAgente", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AgenteSeguridad idAgente;
    @JoinColumn(name = "IdPuesto", referencedColumnName = "IdPuesto", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PuestoServicio idPuesto;

    public Ronda() {
    }

    public Ronda(Integer idRonda) {
        this.idRonda = idRonda;
    }

    public Ronda(Integer idRonda, Date fecha, String estado) {
        this.idRonda = idRonda;
        this.fecha = fecha;
        this.estado = estado;
    }

    public Integer getIdRonda() {
        return idRonda;
    }

    public void setIdRonda(Integer idRonda) {
        this.idRonda = idRonda;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<RegistroPuntoControl> getRegistroPuntoControlList() {
        return registroPuntoControlList;
    }

    public void setRegistroPuntoControlList(List<RegistroPuntoControl> registroPuntoControlList) {
        this.registroPuntoControlList = registroPuntoControlList;
    }

    public AgenteSeguridad getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(AgenteSeguridad idAgente) {
        this.idAgente = idAgente;
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
        hash += (idRonda != null ? idRonda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ronda)) {
            return false;
        }
        Ronda other = (Ronda) object;
        if ((this.idRonda == null && other.idRonda != null) || (this.idRonda != null && !this.idRonda.equals(other.idRonda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.empresa_de_seguridad.model.Ronda[ idRonda=" + idRonda + " ]";
    }
    
}
