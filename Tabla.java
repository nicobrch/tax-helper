import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

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

    private boolean isNumeric(Object dato){
        if (dato == null) return false;
        try {
            Double.parseDouble(String.valueOf(dato));
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    private boolean isNegative(Object dato){
        if (dato == null) return false;
        try {
            return (Double.parseDouble(String.valueOf(dato)) < 0);
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
