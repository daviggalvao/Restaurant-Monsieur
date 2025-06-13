package classes;

import java.time.LocalDate;

public class Cliente extends Pessoa {
    private int numeroReservas = 0;
    private int fidelidade = 0;

    public Cliente(String nome, String dataAniversario, String endereco, String senha, String email) {
        super(nome, dataAniversario, endereco, senha, email);
    }

    public int getFidelidade() {return fidelidade;}
    public int getNumeroReservas() {return numeroReservas;}

    public void setNumeroReservas(int numeroReservas) {this.numeroReservas += numeroReservas;}
    public void setFidelidade(int fidelidade) {this.fidelidade = fidelidade;}

    public float descontoIdade(float valor){
        int dia,mes,ano;
        String[] Partes = this.getDataAniversario().split("/");
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
