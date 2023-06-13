package ru.alishev.springcourse.models;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "book")
public class Book {
  @Id
  @Column(name = "book_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int bookId;

  @NotEmpty(message = "Title should not be empty")
  @Size(min = 1, max = 200, message = "Title should be between 1 and 200 characters")
  @Column(name = "title")
  private String title;

  @NotEmpty(message = "Author should not be empty")
  @Size(min = 1, max = 200, message = "Author should be between 1 and 200 characters")
  @Column(name = "author")
  private String author;

  @Min(value = 1900, message = "Year of berth can't be early than 1900")
  @Column(name = "year_of_publish")
  private int yearOfPublish;

  @Column(name = "taken_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date takenAt;

  @ManyToOne()
  @JoinColumn(name = "person_id", referencedColumnName = "person_id")
  private Person owner;

  @Transient
  private boolean expired;

  public Book() {

  }

  public Book(int bookId, String title, String author, int yearOfPublish) {
    this.bookId = bookId;
    this.title = title;
    this.author = author;
    this.yearOfPublish = yearOfPublish;
  }

  public Person getOwner() {
    return this.owner;
  }

  public void setOwner(Person owner) {
    this.owner = owner;
  }

  public int getBookId() {
    return this.bookId;
  }

  public void setBookId(int bookId) {
    this.bookId = bookId;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return this.author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public int getYearOfPublish() {
    return this.yearOfPublish;
  }

  public void setYearOfPublish(int yearOfPublish) {
    this.yearOfPublish = yearOfPublish;
  }

  public Date getTakenAt() {
    return takenAt;
  }

  public void setTakenAt(Date takenAt) {
    this.takenAt = takenAt;
  }

  public boolean isExpired() {
    return expired;
  }

  public void setExpired(boolean expired) {
    this.expired = expired;
  }
}