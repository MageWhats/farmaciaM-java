package controllers;

import models.Colaborator;
import dao.ColaboratorDao;
import views.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ColaboratorsController implements ActionListener, MouseListener, KeyListener {

    private final Colaborator colaborator;
    private final ColaboratorDao colaboratorDao;
    private final SystemView views;
    private DefaultTableModel model = new DefaultTableModel();

    public ColaboratorsController(Colaborator colaborator, ColaboratorDao colaboratorDao, SystemView views) {
        this.colaborator = colaborator;
        this.colaboratorDao = colaboratorDao;
        this.views = views;

        this.views.btn_colaborator_register.addActionListener(this); 
        this.views.btn_colaborator_update.addActionListener(this);   
        this.views.btn_colaborator_delete.addActionListener(this);   
        this.views.btn_colaborator_cancel.addActionListener(this);   

        this.views.txt_colaborator_search.addKeyListener(this);
        this.views.tb_colaborator.addMouseListener(this);
        this.views.jLabelColaborator.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_colaborator_register) {
            executeRegister();
        } else if (e.getSource() == views.btn_colaborator_update) {
            executeUpdate();
        } else if (e.getSource() == views.btn_colaborator_delete) {
            executeDelete();
        } else if (e.getSource() == views.btn_colaborator_cancel) {
            cleanFields();
            views.btn_colaborator_register.setEnabled(true);
            views.txt_colaborator_password.setEnabled(true);
        }
    }

    private void executeRegister() {
        if (areFieldsEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Captura de la identificación desde la caja de texto del diseño
            colaborator.setId(Integer.parseInt(views.txt_colaborator_id.getText().trim()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "La identificación debe ser numérica.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }
        colaborator.setIdCard(Integer.parseInt(views.txt_colaborator_id_card.getText().trim()));
        colaborator.setFullName(views.txt_colaborator_name.getText().trim());
        colaborator.setUsername(views.txt_colaborator_username.getText().trim());
        colaborator.setAddress(views.txt_colaborator_address.getText().trim()); 
        colaborator.setTelephone(views.txt_colaborator_telephone.getText().trim());
        colaborator.setEmail(views.txt_colaborator_email.getText().trim());
        colaborator.setPassword(String.valueOf(views.txt_colaborator_password.getPassword()).trim());
        colaborator.setRol(views.cb_colaborator_rol.getSelectedItem().toString().trim());

        if (colaboratorDao.registerColaboratorQuery(colaborator)) {
            JOptionPane.showMessageDialog(null, "Colaborador registrado exitosamente.");
            cleanFields();
            listAllEmployees();
        }
    }

    private void executeUpdate() {
        if (views.txt_colaborator_id.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro para continuar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (areFieldsEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            colaborator.setId(Integer.parseInt(views.txt_colaborator_id.getText().trim()));
            colaborator.setIdCard(Integer.parseInt(views.txt_colaborator_id_card.getText().trim()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Verifique los valores numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        colaborator.setFullName(views.txt_colaborator_name.getText().trim());
        colaborator.setUsername(views.txt_colaborator_username.getText().trim());
        colaborator.setAddress(views.txt_colaborator_address.getText().trim());
        colaborator.setTelephone(views.txt_colaborator_telephone.getText().trim());
        colaborator.setEmail(views.txt_colaborator_email.getText().trim());
        colaborator.setPassword(String.valueOf(views.txt_colaborator_password.getPassword()).trim());
        colaborator.setRol(views.cb_colaborator_rol.getSelectedItem().toString().trim());

        if (colaboratorDao.updateColaboratorQuery(colaborator)) {
            cleanFields();
            listAllEmployees();
            views.btn_colaborator_register.setEnabled(true);
            views.txt_colaborator_password.setEnabled(true);
            JOptionPane.showMessageDialog(null, "Datos modificados de manera correcta.");
        }
    }

    private void executeDelete() {
        int row = views.tb_colaborator.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un colaborador.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = Integer.parseInt(views.tb_colaborator.getValueAt(row, 0).toString());
            if (id == ColaboratorDao.id_user) {
                JOptionPane.showMessageDialog(null, "No puedes eliminar tu propio usuario en sesión.", "Restricción", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int question = JOptionPane.showConfirmDialog(null, "¿Eliminar este colaborador?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (question == JOptionPane.YES_OPTION && colaboratorDao.deleteColaboratorQuery(id)) {
                cleanFields();
                listAllEmployees();
                views.btn_colaborator_register.setEnabled(true);
                JOptionPane.showMessageDialog(null, "Colaborador eliminado.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Error de ID.");
        }
    }

    private boolean areFieldsEmpty() {
        return views.txt_colaborator_id_card.getText().trim().isEmpty()
                || views.txt_colaborator_name.getText().trim().isEmpty()
                || views.txt_colaborator_username.getText().trim().isEmpty()
                || views.txt_colaborator_address.getText().trim().isEmpty()
                || views.txt_colaborator_telephone.getText().trim().isEmpty()
                || views.txt_colaborator_email.getText().trim().isEmpty();
                
    }

    public void cleanFields() {
        views.txt_colaborator_id.setText("");
        views.txt_colaborator_id_card.setText("");
        views.txt_colaborator_name.setText("");
        views.txt_colaborator_username.setText("");
        views.txt_colaborator_address.setText("");
        views.txt_colaborator_telephone.setText("");
        views.txt_colaborator_email.setText("");
        views.txt_colaborator_password.setText("");
    }

    public void listAllEmployees() {
        List<Colaborator> list = colaboratorDao.listColaboratorQuery(views.txt_colaborator_search.getText().trim());
        model = (DefaultTableModel) views.tb_colaborator.getModel();
        model.setRowCount(0);
        for (Colaborator col : list) {
            Object[] row = new Object[8];
            row[0] = col.getId();
            row[1] = col.getIdCard(); 
            row[2] = col.getFullName();
            row[3] = col.getUsername();
            row[4] = col.getAddress();
            row[5] = col.getTelephone();
            row[6] = col.getEmail();
            row[7] = col.getRol();
            model.addRow(row);
        }
        views.tb_colaborator.setModel(model);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.tb_colaborator) {
            int row = views.tb_colaborator.getSelectedRow();
            if (row != -1) {
                views.txt_colaborator_id.setText(views.tb_colaborator.getValueAt( row, 0).toString());
                views.txt_colaborator_id_card.setText(views.tb_colaborator.getValueAt(row, 1).toString());
                views.txt_colaborator_name.setText(views.tb_colaborator.getValueAt(row, 2).toString());
                views.txt_colaborator_username.setText(views.tb_colaborator.getValueAt(row, 3).toString());
                views.txt_colaborator_address.setText(views.tb_colaborator.getValueAt(row, 4).toString());
                views.txt_colaborator_telephone.setText(views.tb_colaborator.getValueAt(row, 5).toString());
                views.txt_colaborator_email.setText(views.tb_colaborator.getValueAt(row, 6).toString());
                views.cb_colaborator_rol.setSelectedItem(views.tb_colaborator.getValueAt(row, 7).toString());
                
                views.btn_colaborator_register.setEnabled(false);
                views.txt_colaborator_password.setEnabled(false); 
            }
        } else if (e.getSource() == views.jLabelColaborator) {
            views.jTabbedPane1.setSelectedIndex(4);
            listAllEmployees();
        }
    }

    @Override public void keyReleased(KeyEvent e) {
        if (e.getSource() == views.txt_colaborator_search) { listAllEmployees(); }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}
}
