package org.example.luxuryhotel.framework.util;




import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static junit.framework.TestCase.assertEquals;
import static org.example.luxuryhotel.framework.Util.Converter.convert;


class ConverterTest {
    @Test
    public void convertFromString(){
        assertEquals(convert("123", Integer.class), 123);
        assertEquals(convert("123", Long.class), 123L);
        assertEquals(convert("12.3", Double.class), 12.3);
        assertEquals(convert("true", Boolean.class), true);
    }

}