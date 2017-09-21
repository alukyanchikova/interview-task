package ru.tinkoff.interview.infrastructure;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

public class Util {

    /**
     * Used when an action should be repeated in case of stale exception
     * @param action to repeat
     */
    public static void staleSafeInvocation(Runnable action) {
        try {
            action.run();
        } catch (StaleElementReferenceException e) {
            action.run();
        }
    }

    /**
     * Used when standart `driver.clear()` method does not work (e.g for Chrome driver)
     * @param driver
     * @param xpath
     */
    public static void deleteInputText(WebDriver driver, String xpath) {
        int textLength = driver.findElement(By.xpath(xpath)).getAttribute("value").length();
        while (textLength > 0) {
            driver.findElement(By.xpath(xpath)).sendKeys(Keys.DELETE);
            textLength--;
        }
    }
}
