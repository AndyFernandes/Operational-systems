// <Tempo de chegada>, <ID do Processo>, <Burst Time>, <Prioridade>, <Tempo chegada>, <Tempo final>, <CPU faltante>
// Andreza:
// > Calcular estatisticas
// UNIR OS CODIGOS
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Iterator;
import java.util.LinkedList;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.IntStream; 
import java.util.Scanner;

public class Escalonador{
	private static final String VIRGULA = ",";
	ArrayList<String> processos;
	ArrayList<String> filaSJF;
	ArrayList<String> filaSJFP;

	ArrayList<String> estatisticas = new ArrayList<String>();
	ArrayList<String> diagrama = new ArrayList<String>();


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

	public void imprimir(ArrayList<String> fila){
		for(int i = 0; i < fila.size(); i++){
			System.out.println(fila.get(i));
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

	public float tempoTotal(ArrayList<String> fila){
		// total cpu burst + troca de contexto
		return this.sumCpuBurst() + (fila.size()-1);
	}
	// <Tempo de chegada>, <ID do Processo>, <Burst Time>, <Prioridade>, <Tempo chegada>, <Tempo final>, <CPU faltante>
	public float percentualUsoCpu(ArrayList<String> fila){
		// tempo total-tempo troca contexto / tempo total
		return (this.tempoTotal(fila)-(fila.size()-1))/this.tempoTotal(fila);
	}

	public float mediaThoughput(ArrayList<String> fila){
		// numero de processos / tempo total
		return this.processos.size()/this.tempoTotal(fila);
	}

	public void ordenarFila2(ArrayList<String> fila){
		for(int i = 0; i < fila.size(); i++){
			for(int j = 0; j <fila.size(); j++){
				if(Float.parseFloat(fila.get(i).split(VIRGULA)[1]) <= Float.parseFloat(fila.get(j).split(VIRGULA)[1])){
					if(Float.parseFloat(fila.get(i).split(VIRGULA)[4]) <= Float.parseFloat(fila.get(j).split(VIRGULA)[4])){
						String aux = fila.get(j);
						fila.set(j, fila.get(i));
						fila.set(i, aux);
					}
				}
			}
		}
	}

	public float mediaTempoEspera(ArrayList<String> fila){
		//devo levar em consideração quando o processo chegou e quando ele subiu pra executar a primeira vez
		ArrayList<String> id = new ArrayList();
		ArrayList<String> tempoProcs = new ArrayList();

		ArrayList<String> copy = new ArrayList();
		for(int i = 0; i < fila.size(); i++){
			copy.add(fila.get(i));
		}
		ordenarFila2(copy);
		// é o seguinte: eu calculo o tempo de resposta pra todos e no final eu só somo aqueles iguais
		// 1. eu deixo ordenado pelo ID
		// 2. eu faço a lógica tipo de se não estiver contido msm coisa
		// 3. se estiver contido, pelo aux[1] eu pego o id  dele e atualizo a 2a posicao
		// 4. só que eu vou adicionar mais um campo na string, da posicao do ultimo tempo de fim
		// 5. ai nesse else eu comparo o tempo de fim - tempo de inicio
		for(int i = 0; i < copy.size(); i++){
			String aux[] = copy.get(i).split(VIRGULA);
			if(!id.contains(aux[1])){
				id.add(aux[1]);
				int resposta = Integer.parseInt(aux[4]) - Integer.parseInt(aux[0]);
				String montagem = aux[1] + "," + Integer.toString(resposta) + "," + aux[5];
				tempoProcs.add(montagem);
			} else {
				int posit1 = Integer.parseInt(aux[1])-1;
				// pegando o tempo procs e reparte (vai ter a informação da espera anterior(posicao1) e tempo final do anterior(posicao2))
				String tempo[] = tempoProcs.get(posit1).split(VIRGULA);
				// pega o copy e reparte (vai ter  ainformacao do tempo inicial do outro processo (posicao4))
				String copia[] = copy.get(i).split(VIRGULA);
				int resposta = Integer.parseInt(copia[1]) + (Integer.parseInt(copia[4]) - Integer.parseInt(tempo[2]));
				String montagem = tempo[1] + "," + Integer.toString(resposta) + "," + copia[5];
				tempoProcs.set(posit1, montagem);
			}
		}

		float sum = 0;
		for(int i=0; i < tempoProcs.size(); i++){
			sum += Float.parseFloat(tempoProcs.get(i).split(VIRGULA)[1]);
		}
		return sum/tempoProcs.size();
	}

	public float mediaTurnAround(ArrayList<String> fila){
		ArrayList<String> id = new ArrayList();
		ArrayList<String> tempoProcs = new ArrayList();

		for(int i = 0; i < fila.size(); i++){
			String aux[] = fila.get(i).split(VIRGULA);
			if(id.contains(aux[1])){
				tempoProcs.set((Integer.parseInt(aux[1])-1), fila.get(i));
			} else {
				id.add(aux[1]);
				tempoProcs.add(fila.get(i));
			}
		}
		
		float sum = 0;
		for(int i = 0; i < tempoProcs.size(); i++){
			int time = Integer.parseInt(tempoProcs.get(i).split(VIRGULA)[5]) - Integer.parseInt(tempoProcs.get(i).split(VIRGULA)[0]);
			sum += time;
		}

		return sum/tempoProcs.size();
	}

	public float mediaTempoResposta(ArrayList<String> fila){
		ArrayList<String> id = new ArrayList();
		ArrayList<String> tempoProcs = new ArrayList();

		for(int i = 0; i < fila.size(); i++){
			String aux[] = fila.get(i).split(VIRGULA);
			if(!id.contains(aux[1])){
				id.add(aux[1]);
				int resposta = Integer.parseInt(aux[4]) - Integer.parseInt(aux[0]);
				String montagem = aux[1] + "," + Integer.toString(resposta);
				tempoProcs.add(montagem);
			}
		}
		
		float sum = 0;
		for(int i = 0; i < tempoProcs.size(); i++){
			sum += Float.parseFloat(tempoProcs.get(i).split(VIRGULA)[1]);
		}

		return sum/tempoProcs.size();
	}

	public float mediaTrocaContexto(ArrayList<String> fila){
		// quantas vezes trocou de processos / numero de processos
		return (fila.size()-1)/this.processos.size();
	}

	public float numProcessosExecutados(){
		return this.processos.size();
	}

    static void bubbleSort(ArrayList<String> arr){
        int n = arr.size();
        String temp;
        for(int i=0; i < n; i++){
            for(int j=1; j < (n-i); j++){
        		if(Integer.parseInt(arr.get(j).split(VIRGULA)[0]) < Integer.parseInt(arr.get(j-1).split(VIRGULA)[0]) ){ ///Integer.parseInt(this.processos.get(i).split(VIRGULA)[0])
	                //swap elements
	                temp = arr.get(j-1);
	                arr.set(j-1, arr.get(j));
	                arr.set(j, temp);
            	}	
            }
	    }
    }

    ////////////////////////////////////// EXPLICACAO DO ALGORITMO ABAIXO //////////////////////////////////
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
				if(Integer.parseInt(clone.get(i).split(VIRGULA)[0]) == tempo_corrente){
					filaEspera.add(clone.get(i));
				} else if(Integer.parseInt(clone.get(i).split(VIRGULA)[0]) > tempo_corrente){
					break;
				}
		}
		ordenarFila(filaEspera, 6);
		if(finalizado && !filaEspera.isEmpty()){
			String proc = filaEspera.get(0);
			filaEspera.remove(0);
			return proc;
		} else if (!filaEspera.isEmpty()){
			if(Integer.parseInt(filaEspera.get(0).split(VIRGULA)[6]) < Integer.parseInt(processoAtual.split(VIRGULA)[6])){
				String proc = filaEspera.get(0);
				filaEspera.remove(0);
				return proc;
			} else {
				return processoAtual;
			}
		} else {
			return processoAtual;
		}
	}

