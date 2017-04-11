package com.example.almundo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

public class NoConcurrentCallsFromDispatcherTest {

	
	/**
	 * 10-OP, 1SUP, 1DIR trabajando de forma concurrente. 20 calls
	 */
	@Test
	public void checkAllAssignedOperators() {
		System.out.println("---------All Assigned Operators Test--------");
		//mismo tam que numero de llamadas producidas
		CallsQueue<Call> op = new CallsQueue<>(20);
		CallsQueue<Call> sup = new CallsQueue<>(3);
		CallsQueue<Call> dir = new CallsQueue<>(3);

		Dispatcher dispatcher = new Dispatcher(op, sup, dir);
		dispatcher.setNumberOfCalls(20);
		// no esperara para agregar una call a la cola, pero no de forma
		// concurrente
		dispatcher.setWaitProduction(false);

		ExecutorService executorDispatcher = Executors.newFixedThreadPool(1);
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		List<Employee> employees = new ArrayList<>();
		
		//consumo llamadas desde 10 threads diferentes(op) +1 sup + 1 dir.
		employees.add(new Operator("op1", op));
		employees.add(new Operator("op2", op));
		employees.add(new Operator("op3", op));
		employees.add(new Operator("op4", op));
		employees.add(new Operator("op5", op));
		employees.add(new Operator("op6", op));
		employees.add(new Operator("op7", op));
		employees.add(new Operator("op8", op));
		employees.add(new Operator("op9", op));
		employees.add(new Operator("op10", op));
		employees.add(new Supervisor("sup1", sup));
		employees.add(new Director("dir1", dir));
		executorDispatcher.execute(dispatcher);

		List<Future<List<Call>>> futures = new ArrayList<>();
		for (Employee e : employees) {
			futures.add(executorService.submit(e));
		}

		
		Utils.getPerformedCalls(futures);
		Utils.shutDownExecutors(executorDispatcher, executorService);
		Utils.showAsignedCalls(dispatcher);
		// todas las llamadas deben ser procesadas por los operadores
		Assert.assertTrue(dispatcher.getOperatorsCallsCount()==20);
	}
	/**
	 * 5-OP, 5-SUP, 5-DIR trabajando de forma concurrente. 20 calls
	 */
	@Test
	public void noConcurrentProducerConcurrentConsumers() {
		
		System.out.println("---------No Concurrent Producer/Concurrent Consumers Test--------");
		CallsQueue<Call> op = new CallsQueue<>(2);
		CallsQueue<Call> sup = new CallsQueue<>(4);
		CallsQueue<Call> dir = new CallsQueue<>(4);

		Dispatcher dispatcher = new Dispatcher(op, sup, dir);
		dispatcher.setNumberOfCalls(20);
		// no esperara para agregar una call a la cola, pero no de forma
		// concurrente
		dispatcher.setWaitProduction(false);

		ExecutorService executorDispatcher = Executors.newFixedThreadPool(1);
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		List<Employee> employees = new ArrayList<>();
		//5 operadores consumiran recursos concurrentemente
		employees.add(new Operator("op1", op));
		employees.add(new Operator("op2", op));
		employees.add(new Operator("op3", op));
		employees.add(new Operator("op4", op));
		employees.add(new Operator("op5", op));
		//5 supervisores consumiran recursos concurrentemente
		employees.add(new Supervisor("sup1", sup));
		employees.add(new Supervisor("sup2", sup));
		employees.add(new Supervisor("sup3", sup));
		employees.add(new Supervisor("sup4", sup));
		employees.add(new Supervisor("sup5", sup));
		//5 directores consumiran recursos concurrentemente
		employees.add(new Director("dir1", dir));
		employees.add(new Director("dir2", dir));
		employees.add(new Director("dir3", dir));
		employees.add(new Director("dir4", dir));
		employees.add(new Director("dir5", dir));
		executorDispatcher.execute(dispatcher);
		

		List<Future<List<Call>>> futures = new ArrayList<>();
		for (Employee e : employees) {
			futures.add(executorService.submit(e));
		}

		Utils.getPerformedCalls(futures);
		Utils.shutDownExecutors(executorDispatcher, executorService);
		Utils.showAsignedCalls(dispatcher);
		
		//cantidad de llamadas procesadas = 20
		Assert.assertTrue(dispatcher.getOperatorsCallsCount()+dispatcher.getSupervisorCallsCount()+dispatcher.getDirectorsCallsCount()==20);
	}
}
