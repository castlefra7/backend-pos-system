package restaurantmanager;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class NumberManipulation {
    public static String currencyFormat(float amount) {
        Currency currency = Currency.getInstance("MGA");

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        numberFormat.setCurrency(currency);
        return numberFormat.format(amount);
    }
}
