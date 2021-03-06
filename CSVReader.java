import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {
    public String path = "./tablaxlsx.csv";
    public String line = "";
    public Double[][] TablaRango = new Double[8][4];

    public CSVReader() {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(this.path));
            int count = 0;
            int index = 0;
            while ((this.line = bf.readLine()) != null) {
                if (count != 0) {
                    String[] values = this.line.split(";");
                    //System.out.println(values[0] + " " + values[1] + " " + values[2] + " " + values[3]);
                    for (int j = 0; j < 4; j++) {
                        TablaRango[index][j] = Double.parseDouble(String.valueOf(values[j]));
                    }
                    index++;
                }
                count++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("e");
            e.printStackTrace();
        }
    }

    public Double[][] getMatrix() {
        return this.TablaRango;
    }
}