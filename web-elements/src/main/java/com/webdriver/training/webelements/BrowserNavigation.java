package com.webdriver.training.webelements;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class BrowserNavigation {

    static final long FOR_SECOND = 1000;

    //We can navigate in browser using WebDriver.Navigation interface
    //available methods: back(), forward(), refresh(), to(String), to(URL)
    //to get WebDriver.Navigation instance use navigate() method of WebDriver interface

    @Test
    public void navigate() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        WebDriver.Navigation navigator = driver.navigate();
        navigator.to("http://www.google.com");

        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("Selenium WebDriver");
        WebElement searchButton = driver.findElement(By.name("btnG"));
        searchButton.click();
        searchBox.clear();
        searchBox.sendKeys("Packt Publishing");
        searchButton.click();
        Thread.sleep(FOR_SECOND);
        driver.findElement(By.xpath("//a[text() = 'Packt Publishing']"));
        navigator.back();
        Thread.sleep(FOR_SECOND);
        driver.findElement(By.xpath("//a[text() = 'Selenium WebDriver']"));
        navigator.forward();
        Thread.sleep(FOR_SECOND);
        driver.findElement(By.xpath("//a[text() = 'Packt Publishing']"));
        navigator.refresh();
        Thread.sleep(FOR_SECOND);
        driver.findElement(By.xpath("//a[text() = 'Packt Publishing']"));
    }
}
