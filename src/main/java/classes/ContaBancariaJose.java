package classes;

import jakarta.persistence.*;

@Entity
@Table(name = "ContaBancaria")
public class ContaBancariaJose {

    public ContaBancariaJose(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private float saldo;
    @Column(nullable = false)
    private float entrada;
    @Column(nullable = false)
    private float saida;

    public float getSaldo() {return saldo;}
    public float getEntrada() {return entrada;}
    public float getSaida() {return saida;}

    public void setSaldo(float saldo) {this.saldo = saldo;}
    public void setEntrada(float entrada) {this.entrada = entrada;}
    public void setSaida(float saida) {this.saida = saida;}
}
