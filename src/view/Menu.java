package view;
/*
@date 02.11.2023
@author Sergey Bugaienko
*/

import model.Book;
import model.Reader;
import service.LibraryService;
import util.MyList;

import java.util.Scanner;

public class Menu {
    private final LibraryService service;

    public Menu(LibraryService service) {
        this.service = service;
    }

    public void run(){
        showMenu();
    }

    private void showMenu(){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Добро пожаловать в библиотеку");
            System.out.println("=========== v 1.0 ===========");
            System.out.println("1. Меню книг");
            System.out.println("2. Меню пользователей");
            System.out.println("3. Меню администратора");
            System.out.println("0. Выход");
            System.out.println("\n Сделайте выбор:");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 0) {
                System.out.println("До свидания");
                break;
            }
            showSubMenu(choice, scanner);

        }
    }

    private void showSubMenu(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                showBookMenu(scanner);
                break;
            case 2:
                showUsersMenu(scanner);
                break;
            case 3:
                showAdminMenu(scanner);
                break;
            default:
                System.out.println("Ваш выбор не корректен");
        }
    }

    private void showAdminMenu(Scanner scanner) {
        //TODO
        System.out.println("Меню администратора");
    }

    private void showUsersMenu(Scanner scanner) {
        //TODO
        System.out.println("Меню пользователей");
    }

    private void showBookMenu(Scanner scanner) {
        while (true) {
            System.out.println("Book Menu");
            System.out.println("1. Список всех книг");
            System.out.println("2. Список всех свободных книг");
            System.out.println("3. Список книг по автору");
            System.out.println("4. Взять книгу");
            System.out.println("5. Вернуть книгу");
            System.out.println("0. Вернуться в предыдущее меню");
            System.out.println("\n Сделайте выбор:");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 0) break;
            choiceBookMenuProcessing(choice, scanner);
        }
    }

    private void choiceBookMenuProcessing(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                MyList<Book> books = service.getAllBooks();
                printBookList(books);
                break;
            case 2:
                MyList<Book> books1 = service.getAllFreeBooks();
                printBookList(books1);
                break;
            case 3:
                System.out.println("Введите автора:");
                String authorSearch = scanner.nextLine();
                MyList<Book> books2 = service.getBooksByAuthor(authorSearch);
                printBookList(books2);
                break;
            case 4:
                System.out.println("Взять книгу");
                Reader reader = service.getActiveReader();
                if (reader == null) {
                    System.out.println("Вы должны авторизоваться");
                    break;
                }
                break;
            case 5:
                System.out.println("Вернуть книгу");
                Reader reader1 = service.getActiveReader();
                if (reader1 == null) {
                    System.out.println("Вы должны авторизоваться");
                    break;
                }
                break;
            default:
                System.out.println("Не верный ввод\n");

        }
    }

    private void printBookList(MyList<Book> books) {
        for (Book book: books){
            System.out.printf("%d. %s (%s) | %d\n", book.getId(), book.getTitle(), book.getAuthor(), book.getYear());
        }
        System.out.println();
    }
}
