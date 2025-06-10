package classes;

import java.util.ArrayList;

public class Prato {
    private String id;
    private float preco;
    private ArrayList<Ingrediente> ingredientes;
    private String nome;
    private String descricao;
    private int quantidade;

    public Prato(float preco, ArrayList<Ingrediente> listaIngredientes, String nome, String descricao, int quantidade) {
        this.preco = preco;
        ingredientes = listaIngredientes;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public String getId() {return id;}
    public float getPreco() {return preco;}
    public ArrayList<Ingrediente> getIngredientes() {return ingredientes;}
    public String getNome() {return nome;}
    public String getDescricao() {return descricao;}
    public int getQuantidade() {return quantidade;}

    
    public void setId(String id) {this.id = id;}
    public void setPreco(float preco) {this.preco = preco;}
    public void setIngredientes(ArrayList<Ingrediente> listaIngredientes) {ingredientes = listaIngredientes;}
    public void setNome(String nome) {this.nome = nome;}
    public void setDescricao(String descricao) {this.descricao = descricao;}
    public void setQuantidade(int quantidade) {this.quantidade = quantidade;}

    public void entregaPrato(ContaBancariaJose conta){
        conta.setEntrada(conta.getEntrada() + this.preco);
        this.quantidade--;
    }
    public void fazPrato(){
        for(Ingrediente i : ingredientes){
            i.setQuantidade(i.getQuantidade() - 1);
        }
        this.quantidade++;
    }
}
