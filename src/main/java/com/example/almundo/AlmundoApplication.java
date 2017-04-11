package com.example.almundo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class AlmundoApplication {

	public static void main(String[] args) {

		CallsQueue<Call> op = new CallsQueue<>(5);
		CallsQueue<Call> sup = new CallsQueue<>(5);
		CallsQueue<Call> dir = new CallsQueue<>(5);

		Dispatcher dispatcher = new Dispatcher(op, sup, dir);
		dispatcher.setNumberOfCalls(10);
		dispatcher.setWaitProduction(false);

		ExecutorService executorDispatcher = Executors.newFixedThreadPool(10);
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		List<Employee> employees = new ArrayList<>();
		employees.add(new Operator("op1", op));
		employees.add(new Operator("op2", op));
		employees.add(new Operator("op3", op));
		employees.add(new Operator("op4", op));
		employees.add(new Operator("op5", op));
		employees.add(new Operator("op6", op));
		employees.add(new Supervisor("sup1", sup));
		employees.add(new Supervisor("sup2", sup));
		employees.add(new Director("dir1", dir));
		employees.add(new Director("dir2", dir));

		for (int i = 0; i < 5; i++) {
			executorDispatcher.execute(dispatcher);
		}

		List<Future<List<Call>>> futures = new ArrayList<>();
		for (Employee e : employees) {
			futures.add(executorService.submit(e));
		}

		getPerformedCalls(futures);
		shutDownExecutors(executorDispatcher, executorService);
		showAsignedCalls(dispatcher);
	}

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
