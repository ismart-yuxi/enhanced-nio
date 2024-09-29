package io.github.yx.socket.practice.session;

// SessionContext类
public class SessionContext {
    private static SessionManager sessionManager;

    public static void initialize(SessionManager manager) {
        sessionManager = manager;
    }

    public static SessionManager getSessionManager() {
        if (sessionManager == null) {
            throw new IllegalStateException("SessionManager is not initialized.");
        }
        return sessionManager;
    }
}