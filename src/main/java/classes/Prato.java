package classes;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "pratos")
public class Prato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "preco", nullable = false)
    private float preco;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "prato_ingrediente",
            joinColumns = @ JoinColumn(name = "prato_id"), // Coluna na tabela de junção que referencia Prato
            inverseJoinColumns = @JoinColumn(name = "ingrediente_id") // Coluna na tabela de junção que referencia Ingrediente
    )
    private List<Ingrediente> ingredientes = new ArrayList<>();

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;
    @Column(name = "descricao", length = 500)
    private String descricao;
    @Column(name = "quantidade_estoque")
    private int quantidade;

    public Prato(float preco, List<Ingrediente> listaIngredientes, String nome, String descricao, int quantidade) {
        this.preco = preco;
        ingredientes = listaIngredientes;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }
    public Prato() {}

    public Long getId() {return id;}
    public float getPreco() {return preco;}
    public List<Ingrediente> getIngredientes() {return ingredientes;}
    public String getNome() {return nome;}
    public String getDescricao() {return descricao;}
    public int getQuantidade() {return quantidade;}


    public void setId(Long id) {this.id = id;}
    public void setPreco(float preco) {this.preco = preco;}
    public void setIngredientes(List<Ingrediente> listaIngredientes) {ingredientes = listaIngredientes;}
    public void setNome(String nome) {this.nome = nome;}
    public void setDescricao(String descricao) {this.descricao = descricao;}
    public void setQuantidade(int quantidade) {this.quantidade = quantidade;}

    public void entregaPrato(ContaBancariaJose conta){
        conta.setEntrada(conta.getEntrada() + this.preco);
        this.quantidade--;
    }
    public void fazPrato(){
        for(Ingrediente i : ingredientes){
            i.setQuantidade(i.getQuantidade() - 1);
        }
        this.quantidade++;
    }
}