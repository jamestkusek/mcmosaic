import com.personal.james.AppGUI;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class AppGUITest {

    @Test
    public void generateGUITest() {
        JFrame frame = AppGUI.generate();
        assertNotNull(frame);
        assertTrue(frame.isBackgroundSet());
        assertTrue(frame.isMaximumSizeSet());
        assertTrue(frame.isMinimumSizeSet());
        assertTrue(frame.getComponents().length != 0);
    }

    @Test
    public void updateFeedbackTest() {
        AppGUI.updateFeedback("this is a test");
        assertEquals("this is a test",AppGUI.statusMessage.getText());

    }
}
