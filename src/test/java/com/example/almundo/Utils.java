package com.example.almundo;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Utils {
	protected static void shutDownExecutors(ExecutorService executorDispatcher, ExecutorService executorService) {
		executorService.shutdown();
		executorDispatcher.shutdown();
		try {
			executorService.awaitTermination(600, TimeUnit.SECONDS);
			executorDispatcher.awaitTermination(600, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	protected static void showAsignedCalls(Dispatcher dispatcher) {
		System.out.println("Operator assigned calls: " + dispatcher.getOperatorsCallsCount());
		System.out.println("Supervisor assigned calls: " + dispatcher.getSupervisorCallsCount());
		System.out.println("Director assigned calls: " + dispatcher.getDirectorsCallsCount());
	}
	
	
	protected static void getPerformedCalls(List<Future<List<Call>>> futures) {
		for (Future<List<Call>> f : futures) {
			List<Call> calls;
			try {
				calls = f.get();
				for (Call c : calls) {
					System.out.println(c.getCallID() + "| duration: " + c.getDuration() + "| Call Performed by: "
							+ c.getEmployeeName());
				}
			} catch (InterruptedException | ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
}
