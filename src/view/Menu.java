package view;
/*
@date 02.11.2023
@author Sergey Bugaienko
*/

import model.Book;
import model.Reader;
import service.LibraryService;
import util.MyList;


import java.util.Optional;
import java.util.Scanner;

public class Menu {
    private final LibraryService service;
    private final Scanner SCANNER = new Scanner(System.in);

    public Menu(LibraryService service) {
        this.service = service;
    }

    public void run() {
        showMenu();
    }

    private void showMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Добро пожаловать в библиотеку");
            System.out.println("=========== v 1.0 ===========");
            System.out.println("1. Меню книг");
            System.out.println("2. Меню пользователей");
            System.out.println("3. Меню администратора");
            System.out.println("0. Выход");
            System.out.println("\nСделайте выбор:");
            int choice = SCANNER.nextInt();
            SCANNER.nextLine();
            if (choice == 0) {
                System.out.println("До свидания");
                System.exit(0);
                break;
            }
            showSubMenu(choice);

        }
    }

    private void showSubMenu(int choice) {
        switch (choice) {
            case 1:
                showBookMenu();
                break;
            case 2:
                showUsersMenu();
                break;
            case 3:
                showAdminMenu();
                break;
            default:
                System.out.println("Ваш выбор не корректен\n");
        }
    }

    private void showAdminMenu() {
        //TODO
        System.out.println("Меню администратора");
    }

    // ============ BOOK menu

    private void showBookMenu() {
        while (true) {
            System.out.println("Book Menu");
            System.out.println("1. Список всех книг");
            System.out.println("2. Список всех свободных книг");
            System.out.println("3. Список книг по автору");

            if (service.getActiveReader() != null) {
                System.out.println("4. Взять книгу");
                System.out.println("5. Вернуть книгу");
            } else {
                System.out.println("\nВы не авторизованы. Некоторые пункты меню не доступны\n");
            }
            System.out.println("0. Вернуться в предыдущее меню");
            System.out.println("\nСделайте выбор:");
            int choice = SCANNER.nextInt();
            SCANNER.nextLine();
            if (choice == 0) break;
            choiceBookMenuProcessing(choice);
        }
    }

    private void choiceBookMenuProcessing(int choice) {
        switch (choice) {
            case 1:
                MyList<Book> books = service.getAllBooks();
                printBookList(books);
                waitRead();
                break;
            case 2:
                MyList<Book> books1 = service.getAllFreeBooks();
                printBookList(books1);
                waitRead();
                break;
            case 3:
                System.out.println("Введите автора:");
                String authorSearch = SCANNER.nextLine();
                MyList<Book> books2 = service.getBooksByAuthor(authorSearch);
                printBookList(books2);
                waitRead();
                break;
            case 4:
                System.out.println("Взять книгу");
                Reader reader = service.getActiveReader();
                if (reader == null) {
                    System.out.println("Вы должны авторизоваться");

                    break;
                } else {
                    // поиск книги
                    Book myBook = menuChoiceBook();
                    if (myBook != null) {
                        boolean isSuccessesTaken = service.takeBook(myBook);
                        if (isSuccessesTaken) {
                            System.out.printf("Пользователь %s ВЗЯЛ из библиотеки книгу %s", service.getActiveReader().getEmail(), myBook.getTitle());
                        } else {
                            System.out.printf("Книгу %s взять не удалось", myBook);
                        }

                    } else {
                        System.out.println("Не удалось найти книгу");
                    }

                }
                waitRead();
                break;
            case 5:
                System.out.println("Вернуть книгу");
                Reader reader1 = service.getActiveReader();
                if (reader1 == null) {
                    System.out.println("Вы должны авторизоваться");
                    waitRead();
                    break;
                }
                break;
            default:
                System.out.println("Не верный ввод\n");
                waitRead();

        }
    }

    private Book menuChoiceBook() {
        while (true) {
            System.out.println("Меню выбора книги");
            System.out.println("1. Найти книгу по id");
            System.out.println("2. Найти книгу по автору");
            System.out.println("3. Найти книгу по названию");
            System.out.println("0. Охрана, отмена!");

            int choiceMethodSearchBook = SCANNER.nextInt();
            SCANNER.nextLine();

            switch (choiceMethodSearchBook) {
                case 1:
                    Book book = findById();
                    if (book == null) continue;
                    return book;
                case 2:
                    System.out.println("Введите часть или полное имя автора:");
                    String author = SCANNER.nextLine();
                    MyList<Book> books = service.getBooksByAuthor(author);
                    if (!books.isEmpty()) {
                        printBookList(books);
                        Book book1 = findById();
                        if (book1 == null) continue;
                        return book1;
                    } else {
                        System.out.println("Таких книг в библиотеке нет :(");
                        waitRead();
                        break;
                    }

                case 3:
                    System.out.println("Введите часть или полное название книги:");
                    String title = SCANNER.nextLine();
                    MyList<Book> books1 = service.getBooksByTitle(title);
                    if (books1.isEmpty()) {
                        printBookList(books1);
                        Book book2 = findById();
                        if (book2 == null) continue;
                        return book2;
                    } else {
                        System.out.println("Таких книг в библиотеке нет :(");
                        waitRead();
                        break;
                    }


                case 0:
                    return null;
                default:
                    System.out.println("Выбор не распознан!");
            }
        }
    }

    private Book findById() {
        System.out.println("Введите id книги:");
        int findId = SCANNER.nextInt();
        SCANNER.nextLine();
        Optional<Book> bookOptional = service.getBookById(findId);
        if (bookOptional.isEmpty()) {
            System.out.printf("Книга с %d не найдена\n", findId);
            waitRead();
            return null;
        }
        Book book = bookOptional.get();
        System.out.println("Найдена книга: " + book);
        return book;
    }

    // ============ User menu
    private void showUsersMenu() {
        while (true) {
            System.out.println("Меню пользователей");
            System.out.println("1 -> Авторизация в системе");
            System.out.println("2 -> Регистрация нового пользователя");
            System.out.println("3 -> Logout");
            System.out.println("4 -> Список всех пользователей");
            System.out.println("0 -> Возврат в предыдущее меню");

            //запрашиваем выбор пользователя
            System.out.println("\nСделайте выбор пункта:");
            int input = SCANNER.nextInt();
            SCANNER.nextLine();

            if (input == 0) break;
            handleUserMenuChoice(input);
        }
    }

    private void handleUserMenuChoice(int input) {
        switch (input) {
            case 1:
                //Авторизация
                System.out.println("Авторизация нового пользователя\n");
                System.out.println("Введите ваш email:");
                String email = SCANNER.nextLine();

                System.out.println("Введи Ваш пароль:");
                String password = SCANNER.nextLine();

                Reader reader = service.authorizationReader(email, password);

                if (reader == null) {
                    System.out.println("Не верный email или password");
                } else {
                    System.out.println("Вы успешно авторизовались в системе!");
                }
                waitRead();
                break;
            case 2:
                //Регистрация
                System.out.println("Регистрация нового пользователя\n");
                System.out.println("Введите ваш email:");
                String emailReg = SCANNER.nextLine();

                System.out.println("Введи Ваш пароль:");
                String passwordReg = SCANNER.nextLine();

                Reader registerUser = service.createReader(emailReg, passwordReg);
                if (registerUser == null) {
                    System.out.println("Вы ввели некорректный email или password");
                } else {
                    System.out.println("Вы успешно зарегистрировались в системе");
                    System.out.println("Для начала работу пройдите авторизацию");
                }
                waitRead();

                break;
            case 3:
                service.logout();
                waitRead();
                break;
            case 4:
                // Вывести список всех пользователей
                MyList<Reader> userList = service.getAllReaders();

                if (!userList.isEmpty()) {
                    for (Reader reader1 : userList.toArray()) {
                        System.out.println(reader1);
                    }
                } else {
                    System.out.println("[]");
                }
                waitRead();
                break;
            default:
                System.out.println("\nНе верный ввод");
                waitRead();
        }
    }


    private void printBookList(MyList<Book> books) {
        for (Book book : books) {
            System.out.printf("%d. %s (%s) | %d\n", book.getId(), book.getTitle(), book.getAuthor(), book.getYear());
        }
        System.out.println();
    }

    private void waitRead() {
        System.out.println("\nДля продолжения нажмите Enter ...");
        SCANNER.nextLine();
    }


}
