package com.example.providerTest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ipctest.Book;
import com.example.ipctest.R;

public class ProviderActivity extends AppCompatActivity {
    private String TAG = "qiruimin_ProviderActivity";
    private String providerAuthority = "com.ipctest.book";
    public static final String EXTRA_RESULT_CODE = "result_code";
    private static final String METHOD_TEST1 = "method_test1";
    ScrollView mScrollView;
    TextView mPrintTextView;
    Button mButton;
    private Uri bookUri = Uri.parse("content://com.ipctest.book/book");

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
//        mScrollView = this.findViewById(R.id.scrollView);
//        mPrintTextView = findViewById(R.id.print);
//        mButton = findViewById(R.id.button4);
//        mButton.setOnClickListener(v -> setmPrintTextView());
        Log.e("qiruimin", "ProviderActivity onCreate: ");
        Bundle callResult = getApplicationContext().getContentResolver()
                .call(providerAuthority, METHOD_TEST1, null, new Bundle());
        Log.e(TAG, "callResult: "+callResult.getInt(EXTRA_RESULT_CODE) );
//        ContentValues values = new ContentValues();
//        values.put("_id", 6);
//        values.put("name", "程序设计的艺术");
//        getContentResolver().insert(bookUri, values);


    }

    private void setmPrintTextView() {
        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
        while (bookCursor.moveToNext()) {
            Book book = new Book();
            book.bookId = bookCursor.getInt(0);
            book.bookName = bookCursor.getString(1);
            mPrintTextView.setText(book.toString());
            mPrintTextView.postDelayed(() -> mScrollView.fullScroll(ScrollView.FOCUS_DOWN), 3);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "query book: " + book.toString());
        }
//        mPrintTextView.setText("设置完成！！！！！！");
        bookCursor.close();
    }
}