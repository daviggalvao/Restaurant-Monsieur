package classes;

import database.JpaUtil;
import jakarta.persistence.*;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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
    private LocalDate validade;

    public Ingrediente(String nome, float preco, int quantidade, LocalDate validade) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.validade = validade;
    }

    public Ingrediente() {}
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public float getPreco() { return preco; }
    public int getQuantidade() { return quantidade; }
    public LocalDate getValidade() { return validade; }
    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setPreco(float preco) { this.preco = preco; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setValidade(LocalDate validade) { this.validade = validade; }

    public static List<Ingrediente> listarTodos() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        try {
            TypedQuery<Ingrediente> query = em.createQuery("SELECT i FROM Ingrediente i", Ingrediente.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao listar ingredientes: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Transient
    public boolean precisaRepor() {
        return this.quantidade < 10;
    }

    @Transient
    public boolean encomendaIngrediente(int quantidadeAComprar) {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            ContaBancariaJose contaJose;
            try {
                TypedQuery<ContaBancariaJose> queryConta = em.createQuery("SELECT c FROM ContaBancariaJose c", ContaBancariaJose.class);
                contaJose = queryConta.setMaxResults(1).getSingleResult();
            } catch (NoResultException ex) {
                System.err.println("Conta bancária não encontrada. Impossível realizar a compra.");
                transaction.rollback();
                return false;
            }

            float precoEncomenda = quantidadeAComprar * this.preco;
            if (contaJose.getSaldo() >= precoEncomenda) {
                contaJose.setSaida(contaJose.getSaida() + precoEncomenda);
                contaJose.setSaldo(contaJose.getSaldo() - precoEncomenda);
                this.quantidade += quantidadeAComprar;

                em.merge(contaJose);
                em.merge(this);

                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                System.out.println("Saldo insuficiente para realizar a encomenda.");
                return false; // Falha
            }
        } catch (Exception ex) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Erro ao processar encomenda: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}