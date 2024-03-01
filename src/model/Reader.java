package model;
/*
@date 01.03.2024
@author Sergey Bugaienko
*/

import util.MyArrayList;
import util.MyList;

import java.time.LocalDate;
import java.util.Objects;

public class Reader {
    private String email;
    private String password;
    private final MyList<Book> readerBooks;
    private Role role = Role.READER;
    private LocalDate createAt; // должно быть финальным. Но для тестирования данных добавим геттер

//    public Reader(String name) {
//        this.name = name;
//        this.readerBooks = new MyArrayList<>();
//        createAt = LocalDate.now();
//    }

    public Reader(String email, String password) {
        this.email = email;
        this.password = password;
        this.readerBooks = new MyArrayList<>();
        createAt = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Reader{" +
                "name='" + email + '\'' +
                ", readerBooks=" + readerBooks +
                ", role=" + role +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MyList<Book> getReaderBooks() {
        return readerBooks;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reader reader = (Reader) o;

        if (!Objects.equals(email, reader.email)) return false;
        if (!Objects.equals(password, reader.password)) return false;
        return Objects.equals(createAt, reader.createAt);
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
        return result;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
