package com.linhuicommon.common;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.linhuicommon.Book;
import com.linhuicommon.IBookManager;
//import com.linhuicommon.IBookManager;

import java.util.ArrayList;
import java.util.List;


public class BookService extends Service {


    private static final String TAG = "BookService";
    public static final List<Book> bookList = new ArrayList<>();
    private BookManagerImp iBookManager;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        buildData();
        iBookManager = new BookManagerImp();

    }

    private void buildData() {

        Book book;
        book = new Book();
        book.setId(100);
        book.setName("封神演义");
        book.setAuthor("许仲琳");
        book.setOrganization("明代");
        bookList.add(book);

        book = new Book();
        book.setId(200);
        book.setName("西游记");
        book.setAuthor("吴承恩");
        book.setOrganization("明代");
        bookList.add(book);

        book = new Book();
        book.setId(300);
        book.setName("红楼梦");
        book.setAuthor("曹雪芹");
        book.setOrganization("清代");
        bookList.add(book);

        book = new Book();
        book.setId(400);
        book.setName("水浒传");
        book.setAuthor("施耐庵");
        book.setOrganization("元");
        bookList.add(book);


    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBookManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    public class BookManagerImp extends IBookManager.Stub {

        @Override
        public List<Book> getBooks() throws RemoteException {
            return bookList;
        }

        @Override
        public int getBooksLenght() throws RemoteException {
            return bookList.size();
        }

        @Override
        public Book createBook(int i, String name) throws RemoteException {
            Book book = new Book();
            book.setId(i);
            book.setName(name);
            boolean f = bookList.add(book);
            if (f) {
                return book;
            } else {
                return null;
            }
        }

        @Override
        public boolean addBook(Book b) throws RemoteException {
            return bookList.add(b);
        }

        @Override
        public boolean deleteBook(Book b) throws RemoteException {
            return bookList.remove(b);
        }

        @Override
        public boolean deleteIndex(int index) throws RemoteException {
            return bookList.remove(index) != null;
        }
    }
}
