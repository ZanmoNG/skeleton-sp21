import org.junit.Test;
import static org.junit.Assert.*;


public class TestEqual {
    // test the equal function


    @Test
    public void checkNonEqual(){
        bear b1 = new bear (2);
        bear b2 = new bear (1);
        bear b3 = new bear (2);

        assertNotEquals(b1, b2);
    }

    @Test
    public void checkEqual(){
        bear b1 = new bear (2);
        bear b3 = new bear (2);

        assertEquals(b1, b3);
    }



}
