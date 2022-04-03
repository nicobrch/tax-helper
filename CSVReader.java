import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {
    public String path = "./tablaxlsx.csv";
    public String line = "";
    public String[][] TablaRango = new String[4][8];

    public CSVReader() {
    }
    public  String[][] getTabla(){
        return this.TablaRango;
    }

    public void Read() {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(this.path));
            int count = 0;
            while ((this.line = bf.readLine()) != null) {
                if (count  != 0){
                    String[] values = this.line.split(";");
                    System.out.println(values[0] + " " + values[1] + " " + values[2] + " " + values[3]);
                }
                count++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    }
}
