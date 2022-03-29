import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Table extends JFrame implements ActionListener {
    
    private JTable tabla;

    public Table() {
        setLayout(new FlowLayout());

        String[] nombreColumnas = {"Mes", "Sueldo Imponible", "Impuestos Retenidos", "Honorarios Brutos", "Impuestos Retenidos"};
        Object[][] datos = {
                {"Enero", "", "", "", ""},
                {"Febrero", "", "", "", ""},
                {"Marzo", "", "", "", ""},
                {"Abril", "", "", "", ""},
                {"Mayo", "", "", "", ""},
                {"Junio", "", "", "", ""},
                {"Julio", "", "", "", ""},
                {"Agosto", "", "", "", ""},
                {"Septiembre", "", "", "", ""},
                {"Octubre", "", "", "", ""},
                {"Noviembre", "", "", "", ""},
                {"Diciembre", "", "", "", ""},
        };

        tabla = new JTable(datos, nombreColumnas){
            @Override
            public boolean isCellEditable(int row, int column){
                return column != 0;
            }
        };

        tabla.setPreferredScrollableViewportSize(new Dimension(500, 192));
        tabla.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane);
    }

    public static void main (String[] args){
        Table gui = new Table();
        gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gui.setSize(520, 300);
        gui.setVisible(true);
        gui.setTitle("Tax Helper");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
