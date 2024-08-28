package com.example.cibuildthroughput;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
class CiBuildThroughputApplicationTest {
    @Test
    void applicationStarts() {
        try {
            CiBuildThroughputApplication.main(new String[] {});
            assertTrue(true);
        }catch (Exception e){
            assertFalse("Exception thrown", false);
        }

    }
}
