/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.Scanner;

/**
 *
 */
public class Control extends Thread {

	private final static int NTHREADS = 3;
	private final static int MAXVALUE = 30000000;
	private final static int TMILISECONDS = 5000;
	private boolean terminaron = false;

	private final int NDATA = MAXVALUE / NTHREADS;

	private PrimeFinderThread pft[];

	private Control() {
		super();

		this.pft = new PrimeFinderThread[NTHREADS];

		int i;
		for (i = 0; i < NTHREADS - 1; i++) {
			PrimeFinderThread elem = new PrimeFinderThread(i * NDATA, (i + 1) * NDATA);
			pft[i] = elem;
		}
		pft[i] = new PrimeFinderThread(i * NDATA, MAXVALUE + 1);
	}

	public static Control newControl() {
		return new Control();
	}

	private void yaT() {
		boolean t = true;
		for (int i = 0; i < NTHREADS; i++) {
			if (pft[i].isAlive()) {
				t = false;
				break;
			}
		}
		terminaron = t;

	}

	@Override
	public void run() {
		
		for (int i = 0; i < NTHREADS; i++) {
			pft[i].start();
		}
		Scanner sc = new Scanner(System.in);

		long maxT = System.currentTimeMillis() + TMILISECONDS;

		while (!terminaron) {

			if (System.currentTimeMillis() >= maxT) {
				
				for (int i = 0; i < NTHREADS; i++) {

					System.out.println("el thread " + i + " ha hallado " + pft[i].hallados() + " primos");

					pft[i].dormir();

					
					

				}
				boolean enter = true;
				while (enter) {
					System.out.println("oprima enter para continuar");
					if (sc.nextLine().equals("")) {

						for (int i = 0; i < pft.length; i++) {
							
							//pft[i].notify();
							pft[i].awake();
						}

						maxT = System.currentTimeMillis() + TMILISECONDS;
					}
					enter = false;
					break;
				}

			}
			yaT();
		}

		for (int i = 0; i < pft.length; i++) {
			System.out.println("el thread " + i + " ha encontrado " + pft[i].hallados() + " primos");
		}
		System.out.println("fin");
		System.exit(0);
	}

}