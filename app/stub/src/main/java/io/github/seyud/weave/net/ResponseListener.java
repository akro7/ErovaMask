package io.github.seyud.weave.net;

public interface ResponseListener<T> {
    void onResponse(T response);
}
