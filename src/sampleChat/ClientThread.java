package sampleChat;

import java.net.Socket;

public class ClientThread extends SocketThread {

    public final long LIVE_TIME_MILLI = 120_000L;
    private String nickname;
    private boolean isAuthorized;
    private boolean isReconnecting;
    private Long connectingTime;

    public ClientThread(SocketThreadListener listener, String name, Socket socket) {
        super(name, listener, socket);
        connectingTime = System.currentTimeMillis();
    }

    public boolean isReconnecting() {
        return isReconnecting;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    void reconnect() {
        isReconnecting = true;
        close();
    }

    void authAccept(String nickname) {
        isAuthorized = true;
        this.nickname = nickname;
        sendMessage(Library.getAuthAccept(nickname));
    }

    void authFail() {
        sendMessage(Library.getAuthDenied());
        close();
    }

    void msgFormatError(String msg) {
        sendMessage(Library.getMsgFormatError(msg));
        close();
    }

    void unautorizedConnect(){
        this.nickname = "Anonymous";
    }

    public boolean isTimeUp(){
        return System.currentTimeMillis() - connectingTime > LIVE_TIME_MILLI;
    }
}
