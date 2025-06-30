package app;

import classes.OrigemDaTela;
import database.JpaUtil;
import jakarta.persistence.EntityManager;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.text.html.parser.Entity;

public class App extends Application{

    @Override
    public void start(Stage primaryStage) {
        PopularBancoDeDados popularBancoDeDados = new PopularBancoDeDados();
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            popularBancoDeDados.popular(em);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Erro ao popular o banco de dados. Fazendo rollback...");
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            System.out.println("Fechando conexões...");
            em.close();
        }
        primaryStage.setTitle("Restaurant Monsieur-José - Sistema de Gestão");
        TelaFuncionarios tela = new TelaFuncionarios(primaryStage);
        tela.mostrarTela();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}