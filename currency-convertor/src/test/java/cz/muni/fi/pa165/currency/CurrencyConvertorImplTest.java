package cz.muni.fi.pa165.currency;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CurrencyConvertorImplTest {

    private CurrencyConvertor currencyConvertor;

    private Currency EUR = Currency.getInstance("EUR");
    private Currency CZK = Currency.getInstance("CZK");

    @Mock
    private ExchangeRateTable mockedExchangeRateTable = mock(ExchangeRateTable.class);

    @Before
    public void init() {
        currencyConvertor = new CurrencyConvertorImpl(mockedExchangeRateTable);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testConvert() throws ExternalServiceFailureException {
        when(mockedExchangeRateTable.getExchangeRate(EUR, CZK)).thenReturn(new BigDecimal("25"));

        assertEquals(new BigDecimal("2500.00"), currencyConvertor.convert(EUR, CZK, new BigDecimal("100")));
        assertEquals(new BigDecimal("27.78"), currencyConvertor.convert(EUR, CZK, new BigDecimal("1.111")));
    }

    @Test
    public void testConvertWithNullSourceCurrency() {
        expectedException.expect(IllegalArgumentException.class);
        currencyConvertor.convert(null, CZK, BigDecimal.ONE);
    }

    @Test
    public void testConvertWithNullTargetCurrency() {
        expectedException.expect(IllegalArgumentException.class);
        currencyConvertor.convert(EUR, null, BigDecimal.ONE);
    }

    @Test
    public void testConvertWithNullSourceAmount() throws ExternalServiceFailureException {
        when(mockedExchangeRateTable.getExchangeRate(EUR, CZK)).thenReturn(null);

        expectedException.expect(IllegalArgumentException.class);
        currencyConvertor.convert(EUR, CZK, null);
    }

    @Test
    public void testConvertWithUnknownCurrency() {
        expectedException.expect(UnknownExchangeRateException.class);
        currencyConvertor.convert(EUR, CZK, BigDecimal.ONE);
    }

    @Test
    public void testConvertWithExternalServiceFailure() throws ExternalServiceFailureException {
        when(mockedExchangeRateTable.getExchangeRate(EUR, CZK)).thenThrow(ExternalServiceFailureException.class);

        expectedException.expect(UnknownExchangeRateException.class);
        currencyConvertor.convert(EUR, CZK, BigDecimal.ONE);
    }

}
