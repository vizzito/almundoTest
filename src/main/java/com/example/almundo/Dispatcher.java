package com.example.almundo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author mvizzolini clase encargada de procesar las llamadas entrantes se
 *         cuenta con 3 tipos diferentes de colas, una para cada tipo de
 *         empleado
 *
 */
public class Dispatcher implements Runnable {

	private CallsQueue<Call> operator;
	private CallsQueue<Call> supervisor;
	private CallsQueue<Call> director;
	private AtomicInteger operatorsCallsCount;
	private AtomicInteger supervisorCallsCount;
	private AtomicInteger directorsCallsCount;
	private AtomicInteger callNumber;

	private boolean waitProduction;
	private Integer numberOfCalls;

	public boolean isWaitProduction() {
		return waitProduction;
	}

	public void setWaitProduction(boolean waitProduction) {
		this.waitProduction = waitProduction;
	}

	public Dispatcher(CallsQueue<Call> operator, CallsQueue<Call> supervisor, CallsQueue<Call> director) {
		this.director = director;
		this.supervisor = supervisor;
		this.operator = operator;
		operatorsCallsCount = new AtomicInteger(0);
		supervisorCallsCount = new AtomicInteger(0);
		directorsCallsCount = new AtomicInteger(0);
		callNumber = new AtomicInteger(0);
	}

	private void dispatchCall(Call call) {
		// el metodo offer, intentara asignar la llamada a la cola de
		// operadores,
		// en caso que esta se encuentre llena, tratara de asignarla a la
		// siguiente en prioridad.
		if (!operator.offer(call)) {
			// si la cola de operadores esta llena, intentara asignar la llamada
			// a un supervisor
			System.out.println("---busy operators---");
			if (!supervisor.offer(call)) {
				// si la cola de supervisores esta llena, intentara asignar la
				// llamada a un director
				System.out.println("---busy supervisors---");
				if (!director.offer(call)) {
					System.out.println("---busy directors---");
					// si las 3 colas estan ocupadas, la llamada esperara
					// hasta que se libere un lugar en la cola de operadores
					System.out.println("waiting for operators queue.... " + call.getCallID());
					operator.put(call);
					operatorsCallsCount.incrementAndGet();
				} else {
					directorsCallsCount.incrementAndGet();
					System.out.println(call.getCallID() + " to director");
				}
			} else {
				supervisorCallsCount.incrementAndGet();
				System.out.println(call.getCallID() + " to supervisor");

			}
		} else {
			operatorsCallsCount.incrementAndGet();
			System.out.println(call.getCallID() + " to operator");
		}
	}

	@Override
	public void run() {
		for (int i = 0; i < getNumberOfCalls(); i++) {
			Call call = new Call("call" +callNumber.incrementAndGet());
			dispatchCall(call);
			// en caso de ser true, esperara entre 0 y 1 segundo (arbitrario)
			// en enviar la siguiente llamada a la cola correspondiente
			waitProduction(isWaitProduction());
		}
		// flag de finalizacion de llamadas
		supervisor.setContinueProducing(false);
		operator.setContinueProducing(false);
		director.setContinueProducing(false);
	}

	/**
	 * @param wait
	 *            wait if true and values betwee 0 and 1 seconds
	 */
	private void waitProduction(boolean waitProduction) {
		if (waitProduction) {
			try {
				int r = (int) (Math.random() * (10));
				Thread.sleep(r * 100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private Integer getNumberOfCalls() {
		return numberOfCalls;
	}

	public void setNumberOfCalls(Integer numberOfCalls) {
		this.numberOfCalls = numberOfCalls;
	}

	public Integer getSupervisorCallsCount() {
		return supervisorCallsCount.get();
	}

	public Integer getDirectorsCallsCount() {
		return directorsCallsCount.get();
	}

	public Integer getOperatorsCallsCount() {
		return operatorsCallsCount.get();
	}

}
