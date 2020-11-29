package me.jacobtread.iaf;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static me.jacobtread.iaf.IAFConstants.*;

public class IAF {


    public static final Logger LOGGER = Logger.getLogger("IAF");
    public static boolean DISABLE_LOGGING = false;

    static {
        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(new LogHandler());
        Path driverPath = DriverUtils.getDriver(false);
        if (driverPath == null) {
            System.exit(1);
        }
        System.setProperty("webdriver.gecko.driver", driverPath.toAbsolutePath().toString());
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
    }

    private final String username;
    private final String password;
    private final boolean isHeadless;
    private WebDriverWait waitTime;
    private WebDriver driver;
    private boolean isRunning;

    public IAF(String username, String password) {
        this(username, password, true);
    }

    /**
     * @param username   The username of the account to send the message to
     * @param password   The password of the account to send the message to
     * @param isHeadless Setting to false will display the firefox window while attacking
     */
    public IAF(String username, String password, boolean isHeadless) {
        this.username = username;
        this.password = password;
        this.isHeadless = isHeadless;
    }

    /**
     * Creates the driver instance and attaches proxy
     */
    public void load() {
        LOGGER.info("Creating driver instance");
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(isHeadless);
        driver = new FirefoxDriver(options);
        waitTime = new WebDriverWait(driver, WAIT_TIME);
        driver.manage().timeouts().setScriptTimeout(1, TimeUnit.SECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    /**
     * Starts the follow bot
     */
    public void run() {
        LOGGER.info(String.format("== Starting Bot [username=%s] [headless=%b] ==", username, isHeadless));
        load();
        LOGGER.info("Loading login");
        driver.get(BASE_URL + "/accounts/login/");
        waitLoaded();
        LOGGER.info("Attempting Login");
        waitTillPresent(USERNAME_SELECTOR, PASSWORD_SELECTOR, SUBMIT_SELECTOR);
        WebElement usernameElement = driver.findElement(USERNAME_SELECTOR);
        WebElement passwordElement = driver.findElement(PASSWORD_SELECTOR);
        WebElement submitElement = driver.findElement(SUBMIT_SELECTOR);
        usernameElement.click();
        usernameElement.sendKeys(username);
        passwordElement.click();
        passwordElement.sendKeys(password);
        submitElement.click();
        waitTime.withTimeout(Duration.ofSeconds(30)).until(webDriver -> !webDriver.getCurrentUrl().equals(BASE_URL + "/accounts/login/"));
        LOGGER.info("Login success");
        isRunning = true;
        loadAndFollow();
    }


    /**
     * Loads the suggested accounts page and goes down the list following each account
     * (Follows
     */
    public void loadAndFollow() {
        if (isRunning) {
            driver.get(BASE_URL + "/explore/people/suggested/");
            waitLoaded();
            waitTime.until((driver) -> driver.findElements(FOLLOW_BUTTONS_SELECTOR).size() > 0);
            List<WebElement> buttons = driver.findElements(FOLLOW_BUTTONS_SELECTOR);
            if (buttons.size() > 0) {
                for (WebElement button : buttons) {
                    WebElement wrapper = getParent(button, 2);
                    WebElement textElement = wrapper.findElement(By.xpath("./div[2]/div/div/span/a"));
                    LOGGER.info("Following: " + textElement.getText());
                    button.click();
                    long lastTime = System.currentTimeMillis();
                    while (isRunning) {
                        if (System.currentTimeMillis() - lastTime >= FOLLOW_DELAY * 1000) {
                            break;
                        }
                    }
                }
            }
            if (isRunning) {
                loadAndFollow();
            }
        }
    }

    public WebElement getParent(WebElement webElement, int nthAmount) {
        StringBuilder script = new StringBuilder("return arguments[0]");
        for (int i = 0; i < nthAmount; i++) {
            script.append(".parentNode");
        }
        script.append(';');
        return (WebElement) ((JavascriptExecutor) driver).executeScript(script.toString(), webElement);
    }

    /**
     * Stops the bot and shuts down the proxy server;
     */
    public void stop() {
        isRunning = false;
        driver.quit();
    }


    /**
     * Waits until the specific element is on the page
     *
     * @param by The element to wait for
     */
    private void waitTillPresent(By by) {
        waitTime.until(webDriver -> webDriver.findElements(by).size() > 0);
    }

    private void waitTillPresent(By... by) {
        for (By value : by) {
            waitTillPresent(value);
        }
    }

    /**
     * Waits until the page is fully loaded
     * (Uses JS to request document.readyState and completes
     */
    private void waitLoaded() {
        waitTime.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete")
        );
    }


}
