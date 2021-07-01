package com.example.reportterm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {


    BookDatabase database;
    BookInfo bookInfos;
    ListView listView;


    TextView nameTextView;
    TextView anthorTextView;
    TextView contentsTextView;
    ImageView imageView;

    String buffer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        nameTextView = (TextView)findViewById(R.id.textViewname);
        anthorTextView = (TextView)findViewById(R.id.textViewanthor);
        contentsTextView = (TextView)findViewById(R.id.textViewcontents);
        imageView = (ImageView)findViewById(R.id.imageView2) ;
        Button button1 = (Button)findViewById(R.id.editbutton);
        Button button2 = (Button)findViewById(R.id.deletebutton);

        if (database != null){
            database.close();
            database = null;
        }


        database = BookDatabase.getInstance(this);
        boolean isOpen = database.open();


        //viewBookInfo();


        Intent intent = getIntent();
        buffer = intent.getStringExtra("name");


        bookInfos = database.selectRecord(buffer);



        nameTextView.setText(bookInfos.getName());
        anthorTextView.setText(bookInfos.getAuthor());
        contentsTextView.setText(bookInfos.getContents());
        imageView.setImageResource(R.drawable.study);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                intent.putExtra("name",buffer);
                //intent.putExtra("image",item.getResId());
                startActivity(intent);
                finish();
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.deleteRecord(buffer);
                Intent intent = new Intent(getApplicationContext(), InquiryActivity.class);
                //intent.putExtra("image",item.getResId());
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








