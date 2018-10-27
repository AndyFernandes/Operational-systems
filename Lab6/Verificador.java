import org.omg.CORBA.IMP_LIMIT;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Verificador {
	private static final String VIRGULA = ",";
	int available[];
	int[][] max;
	int[][] allocation;
	int[][] need;
	int[] qtsRecurso = {12, 9, 8};
	ArrayList<String> processos = new ArrayList<>();
	int m = 0, n = 0;
	Verificador(){}

	public void imprimirVetor(int[] vetor){
		String linha = " ";
		for (int i = 0; i < vetor.length; i ++){
			linha += vetor[i] + " ";
		}
		System.out.println(linha);
	}

	public void imprimirMatriz(int[][] matriz){
		for(int i = 0; i < matriz.length; i++){
			String linha = " ";
			for(int j = 0; j < matriz[0].length; j++){
				linha += matriz[i][j] + " ";
			}
			System.out.println(linha);
		}
	}

	public void calcularNeed(){
		for(int i = 0; i < this.n; i++){
			for(int j = 0; j < this.m; j++){
				this.need[i][j] = max[i][j] - allocation[i][j];
			}
		}
	}

	public void calcularAvailable(){
		for(int j = 0; j < m; j++){
			int soma = 0;
			for(int k = 0; k < n; k++){
				soma += allocation[k][j];
			}
			this.available[j] = qtsRecurso[j] - soma;
		}
	}

	public void lerArquivo(String arquivo) throws Exception{
		BufferedReader readeer = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo)));
		String linha = null;

		while ((linha = readeer.readLine()) != null) {
			this.processos.add(linha);
		}

		// calcular n e m  e atribuir a variaveis
		// aparentemente n = processos.size() e m = processos[0].split() -> Ai (tamanho do vetor splitado - 1 )/2
		this.n = this.processos.size();
		String[] aux = this.processos.get(0).split(",");
		this.m = (aux.length - 1)/2;


		// instanciar os bixos
		max = new int[n][m];
		available = new int[m];
		allocation = new int[n][m];
		need = new int[n][m];

		// quando terminar de ler aqui povoar o available, max, allocation, need
		for(int i = 0; i < n; i++){
			aux = this.processos.get(i).split(",");
			for(int j = 0; j < m; j++){
				allocation[i][j] = Integer.parseInt(aux[j+1]);
				max[i][j] = Integer.parseInt(aux[j+4]);
			}
		}
		calcularNeed();
		calcularAvailable();
	}

	public void safety(){}

	public void avoid(String pi){}
	public void detection(){}

	public static void main(String[] args) throws Exception{

		Verificador verificador =  new Verificador();
		verificador.lerArquivo("dados.csv");

	}


}