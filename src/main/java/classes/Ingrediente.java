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

    // --- MUDANÇA 1: MÉTODO ESTÁTICO PARA BUSCAR TODOS OS INGREDIENTES ---
    /**
     * Busca no banco de dados e retorna uma lista de todos os ingredientes.
     * Este método é estático, o que significa que pode ser chamado diretamente
     * da classe (ex: Ingrediente.listarTodos()) sem precisar de um objeto.
     * @return Uma lista de Ingredientes.
     */
    public static List<Ingrediente> listarTodos() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        try {
            TypedQuery<Ingrediente> query = em.createQuery("SELECT i FROM Ingrediente i", Ingrediente.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao listar ingredientes: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList(); // Retorna uma lista vazia em caso de erro
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

    // --- MUDANÇA 2: CORREÇÃO DO MÉTODO DE ENCOMENDA ---
    /**
     * Processa a encomenda de uma certa quantidade do ingrediente, atualizando
     * o banco de dados.
     * @param quantidadeAComprar A quantidade a ser encomendada.
     * @return true se a compra foi bem-sucedida, false caso contrário (ex: saldo insuficiente).
     */
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
                // Se não houver conta, a compra não pode ser efetuada.
                System.err.println("Conta bancária não encontrada. Impossível realizar a compra.");
                transaction.rollback();
                return false;
            }

            float precoEncomenda = quantidadeAComprar * this.preco;
            if (contaJose.getSaldo() >= precoEncomenda) {
                // Atualiza os valores nos objetos
                contaJose.setSaida(contaJose.getSaida() + precoEncomenda);
                contaJose.setSaldo(contaJose.getSaldo() - precoEncomenda);
                this.quantidade += quantidadeAComprar;

                // **CORREÇÃO**: Persiste as alterações no banco de dados usando merge()
                em.merge(contaJose);
                em.merge(this);

                // **CORREÇÃO**: Confirma a transação para salvar as mudanças
                transaction.commit();
                return true; // Sucesso
            } else {
                // Saldo insuficiente, desfaz a transação
                transaction.rollback();
                System.out.println("Saldo insuficiente para realizar a encomenda.");
                return false; // Falha
            }
        } catch (Exception ex) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            // A lógica de alerta foi mantida, mas a exceção é tratada
            System.err.println("Erro ao processar encomenda: " + ex.getMessage());
            ex.printStackTrace();
            return false; // Falha
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}