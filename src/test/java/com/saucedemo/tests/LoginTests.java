package com.saucedemo.tests;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {
    @Test(description = "Positive: Login with standard_user")
    public void loginStandardUser() {
        var login = new LoginPage(getDriver());
        login.open();
        login.login("standard_user", "secret_sauce");
        var products = new ProductsPage(getDriver());
        Assert.assertEquals(products.getTitle(), "Products", "Products page title should be visible");
    }

    @Test(description = "Negative: Login with wrong password shows error")
    public void loginWrongPassword() {
        var login = new LoginPage(getDriver());
        login.open();
        login.login("standard_user", "wrong_password");
        String err = login.getError();
        Assert.assertTrue(err.toLowerCase().contains("username and password do not match"), "Error message must indicate bad credentials");
    }

    @Test(description = "Negative: Locked out user cannot login")
    public void lockedOutUser() {
        var login = new LoginPage(getDriver());
        login.open();
        login.login("locked_out_user", "secret_sauce");
        String err = login.getError();
        Assert.assertTrue(err.toLowerCase().contains("locked out"), "Locked out user should see locked out message");
    }
}
