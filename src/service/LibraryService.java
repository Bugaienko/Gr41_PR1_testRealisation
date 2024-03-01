package service;
/*
@date 28.10.2023
@author Sergey Bugaienko
*/

import inerfaces.InterfaceLibrary;
import model.Book;
import model.Reader;
import model.Role;
import repository.BookRepository;
import repository.ReaderRepository;

import util.MyArrayList;
import util.MyList;
import validators.UserValidator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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

        Optional<Book> optionalBook = bookRepository.getBookById(4);
        Book book = null;
        if (optionalBook.isPresent()) {
            book = optionalBook.get();
        } else {
            return;
        }
        Reader reader = readerRepository.getReaderByEmail("User3");
        bookRepository.takeBook(book);
        readerRepository.takeBook(book, reader);
        book.setDateTaken(LocalDate.of(2023, 8, 1));

//        book = bookRepository.getBookByTitle("Чистый код. Создание, анализ и рефакторинг");
        if (bookRepository.getBookById(7).isPresent()) {
            book = bookRepository.getBookById(7).get();
        }
        reader = readerRepository.getReaderByEmail("User3");
        bookRepository.takeBook(book);
        readerRepository.takeBook(book, reader);
        book.setDateTaken(LocalDate.of(2023, 1, 15));

//        book = bookRepository.getBookByTitle("Разработка через тестирование. Практика TDD и приемочного TDD для Java-разработчиков");
        if (bookRepository.getBookById(8).isPresent()) {
            book = bookRepository.getBookById(8).get();
        }
        reader = readerRepository.getReaderByEmail("User6");
        bookRepository.takeBook(book);
        readerRepository.takeBook(book, reader);
        book.setDateTaken(LocalDate.of(2023, 9, 13));

    }

    @Override
    public long getBusyDaysForBook(int id) {
        Optional<Book> optional = bookRepository.getBookById(id);
        Book book;
        if (optional.isPresent()) {
            book = optional.get();
        } else {
            return -1;
        }
        if (!book.isTaken()) return -1;
        long busyDays = book.getDateTaken().until(LocalDate.now(), ChronoUnit.DAYS);
        return busyDays;

    }

    @Override
    public Optional<Book> getBookById(int id) {
        return bookRepository.getBookById(id);
    }


    @Override
    public MyList<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    @Override
    public MyList<Reader> getAllReaders() {
        if (activeReader == null || activeReader.getRole() != Role.ADMIN) {
            System.err.println("Для выполнения запроса авторизуйтесь с правами администратора");
            return new MyArrayList<>();
        }
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
    public Reader authorizationReader(String email, String password) {
        Reader reader = readerRepository.getReaderByEmail(email);
        if (reader == null) return null;

        if (reader.getPassword().equals(password)) {
            logout();
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
        Optional<Book> optional = bookRepository.getBookById(id);

        if (optional.isEmpty()) {
            System.out.println("Книги с таким id свободна");
            return null;
        } else {
            return optional.get().getReader();
        }
    }

    public MyList<Book> getBooksByTitle(String title) {
        return bookRepository.getBooksByPredicate(book -> book.getTitle().contains(title));
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

        Book book;
        Optional<Book> optional = bookRepository.getBookById(id);
        if (optional.isEmpty()) {
            System.out.println("Книга не найдена!");
            return false;
        } else {
            book = optional.get();
        }

        if (book.isTaken()) {
            System.out.println("Книга занята. Сейчас у пользователя: " + book.getReader());
        }
        bookRepository.takeBook(book);
        readerRepository.takeBook(book, activeReader);
        return true;
    }

    public boolean takeBook(Book book) {
        if (activeReader == null) {
            System.out.println("Необходимо авторизовать пользователя");
            return false;
        }


        if (book.isTaken()) {
            System.out.println("Книга занята. Сейчас у пользователя: " + book.getReader());
            return false;
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

        Book book;
        Optional<Book> optional = bookRepository.getBookById(id);
        if (optional.isEmpty()) {
            System.out.println("Книга не найдена!");
            return false;
        } else {
            book = optional.get();
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
        if (title == null || title.isEmpty() || author == null || year < 0) {
            System.err.println("Не корректные параметры");
            return null;
        }
        Book book = null;
        if (!(activeReader == null || activeReader.getRole() != Role.LIBRARIAN)) {
            // Book newBook = new Book(title, author, year);
            if (!bookRepository.isBookExist(title, author, year)) {
                book = bookRepository.addBook(title, author, year);
            }
        } else {
            System.err.println("Вы должны быть авторизированны с правами библиотекаря");
        }
        return book;
    }

    @Override
    public void logout() {
        if (activeReader != null) {
            System.out.printf("Пользователь %s вышел из системы\n", activeReader.getEmail());
        }
        activeReader = null;
    }

    @Override
    public Reader createReader(String email, String password) {
        Reader reader = null;
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            System.out.println("Пустое имя или пароль");
        } else if (readerRepository.isReaderEmailExist(email)) {
            System.out.println("Пользователь с таким именем существует");
        } else {
            if (UserValidator.isEmailValid(email) && UserValidator.isPasswordValid(password)) {
                reader = new Reader(email, password);
                readerRepository.save(reader);
            }
//            else {
//                System.out.println("Email или пароль не соответствуют требованиям");
//            }
        }
        return reader;
    }

    @Override
    public LocalDate getDateTaken(int id) {
        Book book;
        Optional<Book> optional = bookRepository.getBookById(id);
        if (optional.isEmpty()) return null;
        return optional.get().getDateTaken();
    }

    @Override
    public MyList<Book> getBooksActiveReader() {
        if (activeReader == null) return null;
        return activeReader.getReaderBooks();
    }

    @Override
    public MyList<Book> getBooksByReader(String readerName) {
        Reader reader = readerRepository.getReaderByEmail(readerName);
        if (reader == null) return null;
        return reader.getReaderBooks();
    }
}
