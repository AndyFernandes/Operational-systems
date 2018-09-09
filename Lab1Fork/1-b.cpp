// Alunas: 		Andreza Fernandes de Oliveira - 384341
//				Arina de Jesus Amador Monteiro Sanches - 392476
// 07.09.2018
#include<errno.h>
#include<signal.h>
#include<stdio.h>
#include<unistd.h>
#include<stdlib.h>
#include <math.h>
#include<iostream>
#include<sys/wait.h>
using namespace std;

double calcFibonacci(int n){	
	return (1.0/sqrt(5.0)) * pow(((1.0 + sqrt(5.0))/2.0), n) - (1.0/sqrt(5.0)) * pow(((1.0 - sqrt(5.0))/2.0), n);
}

void imprimirSequencia(int inicio, int fim){
	for(int i = inicio -1; i < fim; i++){
		cout << calcFibonacci(i) << " ";
	}
}

int main(){
	int pid, status;
	int inicio, fim;
	
	cout << "\nPosição Inicial: ";
	cin >> inicio;

	cout << "\nPosição Final: ";
	cin >> fim;

	while (inicio < 0){
		cout << "\nNumero inválido. Digite novamente a posição Inicial: ";
		cin >> inicio;
	}

	while (fim < 0){
		cout << "\nNumero inválido. Digite novamente a posição Final: ";
		cin >> fim;
	}

	int meio = (fim + inicio)/2;
	pid = vfork();
	pid = vfork();

	if(pid < 0){
		perror("\nFalha ao fazer o fork.");
	} else if (pid == 0){
		//cout << "\nEu sou o processo " << getpid() << ", filho do processo " << getppid() << ". Fiquei encarregado de imprimir essa parte da Sequência de Fibonnaci"<< endl;
		imprimirSequencia(inicio, meio);
		inicio = meio + 1;
		meio = fim;
	} else {
		//cout << "\nOlá, eu sou o pai " << getpid() << ", o meu filho é " << pid << endl;
		wait(&status);
	}

	exit(0);
}