	public void imprimirBonito(ArrayList<String> fila){
		for(int i=0; i < fila.size(); i++ ){
			String []aux = fila.get(i).split(VIRGULA);
			int processado = Integer.parseInt(aux[5]) - Integer.parseInt(aux[4]);
			System.out.println("ID: " + aux[1] + "| Tempo processado: " + processado);
		}
	}

	public void sjfp(String opcao){
		this.filaSJFP = new ArrayList();
		ArrayList<String> filaEspera = new ArrayList();
		ArrayList<String> clone = this.cloneComAlteracoes();

		int tempo_corrente = 0;
		int posicao = 0;
		int cont = 0;
		String processoCorrente;

		while(tempo_corrente <= this.sumCpuBurst()){
			// verificar aqui se o processo terminou, se sim manda a flag(ai ele olha a fila de espera)
			// posicao com cpu burst restante é a 6
			// se não ele olha somente se há novos processos
			if(tempo_corrente == 0){
				processoCorrente = buscarProcesso(clone, filaEspera, "", tempo_corrente, true);
				String [] aux = processoCorrente.split(VIRGULA);
				String montagem = aux[0] + "," + aux[1] + "," + aux[2] + "," + aux[3] + "," + Integer.toString(tempo_corrente) + ",?," + Integer.toString(Integer.parseInt(aux[6]) - 1);
				filaSJFP.add(montagem);
				posicao++;
				cont = 0;
			} else if(tempo_corrente == this.sumCpuBurst()){
				String[] aux = filaSJFP.get(posicao-1).split(VIRGULA);
				String montagem = aux[0] + "," + aux[1] + "," + aux[2] + "," + aux[3] + "," + aux[4] + "," + Integer.toString(tempo_corrente) + ",0";
				filaSJFP.set(posicao-1, montagem);
			} else if(cont == Integer.parseInt(filaSJFP.get(posicao-1).split(VIRGULA)[6])){
				// true significa que terminou
				String[] aux = filaSJFP.get(posicao-1).split(VIRGULA);
				String montagem = aux[0] + "," + aux[1] + "," + aux[2] + "," + aux[3] + "," + aux[4] + "," + Integer.toString(tempo_corrente) + ",0";
				filaSJFP.set(posicao-1, montagem);
				
				processoCorrente = buscarProcesso(clone, filaEspera, filaSJFP.get(posicao-1), tempo_corrente, true);
				
				aux = processoCorrente.split(VIRGULA);
				montagem = aux[0] + "," + aux[1] + "," + aux[2] + "," + aux[3] + "," + Integer.toString(tempo_corrente) + ",?," + Integer.toString(Integer.parseInt(aux[6]) - 1);
				filaSJFP.add(montagem);
				posicao++;
				cont = 0;

			} else {
				// false significa que não terminou
				processoCorrente = buscarProcesso(clone, filaEspera, filaSJFP.get(posicao-1), tempo_corrente, false);
				if(processoCorrente != filaSJFP.get(posicao-1)){
					// atualiza o tempo que falta para executar do processo na string 
					// adiciona o processo na fila de espera
					// adiciona o novo processo na fila
					// cont = 1
					String[] aux = filaSJFP.get(posicao-1).split(VIRGULA);
					String montagem = aux[0] + "," + aux[1] + "," + aux[2] + "," + aux[3] + "," + aux[4] + "," + Integer.toString(tempo_corrente) + "," + Integer.toString((Integer.parseInt(aux[6]) - cont));
					filaSJFP.set(posicao-1, montagem);
					filaEspera.add(montagem);
					
					aux = processoCorrente.split(VIRGULA);
					montagem = aux[0] + "," + aux[1] + "," + aux[2] + "," + aux[3] + "," + Integer.toString(tempo_corrente) + ",?," + Integer.toString(Integer.parseInt(aux[6]) - 1);
					filaSJFP.add(montagem);
					posicao++;
					cont = 0;
				} else {
					cont++;
				}
			}		
			tempo_corrente++;
		}

		if(opcao == "1"){
			System.out.println("a. Algoritmo: SJF Preemptivo");
			System.out.println("b. Tempo total de processamento: " + (this.sumCpuBurst() + filaSJFP.size()-1));
			System.out.println("c. Percentual de utilização da CPU: " + this.percentualUsoCpu(filaSJFP));
			System.out.println("d. Média Throughput dos processos: " + this.mediaThoughput(filaSJFP));
			System.out.println("e. Média Turnaround dos processos: " + this.mediaTurnAround(filaSJFP));
			System.out.println("f. Média Tempo de Espera dos processos: " + this.mediaTempoEspera(filaSJFP));
			System.out.println("g. Média de Tempo de Resposta dos processos: " + this.mediaTempoResposta(filaSJFP));
			System.out.println("h. Média de troca de contextos: " + this.mediaTrocaContexto(filaSJFP));
			System.out.println("i. Número de processos executados: " + this.processos.size());
		} else if(opcao == "2"){
			this.imprimirBonito(filaSJFP);
		} else {
			System.out.println("Opção inválida!");
		}
	}

