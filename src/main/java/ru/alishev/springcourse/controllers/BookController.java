package ru.alishev.springcourse.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ru.alishev.springcourse.models.Book;
import ru.alishev.springcourse.models.Person;
import ru.alishev.springcourse.services.BookService;
import ru.alishev.springcourse.services.PeopleService;

@Controller
@RequestMapping("/book")
public class BookController {

  private final BookService bookService;
  private final PeopleService peopleService;

  @Autowired
  public BookController(BookService bookService, PeopleService peopleService) {
    this.bookService = bookService;
    this.peopleService = peopleService;
  }

  @GetMapping()
  public String index(
    Model model,
    @RequestParam(value = "page", required = false) Integer page,
    @RequestParam(value = "per_page", required = false) Integer perPage,
    @RequestParam(value = "sort_by_year", required = false, defaultValue = "false") Boolean sortByYear
  ) {

    model.addAttribute("books", bookService.findAll(page, perPage, sortByYear));
    return "books/index";
  }

  @GetMapping("/new")
  public String newBook(@ModelAttribute("book") Book book) {
    return "books/new";
  }

  @GetMapping("/search")
  public String search(
    Model model,
    @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText
  ) {
    Optional<Book> bookOptional = bookService.searchByTitle(searchText);

    if (bookOptional.isPresent()) {
      Book book = bookOptional.get();

      model.addAttribute("book", book);
      model.addAttribute("owner", book.getOwner());
    } else
      model.addAttribute("book", null);

    return "books/search";
  }

  @PostMapping()
  public String create(
      @ModelAttribute("book") @Valid Book book,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors())
      return "books/new";

    bookService.save(book);
    return "redirect:/book";
  }

  @GetMapping("/{id}/edit")
  public String edit(Model model, @PathVariable("id") int id) {
    model.addAttribute("book", bookService.findOne(id));
    return "books/edit";
  }

  @PatchMapping("/{id}")
  public String update(
      @ModelAttribute("book") @Valid Book book,
      BindingResult bindingResult,
      @PathVariable("id") int id) {

    if (bindingResult.hasErrors())
      return "books/edit";

      bookService.update(id, book);
    return "redirect:/book";
  }

  @GetMapping("/{id}")
  public String info(
      Model model,
      @PathVariable("id") int id,
      @ModelAttribute("person") Person person) {

    Book book = bookService.findOne(id);
  
    if (book == null)
      return "books/init";

    Person owner = book.getOwner();

    if (owner != null) {
      model.addAttribute("owner", owner);
    }

    model.addAttribute("people", peopleService.findAll());

    model.addAttribute("book", book);

    return "books/info";
  }

  @DeleteMapping("/{id}/delete")
  public String deleteBook(@PathVariable("id") int bookId) {

    bookService.delete(bookId);

    return "redirect:/book";
  }

  @PatchMapping("/{id}/new-owner")
  public String setNewOwner(
      @PathVariable("id") int bookId,
      @ModelAttribute("person") Person person) {

    bookService.updateOwner(bookId, person);

    return "redirect:/book/" + bookId;
  }

  @PatchMapping("/{id}/delete-owner")
  public String deleteOwner(@PathVariable("id") int bookId) {

    bookService.deleteOwner(bookId);

    return "redirect:/book/" + bookId;
  }
}