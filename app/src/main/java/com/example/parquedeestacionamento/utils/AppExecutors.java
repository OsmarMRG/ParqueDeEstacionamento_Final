package com.example.parquedeestacionamento.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Gestão centralizada de executors para operações assíncronas.
 * Fornece acesso a executors para operações I/O e main thread.
 */
public final class AppExecutors {

    private static final ExecutorService IO_EXECUTOR = Executors.newSingleThreadExecutor();
    private static final Executor MAIN_EXECUTOR = new MainThreadExecutor();

    private AppExecutors() {
        // Construtor privado para prevenir instanciação
    }

    /**
     * Executor para operações de I/O (base de dados, rede, ficheiros).
     * Executa numa single thread para garantir ordem de operações.
     *
     * @return ExecutorService para operações I/O
     */
    @NonNull
    public static ExecutorService io() {
        return IO_EXECUTOR;
    }

    /**
     * Executor para operações na main thread (UI).
     * Útil para callbacks após operações assíncronas.
     *
     * @return Executor que executa na main thread
     */
    @NonNull
    public static Executor main() {
        return MAIN_EXECUTOR;
    }

    /**
     * Executor que posta Runnables na main thread via Handler.
     */
    private static class MainThreadExecutor implements Executor {
        private final Handler mainHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainHandler.post(command);
        }
    }
}
