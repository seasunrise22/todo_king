package com.lee.todo_king;

import android.os.Bundle;
import android.util.Log;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText todoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 등록 버튼에 클릭 리스너 설정
        Button addTodoButton = findViewById(R.id.addTodoButton);
        todoText = findViewById(R.id.todoText);
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = todoText.getText().toString();
                if(!text.isEmpty()) {
                    sendAddTodoRequest(text);
                } else {
                    Toast.makeText(MainActivity.this, "할 일을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendAddTodoRequest(String todoText) {
        // 서버 URL
        String url = "http://10.0.2.2:8080/api/todo/create"; // 호스트 머신의 localhost에 접근하려면 10.0.2.2 로 해야

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json = "{\"text\": \"" + todoText + "\"}"; // json 형식으로 변환

        // HTTP 요청 생성
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // HTTP 클라이언트 생성
        OkHttpClient client = new OkHttpClient();

        // 비동기 요청 보내기
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // 요청 실패 처리
                Log.e("TodoListRequest", "Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 요청 성공 처리
                if(response.isSuccessful()) {
                    String todoList = response.body() != null ? response.body().string() : "";
                    Log.d("TodoListRequest", "Request success(" + response.code() + "): " + todoList);
                } else {
                    Log.e("TodoListRequest", "Request failed with code " + response.code());
                }
            }
        });
    }
}