import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Tabla implements TableModelListener {

    private JTable table;
    private Random rand;

    public Tabla(){
        rand = new Random();
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
                {"Diciembre", "", "", "", ""}
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

        //Evitamos edicion de la tabla
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);

        table.getModel().addTableModelListener(this);
    }


    public Long totalSueldoImponible(Long[][] matrix){
        Long suma = 0L;
        for (int i = 0; i < 12; i++){
            suma += matrix[i][0];
        }
        return suma;
    }


    public Long totalHonorarios(Long[][] matrix){
        Long suma = 0L;
        for (int i = 0; i < 12; i++){
            suma += matrix[i][2];
        }
        return suma;
    }


    public Long totalImpuestos(Long[][] matrix){
        long suma = 0L;
        for (int i = 0; i < 12; i++){
            suma += matrix[i][1] + matrix[i][4];
        }
        return suma;
    }

    public void setImpuetoHonorario(){
        for (int i = 0 ; i < 12 ; i++){
            double honorario = Double.parseDouble(String.valueOf(table.getValueAt(i,3)));
            table.getModel().setValueAt(honorario*0.1225,i,4);
        }
    }


    public Double gastosPresuntos(Long totalHonorarios){
        return (double)totalHonorarios * 0.3;
    }


    public Double rentaAnual(Long sueldoImponible, Long totalHonorarios, Double gastosPresuntos){
        return (double)sueldoImponible + (double)totalHonorarios - gastosPresuntos;
    }


    public Long[][] getTableValues(){
        Long[][] matrix = new Long[12][4];

        for (int i = 0; i < 12; i++){
            for (int j = 0; j < 4; j++){
                if (!isEmpty(this.table.getValueAt(i, j))){
                    matrix[i][j] = (Long.parseLong(String.valueOf(this.table.getValueAt(i,j+1))));
                }
            }
        }

        return matrix;
    }


    public void imprimirMatriz(Long[][] matrix){
        for (int i = 0; i < 12; i++){
            System.out.println(matrix[i][0] + " " + matrix[i][1] + " " + matrix[i][2] + " " + matrix[i][3]);
        }
    }


    public void setRandomValuesOnTable(){
        for (int i = 0; i < 12; i++){
            for (int j = 1; j < 5; j++){
                Long v = rand.nextLong(9999999);
                this.table.setValueAt(v, i, j);
            }
        }
    }


    private boolean isEmpty(Object obj){
        return obj == "";
    }


    private boolean isNumeric(Object obj){
        if (obj == null) return false;
        try {
            Long.parseLong(String.valueOf(obj));
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }


    private boolean isNegative(Object obj){
        if (isNumeric(obj)){
            return (Double.parseDouble(String.valueOf(obj)) < 0);
        }
        return false;
    }


    @Override
    public void tableChanged(TableModelEvent e) {

    }


    public JTable getTable() {
        return table;
    }
}
