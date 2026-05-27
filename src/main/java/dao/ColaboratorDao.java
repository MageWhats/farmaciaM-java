package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import models.Colaborator;

public class ColaboratorDao {

    private final ConnectionMySQL cn = new ConnectionMySQL();

    public static int id_user = 0;
    public static int id_card = 0;
    public static String fullName_user = "";
    public static String username_user = "";
    public static String rol_user = "";

    //Inicio sesion
    public Colaborator loginQuery(String user, String password) {
        String query = "SELECT * FROM employees WHERE user_name = ? AND password = ?";
        Colaborator colaborator = new Colaborator();
        try (Connection conn = cn.getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, user);
            pst.setString(2, password);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    colaborator = mapResultSetToEmployee(rs);
                    id_user = colaborator.getId();
                    fullName_user = colaborator.getFullName();
                    username_user = colaborator.getUsername();
                    rol_user = colaborator.getRol();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en login: " + e.getMessage());
        }
        return colaborator;
    }

    //Registrar Colaboradores
    public boolean registerColaboratorQuery(Colaborator colaborator) {
        String query = "INSERT INTO employees (id_card, full_name, user_name, address, telephone, email, password, rol, created, updated) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Timestamp dateTime = new Timestamp(new Date().getTime());
        try (Connection conn = cn.getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, colaborator.getIdCard());
            pst.setString(2, colaborator.getFullName());
            pst.setString(3, colaborator.getUsername());
            pst.setString(4, colaborator.getAddress());
            pst.setString(5, colaborator.getTelephone());
            pst.setString(6, colaborator.getEmail());
            pst.setString(7, colaborator.getPassword());
            pst.setString(8, colaborator.getRol());
            pst.setTimestamp(9, dateTime);
            pst.setTimestamp(10, dateTime);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar: " + e.getMessage());
            return false;
        }
    }

    //Listar Colaboradores
    public List<Colaborator> listColaboratorQuery(String value) {
        List<Colaborator> listColaborator = new ArrayList<>();
        String queryAll = "SELECT * FROM employees ORDER BY full_name ASC";
        String querySearch = "SELECT * FROM employees WHERE full_name LIKE ? ORDER BY full_name ASC";
        try (Connection conn = cn.getConnection()) {
            if (value == null || value.trim().isEmpty()) {
                try (PreparedStatement pst = conn.prepareStatement(queryAll); ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        listColaborator.add(mapResultSetToEmployee(rs));
                    }
                }
            } else {
                try (PreparedStatement pst = conn.prepareStatement(querySearch)) {
                    pst.setString(1, "%" + value + "%");
                    try (ResultSet rs = pst.executeQuery()) {
                        while (rs.next()) {
                            listColaborator.add(mapResultSetToEmployee(rs));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar: " + e.getMessage());
        }
        return listColaborator;
    }

    //Modificar Colaboradores
    public boolean updateColaboratorQuery(Colaborator colaborator) {
        String query = "UPDATE employees SET id_card=?, full_name=?, user_name=?, address=?, telephone=?, email=?, password=?, rol=?, updated=? WHERE id=?";
        Timestamp dateTime = new Timestamp(new Date().getTime());
        try (Connection conn = cn.getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, colaborator.getIdCard());
            pst.setString(2, colaborator.getFullName());
            pst.setString(3, colaborator.getUsername());
            pst.setString(4, colaborator.getAddress());
            pst.setString(5, colaborator.getTelephone());
            pst.setString(6, colaborator.getEmail());
            pst.setString(7, colaborator.getPassword());
            pst.setString(8, colaborator.getRol());
            pst.setTimestamp(9, dateTime);
            pst.setInt(10, colaborator.getId());
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar: " + e.getMessage());
            return false;
        }
    }

    //Eliminar Colaboradores
    public boolean deleteColaboratorQuery(int id) {
        String query = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = cn.getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    private Colaborator mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Colaborator colaborator = new Colaborator();
        colaborator.setId(rs.getInt("id"));
        colaborator.setIdCard(rs.getInt("id_card"));
        colaborator.setFullName(rs.getString("full_name"));
        colaborator.setUsername(rs.getString("user_name"));
        colaborator.setAddress(rs.getString("address"));
        colaborator.setTelephone(rs.getString("telephone"));
        colaborator.setEmail(rs.getString("email"));
        colaborator.setPassword(rs.getString("password"));
        colaborator.setRol(rs.getString("rol"));
        if (rs.getTimestamp("created") != null) {
            colaborator.setCreated(rs.getTimestamp("created").toString());
        } else {
            colaborator.setCreated("");
        }

        if (rs.getTimestamp("updated") != null) {
            colaborator.setUpdated(rs.getTimestamp("updated").toString());
        } else {
            colaborator.setUpdated("");
        }
        return colaborator;
    }

}
