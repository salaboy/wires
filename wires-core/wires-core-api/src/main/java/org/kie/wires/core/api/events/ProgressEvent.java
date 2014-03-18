package org.kie.wires.core.api.events;

import java.io.Serializable;

public class ProgressEvent implements Serializable {
    private static final long serialVersionUID = 910177908332108118L;

    private boolean clearProgressBar;

    public ProgressEvent() {
        clearProgressBar = false;

    }

    public ProgressEvent(boolean clearProgressBar) {
        this.clearProgressBar = clearProgressBar;
    }

    public boolean isClearProgressBar() {
        return clearProgressBar;
    }

    public void setClearProgressBar(boolean clearProgressBar) {
        this.clearProgressBar = clearProgressBar;
    }

}
