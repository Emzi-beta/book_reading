package com.example.reportterm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class userActivity extends AppCompatActivity{


    BookDatabase database;
    ArrayList<BookInfo> bookInfos;
    ListView listView;
    float distance;
    float pressedX;
    ConstraintLayout layout;
    int mode = 0;
    GestureDetector detector;
    private boolean waitDouble = true;
    private static final int DOUBLE_CLICK_TIME = 200;
    Speech speech;
    BookAdapter adapter;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        // Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_in);
        // view.startAnimation(animation);
        View view1 = findViewById(R.id.view);


        TextView textView1 = (TextView)findViewById(R.id.textView5);//재목
        TextView textView2 = (TextView)findViewById(R.id.textView7);
        TextView textView3 = (TextView)findViewById(R.id.textView8);
        TextView textView4 = (TextView)findViewById(R.id.textView9); //내영
        ImageView imageView = (ImageView)findViewById(R.id.imageView3) ;
        ImageView imageView2 = (ImageView)findViewById(R.id.imageView6) ;

        adapter = new BookAdapter();


        database = BookDatabase.getInstance(this);
        boolean isOpen = database.open();

        viewBookInfo();

        textView1.setText(bookInfos.get(i).getName());
        textView2.setText(bookInfos.get(i).getAuthor());
        //textView3.setText(bookInfos.get(0).getContents());
        //textView4.setText(bookInfos.get(0).getContents());





        layout = findViewById(R.id.userlay);
        pressedX = 0;
        distance = 0;
        speech = new Speech(getApplicationContext());
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                speech.say("현재 책은" + bookInfos.get(i).getName()+ " 입니다.다른책을 원하시면 좌우로 드래그 해주세요.");
                textView3.setText("음성안내 : 현재 책은" + bookInfos.get(i).getName()+ " 입니다.다른책을 원하시면 좌우로 드래그 해주세요.");
                // 시간 지난 후 실행할 코딩
            }
        }, 500);






        detector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }
            @Override
            public void onShowPress(MotionEvent motionEvent) {
            }
            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                if (waitDouble == true) {
                    waitDouble = false;
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                sleep(DOUBLE_CLICK_TIME);
                                if (waitDouble == false) {
                                    waitDouble = true;
                                    //single click event
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                } else {
                    waitDouble = true;
                    Log.d("dsad", "두번클릭 ");


                    speech.say("책읽기를 시작해겠습니다." + bookInfos.get(i).getContents());
                    textView4.setText("");
                    textView3.setText("음성안내 : "+bookInfos.get(i).getContents());
                    imageView.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.VISIBLE);


                }
                return true;
            }
            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return true;
            }
            @Override
            public void onLongPress(MotionEvent motionEvent) {
                Log.d("dsad", "누르고있기 ");
            }
            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return true;
            }
        });






        view1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    pressedX = event.getX();
                    Log.d("dsad", "a"+event.getX());
                } else if (action == MotionEvent.ACTION_MOVE) {

                } else if (action == MotionEvent.ACTION_UP) {
                    distance = pressedX - event.getX();
                    Log.d("dsad", "s"+event.getX());

                }
                if (Math.abs(distance) < 100) {
                    Log.d("dsad", "dsd"+distance);
                    //distance = 0;
                }
                else if ( distance > 0.0f) {
                    // 손가락을 왼쪽으로 움직였으면 오른쪽 화면이 나타나야 한다.
                    Log.d("dsad", "오른쪽드래그 ");
                    if (i == bookInfos.size()-1){
                        speech.say("마지막 책입니다.");
                        textView3.setText("음성안내: 마지막 책입니다.");
                    }
                    else {
                    i ++;
                    speech.say(""+ bookInfos.get(i).getName()+ "책이며 저자는 "+bookInfos.get(i).getAuthor()+"입니다.");
                    textView3.setText("음성안내: "+ bookInfos.get(i).getName()+ "책이며 저자는 "+bookInfos.get(i).getAuthor()+"입니다.");
                    textView1.setText(bookInfos.get(i).getName());
                    textView2.setText(bookInfos.get(i).getAuthor());



                }}
                else if ( distance < 0.0f) {
// 손가락을 왼쪽으로 움직였으면 오른쪽 화면이 나타나야 한다.
                    Log.d("dsad", "왼쪽드래그 "+distance);
                        if (i == 0){
                            speech.say("첫책입니다.");
                            textView3.setText("음성안내: 첫책입니다.");
                        }
                        else {
                            i -- ;
                            speech.say(""+ bookInfos.get(i).getName()+ "책이며 저자는 "+bookInfos.get(i).getAuthor()+"입니다.");
                            textView3.setText("음성안내: "+ bookInfos.get(i).getName()+ "책이며 저자는 "+bookInfos.get(i).getAuthor()+"입니다.");
                            textView1.setText(bookInfos.get(i).getName());
                            textView2.setText(bookInfos.get(i).getAuthor());



                }}
                return true;}
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




