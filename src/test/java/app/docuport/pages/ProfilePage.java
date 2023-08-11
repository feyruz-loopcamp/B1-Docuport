package app.docuport.pages;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.WebElement;
public class ProfilePage extends BasePage {


    @FindBy (xpath = "//h1[.='Profile']")
    public WebElement profileTitle;


    @FindBy (xpath = "//div[@class='mt-2 col col-12']")
    public WebElement fullName;


    @FindBy (xpath = "//div[@class='col col-12']/span")
    public WebElement role;







}
