import jdk.swing.interop.SwingInterOpUtils;

import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Topicos {
    public static void main(String[] args) throws FileNotFoundException {

        int[][] dataset = read();
        double instancias = NumInstancias(dataset);
        int[] variable = {0,1};
        int[] valor = {0,0};
        System.out.println("Probabilidad Marginal: " +
                ProbabilidadMarginal(variable[0],valor[0],instancias,dataset));
        System.out.println("Probabilidad Conjunta: " +
                ProbabilidadConjunta(variable,valor,instancias,dataset));
        System.out.println("Probabilidad Condicional: " +
                ProbabilidadCondicionada(variable,valor,instancias,dataset));
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

    public static double NumInstancias(int [][] dataset){
        double instancias = 0.0;
        for (int i=0;i < dataset.length;i++){
            instancias += 1.0;
        }
        return instancias;
    }

    public static double ProbabilidadMarginal(int var, int val, double instancias, int[][] ds){
        double contador = 0.0;
        double probMarginal;

        for (int i=0; i<ds.length; i++){
            if (ds[i][var] == val)
                contador += 1.0;
        }
        probMarginal = contador/instancias;
        return probMarginal;
    }

    public static double ProbabilidadConjunta(int[] var, int[] val, double instancias, int[][] ds){
        double contador = 0.0;
        double probConjunta;
        boolean flag = false;
        int aux = var.length;
        for (int i=0; i<ds.length; i++) {
            for (int j = 0; j < aux; j++) {
                if (ds[i][var[j]] != val[j]){
                    flag = false;
                    break;
                }
                else
                    flag=true;
            }
            if (flag==true)
                contador+=1.0;
        }

        probConjunta = contador/instancias;
        return probConjunta;
    }

    public static double ProbabilidadCondicionada(int var[], int[] val, double instancias, int[][]ds){
        double PB = ProbabilidadMarginal(var[1],val[1],instancias,ds);
        //System.out.println(PB);
        double PAB = ProbabilidadConjunta(var,val,instancias,ds);
        //System.out.println(PAB);
        double probCondionada = PAB/PB;
        return probCondionada;
    }
}