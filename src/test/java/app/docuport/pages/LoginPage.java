package app.docuport.pages;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.WebElement;
public class LoginPage extends BasePage{

    @FindBy(xpath = "//label[.='Username or email']//following-sibling::input")
    public WebElement usernameInput;
    @FindBy(xpath = "//input[@type='password']")
    public WebElement passwordInput;
    @FindBy(xpath = "//button[@type='submit']")
    public WebElement loginButton;



    public void login (String userEmail, String userPassword) {
        usernameInput.sendKeys(userEmail);
        passwordInput.sendKeys(userPassword);
        loginButton.click();

    }

}
