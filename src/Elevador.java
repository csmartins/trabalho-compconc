import java.util.ArrayList;
import java.util.List;


public class Elevador extends Thread{	
	
	int 		  id;
	int 	      andarDeInicio;
	Tarefa   	  tarefaElevador;
	int 		  sentido;
	int 		  numRequisicoes;
	int 		  idTarefaElevador;
	List<Integer> requisicoes; 
	Integer		  andarAtual;
	MonitorDeTarefas monitor;
	
	public Elevador(int id, int andarDeInicio, MonitorDeTarefas monitor) 
	{
		this.id =id;
		this.andarDeInicio = andarDeInicio;
		this.andarAtual = andarDeInicio;
		this.monitor = monitor;
		requisicoes= new ArrayList<Integer>();
	}
	
	public void run()
	{
		
		System.out.println("Elevador "+id+" começou");
		
		tarefaElevador = monitor.escolherTarefa(andarAtual);
		
		this.idTarefaElevador = tarefaElevador.getIdTarefa();
		this.andarDeInicio = tarefaElevador.getAndarDeInicio();
		this.sentido = tarefaElevador.getSentido();
		this.requisicoes = tarefaElevador.getRequisicoes();
		
		synchronized (this) {
			imprimeStatusTarefa(tarefaElevador.getRequisicaoBruta());
			
			processaTarefa();
		}
		
		this.andarDeInicio = this.andarAtual;
		
		monitor.finalizaTarefa(tarefaElevador);
		
	}
	
	public void imprimeStatusTarefa(List<Integer> requisicao){
		String status = andarAtual+" ("+idTarefaElevador+", "+andarDeInicio+", "+sentido+" (";
		if(requisicao.size() > 0){
			for(int i=0; i< requisicao.size(); i++){
				if(i == (requisicao.size()-1)) {
					status += requisicao.get(i)+"))";
				}
				else {
					status += requisicao.get(i)+", ";
				}
			}
		}
		else{
			status += "))";
		}
		System.out.println(status);
		
	}
	
	public void processaTarefa(){
		
		imprimeStatusTarefa(requisicoes);
		while(requisicoes.size()>0){
			Integer andando = requisicoes.get(0);
			andarAtual = andando;
			requisicoes.remove(0);
			imprimeStatusTarefa(requisicoes);
		}
		
		
	}

}
