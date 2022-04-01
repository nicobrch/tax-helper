import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class Tabla implements TableModelListener {

    private JTable table;

    public Tabla(){
        // Inicializamos la informacion de las celdas
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
                {"Total", "", "", "", ""}
        };

        //Hacemos que la primera columna (mes) no se pueda editar
        this.table = new JTable(datos, nombreColumnas){
            @Override
            public boolean isCellEditable(int row, int column){
                return column != 0;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (!isNumeric(aValue)) return;
                if (isNegative(aValue)) return;
                super.setValueAt(aValue, row, column);
            }
        };

        table.getModel().addTableModelListener(this);
    }

    public ArrayList<Double> getColumnValues(int column){
        //Las columnas son 1, 2, 3, 4
        ArrayList<Double> datos = new ArrayList<>();
        for (int i = 0; i < 12; i++){
            if (!isEmpty(this.table.getValueAt(i, column))){
                datos.add(Double.parseDouble(String.valueOf(this.table.getValueAt(i, column))));
            } else {
                datos.add(-1.0); //La celda esta vacía
            }
        }
        return datos;
    }

    private boolean isEmpty(Object obj){
        return obj == "";
    }

    private boolean isNumeric(Object obj){
        if (obj == null) return false;
        try {
            Double.parseDouble(String.valueOf(obj));
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    private boolean isNegative(Object obj){
        if (obj == null) return false;
        try {
            Double.parseDouble(String.valueOf(obj));
            return (Double.parseDouble(String.valueOf(obj)) < 0);
        } catch (NumberFormatException e){
            return false;
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {

    }

    public JTable getTable() {
        return table;
    }
}
