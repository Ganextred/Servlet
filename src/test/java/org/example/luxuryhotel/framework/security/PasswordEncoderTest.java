package org.example.luxuryhotel.framework.security;


import org.junit.Assert;
import org.junit.jupiter.api.Test;


class PasswordEncoderTest {

    @Test
    void encodeSHA256() {
        Assert.assertEquals(PasswordEncoder.encode("Lorem ipsum"),"ﾩﾦix￳xElﾁﾏﾸﾣ￧ￆﾭ=,ﾃ￦'$ￌﾽ￪{6%?ﾸ￟^￝");
    }
}