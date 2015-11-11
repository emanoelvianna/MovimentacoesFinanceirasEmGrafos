package br.com.implementacao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
		for (i = 0; i < matriz.length; i++)
			System.out.printf("%5s", indice2name(i));
		for (i = 0; i < matriz.length; i++) {
			System.out.printf("\n%5s: ", indice2name(i));
			for (int j = 0; j < matriz.length; j++)
				System.out.printf("%5d", (matriz[i][j]));
		}
		System.out.println();
	}

	public void showInfo() {
		System.out.print("V = { ");
		for (int i = 0; i < matriz.length - 1; i++)
			System.out.printf("%s, ", indice2name(i));
		System.out.printf("%s }\n", indice2name(matriz.length - 1));

		ArrayList<String> arestas = new ArrayList<String>();
		for (int i = 0; i < matriz.length; i++)
			for (int j = 0; j < matriz.length; j++)
				if (matriz[i][j] != 0)
					arestas.add(String.format("(%s, %s, %d)", indice2name(i), indice2name(j), matriz[i][j]));

		System.out.print("E = {\n");
		if (!arestas.isEmpty()) {
			System.out.printf("      %s", arestas.get(0));

			for (int i = 1; i < arestas.size(); i++)
				System.out.printf(",\n      %s", arestas.get(i));
		}
		System.out.println("\n    }");
	}
	

	public void simplificar() {
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

		try {

			BufferedReader info = new BufferedReader(new FileReader("movimentacoes.txt"));
			String linha = info.readLine();
			String[] tamanhoMatriz = linha.split(" ");
			System.out.println(tamanhoMatriz[0]);
			System.out.println(tamanhoMatriz[1]);
			
			iniciaMatriz(Integer.valueOf(tamanhoMatriz[0]), Integer.valueOf(tamanhoMatriz[1]));

			linha = info.readLine();
			while (linha != null) {
				String[] aux = linha.split(" ");

				addVertice(aux[0]);
				addVertice(aux[1]);
				movimentacoes(aux[0], aux[1], Integer.valueOf(aux[2]));
				linha = info.readLine();
			}

		} catch (IOException e) {
			System.out.println("Erro ao abrir arquivo de movimentacoes");
		}
	}

	public void imprimirMovimentacoes() {
		for (int linha = 0; linha < matriz.length; linha++) {
			for (int coluna = 0; coluna < matriz.length; coluna++) {
				if (matriz[linha][coluna] != 0) {
					System.out.println(linha + " " + coluna + " " + matriz[linha][coluna]);
				}
			}
		}
	}

	public void iniciaMatriz(int tam1, int tam2) {
		if (tam1 <= 0 || tam2 <= 0)
			throw new IllegalArgumentException("Numero de nodos invalido!");

		max = tam1 * tam2;
		matriz = new int[tam1][tam2];
		vert = new ArrayList<Vertice>(max);
		
		for (int i = 0; i < tam1; i++)
			for (int j = 0; j < tam2; j++)
				matriz[i][j] = 0;
	}

	public static void main(String[] args) {

		Grafo grafo = new Grafo();
		grafo.simplificar();
		grafo.simplificar();
		grafo.simplificar();
		
		grafo.showMatrix();
	
		grafo.showInfo();
		/*
		 * Calcular descont de 1% 0,01 x valor = ??
		 * 
		 * uma possivel condicao de parada e analisar se pela segunde vez
		 * seguida o ganho foi o mesmo, caso sim então deve-se parar !
		 */
		grafo.imprimirMovimentacoes();

	}
}
