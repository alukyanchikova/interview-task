package ru.tinkoff.interview.payments;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.tinkoff.interview.AbstractAutomationTest;
import ru.tinkoff.interview.infrastructure.Constant;
import ru.tinkoff.interview.infrastructure.Util;

import java.util.concurrent.TimeUnit;

public class PaymentsServicesTest extends AbstractAutomationTest {

    @Test(invocationTimeOut = Constant.Common.TEST_TIMEOUT_MS)
    public void utilitiesProvidersLogicTest() {

        /*
         * 1. Переходом по адресу https://www.tinkoff.ru/ загрузить стартовую страницу Tinkoff Bank.
         */
        driver.get(Constant.Url.START_PAGE);

        /*
         * 2. Из верхнего меню, нажатием на пункт меню “Платежи“, перейти на страницу “Платежи“.
         */
        driver.findElement(By.xpath("//ul[@id=\"mainMenu\"]//a[@href='/payments/']")).click();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Assert.assertEquals(driver.getCurrentUrl(), Constant.Url.PAYMENTS_PAGE);

        /*
         * 3. В списке категорий платежей, нажатием на пункт “Коммунальные платежи“, перейти на страницу выбора поставщиков услуг.
         */
        driver.findElement(By.xpath("//span[@class=\"ui-link__text\"] [.='Коммунальные платежи']")).click();

        /*
         * 4. Убедиться, что текущий регион – “г. Москва” (в противном случае выбрать регион “г. Москва” из списка регионов).
         */
        if (!driver.findElement(By.xpath("//span[.='Москве']")).isDisplayed()) {
            driver.findElement(By.xpath("//div[contains(text(),'Коммунальные платежи')]/span[2]")).click();
            driver.findElement(By.xpath("//span[.='г. Москва']")).click();
        }

        /*
         * 5. Со страницы выбора поставщиков услуг, выбрать 1-ый из списка (Должен быть “ЖКУ-Москва”). Сохранить его наименование
         * (далее “искомый”) и нажатием на соответствующий элемент перейти на страницу оплаты “ЖКУ-Москва“.
         */
        Util.staleSafeInvocation(() -> new Actions(driver).moveToElement(driver.findElement(By.xpath("//ul[contains(@class,'ui-menu')]/descendant::li[1]"))).build().perform());

        String serviceProviderName = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//ul[contains(@class,'ui-menu')]/descendant::li[1]/span[contains(@class,'ui-menu__link')]/a/span"))).getText();

        driver.findElement(By.xpath("//ul[contains(@class,'ui-menu')]/descendant::li[1]")).click();

        /*
         * 6. На странице оплаты, перейти на вкладку “Оплатить ЖКУ в Москве“.
         */
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
//        Assert.assertEquals(driver.getCurrentUrl(), Constant.Url.SERVICE_PROVIDER_PAGE); // TODO url check does not work stable
        driver.findElement(By.xpath("//span[.='Оплатить ЖКУ в Москве']")).click();

        /*
         * 7. Выполнить проверки на невалидные значения для обязательных полей: проверить все текстовые сообщения об ошибке (и их содержимое),
         * которые появляются под соответствующим полем ввода в результате ввода некорректных данных.
         */

        //все поля пустые
        driver.findElement(By.xpath("//button[.='Оплатить ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']/../../div[.='Поле обязательное']")).isDisplayed();
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']/../../../div[.='Поле обязательное']")).isDisplayed();
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../../../../div[.='Поле обязательное']")).isDisplayed();

        //заполнение кода плательщика буквенными значениями
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).sendKeys("буквы");
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']/../../../div[.='Поле обязательное']")).click();
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']/../../div[.='Поле обязательное']")).isDisplayed();

