package classes;

import database.JpaUtil;
import jakarta.persistence.*;
import javafx.scene.control.Alert;

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

    public Ingrediente() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public float getPreco() {
        return preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getValidade() {
        return validade;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    @Transient
    public boolean precisaRepor() {
        return this.quantidade < 10;
    }

    @Transient
    public void encomendaIngrediente(int quantidade) {
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

            float precoEncomenda = quantidade * this.preco;
            if (contaJose.getSaldo() > precoEncomenda){
                contaJose.setSaida(contaJose.getSaida() + precoEncomenda);
                contaJose.setSaldo(contaJose.getSaldo() - precoEncomenda);
                this.quantidade += quantidade;
            }
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
}

