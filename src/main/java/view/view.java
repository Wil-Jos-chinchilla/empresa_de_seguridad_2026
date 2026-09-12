/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import com.mycompany.empresa_de_seguridad.jpacontroller.AgenteSeguridadJpaController;
import com.mycompany.empresa_de_seguridad.model.AgenteSeguridad;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JOSUE
 */
public class view {

    public static void main(String[] args) {
        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("empresa_seguridadPU");

        EntityManager em = emf.createEntityManager();
        try {
            AgenteSeguridadJpaController controller = new AgenteSeguridadJpaController(emf);

            AgenteSeguridad nuevoAgente = new AgenteSeguridad();
            nuevoAgente.setNombre("Juan");
            nuevoAgente.setApellido("Pérez");
            nuevoAgente.setDpi("1234567890101");
            nuevoAgente.setTelefono("55501234");
            nuevoAgente.setDireccion("Zona 1, Guatemala");
            nuevoAgente.setFechaIngreso(new SimpleDateFormat("yyyy-MM-dd").parse("2026-09-09"));
            nuevoAgente.setEstado("DISPONIBLE");

            controller.create(nuevoAgente);
            System.out.println("¡Agente creado con éxito! ID asignado: " + nuevoAgente.getIdAgente());

            List<AgenteSeguridad> lista = controller.findAgenteSeguridadEntities();
            System.out.println("Total de agentes ahora: " + lista.size());
            for (AgenteSeguridad a : lista) {
                System.out.println("Agente: " + a.getNombre() + " " + a.getApellido() + " - Estado: " + a.getEstado());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            emf.close();
        }
            AgenteSeguridadJpaController agenteC = new AgenteSeguridadJpaController(emf);

            List<AgenteSeguridad> lstAgente = new ArrayList<>();
            lstAgente = agenteC.findAgenteSeguridadEntities();
            for (AgenteSeguridad a : lstAgente) {
                System.out.println("Primer Nombre " + a.getNombre());
                System.out.println("Primer Apellido " + a.getApellido());
                System.out.println("--------------");
            }
    }
}
