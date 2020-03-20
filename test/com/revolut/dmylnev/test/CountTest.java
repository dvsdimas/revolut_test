package com.revolut.dmylnev.test;

import com.revolut.dmylnev.Counter;
import org.junit.Assert;
import org.junit.Test;
import java.util.Map;

public class CountTest {

    @Test
    public void positiveStream() {

        Map<Character, Integer> map = Counter.countLetters("qwertyq".chars().mapToObj(i -> (char) i));

        Assert.assertNotNull(map);
        Assert.assertEquals(6, map.size());

        Assert.assertEquals(2, map.get('q').intValue());

        Assert.assertEquals(1, map.get('w').intValue());
        Assert.assertEquals(1, map.get('r').intValue());
        Assert.assertEquals(1, map.get('t').intValue());
        Assert.assertEquals(1, map.get('y').intValue());
        Assert.assertEquals(1, map.get('e').intValue());
    }

    @Test
    public void positive() {

        Map<Character, Integer> map = Counter.countLetters("qwertyq");

        Assert.assertNotNull(map);
        Assert.assertEquals(6, map.size());

        Assert.assertEquals(2, map.get('q').intValue());

        Assert.assertEquals(1, map.get('w').intValue());
        Assert.assertEquals(1, map.get('r').intValue());
        Assert.assertEquals(1, map.get('t').intValue());
        Assert.assertEquals(1, map.get('y').intValue());
        Assert.assertEquals(1, map.get('e').intValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNPE() {
        Counter.countLetters((String) null);
    }

    @Test
    public void testEmpty() {

        Map<Character, Integer> map = Counter.countLetters("");

        Assert.assertNotNull(map);
        Assert.assertEquals(0, map.size());
    }

}
