package classes;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name = "pessoas")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "data_aniversario", nullable = false)
    private LocalDate dataAniversario; // RECOMENDADO: Alterado para LocalDate

    @Column(name = "endereco", length = 255)
    private String endereco;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "email", nullable = false)
    private String email;

    public Pessoa(){}

    public Pessoa(String nome,LocalDate dataAniversario, String email,String senha){
        this.dataAniversario=dataAniversario;
        this.nome=nome;
        this.email=email;
        this.senha=senha;
    }

    public Pessoa(String nome, LocalDate dataAniversario, String endereco, String email, String senha) {
        this.nome = nome;
        this.dataAniversario = dataAniversario;
        this.endereco = endereco;
        this.email = email;
        this.senha = senha;

    }
    public Long getId(){return id;}
    public LocalDate getDataAniversario() {return dataAniversario;}
    public String getNome() {return nome;}
    public String getEndereco() {return endereco;}

    public void setDataAniversario(LocalDate dataAniversario) {this.dataAniversario = dataAniversario;}
    public void setEndereco(String endereco) {this.endereco = endereco;}
    public void setNome(String nome) {this.nome = nome;}
    public void setId(Long id) {this.id = id;}

    @Transient //nao vira coluna
    public boolean ehAniversario(){
        if (this.dataAniversario == null) { return false;}
        LocalDate hoje = LocalDate.now();
        return this.dataAniversario.getMonthValue() == hoje.getMonthValue() &&
                this.dataAniversario.getDayOfMonth() == hoje.getDayOfMonth();
    }
}
