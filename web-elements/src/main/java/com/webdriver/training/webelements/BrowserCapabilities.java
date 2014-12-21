package com.webdriver.training.webelements;


import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BrowserCapabilities {

    @Test
    public void browserCapabilities() {

        //Capabilities is an interface with DesiredCapabilities as main implementation
        //https://code.google.com/p/selenium/wiki/DesiredCapabilities
        Map<String, Boolean> capabilitiesMap = new HashMap<>();
        capabilitiesMap.put("databaseEnabled", false);

        Capabilities capabilities = new DesiredCapabilities(capabilitiesMap);
        WebDriver driver = new FirefoxDriver(capabilities);
        driver.get("http://www.google.com");

        //Use HasCapabilities interface implemented by RemoteWebDriver to get information about supported capabilities
        System.out.println(((HasCapabilities) driver).getCapabilities().toString());
    }


    @Test
    public void screenshot() {

        //The TakesScreenShot capability is enabled in all of the browsers by default.
        // Because this is a read-only capability, a user doesn't have much say on toggling it

        WebDriver driver = new FirefoxDriver();
        driver.get("http://www.google.com");

        //The file to which the screenshot data is written is a temporary file and will be deleted as soon as the JVM exits.
        // So it is a good idea to copy the file before the test completes.
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        System.out.println(scrFile.getAbsolutePath());
    }
}
