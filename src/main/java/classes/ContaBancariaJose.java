package classes;

public class ContaBancariaJose {
    private float saldo;
    private float entrada;
    private float saida;

    public static float getSaldo() {return saldo;}
    public static float getEntrada() {return entrada;}
    public static float getSaida() {return saida;}

    public static void setSaldo(float saldo) {this.saldo = saldo;}
    public static void setEntrada(float entrada) {this.entrada = entrada;}
    public static void setSaida(float saida) {this.saida = saida;}
}
