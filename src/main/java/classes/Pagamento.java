package classes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Pagamento{
    @Column(name = "pagamento_preco") // Nome da coluna na tabela da entidade que incorporar Pagamento
    private float preco;
    @Column(name = "pagamento_nome") // Nome da coluna
    private String nome;
    @Column(name = "pagamento_tipo") // Nome da coluna
    private String tipo;
    @Column(name = "pagamento_parcelas") // Nome da coluna
    private int parcelas;

    public Pagamento(){}
    public Pagamento(float preco,String nome,String tipo,int parcelas){
        this.nome = nome;
        this.preco = preco;
        this.tipo = tipo;
        this.parcelas = parcelas;
    }

    public float getPreco(){return this.preco;} 
    public String getNome() {return this.nome;}
    public String getTipo(){return this.tipo;}
    public int getParcelas(){return this.parcelas;}

    public void setPreco(float preco){this.preco = preco;}
    public void setNome(String nome){this.nome = nome;}
    public void setTipo(String tipo){this.tipo = tipo;}
    public void setParcelas(int parcelas){this.parcelas = parcelas;}

    public boolean ehPix(){
        if(!this.tipo.equalsIgnoreCase("Pix"))return false;
        this.preco *= 0.9f;
        return true;
    }

    public void parcelamento(int num){
        for(int i=1; i<=num; i++){
            this.preco += this.preco*0.01f;
        }
    }
}