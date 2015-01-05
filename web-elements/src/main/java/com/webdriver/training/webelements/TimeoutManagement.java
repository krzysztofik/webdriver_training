package com.webdriver.training.webelements;


import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.concurrent.TimeUnit;

public class TimeoutManagement {

    //Webdriver.Timeouts interface is used to manages implicit timeouts
    //First we need reference to WebDriver.Options using WebDriver.manage() method
    //Then using Webdriver.Options.timeouts() method we receive reference to WebDriver.Timeouts instance

    //compare this test with test from BrowserNavigation class
    @Test
    public void navigateWithImplicitTimeout() {
        WebDriver driver = new ChromeDriver();
        WebDriver.Options options = driver.manage();
        WebDriver.Timeouts timeouts = options.timeouts();

        //amount of time the driver should wait when searching for an element if it is not immediately present.
        //driver should poll the page until the element has
        //been found, or this timeout expires before throwing a {@link NoSuchElementException}.
        timeouts.implicitlyWait(2, TimeUnit.SECONDS);

        //amount of time to wait for a page load to complete before throwing an error
        timeouts.pageLoadTimeout(10, TimeUnit.SECONDS);

        WebDriver.Navigation navigator = driver.navigate();
        navigator.to("http://www.google.com");

        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("Selenium WebDriver");
        WebElement searchButton = driver.findElement(By.name("btnG"));
        searchButton.click();
        searchBox.clear();
        searchBox.sendKeys("Packt Publishing");
        searchButton.click();
        driver.findElement(By.xpath("//a[text() = 'Packt Publishing']"));
        navigator.back();
        driver.findElement(By.xpath("//a[text() = 'Selenium WebDriver']"));
        navigator.forward();
        driver.findElement(By.xpath("//a[text() = 'Packt Publishing']"));
        navigator.refresh();
        driver.findElement(By.xpath("//a[text() = 'Packt Publishing']"));

        //Thanks to implicit wait we eliminated Thread.sleeps!!!!
    }


    //Unlike implicit timeouts which is part of selenium-api jar, there is also explicit timeout located in selenium-support jar
    //Explicit timeout can be used for particular web elements
    //Our main class is WebDriverWait which extends FluentWait<WebDriver> class
    //Because FluentWait<WebDriver> implements Wait<WebDriver> interface we can use T Wait.until(Function<? super WebDriver, T> isTrue) method to evaluate our expected condition in defined amount of time
    //We can use predefined conditions collected in ExpectedConditions class or create custom using ExpectedCondition interface
}
