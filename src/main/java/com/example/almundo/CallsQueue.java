package com.example.almundo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("hiding")
/**
 * 
 * @author mvizzolini
 * @param <Call>
 *            Clase encargada de manejar internamente los pedidos, implementada
 *            mediante una BlockingQueue
 */
public class CallsQueue<Call> {
	private BlockingQueue<Call> queue;
	private Boolean continueProducing = Boolean.TRUE;

	public CallsQueue(Integer size) {
		this.queue = new ArrayBlockingQueue<Call>(size);
	}

	public void put(Call call) {
		try {
			this.queue.put(call);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean offer(Call call) {
		return this.queue.offer(call);
	}

	public Call get() {
		try {
			return this.queue.poll(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setContinueProducing(boolean continueProducing) {
		this.continueProducing = continueProducing;
	}

	public boolean getContinueProducing() {
		return this.continueProducing;
	}

	public int getSize() {
		return this.queue.size();
	}
}