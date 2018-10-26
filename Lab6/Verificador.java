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
	ArrayList<String> processos;

	Verificador(){}

	public void lerArquivo(String arquivo) throws Exception{
		BufferedReader readeer = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo)));
    	String linha = null;
    	
    	while ((linha = readeer.readLine()) != null) {
            this.processos.add(linha);
        }

        // calcular n e m  e atribuir a variaveis
        // aparentemente n = processos.size() e m = processos[0].split() -> Ai (tamanho do vetor splitado - 1 )/2
        int n = this.processos.size();
        String[] aux = this.processos.get(0).split(",");
        int m = (aux.length - 1)/2;
        
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
	}

	public void safety(){}

	public void avoid(String pi){}
	public void detection(){}

	public static void main(String[] args) throws Exception{}


}