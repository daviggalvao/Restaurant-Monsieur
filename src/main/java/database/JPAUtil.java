package database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

    public class JPAUtil {

        private static final EntityManagerFactory FACTORY =
                Persistence.createEntityManagerFactory("mysql-jpa"); // Mesmo nome do persistence.xml

        public static EntityManager getEntityManager() {
            return FACTORY.createEntityManager();
        }

        public static void close() {
            FACTORY.close();
        }
    }

