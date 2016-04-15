package com.build.coordination;

import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;

import rx.Single;
import rx.SingleSubscriber;

public class AsyncUtils {
	
	public static <T> Single<T> fromResponseEntityFuture(final ListenableFuture<ResponseEntity<T>> future) {
		return Single.create(new Single.OnSubscribe<T>() {

			@Override
			public void call(final SingleSubscriber<? super T> subscriber) {
				future.addCallback(c -> {
					try {
						subscriber.onSuccess(future.get().getBody());
					} catch (Exception e) {
						subscriber.onError(e);
					}
				}, e -> {
					subscriber.onError(e);
				});
				
			}
		});
	}
	
	public static Single<Void> fromVoidFuture(final ListenableFuture<?> future) {
		return Single.create(new Single.OnSubscribe<Void>() {
			
			@Override
			public void call(final SingleSubscriber<? super Void> subscriber) {
				future.addCallback(c -> {
					try {
						future.get();
						subscriber.onSuccess(null);
					} catch (Exception e) {
						subscriber.onError(e);
					}
				}, e -> {
					subscriber.onError(e);
				});
			}
		});
	}

}
