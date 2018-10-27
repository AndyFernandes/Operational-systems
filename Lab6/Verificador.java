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
	ArrayList<String> processos;
	int m = 0, n = 0;

	Verificador(){
		this.processos = new ArrayList();
	}

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
				this.need[i][j] = this.max[i][j] - this.allocation[i][j];
			}
		}
	}

	public void calcularAvailable(){
		for(int j = 0; j < m; j++){
			int soma = 0;
			for(int k = 0; k < n; k++){
				soma += this.allocation[k][j];
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
		this.max = new int[n][m];
		this.available = new int[m];
		this.allocation = new int[n][m];
		this.need = new int[n][m];

		// quando terminar de ler aqui povoar o available, max, allocation, need
		for(int i = 0; i < n; i++){
			aux = this.processos.get(i).split(",");
			for(int j = 0; j < m; j++){
				this.allocation[i][j] = Integer.parseInt(aux[j+1]);
				this.max[i][j] = Integer.parseInt(aux[j+4]);
			}
		}
		calcularNeed();
		calcularAvailable();
	}

	public int[] copiar(int[] vetor){
		int[] copia = new int[vetor.length];
		for(int i = 0; i < vetor.length; i++){
			copia[i] = vetor[i];
		}
		return copia;
	}

	public void atribuirFalse(boolean[] vetor){
		for(int i = 0; i < vetor.length; i++){
			vetor[i] = false;
		}
	}

	//duvida se a posicao de acesso ao need e allocation é [i][i] mesmo. Acho que pra cada processo. Verificar isso com a Arina
	//testar
	public void safety(){
		int[] work = copiar(this.available);
		boolean[] finish = new boolean[this.n];
		atribuirFalse(finish);

		// fazendo a busca
		int cont = 0;
		for(int i = 0; i < this.n; i++){
			if(finish[i] == false && this.need[i][i] <= work[i]){
				work[i] = work[i] + this.allocation[i][i];
				finish[i] = true;
			} else {
				cont = 1;
				break;
			}
		}
		if(cont == 1){
			System.out.println("Estado inseguro!");
		} else{
			System.out.println("Estado seguro!");
		}
	}

	//duvida se a posicao de acesso ao need e allocation é [i][i] mesmo. Acho que pra cada processo. Verificar isso com a Arina
	//testar
	//duvida se a proxima posição não der certo, se há problemas ficar aquele lixo de informação do caso que deu certo anterior
	public void avoid(int[] request){
		int cont = 0;
		for(int i = 0; i < request.length; i++){
			// eu realmente acho que tipo a linha seria pelo i do processo e a coluna o j que representa o recurso. Checar c a Arina
			if(request[i] <= this.need[i][i] && request[i] <= this.available[i]){
				this.available[i] = this.available[i] - request[i];
				this.allocation[i][i] = this.allocation[i][i] + request[i];
				this.need[i][i] = this.need[i][i] - request[i];
			} else {
				cont = 1;
				break;
			}
		}
		if(cont == 1){
			System.out.println("Estado inseguro! Espera");
		} else{
			System.out.println("Recursos alocados!");
		}
	}
	public void detection(){}

	public static void main(String[] args) throws Exception{

	}
}