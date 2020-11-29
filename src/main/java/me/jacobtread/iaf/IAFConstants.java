package me.jacobtread.iaf;

import org.openqa.selenium.By;

public class IAFConstants {

    /**
     * The base path url of Instagram
     */
    public static final String BASE_URL = "https://www.instagram.com";
    /**
     * The version of the bot
     */
    public static final String VERSION = "1.0.0";


    public static final By USERNAME_SELECTOR = By.xpath("/html/body/div[1]/section/main/div/div/div[1]/div/form/div/div[1]/div/label/input");
    public static final By PASSWORD_SELECTOR = By.xpath("/html/body/div[1]/section/main/div/div/div[1]/div/form/div/div[2]/div/label/input");
    public static final By SUBMIT_SELECTOR = By.xpath("/html/body/div[1]/section/main/div/div/div[1]/div/form/div/div[3]/button");
    public static final By FOLLOW_BUTTONS_SELECTOR = By.cssSelector("div.Igw0E > div> button");

    /**
     * The time to wait before rechecking if elements have loaded (seconds)
     */
    public static final int WAIT_TIME = 5;

    /**
     * The time to wait between each follow (seconds)
     */
    public static final int FOLLOW_DELAY = 15;

}
