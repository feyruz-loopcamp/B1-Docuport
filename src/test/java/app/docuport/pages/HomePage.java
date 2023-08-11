package app.docuport.pages;

import app.docuport.utilities.Driver;
import io.cucumber.java.en_old.Ac;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class HomePage extends BasePage {

    @FindBy (xpath = "//button[@role='button']")
    public WebElement selfButton;

    @FindBy (linkText = "Profile")
    public WebElement profileButton;

    @FindBy (xpath = "(//span[@class='v-btn__content'])[2]")
    public WebElement hamburgerButton;


    //List<WebElement> sideNavigationBar;

    //List<WebElement> selfDropDownList;


    public void goToProfilePage() {

        Actions actions = new Actions(Driver.getDriver());
        actions.moveToElement(selfButton).perform();
        selfButton.click();
        profileButton.click();

    }







}
