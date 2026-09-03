/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.empresa_de_seguridad;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author JOSUE
 */
@Entity
@Table(name = "Ronda")
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
    @Column(name = "IdRonda")
    private Integer idRonda;
    @Basic(optional = false)
    @Column(name = "Fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "HoraInicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaInicio;
    @Column(name = "HoraFin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaFin;
    @Basic(optional = false)
    @Column(name = "Estado")
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRonda")
    private Collection<RegistroPuntoControl> registroPuntoControlCollection;
    @JoinColumn(name = "IdAgente", referencedColumnName = "IdAgente")
    @ManyToOne(optional = false)
    private AgenteSeguridad idAgente;
    @JoinColumn(name = "IdPuesto", referencedColumnName = "IdPuesto")
    @ManyToOne(optional = false)
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

    public Collection<RegistroPuntoControl> getRegistroPuntoControlCollection() {
        return registroPuntoControlCollection;
    }

    public void setRegistroPuntoControlCollection(Collection<RegistroPuntoControl> registroPuntoControlCollection) {
        this.registroPuntoControlCollection = registroPuntoControlCollection;
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
        return "com.mycompany.empresa_de_seguridad.Ronda[ idRonda=" + idRonda + " ]";
    }
    
}
