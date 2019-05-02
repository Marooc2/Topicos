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
        //INPUT
        //Matriz de adyacencia de g
        int[][] g = {{0,1},
                     {0,0},};

        //Cardinalidad de las variables de izquierda a derecha
        int[] card={124592,2};

        //Alpha
        int alpha = 1;

        //Lectura del dataset
        int[][] dataset = ReadDs("freud_dataset_procesado.txt");

        //RealizaDistribuciones(g,card,alpha,dataset);
        //ProbabilidadCondicionadaDiricht(variable,valores,card,alpha,dataset);
        //System.out.println(GeneraDistribuciones(g));

        //Variables
        int columnas = ContarColumnas(dataset);
        char[] varnames = new char[columnas];
        varnames = GeneraVariables(varnames);

        VisualizarDistribuciones(g,card,alpha,dataset,varnames);
        //System.out.println(HallarCardinalidad(dataset,1));

        //P(A|C)    -> P(A=0 | C=0) + P(A=1 | C=0) +
        //             P(A=0 | C=1) + P(A=1 | C=0)                  = 1

        //P(B|A)    -> P(B=0 | A=0) + P(B=1 | A=0) + P(B=3 | A=0) +
        //             P(A=0 | A=1) + P(A=1 | A=1) + P(B=3 | A=1)   = 1

        //P(C)      -> P(C=0) + P(C=1)

    }
    public static int ContarColumnas(int[][]ds){
        int aux = 0;

        for (int i=0;i<ds.length;i++){
            for (int j=0;j<ds[i].length;j++){
                aux+=1;
            }
            break;
        }
        return aux;
    }
    public static char[] GeneraVariables(char[] vn){

        for (int i=0;i<vn.length;i++){
            vn[i] = (char)('A'+i);
        }
        return vn;
    }
    public static int HallarCardinalidad(int [][] dataset, int col){
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int[][] ReadDs(String ds) throws FileNotFoundException{

        ArrayList<int[]> listDataset = new ArrayList<int[]>();

        FileReader fr = new FileReader(ds);
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
        int[] intento;

        auxvar = Arrays.copyOfRange(var,1,var.length);

        //El arreglo va a recorer hasta la cardinalidad de la variable 0 que es el hijo
        for (int i = 0; i < card[var[0]]; i++){
            ProbA = ProbabilidadConjuntaDiricht(var,vals[i],card,alpha,ds);

            auxval = Arrays.copyOfRange(vals[i],1,vals[i].length);

            if(auxvar.length > 1){
                ProbB = ProbabilidadConjuntaDiricht(auxvar,auxval,card,alpha,ds);
            }
            else{
                ProbB = ProbabilidadMarginalDiricht(auxvar[0],auxval[0],card[0],alpha,ds);
            }
            Distribucion[i] = ProbA/ProbB;
        }

        // Normalizar

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

            //System.out.println();
            System.out.println(listDis.get(i));

            //Si el arreglo de distribuciones en la posicion i es mayor que 1
            // hace la distribucion condicional del arreglo (A DADO B) A:{0,1} B:{0,1}
            if(listDis.get(i).size() > 1) {

                int[] val = new int[listDis.get(i).size()];
                int ivar;
                int cardAnterior = 1;
                int tamfactor = 1;
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
                    for (int l = 0;l<auxval.length;l++) {
                        System.out.print("Valor de las variables: ");
                        for (int o = 0; o < auxval[i].length; o++){
                            System.out.print(auxval[l][o]);
                            if(auxval[l].length - 1 == o)
                                System.out.println();
                        }
                    }
                    ProbabilidadCondicionadaDiricht(listDis.get(i).stream().mapToInt(Integer::intValue).toArray(), auxval, card, alpha, ds);
                    System.out.println();
                }
            }
            //De lo contrario hace marginal de los Padres
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
                    for (int l = 0;l<val.length;l++) {
                        System.out.println("Valor de la variable: " + val[l]);
                    }
                    System.out.println(ProbabilidadConjuntaDiricht(listDis.get(i).stream().mapToInt(Integer::intValue).toArray(), val, card, alpha, ds));
                }
            }

        }
    }

    public static void VisualizarDistribuciones(int[][] grafo, int[] card, int alpha, int[][]ds,char[] vn){

        List<List<Integer>> listDis = GeneraDistribuciones(grafo);

        int aux;

        System.out.println("Distribuciones de la matriz de Adyacencia");
        for (int i=0;i<listDis.size();i++) {
            for (int j = 0; j < listDis.get(i).size(); j++) {
                aux = listDis.get(i).get(j);
                switch (aux){
                    case 0:
                    {
                        System.out.print(vn[0]+ " ");
                        break;
                    }
                    case 1:
                    {
                        System.out.print(vn[1]+ " ");
                        break;
                    }
                    case 2:
                    {
                        System.out.print(vn[2]+ " ");
                        break;
                    }
                    case 3:
                    {
                        System.out.print(vn[3]+ " ");
                        break;
                    }
                    case 4:
                    {
                        System.out.print(vn[4]+ " ");
                        break;
                    }
                    case 5:
                    {
                        System.out.print(vn[5]+ " ");
                        break;
                    }
                    default:
                        System.out.println("N");
                }
            }
            System.out.println();
        }
        //RealizaDistribuciones(grafo,card,alpha,ds);

    }
}