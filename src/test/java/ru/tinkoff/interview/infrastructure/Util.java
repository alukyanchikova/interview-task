package ru.tinkoff.interview.infrastructure;

import org.openqa.selenium.StaleElementReferenceException;

public class Util {

    public static void staleSafeInvocation(Runnable action) {
        try {
            action.run();
        } catch (StaleElementReferenceException e) {
            action.run();
        }
    }
}
