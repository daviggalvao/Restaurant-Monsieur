package classes;

import java.time.LocalDate;
import java.time.DayOfWeek;
import classes.Pagamento;

    public class Reserva{
        private String id;
        private String data;
        private String horario;
        private boolean chofer;
        private Cliente cliente;
        private int qtdPessoas;
        private Pagamento pagamento;

        public Reserva(){}
        public Reserva(String data,String horario,
        Cliente cliente,int qtdPessoas,boolean chofer,Pagamento pagamento){
            this.data = data;
            this.horario = horario;
            this.cliente = cliente;
            this.qtdPessoas = qtdPessoas;
            this.pagamento = pagamento;
            this.chofer = chofer;
        }

        public String getId(){return this.id;}
        public String getData(){return this.data;}
        public String getHorario(){return this.horario;}
        public boolean getChofer(){return this.chofer;}
        public Cliente getCliente(){return this.cliente;}
        public int getQuantidadePessoas(){return this.qtdPessoas;}
        public String getPagamento(){return this.pagamento.getTipo();}

        public void setId(String id){this.id = id;}
        public void setData(String data){this.data = data;}
        public void setHorario(String horario){this.horario = horario;}
        public void setChofer(boolean chofer){this.chofer = chofer;}
        public void setCliente(Cliente cliente){this.cliente = cliente;}
        public void setQuantidadePessoas(int qtdPessoas){this.qtdPessoas = qtdPessoas;}
        public void setPagamento(Pagamento pagamento){this.pagamento = pagamento;}

        public boolean ehDiaSemana(){
            int dia,mes,ano;
            String[] Partes = this.data.split("/");
            dia = Integer.parseInt(Partes[0]);
            mes = Integer.parseInt(Partes[1]);
            ano = Integer.parseInt(Partes[2]);
            LocalDate data = LocalDate.of(ano,mes,dia);
            DayOfWeek diaSemana = data.getDayOfWeek();
            return diaSemana.getValue() >=1 && diaSemana.getValue() <=5;
        }

        public void ehMuitaGente(){
            if(this.qtdPessoas > 5){
                this.pagamento.setPreco(this.qtdPessoas);
            }else{ 
                this.pagamento.setPreco(this.qtdPessoas*2);
            }
        }

        public boolean querChofer(){
            if (!this.chofer) return false;
            this.pagamento.setPreco(this.pagamento.getPreco() + 10);
            return true;
        }
    }