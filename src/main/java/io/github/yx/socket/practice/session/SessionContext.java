package io.github.yx.socket.practice.session;

/**
 * SessionContext类用于管理会话。
 */
public class SessionContext {
    private static SessionManager sessionManager;

    public static void initialize(SessionManager manager) {
        sessionManager = manager; // 初始化会话管理器
    }

    public static SessionManager getSessionManager() {
        if (sessionManager == null) {
            throw new IllegalStateException("SessionManager is not initialized."); // 检查会话管理器是否已初始化
        }
        return sessionManager;
    }
}
