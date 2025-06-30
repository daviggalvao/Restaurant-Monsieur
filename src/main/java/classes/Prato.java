package classes;

import database.JpaUtil;
import jakarta.persistence.*;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
@Entity
@Table(name = "pratos")
public class Prato implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "preco", nullable = false)
    private float preco;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
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

    public void entregaPrato() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            ContaBancariaJose contaJose;
            try {
                // Tenta buscar a única instância de ContaBancariaJose (assumindo que existe apenas uma)
                TypedQuery<ContaBancariaJose> queryConta = em.createQuery("SELECT c FROM ContaBancariaJose c", ContaBancariaJose.class);
                queryConta.setMaxResults(1); // Limita a 1 resultado
                contaJose = queryConta.getSingleResult();
            } catch (NoResultException ex) {
                contaJose = new ContaBancariaJose();
                em.persist(contaJose);
                em.flush();
            }
            contaJose.setEntrada(contaJose.getEntrada() + this.preco);
            this.quantidade--;
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Erro ao achar ingrediente: " + ex.getMessage());
            ex.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erro");
            errorAlert.setHeaderText("Falha ao achar ingrediente");
            errorAlert.setContentText("Ocorreu um erro ao processar a transação. Por favor, tente novamente.\nDetalhes: " + ex.getMessage());
            errorAlert.showAndWait();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void fazPrato(){
        for(Ingrediente i : ingredientes){
            i.setQuantidade(i.getQuantidade() - 1);
        }
        this.quantidade++;
    }
}
