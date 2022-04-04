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
        setSize(890,180); // Mensaje Completo y Boton Abajo //

        JTextArea descripcion = new JTextArea();
        descripcion.setFont(new Font("Times New Roman", Font.BOLD, 12));
        descripcion.setEditable(false);
        descripcion.setLineWrap(false);
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
        botonOk.setFont(new Font("Times New Roman", Font.BOLD, 12));
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
        setSize(550, 300); // Botones Abajo //

        tab = new Tabla();
        this.table = tab.getTable();

        // Añadimos la tabla al panel
        table.setPreferredScrollableViewportSize(new Dimension(500, 192));
        table.setFillsViewportHeight(true);
        scrollPaneTable = new JScrollPane(table);
        scrollPaneTable.setVisible(true);
        panel2.add(scrollPaneTable);

        // Botones

        JButton botonTest = new JButton("Test");
        botonTest.setSize(320, 300);
        botonTest.addActionListener(e -> tab.setRandomValuesOnTable());
        panel2.add(botonTest);

        JButton botonLimpiarTabla = new JButton("Limpiar Tabla");
        botonLimpiarTabla.setSize(320, 300);
        botonLimpiarTabla.addActionListener(this);
        botonLimpiarTabla.setActionCommand("Limpiar");
        botonLimpiarTabla.setFont(new Font("Times New Roman", Font.BOLD,12));
        panel2.add(botonLimpiarTabla);

        JButton botonEjecutar = new JButton("Ejecutar Programa");
        botonEjecutar.setSize(320, 300);
        botonEjecutar.addActionListener(this);
        panel2.add(botonEjecutar);
        botonEjecutar.addActionListener(e -> {
            tab.setImpuestoHonorario();
            if (tab.checkMatrixSueldoImpuesto()){

            }else {
                tab.getMatrixValues();
            }

            Long[][] matrix = tab.getMatrix();
            //tab.imprimirMatriz(matrix);
            //System.out.println( "Total sueldo imponible: " + tab.totalSueldoImponible(matrix)) ;
            //System.out.println( "Total honorarios: " + tab.totalHonorarios(matrix)) ;
            System.out.println("GAstos presuntos: " + tab.gastosPresuntos(tab.totalHonorarios()));

            //tab.totalImpuestos(matrix);
            //System.out.println("Total SI: " + tab.totalSueldoImponible(matrix));

        });

        // Boton de Prueba para saber si esta funcionando la ultima ventana(la que indica si paga o le devuelven impuestos) //
        botonOk = new JButton("Respuesta");
        botonOk.setFont(new Font("Times New Roman", Font.BOLD, 12));
        panel2.add(botonOk);

        ActionListener finalpanel = e -> {
            remove(panel2);
            panelFinal();
            revalidate();
        };
        botonOk.addActionListener(finalpanel);
    }

    // Panel con Mensaje sobre Devolucion o Pago de Impuestos.
    public void panelFinal() {
        JPanel panel3 = new JPanel();
        this.getContentPane().add(panel3);
        setSize(670, 140); // Solo Texto Completo //

        JTextArea detalles = new JTextArea();
        detalles.setFont(new Font("Times New Roman", Font.BOLD, 12));
        detalles.setEditable(false);
        detalles.setLineWrap(false);
        // double valorImpuestoFinal = tab.getImpuesto();
        Double num = tab.PagoDevolucion();
        if(num > 0) { // Paga de Impuestos
            detalles.setText(
                """
                TAX-HELPER.
                
                Te corresponde PAGO de impuestos.
                
                Segun la tabla de calculo del Impuesto Global del año 2021, y los datos de la tabla rellenados
                hasta el mes {mes}, se ha proyectado {proyeccion}, por lo cual le corresponde una paga aproximada """
                + " de " + num + " pesos."
            );
        } else if (num < 0) { // Devolucion
            detalles.setText(
                """
                TAX-HELPER.
                                        
                Te corresponde DEVOLUCION de impuestos.
                                        
                Segun la tabla de calculo del Impuesto Global del año 2021, y los datos de la tabla rellenados
                hasta el mes {mes}, se ha proyectado {proyeccion}, por lo cual le corresponde una paga aproximada """
                + " de " + num + " pesos."
            );
        }
        detalles.setVisible(true);
        panel3.add(detalles);
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