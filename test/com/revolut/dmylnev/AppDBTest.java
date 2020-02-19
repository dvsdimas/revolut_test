package com.revolut.dmylnev;

import com.revolut.dmylnev.test.base.BaseDBTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */

public class AppDBTest extends BaseDBTest {

    private static final Logger log = LogManager.getLogger(BaseDBTest.class);

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue( true );
    }
}
