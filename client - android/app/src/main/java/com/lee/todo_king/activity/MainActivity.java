package com.lee.todo_king.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lee.todo_king.R;
import com.lee.todo_king.model.TodoDto;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private EditText todoText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<TodoDto> todoList;

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

        listView = findViewById(R.id.todoListView);
        todoList = new ArrayList<>();

        fetchTodoList(); // 할 일 목록 받아오기

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

    // 할 일 목록 받아오기 메서드
    private void fetchTodoList() {
        // 서버 URL
        String url = "http://10.0.2.2:8080/api/todo/lists";

        // HTTP 요청 생성(미사일)
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        // HTTP 클라이언트 생성(미사일 발사대)
        OkHttpClient client = new OkHttpClient();

        // 비동기 요청 보내기(발사)
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // 요청 실패 처리
                Log.e("MainActivity", "fetchTodoList() failed: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 요청 성공 처리
                if(response.isSuccessful()) {
                    String jsonArrayString = response.body() != null ? response.body().string() : "";
                    Log.d("MainActivity", "할 일 목록: " + jsonArrayString);

                    // JSON 배열을 List<TodoDto>로 변환
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<TodoDto>>(){}.getType(); // 런타임시에도 타입정보를 유지하기 위해
                    todoList = gson.fromJson(jsonArrayString, listType);
                    Log.d("MainActivity", "todoList 변환 잘 됐나?: " + todoList);

                    // UI 업데이트는 메인 스레드에서 수행해야 함
                    runOnUiThread(() -> updateListView());
                } else {
                    // 요청 실패 처리
                    Log.e("MainActivity", "할 일 목록 가져오기 실패");
                }
            }
        });
    }

    private void updateListView() {
        List<String> todoTexts = new ArrayList<>();
        for(TodoDto todo : todoList) {
            todoTexts.add(todo.getText());
        }

        // 어댑터 설정
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoTexts);
        listView.setAdapter(adapter);
    }

    // 할 일 등록 메서드
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
                Log.e("MainActivity", "sendAddTodoRequest() failed: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 요청 성공 처리
                if(response.isSuccessful()) {
                    String todoList = response.body() != null ? response.body().string() : "";
                    Log.d("MainActivity", "sendAddTodoRequest() success(" + response.code() + "): " + todoList);
                } else {
                    Log.e("MainActivity", "sendAddTodoRequest() failed with code " + response.code());
                }
            }
        });
    }
}