	public  void sjf(String opcao){
		this.filaSJF = cloneComAlteracoes();
		for(int i = 0; i < this.filaSJF.size(); i++){
			for(int j = 0; j <i; j++){
				if(Float.parseFloat(this.filaSJF.get(i).split(VIRGULA)[0]) <= Float.parseFloat(this.filaSJF.get(j).split(VIRGULA)[0])){
					if(Float.parseFloat(this.filaSJF.get(i).split(VIRGULA)[2]) <= Float.parseFloat(this.filaSJF.get(j).split(VIRGULA)[2])){
						String aux = this.filaSJF.get(j);
						this.filaSJF.set(j, this.filaSJF.get(i));
						this.filaSJF.set(i, aux);
					}
				}
			}
		}

		int tempo = 0;
		for(int i = 0; i < filaSJF.size(); i++){
			String aux [] = filaSJF.get(i).split(VIRGULA);
			// tempo de inicio
			aux[4] = Integer.toString(tempo);
			// tempo de fim
			tempo += Integer.parseInt(aux[2]); 
			aux[5] = Integer.toString(tempo);
			String montagem = aux[0] + "," + aux[1] + "," + aux[2] + "," + aux[3] + "," + aux[4] + "," + aux[5] + ",0";
			filaSJF.set(i, montagem); 
		}

		if(opcao == "1"){
			System.out.println("a. Algoritmo: SJF Não-Preemptivo");
			System.out.println("b. Tempo total de processamento: " + (this.sumCpuBurst() + filaSJF.size()-1));
			System.out.println("c. Percentual de utilização da CPU: " + this.percentualUsoCpu(filaSJF));
			System.out.println("d. Média Throughput dos processos: " + this.mediaThoughput(filaSJF));
			System.out.println("e. Média Turnaround dos processos: " + this.mediaTurnAround(filaSJF));
			System.out.println("f. Média Tempo de Espera dos processos: " + this.mediaTempoEspera(filaSJF));
			System.out.println("g. Média de Tempo de Resposta dos processos: " + this.mediaTempoResposta(filaSJF));
			System.out.println("h. Média de troca de contextos: " + this.mediaTrocaContexto(filaSJF));
			System.out.println("i. Número de processos executados: " + this.processos.size());
		} else if(opcao == "2"){
			imprimirBonito(filaSJF);
		} else {
			System.out.println("Opção inválida!");
		}
	}

