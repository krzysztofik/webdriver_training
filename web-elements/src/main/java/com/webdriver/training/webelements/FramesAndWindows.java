package com.webdriver.training.webelements;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FramesAndWindows {

    //The WebDriver.TargetLocator interface is used to locate a given frame or window
    @Test
    public void switchWindows() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get(getClass().getResource("/windows/Window.html").toString());

        //Every time you open a web page using WebDriver in a browser window, WebDriver assigns a window handle to that
        String window1 = driver.getWindowHandle();
        System.out.println("First Window Handle is: "+window1);

        WebElement link = driver.findElement(By.linkText("Google Search"));
        link.click();
        Thread.sleep(5000);

        //not true, focus on new window does not mean that getWindowHandle return handle for that window
        //actually you have to switch to new window before making any action on it

        assertThat(driver.getWindowHandles().size(), is(2));

        driver.getWindowHandles().forEach(handle -> {
                    if (!handle.equals(window1))
                        driver.switchTo().window(handle);
            }
        );

        String window2 = driver.getWindowHandle();
        System.out.println("Second Window Handle is: "+window2);
        driver.findElement(By.name("q"));

        //driver.switchTo() return WebDriver.TargetLocator instance, RemoteWebDriver.RemoteTargetLocator is only available implementation
        //window method switch the focus of future commands for this driver to the window with the given name/handle.
        driver.switchTo().window(window1);

    }

    @Test
    public void frames() {
        WebDriver driver = new FirefoxDriver();
        driver.get(getClass().getResource("/windows/Frames.html").toString());

        //method frame is overloaded, possible location by index, id, name, etc
        driver.switchTo().frame(0);
        WebElement txt1 = driver.findElement(By.name("1"));
        txt1.sendKeys("I'm Frame One located by index");

        driver.switchTo().defaultContent();

        driver.switchTo().frame(1);
        WebElement txt2 = driver.findElement(By.name("2"));
        txt2.sendKeys("I'm Frame Two");

        driver.switchTo().defaultContent();

        driver.switchTo().frame("frameOne");
        txt1.clear();
        txt1.sendKeys("I'm Frame One located by name");

        driver.switchTo().defaultContent();

        driver.switchTo().frame("two");
        txt2.clear();
        txt2.sendKeys("I'm Frame Two located by id");

        driver.switchTo().defaultContent();

        driver.switchTo().frame(driver.findElement(By.xpath("//frame[1]")));
        txt1.clear();
        txt1.sendKeys("I`m Frame One located by web element");
    }
}
