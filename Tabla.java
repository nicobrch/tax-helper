import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Tabla implements TableModelListener {

    private final JTable table;
    private final Random rand;
    private final CSVReader reader = new CSVReader();
    private final Long[][] matrix = new Long[12][4]; //Matriz de valores (sin contar la columna de meses ni imp honorario).

    public Tabla(){
        rand = new Random();
        // Inicializamos la informacion de las celdas
        String[] nombreColumnas = {"Mes", "Sueldo Imponible", "Impuestos Retenidos", "Honorarios Brutos", "Impuestos Retenidos"};
        Object[][] datos = {
                {"Enero", "0", "0", "0", "0"},
                {"Febrero", "0", "0", "0", "0"},
                {"Marzo", "0", "0", "0", "0"},
                {"Abril", "0", "0", "0", "0"},
                {"Mayo", "0", "0", "0", "0"},
                {"Junio", "0", "0", "0", "0"},
                {"Julio", "0", "0", "0", "0"},
                {"Agosto", "0", "0", "0", "0"},
                {"Septiembre", "0", "0", "0", "0"},
                {"Octubre", "0", "0", "0", "0"},
                {"Noviembre", "0", "0", "0", "0"},
                {"Diciembre", "0", "0", "0", "0"}
        };

        //Hacemos que la primera columna (mes) no se pueda editar
        this.table = new JTable(datos, nombreColumnas){
            @Override
            public boolean isCellEditable(int row, int column){
                return column != 0 && column !=4;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (!isNumeric(aValue)) return;
                if (isNegative(aValue)) return;
                super.setValueAt(aValue, row, column);
            }
        };


        //Dimensiones
        table.setRowHeight(48);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(150);
        for (int i = 1; i < 5; i++){
            columnModel.getColumn(i).setPreferredWidth(275);
        }

        //Letra
        Font fontH = new Font("SansSerif", Font.BOLD, 20);
        Font fontT = new Font("SansSerif", Font.PLAIN, 18);
        table.getTableHeader().setFont(fontH);
        table.setFont(fontT);

        //Evitar edicion
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getModel().addTableModelListener(this);
    }

    private boolean isNumeric(Object obj) {
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

    public void limpiarTabla(){
        for (int i = 0; i < 12; i++){
            for (int j = 1; j < 5; j++){
                this.table.setValueAt("0", i, j);
            }
        }
    }

    public void parseMatrixValues(){
        for (int i = 0; i < 12; i++){
            for (int j = 0; j < 4; j++){
                this.matrix[i][j] = (Long.parseLong(String.valueOf(this.table.getValueAt(i,j+1))));
            }
        }
    }

    public void Proyeccion() {
        // Conteos necesarios
        int cantSueldo = cantidadSueldo();
        int cantHonorario = cantidadHonorario();
        Long totalSueldoImpo = totalSueldoImponible();
        Long totalImpuSueldo = totalImpuestoSueldo();
        Long totalHon = totalHonorarios();

        //Variables
        Long promedioSueldo = 0L;
        Long promedioImpuestosSueldo = 0L;
        Long promedioHonorario = 0L;

        //Evitamos divisiones por 0
        if (cantSueldo > 0){
            promedioSueldo = totalSueldoImpo / cantSueldo;
        }
        if (cantSueldo > 0){
            promedioImpuestosSueldo = totalImpuSueldo / cantSueldo;
        }
        if(cantHonorario > 0) {
            promedioHonorario = totalHon / cantHonorario;
        }

        //Obtenemos ultima fila con sueldo u honorario
        int inicio = checkLastFullRow();

        //Remplazamos los datos desde la ultima fila encontrada para abajo
        for (int i = inicio ; i < 12 ; i++){
            this.table.setValueAt(promedioSueldo,i,1);
            this.table.setValueAt(promedioImpuestosSueldo,i,2);
            this.table.setValueAt(promedioHonorario,i,3);
        }
    }

    public void setRandomValuesOnTable(String modo){
        switch (modo) {
            case "R" -> {
                for (int i = 0; i < 4; i++) {
                    for (int j = 1; j < 4; j++) {
                        Long v = rand.nextLong(9999999);
                        this.table.setValueAt(v, i, j);
                    }
                }
                for (int i = 4; i < 12; i++) {
                    this.table.setValueAt(0, i, 1);
                    this.table.setValueAt(0, i, 2);
                    this.table.setValueAt(0, i, 3);
                }
            }
            case "P" -> {
                for (int i = 0; i < 4; i++) {
                    this.table.setValueAt(10000000, i, 1);
                    this.table.setValueAt(1000, i, 2);
                    this.table.setValueAt(10000000, i, 3);
                }
                for (int i = 4; i < 12; i++) {
                    this.table.setValueAt(0, i, 1);
                    this.table.setValueAt(0, i, 2);
                    this.table.setValueAt(0, i, 3);
                }
            }
            case "D" -> {
                for (int i = 0; i < 6; i++) {
                    this.table.setValueAt(1000000, i, 1);
                    this.table.setValueAt(10000, i, 2);
                    this.table.setValueAt(1000000, i, 3);
                }
                for (int i = 6; i < 12; i++) {
                    this.table.setValueAt(0, i, 1);
                    this.table.setValueAt(0, i, 2);
                    this.table.setValueAt(0, i, 3);
                }
            }
        }
    }

    public int checkLastFullRow(){
        for (int i = 11; i >= 0; i--){
            if ((this.matrix[i][0] > 0 && this.matrix[i][1] > 0) || this.matrix[i][2] > 0){
                return i;
            }
        }
        return 0;
    }

    public boolean isImpuestoMayorQueSueldo() {
        for (int i = 0; i < 12; i++) {
            if (this.matrix[i][0] < this.matrix[i][1]){
                return true;
            }
        }
        return false;
    }

    public boolean isImpuestoCeroConSueldo(){
        for (int i = 0; i < 12; i++) {
            if (this.matrix[i][0] != 0 && this.matrix[i][1] == 0){
                return true;
            }
        }
        return false;
    }

    public void setImpuestoHonorario(){
        long honorario;
        for (int i = 0 ; i < 12 ; i++){
            honorario = Long.parseLong(String.valueOf(table.getValueAt(i,3)));
            double num = honorario * 0.1225;
            Long x = Math.round(num);
            table.getModel().setValueAt(x,i,4);
        }
    }

    public Long totalSueldoImponible(){
        Long suma = 0L;
        for (int i = 0; i < 12; i++){
            suma += this.matrix[i][0];
        }
        return suma;
    }

    public Long totalHonorarios(){
        Long suma = 0L;
        for (int i = 0; i < 12; i++){
            suma += this.matrix[i][2];
        }
        return suma;
    }

    public Long totalImpuestos() {
        long suma = 0L;
        for (int i = 0; i < 12; i++){
            suma += this.matrix[i][1] + this.matrix[i][3];
        }
        return suma;
    }

    public Long totalImpuestoSueldo(){
        Long suma = 0L;
        for (int i = 0 ; i < 12 ; i++){
            suma += matrix[i][1];
        }
        return suma;
    }

    public Long gastosPresuntos(Long totalHonorarios){
        return Math.round(totalHonorarios * 0.3) ;
    }

    public Long rentaAnual() {
        Long sueldoImponible = totalSueldoImponible();
        Long totalHonorarios = totalHonorarios();
        Long gastosPresuntos = gastosPresuntos(totalHonorarios);
        return (sueldoImponible + totalHonorarios - gastosPresuntos);
    }

    public Double ImpuestoGlobalComplementario() {
        Double[][] matrixCSV = this.reader.getMatrix();
        Long rentaAnual = rentaAnual();

        for (int i = 0 ; i < 8 ; i++) {
            if (i == 7) {
                return (rentaAnual * matrixCSV[i][2] - matrixCSV[i][3]);
            }
            if ( rentaAnual < matrixCSV[i][1] ) {
                return (rentaAnual * matrixCSV[i][2] - matrixCSV[i][3]);
            }
        }
        return 0.0;
    }

    public Double PagoDevolucion() {
        setImpuestoHonorario();
        Double impuestoTabla = ImpuestoGlobalComplementario();
        Long totalImpuestos = totalImpuestos();
        return impuestoTabla - totalImpuestos;
    }

    public Integer cantidadSueldo() {
        int countSueldo = 0;

        for (int i = 0 ; i < 12 ; i++){
            if (this.matrix[i][0] == 0 || this.matrix[i][0] == 0L) {
                countSueldo++;
            }
        }
        return 12 - countSueldo;
    }

    public Integer cantidadHonorario() {
        int countHonorario = 0;

        for (int i = 0 ; i < 12 ; i++){
            if (this.matrix[i][2] == 0 || this.matrix[i][2] == 0L) {
                countHonorario++;
            }
        }
        return  12 - countHonorario;
    }

    @Override
    public void tableChanged(TableModelEvent e) { }

    public JTable getTable() {
        return table;
    }
}