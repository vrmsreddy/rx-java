package com.ms.reactive;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import yahoofinance.Stock;

/**
 * @author msreddy
 *
 */
public class ReactiveResource {
	private static final Logger LOG = LoggerFactory.getLogger(ReactiveResource.class);

	public static void main(String[] args) {

		Executor executor = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(2));
		Scheduler s = Schedulers.from(executor);

		Observable<Stock> stockQuote = new ReactiveResource().getStockQuote();
		LOG.info("Going to Subscribe");
		stockQuote.subscribe(ReactiveResource::callBack, ReactiveResource::errorCallBack,
				ReactiveResource::completeCallBack);

		LOG.info("Processing completed");

	}

	private static void completeCallBack() {
		LOG.info("completeCallBack:: Completed Successfully");
	}

	private static void errorCallBack(Throwable throwable) {
		LOG.error("errorCallBack:: " + throwable);
	}

	private static Action1 callBack(Stock stock) {

		LOG.info(String.format("callBack:: Quote: %s, Price: %s, Day's High: %s, " + "Day's Low: %s", stock.getSymbol(),
				stock.getQuote().getPrice(), stock.getQuote().getDayHigh(), stock.getQuote().getDayLow()));
		return null;
	}

	private Observable<Stock> getStockQuote() {
		return new StockService().getStock();
	}
}
