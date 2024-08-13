package com.lee.todo_king.network;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NetworkClient {
    private final OkHttpClient client = new OkHttpClient(); // HTTP 클라이언트 생성

    // 전체 할 일 목록 받아오기
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

    // 할 일 등록
    public void sendAddTodoRequest(String url, String todoText, Callback responseCallback) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json = "{\"text\": \"" + todoText + "\"}"; // json 형식으로 변환

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
