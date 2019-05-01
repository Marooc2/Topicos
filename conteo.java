import java.lang.reflect.Array;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class conteo {

    public static Map<Integer,Integer> cardinalidades = new HashMap<Integer, Integer>();
    public static Map<Integer,Integer> colDataset = new HashMap<Integer, Integer>();
    public static int M = 0;

    public static void main(String[] args) throws FileNotFoundException {

        int[] columnsToRead = {0,1,2};

        int[][] dataset = read(columnsToRead, "data.txt", false);

        distribucionMarginal(1, dataset, 0);

        int var[] = {0,1};
        int vals[] = {0,1};

        //distribucionConjunta(var, dataset, 0);

        int var2[] = {0}; //dado
        int vals2[] = {50};

        double[] valores = {0.7666777155420222,0.2555592385140074};

        // System.out.println(normalizar(valores)[0]);

        //dataset = read(columnsToRead);
        //System.out.println(dataset.length);
        //System.out.println(contar(var, vals, dataset));

        //System.out.println(1.0*contar(var, vals, dataset)/dataset.length);
        // System.out.println(1.0*contar(var, vals, dataset)/dataset.length);
    }

    public static int getNroVariables(String ruta) throws FileNotFoundException{
        FileReader fr = new FileReader(ruta);
        Scanner s = new Scanner(fr);
        String[] campos = s.nextLine().split(",");
        return campos.length;
    }

    public static int[][] read(int[] cols, String path, boolean hasHeader) throws FileNotFoundException{ //agregar como parametro ruta del dataset

        ArrayList<int[]> listDataset = new ArrayList<int[]>();
        List<Integer> listCardinalidades = new ArrayList<Integer>();
        Map<Integer,Integer>[] uniques = new HashMap[cols.length];

        for (int i = 0; i < cols.length; i++) {
            uniques[i] = new HashMap<Integer, Integer>();
            colDataset.put(cols[i], i);
        }

        FileReader fr = new FileReader(path);
        Scanner s = new Scanner(fr);

        if(hasHeader)
            s.nextLine();

        System.out.println("Leyendo dataset...");

        while(s.hasNextLine()){
            String linea = s.nextLine();
            String[] campos = linea.split("\t");
            int [] camposInt = new int[campos.length];
            for (int i = 0; i < cols.length; i++){
                camposInt[i] = Integer.parseInt(campos[cols[i]]);
                uniques[i].put(camposInt[i], 0);
            }
            listDataset.add(camposInt);
            M++;
        }

        System.out.println();
        System.out.println("Datos del dataset: ");
        System.out.println("------------------");
        System.out.println("Cantidad de filas: "+M);

        for(int i = 0; i < cols.length; i++) {
            System.out.print("Cardinalidad de columna "+ cols[i] + ": ");
            cardinalidades.put(cols[i],uniques[i].size());
            System.out.println(cardinalidades.get(cols[i]));
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

    public static int contar(int[] var, int []vals, int dataset[][]){
        int cont = 0;
        for(int y = 0; y < dataset.length; y++){
            boolean allTrue = true;
            //if todos los vals son iguales por posicion
            for(int i = 0; i < vals.length; i++){
                if(dataset[y][var[i]] != vals[i]){
                    allTrue = false;
                    continue;
                }
            }
            if(allTrue){
                cont+=1;
            }
        }
        return cont;
    }

    public static double distribucionCondicional(int[] var, int vals[], int dataset[][]){

        int[] newVar = new int[1];
        newVar[0] = var[1];
        int[] newVals = new int[1];
        newVals[0] = vals[1];
        return 1.0*contar(var, vals, dataset)/contar(newVar, newVals, dataset);
    }

    public static double distribucionCondicional2(int[] var, int vals[], int dataset[][]){
        return 1.0*contadorCondicional(var,vals,dataset)/dataset[0].length;
    }

    public static double distribucionCondicional3(int[] var, int vals[], int[] givenVar, int givenVals[], int dataset[][], double a){

        int newSize = var.length+givenVar.length;

        int[] newVars = new int[newSize];

        for(int i = 0; i < newSize; i++){
            newVars[0] = i;
        }

        int[] newVals = new int[newSize];

        int j = 0;
        int j2 = 0;
        boolean maxJ = false;
        for(int i = 0; i < newSize; i++){
            if(maxJ == false){
                newVals[i] = vals[j];
            }
            else{
                newVals[i] = givenVals[j2];
                j2++;
            }
            j++;
            if(j>=vals.length-1)
                maxJ = true;
        }

        int M = dataset.length;
        double multiplicacionDeCardinalidades = 1;

        for(int i = 0; i < newVars.length; i++){
            System.out.println("Here i am");
            System.out.println(hallarCardinalidad(dataset, newVars[i]));
            multiplicacionDeCardinalidades = multiplicacionDeCardinalidades * hallarCardinalidad(dataset, newVars[i]);
        }

        double multiplicacionDeCardinalidades2 = 1;

        for(int i = 0; i < givenVar.length; i++){
            multiplicacionDeCardinalidades2 = multiplicacionDeCardinalidades2 * hallarCardinalidad(dataset, givenVar[i]);
        }

        return 1.0*(((contar(newVars,newVals,dataset)+a)/(M+multiplicacionDeCardinalidades*a))/((contar(givenVar, givenVals,dataset)+a)/(M+multiplicacionDeCardinalidades2*a)));
    }

    public static int contadorCondicional(int[] var, int vals[], int dataset[][]){
        int cont = 0;

        for(int i = 0; i < dataset[0].length; i++){
            if(dataset[i][var[1]] == vals[1]){
                if(dataset[i][var[0]] == vals[0]){
                    cont+=1;
                }
            }
        }
        return cont;
    }

    public static double[] normalizar(double []valores){
        double suma = 0;
        double [] probabilidadesFinales = new double[valores.length];
        for(int i = 0; i < valores.length; i++){
            suma+=valores[i];
        }
        for(int i = 0; i < probabilidadesFinales.length; i++){
            probabilidadesFinales[i] = valores[i]/suma;
        }
        return probabilidadesFinales;
    }

    public static double probabilidadMarginal(int var, int val, int dataset[][], double alpha){

        int[] varUnica = new int[1];
        varUnica[0] = colDataset.get(var);
        int[] valUnico = new int[1];
        valUnico[0] = val;

        return (contar(varUnica, valUnico, dataset)+alpha)/(M+cardinalidades.get(var)*alpha);
    }

    public static double probabilidadConjunta(int var[], int vals[], int dataset[][], double alpha){

        double multiplicacionCardinalidades = 1;

        for(int i = 0; i < var.length; i++){
            multiplicacionCardinalidades *= cardinalidades.get(colDataset.get(var[i]));
        }

        return (contar(var, vals, dataset)+alpha)/(M+multiplicacionCardinalidades*alpha);
    }

    public static void distribucionMarginal(int col, int dataset[][], double alpha){
        System.out.println();
        System.out.println("Distribucion Marginal de columna: " + col);
        double suma = 0;
        for(int i = 0; i < cardinalidades.get(col); i++){
            double probM = probabilidadMarginal(col, i, dataset, alpha);
            suma+=probM;
            System.out.println("P( " + col + " = " + i +" ) = "+probM);
        }
        System.out.println("Suma: " + suma);
    }

    public static void distribucionConjunta(int cols[], int dataset[][], double alpha){
        System.out.println();
        String strCols = new String();

        for(int i = 0; i < cols.length; i++){
            strCols+=Integer.toString(cols[i]) + ", ";
        }
        strCols = strCols.substring(0, strCols.length()-2);

        List<List<Integer>> listOfList = new ArrayList<List<Integer>>();

        for(int i = 0; i < cols.length; i++){
            List<Integer> vals = new ArrayList<>();
            for(int j = 0; j < cardinalidades.get(cols[i]); j++){
                vals.add(j);
            }
            listOfList.add(vals);
        }

        List<List<Integer>> res = new ArrayList<List<Integer>>();

        Collections.reverse(listOfList);

        generatePermutations(listOfList, res, 0, new ArrayList<>());

        System.out.println("Distribucion Conjunta de las columnas: " + strCols);
        for(int i = 0; i < res.size();i++){
            List<Integer> varsProb = new ArrayList<Integer>();
            List<Integer> valsProb = new ArrayList<Integer>();
            System.out.print("P( ");
            for(int j = 0; j < res.get(i).size(); j++){
                int n = res.get(i).size()-j-1;
                //System.out.print(res.get(i).get(n)+" = "++", ");
                varsProb.add(j);
                valsProb.add(res.get(i).get(n));
            }
            String newStr = new String();
            for(int j = 0; j < varsProb.size(); j++){

                newStr += varsProb.get(j)+" = "+valsProb.get(j)+", ";

            }
            newStr = newStr.substring(0, newStr.length()-2);
            System.out.print(newStr);
            System.out.println(" ) = " +probabilidadConjunta(toIntArray(varsProb), toIntArray(valsProb), dataset, 0));
        }
    }

    public static int[] toIntArray(List<Integer> list){
        int[] ret = new int[list.size()];
        for(int i = 0;i < ret.length;i++)
            ret[i] = list.get(i);
        return ret;
    }

    public static void generatePermutations(List<List<Integer>> Lists, List<List<Integer>> result, int depth, List<Integer> current)
    {
        if(depth == Lists.size())
        {
            List<Integer> permutation = new ArrayList<>(current);
            result.add(permutation);
            return;
        }

        for(int i = 0; i < Lists.get(depth).size(); i++)
        {
            List<Integer> actualList = new ArrayList<>(current);
            current.add(Lists.get(depth).get(i));
            generatePermutations(Lists, result, depth + 1, current);
            current = actualList;
        }
    }
}