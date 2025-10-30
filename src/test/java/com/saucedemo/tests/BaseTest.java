package com.saucedemo.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.*;
import java.time.Duration;

public class BaseTest {
    protected ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @Parameters({"browser"})
    @BeforeClass(alwaysRun = true)
    public void setupClass(@Optional("chrome") String browser) {
        if (browser == null || browser.isEmpty()) {
            browser = "chrome"; // default browser
        }
        
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                break;
            case "edge":
            case "msedge":
                try {
                    WebDriverManager.edgedriver().setup();
                } catch (Exception e) {
                    // Fallback: Selenium Manager will handle Edge driver automatically
                    System.out.println("Warning: Could not setup EdgeDriver via WebDriverManager. Using Selenium Manager fallback.");
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser + ". Supported browsers: chrome, edge");
        }
    }

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setup(@Optional("chrome") String browser) {
        if (browser == null || browser.isEmpty()) {
            browser = "chrome"; // default browser
        }
        
        WebDriver wd;
        
        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--window-size=1920,1080");
                wd = new ChromeDriver(chromeOptions);
                break;
            case "edge":
            case "msedge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--headless=new");
                edgeOptions.addArguments("--window-size=1920,1080");
                wd = new EdgeDriver(edgeOptions);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser + ". Supported browsers: chrome, edge");
        }
        
        wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // rely on explicit waits
        driver.set(wd);
    }

    protected WebDriver getDriver() {
        return driver.get();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }
}
