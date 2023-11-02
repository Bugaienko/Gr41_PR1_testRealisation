package service;
/*
@date 28.10.2023
@author Sergey Bugaienko
*/

import inerfaces.InterfaceLibrary;
import model.Book;
import model.Reader;
import repository.BookRepository;
import repository.ReaderRepository;
import util.MyList;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LibraryService implements InterfaceLibrary {

    private Reader activeReader;


    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    public LibraryService(BookRepository bookRepository, ReaderRepository repository) {
        this.bookRepository = bookRepository;
        this.readerRepository = repository;
        initData();
    }

    private void initData() {

//        Book book = bookRepository.getBookByTitle("Изучаем Java");
        Book book = bookRepository.getBookById(4);
        Reader reader = readerRepository.getReaderByName("User3");
        bookRepository.takeBook(book);
        readerRepository.takeBook(book, reader);
        book.setDateTaken(LocalDate.of(2023, 8, 1));

//        book = bookRepository.getBookByTitle("Чистый код. Создание, анализ и рефакторинг");
        book = bookRepository.getBookById(7);
        reader = readerRepository.getReaderByName("User3");
        bookRepository.takeBook(book);
        readerRepository.takeBook(book, reader);
        book.setDateTaken(LocalDate.of(2023, 1, 15));

//        book = bookRepository.getBookByTitle("Разработка через тестирование. Практика TDD и приемочного TDD для Java-разработчиков");
        book = bookRepository.getBookById(8);
        reader = readerRepository.getReaderByName("User6");
        bookRepository.takeBook(book);
        readerRepository.takeBook(book, reader);
        book.setDateTaken(LocalDate.of(2023, 9, 13));

    }

    @Override
    public long getBusyDaysForBook(int id){
        Book book = bookRepository.getBookById(id);
        if (book == null || !book.isTaken()) return -1;
        long busyDays = book.getDateTaken().until(LocalDate.now(), ChronoUnit.DAYS);
        return busyDays;

    }

    @Override
    public Book getBookById(int id){
        return bookRepository.getBookById(id);
    }


    @Override
    public MyList<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    @Override
    public MyList<Reader> getAllReaders() {
        return readerRepository.getAllReaders();
    }

    @Override
    public MyList<Book> findBookByTitle(String title) {
        return bookRepository.getBooksByTitle(title);
    }

    @Override
    public MyList<Book> findBooksByPartTitle(String partTitle) {
        return bookRepository.getBooksByPredicate(book -> book.getTitle().contains(partTitle));
    }

    @Override
    public Reader authorizationReader(String name, String password) {
        Reader reader = readerRepository.getReaderByName(name);
        if (reader == null) return null;

        if (reader.getPassword().equals(password)) {
            activeReader = reader;
            return reader;
        }

        return null;
    }

    @Override
    public Reader getActiveReader() {
        return activeReader;
    }

    @Override
    public Reader getReaderByBook(int id) {
        Book book = bookRepository.getBookById(id);
        if (book == null) {
            System.out.println("Книги с таким названием не найдено");
            return null;
        } else {
            return book.getReader();
        }
    }

    @Override
    public MyList<Book> getBooksByAuthor(String author) {
        return bookRepository.getBooksByPredicate(book -> book.getAuthor().contains(author));
    }

    @Override
    public MyList<Book> getAllFreeBooks() {
        return getBooksByStatus(false);
    }

    @Override
    public MyList<Book> getAllBusyBook() {
        return getBooksByStatus(true);
    }

    @Override
    public MyList<Book> getBooksByStatus(boolean status) {
        return bookRepository.getBooksByPredicate(book -> book.isTaken() == status);
    }

    @Override
    public boolean takeBook(int id) {
        if (activeReader == null) {
            System.out.println("Необходимо авторизовать пользователя");
            return false;
        }

        Book book = bookRepository.getBookById(id);
        if (book == null) {
            System.out.println("Книга не найдена!");
            return false;
        }

        if (book.isTaken()) {
            System.out.println("Книга занята. Сейчас у пользователя: " + book.getReader());
        }
        bookRepository.takeBook(book);
        readerRepository.takeBook(book, activeReader);
        return true;
    }

   @Override
   public boolean releaseBook(int id) {
        if (activeReader == null) {
            System.out.println("Необходимо авторизовать пользователя");
            return false;
        }

        Book book = bookRepository.getBookById(id);
        if (book == null) {
            System.out.println("Книга не найдена!");
            return false;
        }

        if (!book.isTaken()) {
            System.out.println("Книга сейчас свободна!");
            return false;
        }

        if (!book.getReader().equals(activeReader)) {
            System.out.println("Эта книга сейчас у другого пользователя");
            return false;
        }

        bookRepository.releaseBook(book);
        readerRepository.releaseBook(book, activeReader);
        return true;
    }


   @Override
   public Book createNewBook(String title, String author, int year) {
        if (title == null || title.isEmpty() || author == null || year < 0) return null;
        Book book = null;
        // Book newBook = new Book(title, author, year);
        if (!bookRepository.isBookExist(title, author, year)) {
           book = bookRepository.addBook(title, author, year);
        }
        return book;
    }

    @Override
    public void logout() {
        activeReader = null;
    }

    @Override
    public Reader createReader(String name, String password) {
        Reader reader = null;
        if (name == null || password == null || name.isEmpty() || password.isEmpty()) {
            System.out.println("Пустое имя или пароль");
        } else if (readerRepository.isReaderNameExist(name)) {
            System.out.println("Пользователь с таким именем существует");
        } else {
            reader = new Reader(name, password);
            readerRepository.save(reader);
        }
        return reader;
    }

    @Override
    public LocalDate getDateTaken(int id) {
        Book book = bookRepository.getBookById(id);
        if (book == null) return null;
        return book.getDateTaken();
    }

    @Override
    public MyList<Book> getBooksActiveReader() {
        if (activeReader == null) return null;
        return activeReader.getReaderBooks();
    }

    @Override
    public MyList<Book> getBooksByReader(String readerName) {
        Reader reader = readerRepository.getReaderByName(readerName);
        if (reader == null) return null;
        return reader.getReaderBooks();
    }
}
