package inerfaces;
/*
@date 28.10.2023
@author Sergey Bugaienko
*/

import model.Book;
import model.Reader;
import util.MyList;

import java.time.LocalDate;
import java.util.function.Predicate;

public interface InterfaceLibrary {

    boolean takeBook(int id);
    boolean releaseBook(int id);

    Book createNewBook(String title, String author, int year);
    MyList<Book> findBookByTitle(String title);

    MyList<Book> findBooksByPartTitle(String partTitle);

    MyList<Book> getAllFreeBooks();
    MyList<Book> getAllBusyBook();

    Book getBookById(int id);

    MyList<Book> getAllBooks();


    MyList<Book> getBooksByAuthor(String author);
    MyList<Book> getBooksActiveReader();
    MyList<Book> getBooksByReader(String readerName);
    MyList<Book> getBooksByStatus(boolean status);

    Reader getActiveReader();

    MyList<Reader> getAllReaders();
    Reader authorizationReader(String name, String password);
    void logout(); //**

    Reader createReader(String name, String password); //**
    Reader getReaderByBook(int bookId); //** TODO Test
    LocalDate getDateTaken(int id);
    long getBusyDaysForBook(int id);



}
