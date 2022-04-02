import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Interfaz extends JFrame implements ActionListener {

    private Tabla tab;
    private JTable table;
    private JScrollPane scrollPaneTable;

    public Interfaz() {
        setLayout(new FlowLayout());
        tab = new Tabla();
        this.table = tab.getTable();

        // Añadimos la tabla al panel

        table.setPreferredScrollableViewportSize(new Dimension(500, 192));
        table.setFillsViewportHeight(true);
        scrollPaneTable = new JScrollPane(table);
        add(scrollPaneTable);

        // Botones

        JButton botonTest = new JButton("Test");
        botonTest.setSize(320, 300);
        JScrollPane scrollPaneBotonTest = new JScrollPane(botonTest);
        add(scrollPaneBotonTest);
        botonTest.addActionListener(e -> tab.setRandomValuesOnTable());

        JButton botonLimpiarTabla = new JButton("Limpiar Tabla");
        botonLimpiarTabla.setSize(320, 300);
        botonLimpiarTabla.addActionListener(this);
        botonLimpiarTabla.setActionCommand("Limpiar");
        JScrollPane scrollPaneBotonLimpiarTabla = new JScrollPane(botonLimpiarTabla);
        add(scrollPaneBotonLimpiarTabla);

        JButton botonEjecutar = new JButton("Ejecutar Programa");
        botonEjecutar.setSize(320, 300);
        JScrollPane scrollPaneBotonEjecutar = new JScrollPane(botonEjecutar);
        botonEjecutar.addActionListener(this);
        add(scrollPaneBotonEjecutar);
        botonEjecutar.addActionListener(e -> {
            Long[][] matrix = tab.getTableValues();
            tab.imprimirMatriz(matrix);
            //System.out.println("Total SI: " + tab.totalSueldoImponible(matrix));
            tab.setImpuetoHonorario();
        });
    }

    public static void main (String[] args){
        Interfaz gui = new Interfaz();
        gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gui.setSize(600, 350);
        gui.setVisible(true);
        gui.setTitle("Tax Helper");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("Limpiar")){
            for (int i = 1; i < 5; i++){
                for (int j = 0; j < 12 ; j++){
                    table.getModel().setValueAt("",j,i);
                }
            }
        }
    }
}
