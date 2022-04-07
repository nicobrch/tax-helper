import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import java.util.Random;

public class Tabla implements TableModelListener {

    private JTable table;
    private Random rand;
    private CSVReader reader = new CSVReader();
    private Long[][] matrix = new Long[12][4]; //Matriz de valores (sin contar la columna de meses).

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
        table.setRowHeight(30);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        for (int i = 1; i < 5; i++){
            columnModel.getColumn(i).setPreferredWidth(200);
        }


        //Evitar edicion
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getModel().addTableModelListener(this);
    }

    public Long totalSueldoImponible(){
        Long suma = 0L;
        for (int i = 0; i < 12; i++){
            if (this.matrix[i][0] == -1L){
                suma += 0;
                continue;
            }
            suma += this.matrix[i][0];
        }
        return suma;
    }

    public Long totalHonorarios(){
        Long suma = 0L;
        for (int i = 0; i < 12; i++){
            if (this.matrix[i][2] == -1L){
                suma += 0;
                continue;
            }
            suma += this.matrix[i][2];
        }
        return suma;
    }

    public Long totalImpuestos() {
        long suma = 0L;
        for (int i = 0; i < 12; i++){
            if (this.matrix[i][1] == -1L || this.matrix[i][3] == -1L){
                suma += 0;
                continue;
            }
            suma += this.matrix[i][1] + this.matrix[i][3];
        }
        return suma;
    }

    public Long totalImpuestoSueldo(){
        Long suma = 0L;
        for (int i = 0 ; i < 12 ; i++){
            if (this.matrix[i][1] == -1L){
                suma += 0;
                continue;
            }
            suma += matrix[i][1];
        }
        return suma;
    }

    public void setImpuestoHonorario(){
        long honorario;
        for (int i = 0 ; i < 12 ; i++){
            if (isNumeric(this.table.getValueAt(i, 3))){
                honorario = Long.parseLong(String.valueOf(table.getValueAt(i,3)));
            } else {
                honorario = 0L;
            }
            double num = honorario * 0.1225;
            Long x = Math.round(num);
            table.getModel().setValueAt(x,i,4);
        }
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

    public void parseMatrixValues(){
        for (int i = 0; i < 12; i++){
            for (int j = 0; j < 4; j++){
                if (!isEmpty(this.table.getValueAt(i, j+1))){
                    this.matrix[i][j] = (Long.parseLong(String.valueOf(this.table.getValueAt(i,j+1))));
                } else {
                    this.matrix[i][j] = -1L;
                }
            }
        }
    }

    public int checkLastFullRow(){
        for (int i = 11; i >= 0; i--){
            if (this.matrix[i][0] != -1L && this.matrix[i][1] != -1L && this.matrix[i][2] != -1L){
                return i;
            }
        }
        return 0;
    }

    public boolean isEmptySandwich() {
        for (int i = 0; i < 11; i++) {
            if (this.matrix[i][0] == -1L && this.matrix[i+1][0] != -1L) {
                return true;
            } else if(this.matrix[i][1] == -1L && this.matrix[i+1][1] != -1L) {
                return true;
            } else if(this.matrix[i][2] == -1L && this.matrix[i+1][2] != -1L) {
                return true;
            } else if((this.matrix[i][0] == -1L && this.matrix[i][1] != -1L) || (this.matrix[i][0] == -1L && this.matrix[i][2] != -1L)) {
                return true;
            } else if((this.matrix[i][1] == -1L && this.matrix[i][0] != -1L) || (this.matrix[i][1] == -1L && this.matrix[i][2] != -1L)) {
                return true;
            } else if((this.matrix[i][2] == -1L && this.matrix[i][0] != -1L) || (this.matrix[i][2] == -1L && this.matrix[i][1] != -1L)) {
                return true;
            }
        }
        return false;
    }

    public boolean isMatrixFull(){
        for (int i = 0; i < 12; i++){
            for (int j = 0; j < 4; j++){
                if (this.matrix[i][j] == -1L){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isFirstRowEmpty(){
        return !isNumeric(this.table.getValueAt(0, 1)) && !isNumeric(this.table.getValueAt(0, 2)) && !isNumeric(this.table.getValueAt(0, 3));
    }

    public boolean checkMatrixProyeccion() {
        for (int i = 0; i < 12; i++) {
            if ( (this.matrix[i][0] == 0 && this.matrix[i][1] != 0) ) {
                return true;
            }
            if ( (this.matrix[i][0] != 0 && this.matrix[i][1] == 0) ) {
                return true;
            }
        }
        return false;
    }

    public Integer cantidadSueldo() {
        int countSueldo = 0;

        for (int i = 0 ; i < 12 ; i++){
            if (this.matrix[i][0] == -1L) {
                countSueldo++;
            }
        }
        return 12 - countSueldo;
    }

    public Integer cantidadHonorario() {
        int countHonorario = 0;

        for (int i = 0 ; i < 12 ; i++){
            if (this.matrix[i][2] == -1L) {
                countHonorario++;
            }
        }
        return  12 - countHonorario;
    }

    public void Proyeccion() {
        System.out.println(cantidadSueldo());
        Long promedioSueldo = totalSueldoImponible() / cantidadSueldo();
        Long promedioImpuestosSueldo = totalImpuestoSueldo() / cantidadSueldo();
        Long promedioHonorario;

        if(cantidadHonorario() == 0) {
            promedioHonorario = 0L;
        } else {
            promedioHonorario = totalHonorarios() / cantidadHonorario();
        }

        int inicio = checkLastFullRow();

        for (int i = inicio ; i < 12 ; i++){
            if (this.matrix[i][0] == -1L){
                this.table.setValueAt(promedioSueldo,i,1);
            }
            if (this.matrix[i][1] == -1L){
                this.table.setValueAt(promedioImpuestosSueldo,i,2);
            }
            if (this.matrix[i][2] == -1L){
                this.table.setValueAt(promedioHonorario,i,3);
            }
        }
    }

    public void imprimirMatriz(){
        for (int i = 0; i < 12; i++){
            System.out.println(this.matrix[i][0] + " " + this.matrix[i][1] + " " + this.matrix[i][2] + " " + this.matrix[i][3]);
        }
    }

    public Long[][] getMatrix(){
        return this.matrix;
    }

    public void setRandomValuesOnTable(String modo){
        if (modo.equals("Random")){
            for (int i = 0; i < 4; i++){
                for (int j = 1; j < 4; j++){
                    Long v = rand.nextLong(9999999);
                    this.table.setValueAt(v, i, j);
                }
            }
        } else if (modo.equals("Paga Impuestos")){
            for (int i = 0; i < 4; i++) {
                this.table.setValueAt(10000000,i,1);
                this.table.setValueAt(1000,i,2);
                this.table.setValueAt(10000000,i,3);
            }
        } else {
            for (int i = 0; i < 6; i++) {
                this.table.setValueAt(1000000,i,1);
                this.table.setValueAt(10000,i,2);
                this.table.setValueAt(1000000,i,3);
            }
        }
    }

    private boolean isEmpty(Object obj) {
        return obj == "";
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

    @Override
    public void tableChanged(TableModelEvent e) { }

    public JTable getTable() {
        return table;
    }
}