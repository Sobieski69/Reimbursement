package org.example;

public class ContextFactory {

    private static final ThreadLocal<Context> contexts = new ThreadLocal<>();

    public static Context getContext() {
        try {
            if (contexts.get() == null) {
                contexts.set(new Context());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contexts.get();
    }
}

