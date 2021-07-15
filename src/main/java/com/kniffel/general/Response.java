package com.kniffel.general;

public class Response<T> {

    private T content;
    private StatusMessage statusMessage;
    private String targetUUID;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public StatusMessage getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(StatusMessage statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getTargetUUID() {
        return targetUUID;
    }

    public void setTargetUUID(String targetUUID) {
        this.targetUUID = targetUUID;
    }
}
