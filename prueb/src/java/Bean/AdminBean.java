package Bean;

import utils.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "adminBean")
@SessionScoped
public class AdminBean {
    private int id;
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

    // Método que se ejecuta al cargar el Bean para obtener los datos del administrador actual
    @PostConstruct
    public void cargarDatosAdministrador() {
        Conexion conexion = new Conexion();
        Connection con = conexion.getConnection();

        try {
            String query = "SELECT * FROM usuarios WHERE rol = 'Administrador'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                this.id = rs.getInt("id");
                this.nombreUsuario = rs.getString("nombre_usuario");
                this.gmail = rs.getString("gmail");
                this.telefono = rs.getString("telefono");
                this.contrasena = rs.getString("contrasena");  // Podrías cifrarla o manejarla de manera segura
            }

            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar el perfil del administrador
    public String actualizarPerfil() {
        Conexion conexion = new Conexion();
        Connection con = conexion.getConnection();

        try {
            String query = "UPDATE usuarios SET nombre_usuario = ?, gmail = ?, telefono = ?, contrasena = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, nombreUsuario);
            ps.setString(2, gmail);
            ps.setString(3, telefono);
            ps.setString(4, contrasena);  // Asegúrate de cifrar la contraseña
            ps.setInt(5, id);

            int filasActualizadas = ps.executeUpdate();

            if (filasActualizadas > 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Perfil actualizado correctamente."));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al actualizar el perfil."));
            }

            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error en la base de datos."));
        }

        return "perfil?faces-redirect=true";  // Redirige a la página del perfil
    }
}
