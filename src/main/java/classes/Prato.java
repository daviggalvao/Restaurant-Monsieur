package classes;

import database.JpaUtil;
import jakarta.persistence.*;
import javafx.scene.control.Alert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.EntityTransaction;

@Entity
@Table(name = "pratos")
public class Prato implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType   .IDENTITY)
    private Long id;

    @Column(name = "preco", nullable = false)
    private float preco;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "prato_ingrediente",
            joinColumns = @JoinColumn(name = "prato_id"),
            inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
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
        this.ingredientes = listaIngredientes;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }
    public Prato() {}

    public static List<Prato> listarTodosComIngredientes() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        try {
            TypedQuery<Prato> query = em.createQuery("SELECT DISTINCT p FROM Prato p LEFT JOIN FETCH p.ingredientes", Prato.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public boolean fazPrato() {
        for (Ingrediente i : this.getIngredientes()) {
            if (i.getQuantidade() <= 0) {
                System.out.println("Não é possível cozinhar " + this.nome + ". Ingrediente em falta: " + i.getNome());
                return false;
            }
        }

        EntityManager em = JpaUtil.getFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            for (Ingrediente i : this.getIngredientes()) {
                Ingrediente ingredienteGerenciado = em.merge(i);
                ingredienteGerenciado.setQuantidade(ingredienteGerenciado.getQuantidade() - 1);
            }
            Prato pratoGerenciado = em.merge(this);
            pratoGerenciado.setQuantidade(pratoGerenciado.getQuantidade() + 1);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean entregaPrato() {
        if (this.getQuantidade() <= 0) {
            System.out.println("Não é possível entregar " + this.nome + ". Sem estoque de pratos prontos.");
            return false;
        }

        EntityManager em = JpaUtil.getFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            ContaBancariaJose contaJose;
            try {
                TypedQuery<ContaBancariaJose> queryConta = em.createQuery("SELECT c FROM ContaBancariaJose c", ContaBancariaJose.class);
                contaJose = queryConta.setMaxResults(1).getSingleResult();
            } catch (NoResultException ex) {
                System.out.println("Conta bancária não encontrada, criando uma nova.");
                contaJose = new ContaBancariaJose();
                em.persist(contaJose);
            }

            contaJose.setEntrada(contaJose.getEntrada() + this.preco);

            Prato pratoGerenciado = em.merge(this);
            pratoGerenciado.setQuantidade(pratoGerenciado.getQuantidade() - 1);

            em.merge(contaJose);

            transaction.commit();
            return true;

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erro na Transação");
            errorAlert.setHeaderText("Falha ao processar a entrega do prato.");
            errorAlert.setContentText("Ocorreu um erro: " + ex.getMessage());
            errorAlert.showAndWait();
            return false;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Long getId() {return id;}
    public float getPreco() {return preco;}
    public List<Ingrediente> getIngredientes() {return ingredientes;}
    public String getNome() {return nome;}
    public String getDescricao() {return descricao;}
    public int getQuantidade() {return quantidade;}
    public void setId(Long id) {this.id = id;}
    public void setPreco(float preco) {this.preco = preco;}
    public void setIngredientes(List<Ingrediente> listaIngredientes) {this.ingredientes = listaIngredientes;}
    public void setNome(String nome) {this.nome = nome;}
    public void setDescricao(String descricao) {this.descricao = descricao;}
    public void setQuantidade(int quantidade) {this.quantidade = quantidade;}
}