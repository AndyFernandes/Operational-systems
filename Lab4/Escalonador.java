// <Tempo de chegada>, <ID do Processo>, <Burst Time>, <Prioridade>
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

	public  void sjfsNaoPreemptivo(){
		// Levar em consideração o tempo de chegada e o burst time
		// DEIXAR ISSO AQUI MAIS BONITINHO e realmente testar se não vai ter conflito nessas duas condições do tempo de chegada com o burst time
		for(int i = 0; i < this.processos.size(); i++){
			for(int j = 1; j < this.processos.size(); j++){
				if(Float.parseFloat(this.processos.get(i).split(VIRGULA)[0]) <= Float.parseFloat(this.processos.get(j).split(VIRGULA)[0])){
					if(Float.parseFloat(this.processos.get(i).split(VIRGULA)[2]) <= Float.parseFloat(this.processos.get(j).split(VIRGULA)[2])){
						String aux = this.processos.get(j);
						this.processos.set(j, this.processos.get(i));
						this.processos.set(i, aux);
					}
				}
			}
		}
	}

	public void sjfsPreemptivo(){
		// realizar a troca dos processos e armazenar o tempo que falta para terminar execução.
	}

	public static void main(String[] args) throws Exception{
		Escalonador escalonador = new Escalonador();  
		escalonador.lerArquivo("dados.csv");
        //String[] dados = escalonador.processos.get(0).split(VIRGULA);
        //System.out.println("Tempo Chegada: " + dados[0] + "| id: " + dados[1] + " | burst time: " + dados[2] + " | prioridade: " + dados[3] + "\n");
    	escalonador.sjfsNaoPreemptivo();
    	escalonador.imprimir();
    }
}