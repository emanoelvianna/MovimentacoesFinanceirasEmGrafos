package br.com.implementacao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Grafo {

	private class Vertice {
		private String elemento;
		private boolean marcado;

		public Vertice(String elemento) {
			this.elemento = elemento;
		}

		public String getElemento() {
			return this.elemento;
		}

		public void setElemento(String item) {
			this.elemento = item;
		}

		public boolean isMarcado() {
			return marcado;
		}

		public void setMarcado(boolean marcado) {
			this.marcado = marcado;
		}
	}

	private int max;
	private int[][] matriz;
	private ArrayList<Vertice> vert;

	public Grafo() {
		lerMovimentacoes();
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

	public void showInfo() {
		System.out.print("V = { ");
		for (int i = 0; i < max - 1; i++)
			System.out.printf("%s, ", indice2name(i));
		System.out.printf("%s }\n", indice2name(max - 1));

		ArrayList<String> arestas = new ArrayList<String>();
		for (int i = 0; i < max; i++)
			for (int j = 0; j < max; j++)
				if (matriz[i][j] != 0)
					arestas.add(String.format("(%s, %s)", indice2name(i), indice2name(j)));

		System.out.print("E = {\n");
		if (!arestas.isEmpty()) {
			System.out.printf("      %s", arestas.get(0));

			for (int i = 1; i < arestas.size(); i++)
				System.out.printf(",\n      %s", arestas.get(i));
		}
		System.out.println("\n    }");
	}

	public void calcular() {
		double porcentagem = 0.01;
		double ganho = 0;

		int cont = 0;
		while (cont != matriz.length) {
			for (int linha = 0; linha < matriz.length; linha++) {
				if (matriz[cont][linha] != 0) {
					for (int coluna = 0; coluna < matriz.length; coluna++) {
						if (matriz[linha][coluna] != 0) {

							int minha = matriz[cont][linha];
							int tua = matriz[linha][coluna];

							if (minha > tua) {
								int dif = minha - tua;
								matriz[cont][linha] = dif;
								matriz[cont][coluna] = matriz[cont][coluna] + tua;
								matriz[linha][coluna] = 0;
								ganho = minha * porcentagem;
							} else {
								matriz[cont][coluna] = matriz[cont][coluna] + matriz[cont][linha];
								matriz[cont][linha] = 0;
								matriz[linha][coluna] = matriz[linha][coluna] - minha;
								ganho = minha * porcentagem;
							}

						}
					}
				}
			}
			cont++;
		}
		System.out.println("ganho: " + ganho);
	}

	public void lerMovimentacoes() {

		BufferedReader br = null;

		try {
			String linha = "";
			String divisor = " ";

			br = new BufferedReader(new FileReader("movimentacoes.txt"));

			String[] info = br.readLine().split(divisor);
			max = Integer.valueOf(info[0]);
			
			// inicializacao da matriz
			matriz = new int[max][max];
			vert = new ArrayList<Vertice>(max);
			for (int i = 0; i < max; i++)
				for (int j = 0; j < max; j++)
					matriz[i][j] = 0;

			while ((linha = br.readLine()) != null) {
				info = br.readLine().split(divisor);
				// adiciona as movimentacoes
				try {
					System.out.println("Olha eu aqui " + info[0]);
					
					
				} catch (NumberFormatException numberFormatException) {

				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {

			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {

		Grafo grafo = new Grafo();

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
		System.out.println("------------------");

		grafo.calcular();

		grafo.showMatrix();

		grafo.calcular();

		System.out.println("------------------");

		grafo.showMatrix();

		grafo.calcular();

		System.out.println("------------------");

		grafo.showMatrix();

		grafo.lerMovimentacoes();

		/*
		 * Calcular descont de 1% 0,01 x valor = ??
		 */

	}
}
