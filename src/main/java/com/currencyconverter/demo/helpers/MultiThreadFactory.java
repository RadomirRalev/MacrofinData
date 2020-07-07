package com.currencyconverter.demo.helpers;

import java.util.ConcurrentModificationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadFactory {
    static ExecutorService es = Executors.newCachedThreadPool();
    static String callingClass = null;

    public static void add(Runnable task) {
        String presentClass = new Throwable().getStackTrace()[1].getClassName();
        presentClass = "Thread " + Thread.currentThread().getId() + ": " + presentClass;
        System.out.println("Thread created!");
        if (callingClass == null) {
            callingClass = presentClass;
        } else {
            if (!callingClass.equals(presentClass)) {
                throw new ConcurrentModificationException("AQ.add called from multiple classes: " + callingClass + ", " + presentClass);
            }
        }
        es.execute(task);
    }

    public static void finish() {
        // request all threads be completed
        es.shutdown();
        System.out.println("Thread finished!");

        // letting threads complete
        try {
            es.awaitTermination(100, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // resetting threads
        es = Executors.newCachedThreadPool();
        // forget the previous calling class
        callingClass = null;
    }
}
