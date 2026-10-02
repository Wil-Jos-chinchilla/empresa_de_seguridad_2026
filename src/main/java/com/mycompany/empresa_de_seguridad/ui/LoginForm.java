package com.mycompany.empresa_de_seguridad.ui;

import com.mycompany.empresa_de_seguridad.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 inicio de sesiÃ³n.
 */
/**public class LoginForm extends JFrame {

    private static final String PERSISTENCE_UNIT_NAME = "empresa_seguridadPU";
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;

    public LoginForm() {
        setTitle("Empresa de Seguridad - Iniciar SesiÃ³n");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 260);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Iniciar SesiÃ³n", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        JLabel lblUsuario = new JLabel("Usuario:");
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblUsuario, gbc);

        txtUsuario = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(txtUsuario, gbc);

        JLabel lblContrasena = new JLabel("ContraseÃ±a:");
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblContrasena, gbc);

        txtContrasena = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(txtContrasena, gbc);

        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.addActionListener(e -> validarLogin());
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnIngresar, gbc);

        
        txtContrasena.addActionListener(e -> validarLogin());

        setContentPane(panel);
    }

    private void validarLogin() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingresa usuario y contraseÃ±a.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.nombreUsuario = :usuario "
                    + "AND u.contrasena = :contrasena AND u.estado = true",
                    Usuario.class);
            query.setParameter("usuario", usuario);
            query.setParameter("contrasena", contrasena);

            List<Usuario> resultado = query.getResultList();

            if (!resultado.isEmpty()) {
                Usuario usuarioEncontrado = resultado.get(0);
                JOptionPane.showMessageDialog(this,
                        "Â¡Bienvenido, " + usuarioEncontrado.getNombreUsuario() + "!",
                        "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);

               
                MainSeg menu = new MainSeg();
                menu.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Usuario o contraseÃ±a incorrectos, o el usuario estÃ¡ inactivo.",
                        "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                txtContrasena.setText("");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al validar el login: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm login = new LoginForm();
            login.setVisible(true);
        });
    }
}
package com.mycompany.empresa_de_seguridad.ui;

import com.mycompany.empresa_de_seguridad.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 inicio de sesión.
 */
public class LoginForm extends JFrame {

    private static final String PERSISTENCE_UNIT_NAME = "empresa_seguridadPU";
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;

    public LoginForm() {
        setTitle("Empresa de Seguridad - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 260);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        JLabel lblUsuario = new JLabel("Usuario:");
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblUsuario, gbc);

        txtUsuario = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(txtUsuario, gbc);

        JLabel lblContrasena = new JLabel("Contraseña:");
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblContrasena, gbc);

        txtContrasena = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(txtContrasena, gbc);

        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.addActionListener(e -> validarLogin());
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnIngresar, gbc);

        
        txtContrasena.addActionListener(e -> validarLogin());

        setContentPane(panel);
    }

    private void validarLogin() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingresa usuario y contraseña.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.nombreUsuario = :usuario "
                    + "AND u.contrasena = :contrasena AND u.estado = true",
                    Usuario.class);
            query.setParameter("usuario", usuario);
            query.setParameter("contrasena", contrasena);

            List<Usuario> resultado = query.getResultList();

            if (!resultado.isEmpty()) {
                Usuario usuarioEncontrado = resultado.get(0);
                JOptionPane.showMessageDialog(this,
                        "¡Bienvenido, " + usuarioEncontrado.getNombreUsuario() + "!",
                        "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);

               
                MainSeg menu = new MainSeg();
                menu.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Usuario o contraseña incorrectos, o el usuario está inactivo.",
                        "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                txtContrasena.setText("");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al validar el login: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm login = new LoginForm();
            login.setVisible(true);
        });
    }
}