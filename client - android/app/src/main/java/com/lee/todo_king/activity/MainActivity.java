package com.lee.todo_king.activity;

import android.os.Bundle;
import android.util.Log;
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
import com.lee.todo_king.adapter.TodoAdapter;
import com.lee.todo_king.model.TodoDto;
import com.lee.todo_king.network.NetworkClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private EditText todoText;
    private ListView listView;
    private List<TodoDto> todoList;
    private TodoAdapter todoAdapter;
    private NetworkClient networkClient;
    private Button addTodoButton;

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

        addTodoButton = findViewById(R.id.addTodoButton);
        todoText = findViewById(R.id.todoText);
        listView = findViewById(R.id.todoListView);
        todoList = new ArrayList<>();
        todoAdapter = new TodoAdapter(this, todoList);
        listView.setAdapter(todoAdapter);
        networkClient = new NetworkClient();

        fetchTodoList(); // 할 일 목록 받아오기

        // 등록 버튼에 클릭 리스너 설정
        addTodoButton.setOnClickListener(view -> {
            String text = todoText.getText().toString();
            if (!text.isEmpty()) {
                sendAddTodoRequest(text);
            } else {
                Toast.makeText(MainActivity.this, "할 일을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 할 일 등록 메서드(CREATE)
    private void sendAddTodoRequest(String todoText) {
        // 서버 URL
        String url = "http://10.0.2.2:8080/api/todo/create"; // 호스트 머신의 localhost에 접근하려면 10.0.2.2 로 해야
        networkClient.sendAddTodoRequest(url, todoText, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // 요청 실패 처리
                Log.e("MainActivity", "sendAddTodoRequest() failed: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 요청 성공 처리
                if (response.isSuccessful()) {
                    runOnUiThread(() -> fetchTodoList());
                } else {
                    Log.e("MainActivity", "sendAddTodoRequest() failed with code " + response.code());
                }
            }
        });
    }

    // 할 일 목록 받아오기 메서드(READ)
    private void fetchTodoList() {
        // 서버 URL
        String url = "http://10.0.2.2:8080/api/todo/lists";
        networkClient.fetchTodoList(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // 요청이 실패한 경우 에러 로그 출력
                Log.e("MainActivity", "fetchTodoList() failed: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 요청 성공 처리
                if (response.isSuccessful()) {
                    // JSON 배열을 List<TodoDto>로 변환하기 위한 Gson 객체 생성
                    Gson gson = new Gson();

                    // List<TodoDto> 타입을 런타임 시에도 유지하기 위한 TypeToken 생성
                    Type listType = new TypeToken<List<TodoDto>>() {
                    }.getType();

                    // 응답 본문에서 JSON 배열 문자열을 가져옴
                    String jsonArrayString = response.body() != null ? response.body().string() : "";

                    // JSON 문자열을 List<TodoDto>로 변환
                    todoList = gson.fromJson(jsonArrayString, listType);

                    // UI 업데이트는 메인 스레드에서 수행해야 하므로 runOnUiThread 사용
                    runOnUiThread(() -> todoAdapter.updateData(todoList));
                } else {
                    // 요청이 성공하지 않은 경우 에러 로그 출력
                    Log.e("MainActivity", "할 일 목록 가져오기 실패");
                }
            }
        });
    }

    // 할 일 수정 메서드(UPDATE)
    public void sendUpdateTodoRequest(TodoDto todo) {
        // 서버 URL
        String url = "http://10.0.2.2:8080/api/todo/update";
        networkClient.sendUpdateTodoRequest(url, todo, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });

    }
}