public class Conteocompuesto {

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

        funcion_conteoCompuesto(variable,valor,ds);
    }

    public static int funcion_conteoCompuesto(int[] var, int[] val, int[][] ds){
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

        System.out.print(contador);
        return 0;
    }
}

