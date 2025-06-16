package classes;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

@Entity
@Table(name = "cliente")
public class Cliente extends Pessoa {
    @Column(name = "numeroReservas")
    private int numeroReservas = 0;

    @Column(name = "fidelidade")
    private int fidelidade = 0;

    @OneToMany(
            mappedBy = "cliente",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private ArrayList<Reserva> reservas = new ArrayList<>();


    public Cliente() {}

    public Cliente(String nome, LocalDate dataAniversario, String endereco, String email, String senha) {
        super(nome, dataAniversario, endereco, email,senha);
    }
    public Cliente (String nome, LocalDate dataAniversario, String email, String senha) {
        super(nome,dataAniversario, email,senha);
    }

    public int getFidelidade() {return fidelidade;}
    public int getNumeroReservas() {return numeroReservas;}
    public ArrayList<Reserva> getReservas() {return reservas;}

    public void setNumeroReservas(int numeroReservas) {this.numeroReservas += numeroReservas;}
    public void setFidelidade(int fidelidade) {this.fidelidade = fidelidade;}
    public void setReservas(ArrayList<Reserva> reservas) {this.reservas = reservas;}

    @Transient
    public float descontoIdade(float valor){
        if (getDataAniversario() == null) { return valor;}
        int idade = Period.between(getDataAniversario(), LocalDate.now()).getYears();
        if (idade >= 70) { return valor * 0.8f;}
        if (idade >= 60) { return valor * 0.9f; }
        return valor;
    }
    @Override
    @Transient
    public boolean ehAniversario() {
        LocalDate dataAniversario = getDataAniversario();
        LocalDate hoje = LocalDate.now();
        if (dataAniversario == null) {return false;}
        if(dataAniversario.getDayOfMonth() == hoje.getDayOfMonth() &&
                dataAniversario.getMonthValue() == hoje.getMonthValue()){
            return true;}
        return false;
    }
}