	public  void rr(String opcao){
		int quantum = 3, time = 0, nproc;
		float total_time, context = 0;
		ArrayList<String> ganttRR = new ArrayList(); //<ID> <tempo_ini> <tempo_fim>
		nproc = this.processos.size();

		int[] rburst = new int[nproc];	//vai contabilizar o burst restante de cada processo
		int[] turnA = new int[nproc];	//vai contabilizar o turnaround de cada processo 
		int[] wait = new int[nproc];	//vai acumular o tempo de espera dos processos
		int[] resp = new int[nproc];	//vai acumular o tempo de espera dos processos
		int[] last = new int[nproc];	//vai guardar o ultimo tempo de processamento, auxiliar pra waiting time
		boolean[] touch = new boolean[nproc];	//vai guardar o ultimo tempo de processamento, auxiliar pra waiting time
		
		for (int i = 0; i < nproc ; i++){
			rburst[i] = Integer.parseInt(this.processos.get(i).split(VIRGULA)[2]) ; //tamanho de burt inicial
			turnA[i] = Integer.parseInt(this.processos.get(i).split(VIRGULA)[0]);
			wait[i] = 0;
			touch[i] = false;
		}

		total_time = IntStream.of(rburst).sum();

		while( IntStream.of(rburst).sum() != 0){ // enquanto algum processo ainda tiver burst
			for (int i = 0; i < nproc ; i++){
				if (rburst[i] == 0 || Integer.parseInt(this.processos.get(i).split(VIRGULA)[0]) > time)
					continue;
				else if (rburst[i] >= quantum ){
					if (touch[i] == false){						
						wait[i] = time - Integer.parseInt(this.processos.get(i).split(VIRGULA)[0]);
						resp[i] = wait[i];
						touch[i] = true;
					}
					else{
						wait[i] = wait[i] + (time - last[i]);
					}
					rburst[i] = rburst[i] - quantum;	// reduz o tempo que foi processado 
					String [] aux = processos.get(i).split(VIRGULA);
					String mount = aux[1] + "," + Integer.toString(time) + "," + Integer.toString(time + quantum); // montando linha do gantt, vai ser no seguinte formato: //<ID> <tempo_ini> <tempo_fim>
					ganttRR.add(mount);
					time = time + quantum;	//atualizando o tempo
					last[i] = time;
					context = context +1;	//adicionando troca de contexto
					if (rburst[i] == 0) turnA[i] = time - turnA[i];	// se o processo tiver acabado, calcula o turnaround
				}
				else{
					if (touch[i] == false){						
						wait[i] = time - Integer.parseInt(this.processos.get(i).split(VIRGULA)[0]);
						resp[i] = wait[i];
						touch[i] = true;
					}
					else{
						wait[i] = wait[i] + (time - last[i]);
					}
					String [] aux = processos.get(i).split(VIRGULA);
					String mount = aux[1] + "," + Integer.toString(time) + "," + Integer.toString(time + rburst[i]);
					ganttRR.add(mount);
					time = time + rburst[i];
					rburst[i] = 0;
					turnA[i] = time - turnA[i];
					context = context +1;
				}
			}
		}
		context = context - 1;	//ajuste necessário, pois o primeiro processamento n envolve troca de contexto		

		if (opcao == "1") {//estatisticas
			System.out.println( "a. Algoritmo Round Robin. quantum = " + quantum);
			System.out.println( "b. Tempo total de processamento = " + (total_time));
			System.out.println( "c. Percentual de utilização da CPU  = " + ((total_time)/(total_time+context))); // não sei como calcular o tempo de troca de contexto
			System.out.println( "d. Média troughput dos processos = " + (float) nproc/(total_time) );
			System.out.println( "e. Média turnaround dos processos = " + (float) IntStream.of(turnA).sum()/nproc );
			System.out.println( "f. Média tempo de espera = " +  (float) IntStream.of(wait).sum()/nproc );
			System.out.println( "g. Média tempo de Resposta dos processos = " +  (float) IntStream.of(resp).sum()/nproc ); // não sei como calcular o tempo de respostaa
			System.out.println( "h. Média de troca de contextos = " + (float) context/nproc); 
			System.out.println( "i. Número de processos executados = " + nproc);
		}
		else if (opcao == "2"){
			float proct;
			for (int i = 0; i < ganttRR.size() ; i++){ //não sei se precisa do "this" antes de pegar o size do array
				System.out.println( "a. ID do processo = " + ganttRR.get(i).split(VIRGULA)[0]);
				proct = (Integer.parseInt(ganttRR.get(i).split(VIRGULA)[2]) - Integer.parseInt(ganttRR.get(i).split(VIRGULA)[1]));
				System.out.println( "b. Tempo de processamento = " + proct);
				System.out.println();
			}		
		} else {
			System.out.println("Opção inválida!");
		}
	}

