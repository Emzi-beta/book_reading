package com.example.reportterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    BookDatabase database;
    BookInfo bookInfos;
    EditText editText1;
    EditText editText2;
    EditText editText3;
    TextView textView1;

    String buffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editText1 = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        //열려있으면 닫아
        if (database != null){
            database.close();
            database = null;
        }

        //데이터베이스 오픈
        database = BookDatabase.getInstance(this);
        boolean isOpen = database.open();

        Intent intent = getIntent();
        buffer = intent.getStringExtra("name");

        bookInfos = database.selectRecord(buffer);

        editText1.setText(bookInfos.getName());
        editText2.setText(bookInfos.getAuthor());
        editText3.setText(bookInfos.getContents());


        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText1.getText().toString();
                String autor = editText2.getText().toString();
                String contents = editText3.getText().toString();

                database.updateRecord(name,autor,contents);


                Intent intent = new Intent(getApplicationContext(), InquiryActivity.class);
                startActivity(intent);
                finish();



            }
        });

    }
    protected void onDestroy(){
        if (database != null){
            database.close();
            database = null;
        }
        super.onDestroy();
    }


}


