/*
@date 28.10.2023
@author Sergey Bugaienko
*/

import repository.BookRepository;
import repository.ReaderRepository;
import service.LibraryService;
import view.Menu;

public class LibraryApp {
    public static void main(String[] args) {

        BookRepository bookRepository = new BookRepository();
        ReaderRepository readerRepository = new ReaderRepository();

        LibraryService service = new LibraryService(bookRepository, readerRepository);

        Menu menu = new Menu(service);
        menu.run();

    }


}
