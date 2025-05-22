package classes;

import java.time.LocalDate;

public class Funcionario extends Cliente {
    private static String id;
    private int idade;
    private FuncionarioCargo cargo;
    private float salario;
    private String dataContrato;

    public Funcionario(String nome, String dataAniversario, String endereco, int idade, FuncionarioCargo cargo, float salario, String dataContrato) {
        super(nome, dataAniversario, endereco);
        this.idade = idade;
        this.cargo = cargo;
        this.salario = salario;
        this.dataContrato = dataContrato;
    }

    public String getId() {return id;}
    public int getIdade() {return idade;}
    public FuncionarioCargo getCargo() {return cargo;}
    public float getSalario() {return salario;}
    public String getDataContrato() {return dataContrato;}  

    public void setId(String id) {this.id = id;}
    public void setIdade(int idade) {this.idade = idade;}
    public void setCargo(FuncionarioCargo cargo) {this.cargo = cargo;}
    public void setSalario(float salario) {this.salario = salario;}
    public void setDataContrato() {this.dataContrato = LocalDate.now().toString();}
    
    public void bonusSalarial(){
        if (super.ehAniversario()){
            this.salario += 100;
        }
        int tempoTrabalho = LocalDate.now().getYear() - Integer.parseInt(this.dataContrato.split("/")[2]);
        this.salario += (tempoTrabalho * 200);
    }

    public void promocao(){
        if (this.cargo == FuncionarioCargo.GARCOM){
            this.cargo = FuncionarioCargo.CHEF;
            this.salario += 200;
            this.dataContrato = LocalDate.now().toString();
        } else if (this.cargo == FuncionarioCargo.VENDEDOR){
            this.cargo = FuncionarioCargo.GERENTE;
            this.salario += 300;
            this.dataContrato = LocalDate.now().toString();
        } else if (this.cargo == FuncionarioCargo.ZELADOR){
            this.cargo = FuncionarioCargo.SUPERVISOR;
            this.salario += 150;
            this.dataContrato = LocalDate.now().toString();
        }
    }
}

enum FuncionarioCargo {
    GERENTE,
    VENDEDOR,
    SUPERVISOR,
    GARCOM,
    CHEF,
    ZELADOR
}