	public void fcfs(String opcao){
		int time = 0, nproc;
		float total_time = 0, context = 0;

		nproc = this.processos.size();
		context = nproc -1;

		int[] start = new int[nproc];
		int[] fin   = new int[nproc];
		int[] wait  = new int[nproc];
		int[] turnA = new int[nproc];

		bubbleSort(this.processos);

		for (int i = 0; i < nproc ; i++){
			wait[i] = Integer.parseInt(this.processos.get(i).split(VIRGULA)[0]);
			start[i] = time;
			time = time + Integer.parseInt(this.processos.get(i).split(VIRGULA)[2]);
			wait[i] = start[i] - wait[i];
			fin[i] = time;
			turnA[i] = fin[i] - Integer.parseInt(this.processos.get(i).split(VIRGULA)[0]);	
		}		

		if(opcao == "1"){
			System.out.println( "a. Algoritmo FCFS.");
			System.out.println( "b. Tempo total de processamento = " + (time));
			System.out.println( "c. Percentual de utilização da CPU  = " + ((time)/(time + context))); // não sei como calcular o tempo de troca de contexto
			System.out.println( "d. Média troughput dos processos = " + (float) nproc/(time));
			System.out.println( "e. Média turnaround dos processos = " + (float) IntStream.of(turnA).sum()/nproc );
			System.out.println( "f. Média tempo de espera = " +  (float) IntStream.of(wait).sum()/nproc );
			System.out.println( "g. Média tempo de Resposta dos processos = " +  (float) IntStream.of(wait).sum()/nproc ); // não sei como calcular o tempo de respostaa
			System.out.println( "h. Média de troca de contextos = " + (float) context/nproc); 
			System.out.println( "i. Número de processos executados = " + nproc);
			System.out.println();

		} else if(opcao == "2"){			
			for (int i = 0; i < nproc ; i++){ //não sei se precisa do "this" antes de pegar o size do array
				System.out.println( "a. ID do processo = " + this.processos.get(i).split(VIRGULA)[1]);				
				System.out.println( "b. Tempo de processamento = " + this.processos.get(i).split(VIRGULA)[2]);
				System.out.println();
			}
		} else{
			System.out.println("Opção inválida!");
		}
	}

