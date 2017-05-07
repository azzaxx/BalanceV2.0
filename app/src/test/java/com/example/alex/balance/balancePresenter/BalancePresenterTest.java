package com.example.alex.balance.balancePresenter;

import android.support.annotation.NonNull;

import com.example.alex.balance.dagger.presenters.BalancePresenter;
import com.example.alex.balance.views.BalanceFragment;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BalancePresenterTest {
    private BalancePresenter presenter = new BalancePresenter(new ViewClass(), null);

    @Test
    public void addOneTest() {
        presenter.addOne("1", "1990");
    }

    public static class ViewClass extends BalanceFragment {
        @Override
        public void setTotalSum(@NonNull String total) {
            assertEquals(total, "199.01");
        }
    }
}
