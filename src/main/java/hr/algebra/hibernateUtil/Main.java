package hr.algebra.hibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();

            Product product = new Product();
            product.setUsername("Laptop");
            product.setPrice(new BigDecimal("100.00"));

            session.persist(product);

            transaction.commit();
        }

        testIsolationLevel();

        HibernateUtil.shutdown();
    }

    public static void testIsolationLevel() {

        for (HibernateUtil.TransactionIsolationLevel isolationLevel :
                HibernateUtil.TransactionIsolationLevel.values()) {

            System.out.println("Testing: " + isolationLevel);

            try (Session session =
                         HibernateUtil.getSessionFactory().openSession()) {

                Transaction transaction = session.beginTransaction();

                session.doWork(connection -> {
                    try {
                        connection.setTransactionIsolation(
                                isolationLevel.getLevel()
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                Product product = session.get(Product.class, 1L);

                if (product != null) {

                    System.out.println("Before update: " + product.getPrice());

                    Thread.sleep(5000);

                    product.setPrice(product.getPrice().add(
                            new java.math.BigDecimal("10.00")
                    ));

                    transaction.commit();

                    System.out.println("After update: " + product.getPrice());

                } else {
                    System.out.println("Product not found!");
                    transaction.rollback();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
