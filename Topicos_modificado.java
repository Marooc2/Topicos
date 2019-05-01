import jdk.swing.interop.SwingInterOpUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

//Guardar cabeceras de los datasets

public class Topicos_modificado {
    public static void main(String[] args) throws FileNotFoundException {

        String[] varnames ={"A","B","C"};

        //Lectura del dataset
        int[][] dataset = ReadDs();

        //Matriz de adyacencia de g
        int[][] g = {{0,1,0},
                     {0,0,0},
                     {1,0,0}};
        //   A B C
        // A 0 1 0
        // B 0 0 0
        // C 1 0 0

        //Vector de variables
        int[] variable = {1,0};

        //Matriz de valores de las variables
        int[][] valores = {{0,50},
                           {1,50}};

        //Cardinalidad de las variables de izquierda a derecha
        int[] card = {2,3,2};

        //Alpha
        int alpha = 1;

        //ProbabilidadCondicionadaDiricht(variable,valores,card,alpha,dataset);
        //GeneraDistribuciones(g);
        //RealizaDistribuciones(g,card,alpha,dataset);
        VisualizarDistribuciones(g, varnames,card,alpha,dataset);

        //P(A|C)    -> P(A=0 | C=0) + P(A=1 | C=0) +
        //             P(A=0 | C=1) + P(A=1 | C=0)
        //P(B|A)    -> P(B=0 | A=0) + P(B=1 | A=0) + P(B=3 | A=0) +
        //             P(A=0 | A=1) + P(A=1 | A=1) + P(B=3 | A=1)
        //P(C)      -> P(C=0) + P(C=1)

    }