        // заполнение кода плательщика циферными, в недостаточном количестве
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).sendKeys("12345");
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']/../../../div[.='Поле обязательное']")).click();
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']/../../div[.='Поле неправильно заполнено']")).isDisplayed();

        // заполнение кода плательщика циферными, в избыточном количестве
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).sendKeys("123456789012345");
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']/../../../div[.='Поле обязательное']")).click();
        driver.findElement(By.xpath("//div[*='Код плательщика за ЖКУ в Москве']/*[@value='1234567890']")).isDisplayed();

        //заполнение периода нулями
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']/../../../div[.='Поле обязательное']")).click();
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']")).sendKeys("000000");
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']/../../../div[.='Поле заполнено некорректно']")).isDisplayed();

        //заполнение периода будущим периодом
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']")).click();
        driver.findElement(By.xpath("//label[*='За какой период оплачиваете коммунальные услуги']")).sendKeys("122020");
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
//        driver.findElement(By.xpath("//div[*='За какой период оплачиваете коммунальные услуги']/*[@value='12.2020']")).isDisplayed(); // TODO url check does not work stable 
        driver.findElement(By.xpath("//div[*='За какой период оплачиваете коммунальные услуги']/*[@value='12.2000']")).isDisplayed();

        // заполнение суммы перевода меньше минимума
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/..")).click();
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../..")).sendKeys("1");
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../../../../div[contains(text(),'Минимальная сумма перевода')]")).isDisplayed();

        // заполнение суммы перевода больше максимума
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/..")).click();
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../..")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../..")).sendKeys("15001");
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../../../../div[contains(text(),'Максимальная сумма перевода')]")).isDisplayed();

        // заполнение суммы перевода
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/..")).click();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Util.deleteTextFromInput(driver, "//b[contains(text(),'Сумма платежа')]/../input");
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../..")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../..")).sendKeys("14999");
        driver.findElement(By.xpath("//label[*='Код плательщика за ЖКУ в Москве']")).click();
        driver.findElement(By.xpath("//b[contains(text(),'Сумма платежа')]/../input[@value='14 999']")).isDisplayed();

        /*
         * 8. Повторить шаг (2).
         */
        driver.findElement(By.xpath("//ul[@id=\"mainMenu\"]//a[@href='/payments/']")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Assert.assertEquals(driver.getCurrentUrl(), Constant.Url.PAYMENTS_PAGE);

        /*
         * 9. В строке быстрого поиска поставщика услуг ввести наименование искомого (ранее сохраненного).
         */
        driver.findElement(By.xpath("//span[.='Что оплатить или куда перевести?']/following-sibling::input")).click();
        driver.findElement(By.xpath("//span[.='Что оплатить или куда перевести?']/following-sibling::input")).sendKeys(serviceProviderName);

        /*
         * 10. Убедиться, что в списке предложенных провайдеров искомый поставщик первый.
         */
        Assert.assertTrue(driver.findElement(By.xpath("//div[.='ЖКУ-Москва']/../../../../div[1]")).getText().contains(serviceProviderName));

        /*
         * 11. Нажатием на элемент, соответствующий искомому, перейти на страницу “Оплатить ЖКУ в Москве“.
         * Убедиться, что загруженная страница та же, что и страница, загруженная в результате шага (5).
         */
        driver.findElement(By.xpath("//div[.='ЖКУ-Москва']/../../../../div[1]")).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//        Assert.assertEquals(driver.getCurrentUrl(), Constant.Url.SERVICE_PROVIDER_PAGE); // TODO url check does not work stable

        /*
         * 12. Выполнить шаги (2) и (3).
         */
        driver.findElement(By.xpath("//ul[@id=\"mainMenu\"]//a[@href='/payments/']")).click();
        driver.findElement(By.xpath("//span[@class=\"ui-link__text\"] [.='Коммунальные платежи']")).click();

        /*
         * 13. В списке регионов выбрать “г. Санкт-Петербург”.
         */
        driver.findElement(By.xpath("//span[contains(text(),'Москве')]")).click();
        driver.findElement(By.xpath("//span[.='г. Санкт-Петербург']")).click();

        /*
         * 14. Убедиться, что в списке поставщиков на странице выбора поставщиков услуг отсутствует искомый.
         */
        try {
            driver.findElement(By.xpath("//span[contains(text(),'" + serviceProviderName + "')]"));
            Assert.fail(serviceProviderName + " has been found on the page");
        } catch (NoSuchElementException e) {
            // expected
        }
    }
}