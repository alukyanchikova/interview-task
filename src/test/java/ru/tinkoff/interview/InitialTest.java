package ru.tinkoff.interview;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class InitialTest {

    private static final String PAYMENTS_PAGE = "https://www.tinkoff.ru/payments/";
    private static final String SERVICE_PROVIDER_PAGE = "https://www.tinkoff.ru/zhku-moskva/";

    @Test
    public void should() throws InterruptedException {
        String currentUrl;
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.manage().window().maximize();

        //1
        driver.get("http://www.tinkoff.ru");

        //2
        driver.findElement(By.xpath("//ul[@id=\"mainMenu\"]//a[@href='/payments/']")).click();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Assert.assertEquals(driver.getCurrentUrl(), PAYMENTS_PAGE);

        //3
        driver.findElement(By.xpath("//span[@class=\"ui-link__text\"] [.='Коммунальные платежи']")).click();

        //4
        if (!driver.findElement(By.xpath("//span[.='Москве']")).isDisplayed()) {
            System.out.println("Не нашел Москву");
            //nead to think
        } else {
            System.out.println("Нашел Москву");
        }

        //5
        try {
            hover(driver);
        } catch (StaleElementReferenceException e) {
            hover(driver);
        }

        String serviceProviderName = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[contains(@class,'ui-menu')]/descendant::li[1]/span[contains(@class,'ui-menu__link')]/a/span"))).getText();

        System.out.println(serviceProviderName);

        driver.findElement(By.xpath("//ul[contains(@class,'ui-menu')]/descendant::li[1]")).click();

        //6
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //Assert.assertEquals(driver.getCurrentUrl(), SERVICE_PROVIDER_PAGE);
        driver.findElement(By.xpath("//span[.='Оплатить ЖКУ в Москве']")).click();

        //7

        //все поля пустые
        driver.findElement(By.xpath("//button[.='Оплатить ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']/../../div[.='Поле обязательное']")).isDisplayed();
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']/../../../div[.='Поле обязательное']")).isDisplayed();
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../../../../div[.='Поле обязательное']")).isDisplayed();

        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).sendKeys("буквы");
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']/../../../div[.='Поле обязательное']")).click();
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']/../../div[.='Поле обязательное']")).isDisplayed();

        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).sendKeys("12345");
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']/../../../div[.='Поле обязательное']")).click();
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']/../../div[.='Поле неправильно заполнено']")).isDisplayed();

        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).sendKeys("123456789012345");
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']/../../../div[.='Поле обязательное']")).click();
        driver.findElement(By.xpath("//div[*='Код плательщика за ЖКУ в Москве']/*[@value='1234567890']")).isDisplayed();

        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']/../../../div[.='Поле обязательное']")).click();
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']")).sendKeys("000000");
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']/../../../div[.='Поле заполнено некорректно']")).isDisplayed();

        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']")).click();
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']")).sendKeys("122020");
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
//        driver.findElement(By.xpath("//div[*='За какой период оплачиваете коммунальные услуги']/*[@value='12.2020']")).isDisplayed();
        driver.findElement(By.xpath("//div[*='За какой период оплачиваете коммунальные услуги']/*[@value='12.2000']")).isDisplayed();

        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/..")).click();
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../..")).sendKeys("1");
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../../../../div[contains(text(),'Минимальная сумма перевода')]")).isDisplayed();

        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/..")).click();
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../..")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../..")).sendKeys("15001");
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../../../../div[contains(text(),'Максимальная сумма перевода')]")).isDisplayed();

        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/..")).click();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        int textLength = driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../input")).getAttribute("value").length();
        while (textLength > 0) {
            driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../input")).sendKeys(Keys.DELETE);
            textLength--;
        }

        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../..")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../..")).sendKeys("14999");
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../input[@value='14 999']")).isDisplayed();

        //8
        driver.findElement(By.xpath("//ul[@id=\"mainMenu\"]//a[@href='/payments/']")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Assert.assertEquals(driver.getCurrentUrl(), PAYMENTS_PAGE);

        //9
        driver.findElement(By.xpath("//span[.='Что оплатить или куда перевести?']/following-sibling::input")).click();
        driver.findElement(By.xpath("//span[.='Что оплатить или куда перевести?']/following-sibling::input")).sendKeys(serviceProviderName);

        //10
        Assert.assertTrue(driver.findElement(By.xpath("//div[.='ЖКУ-Москва']/../../../../div[1]")).getText().contains(serviceProviderName));

        //11
        driver.findElement(By.xpath("//div[.='ЖКУ-Москва']/../../../../div[1]")).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //Assert.assertEquals(driver.getCurrentUrl(), SERVICE_PROVIDER_PAGE);

        //12
                //повторение шагов 2, 3 ( !!! )
        driver.findElement(By.xpath("//ul[@id=\"mainMenu\"]//a[@href='/payments/']")).click();
        driver.findElement(By.xpath("//span[@class=\"ui-link__text\"] [.='Коммунальные платежи']")).click();

        //13
        driver.findElement(By.xpath("//span[contains(text(),'Москве')]")).click();
        driver.findElement(By.xpath("//span[.='г. Санкт-Петербург']")).click();

        //14
        try {
            driver.findElement(By.xpath("//span[contains(text(),'" + serviceProviderName + "')]"));
            Assert.fail(serviceProviderName + " has been found on the page");
        } catch (NoSuchElementException e) {
            // expected
        }

        driver.quit();
    }

    private void hover(WebDriver driver) {
        new Actions(driver).moveToElement(driver.findElement(By.xpath("//ul[contains(@class,'ui-menu')]/descendant::li[1]"))).build().perform();
    }

}
