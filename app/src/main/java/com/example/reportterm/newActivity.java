package com.example.reportterm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class newActivity extends AppCompatActivity {

    BookDatabase database;
    ArrayList<BookInfo> bookInfos;
    EditText editText1;
    EditText editText2;
    EditText editText3;
    TextView textView1;
    BookAdapter adapter;


    String buffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editText1 = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);

        adapter = new BookAdapter();
        //열려있으면 닫아
        if (database != null){
            database.close();
            database = null;
        }

        //데이터베이스 오픈
        database = BookDatabase.getInstance(this);
        boolean isOpen = database.open();

        viewBookInfo();








        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i;
                String name = editText1.getText().toString();
                String autor = editText2.getText().toString();
                String contents = editText3.getText().toString();
                for (i = 0;i < bookInfos.size();i++ ){
                    if (name.equals(bookInfos.get(i).getName())) {
                        Toast.makeText(getApplicationContext(), ""+name+ "은 이미 등록 되어있습니다.", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                if(i == bookInfos.size()){
                    database.insertRecord(name,autor,contents);
                    Intent intent = new Intent(getApplicationContext(), InquiryActivity.class);
                    startActivity(intent);
                    finish();
                }






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

    class BookAdapter extends BaseAdapter {
        ArrayList<Bookitem> items = new ArrayList<Bookitem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Bookitem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            BookItemView view = new BookItemView(getApplicationContext());

            Bookitem item = items.get(position);
            view.setName(item.getName());
            view.setAnthor(item.getAnthor());
            view.setContents(item.getContents());
            view.setImage(item.getResId());

            return view;
        }
    }

    public void viewBookInfo(){
        bookInfos = database.selectAll();
        //textView1.setText("");
        for(int i=0;i<bookInfos.size();i++){
            adapter.addItem(new Bookitem(bookInfos.get(i).getName(), bookInfos.get(i).getAuthor(), bookInfos.get(i).getContents(), R.drawable.study));

        }

    }


}