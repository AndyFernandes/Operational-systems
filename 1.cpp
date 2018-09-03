#include<stdio.h>
#include<math.h>

using namespace std;

void fib(int inicio, int fim){

	while(inicio < fim){
		cout << (1/sqrt(5)) * pow(((1 + sqrt(5))/2), inicio) - (1/sqrt(5)) * pow(((1 - sqrt(5))/2), inicio)
		inicio++;
	}

}

void fibWithFork(int inicio, int fim){

}

int main(){
	
	int inicio, fim;
	cout << "\nInício: ";
	cin >> inicio;

	cout << "\nFim: ";
	cin >> fim;

	if(fim > inicio){
		fib(inicio, fim);
	}
	
	return 0;
}