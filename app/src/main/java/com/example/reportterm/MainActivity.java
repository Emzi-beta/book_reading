package com.example.reportterm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_in);
        // view.startAnimation(animation);
        View view1 = findViewById(R.id.view);

        TextView textView1 = (TextView)findViewById(R.id.textView5);
        TextView textView2 = (TextView)findViewById(R.id.textView7);
        TextView textView3 = (TextView)findViewById(R.id.textView8);
        ImageView imageView = (ImageView)findViewById(R.id.imageView3) ;







        layout = findViewById(R.id.userlay);
        pressedX = 0;
        distance = 0;
        speech = new Speech(getApplicationContext());
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                speech.say("화이트북을 이용해주셔서 감사합니다. 책을 읽으실려면 화면을 두번터치해주시고 보호자모드로 변경하시려면 오른쪽으로 드래그 해주세요.");
                textView3.setText("음성안내 : 화이트북을 이용해주셔서 감사합니다. 책을 읽으실려면 화면을 두번터치해주시고 보호자모드로 변경하시려면 오른쪽으로 드래그 해주세요.");
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
                    if(mode == 0){
                        Intent intent = new Intent(getApplicationContext(), userActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(mode == 1){
                        Intent intent = new Intent(getApplicationContext(), InquiryActivity.class);
                        startActivity(intent);
                        finish();
                    }
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
                    mode = 1;
                    speech.say("보호자 모드입니다. 책의 수정과 추가 삭제를 할수있습니다. 모드에 진입하시려면 화면을 두번 터치해주세요 다시 돌아가려면 왼쪽으로 드래그 해주세요.");
                    textView1.setText("보호자모드");
                    textView2.setText("책 수정,삭제,추가,검색,조회 기능");
                    textView3.setText("음성안내: 보호자 모드입니다. 책의 수정과 추가 삭제를 할수있습니다. 모드에 진입하시려면 화면을 두번 터치해주세요 다시 돌아가려면 왼쪽으로 드래그 해주세요.");
                    imageView.setImageResource(R.drawable.view);

                    /*
                    Intent intent = new Intent(mCo, administerActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
*/
                }
                else if ( distance < 0.0f) {
// 손가락을 왼쪽으로 움직였으면 오른쪽 화면이 나타나야 한다.
                    Log.d("dsad", "왼쪽드래그 "+distance);

                    mode = 0;

                    speech.say("책읽기 모드입니다. 화면을 두번 터치시 책을 읽어줍니다. 관리자 모드로 돌아가려면 오른쪽으로 드래그 해주세요.");
                    textView1.setText("책읽기모드");
                    textView2.setText("유저모드");
                    textView3.setText("음성안내: 책읽기 모드입니다. 화면을 두번 터치시 책을 읽어줍니다. 관리자 모드로 돌아가려면 오른쪽으로 드래그 해주세요.");
                    imageView.setImageResource(R.drawable.book);
                    /*
                    Intent intent = new Intent(mCo, administerActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
*/
                }
                return true;}
        });
                /*
                else {
// 손가락을 오른쪽으로 움직였으면 왼쪽 화면이 나타나야 한다.
                    Intent intent = new Intent(getApplicationContext(), administerActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);


                }*/

                //finish();

            }


    }




