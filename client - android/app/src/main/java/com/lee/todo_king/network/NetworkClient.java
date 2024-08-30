package com.lee.todo_king.network;

import com.google.gson.Gson;
import com.lee.todo_king.model.TodoDto;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NetworkClient {
    private final OkHttpClient client = new OkHttpClient(); // HTTP 클라이언트 생성

    // 할 일 등록 요청(CREATE)
    public void sendAddTodoRequest(String url, TodoDto todoDto, Callback responseCallback) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
//        String json = "{\"text\": \"" + todoText + "\"}"; // json 형식으로 변환 // {"text": "todoText"} Gson 안 쓰고 json 변환하기
        String json = new Gson().toJson(todoDto);

        // HTTP 요청 생성
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // 비동기 요청 보내기
        client.newCall(request).enqueue(responseCallback);
    }

    // 할 일 목록 받아오기 요청(READ)
    public void fetchTodoList(String url, Callback responseCallback) {
        // MainActivity.java 로부터 책(responseCallback) 넘겨받음
        // HTTP 요청 생성(미사일)
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        // 비동기 요청 보내기(발사)
        client.newCall(request).enqueue(responseCallback); // 책(responseCallback) 돌려주기(비동기 요청 응답)
    }

    // 할 일 수정 요청(UPDATE)
    public void sendUpdateTodoRequest(String url, TodoDto todoDto, Callback responseCallback) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        // todoDto 객체를 JSON 형식으로 변환
        String json = new Gson().toJson(todoDto);

        // HTTP 요청 생성
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // 비동기 요청 보내기
        client.newCall(request).enqueue(responseCallback);
    }
}
