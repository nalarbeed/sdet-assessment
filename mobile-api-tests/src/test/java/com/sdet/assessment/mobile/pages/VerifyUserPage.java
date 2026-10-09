package com.sdet.assessment.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

import java.util.LinkedHashMap;
import java.util.Map;

public class VerifyUserPage extends BasePage {

    private static final String PKG = "io.selendroid.testapp";

    private static final By HEADING = By.xpath("//android.widget.TextView[@text='Verify user']");
    private static final By REGISTER = By.id(PKG + ":id/buttonRegisterUser");

    private static final Map<String, By> DATA = new LinkedHashMap<>();

    static {
        DATA.put("Name", By.id(PKG + ":id/label_name_data"));
        DATA.put("Username", By.id(PKG + ":id/label_username_data"));
        DATA.put("Password", By.id(PKG + ":id/label_password_data"));
        DATA.put("E-Mail", By.id(PKG + ":id/label_email_data"));
        DATA.put("Programming Language", By.id(PKG + ":id/label_preferedProgrammingLanguage_data"));
        DATA.put("I accept adds", By.id(PKG + ":id/label_acceptAdds_data"));
    }

    public VerifyUserPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isDisplayed(HEADING, 5);
    }

    public String valueOf(String field) {
        By by = DATA.get(field);
        if (by == null) {
            throw new IllegalArgumentException("Unknown verify field: " + field);
        }
        return getText(by);
    }

    public void register() {
        click(REGISTER);
    }
}
