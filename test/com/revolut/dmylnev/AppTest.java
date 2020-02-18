package com.revolut.dmylnev;

import com.revolut.dmylnev.test.BaseDbTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */

public class AppTest extends BaseDbTest {

    private static final Logger log = LogManager.getLogger(BaseDbTest.class);

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue( true );
    }
}
