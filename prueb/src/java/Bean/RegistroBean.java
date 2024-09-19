package Bean;

import utils.Conexion; // Importa tu clase de conexión
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "registroBean")
@RequestScoped
public class RegistroBean {
    private String nombreUsuario;
    private String gmail;
    private String telefono;
    private String contrasena;

    // Getters y Setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    // Método para registrar el usuario
    public String registrarUsuario() {
        // Validaciones básicas
        if (nombreUsuario == null || nombreUsuario.length() < 4) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El nombre de usuario debe tener al menos 4 caracteres."));
            return null;
        }
        if (contrasena == null || contrasena.length() < 8) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La contraseña debe tener al menos 8 caracteres."));
            return null;
        }

        // Se elimina la validación de formato del correo electrónico
        if (gmail == null || gmail.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El correo electrónico no puede estar vacío."));
            return null;
        }

        Conexion conexion = new Conexion();  // Crear instancia de la clase de conexión
        Connection con = conexion.getConnection(); // Obtener la conexión

        if (con == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al conectar con la base de datos."));
            return "registro?faces-redirect=true";
        }

        PreparedStatement ps = null;
        try {
            String query = "INSERT INTO usuarios (nombre_usuario, gmail, telefono, contrasena, rol) VALUES (?, ?, ?, ?, 'Usuario')";  // Por defecto, todos los registrados son usuarios normales
            ps = con.prepareStatement(query);
            ps.setString(1, nombreUsuario);
            ps.setString(2, gmail);
            ps.setString(3, telefono);
            ps.setString(4, contrasena);

            int result = ps.executeUpdate();
            if (result > 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario registrado exitosamente."));
                return "login?faces-redirect=true";  // Redirige al login después del registro
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al registrar el usuario."));
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al procesar el registro."));
            return null;
        } finally {
            try {
                if (ps != null) ps.close();
                conexion.closeConnection(); // Cerrar la conexión correctamente
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
