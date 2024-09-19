package Bean;

import utils.Conexion; // Importa tu clase de conexión
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean {
    private String nombreUsuario;
    private String contrasena;
    private String rolSeleccionado; // Campo para almacenar el rol seleccionado

    // Getters y Setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(String rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

    // Método para iniciar sesión
    public String iniciarSesion() {
        Conexion conexion = new Conexion();  // Crear instancia de la clase de conexión
        Connection con = conexion.getConnection(); // Obtener la conexión

        if (con == null) {
            System.out.println("Error al conectar con la base de datos.");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al conectar con la base de datos."));
            return "login?faces-redirect=true";
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, nombreUsuario);
            ps.setString(2, contrasena);

            rs = ps.executeQuery();
            if (rs.next()) {
                // Login exitoso, verificar el rol seleccionado
                switch (rolSeleccionado) {
    case "Usuario":
        return "/Cliente/rolCliente/Inicio.xhtml?faces-redirect=true";  // Asegúrate de que esta ruta sea válida
    case "Carpintero":
        return "/Carpintero/index.xhtml?faces-redirect=true";
    case "Administrador":
        return "/Administrador/index.xhtml?faces-redirect=true";
    default:
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Rol no válido."));
        return "login?faces-redirect=true";
}

            } else {
                // Si las credenciales no coinciden
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nombre de usuario o contraseña incorrectos."));
                return "login?faces-redirect=true";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "login?faces-redirect=true";
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close(); // Cierra también la conexión
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
