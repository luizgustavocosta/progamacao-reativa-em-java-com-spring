package org.hibernate.reactive.example;

import io.smallrye.mutiny.groups.UniAwaitOptional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static java.lang.System.out;
import static java.time.Month.*;
import static javax.persistence.Persistence.createEntityManagerFactory;
import static org.hibernate.reactive.mutiny.Mutiny.SessionFactory;

/**
 * Demonstrates the use of Hibernate Reactive with the
 * {@link io.smallrye.mutiny.Uni Mutiny}-based API.
 * <p>
 * Here we use stateless sessions and handwritten SQL.
 * Unis are lazy. The operation is only triggered once there is a subscription.
 */
public class MutinyExample {

    // The first argument can be used to select a persistenceUnit.
    // Check resources/META-INF/persistence.xml for available names.
    public static void main(String[] args) {
        out.println("== Mutiny API Example ==");

        // obtain a factory for reactive sessions based on the
        // standard JPA configuration properties specified in
        // resources/META-INF/persistence.xml
        SessionFactory factory =
                createEntityManagerFactory(persistenceUnitName(args))
                        .unwrap(SessionFactory.class);

        // define some test data
        Author author1 = new Author("Iain M. Banks");
        Author author2 = new Author("Neal Stephenson");
        Book book1 = new Book("1-85723-235-6", "Feersum Endjinn", author1, LocalDate.of(1994, JANUARY, 1));
        Book book2 = new Book("0-380-97346-4", "Cryptonomicon", author2, LocalDate.of(1999, MAY, 1));
        Book book3 = new Book("0-553-08853-X", "Snow Crash", author2, LocalDate.of(1992, JUNE, 1));
        author1.getBooks().add(book1);
        author2.getBooks().add(book2);
        author2.getBooks().add(book3);

        try {
            // obtain a reactive session
            factory.withStatelessSession(
                    // persist the Authors with their Books in a transaction
                    session -> session.withTransaction(
                            tx -> session.insertAll(author1, author2, book1, book2, book3)
                    )
            )
            // wait for it to finish
            .await().indefinitely();

            factory.withStatelessSession(
                            // retrieve a Book
                            session -> session.get(Book.class, book1.getId())
                                    // print its title
                                    .invoke(book -> out.println(book.getTitle() + " is a great book! But won't be printed"))
                    )
                    .await().atMost(Duration.ofSeconds(5));

            UniAwaitOptional<List<Author>> listUniAwaitOptional = factory.withSession(
                            // retrieve both Authors at once
                            session -> session.find(Author.class, author1.getId(), author2.getId())
                                    .invoke(authors -> authors.forEach(author -> out.println(author.getName())))
                    )
                    .await().asOptional();

            listUniAwaitOptional.atMost(Duration.ofSeconds(5))
                    .stream()
                    .flatMap(List::stream)
                    .forEach(author -> out.println("Bring by optional the author name ["+author.getName()+"]"));

            factory.withStatelessSession(
                            // retrieve an Author
                            session -> session.get(Author.class, author2.getId())
                                    // lazily fetch their books
                                    .chain(author -> session.fetch(author.getBooks())
                                            // print some info
                                            .invoke(books -> {
                                                out.println(author.getName() + " wrote " + books.size() + " books");
                                                books.forEach(book -> out.println(book.getTitle()));
                                            })
                                    )
                    )
                    .await().indefinitely();

            factory.withStatelessSession(
                            // query the Book titles
                            session -> session.createNativeQuery(
                                            "select book.title, author.name from books book join authors author on book.author_id = author.id order by book.title desc",
                                            Object[].class
                                    )
                                    .getResultList()
                                    .invoke(rows -> rows.forEach(
                                            row -> out.printf("%s (%s)%n", row[0], row[1])
                                    ))
                    )
                    .await().indefinitely();

            factory.withStatelessSession(
                            // query the entire Book entities
                            session -> session.createNativeQuery(
                                            "select * from books order by title desc",
                                            Book.class
                                    )
                                    .getResultList()
                                    .invoke(books -> books.forEach(
                                            book -> out.printf(
                                                    "%s: %s%n",
                                                    book.getIsbn(),
                                                    book.getTitle()
                                            )
                                    ))
                    )
                    .await().indefinitely();

            factory.withStatelessSession(
                            session -> session.withTransaction(
                                    // delete a detached Book
                                    tx -> session.delete(book2)
                            )
                    )
                    .await().indefinitely();

            factory.withStatelessSession(
                            session -> session.withTransaction(
                                    // delete all the Books
                                    tx -> session.createNativeQuery("delete from books").executeUpdate()
                                            //delete all the Authors
                                            .chain(() -> session.createNativeQuery("delete from authors").executeUpdate())
                            )
                    )
                    .await().indefinitely();

        } catch (Exception exception) {
            exception.printStackTrace();
            out.println("Houston, we've a problem [" + exception.getMessage() + "]");
        } finally {
            // remember to shut down the factory
            factory.close();
        }
    }

    /**
     * Return the persistence unit name to use in the example.
     *
     * @param args the first element is the persistence unit name if present
     * @return the selected persistence unit name or the default one
     */
    public static String persistenceUnitName(String[] args) {
        return args.length > 0 ? args[0] : "postgresql-example";
    }
}
