package com.example.almundo;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;

public abstract class Employee implements Callable<List<Call>> {
	private static final Integer MIN_TIME = 5;
	private static final Integer MAX_TIME = 11;
	private CallsQueue<Call> queue;
	private String name;

	public Employee(String name, CallsQueue<Call> queue) {
		this.queue = queue;
		this.name = name;
	}

	/**
	 * 
	 * @return callDuration aleatoreamente pondra a dormir el thread en un valor
	 *         entre 5 y 10 seg.
	 */
	public Integer takeCall() {
		try {
			int callDuration = (int) (Math.random() * (MAX_TIME - MIN_TIME)) + MIN_TIME;
			Thread.sleep(callDuration * 1000);
			return callDuration;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
/**
 * implementacion de la interfaz Callable
 * retorna la lista de llamadas realizadas por cada empleado.
 * 
 */
	@Override
	public List<Call> call() {
		Vector<Call> performedCalls = new Vector<>();
		Call call = getQueue().get();
		while (getQueue().getContinueProducing() || call != null) {
			Integer duration = takeCall();
			if (call != null) {
				call.setCallID(call.getCallID()+" by: "+Thread.currentThread().getName());
				call.setDuration(duration);
				call.setEmployeeName(getName());
				performedCalls.add(call);
				call = getQueue().get();
			}
		}
		return performedCalls;
	}

	public String getName() {
		return this.name;
	}

	public CallsQueue<Call> getQueue() {
		return queue;
	}

}
