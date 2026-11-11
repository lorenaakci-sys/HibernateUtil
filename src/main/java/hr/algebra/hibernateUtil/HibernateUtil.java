package hr.algebra.hibernateUtil;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(Product.class);
        return configuration.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

    public enum TransactionIsolationLevel {
        READ_UNCOMMITTED(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED),
        READ_COMMITTED(java.sql.Connection.TRANSACTION_READ_COMMITTED),
        REPEATABLE_READ(java.sql.Connection.TRANSACTION_REPEATABLE_READ),
        SERIALIZABLE(java.sql.Connection.TRANSACTION_SERIALIZABLE);
        private final int level;
        TransactionIsolationLevel(int level) {
            this.level = level;
        }
        public int getLevel() {
            return level;
        }
    }
}
