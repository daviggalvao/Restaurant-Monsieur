package classes;

public class Ingrediente {
    private String id;
    private String nome;
    private float preco;
    private int quantidade;
    private String validade;

    public Ingrediente(String nome, float preco, int quantidade, String validade) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.validade = validade;
    }

    public String getId(){return id;}
    public String getNome() {return nome;}
    public float getPreco() {return preco;}
    public int getQuantidade() {return quantidade;}
    public String getValidade() {return validade;}

    public void setId(String id){this.id = id;}
    public void setNome(String nome) {this.nome = nome;}
    public void setPreco(float preco) {this.preco = preco;}
    public void setQuantidade(int quantidade) {this.quantidade = quantidade;}
    public void setValidade(String validade) {this.validade = validade;}

    void previsaoEstoque(){}
    void compoePrato(Prato prato){}
}
