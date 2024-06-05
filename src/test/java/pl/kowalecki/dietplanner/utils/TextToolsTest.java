package pl.kowalecki.dietplanner.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;


public class TextToolsTest {

//    @BeforeAll
//    void init(){
//    }
    @Test
    void emailShouldBeValid(){
        assertTrue(TextTools.isValidEmail("testtet@gmail.com"));
        assertTrue(TextTools.isValidEmail("234hg2ui3yh489fdsf@wp.pl"));
        assertTrue(TextTools.isValidEmail("sdfsdf28.0u3jnrmf@o2.pl"));
        assertTrue(TextTools.isValidEmail("fsdfjbh2i3hfi32f@yahoo.com"));
    }
    @Test
    void emailShouldNotBeValid(){
        assertFalse(TextTools.isValidEmail("emailwitohutcomain@"));
        assertFalse(TextTools.isValidEmail("weirddomain@domain..com"));
        assertFalse(TextTools.isValidEmail("plainaddress"));
        assertFalse(TextTools.isValidEmail("userdoubledddomain@domain@domain.com"));
        assertFalse(TextTools.isValidEmail(""));
        assertFalse(TextTools.isValidEmail(null));
    }

    @Test
    void textLengthIsOk(){
        assertTrue(TextTools.isTextLengthOk("", 0, 10));
        assertTrue(TextTools.isTextLengthOk("3424sdf", 0, 10));
        assertTrue(TextTools.isTextLengthOk("fjf83i102k", 0, 10));
    }
    @Test
    void textLengthIsNotOk(){
        assertFalse(TextTools.isTextLengthOk("", 1, 10));
        assertFalse(TextTools.isTextLengthOk("sd22", 5, 10));
        assertFalse(TextTools.isTextLengthOk("32hjf93hbf9s", 5, 10));
        assertFalse(TextTools.isTextLengthOk("34534dgfsdfs", 5, 10));
    }

    @Test
    void passwordPatternIsValid(){
        assertTrue(TextTools.passwordPatternValidate("!Pass23f"));
        assertTrue(TextTools.passwordPatternValidate("jsdhf2#1opA"));
        assertTrue(TextTools.passwordPatternValidate("Aa1!Aa132!"));
    }

    @Test
    void passwordPatternIsInvalid(){
        assertFalse((TextTools.passwordPatternValidate("Test")));
        assertFalse((TextTools.passwordPatternValidate("testtest1")));
        assertFalse((TextTools.passwordPatternValidate("")));
        assertFalse((TextTools.passwordPatternValidate("1234567")));
        assertFalse((TextTools.passwordPatternValidate("ToooooooooLongPass1!23123")));
    }
}
