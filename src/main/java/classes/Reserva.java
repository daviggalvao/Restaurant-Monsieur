import java.time.LocalDate;
import java.time.DayOfWeek;

    public class Reserva{
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

        public String getData(){return this.data}
        public String getHorario(){return this.horario}
        public String getChofer(){return this.chofer}
        public Cliente getCliente(){return this.cliente}
        public String getQuantidadePessoas(){return this.qtdPessoas}
        public String getPagamento(){return this.pagamento}


        public void setData(String data){this.data = data;}
        public void setHorario(String horario){this.horario = horario;}
        public void setChofer(boolean chofer){this.chofer = chofer}
        public void setCliente(Cliente cliente){this.cliente = cliente;}
        public void setQuantidadePessoas(int qtdPessoas){this.qtdPessoas = qtdPessoas;}
        public void setPagamento(Pagamento pagamento){this.pagamento = pagamento;}

        public boolean ehDiaSemana(){
            int dia,mes,ano;
            String[] Partes = this.data.split("/")
            dia = Integer.parseInt(Partes[0]);
            mes = Integer.parseInt(Partes[1]);
            ano = Integer.parseInt(Partes[2]);
            LocalDate data = LocalDate.of(ano,mes,dia);
            data.getDayOfWeek().getValue() >=6 ? return true : return false;
        }

        public void ehMuitaGente(){
            this.qtdPessoas >= 10 ? this.pagamento.setPreco(this.qtdPessoas) 
            : this.pagamento.setPreco(this.qtdPessoas*2);
        }

        public boolean querChofer(){
            if (!this.chofer) return false;
            this.pagamento.setPreco(this.pagamento.getPreco() + 10);
            return true;
        }
    }