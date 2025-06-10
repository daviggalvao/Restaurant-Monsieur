package classes;

import java.time.LocalDate;
import classes.FuncionarioCargo;

public class Funcionario extends Pessoa {
    private FuncionarioCargo cargo;
    private float salario;
    private String dataContrato;

    public Funcionario(String nome, String dataAniversario, String endereco, FuncionarioCargo cargo, float salario, String dataContrato, String senha, String email) {
        super(nome, dataAniversario, endereco, senha, email);
        this.cargo = cargo;
        this.salario = salario;
        this.dataContrato = dataContrato;
        char[] functId = this.getId().toCharArray();
        functId[0] = 'F';
        this.setId(String.valueOf(functId));
    }

    public FuncionarioCargo getCargo() {return cargo;}
    public float getSalario() {return salario;}
    public String getDataContrato() {return dataContrato;}  

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
