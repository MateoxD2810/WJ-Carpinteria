package Bean;

import utils.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "clienteBean")
@RequestScoped
public class ClienteBean {
    private List<Usuario> usuarios;

    // Método para obtener los usuarios con rol 'Usuario'
    public List<Usuario> getUsuarios() {
        if (usuarios == null) {
            usuarios = new ArrayList<>();
            Conexion conexion = new Conexion();
            Connection con = conexion.getConnection();

            try {
                String query = "SELECT id, nombre_usuario, gmail, telefono FROM usuarios WHERE rol = 'Usuario'";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setGmail(rs.getString("gmail"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuarios.add(usuario);
                }

                rs.close();
                ps.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return usuarios;
    }
}
