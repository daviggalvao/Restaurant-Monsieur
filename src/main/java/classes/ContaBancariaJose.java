package classes;

import jakarta.persistence.*;

@Entity
@Table(name = "ContaBancaria")
public class ContaBancariaJose {

    public ContaBancariaJose(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private static float saldo;
    @Column
    private static float entrada;
    @Column
    private static float saida;

    public static float getSaldo() {return saldo;}
    public static float getEntrada() {return entrada;}
    public static float getSaida() {return saida;}

    public static void setSaldo(float saldo) {ContaBancariaJose.saldo = saldo;}
    public static void setEntrada(float entrada) {ContaBancariaJose.entrada = entrada;}
    public static void setSaida(float saida) {ContaBancariaJose.saida = saida;}
}
