/*
@date 28.10.2023
@author Sergey Bugaienko
*/

import model.Book;
import model.Reader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import repository.BookRepository;
import repository.ReaderRepository;
import service.LibraryService;
import util.MyList;

import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class TestLibrary {

    private final BookRepository bookRepository = new BookRepository();
    private final ReaderRepository readerRepository = new ReaderRepository();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    LibraryService service = new LibraryService(bookRepository, readerRepository);


    @ParameterizedTest
    @MethodSource("dataTakeValidBook")
    void testTakeValidBook(int id, String titleBook, String userName, String password) {
        MyList<Book> busyBooks = service.getAllBusyBook();
        int sizeBusy = busyBooks.size();
        service.authorizationReader(userName, password);
        boolean isSuccesses = service.takeBook(id);
        busyBooks = service.getAllBusyBook();
        MyList<Book> availableBooks = service.getAllFreeBooks();

//        Book book = null;
//        Optional<Book> optional = service.getBookById(id);
//        if (optional.isPresent()) book = optional.get();

        Book book = service.getBookById(id).orElse(null);
        assertTrue(book.isTaken());
        assertTrue(busyBooks.contains(book));
        assertFalse(availableBooks.contains(book));
        assertEquals(sizeBusy + 1, busyBooks.size());
        assertTrue(isSuccesses);

        // release Book
        boolean isReleased = service.releaseBook(id);
        busyBooks = service.getAllBusyBook();
        availableBooks = service.getAllFreeBooks();
        assertTrue(isReleased);
        assertFalse(busyBooks.contains(book));
        assertTrue(availableBooks.contains(book));
        assertFalse(book.isTaken());

    }

    private static Stream<Arguments> dataTakeValidBook() {
        return Stream.of(
                Arguments.of(1, "Эффективное программирование на Java", "User1", "Password1"),
                Arguments.of(6, "Spring в действии", "User2", "Password2"),
                Arguments.of(3, "Java. Библиотека профессионала. Том 1. Основы", "User4", "Password4")
        );
    }

    @Test
    void testBusyDuration() {
        MyList<Book> books = service.getAllBooks();
        for (Book book : books) {
            long busyDuration = service.getBusyDaysForBook(book.getId());
            System.out.println(book.getTitle() + " (" + book.isTaken() + "): dur: " + busyDuration);
        }
    }

    @ParameterizedTest
    @MethodSource("dataValidReaderCreate")
    void testCreateCorrectReader(String name, String password) {
        service.authorizationReader("User1", "Password1"); // авторизация админа
        //TODO дописать тест на роли

        MyList<Reader> readers = service.getAllReaders();
        int size = readers.size();
        Reader reader = service.createReader(name, password);
        MyList<Reader> newReaders = service.getAllReaders();
        assertTrue(newReaders.contains(reader));
        assertEquals(size + 1, newReaders.size());
    }

    private static Stream<Arguments> dataValidReaderCreate() {
        return Stream.of(
                Arguments.of("UserT", "passwordT"),
                Arguments.of("UserTest", "passwordTest")
        );
    }

    @ParameterizedTest
    @MethodSource("dataInvalidReaderCreate")
    void testCreateInvalidReader(String name, String password) {
        MyList<Reader> readers = service.getAllReaders();
        int size = readers.size();
        Reader reader = service.createReader(name, password);
        MyList<Reader> newReaders = service.getAllReaders();
        assertNull(reader);
        assertEquals(size, newReaders.size());
    }

    private static Stream<Arguments> dataInvalidReaderCreate() {
        return Stream.of(
                Arguments.of("", "passwordT"),
                Arguments.of("UserTest", ""),
                Arguments.of("UserTest", null),
                Arguments.of("User1", "Password1"),
                Arguments.of("User4", "Pass"),
                Arguments.of(null, "Password1")

        );
    }

    @ParameterizedTest
    @MethodSource("dataForTestTakeBooksByAuthor")
    void testPredicate(String author, int count) {
        MyList<Book> booksByAuthor = service.getBooksByAuthor(author);
        assertEquals(count, booksByAuthor.size());
//        System.out.println("Список книг по автору: " + author);
//        for (Book book : booksByAuthor) {
//            System.out.println(book);
//        }
//        System.out.println("=====================\n");
    }

    private static Stream<Arguments> dataForTestTakeBooksByAuthor() {
        return Stream.of(
                Arguments.of("Джошуа Блох", 3),
                Arguments.of("Кей Хорстманн", 1),
                Arguments.of("Роберт Мартин", 2),
                Arguments.of("Нет такого автора", 0)
        );
    }

    @ParameterizedTest
    @MethodSource("dataTestCorrectTitle")
    void testFindBookByCorrectId(int id, String title, String author, int year) {
        Book book = service.getBookById(id).get();
        assertEquals(id, book.getId());
        assertEquals(title, book.getTitle());
        assertEquals(author, book.getAuthor());
        assertEquals(year, book.getYear());
    }

    private static Stream<Arguments> dataTestCorrectTitle() {
        return Stream.of(
                Arguments.of(1, "Эффективное программирование на Java", "Джошуа Блох", 2018),
                Arguments.of(6, "Spring в действии", "Крейг Уоллс", 2018),
                Arguments.of(3, "Java. Библиотека профессионала. Том 1. Основы", "Кей Хорстманн", 2018)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    void testGetById(int id) {
        Book book = service.getBookById(id).orElse(null);
        System.out.println("test get: " + book);
        assertNotNull(book);
    }

    @ParameterizedTest
    @MethodSource("dataTestInvalidTitle")
    void testFindBookByInvalidId(int invalidId) {
        Book book = service.getBookById(invalidId).orElse(null);
        assertNull(book);
    }

    private static Stream<Integer> dataTestInvalidTitle() {
        return Stream.of(
                14,
                -1,
                25,
                55
        );
    }

    @Test
    void testGetAllBooks() {
        MyList<Book> books = service.getAllBooks();
        assertEquals(11, books.size());
    }

    @Test
    void testGetAllReaders() {
        service.authorizationReader("User1", "Password1"); // авторизация админа
        //TODO тест на роли

        MyList<Reader> readers = service.getAllReaders();
        assertEquals(6, readers.size());
//        for (Reader reader : readers) {
//            System.out.print(reader + " | ");
//            System.out.print(reader.getCreateAt().format(formatter));
//            System.out.println();
//        }
    }

    @Test
    void testFindBooksByPartTitle() {
        String partTitle = "Java";
        MyList<Book> books = service.findBooksByPartTitle(partTitle);
        assertEquals(8, books.size());
//        System.out.println(books);
        String partTitle1 = "Test";
        MyList<Book> books1 = service.findBooksByPartTitle(partTitle1);
        assertEquals(0, books1.size());
//        System.out.println(books1);
    }

    @ParameterizedTest
    @MethodSource("dataAddBooks")
    void testAddBook(String title, String author, int year, boolean flag) {
        service.authorizationReader("User2", "Password2"); // авторизация библиотекаря
        //TODO дописать тест проверки на роль
        MyList<Book> books = service.getAllBooks();
        int size = books.size();
        Book newBook = service.createNewBook(title, author, year);
        MyList<Book> newBooks = service.getAllBooks();
        if (flag) {
            assertNotNull(newBook);
            assertEquals(size + 1, newBooks.size());
            assertTrue(newBooks.contains(newBook));

        } else {
            assertNull(newBook);
            assertEquals(books.size(), newBooks.size());
        }
    }

    static Stream<Arguments> dataAddBooks() {
        return Stream.of(
                Arguments.of("Эффективное программирование на Java", "Джошуа Блох", 2018, false),
                Arguments.of("Java 8. Руководство для начинающих", "Герберт Шилдт", 2014, true),
                Arguments.of("Java. Эффективное программирование", "Джошуа Блох", 2009, false),
                Arguments.of("Java Performance. In-Depth Advice for Tuning and Programming Java 8, 11, and Beyond", "Скотт Оакс", 2020, true)
        );
    }


    @ParameterizedTest
    @MethodSource("getUsersAuthData")
    void testAuthorisationReader(String name, String password, boolean flag) {
        Reader reader = service.getActiveReader();
        Reader authReader = service.authorizationReader(name, password);
        Reader active = service.getActiveReader();
        if (flag) {
            assertNotNull(authReader);
            assertEquals(name, active.getEmail());
            assertEquals(password, active.getPassword());
            assertEquals(authReader, active);
        } else {
            assertNull(authReader);
            if (reader == null) {
                assertNull(active, "Active null");
            } else {
                assertEquals(reader, active);
            }
        }
    }

    static Stream<Arguments> getUsersAuthData() {
        return Stream.of(
                Arguments.of("User1", "Password1", true),
                Arguments.of("User1", "Password2", false),
                Arguments.of("User2", "Password2", true),
                Arguments.of("User", "Password1", false)
        );
    }

}
