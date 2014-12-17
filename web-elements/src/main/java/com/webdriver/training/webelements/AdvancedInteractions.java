package com.webdriver.training.webelements;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AdvancedInteractions {

    WebDriver fireforDriver;
    WebDriver chromeDriver;

    @Before
    public void instantiateWebDriver() {
        fireforDriver = new FirefoxDriver();
    }

    @After
    public void quiteWebDriver() {
        fireforDriver.quit();
    }

    @Test
    public void actionBuildPerform() throws URISyntaxException, InterruptedException {
        //it does not work for firefox so used chrome instead
        chromeDriver = new ChromeDriver();
        chromeDriver.get(getClass().getResource("/selectable.html").toString());
        WebElement one = chromeDriver.findElement(By.name("one"));
        WebElement three = chromeDriver.findElement(By.name("three"));
        WebElement five = chromeDriver.findElement(By.name("five"));
        // Add all the actions into the Actions builder.
        Actions builder = new Actions(chromeDriver);
        builder.keyDown(Keys.CONTROL)
                .click(one)
                .click(three)
                .click(five)
                .keyUp(Keys.CONTROL);
        // Generate the composite action.
        Action compositeAction = builder.build();
        // Perform the composite action.
        compositeAction.perform();
        Thread.sleep(3000);
        chromeDriver.quit();
    }

    @Test
    public void moveToElementAndClickOrMoveByOffsetAndClick() throws URISyntaxException, InterruptedException {
        fireforDriver.get(getClass().getResource("/selectable.html").toString());
        WebElement three = fireforDriver.findElement(By.name("three"));
        WebElement twelve = fireforDriver.findElement(By.name("twelve"));
        Actions moveToElementBuilder = new Actions(fireforDriver);
        moveToElementBuilder.moveToElement(three);
        moveToElementBuilder.click();
        moveToElementBuilder.perform();

        Actions moveByOffsetBuilder = new Actions(fireforDriver);
        moveByOffsetBuilder.moveByOffset(three.getSize().getWidth(), three.getSize().getHeight() * 2).click();
        moveByOffsetBuilder.perform();
        assertThat(twelve.getAttribute("class"), is("ui-state-default ui-selectee ui-selected"));
        Thread.sleep(5000);
    }

    @Test
    public void ClickOnWebElement() throws URISyntaxException, InterruptedException {
        fireforDriver.get(getClass().getResource("/selectable.html").toString());
        WebElement one = fireforDriver.findElement(By.name("one"));
        WebElement eleven = fireforDriver.findElement(By.name("eleven"));
        WebElement five = fireforDriver.findElement(By.name("five"));
        Actions builder = new Actions(fireforDriver);

        builder.click(one);
        builder.build().perform();

        builder.click(eleven);
        builder.build().perform();

        builder.click(five);
        builder.build().perform();

        builder.click(one).click(eleven).click(five);
        builder.build().perform();
    }

    @Test
    public void clickHoldAndReleaseIt() throws URISyntaxException, InterruptedException {
        fireforDriver.get(getClass().getResource("/sortable.html").toString());
        WebElement three = fireforDriver.findElement(By.name("three"));
        Actions builder = new Actions(fireforDriver);
        //Move tile3 to the position of tile2
        builder.moveByOffset(200, 20)
                .clickAndHold()
                .moveByOffset(120, 0)
                .release()
                .perform();
        Thread.sleep(5000);

        builder.clickAndHold(three)
                .moveByOffset(120, 0)
                .release()
                .perform();
    }

    @Test
    public void clickAndHoldAndReleaseOnWebElement() throws URISyntaxException, InterruptedException {
        fireforDriver.get(getClass().getResource("/Sortable.html").toString());
        WebElement three = fireforDriver.findElement(By.name("three"));
        WebElement two = fireforDriver.findElement(By.name("two"));
        Actions builder = new Actions(fireforDriver);
        //Move tile3 to the position of tile2
        builder.clickAndHold(three)
                .release(two)
                .perform();
    }

    @Test
    public void dragMe () throws URISyntaxException {
        fireforDriver.get(getClass().getResource("/DragMe.html").toString());
        WebElement dragMe = fireforDriver.findElement(By.id("draggable"));
        Actions builder = new Actions(fireforDriver);
        builder.dragAndDropBy(dragMe, 300, 200).perform();
    }

    @Test
    public void DragAndDrop() throws URISyntaxException {
        fireforDriver.get(getClass().getResource("/DragAndDrop.html").toString());
        WebElement src = fireforDriver.findElement(By.id("draggable"));
        WebElement trgt = fireforDriver.findElement(By.id("droppable"));
        Actions builder = new Actions(fireforDriver);
        builder.dragAndDrop(src, trgt).perform();
    }

    @Test
    public void DoubleClick() throws URISyntaxException, InterruptedException {
        fireforDriver.get(getClass().getResource("/DoubleClick.html").toString());
        WebElement dblClick= fireforDriver.findElement(By.name("dblClick"));
        Actions builderA = new Actions(fireforDriver);
        builderA.moveToElement(dblClick).doubleClick().perform();
        Alert alert = fireforDriver.switchTo().alert();
        String textOnAlert = alert.getText();
        assertThat(textOnAlert, is(equalTo("Double Clicked !!")));
        alert.accept();

        Thread.sleep(5000);
        Actions builderB = new Actions(fireforDriver);
        builderB.doubleClick(dblClick).perform();
        fireforDriver.switchTo().alert().accept();
    }

    @Test
    public void ContextClick() throws URISyntaxException, InterruptedException {
        fireforDriver.get(getClass().getResource("/ContextClick.html").toString());
        WebElement contextMenu = fireforDriver.findElement(By.id("div-context"));
        Actions builder = new Actions(fireforDriver);
        builder.contextClick(contextMenu)
                .click(fireforDriver.findElement(By.name("Item 4")))
                .perform();
        Alert alert = fireforDriver.switchTo().alert();
        String textOnAlert = alert.getText();
        assertThat(textOnAlert, is(equalTo("Item 4 Clicked")));
        alert.accept();

        fireforDriver.navigate().refresh();
        Actions builder2 = new Actions(fireforDriver);
        builder2.moveToElement(fireforDriver.findElement(By.id("div-context"))) //after refresh cached contextMenu element was erased
                .contextClick()
                .click(fireforDriver.findElement(By.name("Item 2")))
                .perform();
        fireforDriver.switchTo().alert().accept();
    }

    //TODO tests for keyboard actions (keyDown/keyUp/sendKeys)
}
