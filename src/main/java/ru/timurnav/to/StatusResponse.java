package ru.timurnav.to;

public class StatusResponse {

    private boolean oldStatus;
    private boolean currentStatus;
    private Long id;

    public StatusResponse setOldStatus(boolean oldStatus) {
        this.oldStatus = oldStatus;
        return this;
    }

    public StatusResponse setCurrentStatus(boolean currentStatus) {
        this.currentStatus = currentStatus;
        return this;
    }

    public StatusResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public boolean isOldStatus() {
        return oldStatus;
    }

    public boolean isCurrentStatus() {
        return currentStatus;
    }

    public Long getId() {
        return id;
    }
}
