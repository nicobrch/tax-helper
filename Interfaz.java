import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interfaz extends JFrame implements ActionListener {

    private Tabla tab;
    private JTable table;
    private JScrollPane scrollPaneTable;
    private JButton botonOk;

    public Interfaz() {
        setTitle("Tax-Helper");
        setSize(650, 300);

        panelInicial();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Interfaz gui = new Interfaz();
        gui.setVisible(true);
    }

    // Panel con Instrucciones
    public void panelInicial() {
        JPanel panel1 = new JPanel();
        this.getContentPane().add(panel1);
        setSize(960,480); // Mensaje Completo y Boton Abajo //

        JTextArea descripcion = new JTextArea();
        Font font = new Font("Arial", Font.PLAIN, 20);
        descripcion.setSize(900,400);
        descripcion.setEditable(false);
        descripcion.setLineWrap(true);
        descripcion.setFont(font);
        descripcion.setText(
        """
        Bienvenido al programa Tax-Help.
        Este programa tiene como funcionalidad indicarle en base a unos ciertos datos que debera ingresar a continuacion, si le corresponde pago o devolucion de impuestos.
        Favor, seguir las siguientes indicaciones:
        1) En caso de que no se encuentre trabajando, rellenar con '0'.
        2) Si no conoce toda la informacion, favor completar solo con la informacion oficial que tenga en su conocimiento.
        3) Luego de tener todo, seleccione el boton 'Ejecutar Programa'.
        """
        );
        descripcion.setVisible(true);
        panel1.add(descripcion);

        botonOk = new JButton("Ok!");
        panel1.add(botonOk);

        ActionListener nextPanel = e -> {
            remove(panel1);
            panelTabla();
            revalidate();
        };
        botonOk.addActionListener(nextPanel);
    }

    // Panel con Tabla y Botones
    public void panelTabla() {
        JPanel panel2 = new JPanel();
        this.getContentPane().add(panel2);
        setSize(960, 480); // Botones Abajo //

        tab = new Tabla();
        this.table = tab.getTable();

        // Añadimos la tabla al panel
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        scrollPaneTable = new JScrollPane(table);
        scrollPaneTable.setVisible(true);
        panel2.add(scrollPaneTable);

        // Botones

        JButton botonAtras = new JButton("Atras");
        botonAtras.setSize(320, 300);
        botonAtras.addActionListener(e -> {
            remove(panel2);
            panelInicial();
            revalidate();
        });
        panel2.add(botonAtras);

        panel2.add(Box.createRigidArea(new Dimension(245, 0)));

        JButton botonTest = new JButton("Test");
        botonTest.setSize(320, 300);
        botonTest.addActionListener(e -> tab.setRandomValuesOnTable("Paga Impuestos")); //modo = "Random" o "Paga Impuestos", sino se devuelve.
        panel2.add(botonTest);

        JButton botonLimpiarTabla = new JButton("Limpiar");
        botonLimpiarTabla.setSize(320, 300);
        botonLimpiarTabla.addActionListener(this);
        botonLimpiarTabla.setActionCommand("Limpiar");
        panel2.add(botonLimpiarTabla);

        panel2.add(Box.createRigidArea(new Dimension(245, 0)));

        JButton botonProyeccion = new JButton("Proyeccion");
        botonProyeccion.setSize(320, 300);
        botonProyeccion.addActionListener(e -> {
            tab.getMatrixValues();

            if (tab.checkMatrixProyeccion() ) {
                MensajeEmergente();
            } else {
                tab.Proyeccion();
                tab.setImpuestoHonorario();

            }
        });
        panel2.add(botonProyeccion);

        JButton botonEjecutar = new JButton("Ejecutar");
        botonEjecutar.setSize(320, 300);
        botonEjecutar.addActionListener(this);
        panel2.add(botonEjecutar);
        botonEjecutar.addActionListener(e -> {
            tab.setImpuestoHonorario();
            tab.getMatrixValues();

            Long[][] matrix = tab.getMatrix();

            if (tab.checkMatrixSueldoImpuesto() ) {
                MensajeEmergente();
            } else {
                tab.PagoDevolucion();
                remove(panel2);
                panelFinal();
                revalidate();
            }

        });

    }

    // Panel con Mensaje sobre Devolucion o Pago de Impuestos.
    public void panelFinal() {
        JPanel panel3 = new JPanel();
        this.getContentPane().add(panel3);
        setSize(960, 480); // Solo Texto Completo //

        JTextArea detalles = new JTextArea();
        Font font = new Font("Arial", Font.PLAIN, 20);
        detalles.setFont(font);
        detalles.setSize(900,400);
        detalles.setEditable(false);
        detalles.setLineWrap(true);

        Double num = tab.PagoDevolucion();
        if(num > 0) { // Paga de Impuestos
            detalles.setText(
                """
                TAX-HELPER.
                
                Te corresponde PAGO de impuestos.
                
                Segun la tabla de calculo del Impuesto Global del año 2021, y los datos de la tabla rellenados
                hasta el mes de diciembre, se han realizado los calculos correspondientes, y por lo tanto, le corresponde una paga aproximada """
                + " de " + Math.round( num ) + " pesos."
            );
        } else if (num < 0) { // Devolucion
            detalles.setText(
                """
                TAX-HELPER.
                                        
                Te corresponde DEVOLUCION de impuestos.
                                        
                Segun la tabla de calculo del Impuesto Global del año 2021, y los datos de la tabla rellenados
                hasta el mes de diciembre, se han realizado los calculos correspondientes, y por lo tanto, le corresponde una paga aproximada """
                + " de " + Math.round( (-1)*num ) + " pesos."
            );
        }
        detalles.setVisible(true);
        panel3.add(detalles);

        botonOk = new JButton("Calcular nuevamente");
        panel3.add(botonOk);

        ActionListener nextPanel = e -> {
            remove(panel3);
            panelTabla();
            revalidate();
        };
        botonOk.addActionListener(nextPanel);
    }

    public void MensajeEmergente() {
        JFrame jFrame = new JFrame();
        JOptionPane.showMessageDialog(jFrame, "Favor, ingresa los datos correctamente.");
    }

    // Funcion Boton Limpiar
    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("Limpiar")) {
            for (int i = 1; i < 5; i++) {
                for (int j = 0; j < 12; j++) {
                    table.getModel().setValueAt("", j, i);
                }
            }
        }
    }

}