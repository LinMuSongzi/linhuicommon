// IBookManager.aidl
package com.linhuicommon;
import com.linhuicommon.Book;
// Declare any non-default types here with import statements

interface IBookManager {

    List<Book> getBooks();

    int getBooksLenght();

    Book createBook(int i,String name);

    boolean addBook(inout Book b);

    boolean deleteBook(inout Book b);

    boolean deleteIndex(int index);


void test2();

}
