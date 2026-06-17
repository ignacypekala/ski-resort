package io.github.ignacypekala.input;

import io.github.ignacypekala.utils.Reporter;

public class DelegatingReporter implements Reporter {
    private Reporter delegate;

    public void setDelegate(final Reporter delegate) {
        this.delegate = delegate;
    }

    @Override
    public void report(final String message) {
        if (delegate != null) {
            delegate.report(message);
        }
    }
}
