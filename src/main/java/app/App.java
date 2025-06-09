package app;

import classes.*; // Importa todas as suas classes de modelo

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App extends Application {

    public static List<Pedido> fixed;

    @Override
    public void start(Stage primaryStage) {
        // 1. Cria uma lista de pedidos (deliveries) já concluídos para simulação.
        fixed = carregarPedidosDeExemplo();

        TelaInicial screen = new TelaInicial(primaryStage);
        screen.mostrar();
    }

    /**
     * Cria e retorna uma lista de Pedidos para simular deliveries que já foram feitos.
     * @return Uma lista de objetos Pedido.
     */
    private List<Pedido> carregarPedidosDeExemplo() {
        List<Pedido> pedidos = new ArrayList<>();

        // --- Pratos disponíveis para serem usados nos pedidos ---
        Prato pratoSalada = new Prato(28.50f, new ArrayList<>(), "Salada Caesar Especial", "...", 20);
        Prato pratoXBurger = new Prato(32.00f, new ArrayList<>(), "X-Burger Clássico", "...", 30);
        Prato pratoPizza = new Prato(35.00f, new ArrayList<>(), "Pizza Mussarela Individual", "...", 15);
        Prato pratoSuco = new Prato(12.00f, new ArrayList<>(), "Suco de Laranja", "...", 50);

        // --- Pedido 1 ---
        // CORREÇÃO: Usando o construtor Cliente(nome, dataAniversario, endereco)
        Cliente cliente1 = new Cliente("João Silva", "15/05/1985", "Rua das Flores, 123");
        Pedido pedido1 = new Pedido();
        pedido1.setConsumidor(cliente1);
        pedido1.setPratos(new ArrayList<>(Arrays.asList(pratoXBurger, pratoSuco)));
        pedido1.setQuantidades(new ArrayList<>(Arrays.asList(1, 2))); // 1 X-Burger, 2 Sucos
        Pagamento pagamento1 = new Pagamento();
        pagamento1.setPreco(pedido1.calcularPrecoTotal()); // Usa o método da sua classe Pedido
        pagamento1.setTipo("PIX");
        pedido1.setPagamento(pagamento1);
        pedidos.add(pedido1);

        // --- Pedido 2 ---
        // CORREÇÃO: Usando o construtor Cliente(nome, dataAniversario, endereco)
        Cliente cliente2 = new Cliente("Maria Oliveira", "20/11/1992", "Avenida Principal, 456");
        Pedido pedido2 = new Pedido();
        pedido2.setConsumidor(cliente2);
        pedido2.setPratos(new ArrayList<>(Arrays.asList(pratoPizza, pratoSalada, pratoSuco)));
        pedido2.setQuantidades(new ArrayList<>(Arrays.asList(2, 1, 1))); // 2 Pizzas, 1 Salada, 1 Suco
        Pagamento pagamento2 = new Pagamento();
        pagamento2.setPreco(pedido2.calcularPrecoTotal());
        pagamento2.setTipo("Cartão de Débito");
        pedido2.setPagamento(pagamento2);
        pedidos.add(pedido2);

        // --- Pedido 3 ---
        // CORREÇÃO: Usando o construtor Cliente(nome, dataAniversario, endereco)
        Cliente cliente3 = new Cliente("Carlos Pereira", "08/02/1960", "Travessa dos Sonhos, 789");
        Pedido pedido3 = new Pedido();
        pedido3.setConsumidor(cliente3);
        pedido3.setPratos(new ArrayList<>(Arrays.asList(pratoSalada)));
        pedido3.setQuantidades(new ArrayList<>(Arrays.asList(1))); // 1 Salada
        Pagamento pagamento3 = new Pagamento();
        pagamento3.setPreco(pedido3.calcularPrecoTotal());
        pagamento3.setTipo("Dinheiro");
        pedido3.setPagamento(pagamento3);
        pedidos.add(pedido3);

        return pedidos;
    }

    public static void main(String[] args) {
        launch(args);
    }
}