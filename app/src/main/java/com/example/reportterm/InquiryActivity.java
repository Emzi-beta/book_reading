package com.example.reportterm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class InquiryActivity extends AppCompatActivity {


    BookDatabase database;
    ArrayList<BookInfo> bookInfos;
    ListView listView;
    BookAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        listView = (ListView) findViewById(R.id.listview);
        EditText serch = (EditText) findViewById(R.id.serch);
        Button serchbutton = (Button) findViewById(R.id.serchbutton) ;
        Button newbutton = (Button) findViewById(R.id.newbutton) ;


        adapter = new BookAdapter();


        database = BookDatabase.getInstance(this);
        boolean isOpen = database.open();
        listView.setAdapter(adapter);
        viewBookInfo();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Bookitem item = (Bookitem) adapter.getItem(position);
                //Toast.makeText(getApplicationContext(), "선택 : " + item.getName(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("name",item.getName());
                //intent.putExtra("image",item.getResId());
                startActivity(intent);
                finish();
            }
        });


        serchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i;
                String serchString = serch.getText().toString();
                for (i = 0;i<bookInfos.size();i++ ){
                    if (serchString.equals(bookInfos.get(i).getName())){
                        Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                        intent.putExtra("name",serchString);
                        //intent.putExtra("image",item.getResId());
                        startActivity(intent);
                        finish();
                        break;

                    }
                }
                if(i == bookInfos.size())
                    Toast.makeText(getApplicationContext(), "검색하신 "+serchString+"은 등록되어있지 않습니다.", Toast.LENGTH_LONG).show();
            }

        });

        newbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), newActivity.class);
                //intent.
                //intent.putExtra("image",item.getResId());
                startActivity(intent);
                finish();
            }
        });
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

    protected void onDestroy(){
        if (database != null){
            database.close();
            database = null;
        }
        super.onDestroy();
    }
}



