#include <pthread.h>
#include <stdio.h>
#include <math.h>
#include<stdlib.h>

void *runner(void* arg); /* the thread */
void imprimir(double* termosFib, int n);
double calcFibonacci(int n);

int main(void){
	pthread_t tid;
	
	int numeroTermos;
	printf("Digite o número de termos: ");
	scanf("%d", &numeroTermos);
	double termosFib[numeroTermos]; 
	
	termosFib[0] = numeroTermos;
	pthread_create(&tid, NULL,runner, (void *)termosFib);
	
	pthread_join(tid,NULL);
	
	imprimir(termosFib, numeroTermos);
}

void imprimir(double* termosFib, int n){
	for(int i = 0; i < n; i++){
		printf("%0.lf  ", termosFib[i]);
	}
}

double calcFibonacci(int n){	
	return (1.0/sqrt(5.0)) * pow(((1.0 + sqrt(5.0))/2.0), n) - (1.0/sqrt(5.0)) * pow(((1.0 - sqrt(5.0))/2.0), n);
}

void *runner(void* arg){
	int i;
	double* termosFib = (double *) arg;
	int numeroTermos = termosFib[0];

	if (numeroTermos > 0) {
		for (i = 0; i < numeroTermos; i++){
			termosFib[i] = calcFibonacci(i);
		}
	}
	pthread_exit(0);
}