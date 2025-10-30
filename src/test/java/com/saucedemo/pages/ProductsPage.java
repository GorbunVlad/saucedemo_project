package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By title = By.cssSelector(".header_secondary_container .title");
    private By shoppingCartBadge = By.cssSelector(".shopping_cart_badge");
    private By cartLink = By.cssSelector(".shopping_cart_link");
    private By inventoryItems = By.cssSelector(".inventory_item");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(title)).getText();
    }

    public void addItemToCartByName(String name) {
        // Wait for products page to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(title));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(inventoryItems));
        
        // Try to find by data-test attribute first (more reliable)
        String normalizedName = name.toLowerCase().replace(" ", "-");
        By btnByDataTest = By.cssSelector(String.format("button[data-test*='%s']", normalizedName));
        
        try {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnByDataTest));
            btn.click();
            return;
        } catch (Exception e) {
            // Continue to fallback
        }
        
        // Fallback: Find item container by name, then find button inside it
        By itemName = By.xpath(String.format(
            "//div[contains(@class,'inventory_item_name') and normalize-space()='%s']", name));
        
        WebElement itemNameElement = wait.until(ExpectedConditions.presenceOfElementLocated(itemName));
        WebElement inventoryItem = itemNameElement.findElement(By.xpath("./ancestor::div[contains(@class,'inventory_item')]"));
        WebElement btn = inventoryItem.findElement(By.tagName("button"));
        wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
    }

    public int getCartCount() {
        try {
            String t = wait.until(ExpectedConditions.visibilityOfElementLocated(shoppingCartBadge)).getText();
            return Integer.parseInt(t);
        } catch (Exception e) {
            return 0;
        }
    }

    public void openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
    }
}
