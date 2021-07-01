package com.example.reportterm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Speech {
    private STT stt;
    private TTS tts;
    private Context mContext;
    MainActivity a;

    Speech(Context mConext) {
        this.mContext = mConext;
        stt = new STT(mContext);
        tts = new TTS(mContext);
    }

    public void say(String text) {
        tts.startTTS(text);
    }

    public void litsen() {
        stt.startSTT();
    }

    public void endSpeech() {
        this.stt.endSTT();
        this.tts.endTTS();
    }

    private class STT {

        Context mContext;
        Intent sttIntent;
        SpeechRecognizer mRecognizer;

        String keyword = "";
        TTS tts;

        STT(Context mContext) {
            this.mContext = mContext;
            sttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            sttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, mContext.getPackageName());
            sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");//한국어 사용

            tts = new TTS(mContext);
        }

        void startSTT() {
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext);
            mRecognizer.setRecognitionListener(listener);
            mRecognizer.startListening(sttIntent);

        }

        void endSTT() {
            if (mRecognizer != null) {
                mRecognizer.destroy();
                mRecognizer.cancel();
                mRecognizer = null;
            }

            tts.endTTS();
        }

        String getKeyword() {
            return keyword;
        }

        private RecognitionListener listener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                //ToastMsg("음성인식 시작");
                tts.startTTS("어떤책을 읽어드릴까요?");
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                String error;

                switch(i) {
                    case SpeechRecognizer.ERROR_AUDIO:
                        error = "ERROR_AUDIO";
                        break;
                    case SpeechRecognizer.ERROR_CLIENT:
                        error = "RROR_CLIENT";
                        break;
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        error = "ERROR_INSUFFICIENT_PERMISSIONS";
                        break;
                    case SpeechRecognizer.ERROR_NETWORK:
                        error = "ERROR_NETWORK";
                        break;
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        error = "ERROR_NO_MATCH";
                        break;
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        error = "ERROR_RECOGNIZER_BUSY";
                        break;
                    case SpeechRecognizer.ERROR_SERVER:
                        error = "ERROR_SERVER";
                        break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        error = "ERROR_SPEECH_TIMEOUT";
                        break;
                    default:
                        error = "UNKOWN ERROR";
                        break;
                }

                ToastMsg("error : " + error);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                String resultStr = "";

                for(int i=0; i < matches.size(); i++) {
                    resultStr += matches.get(i);
                }

                if (resultStr.length() < 1) return;
                resultStr = resultStr.replace("", "");

                //ToastMsg("음성인식 성공");
                voiceOrderCheck(resultStr);
            }


            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        };

        //입력된 음성 메세지 확인 후 동작 처리
        private void voiceOrderCheck(String speech) {
/*
            String subway_line_4_keyword = typoEdit.typo_edit(speech, SUBWAY_LINE_4_KEYWORDS);

            if (subway_line_4_keyword.equals("ERROR")) {
                tts.startTTS("다시 말해주세요");

            } else {
                keyword = subway_line_4_keyword;
                tts.startTTS("포카칩이 있는 진열대로 안내하겠습니다");
  */
            }
        }

        private void ToastMsg(final String msg) {
            final String message = msg;
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }


    private class TTS implements TextToSpeech.OnInitListener{
        Context mContext;
        TextToSpeech tts;

        TTS(Context mContext) {
            this.mContext = mContext;

            tts = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        tts.setLanguage(Locale.KOREAN);
                    }
                }
            });
        }

        @Override
        public void onInit(int i) {
            if (i == TextToSpeech.SUCCESS) {
                tts.setPitch(1.0f);//목소리 톤1.0
                tts.setSpeechRate(1.0f);//목소리 속도
            } else {
                ToastMsg("tts 초기화 실패");
            }
        }

        //음성 메세지 출력용
        void startTTS(String OutMsg) {
            if (OutMsg.length() < 1) return;

            if(!tts.isSpeaking()) {
                tts.speak(OutMsg, TextToSpeech.QUEUE_FLUSH, null);
            }
        }

        void endTTS() {
            if (tts != null) {
                tts.stop();
                tts.shutdown();
                tts = null;
            }
        }

        private void ToastMsg(final String msg) {
            final String message = msg;
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

}