package repository;
/*
@date 28.10.2023
@author Sergey Bugaienko
*/

import inerfaces.InterfaceReaderRepository;
import model.Book;
import model.Reader;
import model.Role;
import util.MyArrayList;
import util.MyList;

import java.time.LocalDate;

public class ReaderRepository implements InterfaceReaderRepository {
    private final MyList<Reader> readers;

    public ReaderRepository() {
        this.readers = new MyArrayList<>();
        init();
    }

    private void init() {

        Reader reader = new Reader("User1", "Password1");
        reader.setCreateAt(LocalDate.of(2020, 1, 25));
        reader.setRole(Role.ADMIN);

        Reader reader1 = new Reader("User2", "Password2");
        reader1.setCreateAt(LocalDate.of(2001, 12, 25));
        reader1.setRole(Role.LIBRARIAN);

        Reader reader2 = new Reader("User3", "Password3");
        reader2.setCreateAt(LocalDate.of(2018, 2, 1));

        Reader reader3 = new Reader("User4", "Password4");
        reader3.setCreateAt(LocalDate.of(2023, 10, 5));

        Reader reader4 = new Reader("User5", "Password5");
        reader4.setCreateAt(LocalDate.of(2020, 10, 25));

        Reader reader5 = new Reader("User6", "Password6");
        readers.addAll(reader, reader1, reader2, reader3, reader4, reader5);
    }


    @Override
    public MyList<Reader> getAllReaders() {
        return readers;
    }

    @Override
    public Reader getReaderByName(String name) {
        for (Reader reader : readers) {
            if (reader.getName().equals(name)) {
                return reader;
            }
        }
        return null;
    }


    @Override
    public boolean isReaderNameExist(String name) {
        for (Reader reader : readers) {
            if (reader.getName().equals(name)) return true;
        }
        return false;
    }

    @Override
    public void save(Reader reader) {
        readers.add(reader);
    }

    @Override
    public void takeBook(Book book, Reader reader) {
        book.setReader(reader);
        reader.getReaderBooks().add(book);
    }

    @Override
    public void releaseBook(Book book, Reader reader) {
        book.setReader(null);
        reader.getReaderBooks().remove(book);
    }
}
