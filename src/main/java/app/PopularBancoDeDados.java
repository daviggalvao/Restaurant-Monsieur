package app;

import classes.*;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class PopularBancoDeDados {

    public void popular(EntityManager em) {
        // --- Clientes e Funcionários ---
        Cliente cliente = new Cliente("Davi Galvão", LocalDate.of(2025, 5, 2),"SQS 203 Bl.C", "davigalvao@gmail.com", "senha123");
        Cliente cliente2 = new Cliente("Roberto Neto", LocalDate.of(2025, 5, 2),"SQS 203 Bl.C", "robertoneto@gmail.com", "senha123");
        Funcionario funcionario = new Funcionario("Jorge", LocalDate.of(2025, 5, 2), "SQN 406 Bl.A", FuncionarioCargo.CHEF, 4000.0f, LocalDate.now(), "senha123", "jorge@gmail.com");
        Funcionario funcionario2 = new Funcionario("Carlos", LocalDate.of(2025, 5, 2), "SQS 407 Bl.C", FuncionarioCargo.GARCOM, 2500.0f, LocalDate.now(), "senha123", "carlos@gmail.com");

        // --- Ingredientes ---
        Ingrediente ingrediente = new Ingrediente("Puré de Batata trufado", 15.0f, 10, LocalDate.of(2025, 10, 5));
        Ingrediente ingrediente2 = new Ingrediente("Entrecote com molho mostarda", 35.0f, 10, LocalDate.of(2025, 10, 5));
        Ingrediente ingrediente3 = new Ingrediente("Alcatra com molho hollandeise", 40.0f, 10, LocalDate.of(2025, 10, 5));
        Ingrediente ingrediente4 = new Ingrediente("Aspargos", 8.0f, 10, LocalDate.of(2025, 10, 5));
        Ingrediente ingrediente5 = new Ingrediente("Filet mignon com molho de vinho do Porto", 45.0f, 10, LocalDate.of(2025, 10, 5));
        Ingrediente ingrediente6 = new Ingrediente("Lagosta Grelhada com manteiga de ervas finas", 60.0f, 10, LocalDate.of(2025, 10, 5));
        Ingrediente ingrediente7 = new Ingrediente("Ceviche de robalo", 50.0f, 10, LocalDate.of(2025, 10, 5));
        Ingrediente ingrediente8 = new Ingrediente("Pimenta dedo-de-moça", 4.0f, 10, LocalDate.of(2025, 10, 5));
        Ingrediente ingrediente9 = new Ingrediente("Risoto de açafrão", 20.0f, 10, LocalDate.of(2025, 10, 5));
        Ingrediente ingrediente10 = new Ingrediente("Vieiras Salteadas", 43.0f, 10, LocalDate.of(2025, 10, 5));

        // --- Pratos (com listas de ingredientes separadas para maior clareza) ---
        List<Ingrediente> ingredientesMbappe = List.of(ingrediente, ingrediente2);
        Prato prato = new Prato(95.79f, ingredientesMbappe, "Kyllian Mbappé", "Uma explosão de sabor e velocidade no paladar, finalização impecável. Acompanha purê de batatas trufado.", 3);

        List<Ingrediente> ingredientesGriezmann = List.of(ingrediente3, ingrediente4);
        Prato prato2 = new Prato(88.59f, ingredientesGriezmann, "Antoine Griezmann", "Elegância e visão de jogo em cada garfada. Um prato versátil com molho hollandaise e aspargos frescos.", 3);

        List<Ingrediente> ingredientesTchouameni = List.of(ingrediente5);
        Prato prato3 = new Prato(82.99f, ingredientesTchouameni, "Aurélien Tchouaméni", "Solidez e força que protegem o meio-campo do seu apetite. Medalhão de filé mignon ao molho de vinho do Porto.", 3);

        List<Ingrediente> ingredientesHenry = List.of(ingrediente6);
        Prato prato4 = new Prato(99.99f, ingredientesHenry, "Thierry Henry", "Um clássico atemporal com um toque de genialidade. Lagosta grelhada com manteiga de ervas finas.", 3);

        List<Ingrediente> ingredientesDembele = List.of(ingrediente7, ingrediente8);
        Prato prato5 = new Prato(78.78f, ingredientesDembele, "Ousmane Dembélé", "Dribles desconcertantes de sabores cítricos e picantes. Ceviche de robalo com maracujá e pimenta dedo-de-moça.", 3);

        List<Ingrediente> ingredientesPayet = List.of(ingrediente9, ingrediente10);
        Prato prato6 = new Prato(85.49f, ingredientesPayet, "Dimitri Payet", "Precisão e criatividade em um prato que encanta. Risoto de açafrão com vieiras salteadas e um toque de magia.", 3);

        em.persist(cliente);
        em.persist(cliente2);
        em.persist(funcionario);
        em.persist(funcionario2);
        em.persist(ingrediente);
        em.persist(ingrediente2);
        em.persist(ingrediente3);
        em.persist(ingrediente4);
        em.persist(ingrediente5);
        em.persist(ingrediente6);
        em.persist(ingrediente7);
        em.persist(ingrediente8);
        em.persist(ingrediente9);
        em.persist(ingrediente10);
        em.persist(prato);
        em.persist(prato2);
        em.persist(prato3);
        em.persist(prato4);
        em.persist(prato5);
        em.persist(prato6);
    }
}