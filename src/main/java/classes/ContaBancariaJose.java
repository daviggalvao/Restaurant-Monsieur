package classes;

public class ContaBancariaJose {
    private static float saldo;
    private static float entrada;
    private static float saida;

    public static float getSaldo() {return saldo;}
    public static float getEntrada() {return entrada;}
    public static float getSaida() {return saida;}

    public static void setSaldo(float saldo) {ContaBancariaJose.saldo = saldo;}
    public static void setEntrada(float entrada) {ContaBancariaJose.entrada = entrada;}
    public static void setSaida(float saida) {ContaBancariaJose.saida = saida;}
}
