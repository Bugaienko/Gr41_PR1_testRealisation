package repository;
/*
@date 01.03.2024
@author Sergey Bugaienko
*/

import inerfaces.InterfaceBookRepo;
import model.Book;
import util.MyArrayList;
import util.MyList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class BookRepository implements InterfaceBookRepo<Book> {
    private final AtomicInteger currentId = new AtomicInteger(1);

    private final MyList<Book> books;

    public BookRepository() {
        this.books = new MyArrayList<>();
        init();
    }

    private void init() {

        books.addAll(
                new Book(currentId.getAndIncrement(), "Эффективное программирование на Java", "Джошуа Блох", 2018),
                new Book(currentId.getAndIncrement(), "Java. Полное руководство", "Герберт Шилдт", 2018),
                new Book(currentId.getAndIncrement(), "Java. Библиотека профессионала. Том 1. Основы", "Кей Хорстманн", 2018),
                new Book(currentId.getAndIncrement(), "Изучаем Java", "Кэти Сьерра и Берт Бейтс", 2005),
                new Book(currentId.getAndIncrement(), "Java. Многопоточность в практике", "Брайан Гетц, Тим Пейерлс, Джошуа Блох, Джозеф Баубир, Дуг Лиа и Дэвид Холмс", 2006),
                new Book(currentId.getAndIncrement(), "Spring в действии", "Крейг Уоллс", 2018),
                new Book(currentId.getAndIncrement(), "Чистый код. Создание, анализ и рефакторинг", "Роберт Мартин", 2008),
                new Book(currentId.getAndIncrement(), "Разработка через тестирование. Практика TDD и приемочного TDD для Java-разработчиков", "Лассе Коскела", 2007),
                new Book(currentId.getAndIncrement(), "Java. Эффективное программирование", "Джошуа Блох", 2009),
                new Book(currentId.getAndIncrement(), "Java. Руководство для начинающих", "Герберт Шилдт", 2018),
                new Book(currentId.getAndIncrement(), "Принципы, паттерны и методики гибкой разработки на языке C#", "Роберт Мартин", 2006)
        );
    }

    //@Override
    @Override
    public MyList<Book> getAllBooks() {
        return books;
    }

    @Override
    public MyList<Book> getBooksByTitle(String title) {
        if (title == null || title.isEmpty()) return null;
        MyList<Book> books = new MyArrayList<>();
        for (Book book1 : books) {
            if (book1.getTitle().equals(title)) {
                books.add(book1);
            }
        }
        return books;
    }

    @Override
    public Optional<Book> getBookById(int id) {
        Book book = null;
        for (Book book1 : books){
            if (book1.getId() == id) {
                book = book1;
                break;
            }
        }

        return Optional.ofNullable(book);

        //return books.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    @Override
    public boolean isBookExist(String title, String author, int year) {
        for (Book book : books) {
            if (book.getTitle().equals(title) && book.getAuthor().equals(author)
                    && book.getYear() == year) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Book addBook(String title, String author, int year) {
        Book book = new Book(currentId.getAndIncrement(), title, author, year);
        books.add(book);
        return book;
    }

    @Override
    public MyList<Book> getBooksByPredicate(Predicate<Book> predicate) {
        MyList<Book> result = new MyArrayList<>();
        for (Book book : books) {
            if (predicate.test(book)) {
                result.add(book);
            }
        }

        return result;
    }

    @Override
    public void takeBook(Book book) {
        book.setDateTaken(LocalDate.now());
        book.setTaken(true);
    }

    @Override
    public void releaseBook(Book book) {
        book.setDateReturn(LocalDate.now());
        book.setTaken(false);
    }

    public MyList<Book> getSortedListBooks(Comparator<Book> comparator) {
        List<Book> sortedBooks = new ArrayList<>();
        for (Book book : books) {
            sortedBooks.add(book);
        }
        sortedBooks.sort(comparator);
        MyList<Book> result = new MyArrayList<>();
        for (Book book : sortedBooks) {
            result.add(book);
        }
        return result;
    }


}
