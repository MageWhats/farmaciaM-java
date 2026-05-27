package controllers;

import java.awt.event.ActionListener;
import dao.ColaboratorDao;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import models.Colaborator;
import views.LoginView;
import views.SystemView;

public class LoginController implements ActionListener {

    private final Colaborator colaborator;
    private final ColaboratorDao colaboratorDao;
    private final LoginView views;

    public LoginController(Colaborator colaborator, ColaboratorDao colaboratorDao, LoginView views) {
        this.colaborator = colaborator;
        this.colaboratorDao = colaboratorDao;
        this.views = views;

        // Registrar los escuchadores para la pantalla de Login
        this.views.btn_login.addActionListener(this);

        // Permite dar ENTER en la caja de contraseña para iniciar sesión rápido
        this.views.txt_password.addActionListener(this);

        // Centrar la pantalla al arrancar
        this.views.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_login || e.getSource() == views.txt_password) {
            executeLoginProcess();
        }
    }

    private void executeLoginProcess() {
        String user = views.txt_username.getText().trim();
        String pass = String.valueOf(views.txt_password.getPassword()).trim();

        // Validar campos vacíos con .isEmpty() eficiente
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese su usuario y contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Consultar a la base de datos con el método blindado contra Inyección SQL que ya hicimos
        Colaborator colActive = colaboratorDao.loginQuery(user, pass);

        if (colActive.getUsername() != null) {
            JOptionPane.showMessageDialog(null, "¡Bienvenido al sistema, " + colActive.getFullName() + "!", "Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);

            // 1. Instanciamos la vista principal de la farmacia
            SystemView system = new SystemView();

            // 2. Inicializamos todos los controladores pasándoles la misma vista principal
            initAllSystemControllers(system);

            // 3. Mostramos la pantalla principal y cerramos el Login
            system.setVisible(true);
            views.dispose();

        } else {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos. Intente de nuevo.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            views.txt_password.setText("");
            views.txt_password.requestFocus();
        }
    }

    private void initAllSystemControllers(SystemView system) {
          // Inicializar el controlador de productos
        new controllers.ProductsController(new models.Products(), new dao.ProductsDao(), system);
        
        // Inicializar el controlador de compras
       new controllers.PurchasesController(new models.Purchases(), new dao.PurchasesDao(), new dao.ProductsDao(), system);
        
        // Inicializar el controlador de ventas
        new controllers.SalesController(new models.Sales(), new dao.SalesDao(), new dao.ProductsDao(), system, new dao.CustomersDao());
        
        // Inicializar el controlador de clientes
        new controllers.CustomersController(new models.Customers(), new dao.CustomersDao(), system);
        
        // Inicializar el controlador de colaboradores
        new controllers.ColaboratorsController(new models.Colaborator(), new dao.ColaboratorDao(), system);
        
        // Inicializar el controlador de proveedores
        new controllers.SuppliersController(new models.Suppliers(), new dao.SuppliersDao(), system);
        
        // Inicializar el controlador de categorías
        new controllers.CategoriesController(new models.Categories(), new dao.CategoriesDao(), system);
        
        // Inicializar el controlador de reportes históricos
        new controllers.ReportsController(new dao.SalesDao(), new dao.PurchasesDao(), system);
        
        // Inicializar el controlador de editar perfil / configuración
        new controllers.SettingsController(new models.Colaborator(), new dao.ColaboratorDao(), system);
        
        // Forzamos a que la pantalla principal cargue el nombre y rol del usuario en sus etiquetas de bienvenida
       system.titleInterface();
    }

}
