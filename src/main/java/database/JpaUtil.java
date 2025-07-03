package database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

    public class JpaUtil {

        private static final EntityManagerFactory FACTORY =
                Persistence.createEntityManagerFactory("mysql-jpa");

        public static EntityManagerFactory getFactory() {
            return FACTORY;
        }

        public static void close() {
            if (FACTORY != null && FACTORY.isOpen()) {
                FACTORY.close();
        }
    }
}
