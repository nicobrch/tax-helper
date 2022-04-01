import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interfaz extends JFrame implements ActionListener {

    private JTable table;
    private JScrollPane scrollPaneTable;

    public Interfaz() {
        setLayout(new FlowLayout());
        Tabla tab = new Tabla();
        this.table = tab.getTable();

        // Añadimos la tabla al panel
        table.setPreferredScrollableViewportSize(new Dimension(500, 192));
        table.setFillsViewportHeight(true);
        scrollPaneTable = new JScrollPane(table);
        add(scrollPaneTable);
        // Botones

        JButton botonLimpiarTabla = new JButton("Limpiar Tabla");
        botonLimpiarTabla.setSize(320, 300);
        JScrollPane scrollPaneBotonLimpiarTabla = new JScrollPane(botonLimpiarTabla);
        add(scrollPaneBotonLimpiarTabla);

        JButton botonEjecutar = new JButton("Ejecutar Programa");
        botonEjecutar.setSize(320, 300);
        JScrollPane scrollPaneBotonEjecutar = new JScrollPane(botonEjecutar);
        add(scrollPaneBotonEjecutar);
    }

    public static void main (String[] args){
        Interfaz gui = new Interfaz();
        gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gui.setSize(520, 300);
        gui.setVisible(true);
        gui.setTitle("Tax Helper");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
