package com.example.administrator.android14thclass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    Button btn;
    EditText editDBName;
    EditText editTableName;
    TextView consoleView;
    ScrollView scrollView;

    String databaseName;
    String tableName;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editDBName = (EditText) findViewById(R.id.edit_dbName);
        editTableName = (EditText) findViewById(R.id.edit_tableName);
        consoleView = (TextView) findViewById(R.id.text_console);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        editDBName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    openDatabase();
                    return true;
                }
                return false;
            }
        });
        editTableName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    createTable();
                    return true;
                }
                return false;
            }
        });

        btn = (Button) findViewById(R.id.btn_usingDB);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatabase();
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });
        btn = (Button) findViewById(R.id.btn_createTable);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTable();
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });
        btn = (Button) findViewById(R.id.btn_insert);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });

        btn = (Button) findViewById(R.id.btn_query);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select();
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });
    }

    private void printInConsole(String message) {
        consoleView.append(message + "\n $ ");
    }

    private void openDatabase() {
        databaseName = editDBName.getText().toString();

        try {
            database =  openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
            Log.d("SQLite",  "USING DATABASE " + databaseName);
            printInConsole( " "+databaseName+" 데이터베이스를 사용합니다..." );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        tableName = editTableName.getText().toString();

        try {
            if (database != null) {
                database.execSQL("CREATE TABLE if not exists " + tableName + "("
                        + " _id integer PRIMARY KEY autoincrement, "
                        + " name text, "
                        + " age integer, "
                        + " mobile text "
                        + " ) ");
                Log.d("SQLite", "TABLE CREATED " + tableName);
                printInConsole( " " + tableName + " 테이블을 생성합니다." );
            } else {
                Log.d("SQLite", "DATABASE IS NOT OPEN");
                printInConsole( " 데이터베이스를 먼저 열어야 합니다." );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insert() {
        try {
            if (tableName == null) {
                tableName = editTableName.getText().toString();
            }

            if (databaseName != null) {
                database.execSQL("INSERT INTO " + tableName + "(name, age, mobile) VALUES "
                        + " ('소녀시대', 20 , '010-9600-7673') ");
                printInConsole( " 데이터를 추가했습니다." );
            } else {
                printInConsole( " 데이터베이스를 먼저 열어야 합니다." );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void select() {
        if (tableName == null) {
            tableName = editTableName.getText().toString();
        }
        if (databaseName != null) {
            Cursor cursor = database.rawQuery("SELECT name, age, mobile FROM " + tableName, null);

            int count = cursor.getCount();
            printInConsole( " 결과 레코드의 갯수 : " + count);

            for (int i=0; i<count; i++) {
                cursor.moveToNext();
                String name = cursor.getString(0);
                int age = cursor.getInt(1);
                String mobile = cursor.getString(2);

                printInConsole( " 레코드 # "+ i + "\n   이름: " + name + ", 나이: "+ age + ", 번호: "+ mobile);
            }

            printInConsole( " 데이터를 조회했습니다." );
        } else {
            printInConsole( " 데이터베이스를 먼저 열어야 합니다." );
        }
    }
}
