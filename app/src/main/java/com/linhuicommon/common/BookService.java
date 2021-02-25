package com.linhuicommon.common;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.linhuicommon.Book;
import com.linhuicommon.IBookManager;
import com.linhuicommon.R;
//import com.linhuicommon.IBookManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;


public class BookService extends Service {


    private static final String TAG = "BookService";
    public static final List<Book> bookList = new ArrayList<>();
    private BookManagerImp iBookManager;
    private boolean isAdd = false;
    private View mainView;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        buildData();
        iBookManager = new BookManagerImp();
        startForeground(0x123,new Notification());
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

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void test2() throws RemoteException {
//            Intent s = new Intent(BookService.this, SceeActivity.class);
//            s.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(s);
            Log.i(TAG, "test2: ");
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            if(Settings.canDrawOverlays(BookService.this)) {
                Log.i(TAG, "test2: canDrawOverlays true");
                if (isAdd) {
                    windowManager.removeView(mainView());
                } else {
                    windowManager.addView(mainView(), mainView().getLayoutParams());
                }
            }else{
                Log.i(TAG, "test2: canDrawOverlays false");
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    private View mainView() {

        if(mainView == null) {
            mainView = LayoutInflater.from(this).inflate(R.layout.activity_scee,null,false);
            mainView.setLayoutParams(createLayout());
        }

        return mainView;
    }

    private ViewGroup.LayoutParams createLayout() {

        WindowManager.LayoutParams w = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        w.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        w.format = PixelFormat.RGBA_8888;
        w.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        w.gravity = Gravity.LEFT | Gravity.TOP;
        return w;
    }
}
