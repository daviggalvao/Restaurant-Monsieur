package classes;

import java.time.LocalDate;

public class Pessoa {
    private String dataAniversario;
    private String endereco;
    private String nome;
    private static int idCount = 00000;
    private String id;

    public Pessoa(String nome, String dataAniversario, String endereco) {
        Pessoa.idCount++;
        this.id = String.format("%05d",Pessoa.idCount);
        this.nome = nome;
        this.dataAniversario = dataAniversario;
        this.endereco = endereco;
    }
    public String getId(){return id;}
    public String getDataAniversario() {return dataAniversario;}
    public String getNome() {return nome;}
    public String getEndereco() {return endereco;}

    public void setDataAniversario(String dataAniversario) {this.dataAniversario = dataAniversario;}
    public void setEndereco(String endereco) {this.endereco = endereco;}
    public void setNome(String nome) {this.nome = nome;}
    public void setId(String id) {this.id = id;}

    public boolean ehAniversario(){
        int dia,mes,ano;
        String[] Partes = this.dataAniversario.split("/");
        dia = Integer.parseInt(Partes[0]);
        mes = Integer.parseInt(Partes[1]);
        ano = Integer.parseInt(Partes[2]);
        LocalDate data = LocalDate.of(ano,mes,dia);
        LocalDate hoje = LocalDate.now();
        if (data.getDayOfMonth() == hoje.getDayOfMonth() && data.getMonthValue() == hoje.getMonthValue()){
            return true;
        } else {
            return false;
        }
    }
}
