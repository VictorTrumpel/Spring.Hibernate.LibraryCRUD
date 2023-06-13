package ru.alishev.springcourse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.alishev.springcourse.models.Book;
import ru.alishev.springcourse.models.Person;
import ru.alishev.springcourse.repositories.BookRepository;
import ru.alishev.springcourse.repositories.PeopleRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PeopleRepository peopleRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(Integer page, Integer perPage, Boolean sortByYear) {
        Boolean isPaginate = page != null && perPage != null;

        if (isPaginate && sortByYear)
            return bookRepository
                .findAll(PageRequest.of(page, perPage, Sort.by("yearOfPublish")))
                .getContent();
        
        if (sortByYear)
            return bookRepository.findAll(Sort.by("yearOfPublish"));

        if (isPaginate)
            return bookRepository.findAll(PageRequest.of(page, perPage)).getContent();

        return bookRepository.findAll();
    }

    public Book findOne(int id) {
        Optional<Book> foundPerson = bookRepository.findById(id);
        return foundPerson.orElse(null);
    }

    public Optional<Book> searchByTitle(String searchText) {
        Optional<Book> searchBook = bookRepository.findByTitleStartingWith(searchText);
        return searchBook;
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updateBook) {
        updateBook.setBookId(id);
        bookRepository.save(updateBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void updateOwner(int bookId, Person owner) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setOwner(owner);
            book.setTakenAt(new Date());
        }
    }

    @Transactional
    public void deleteOwner(int bookId) {
        Optional<Book> book = bookRepository.findById(bookId);

        if (book.isPresent()) {
            book.get().setOwner(null);
        }
    }
}
