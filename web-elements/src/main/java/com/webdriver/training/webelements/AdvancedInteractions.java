package com.webdriver.training.webelements;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AdvancedInteractions {

    WebDriver driver;
    URL resource_selectable;
    URL resource_sortable;
    URL resource_drag;
    URL resource_dragAndDrop;
    URL resource_doubleClick;
    URL resource_contextClick;

    @Before
    public void before() {
        driver = new FirefoxDriver();
        resource_selectable = getClass().getResource("/selectable.html");
        resource_sortable = getClass().getResource("/Sortable.html");
        resource_drag = getClass().getResource("/DragMe.html");
        resource_doubleClick = getClass().getResource("/DoubleClick.html");
        resource_contextClick = getClass().getResource("/ContextClick.html");
    }

    @After
    public void after() {
        driver.quit();
    }

    @Test
    public void actionBuildPerform() throws URISyntaxException {

        driver.get(Paths.get(resource_selectable.toURI()).toString());
        WebElement one = driver.findElement(By.name("one"));
        WebElement three = driver.findElement(By.name("three"));
        WebElement five = driver.findElement(By.name("five"));
        // Add all the actions into the Actions builder.
        Actions builder = new Actions(driver);
        builder.keyDown(Keys.CONTROL)
                .click(one)
                .click(three)
                .click(five)
                .keyUp(Keys.CONTROL);
        // Generate the composite action.
        Action compositeAction = builder.build();
        // Perform the composite action.
        compositeAction.perform();
    }

    @Test
    public void moveToElementAndClickOrMoveByOffsetAndClick() throws URISyntaxException, InterruptedException {

        driver.get(Paths.get(resource_selectable.toURI()).toString());
        WebElement three = driver.findElement(By.name("three"));
        WebElement twelve = driver.findElement(By.name("twelve"));
        Actions moveToElementBuilder = new Actions(driver);
        moveToElementBuilder.moveToElement(three);
        moveToElementBuilder.click();
        moveToElementBuilder.perform();

        Actions moveByOffsetBuilder = new Actions(driver);
        moveByOffsetBuilder.moveByOffset(three.getSize().getWidth(), three.getSize().getHeight() * 2).click();
        moveByOffsetBuilder.perform();
        assertThat(twelve.getAttribute("class"), is("ui-state-default ui-selectee ui-selected"));
        Thread.sleep(5000);
    }

    @Test
    public void ClickOnWebElement() throws URISyntaxException, InterruptedException {

        driver.get(Paths.get(resource_selectable.toURI()).toString());
        WebElement one = driver.findElement(By.name("one"));
        WebElement eleven = driver.findElement(By.name("eleven"));
        WebElement five = driver.findElement(By.name("five"));
        Actions builder = new Actions(driver);

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
        driver.get(Paths.get(resource_sortable.toURI()).toString());
        WebElement three = driver.findElement(By.name("three"));
        Actions builder = new Actions(driver);
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
        driver.get(Paths.get(resource_sortable.toURI()).toString());
        WebElement three = driver.findElement(By.name("three"));
        WebElement two = driver.findElement(By.name("two"));
        Actions builder = new Actions(driver);
        //Move tile3 to the position of tile2
        builder.clickAndHold(three)
                .release(two)
                .perform();
    }

    @Test
    public void dragMe () throws URISyntaxException {
        driver.get(Paths.get(resource_drag.toURI()).toString());
        WebElement dragMe = driver.findElement(By.id("draggable"));
        Actions builder = new Actions(driver);
        builder.dragAndDropBy(dragMe, 300, 200).perform();
    }

    @Test
    public void DragAndDrop() throws URISyntaxException {
        driver.get(Paths.get(resource_dragAndDrop.toURI()).toString());
        WebElement src = driver.findElement(By.id("draggable"));
        WebElement trgt = driver.findElement(By.id("droppable"));
        Actions builder = new Actions(driver);
        builder.dragAndDrop(src, trgt).perform();
    }

    @Test
    public void DoubleClick() throws URISyntaxException, InterruptedException {
        driver.get(Paths.get(resource_doubleClick.toURI()).toString());
        WebElement dblClick= driver.findElement(By.name("dblClick"));
        Actions builderA = new Actions(driver);
        builderA.moveToElement(dblClick).doubleClick().perform();
        Alert alert = driver.switchTo().alert();
        String textOnAlert = alert.getText();
        assertThat(textOnAlert, is(equalTo("Double Clicked !!")));
        alert.accept();

        Thread.sleep(5000);
        Actions builderB = new Actions(driver);
        builderB.doubleClick(dblClick).perform();
        driver.switchTo().alert().accept();
    }

    @Test
    public void ContextClick() throws URISyntaxException, InterruptedException {
        driver.get(Paths.get(resource_contextClick.toURI()).toString());
        WebElement contextMenu = driver.findElement(By.id("div-context"));
        Actions builder = new Actions(driver);
        builder.contextClick(contextMenu)
                .click(driver.findElement(By.name("Item 4")))
                .perform();
        Alert alert = driver.switchTo().alert();
        String textOnAlert = alert.getText();
        assertThat(textOnAlert, is(equalTo("Item 4 Clicked")));
        alert.accept();

        driver.navigate().refresh();
        Actions builder2 = new Actions(driver);
        builder2.moveToElement(driver.findElement(By.id("div-context"))) //after refresh cached contextMenu element was erased
                .contextClick()
                .click(driver.findElement(By.name("Item 2")))
                .perform();
        driver.switchTo().alert().accept();
    }

    //TODO tests for keyboard actions (keyDown/keyUp/sendKeys)
}
