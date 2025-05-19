package classes;

import java.util.ArrayList;

public class Prato {
    private float preco;
    private ArrayList<Ingrediente> Ingredientes;
    private String nome;
    private String descricao;
    private int quantidade;

    public Prato(float preco, ArrayList<Ingrediente> ingredientes, String nome, String descricao, int quantidade) {
        this.preco = preco;
        Ingredientes = ingredientes;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public float getPreco() {return preco;}
    public ArrayList<Ingrediente> getIngredientes() {return Ingredientes;}
    public String getNome() {return nome;}
    public String getDescricao() {return descricao;}
    public int getQuantidade() {return quantidade;}

    public void setPreco(float preco) {this.preco = preco;}
    public void setIngredientes(ArrayList<Ingrediente> ingredientes) {Ingredientes = ingredientes;}
    public void setNome(String nome) {this.nome = nome;}
    public void setDescricao(String descricao) {this.descricao = descricao;}
    public void setQuantidade(int quantidade) {this.quantidade = quantidade;}

    void entregaPrato(){}
    void fazPrato(){}
}
