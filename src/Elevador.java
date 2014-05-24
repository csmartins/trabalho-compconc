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
		System.out.println("Elevador "+id+" começou no andar "+andarDeInicio);
		
		while(true)
		{
			synchronized (monitor) 
			{
				try 
				{
					while(monitor.listaDeTarefas.isEmpty())
					{
						System.out.println("Elevador "+ this.id + " esperando");
						this.wait();
					}
					
					tarefaElevador = monitor.escolherTarefa(andarAtual, id);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(tarefaElevador.equals(null)){
					//kill thread
					break;
				}
			}
			
			this.idTarefaElevador = tarefaElevador.getIdTarefa();
			this.andarDeInicio = tarefaElevador.getAndarDeInicio();
			this.sentido = tarefaElevador.getSentido();
			this.requisicoes = tarefaElevador.getRequisicoes();
			
			imprimeStatusTarefa(tarefaElevador.getRequisicaoBruta());
			
			processaTarefa();
			
			monitor.finalizaTarefa(id, tarefaElevador.getIdTarefa());
		}
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
		System.out.println("Elevador " + id+ ": " +status);
		
	}
	
	public void processaTarefa(){
		
		andarAtual = andarDeInicio;
		imprimeStatusTarefa(requisicoes);
		
		while(requisicoes.size()>0){
			Integer andando = requisicoes.get(0);
			andarAtual = andando;
			requisicoes.remove(0);
			imprimeStatusTarefa(requisicoes);
		}
		
		
	}

}
