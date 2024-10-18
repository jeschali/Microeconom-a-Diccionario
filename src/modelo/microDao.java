package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class microDao {

    Conexion conectar = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public List<micro> listar() {
        List<micro> datos = new ArrayList<>();
        String sql = "SELECT * FROM micro";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                micro p = new micro();
                p.setId(rs.getInt(1));
                p.setPalabra(rs.getString(2));
                p.setDefinicion(rs.getString(3));
                datos.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datos;
    }

    public int agregar(micro p) {
        String sql = "INSERT INTO micro(Palabra, Definicion) VALUES(?,?)";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getPalabra());
            ps.setString(2, p.getDefinicion());
            return ps.executeUpdate(); // Devolver resultado de la operación
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Retornar 0 si ocurre un error
        }
    }

    public int Actualizar(micro p) {
        String sql = "UPDATE micro SET Palabra = ?, Definicion = ? WHERE id = ?";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getPalabra());
            ps.setString(2, p.getDefinicion());
            ps.setInt(3, p.getId()); // Asegurarse de usar el ID en la consulta
            return ps.executeUpdate(); // Devolver resultado de la operación
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Retornar 0 si ocurre un error
        }
    }
    
    public void delete(int id){
        String sql = "DELETE FROM micro WHERE id ="+id;
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
        }
    }
}
