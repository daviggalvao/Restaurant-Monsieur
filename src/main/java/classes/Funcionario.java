package classes;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Funcionario extends Pessoa {
    private ObjectProperty<FuncionarioCargo> cargo;
    private float salario;
    private String dataContrato;
    float base = this.salario;

    public Funcionario(String nome, LocalDate dataAniversario, String endereco, FuncionarioCargo cargo, float salario, String dataContrato, String senha, String email) {
        super(nome, dataAniversario, endereco, email, senha);
        this.cargo = new SimpleObjectProperty<>();
        this.cargo.set(cargo);
        this.salario = salario;
        this.dataContrato = dataContrato;
    }

    public ObjectProperty<FuncionarioCargo> getCargo() {return cargo;}
    public float getSalario() {return salario;}
    public String getDataContrato() {return dataContrato;}  

    public void setCargo(ObjectProperty<FuncionarioCargo> cargo) {this.cargo = cargo;}
    public void setSalario(float salario) {this.salario = salario;}
    public void setDataContrato() {this.dataContrato = LocalDate.now().toString();}

    public void bonusSalarial(){
        if (this.ehAniversario()){
            this.salario += 100;
        }
        else
            this.salario = base;
        int tempoTrabalho = LocalDate.now().getYear() - Integer.parseInt(this.dataContrato.split("/")[2]);
        this.salario += (tempoTrabalho * 200);
    }

    public void promocao(){
        if (this.getCargo().get() == FuncionarioCargo.GARCOM){
            this.cargo.set(FuncionarioCargo.CHEF);
            this.salario = base + 200;
            base += 200;
            this.dataContrato = LocalDate.now().toString();
        } else if (this.getCargo().get() == FuncionarioCargo.VENDEDOR){
            this.cargo.set(FuncionarioCargo.GERENTE);
            this.salario = base + 300;
            base += 300;
            this.dataContrato = LocalDate.now().toString();
        } else if (this.getCargo().get() == FuncionarioCargo.ZELADOR){
            this.cargo.set(FuncionarioCargo.SUPERVISOR);
            this.salario = base + 150;
            base += 150;
            this.dataContrato = LocalDate.now().toString();
        }
    }

    @Override
    public boolean ehAniversario() {
        LocalDate dataAniversario = super.getDataAniversario();
        LocalDate hoje = LocalDate.now();
        if (dataAniversario == null) {return false;}
        if(dataAniversario.getDayOfMonth() == hoje.getDayOfMonth() &&
                dataAniversario.getMonthValue() == hoje.getMonthValue()){
            return true;}
        return false;
    }
}
