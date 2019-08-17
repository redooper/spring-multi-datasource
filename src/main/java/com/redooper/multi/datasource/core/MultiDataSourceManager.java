package com.redooper.multi.datasource.core;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @Auther: Jackie
 * @Date: 2019-08-03 17:21
 * @Description:
 */
public class MultiDataSourceManager implements Closeable {

    private static final ThreadLocal<LinkedList<String>> LOOKUP_KEY_HOLDER = ThreadLocal.withInitial(LinkedList::new);

    public MultiDataSourceManager(String lookupKey) {
        push(lookupKey);
    }

    public static void push(String lookupKey) {
        LOOKUP_KEY_HOLDER.get().push(lookupKey);
    }

    public static String peek() {
        return LOOKUP_KEY_HOLDER.get().peek();
    }

    public static void poll() {
        LinkedList<String> lookupKeys = LOOKUP_KEY_HOLDER.get();
        lookupKeys.poll();
        if (lookupKeys.isEmpty()) {
            LOOKUP_KEY_HOLDER.remove();
        }
    }

    @Override
    public void close() throws IOException {
        poll();
    }
}
