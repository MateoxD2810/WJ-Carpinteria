package Bean;

import utils.Conexion;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "carpinteroBean")
@SessionScoped
public class CarpinteroBean {
    private int id;
    private String nombreUsuario;
    private String gmail;
    private String telefono;
    private String contrasena;
    
    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
    
    // Método para validar formato de correo electrónico
    public boolean validarEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(regex);
    }

    // Método para hacer hash de contraseñas
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedPassword) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para listar carpinteros
    public List<CarpinteroBean> listarCarpinteros() {
        List<CarpinteroBean> lista = new ArrayList<>();
        Conexion conexion = new Conexion();
        Connection con = conexion.getConnection();
        if (con == null) {
            return null;
        }
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM usuarios WHERE rol = 'Carpintero'";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                CarpinteroBean carpintero = new CarpinteroBean();
                carpintero.setId(rs.getInt("id"));
                carpintero.setNombreUsuario(rs.getString("nombre_usuario"));
                carpintero.setGmail(rs.getString("gmail"));
                carpintero.setTelefono(rs.getString("telefono"));
                // No devolvemos contraseñas en el listado
                lista.add(carpintero);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                conexion.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    // Método para agregar carpintero
    public String agregarCarpintero() {
        // Validación de email
        if (!validarEmail(gmail)) {
            System.out.println("Formato de email no válido");
            return null;
        }
        
        Conexion conexion = new Conexion();
        Connection con = conexion.getConnection();
        if (con == null) {
            return null;
        }

        PreparedStatement ps = null;
        try {
            String query = "INSERT INTO usuarios (nombre_usuario, gmail, telefono, contrasena, rol) VALUES (?, ?, ?, ?, 'Carpintero')";
            ps = con.prepareStatement(query);
            ps.setString(1, nombreUsuario);
            ps.setString(2, gmail);
            ps.setString(3, telefono);
            ps.setString(4, hashPassword(contrasena));  // Almacenamos la contraseña en hash

            int result = ps.executeUpdate();
            if (result > 0) {
                return "carpinteros?faces-redirect=true";  // Redirige a la lista de carpinteros
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (ps != null) ps.close();
                conexion.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para eliminar carpintero
    public void eliminarCarpintero(int id) {
        Conexion conexion = new Conexion();
        Connection con = conexion.getConnection();
        if (con == null) {
            return;
        }

        PreparedStatement ps = null;
        try {
            String query = "DELETE FROM usuarios WHERE id = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                conexion.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para actualizar carpintero
    public String actualizarCarpintero() {
        // Validación de email
        if (!validarEmail(gmail)) {
            System.out.println("Formato de email no válido");
            return null;
        }
        
        Conexion conexion = new Conexion();
        Connection con = conexion.getConnection();
        if (con == null) {
            return null;
        }

        PreparedStatement ps = null;
        try {
            String query = "UPDATE usuarios SET nombre_usuario = ?, gmail = ?, telefono = ?, contrasena = ? WHERE id = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, nombreUsuario);
            ps.setString(2, gmail);
            ps.setString(3, telefono);
            ps.setString(4, hashPassword(contrasena));  // Almacenamos la contraseña en hash
            ps.setInt(5, id);

            int result = ps.executeUpdate();
            if (result > 0) {
                return "carpinteros?faces-redirect=true";  // Redirige a la lista de carpinteros
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (ps != null) ps.close();
                conexion.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
