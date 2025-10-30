package com.saucedemo.tests;

import com.saucedemo.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SauceDemoTests extends BaseTest {

    @Test(description = "Positive: Add item to cart")
    public void addItemToCart() {
        var login = new LoginPage(getDriver());
        login.open();
        login.login("standard_user", "secret_sauce");
        var products = new ProductsPage(getDriver());
        products.addItemToCartByName("Sauce Labs Backpack");
        Assert.assertEquals(products.getCartCount(), 1, "Cart count should be 1 after adding");
    }

    @Test(description = "Positive: Complete checkout flow")
    public void completeCheckout() {
        var login = new LoginPage(getDriver());
        login.open();
        login.login("standard_user", "secret_sauce");
        var products = new ProductsPage(getDriver());
        products.addItemToCartByName("Sauce Labs Backpack");
        products.openCart();
        var cart = new CartPage(getDriver());
        cart.clickCheckout();
        var checkout = new CheckoutPage(getDriver());
        checkout.fillInfo("Ivan", "Ivanov", "12345");
        checkout.clickFinish();
        Assert.assertEquals(checkout.getCompleteHeader(), "Thank you for your order!", "Order should be completed");
    }

    @Test(description = "Negative: Attempt checkout without required fields")
    public void checkoutMissingInfo() {
        var login = new LoginPage(getDriver());
        login.open();
        login.login("standard_user", "secret_sauce");
        var products = new ProductsPage(getDriver());
        products.addItemToCartByName("Sauce Labs Backpack");
        products.openCart();
        var cart = new CartPage(getDriver());
        cart.clickCheckout();
        var checkout = new CheckoutPage(getDriver());
        checkout.clickContinue();
        String err = checkout.getError();
        Assert.assertTrue(err.toLowerCase().contains("first name"), "Should require first name");
    }
}