	public ArrayList<String> adicionarCampoString(){
		ArrayList<String> processos = new ArrayList<>();
		int limite = this.processos.size();
		for(int i = 0; i < limite; i++){
			processos.add(this.processos.get(i) + ",0,0,0,0");
		}

		return processos;
	}

	public double somatorio(ArrayList<String> listaProcessos, int pos){
		int sum = 0;
		for(int i = 0; i < listaProcessos.size(); i++){
			sum += Integer.parseInt(listaProcessos.get(i).split(VIRGULA)[pos]);
		}
		return sum;
	}

	public double calcularMedia(ArrayList<String> listaProcessos, int pos){
		return somatorio(listaProcessos, pos)/(double)listaProcessos.size();
	}

	public void calcularEstaticica(String nomeAlg, ArrayList<String> listaProcessosExecutados){
		double tempoCPUburst = somatorio(this.processos, 2);
		double tempoTrocaContexto = (double)(this.diagrama.size()-1.0); 
		double mediaThoughput = this.processos.size() / (double)(tempoCPUburst + tempoTrocaContexto);
		double tempoProcessamento = tempoCPUburst + tempoTrocaContexto;
		double percentualUsoCpu = (tempoProcessamento - tempoTrocaContexto)/(double)tempoProcessamento;
		System.out.println("a. Algoritmo " + nomeAlg);
		System.out.println("b. Tempo total de processamento = " + tempoProcessamento );
		System.out.println("c. Percentual de utilização da CPU  = " + percentualUsoCpu);
		System.out.println("d. Média troughput dos processos = " + mediaThoughput);
		System.out.println("e. Média turnaround dos processos = " + Double.toString(calcularMedia(listaProcessosExecutados, 4)));
		System.out.println("f. Média tempo de espera = " + Double.toString(calcularMedia(listaProcessosExecutados, 5)));
		System.out.println("g. Média tempo de Resposta dos processos = " + Double.toString(calcularMedia(listaProcessosExecutados, 6)));
		System.out.println("h. Média de troca de contextos = " + (this.diagrama.size() - 1.0)/(double)this.processos.size());
		System.out.println("i. Número de processos executados = " + this.processos.size());
	}

	public void exibirDiagrma(ArrayList<String> diagrama){
		String[] processo;
		for (int i = 0; i < diagrama.size; i++){
			processo = diagrama.get(i).split(","); 
			System.out.println("a. ID processo = "+ processado[0])
		}
	}

	public String construirString(String[] processo){

		String dadosProcesso = "";
		for (int k = 0; k < 8; k++){
			if (k < 7) {
				dadosProcesso += processo[k] + ",";
			}else {
				dadosProcesso += processo[k];
			}
		}
		return  dadosProcesso;

	}

