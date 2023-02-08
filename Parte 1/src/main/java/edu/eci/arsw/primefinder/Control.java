/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 300000;
    private final static int TMILISECONDS = 3000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    private Control control;
    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];
        control = this;
        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA, this);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1, this);
    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }
        while(true){
            try{
                System.out.println("Ëntro");
                sleep(TMILISECONDS);

                //Detener todos los hilos
                for(int i = 0; i < NTHREADS; i++){
                    pft[i].sleep();
                }

                System.out.println("Se han encontrado: "+primesFound()+ " numeros primos. Presione enter para continuar.");
                Scanner enter = new Scanner(System.in);
                enter.nextLine();

                //
                for(int i = 0; i <NTHREADS; i++){
                    pft[i].wakeUp();
                }
                synchronized(control){
                    control.notifyAll();
                }
            }catch(InterruptedException e){
                Logger.getLogger(Control.class.getName()).log(Level.SEVERE,null, e);
            }
        }
    }


    /**
     * Metodo que da la cantidad de primos encontrados cuando el hilo esta en espera.
     * @return Cantidad de hilos encontrados.
     */



    public int primesFound(){
        int total = 0;
        int i = 0;
        while(i<NTHREADS){
            if(pft[i].waiting()){
                total = total+pft[i].getPrimesFound();
                i++;
            }
        }
        return total;
    }
    
}
