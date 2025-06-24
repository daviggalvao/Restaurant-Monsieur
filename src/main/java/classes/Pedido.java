package classes;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Pagamento pagamento;
    @OneToMany(mappedBy = "pedido", // 'pedido' is the field name in PedidoItem that maps back to Pedido
            cascade = CascadeType.ALL, // Operations (persist, merge, remove) on Pedido cascade to PedidoItem
            orphanRemoval = true,      // If a PedidoItem is removed from the collection, it's deleted
            fetch = FetchType.LAZY)
    private List<PedidoItem> itensPedido;
    @ManyToOne
    @JoinColumn(name = "consumidor_id")
    private Cliente consumidor;
    @Column
    private int frete;

    public Pedido(){
        this.itensPedido = new ArrayList<>();
    }

    public Pedido(Pagamento pagamento,List<PedidoItem> itens, Cliente consumidor){
        this.pagamento = pagamento;
        this.consumidor = consumidor;
        this.itensPedido = itens;
    }

    public Long getId() {return id;}
    public Pagamento getPagamento(){return this.pagamento;}
    public List<PedidoItem> getItensPedido(){return this.itensPedido;}
    public Cliente getConsumidor(){return this.consumidor;}
    public int getFrete(){return this.frete;}

    public void setId(Long id) {this.id = id;}
    public void setFrete(int frete){this.frete = frete;}
    public void setPagamento(Pagamento pagamento){this.pagamento = pagamento;}
    public void setItensPedido(List<PedidoItem> itens){this.itensPedido = itens;}
    public void setConsumidor(Cliente consumidor){this.consumidor = consumidor;}
    
    public void calcularFrete(){
        if(consumidor.getEndereco().equalsIgnoreCase("Centro")){
            this.frete = 10;
        }else if(consumidor.getEndereco().equalsIgnoreCase("Zona Leste")){
            this.frete = 15;
        }else if(consumidor.getEndereco().equalsIgnoreCase("Zona Sul")){
            this.frete = 20;
        }else if(consumidor.getEndereco().equalsIgnoreCase("Zona Norte")){
            this.frete = 25;
        }else if(consumidor.getEndereco().equalsIgnoreCase("Zona Oeste")){
            this.frete = 30;
        }else{
            this.frete = 40;
        }
    }

    public float calcularPrecoTotal(){
        float valorTotal = 0.0f;
        for (PedidoItem pedidoItem : this.itensPedido) {
            valorTotal += (pedidoItem.getPrato().getPreco() * pedidoItem.getQuantidade());
        }
        this.calcularFrete();
        valorTotal += this.frete;
        return valorTotal;
    }

    public void fidelidade(Cliente cliente){
        int quantidadePedido = cliente.getFidelidade();
        float preco = this.pagamento.getPreco();
        float descontoTotal = 0.0f;

        for(int i=1;i<=quantidadePedido;i++){
            preco -= (preco*0.05f);
        }

        if(this.frete == 0){this.calcularFrete();}
        float valorTotal = preco + this.frete;
        this.pagamento.setPreco(valorTotal);
    }
}
