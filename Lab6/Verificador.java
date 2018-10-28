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
	int[] qtsRecurso = new int[]{7, 2, 6};
	int[][] request;
	ArrayList<String> processos = new ArrayList();
	int m = 0;
	int n = 0;

	Verificador() {
	}

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

	public void atribuirFalse(boolean[] var1) {
		for(int var2 = 0; var2 < var1.length; ++var2) {
			var1[var2] = false;
		}

	}

	public void safety() {
		int[] var1 = this.copiar(this.available);
		boolean[] var2 = new boolean[this.n];
		this.atribuirFalse(var2);
		boolean var3 = false;

		for(int var4 = 0; var4 < this.n; ++var4) {
			if (var2[var4] || this.need[var4][var4] > var1[var4]) {
				var3 = true;
				break;
			}

			var1[var4] += this.allocation[var4][var4];
			var2[var4] = true;
		}

		if (var3) {
			System.out.println("Estado inseguro!");
		} else {
			System.out.println("Estado seguro!");
		}

	}

	public void avoid(int[] var1) {
		boolean var2 = false;

		for(int var3 = 0; var3 < var1.length; ++var3) {
			if (var1[var3] > this.need[var3][var3] || var1[var3] > this.available[var3]) {
				var2 = true;
				break;
			}

			this.available[var3] -= var1[var3];
			this.allocation[var3][var3] += var1[var3];
			this.need[var3][var3] -= var1[var3];
		}

		if (var2) {
			System.out.println("Estado inseguro! Espera");
		} else {
			System.out.println("Recursos alocados!");
		}

	}

	public boolean detection(String var1) throws Exception{
		this.request = new int[this.n][this.m];

		BufferedReader var2 = new BufferedReader(new InputStreamReader(new FileInputStream(var1)));
		String var3 = null;

		processos.clear();
		while((var3 = var2.readLine()) != null) {
			this.processos.add(var3);
		}

		String[] var4;

		for(int var5 = 0; var5 < this.n; ++var5) {
			var4 = ((String)this.processos.get(var5)).split(",");
			for(int var6 = 0; var6 < this.m; ++var6) {
				this.request[var5][var6] = Integer.parseInt(var4[var6 + 1]);
			}
		}

		int[] work = this.copiar(this.available);
		boolean[] finish = new boolean[this.n];

		atribuirFalse(finish);

		int[] safeSeq =  new int[this.n];
		int count = 0;

		while (count < this.n){
			boolean flag = false;
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
						safeSeq[count] = p;
						count++;
						finish[p] = true;
						flag = true;
						p = 0;
					}
				}
			}
			if(flag == false){
				System.out.println("Sistema está em deadlock");
				return false;
			}

		}


		System.out.println("Sistema não está em deadlock \nSequencia segura de execução:");
		String execucao = " ";
		for(int i = 0; i < this.n; i++){
			execucao += "P" + safeSeq[i] + " ";
		}
		System.out.println(execucao);
		return true;

	}

}
