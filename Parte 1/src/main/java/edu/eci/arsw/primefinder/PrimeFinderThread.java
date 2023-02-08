package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread {

	int a,b;
	boolean sleep;

	private List<Integer> primes;


	public PrimeFinderThread(int a, int b) {
		super();
		this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
	}

	/**
	 * Método que hace que cada hilo corra y que agrega los primos encontrados a una lista propia
	 */
	@Override
	public void run() {
		int i =0;
		for (i = a; i < b; i++) {
			if(sleep) {
				try {
					this.sleep();
				}
				catch(Exception e) {System.out.println("error al dormir "+e);}
			}
			else {
				if (isPrime(i)) {
					primes.add(i);
				}
			}
		}
		
		

	}

	/**
	 * Método que determina si un número dado es primo
	 * @param n Número el cual se verificará si es primo o no
	 * @return Booleano que indica si n es primo o no
	 */
	boolean isPrime(int n) {
		boolean ans;
		if (n > 2) {
			ans = n % 2 != 0;
			for (int i = 3; ans && i * i <= n; i += 2) {
				ans = n % i != 0;
			}
		} else {
			ans = n == 2;
		}
		return ans;
	}

	/**
	 * Método que retorna la lista de primos encontrados
	 */
	public List<Integer> getPrimes() {
		return primes;
	}
	
	/**
	 * Método que duerme al thread
	 */
	public synchronized void sleep() {
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método que cambia el estado de la variable sleep 
	 */
	public void changeSleep() {
		sleep=true;
	}
	
	/**
	 * Método que devuelve la cantidad de numeros primos encontrados
	 * @return Tamaño del arreglo de primos encontrado
	 */
	public int getPrimesFound() {
		return primes.size();
	}
	
	/**
	 * Método que reactiva un thread
	 */
	public synchronized void wakeUp() {
		sleep=false;
		this.notifyAll();
	}

}