	public int processoMenosPrioridade(int limite, int prioridade, ArrayList<String> processos){
		int pos = -1, tempo = 1000;
		for (int i = 0; i < processos.size(); i++) {
			String[] dados = processos.get(i).split(",");
			if (limite > Integer.parseInt(dados[0])) {
				if (prioridade > Integer.parseInt(dados[3])) {
					if(tempo >= Integer.parseInt(dados[0])){
						tempo = Integer.parseInt(dados[0]);
						pos = i;
					}
				}
			}
		}
		return pos;
	}

	public String[] atualizarInfoProcessoFinalizado(String[] processo, int tempoInicio){
		int tempoChegadaP = Integer.parseInt(processo[0]);
		processo[4] = Integer.toString(Integer.parseInt(processo[4])+ (tempoInicio - tempoChegadaP) + Integer.parseInt(processo[2]));
		processo[5] = Integer.toString(Integer.parseInt(processo[5]) + tempoInicio - tempoChegadaP);
		if(Integer.parseInt(processo[7]) == 0) {
			processo[6] = Integer.toString(tempoInicio - tempoChegadaP);
			processo[7] = "0";
		}

		return processo;

	}

	public String[] atualizarInfoProcessoFila(String[] processo, String[] processo2, int tempoInicio){

		int tempoChegadaP = Integer.parseInt(processo[0]);
		processo[4] = Integer.toString(Integer.parseInt(processo[4]) + (tempoInicio - tempoChegadaP)
				+ (Integer.parseInt(processo2[0]) - tempoInicio));
		processo[2] = Integer.toString(Integer.parseInt(processo[2]) - (Integer.parseInt(processo2[0]) - tempoInicio));
		processo[5] = Integer.toString(Integer.parseInt(processo[5]) + tempoInicio - tempoChegadaP);
		if(Integer.parseInt(processo[7]) == 0) {
			processo[6] = Integer.toString(tempoInicio - tempoChegadaP);
			processo[7] = "1";
		}
		processo[0] = processo2[0];

		return processo;
	}

	public void priority(String opcao){
		int prioridade = 1000;
		int pos = 0, tempoUltimoProcesso = 0, tempoChegadaP = 0, existeProcesso = 0;

		ArrayList<String> execucao = new ArrayList<>();

		ArrayList<String> processos = adicionarCampoString();

		while(processos.size() > 0) {

			for (int i = 0; i < processos.size(); i++) {
				String[] dados = processos.get(i).split(",");
				if (prioridade > Integer.parseInt(dados[3])) {
					if (tempoUltimoProcesso >= Integer.parseInt(dados[0])) {
						pos = i;
						prioridade = Integer.parseInt(dados[3]);
						existeProcesso = 1;
					}
				}
			}

			if(existeProcesso == 1) {
				String[] processo = processos.get(pos).split(",");
				tempoChegadaP = Integer.parseInt(processo[0]);
				processo[4] = Integer.toString((tempoUltimoProcesso - tempoChegadaP) + Integer.parseInt(processo[2]));
				processo[5] = Integer.toString(tempoUltimoProcesso - tempoChegadaP);
				processo[6] = Integer.toString(tempoUltimoProcesso - tempoChegadaP);

				String dadosP = construirString(processo);
				execucao.add(dadosP);

				String execucaoAtual = processo[1] + "," + tempoUltimoProcesso + ",";
				tempoUltimoProcesso = tempoUltimoProcesso + Integer.parseInt(processo[2]);
				execucaoAtual = execucaoAtual + tempoUltimoProcesso;

				this.diagrama.add(execucaoAtual);
				processos.remove(pos);

			}else{
				tempoUltimoProcesso ++;
			}
			pos = 0;
			prioridade = 1000;
			existeProcesso = 0;

		}

		if(opcao == "1") {
			calcularEstaticica("Prioridade Não Preemptivo", execucao);
			System.out.println(estatisticas);
		}else if(opcao == "2"){
			System.out.println(diagrama);
			System.out.println(estatisticas);
		}else {
			System.out.println("Opção inválida!");
		}
	}


