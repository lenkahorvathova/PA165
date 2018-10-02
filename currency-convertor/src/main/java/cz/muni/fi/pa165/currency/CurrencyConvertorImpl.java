package cz.muni.fi.pa165.currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;


/**
 * This is base implementation of {@link CurrencyConvertor}.
 *
 * @author petr.adamek@embedit.cz
 */
public class CurrencyConvertorImpl implements CurrencyConvertor {

    private final ExchangeRateTable exchangeRateTable;
    private final Logger logger = LoggerFactory.getLogger(CurrencyConvertorImpl.class);

    public CurrencyConvertorImpl(ExchangeRateTable exchangeRateTable) {
        this.exchangeRateTable = exchangeRateTable;
    }

    @Override
    public BigDecimal convert(Currency sourceCurrency, Currency targetCurrency, BigDecimal sourceAmount) {
        logger.trace("convert({},{},{})",sourceCurrency, targetCurrency, sourceAmount);

        if (sourceCurrency == null || targetCurrency == null || sourceAmount == null) {
            throw new IllegalArgumentException("Some Input data are missing. Please, check all values!");
        }
        try {
            BigDecimal exchangeRate = exchangeRateTable.getExchangeRate(sourceCurrency, targetCurrency);
            if (exchangeRate == null) {
                logger.warn("No exchange rate for these currencies.");
                throw new UnknownExchangeRateException("No exchange rate for these currencies.");
            }

            return exchangeRate.multiply(sourceAmount).setScale(2, RoundingMode.HALF_EVEN);

        } catch (ExternalServiceFailureException e) {
            logger.error("Error occured.");
            throw new UnknownExchangeRateException("Error occured.", e);
        }
    }

}
