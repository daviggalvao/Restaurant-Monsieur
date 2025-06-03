package app;

import classes.Ingrediente; // Sua classe Ingrediente
import classes.Prato;       // Sua classe Prato

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List; // Usado para a lista de pratos do menu

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        List<Prato> pratosParaMenu = carregarPratosConformeSuaClasse();

        TelaDelivery telaDelivery = new TelaDelivery(primaryStage, pratosParaMenu);
        telaDelivery.mostrar();
    }

    // Método para carregar/criar os pratos usando o construtor da SUA classe Prato
    // Prato(float preco, ArrayList<Ingrediente> listaIngredientes, String nome, String descricao, int quantidade)
    private List<Prato> carregarPratosConformeSuaClasse() {
        List<Prato> pratosDoMenu = new ArrayList<>();

        // Ingredientes de exemplo (usando o construtor da sua classe Ingrediente)
        // Ingrediente(String nome, float preco, int quantidade, String validade)
        // A 'quantidade' em Ingrediente aqui pode ser interpretada como estoque do ingrediente
        // ou quantidade necessária para UMA unidade do prato. Para a lista de ingredientes de um prato,
        // geralmente não se armazena o estoque do ingrediente DENTRO do prato, mas sim a receita.
        // Vou assumir que a 'quantidade' no construtor de Ingrediente é apenas informativa aqui, ou
        // que você tem um sistema mais complexo de gerenciamento de ingredientes.
        Ingrediente tomate = new Ingrediente("Tomate", 0.5f, 100, "2025-12-31");
        Ingrediente queijo = new Ingrediente("Queijo Mussarela", 2.0f, 50, "2025-11-30");
        Ingrediente frango = new Ingrediente("Frango Desfiado", 5.0f, 30, "2025-10-10");
        Ingrediente alface = new Ingrediente("Alface Crespa", 0.8f, 40, "2025-10-05");
        Ingrediente pao = new Ingrediente("Pão de Forma", 1.0f, 60, "2025-10-08");
        Ingrediente carne = new Ingrediente("Carne Moída", 4.5f, 40, "2025-10-12");

        // Lista de ingredientes para Salada (usando sua classe Ingrediente)
        ArrayList<Ingrediente> ingredientesSalada = new ArrayList<>(Arrays.asList(
                new Ingrediente("Alface Americana", 1.0f, 1, "saco"), // Quantidade do ingrediente
                new Ingrediente("Frango Grelhado Tiras", 3.0f, 100, "gramas"),
                new Ingrediente("Tomate Cereja", 0.5f, 50, "gramas"),
                new Ingrediente("Molho Caesar", 0.8f, 30, "ml")
        ));
        pratosDoMenu.add(new Prato(28.50f, ingredientesSalada, "Salada Caesar Especial",
                "Salada refrescante com frango, tomate cereja e molho caesar.", 20)); // 20 é o estoque deste Prato

        ArrayList<Ingrediente> ingredientesXBurger = new ArrayList<>(Arrays.asList(
                new Ingrediente("Pão de Hambúrguer", 1.5f, 1, "unidade"),
                new Ingrediente("Hambúrguer de Carne", 5.0f, 1, "unidade"),
                new Ingrediente("Queijo Cheddar", 1.0f, 2, "fatias"),
                new Ingrediente("Alface", 0.3f, 2, "folhas")
        ));
        pratosDoMenu.add(new Prato(32.00f, ingredientesXBurger, "X-Burger Clássico",
                "Delicioso hambúrguer com queijo cheddar e alface.", 30));

        ArrayList<Ingrediente> ingredientesPizza = new ArrayList<>(Arrays.asList(
                new Ingrediente("Massa de Pizza", 3.0f, 1, "disco"),
                new Ingrediente("Molho de Tomate", 1.0f, 100, "ml"),
                new Ingrediente("Queijo Mussarela", 2.5f, 150, "gramas"),
                new Ingrediente("Orégano", 0.1f, 5, "gramas")
        ));
        pratosDoMenu.add(new Prato(35.00f, ingredientesPizza, "Pizza Mussarela Individual",
                "Pizza individual com borda recheada de catupiry.", 15));

        // Bebidas (podem ter lista de ingredientes vazia se não aplicável)
        pratosDoMenu.add(new Prato(12.00f, new ArrayList<>(), "Suco de Laranja Natural 500ml",
                "Feito com laranjas frescas selecionadas.", 50));
        pratosDoMenu.add(new Prato(7.00f, new ArrayList<>(), "Refrigerante Lata 350ml",
                "Escolha entre Coca-Cola, Guaraná, Fanta ou Sprite.", 100));

        return pratosDoMenu;
    }

    public static void main(String[] args) {
        launch(args);
    }
}