	public void priorityP(String opcao){

		int prioridade = 1000;
		int pos = 0, tempoUltimoProcesso = 0;
		int existeP = 0;
		ArrayList<String> execucao = new ArrayList<>();

		ArrayList<String> processos = adicionarCampoString();

		while(processos.size() > 0) {
			for (int i = 0; i < processos.size(); i++) {
				String[] dados = processos.get(i).split(",");
				if (tempoUltimoProcesso >= Integer.parseInt(dados[0]) ) {
					if (prioridade > Integer.parseInt(dados[3])) {
						pos = i;
						prioridade = Integer.parseInt(dados[3]);
						existeP = 1;
					}
				}

			}
			if(existeP == 1){
				String[] processo = processos.get(pos).split(",");

				int verificacao = processoMenosPrioridade(Integer.parseInt(processo[2])+ tempoUltimoProcesso, Integer.parseInt(processo[3]), processos);
				if(verificacao != -1){
					String[] processo3 = processos.get(verificacao).split(",");

					String[] dadosProcessados = atualizarInfoProcessoFila(processo, processo3, tempoUltimoProcesso);

					String dadosP = construirString(dadosProcessados);

					String execucaoAtual = processo[1] + "," + tempoUltimoProcesso + ",";

					tempoUltimoProcesso = tempoUltimoProcesso + Math.abs(tempoUltimoProcesso - Integer.parseInt(processo3[0]));
					processos.remove(pos);
					processos.add(dadosP);

					execucaoAtual = execucaoAtual+tempoUltimoProcesso;
					this.diagrama.add(execucaoAtual);

				}else{
					String[] dadosProcesso = atualizarInfoProcessoFinalizado(processo, tempoUltimoProcesso);

					String dadosP = construirString(dadosProcesso);

					String execucaoAtual = processo[1] + "," + tempoUltimoProcesso + ",";

					tempoUltimoProcesso = tempoUltimoProcesso + Integer.parseInt(dadosProcesso[2]);
					execucao.add(dadosP);
					processos.remove(pos);
					execucaoAtual = execucaoAtual + tempoUltimoProcesso;

					this.diagrama.add(execucaoAtual);

				}

			}else{
				tempoUltimoProcesso++;
			}

			pos = 0;
			prioridade = 1000;
			existeP = 0;
		}
		if(opcao == "1"){
			calcularEstaticica("Prioridade Preemptivo", execucao);
			System.out.println(estatisticas);
		} else if(opcao == "2"){
			System.out.println(diagrama);
		} else {
			System.out.println("Opção inválida!");
		}
	}

	public static void main(String[] args) throws Exception{
		Escalonador escalonador = new Escalonador();  
		Scanner reader = new Scanner(System.in);
		escalonador.lerArquivo("dados.csv");
		
		escalonador.fcfs("1");
		escalonador.fcfs("2");

		escalonador.sjf("1");
		escalonador.sjf("2");

		escalonador.sjfp("1");
		escalonador.sjfp("2");

		escalonador.priority("1");
		escalonador.priorityP("2");

		escalonador.rr("1");
		escalonador.rr("2");
		
		///////////////////////////////////// CONSOLE
		
		// System.out.println("Digite o nome do arquivo desejado: ");
		// String dados = reader.next();
		
		// escalonador.lerArquivo(dados);

		// System.out.println("Você deseja: \n (1) Visualizar Estatisticas\n(2) Lista de processos executados\n");
		// String opcao = reader.next();

		// System.out.println("Selecione o método desejado:\n(1) FCFS\n(2) SJF\n(3) SJFP\n(4) Priority\n(5) PriorityP\n(6) RR\n Por favor, selecione algum dos números acima");
		// String opcAlgoritm = reader.next();
    	
  //   	switch(opcAlgoritm){
  //   		case "1":
  //   			escalonador.fcfs(opcao);
  //   			break;
  //   		case "2":
  //   			escalonador.sjf(opcao);
  //   			break;
  //   		case "3":
  //   			escalonador.sjfp(opcao);
  //   			break;
  //   		case "4":
  //   			escalonador.priority(opcao);
  //   			break;
  //   		case "5":
  //   			escalonador.priorityP(opcao);
  //   			break;
  //   		case "6":
  //   			escalonador.rr(opcao);
  //   			break;
  //   		default:
  //   			System.out.println("Opção selecionada inválida");
  //   			break;
  //   	};
		// escalonador.sjf("1");
		// for(int i = 0; i < escalonador.filaSJF.size(); i++){
		// 	System.out.println(escalonador.filaSJF.get(i));
		// }   	

    }
}