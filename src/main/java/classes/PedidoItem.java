package classes;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pedido_itens")
public class PedidoItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prato_id", nullable = false)
    private Prato prato;

    @Column(nullable = false)
    private int quantidade;

    public PedidoItem() {}

    public PedidoItem(Pedido pedido, Prato prato, int quantidade) {
        this.pedido = pedido;
        this.prato = prato;
        this.quantidade = quantidade;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Pedido getPedido() {return pedido;}
    public void setPedido(Pedido pedido) {this.pedido = pedido;}
    public Prato getPrato() {return prato;}
    public void setPrato(Prato prato) {this.prato = prato;}
    public int getQuantidade() {return quantidade;}
    public void setQuantidade(int quantidade) {this.quantidade = quantidade;}
}