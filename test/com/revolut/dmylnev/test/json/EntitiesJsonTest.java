package com.revolut.dmylnev.test.json;

import com.google.gson.JsonSyntaxException;
import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.entity.Activity;
import com.revolut.dmylnev.entity.ActivityType;
import org.junit.Assert;
import org.junit.Test;
import javax.annotation.Nonnull;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public class EntitiesJsonTest {

    @Test
    public void accountToJsonTest() {

        @Nonnull final Account account = new Account(1L, "USD", 123.7, 0L);

        @Nonnull final String json = account.toJson();
        @Nonnull final String str = account.toJson();

        Assert.assertEquals(json, str);

        @Nonnull final Account fromJson = Account.fromJson(json);

        Assert.assertNotNull(fromJson);
        Assert.assertEquals(account, fromJson);
    }

    @Test(expected = JsonSyntaxException.class)
    public void failAccountTest() {
        Account.fromJson("json");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failAccountTest2() {
        Account.fromJson("{ \"status\": \"ok\"}");
    }

    @Test
    public void activityToJsonTest() {

        @Nonnull final Activity activity = new Activity(3,ActivityType.DEPOSIT, "EUR", 1.13, 2, null);

        @Nonnull final String json = activity.toJson();
        @Nonnull final String str = activity.toJson();

        Assert.assertEquals(json, str);

        @Nonnull final Activity fromJson = Activity.fromJson(json);

        Assert.assertNotNull(fromJson);
        Assert.assertEquals(activity, fromJson);
    }

    @Test(expected = JsonSyntaxException.class)
    public void failActivityTest() {
        Activity.fromJson("json");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failActivityTest2() {
        Activity.fromJson("{ \"status\": \"ok\"}");
    }

}

