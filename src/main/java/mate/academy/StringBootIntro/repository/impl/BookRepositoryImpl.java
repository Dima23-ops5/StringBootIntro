package mate.academy.StringBootIntro.repository.impl;

import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import mate.academy.StringBootIntro.dto.BookDto;
import mate.academy.StringBootIntro.dto.CreateBookRequestDto;
import mate.academy.StringBootIntro.exeption.DataProcessingException;
import mate.academy.StringBootIntro.mapper.BookMapper;
import mate.academy.StringBootIntro.model.Book;
import mate.academy.StringBootIntro.repository.BookRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<Book> criteriaQuery = session.getCriteriaBuilder()
                    .createQuery(Book.class);
            criteriaQuery.from(Book.class);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Cannot find books", e);
        }
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Book.class, id));
        } catch (RuntimeException e) {
            throw new DataProcessingException("Cannot get book with id: " + id, e);
        }
    }

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can`t save book", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return book;
    }
}
