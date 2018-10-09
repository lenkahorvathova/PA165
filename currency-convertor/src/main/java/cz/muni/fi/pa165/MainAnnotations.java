package cz.muni.fi.pa165;

import cz.muni.fi.pa165.currency.CurrencyConvertor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

public class MainAnnotations {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"src/Beam.xml"});

        CurrencyConvertor obj = (CurrencyConvertor) context.getBean("currencyConvertor");
        Currency CZK = Currency.getInstance("CZK");
        Currency EUR = Currency.getInstance("EUR");
        System.out.println(obj.convert(EUR, CZK, new BigDecimal("1")));
    }
}
