// <Tempo de chegada>, <ID do Processo>, <Burst Time>, <Prioridade>, <Tempo chegada>, <Tempo final>, <CPU faltante>
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Iterator;
import java.util.LinkedList;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;

public class Escalonador{
	private static final String VIRGULA = ",";
	ArrayList<String> processos;
	ArrayList<String> filaSJF;
	ArrayList<String> filaSJFP;

	Escalonador(){
		processos = new ArrayList();
	}

	public void lerArquivo(String arquivo) throws Exception{
		BufferedReader readeer = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo)));
    	String linha = null;
    	
    	while ((linha = readeer.readLine()) != null) {
            this.processos.add(linha);
        }
	}

	public void imprimir(){
		for(int i = 0; i < this.processos.size(); i++){
			System.out.println(this.processos.get(i));
		}
	}

	public int sumCpuBurst(){
		int sum = 0;
		for(int i = 0; i < this.processos.size(); i++){
			sum += Integer.parseInt(this.processos.get(i).split(VIRGULA)[2]);
		}
		return sum;
	}

	public void ordenarFila(ArrayList<String> fila, int posicaoRelativa){
		// algoritmo de ordenação em relação ao tempo que falta (posicaoRelativa)
		for(int i = 0; i < fila.size(); i++){
			for(int j = 0; j < i; j++){
					if(Integer.parseInt(fila.get(i).split(VIRGULA)[posicaoRelativa]) <= Integer.parseInt(fila.get(j).split(VIRGULA)[posicaoRelativa])){
						String aux = fila.get(j);
						fila.set(j, fila.get(i));
						fila.set(i, aux);
					}
			}
		}
	}

	public ArrayList<String> cloneComAlteracoes(){
		// adicionar aqui o novo campo da string: tempo_inicio_exec | tempo_fim_exec | tempo que falta
		ArrayList<String> clone = new ArrayList();
		for(int i = 0; i < this.processos.size(); i++){
			String[] aux = this.processos.get(i).split(VIRGULA);
			String montagem = aux[0]+","+aux[1]+","+aux[2]+","+aux[3]+",-1,-1," + aux[2];
			clone.add(montagem);
		}
		return clone;
	}

	// Se o processo tiver finalizado:
		// 1º Olha se a fila de espera está vazia
		// Se não estiver:  1.a. Olha se chegou nesse instante processos com cpu burst menor do que a cabeça da fila de espera. 
								// OBS: Pode chegar mais de um processo no mesmo instante, daí tem que pegar o menor e incluir o restante na fila de espera e mandar ordenar a fila de espera 
		// 					1.b. Pega o processo da cabeça da fila
		// Se estiver: verificar se acabou os processos na fila de processos
		// PERA EU POSSO OTIMIZAR ISSO AQUI!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// Eu posso pegar todos os processos que chegaram naquele tempo e adicionar na fila de espera, ordenar a fila e pegar a cabeçaaaa
			

		// Se o processo não tiver finalizado:
		// 1º Verificar se chegou nesse instante processos com cpu burst menor do que o processo atual
		// OBS: Pode chegar mais de um processo no mesmo instante, daí tem que pegar o menor e incluir o restante na fila de espera e mandar ordenar a fila de espera 
		// Se não tiver chegado: continua com o processo atual
	/// devido ao insight anterior da pra eu otimizar a solução pra forma que ta abaixo:
	public String buscarProcesso(ArrayList<String> clone, ArrayList<String> filaEspera, String processoAtual, int tempo_corrente, boolean finalizado){
		// FAZENDO A BUSCA
		// TODO: Ver se não tem como eu começar da ultima posicao que vasculhei
		for(int i = 0; i < clone.size(); i++){
				if(clone.get(i).split(VIRGULA)[0] == Integer.toString(tempo_corrente)){
					filaEspera.add(clone.get(i));
				} else if(Integer.parseInt(clone.get(i).split(VIRGULA)[0]) > tempo_corrente){
				break;
			}
		}
		ordenarFila(filaEspera, 6);
		
		if(finalizado){
			return filaEspera.remove(0);
		} else{
			if(Integer.parseInt(filaEspera.get(0).split(VIRGULA)[6]) < Integer.parseInt(processoAtual.split(VIRGULA)[6])){
				return filaEspera.remove(0);
			} else {
				return processoAtual;
			}
		}
	}

	public void sjfpPreemptivo(){
		this.filaSJFP = new ArrayList();
		//fazer uma fila de espera de execução praqueles que ainda tão aguardando terminar de exec
		ArrayList<String> filaEspera = new ArrayList();
		//fazer a refatoração do formato dos processos aqui
		ArrayList<String> clone = this.cloneComAlteracoes();

		int tempo_corrente = 0;
		int posicao = 0;
		int cont = 0;
		String processoCorrente;

		while(tempo_corrente < this.sumCpuBurst()){
			// verificar aqui se o processo terminou, se sim manda a flag(ai ele olha a fila de espera)
			// posicao com cpu burst restante é a 6
			// se não ele olha somente se há novos processos
			
			if(cont == Integer.parseInt(filaSJFP.get(posicao).split(VIRGULA)[6])){
				// true significa que terminou
				String[] aux = filaSJFP.get(posicao).split(VIRGULA);
				String montagem = aux[0] + "," + aux[1] + "," + aux[2] + "," + aux[3] + "," + aux[4] + "," + Integer.toString(tempo_corrente) + ",0";
				filaSJFP.set(posicao, montagem);
				processoCorrente = buscarProcesso(clone, filaEspera, filaSJFP.get(posicao), tempo_corrente, true);
				aux = processoCorrente.split(VIRGULA);
				montagem = aux[0] + "," + aux[1] + "," + aux[2] + "," + aux[3] + "," + Integer.toString(tempo_corrente) + ",?," + Integer.toString(Integer.parseInt(aux[6]) - 1);
				filaEspera.add(montagem);
				posicao++;
				cont = 1;
			} else {
				// false significa que não terminou
				processoCorrente = buscarProcesso(clone, filaEspera, filaSJFP.get(posicao), tempo_corrente, false);
				if(processoCorrente != filaSJFP.get(posicao)){
					// atualiza o tempo que falta para executar do processo na string ou só no cont? se for no cont tem que mandar pro buscarProcesso
					// adiciona o processo na fila de espera
					// adiciona o novo processo na fila
					// cont = 1
					String[] aux = filaSJFP.get(posicao).split(VIRGULA);
					String montagem = aux[0] + "," + aux[1] + "," + aux[2] + "," + aux[3] + "," + aux[4] + "," + Integer.toString(tempo_corrente) + ",0";
					filaSJFP.set(posicao, montagem);
					
					aux = processoCorrente.split(VIRGULA);
					montagem = aux[0] + "," + aux[1] + "," + aux[2] + "," + aux[3] + "," + Integer.toString(tempo_corrente) + ",?," + Integer.toString(Integer.parseInt(aux[6]) - 1);
					filaEspera.add(montagem);
					posicao++;
					cont = 1;
				} else {
					cont++;
				}
			}		
			tempo_corrente++;
		}

	}

	//ajeitar aqui
	public  void sjfNaoPreemptivo(){
		this.filaSJF = cloneComAlteracoes();
		// Levar em consideração o tempo de chegada e o burst time
		// DEIXAR ISSO AQUI MAIS BONITINHO e realmente testar se não vai ter conflito nessas duas condições do tempo de chegada com o burst time
		for(int i = 0; i < this.filaSJF.size(); i++){
			for(int j = 0; j <i; j++){
				if(Float.parseFloat(this.filaSJF.get(i).split(VIRGULA)[0]) <= Float.parseFloat(this.filaSJF.get(j).split(VIRGULA)[0])){
					if(Float.parseFloat(this.filaSJF.get(i).split(VIRGULA)[2]) <= Float.parseFloat(this.filaSJF.get(j).split(VIRGULA)[2])){
						String aux = this.filaSJF.get(j);
						//TODO: Ajeitar os tempos de chegada e finalizacao
						this.filaSJF.set(j, this.filaSJF.get(i));
						this.filaSJF.set(i, aux);
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception{
		Escalonador escalonador = new Escalonador();  
		escalonador.lerArquivo("dados.csv");
        //String[] dado
        //System.out.println("Tempo Chegada: " + dados[0] + "| id: " + dados[1] + " | burst time: " + dados[2] + " | prioridade: " + dados[3] + "\n");
    	//System.out.println(escalonador.sumCpuBurst());
    	//ArrayList<String> teste = escalonador.cloneComAlteracoes();
    	
		//escalonador.imprimir();
		//escalonador.ordenarFila(escalonador.processos, 2);
    	escalonador.sjfNaoPreemptivo();
    	//escalonador.imprimir();
    	for(int i = 0; i < escalonador.filaSJF.size(); i++){
			System.out.println(escalonador.filaSJF.get(i));
		}
    }
}