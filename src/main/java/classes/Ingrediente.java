package classes;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredientes")
public class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String nome;
    @Column
    private float preco;
    @Column
    private int quantidade;
    @Column
    private String validade;

    public Ingrediente(Long id, String nome, float preco, int quantidade, String validade) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.validade = validade;
    }
    public Ingrediente() {}

    public Long getId(){return id;}
    public String getNome() {return nome;}
    public float getPreco() {return preco;}
    public int getQuantidade() {return quantidade;}
    public String getValidade() {return validade;}

    public void setId(Long id){this.id = id;}
    public void setNome(String nome) {this.nome = nome;}
    public void setPreco(float preco) {this.preco = preco;}
    public void setQuantidade(int quantidade) {this.quantidade = quantidade;}
    public void setValidade(String validade) {this.validade = validade;}
    @Transient
    boolean precisaRepor(){
        return this.quantidade < 10;
    }
    @Transient
    boolean encomendaIngrediente(ContaBancariaJose conta, int quantidade){
        float precoEncomenda = quantidade * this.preco;
        if(conta.getSaldo() > precoEncomenda){
            conta.setSaida(conta.getSaida() + precoEncomenda);
            conta.setSaldo(conta.getSaldo() - precoEncomenda);
            this.quantidade += quantidade;
            return true;
        }
        return false;
    }
}
