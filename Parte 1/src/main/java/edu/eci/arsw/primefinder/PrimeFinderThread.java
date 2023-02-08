package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrimeFinderThread extends Thread{

    private boolean awake;
    private Control control;
	int a,b;
	
	private List<Integer> primes;
	
	public PrimeFinderThread(int a, int b, Control control) {
		super();
        this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
        this.control = control;
        awake =true;
	}

    @Override
	public void run(){
        for (int i= a;i < b;i++){
            if(!awake){
                try{
                    synchronized(control){
                        control.wait();
                    }
                }catch(InterruptedException e){
                    Logger.getLogger(PrimeFinderThread.class.getName()).log(Level.SEVERE, null, e);
                }
            }						
            if (isPrime(i)){
                primes.add(i);
                System.out.println(i);

                if(i == b){
                    control.isOver();
                }
            }
        }
	}
	
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public List<Integer> getPrimes() {
		return primes;
	}

    /**
     * Metodo que se encarga de dormir al hilo
     */
    public void sleep(){
        awake = false;
    }


    /**
     * Metodo que se encarga de despertar el hilo
     */
    public void wakeUp(){
        awake = true;
    }

    /**
     * Metodo que retorna la cantidad de primos encontrados en cada hilo
     * @return Cantidad de primos encontrados
     */
    public int getPrimesFound(){
        return primes.size();
    }


    /**
     * Metodo que muestra si el hilo esta esperando
     * @return Estado del hilo
     */
    public boolean waiting(){
        return this.getState() == Thread.State.WAITING;
    }


	
}
