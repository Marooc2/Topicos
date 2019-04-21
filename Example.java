import java.io.BufferedReader;
import java.util.ArrayList; // import the ArrayList class
        import java.util.Arrays;
        import java.io.FileNotFoundException;
        import java.io.FileReader;
        import java.util.Scanner;

public class Example {
    public static void main(String[] args) throws FileNotFoundException {

        int[][] dataset = {{0,1},{1,0},{2,1},{0,1},{0,1},{1,1}};
        //System.out.println(System.getProperty("user.dir"));
        System.out.println(hallarRatio(read()));
    }
    public static int[][] read() throws FileNotFoundException{

        ArrayList<int[]> listDataset = new ArrayList<int[]>();


        FileReader fr = new FileReader("archivo.txt");
        Scanner s = new Scanner(fr);


        while(s.hasNextLine()){
            String linea = s.nextLine();
            String[] campos = linea.split(" ");
            int [] camposInt = new int[campos.length];
            for(int i = 0; i < campos.length; i++)
                camposInt[i] = Integer.parseInt(campos[i]);
            listDataset.add(camposInt);
        }

        int[][] dataset = new int [listDataset.size()][listDataset.get(0).length];

        for(int i = 0; i < dataset.length; i++){
            dataset[i] = listDataset.get(i);
        }

        return dataset;
    }

    public static int hallarCardinalidad(int [][] dataset, int col){
        ArrayList<Integer> uniques = new ArrayList<Integer>();
        boolean isUnique = true;
        int pos = 0; //posicion de recorrido por filas en el dataset

        while(pos < dataset.length){
            isUnique = true;
            int e = dataset[pos][col];
            for(int i = 0; i < uniques.size(); i++){
                if(uniques.get(i) == e){
                    isUnique = false;
                    continue;
                }
                if(isUnique == false){continue;}
            }
            if(isUnique){
                uniques.add(e);
            }
            pos+=1;
        }
        return uniques.size();

    }

    public static int hallarInstanciasUnicas(int[][] dataset){
        ArrayList<int[]> uniques = new ArrayList<int[]>();
        boolean isUnique = true;
        boolean allEqual = false;
        int countFrom = 0;

        while(countFrom < dataset.length){
            isUnique = true;
            int[] e = dataset[countFrom];
            for(int i = 0; i < uniques.size(); i++){
                if(Arrays.equals(uniques.get(i), e)){
                    isUnique = false;
                    continue;
                }
                if(isUnique == false){continue;}
            }
            if(isUnique){
                uniques.add(e);
            }
            countFrom+=1;
        }
        return uniques.size();
    }

    public static double hallarRatio(int [][] dataset){

        ArrayList<Integer> listCardinalidades = new ArrayList<Integer>();

        for(int i = 0; i < dataset[0].length; i++){
            listCardinalidades.add(hallarCardinalidad(dataset, i));
        }

        double instanciasUnicasPosibles = 1;

        for(int i = 0; i < dataset[0].length; i++){
            System.out.println(listCardinalidades.get(i));
            instanciasUnicasPosibles*=listCardinalidades.get(i);
        }

        System.out.println(instanciasUnicasPosibles);

        double ratio = hallarInstanciasUnicas(dataset)/instanciasUnicasPosibles;

        return ratio;
    }
}