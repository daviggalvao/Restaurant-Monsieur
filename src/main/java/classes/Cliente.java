package classes;

import java.time.LocalDate;

public class Cliente {
    private String dataAniversario;
    private int numeroReservas = 0;
    private String endereco;
    private int fidelidade = 0;
    private String nome;    


    public Cliente(String nome, String dataAniversario, String endereco) {
        this.nome = nome;
        this.dataAniversario = dataAniversario;
        this.endereco = endereco;
    }

    public String getDataAniversario() {return dataAniversario;}
    public int getFidelidade() {return fidelidade;}
    public String getNome() {return nome;}
    public int getNumeroReservas() {return numeroReservas;}
    public String getEndereco() {return endereco;}

    public void setDataAniversario(String dataAniversario) {this.dataAniversario = dataAniversario;}
    public void setNumeroReservas(int numeroReservas) {this.numeroReservas = numeroReservas;}
    public void setEndereco(String endereco) {this.endereco = endereco;}
    public void setNome(String nome) {this.nome = nome;}

    public boolean ehAniversario(){
        int dia,mes,ano;
        String[] Partes = this.dataAniversario.split("/")
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

    public float descontoIdade(float valor){
        int dia,mes,ano;
        String[] Partes = this.dataAniversario.split("/")
        dia = Integer.parseInt(Partes[0]);
        mes = Integer.parseInt(Partes[1]);
        ano = Integer.parseInt(Partes[2]);
        LocalDate data = LocalDate.of(ano,mes,dia);
        LocalDate hoje = LocalDate.now();
        int idade = hoje.getYear() - data.getYear();
        if (hoje.getMonthValue() < data.getMonthValue() || (hoje.getMonthValue() == data.getMonthValue() && hoje.getDayOfMonth() < data.getDayOfMonth())){
            idade--;
        }
        if (idade >= 60){
            if (idade >= 70) return valor * 0.8f;
            return valor * 0.9f;
        } else {
            return valor;
        }
    }
}
