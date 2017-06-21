/*CREATE TABLE hibernate_gradle_developers(
        ID INT NOT NULL AUTO_INCREMENT,
        FIRST_NAME VARCHAR(50) DEFAULT NULL,
        LAST_NAME VARCHAR(50) DEFAULT NULL,
        SPECIALTY VARCHAR(50) DEFAULT NULL,
        EXPERIENCE INT DEFAULT NULL,
        PRIMARY KEY(ID)
        );*/
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;
import java.util.List;

public class Main {
    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        sessionFactory = new Configuration().configure().buildSessionFactory();

        Main m = new Main();

        System.out.println("Adding developer's records to the DB");
        m.addDeveloper("Vlad", "Sever", "Java Developer", 1);
        m.addDeveloper("John", "Doe", "Java Developer", 0);
        m.addDeveloper("Ross", "Geller", "C++ Developer", 100);
        m.addDeveloper("Max", "Rand", "C# Developer", 101);
        m.listDevelopers().forEach(System.out::println);
        System.out.println("=================================");

        System.out.println("Updating Vlad's records to the DB");
        m.updateDeveloper(1, 2);
        m.listDevelopers().forEach(System.out::println);
        System.out.println("=================================");

        System.out.println("Removing developer's records from the DB");
        m.removeDeveloper(2);
        m.listDevelopers().forEach(System.out::println);
        System.out.println("=================================");

        System.out.println("Removing developer's records from the DB by name");
        m.removeDeveloperByName("Max");
        m.listDevelopers().forEach(System.out::println);
        System.out.println("=================================");

        System.out.println("Experienced developers ( >5 years ):");
        m.listOfExperiencedDevelopers(5).forEach(System.out::println);
        System.out.println("=================================");

        sessionFactory.close();
    }

    public List<Developer> listOfExperiencedDevelopers(int experience) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("FROM Developer WHERE experience > :experience");
        query.setParameter("experience", experience);
        List<Developer> list = query.getResultList();

        transaction.commit();
        session.close();
        return list;
    }

    public void addDeveloper(String firstName, String lastName, String specialty, int experience) {
        Session session = sessionFactory.openSession();
        Transaction transaction;

        transaction = session.beginTransaction();
        Developer developer = new Developer(firstName, lastName, specialty, experience);
        session.save(developer);
        transaction.commit();
        session.close();
    }

    public List<Developer> listDevelopers() {
        Session session = sessionFactory.openSession();
        Transaction transaction;

        transaction = session.beginTransaction();
        List<Developer> developers = session.createQuery("FROM Developer").list();

        transaction.commit();
        session.close();
        return developers;
    }

    public void updateDeveloper(int developerId, int experience) {
        Session session = sessionFactory.openSession();
        Transaction transaction;

        transaction = session.beginTransaction();
        Developer developer = session.get(Developer.class, developerId);
        developer.setExperience(experience);
        session.update(developer);
        transaction.commit();
        session.close();
    }

    public void removeDeveloper(int developerId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Developer developer = session.get(Developer.class, developerId);
        session.delete(developer);

        transaction.commit();
        session.close();
    }

    public void removeDeveloperByName(String name) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("DELETE FROM Developer WHERE firstName = :name");
        query.setParameter("name", name);
        query.executeUpdate();

        transaction.commit();
        session.close();
    }

}
