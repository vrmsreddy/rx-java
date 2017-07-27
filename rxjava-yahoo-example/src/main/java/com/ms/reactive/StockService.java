package com.ms.reactive;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * @author msreddy
 *
 */
public class StockService {

	private static String[] quotes = {"AAPL", "GOOG", "INTC", "BABA", "TSLA", "AIR.PA"};
	private static Logger LOG = LoggerFactory.getLogger(StockService.class);
	
    Executor executor = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(100));
    Scheduler scheduler= Schedulers.from(executor);
    
    /**
     * @return
     */
    public Observable<Stock> getStock() {
		return Observable.from(quotes).flatMap(quote -> getStock(quote)
				.subscribeOn(scheduler))
				.onErrorResumeNext(Observable.just(null));
    }

    private void sleep(int i) {
        try {
        	LOG.info("Sleeping...");
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Stock getStock(String quote, Subscriber<? super Stock> subscriber) {

    	LOG.info("service:: Retrieve stock info for: " + quote);
        try {
        	
            if (quote.equals("GOOG")) {
                throw new IOException("Hye");
            }
            Thread.sleep(3000);
            return YahooFinance.get(quote);
            
        } catch (Exception e) {
            subscriber.onError(e);
        }
        return null;
    }
    
	private Observable<Stock> getStock(String quote) {
		Observable<Stock> stock = null;
		LOG.info("service:: Retrieve stock info for: " + quote);
		try {
			stock = Observable.just(YahooFinance.get(quote));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}
}
