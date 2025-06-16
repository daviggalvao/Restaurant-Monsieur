package classes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "cliente")
public class Cliente extends Pessoa {
    @Column(name = "numeroReservas")
    private int numeroReservas = 0;

    @Column(name = "fidelidade")
    private int fidelidade = 0;


    public Cliente() {}

    public Cliente(String nome, LocalDate dataAniversario, String endereco, String email, String senha) {
        super(nome, dataAniversario, endereco, email,senha);
    }
    public Cliente (String nome, LocalDate dataAniversario, String email, String senha) {
        super(nome,dataAniversario, email,senha);
    }

    public int getFidelidade() {return fidelidade;}
    public int getNumeroReservas() {return numeroReservas;}

    public void setNumeroReservas(int numeroReservas) {this.numeroReservas += numeroReservas;}
    public void setFidelidade(int fidelidade) {this.fidelidade = fidelidade;}

    @Transient
    public float descontoIdade(float valor){
        if (getDataAniversario() == null) { return valor;}
        int idade = Period.between(getDataAniversario(), LocalDate.now()).getYears();
        if (idade >= 70) { return valor * 0.8f;}
        if (idade >= 60) { return valor * 0.9f; }
        return valor;
    }
    @Override
    public boolean ehAniversario() {
        int dia,mes,ano;
        String[] Partes = super.getDataAniversario().split("/");
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
