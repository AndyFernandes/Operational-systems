import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream; 

public class Verificador {
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
        // instanciar os bixos
        // max = new int[n][m];
        // quando terminar de ler aqui povoar o available, max, allocation, need
	}

	public void safety(){}

	public void avoid(String pi){}
	public void detection(){}


}