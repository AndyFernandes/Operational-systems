// Andreza Fernandes de Oliveira - 384341
// Arina de Jesus - 39

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
	// refere-se ao vetor base que diz quanto há pra cada recurso
	int[] qtsRecurso = new int[]{20, 10, 10};
	int[][] request;
	ArrayList<String> processos = new ArrayList();
	int m = 0;
	int n = 0;

	Verificador() {}


	public void imprimirVetor(int[] vetor) {
		String dados = " ";
		for(int i = 0; i < vetor.length; ++i) {
			dados = dados + vetor[i] + " ";
		}
		System.out.println(dados);
	}

	public void imprimirMatriz(int[][] matriz) {
		for(int i = 0; i < matriz.length; ++i) {
			String dados = " ";
			for(int j = 0; j < matriz[0].length; ++j) {
				dados = dados + matriz[i][j] + " ";
			}
			System.out.println(dados);
		}
	}

	public void calcularNeed() {
		for(int i = 0; i < this.n; ++i) {
			for(int j = 0; j < this.m; ++j) {
				this.need[i][j] = this.max[i][j] - this.allocation[i][j];
			}
		}
	}

	public void calcularAvailable() {
		for(int i = 0; i < this.m; ++i) {
			int dados = 0;
			for(int j = 0; j < this.n; ++j) {
				dados += this.allocation[j][i];
			}
			this.available[i] = this.qtsRecurso[i] - dados;
		}
	}


	public void lerArquivo(String var1) throws Exception {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(var1)));
		String dados = null;

		while((dados = bufferedReader.readLine()) != null) {
			this.processos.add(dados);
		}

		this.n = this.processos.size();
		String[] dadosProcesso = ((String)this.processos.get(0)).split(",");
		this.m = (dadosProcesso.length - 1) / 2;
		this.max = new int[this.n][this.m];
		this.available = new int[this.m];
		this.allocation = new int[this.n][this.m];
		this.need = new int[this.n][this.m];

		for(int i = 0; i < this.n; ++i) {
			dadosProcesso = ((String)this.processos.get(i)).split(",");
			for(int j = 0; j < this.m; ++j) {
				this.allocation[i][j] = Integer.parseInt(dadosProcesso[j + 1]);
				this.max[i][j] = Integer.parseInt(dadosProcesso[j + 4]);
			}
		}
		this.calcularNeed();
		this.calcularAvailable();
	}

	public int[] copiar(int[] vetor) {
		int[] dados = new int[vetor.length];
		for(int i = 0; i < vetor.length; ++i) {
			dados[i] = vetor[i];
		}
		return dados;
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

	public boolean avoid(int[] request) {
		// assume-se que o processo passado está na lista de processos
		int i;
		int Pi = request[0];

		for(i = 0; i < this.m; i++) {
			if (request[i+1] > this.need[Pi][i]) {
				System.out.println("Erro, processo excedeu limite máximo de requisições");
				return false;
			}
		}
		
		if(i == this.m){
			for(i = 0; i < this.m; i++){
				if(request[i+1] > this.available[i]){
					System.out.println("Processo espera por recursos");
					return false;
				}
			}
			
			if(i == this.m){
				for(i = 0; i < this.m; i++){
					this.available[i] -= request[i+1];
					this.allocation[Pi][i] += request[i+1];
					this.need[Pi][i] -= request[i+1];
				}
			System.out.println("Recursos alocados!");
			return true;
			}
		}
		System.out.println("Erro");
		return false;

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
		verificador.lerArquivo("dados_slide.csv");

		// safety para o conjunto de dados oferecidos
		verificador.safety();

		// Simulando o Pi que é o request
		int [] pi = new int[4];
		pi[0] = 1;
		
		pi[1] = 1;
		pi[2] = 0;
		pi[3] = 0;

		// avoid
		verificador.avoid(pi);
		
		// dectetion
		detection("requests.csv")
	}

}
