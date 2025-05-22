package classes;

import java.util.ArrayList;

public class Pedido {
    private Pagamento pagamento;
    private ArrayList<Prato> pratos;
    private ArrayList<Integer> quantidades;
    private Cliente consumidor;
    private int frete;

    public Pedido(){
        this.pratos = new ArrayList<>();
        this.quantidades = new ArrayList<>();
    }

    public Pedido(Pagamento pagamento,ArrayList<Prato> pratos,ArrayList<Integer> quantidades,
    Cliente consumidor){
        this.pagamento = pagamento;
        this.pratos = pratos;
        this.quantidades = quantidades;
        this.consumidor = consumidor;
    }

    public Pagamento getPagamento(){return this.pagamento;}
    public ArrayList<Prato> getPratos(){return this.pratos;}
    public ArrayList<Integer> getQuantidades(){return this.quantidades;}
    public Cliente getConsumidor(){return this.consumidor;}
    public int getFrete(){return this.frete;}

    public void setFrete(int frete){this.frete = frete;}
    public void setPagamento(Pagamento pagamento){this.pagamento = pagamento;}
    public void setPratos(ArrayList<Prato> pratos){this.pratos = pratos;}
    public void setQuantidades(ArrayList<Integer> quantidades){this.quantidades = quantidades;}
    public void setConsumidor(Cliente consumidor){this.consumidor = consumidor;}
    
    public void calcularFrete(){
        if(cliente.getEndereco().equalsIgnoreCase("Centro")){
            this.frete = 10;
        }else if(consumidor.getEndereco().equalsIgnoreCase("Zona Leste")){
            this.frete = 15;
        }else if(consumidor.getEndereco().equalsIgnoreCase("Zona Sul")){
            this.frete = 20;
        }else if(consumidor.getEndereco().equalsIgnoreCase("Zona Norte")){
            this.frete = 25;
        }else if(consumidor.getEndereco().equalsIgnoreCase("Zona Oeste")){
            this.frete = 30;
        }
    }

    public float calcularPrecoTotal(){
        float valorTotal = 0.0f;
        for(int i=0;i<this.pratos.size();i++){
            valorTotal += (this.pratos.get(i).getPreco()*this.quantidades.get(i));
        }
        this.calcularFrete();
        valorTotal += this.frete;
        return valorTotal;
    }

    public void fidelidade(Cliente cliente){
        
    }
}
