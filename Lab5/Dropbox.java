public class Dropbox {
	private int number;
	private boolean evenNumber = false;
	public boolean available = true;
	public boolean producerPodeProduzir = true;

	public synchronized int take(final boolean even) throws InterruptedException{
	 	if(available == true  && checkNumber()==even){
			 System.out.format("%s CONSUMIDOR obtem %d.%n", even ? "PAR" : "IMPAR", number);
			 alterarDisponibilidade(false);
			 producerPodeProduzir = true;
		} else{
			wait();
		}
		return number;
	}

	public boolean checkNumber(){
	 	if(this.number % 2 == 0){
	 		return true;
	 	}
	 	return false;
	}

	public synchronized void alterarDisponibilidade(boolean x){
	 	available = x;
	}

	public synchronized void put(int number)  throws InterruptedException{
	 	if(producerPodeProduzir == true){
			 this.number = number;
			 evenNumber = number % 2 == 0;
			 
			 System.out.format("PRODUTOR gera %d.%n", number);
			 
			 alterarDisponibilidade(true);
			 producerPodeProduzir = false;
			 notifyAll();
		}
	}
}