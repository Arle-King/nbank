package api.senior_autotest;

import org.assertj.core.api.SoftAssertions;
import org.example.BankWidget;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BaseTest {
    protected SoftAssertions softAssertions;

    @BeforeEach
    public void precondition() {
        this.softAssertions = new SoftAssertions();
    }

    @AfterEach
    public void postcondition() {
        softAssertions.assertAll();
    }
}
