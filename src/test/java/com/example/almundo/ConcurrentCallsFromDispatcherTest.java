package com.example.almundo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

public class ConcurrentCallsFromDispatcherTest {
	/**
	 * 10 Dispatcher de calls. 2 calls por thread.
	 * 5-OP, 1-SUP, 1-DIR trabajando de forma concurrente. 20 calls
	 */
	@Test
	public void ConcurrentCallsMultiConsumersTest() {

		System.out.println("---------Concurrent Producer (10*2)/Concurrent Consumers Test--------");
		CallsQueue<Call> op = new CallsQueue<>(4);
		CallsQueue<Call> sup = new CallsQueue<>(3);
		CallsQueue<Call> dir = new CallsQueue<>(3);

		Dispatcher dispatcher = new Dispatcher(op, sup, dir);
		// numero de llamadas producidas por cada Thread!
		dispatcher.setNumberOfCalls(2);
		// no esperara para agregar una call a la cola, pero no de forma
		// concurrente
		dispatcher.setWaitProduction(false);
		// 10 llamadas de forma concurrente
		ExecutorService executorDispatcher = Executors.newFixedThreadPool(10);
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		List<Employee> employees = new ArrayList<>();
		// 5 operadores consumiran recursos concurrentemente
		employees.add(new Operator("op1", op));
		employees.add(new Operator("op2", op));
		employees.add(new Operator("op3", op));
		employees.add(new Operator("op4", op));
		employees.add(new Operator("op5", op));
		
		employees.add(new Supervisor("sup1", sup));
		employees.add(new Director("dir1", dir));

		//10 threads de llamadas
		for (int i = 0; i < 10; i++) {
			executorDispatcher.execute(dispatcher);
		}
		
		List<Future<List<Call>>> futures = new ArrayList<>();
		for (Employee e : employees) {
			futures.add(executorService.submit(e));
		}

		Utils.getPerformedCalls(futures);
		Utils.shutDownExecutors(executorDispatcher, executorService);
		Utils.showAsignedCalls(dispatcher);

		// cantidad de llamadas procesadas = 20
		Assert.assertTrue(dispatcher.getOperatorsCallsCount() + dispatcher.getSupervisorCallsCount()
				+ dispatcher.getDirectorsCallsCount() == 20);
	}
	/**
	 * 10 Dispatcher de calls. 10 calls por thread.
	 * 6-OP, 4-SUP, 2-DIR trabajando de forma concurrente. 100 calls
	 */
	@Test
	public void ConcurrentCallsMultiConsumersTest2(){
		System.out.println("---------Concurrent Producer (10*10)/Concurrent Consumers Test 2--------");

		CallsQueue<Call> op = new CallsQueue<>(10);
		CallsQueue<Call> sup = new CallsQueue<>(10);
		CallsQueue<Call> dir = new CallsQueue<>(10);

		Dispatcher dispatcher = new Dispatcher(op, sup, dir);
		dispatcher.setNumberOfCalls(10);
		dispatcher.setWaitProduction(false);

		ExecutorService executorDispatcher = Executors.newFixedThreadPool(10);
		
		//un thread para cada empleado
		ExecutorService executorService = Executors.newFixedThreadPool(12);

		List<Employee> employees = new ArrayList<>();
		// 6 operadores consumiran recursos concurrentemente
		employees.add(new Operator("op1", op));
		employees.add(new Operator("op2", op));
		employees.add(new Operator("op3", op));
		employees.add(new Operator("op4", op));
		employees.add(new Operator("op5", op));
		employees.add(new Operator("op6", op));
		// 4 supervisores consumiran recursos concurrentemente
		employees.add(new Supervisor("sup1", sup));
		employees.add(new Supervisor("sup2", sup));
		employees.add(new Supervisor("sup3", sup));
		employees.add(new Supervisor("sup4", sup));
		// 2 directores consumiran recursos concurrentemente
		employees.add(new Director("dir1", dir));
		employees.add(new Director("dir2", dir));

		//10 llamadas por thread
		for (int i = 0; i < 10; i++) {
			executorDispatcher.execute(dispatcher);
		}

		List<Future<List<Call>>> futures = new ArrayList<>();
		for (Employee e : employees) {
			futures.add(executorService.submit(e));
		}
		
		Utils.getPerformedCalls(futures);
		Utils.shutDownExecutors(executorDispatcher, executorService);
		Utils.showAsignedCalls(dispatcher);

		// cantidad de llamadas procesadas = 100
		Assert.assertTrue(dispatcher.getOperatorsCallsCount() + dispatcher.getSupervisorCallsCount()
				+ dispatcher.getDirectorsCallsCount() == 100);

	}
}
