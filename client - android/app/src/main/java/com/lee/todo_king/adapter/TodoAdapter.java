package com.lee.todo_king.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lee.todo_king.R;
import com.lee.todo_king.activity.MainActivity;
import com.lee.todo_king.model.TodoDto;

import java.util.List;

public class TodoAdapter extends ArrayAdapter<TodoDto> {
    private final List<TodoDto> todoList;
    private final Context context;
    private final MainActivity mainActivity;

    public TodoAdapter(MainActivity mainActivity, List<TodoDto> todoList) {
        super(mainActivity, 0, todoList);
        this.context = mainActivity;
        this.todoList = todoList;
        this.mainActivity = mainActivity;
    }

    // 데이터 업데이트 메서드
    public void updateData(List<TodoDto> newTodoList) {
        this.todoList.clear(); // 기존 리스트 비우기
        this.todoList.addAll(newTodoList); //새 데이터 추가
        notifyDataSetChanged(); // 어댑터에 데이터 변경 알림(다시 getView 메서드 호출)
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 이미 생성된 뷰가 있는지 확인
        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);

        // 현재 할 일 목록 가져오기
        TodoDto todo = todoList.get(position);

        // UI요소 설정
        TextView todoText = convertView.findViewById(R.id.todoText);
        Button editButton = convertView.findViewById(R.id.editButton);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        todoText.setText(todo.getText());

        // 수정 버튼 클릭 리스너 설정
        editButton.setOnClickListener(v -> {
            // 다이얼로그 생성
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("할 일 수정");

            // EditText 추가
            EditText input = new EditText(context);
            input.setText(todo.getText());
            builder.setView(input);

            // 확인 버튼 설정
            builder.setPositiveButton("수정", (dialog, which) -> {
               String newText = input.getText().toString();
               todo.setText(newText);
               mainActivity.sendUpdateTodoRequest(todo);
               notifyDataSetChanged();
            });

            // 취소 버튼 설정
            builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

            // 다이얼로그 표시
            builder.show();
        });

        // 삭제 버튼 클릭 리스너 설정
        deleteButton.setOnClickListener(v -> {
            // 삭제 로직 추가
            todoList.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }
}