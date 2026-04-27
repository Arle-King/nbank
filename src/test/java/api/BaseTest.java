package api;

import org.assertj.core.api.SoftAssertions;
import org.example.common.extensions.TimingExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TimingExtension.class)
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
