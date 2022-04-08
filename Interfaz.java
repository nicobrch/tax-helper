import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

public class Interfaz extends JFrame implements ActionListener {

    private Tabla tab;
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
        setSize(1280,720); // Mensaje Completo y Boton Abajo //

        JTextArea descripcion = new JTextArea();
        Font font = new Font("SansSerif", Font.PLAIN, 24);
        descripcion.setSize(1200,600);
        descripcion.setEditable(false);
        descripcion.setLineWrap(true);
        descripcion.setFont(font);
        descripcion.setText(
        """
        Bienvenido al programa Tax-Help.
        Este programa tiene como funcionalidad indicarle en base a unos ciertos datos que debera ingresar a continuacion,
        si le corresponde pago o devolucion de impuestos, de acuerdo a la tabla de impuesto global unico del servicio
        de impuestos internos de Chile en el anio 2021.
        
        Favor, seguir las siguientes indicaciones:
        1) En caso de que no se encuentre trabajando, deje los datos en 0.
        2) Puesto que hay impuestos voluntarios, debera ingresar los impuestos correspondiente al sueldo de forma manual.
        3) Si desea realizar una proyeccion, rellene las filas hasta el ultimo mes que desee, ya sea sueldo e impuesto
        correspondiente al sueldo, honorario o todo lo anterior y presione 'Proyeccion'.
        3) Una vez listo, presione 'Ejecutar Programa'.
        """
        );
        descripcion.setVisible(true);
        panel1.add(descripcion);

        botonOk = new JButton("Ok!");
        botonOk.setPreferredSize(new Dimension(100, 50));
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
        setSize(1280, 720); // Botones Abajo //

        tab = new Tabla();
        JTable table = tab.getTable();

        // Añadimos la tabla al panel
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        JScrollPane scrollPaneTable = new JScrollPane(table);
        scrollPaneTable.setVisible(true);
        panel2.add(scrollPaneTable);

        // Botones

        JButton botonAtras = new JButton("Atras");
        botonAtras.setPreferredSize(new Dimension(100, 50));
        botonAtras.addActionListener(e -> {
            remove(panel2);
            panelInicial();
            revalidate();
        });
        panel2.add(botonAtras);

        panel2.add(Box.createRigidArea(new Dimension(250, 0)));

        /*
            BOTONES TEST Y LIMPIAR
         */

        JButton botonTestR = new JButton("Test R");
        botonTestR.setPreferredSize(new Dimension(75, 50));
        botonTestR.addActionListener(e -> {
            /*
            Modos para los valores
            R = Random | P = Paga Impuestos | D = Devolucion
            */
            tab.setRandomValuesOnTable("R");
            tab.setImpuestoHonorario();
        });
        panel2.add(botonTestR);

        JButton botonTestP = new JButton("Test P");
        botonTestP.setPreferredSize(new Dimension(75, 50));
        botonTestP.addActionListener(e -> {
            /*
            Modos para los valores
            R = Random | P = Paga Impuestos | D = Devolucion
            */
            tab.setRandomValuesOnTable("P");
            tab.setImpuestoHonorario();
        });
        panel2.add(botonTestP);

        JButton botonTestD = new JButton("Test D");
        botonTestD.setPreferredSize(new Dimension(75, 50));
        botonTestD.addActionListener(e -> {
            /*
            Modos para los valores
            R = Random | P = Paga Impuestos | D = Devolucion
            */
            tab.setRandomValuesOnTable("D");
            tab.setImpuestoHonorario();
        });
        panel2.add(botonTestD);

        /*
            LIMPIAR PROYECCION EJECUTAR
         */

        panel2.add(Box.createRigidArea(new Dimension(250, 0)));

        JButton botonLimpiarTabla = new JButton("Limpiar");
        botonLimpiarTabla.setPreferredSize(new Dimension(100, 50));
        botonLimpiarTabla.addActionListener(e -> tab.limpiarTabla());
        panel2.add(botonLimpiarTabla);

        panel2.add(Box.createRigidArea(new Dimension(50, 0)));

        JButton botonProyeccion = new JButton("Proyeccion");
        botonProyeccion.setPreferredSize(new Dimension(100, 50));
        botonProyeccion.addActionListener(e -> {
            tab.parseMatrixValues();

            if (!tab.isImpuestoMayorQueSueldo()){
                MensajeImpuestoMayorQueSueldo();
            } else if (!tab.isImpuestoCeroConSueldo()){
                MensajeImpuestoCeroConSueldo();
            } else {
                tab.Proyeccion();
                tab.setImpuestoHonorario();
            }
        });
        panel2.add(botonProyeccion);


        JButton botonEjecutar = new JButton("Ejecutar");
        botonEjecutar.setPreferredSize(new Dimension(100, 50));
        botonEjecutar.addActionListener(this);
        panel2.add(botonEjecutar);
        botonEjecutar.addActionListener(e -> {
            tab.parseMatrixValues();

            if (!tab.isImpuestoMayorQueSueldo()){
                MensajeImpuestoMayorQueSueldo();
            } else if (!tab.isImpuestoCeroConSueldo()){
                MensajeImpuestoCeroConSueldo();
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
        setSize(1280, 720); // Solo Texto Completo //

        JTextArea detalles = new JTextArea();
        Font font = new Font("SansSerif", Font.PLAIN, 24);
        detalles.setFont(font);
        detalles.setSize(1200,600);
        detalles.setEditable(false);
        detalles.setLineWrap(true);
        Double num = tab.PagoDevolucion();
        Long numRedondeado = Math.round(num);
        if(num > 0) {               // Paga de Impuestos
            detalles.setText(
                """
                TAX-HELPER.
                
                Te corresponde PAGO de impuestos.
                
                Segun la tabla de calculo del Impuesto Global del anio 2021, y los datos de la tabla rellenados
                hasta el mes de diciembre, se han realizado los calculos correspondientes, y por lo tanto,
                le corresponde una paga aproximada"""
                + " de $" + NumberFormat.getNumberInstance(Locale.US).format(numRedondeado) + " pesos."
            );
        } else if (num < 0) {       // Devolucion
            numRedondeado = numRedondeado * -1;
            detalles.setText(
                """
                TAX-HELPER.
                                        
                Te corresponde DEVOLUCION de impuestos.
                                        
                Segun la tabla de calculo del Impuesto Global del anio 2021, y los datos de la tabla rellenados
                hasta el mes de diciembre, se han realizado los calculos correspondientes, y por lo tanto,
                le corresponde una paga aproximada"""
                + " de $" + NumberFormat.getNumberInstance(Locale.US).format(numRedondeado) + " pesos."
            );
        } else {
            detalles.setText(
                    """
                    Vaya a trabajar
                    """
            );
        }
        detalles.setVisible(true);
        panel3.add(detalles);

        botonOk = new JButton("Calcular nuevamente");
        botonOk.setPreferredSize(new Dimension(200, 50));
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

    public void MensajeImpuestoMayorQueSueldo() {
        JFrame jFrame = new JFrame();
        JOptionPane.showMessageDialog(jFrame, "No puede tener un impuesto mayor que su sueldo! Revise sus datos.");
    }

    public void MensajeImpuestoCeroConSueldo() {
        JFrame jFrame = new JFrame();
        JOptionPane.showMessageDialog(jFrame, "Si tiene sueldo, por favor ingrese el impuesto correspondiente!");
    }


    // Funcion Boton Limpiar
    @Override
    public void actionPerformed(ActionEvent e) {
    }

}