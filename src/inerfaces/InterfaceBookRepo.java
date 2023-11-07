package inerfaces;
/*
@date 28.10.2023
@author Sergey Bugaienko
*/

import model.Book;
import util.MyList;

import java.util.Optional;
import java.util.function.Predicate;

public interface InterfaceBookRepo<T extends Book> {


    boolean isBookExist(String title, String author, int year);

    //boolean isBookExist(Book book);

    MyList<T> getBooksByTitle(String title);
    Optional<T> getBookById(int id);
    MyList<T> getAllBooks();

    T addBook(String title, String author, int year);

    MyList<T> getBooksByPredicate(Predicate<Book> predicate);
    void takeBook(T book);
    void releaseBook(T book);

    // MyList<T> getAllAvailableBooks(); //** predicate
    // MyList<T> getAllBusyBook(); //**
    // MyList<T> getSortedListBooks(Comparator<T> comparator); // Надо писать сортировку
    // MyList<T> getSortedListBooks(MyList<T> list, Comparator<T> comparator);
    //    MyList<T> getBooksByPartTitle(String partTitle); //**  Predicate
    // void addBook(T book);
}
