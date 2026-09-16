package com.example.parquedeestacionamento.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Fornece executors para correr código em background (I/O) e na UI thread
public final class AppExecutors {

    private static final ExecutorService IO_EXECUTOR = Executors.newSingleThreadExecutor();
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private AppExecutors() {}

    // Executor para operações de base de dados e rede
    @NonNull
    public static ExecutorService io() {
        return IO_EXECUTOR;
    }

    // Posta um Runnable na main thread (para atualizar a UI)
    public static void main(Runnable runnable) {
        MAIN_HANDLER.post(runnable);
    }
}
