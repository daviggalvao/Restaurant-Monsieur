package classes;


public class Pagamento{
    private float preco;
    private String nome;
    private String tipo;
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
        this.preco *= 0.9;
        return true;
    }

    public void parcelamento(int num){
        for(int i=1; i<=num; i++){
            this.preco += this.preco*0.01;
        }
    }
}