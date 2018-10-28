//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;

public class Verificador {
	private static final String VIRGULA = ",";
	int[] available;
	int[][] max;
	int[][] allocation;
	int[][] need;
	int[] qtsRecurso = new int[]{20, 10, 10};
	int[][] request;
	ArrayList<String> processos = new ArrayList();
	int m = 0;
	int n = 0;

	Verificador() {}

	public void imprimirVetor(int[] var1) {
		String var2 = " ";
		for(int var3 = 0; var3 < var1.length; ++var3) {
			var2 = var2 + var1[var3] + " ";
		}
		System.out.println(var2);
	}

	public void imprimirMatriz(int[][] var1) {
		for(int var2 = 0; var2 < var1.length; ++var2) {
			String var3 = " ";
			for(int var4 = 0; var4 < var1[0].length; ++var4) {
				var3 = var3 + var1[var2][var4] + " ";
			}
			System.out.println(var3);
		}
	}

	public void calcularNeed() {
		for(int var1 = 0; var1 < this.n; ++var1) {
			for(int var2 = 0; var2 < this.m; ++var2) {
				this.need[var1][var2] = this.max[var1][var2] - this.allocation[var1][var2];
			}
		}
	}

	public void calcularAvailable() {
		for(int var1 = 0; var1 < this.m; ++var1) {
			int var2 = 0;
			for(int var3 = 0; var3 < this.n; ++var3) {
				var2 += this.allocation[var3][var1];
			}
			this.available[var1] = this.qtsRecurso[var1] - var2;
		}
	}

	public void lerArquivo(String var1) throws Exception {
		BufferedReader var2 = new BufferedReader(new InputStreamReader(new FileInputStream(var1)));
		String var3 = null;

		while((var3 = var2.readLine()) != null) {
			this.processos.add(var3);
		}

		this.n = this.processos.size();
		String[] var4 = ((String)this.processos.get(0)).split(",");
		this.m = (var4.length - 1) / 2;
		this.max = new int[this.n][this.m];
		this.available = new int[this.m];
		this.allocation = new int[this.n][this.m];
		this.need = new int[this.n][this.m];

		for(int var5 = 0; var5 < this.n; ++var5) {
			var4 = ((String)this.processos.get(var5)).split(",");
			for(int var6 = 0; var6 < this.m; ++var6) {
				this.allocation[var5][var6] = Integer.parseInt(var4[var6 + 1]);
				this.max[var5][var6] = Integer.parseInt(var4[var6 + 4]);
			}
		}
		this.calcularNeed();
		this.calcularAvailable();
	}

	public int[] copiar(int[] var1) {
		int[] var2 = new int[var1.length];
		for(int var3 = 0; var3 < var1.length; ++var3) {
			var2[var3] = var1[var3];
		}
		return var2;
	}

	public void atribuirFalse(boolean[] vetor) {
		for(int i = 0; i < vetor.length; i++) {
			vetor[i] = false;
		}
	}

	public boolean safety() {
		int[] work = this.copiar(this.available);
		boolean[] finish = new boolean[this.n];
		this.atribuirFalse(finish);
		int safeSeq[] = new int[this.n];
		int cont = 0;
		
		while(cont < this.n){
			boolean bool = false;
			for(int i = 0; i < this.n; i++) {
				if (finish[i] == false) { 
					int j;
					for(j = 0; j < this.m; j++){
						if(this.need[i][j] > work[j]){
							break;
						}
					}
					if(j == this.m){
						for(int k = 0; k < this.m; k++){
							work[k] += allocation[i][k]; 
						}
						finish[i] = true;
						safeSeq[cont] = i;
						cont +=1;
						bool = true;
					} else {
						bool = false;
						break;
					}
				
				}
			}
			if(bool == false){
				System.out.println("Estado inseguro!");
				return false;
			}
		}
		System.out.println("Processos sem deadlock!\nSequencia de processos:");
		imprimirVetor(safeSeq);
		return true;
	}

	public void avoid(int[] var1) {
		boolean bool = false;

		for(int i = 0; i < var1.length; i++) {
			if (var1[i] > this.need[i][i] || var1[i] > this.available[i]) {
				bool = true;
				break;
			}
			this.available[i] -= var1[i];
			this.allocation[i][i] += var1[i];
			this.need[i][i] -= var1[i];
		}

		if (bool) {
			System.out.println("Estado inseguro! Espera");
		} else {
			System.out.println("Recursos alocados!");
		}
	}

	public boolean detection(String var1) throws Exception{
		this.request = new int[this.n][this.m];

		BufferedReader listaRequesicoes = new BufferedReader(new InputStreamReader(new FileInputStream(var1)));
		String requesicao = null;

		processos.clear();
		while((requesicao = listaRequesicoes.readLine()) != null) {
			this.processos.add(requesicao);
		}

		String[] dadosRequisicao;

		for(int i = 0; i < this.n; ++i) {
			dadosRequisicao = ((String)this.processos.get(i)).split(",");
			for(int j = 0; j < this.m; ++j) {
				this.request[i][j] = Integer.parseInt(dadosRequisicao[j + 1]);
			}
		}

		int[] work = this.copiar(this.available);
		boolean[] finish = new boolean[this.n];

		atribuirFalse(finish);

		int[] sequenciaSegura =  new int[this.n];
		int numProcessos = 0;

		while (numProcessos < this.n){
			boolean found = false;
			for(int p = 0; p < this.n; p++){
				if(finish[p] == false){
					int j;
					for(j = 0; j < this.m; j++){
						if(this.request[p][j] > work[j]){
							break;
						}
					}

					if(j == this.m){
						for(int k = 0; k < this.m; k++){
							work[k] += this.allocation[p][k];
						}
						sequenciaSegura[numProcessos] = p;
						numProcessos++;
						finish[p] = true;
						found = true;
						p = 0;
					}
				}
			}
			if(found == false){
				System.out.println("Sistema está em deadlock");
				return false;
			}

		}


		System.out.println("Sistema não está em deadlock \nSequencia segura de execução:");
		String sequenciaExecucao = " ";
		for(int i = 0; i < this.n; i++){
			sequenciaExecucao += "P" + sequenciaSegura[i] + " ";
		}
		System.out.println(sequenciaExecucao);
		return true;

	}

	public static void main(String[] args) throws Exception {
		Verificador verificador = new Verificador();
		verificador.lerArquivo("dados.csv");

		verificador.safety();

	}

}
