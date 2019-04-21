public class ConteoCompuesto {

    public static void main(String[] args) {
        int[] variable = {0,1};
        int[] valor = {1,2};

        int[][] ds = {
                {1,2},
                {1,1},
                {2,1},
                {0,0},
                {1,2},
                {1,2}
        };
        System.out.println(Conteo_Compuesto(variable,valor,ds));
        System.out.println(Conteo_simple(0,1,ds));
    }

    public static int Conteo_Compuesto(int[] var, int[] val, int[][] ds){
        int contador = 0;
        boolean flag = false;

        for (int i=0; i<ds.length; i++) {
            for (int j = 0; j < ds[i].length; j++) {
                if (ds[i][var[j]] != val[j]){
                    flag = false;
                    break;
                }
                else
                    flag=true;
            }
            if (flag==true)
                contador++;
        }
        return contador;
    }

    public static int Conteo_simple(int var, int val, int [][] ds){
        int contador = 0;

        for (int i=0; i<ds.length; i++){
            if (ds[i][var]== val)
                contador++;
        }
        return contador;
    }
}