    public static int[][] ReadDs() throws FileNotFoundException{

        ArrayList<int[]> listDataset = new ArrayList<int[]>();

        FileReader fr = new FileReader("data.txt");
        Scanner s = new Scanner(fr);

        while(s.hasNextLine()){
            String linea = s.nextLine();
            String[] campos = linea.split("\t");
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
    public static double Cardinalid(int [][] dataset){
        double instancias = 0.0;
        for (int i=0;i < dataset.length;i++){
            instancias += 1.0;
        }
        return instancias;
    }

    public static double ProbabilidadMarginal(int var, int val, int[][] ds){
        double contador = 0.0;
        double probMarginal;

        for (int i=0; i<ds.length; i++){
            if (ds[i][var] == val)
                contador += 1.0;
        }
        probMarginal = contador/ds.length;
        return probMarginal;
    }

    public static double ProbabilidadConjunta(int[] var, int[] val, int[][] ds){
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

        probConjunta = contador/ds.length;
        return probConjunta;
    }

    public static double ProbabilidadCondicionada(int var[], int[] val, int[][]ds){
        double PB = ProbabilidadMarginal(var[1],val[1],ds);
        //System.out.println(PB);
        double PAB = ProbabilidadConjunta(var,val,ds);
        //System.out.println(PAB);
        double probCondionada = PAB/PB;
        return probCondionada;
    }

    public static double ProbabilidadMarginalDiricht(int var, int val,int card, int alpha, int [][] ds){
        double contador = 0.0;
        double probMarginalD;

        for (int i=0; i<ds.length; i++){
            if (ds[i][var] == val)
                contador +=1.0;
        }
        probMarginalD = (contador + alpha)/(ds.length + card * alpha);
        return probMarginalD;
    }

    public static double ProbabilidadConjuntaDiricht(int[] var, int[] val, int[] card, int alpha, int[][]ds){
        double contador = 0.0;
        double probConjuntaD;
        double prodCard = 1.0;
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

        for (int i=0; i<var.length; i++) {
            prodCard = prodCard * card[var[i]];
        }

        probConjuntaD = (contador + alpha)/(ds.length + (prodCard*alpha));
        return probConjuntaD;
    }

    public static void ProbabilidadCondicionadaDiricht(int[] var, int[][] vals, int[] card, int alpha, int[][]ds){
        double ProbA;
        double ProbB;
        double SumProb = 0.0;
        double[] Distribucion = new double[card[var[0]]];
        int[] auxvar;
        int[] auxval;

        auxvar = Arrays.copyOfRange(var,1,var.length);

        //El arreglo va a recorer hasta la cardinalidad de la variable 0 que es el hijo
        for (int i = 0; i < card[var[0]]; i++){
            ProbA = ProbabilidadConjuntaDiricht(var,vals[i],card,alpha,ds);

            auxval = Arrays.copyOfRange(vals[i],1,vals[i].length);

            if(auxvar.length > 1){
                ProbB = ProbabilidadConjuntaDiricht(auxvar,auxval,card,alpha,ds);
            }
            else
                ProbB = ProbabilidadMarginalDiricht(auxvar[0],auxval[0],card[0],alpha,ds);
            Distribucion[i] = ProbA/ProbB;
        }
        for (int i=0; i<Distribucion.length;i++){
            SumProb += Distribucion[i];
        }
        for (int j=0; j<Distribucion.length;j++){
            Distribucion[j] = Distribucion[j]/SumProb;
        }
        for (int k=0;k<Distribucion.length;k++){
            System.out.println(Distribucion[k]);
        }
    }

    public static List<List<Integer>> GeneraDistribuciones(int[][] grafo){

        List<List<Integer>> listPosicionesDis = new ArrayList<List<Integer>>();

        for (int i=0; i < grafo.length; i++){
            List<Integer> listAux = new ArrayList<Integer>();
            listAux.add(i);
            for (int j=0; j< grafo[i].length;j++){
                if(grafo[j][i] == 1)
                    listAux.add(j);
            }
            listPosicionesDis.add(listAux);

        }
        return listPosicionesDis;
    }

    public static void RealizaDistribuciones(int[][] grafo, int[] card, int alpha, int[][]ds){

        List<List<Integer>> listDis = GeneraDistribuciones(grafo);

        for(int i = 0;i<listDis.size();i++){

            System.out.println(" ");
            System.out.println(listDis.get(i));
            System.out.println(" ");

            if(listDis.get(i).size() > 1) {

                int[] val = new int[listDis.get(i).size()];
                int ivar;
                int cardAnterior = 1;
                int tamfactor = 1;
                double[] arrDis;
                int[][] auxval = new int [card[listDis.get(i).get(0)]][listDis.get(i).size()];

                for (int p = 0; p < listDis.get(i).size(); p++) {
                    tamfactor *= card[listDis.get(i).get(p)];
                }
                double[] probs = new double[tamfactor];

                for (int k = 0; k < probs.length; k+= card[listDis.get(i).get(0)]) {
                    for (int x = 0; x< card[listDis.get(i).get(0)];x++){
                        cardAnterior = 1;
                        for (int j = 0; j < listDis.get(i).size(); j++) {
                            ivar = listDis.get(i).get(j);
                            val[j] = (int) (Math.floor((k+x) / cardAnterior) % card[ivar]);
                            cardAnterior *= card[ivar];
                        }
                        auxval[x] = val.clone();
                    }
                    ProbabilidadCondicionadaDiricht(listDis.get(i).stream().mapToInt(Integer::intValue).toArray(), auxval, card, alpha, ds);
                }
            }
            else{
                int []val = new int[listDis.get(i).size()];
                int ivar;
                int cardAnterior = 1;
                int tamfactor = 1;
                for(int p = 0;p<listDis.get(i).size();p++){
                    tamfactor *= card[listDis.get(i).get(p)];
                }
                double[] probs = new double[tamfactor];
                for(int k = 0; k<probs.length;k++) {
                    cardAnterior = 1;
                    for(int j = 0;j < listDis.get(i).size(); j++){
                        ivar = listDis.get(i).get(j);
                        val[j] = (int)(Math.floor (k/cardAnterior)%card[ivar]);
                        cardAnterior *= card[ivar];
                    }
                    System.out.println(ProbabilidadConjuntaDiricht(listDis.get(i).stream().mapToInt(Integer::intValue).toArray(), val, card, alpha, ds));
                }
            }

        }
    }

    public static void VisualizarDistribuciones(int[][] grafo, String[] varnames, int[] card, int alpha, int[][]ds){

        List<List<Integer>> listDis = GeneraDistribuciones(grafo);
        double[] valoresDis;

        int aux;

        System.out.println("Distribuciones de la matriz de Adyacencia");
        for (int i=0;i<listDis.size();i++) {
            for (int j = 0; j < listDis.get(i).size(); j++) {
                aux = listDis.get(i).get(j);
                switch (aux){
                    case 0:
                    {
                        System.out.print(varnames[0]+ " ");
                        break;
                    }
                    case 1:
                    {
                        System.out.print(varnames[1]+ " ");
                        break;
                    }
                    case 2:
                    {
                        System.out.print(varnames[2]+ " ");
                        break;
                    }
                    default:
                        System.out.println("N");
                }
            }
            System.out.println(" ");
        }
        RealizaDistribuciones(grafo,card,alpha,ds);

    }
}