
package main;

import com.formdev.flatlaf.FlatDarkLaf; // Importamos el tema oscuro moderno
import javax.swing.UIManager;
import views.LoginView;

public class Main {
    public static void main(String[] args){
                try {
            // 1. Activar el Look and Feel moderno de FlatLaf
            FlatDarkLaf.setup();
            
            // 2. Pro-Tip: Redondear los bordes de botones y cajas de texto para un look premium
            UIManager.put("Component.arc", 12);
            UIManager.put("TextComponent.arc", 12);
            
            System.out.println("¡FlatLaf iniciado con éxito!");
        } catch (Exception ex) {
            System.err.println("No se pudo cargar el tema visual: " + ex.getMessage());
        }
                        // 3. Lanzar tu interfaz gráfica de forma segura
        java.awt.EventQueue.invokeLater(() -> {
            // Instancia la ventana que quieres que aparezca primero (Login, Menú, etc.)
            LoginView login = new LoginView();
            login.setLocationRelativeTo(null); // Esto centra la ventana en la pantalla automáticamente
            login.setVisible(true); // Hace visible la app
        });
    }
    
}
