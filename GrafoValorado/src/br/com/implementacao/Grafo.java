package br.com.implementacao;

import java.util.ArrayList;

public class Grafo {

	private class Vertice {
		private String elemento;

		public Vertice(String elemento) {
			this.elemento = elemento;
		}

		public String getElemento() {
			return this.elemento;
		}

		public void setElemento(String item) {
			this.elemento = item;
		}
	}

	private int max;
	private int[][] matriz;
	private ArrayList<Vertice> vert;

	public Grafo(int n) {
		if (n <= 0)
			throw new IllegalArgumentException("Numero de nodos invalido!");

		max = n;
		matriz = new int[max][max];
		vert = new ArrayList<Vertice>(max);
		// inicializacao da matriz
		for (int i = 0; i < max; i++)
			for (int j = 0; j < max; j++)
				matriz[i][j] = 0;
	}

	private int buscar(String item) {
		int i, res = -1;
		// percorre a lista de vertices para achar o elemento
		for (i = 0; ((i < vert.size()) && !item.equals(vert.get(i).getElemento())); i++)
			;

		// se o indice
		if (i < vert.size())
			res = i;

		return res;
	}

	public void addVertice(String item) {
		// se tem espaco pra adicionar ainda
		if (vert.size() < max) {
			// se o vertice ainda nao foi adicionado, entao adiciona-o
			if (buscar(item) == -1) {
				Vertice v = new Vertice(item);
				vert.add(v);
			}
		} else
			throw new IllegalArgumentException("Capacidade do grafo atingida: " + max);
	}

	public void movimentacoes(String strOrig, String strDest, int valor) {
		int orig, dest;

		orig = buscar(strOrig);
		dest = buscar(strDest);

		if (orig == -1)
			throw new IllegalArgumentException("Aresta origem invalida: " + strOrig);
		else if (dest == -1)
			throw new IllegalArgumentException("Aresta destino invalida: " + strDest);
		else {
			matriz[orig][dest] = valor;
		}
	}

	private String indice2name(int i) {
		if (i < max)
			return vert.get(i).getElemento();
		return null;
	}

	public void showMatrix() {
		int i;
		System.out.printf("       ");
		for (i = 0; i < max; i++)
			System.out.printf("%5s", indice2name(i));
		for (i = 0; i < max; i++) {
			System.out.printf("\n%5s: ", indice2name(i));
			for (int j = 0; j < max; j++)
				System.out.printf("%5d", (matriz[i][j]));
		}
		System.out.println();
	}
	
	public void calcular() {
		for (int i = 0; i < matriz.length; i++) {
			if(matriz[0][i] != 0) {
				for (int j = 0; j < matriz.length; j++) {
					System.out.println(matriz[i][j]);
				}
				break;
			}
		}
	}

	public static void main(String[] args) {
		Grafo grafo = new Grafo(5);
		
		grafo.addVertice("1");
		grafo.addVertice("2");
		grafo.addVertice("3");
		grafo.addVertice("4");
		grafo.addVertice("5");

		grafo.movimentacoes("1", "2", 500);
		grafo.movimentacoes("2", "3", 230);
		grafo.movimentacoes("3", "4", 120);
		grafo.movimentacoes("1", "4", 120);
		grafo.movimentacoes("2", "5", 200);
		
		
		grafo.showMatrix();
		
		grafo.calcular();
	}
}
