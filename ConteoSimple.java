public class ConteoSimple {

    public static void main(String[] args) {
        int variable = 0;
        int valor = 1;

        int[][] ds = {
            {1,2},
            {1,1},
            {2,1},
            {0,0}
        };

        funcion_conteo(variable,valor,ds);

    }

    public static int funcion_conteo(int var, int val, int[][] ds){
        int contador = 0;

        for (int i=0; i<ds.length; i++){
            if (ds[i][var] == val)
                contador++;
            }
        System.out.print(contador);
        return 0;
        }
    }
