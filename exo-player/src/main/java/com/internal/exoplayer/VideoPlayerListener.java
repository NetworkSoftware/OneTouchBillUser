package com.internal.exoplayer;

public interface VideoPlayerListener {

    void onBackClick();
    void onShareClick(String url, String status, String name);
}
