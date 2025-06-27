package classes;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "reservas")
    public class Reserva implements RegraDeCalculo{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        @Column
        private LocalDate data;
        @Column
        private String horario;
        @Column
        private boolean chofer;
        @Column
        private int qtdPessoas;
        @ManyToOne(fetch = FetchType.LAZY) // Muitos (Reservas) para Um (Cliente)
        @JoinColumn(name = "id_cliente", nullable = false) // Define a chave estrangeira
        private Cliente cliente;

        @Embedded
        private Pagamento pagamento;

        public Reserva(){}
        public Reserva(LocalDate data,String horario,
        Cliente cliente,int qtdPessoas,boolean chofer,Pagamento pagamento){
            this.data = data;
            this.horario = horario;
            this.cliente = cliente;
            this.qtdPessoas = qtdPessoas;
            this.pagamento = pagamento;
            this.chofer = chofer;
        }

        public int getId(){return this.id;}
        public LocalDate getData(){return this.data;}
        public String getHorario(){return this.horario;}
        public boolean getChofer(){return this.chofer;}
        public Cliente getCliente(){return this.cliente;}
        public int getQuantidadePessoas(){return this.qtdPessoas;}
        public Pagamento getPagamento(){return this.pagamento;}

        public void setId(int id){this.id = id;}
        public void setData(LocalDate data){this.data = data;}
        public void setHorario(String horario){this.horario = horario;}
        public void setChofer(boolean chofer){this.chofer = chofer;}
        public void setCliente(Cliente cliente){this.cliente = cliente;}
        public void setQuantidadePessoas(int qtdPessoas){this.qtdPessoas = qtdPessoas;}
        public void setPagamento(Pagamento pagamento){this.pagamento = pagamento;}

        public void ehDiaSemana(){
            LocalDate dia = this.getData();
            int valorDia = dia.getDayOfWeek().getValue();
            if(valorDia >= 1 && valorDia <= 5){
                this.pagamento.setPreco(this.pagamento.getPreco()-5);
            }
        }

        @Override
        public void calcular(){
            if(this.qtdPessoas > 5){
                this.pagamento.setPreco(this.pagamento.getPreco()/2);
            }else{
                this.pagamento.setPreco(this.pagamento.getPreco());
            }
        }

        /*public void ehMuitaGente(){
            if(this.qtdPessoas > 5){
                this.pagamento.setPreco(this.pagamento.getPreco()/2);
            }else{ 
                this.pagamento.setPreco(this.pagamento.getPreco());
            }
        }*/

        public void querChofer(){
            if (!this.chofer) return;
            this.pagamento.setPreco(this.pagamento.getPreco() + 10);
        }
    }