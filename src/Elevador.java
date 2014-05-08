import java.util.ArrayList;
import java.util.List;


public class Elevador extends Thread{	
	
	
	int 		  id;
	int 	      andarDeInicio;
	Tarefa   	  tarefaElevador;
	int 		  andarDePartida;
	int 		  sentido;
	int 		  numRequisicoes;
	List<Integer> requisicoes; 
	Integer		  andarAtual;
	
	public Elevador(int id, int andarDeInicio, MonitorDeTarefas monitor) 
	{
		this.id =id;
		this.andarDeInicio = andarDeInicio;
		this.andarAtual = andarDeInicio;
		requisicoes= new ArrayList<Integer>();
	}
	
	public void run()
	{
		
		System.out.println("Thread "+id+" começou");
		tarefaElevador = MonitorDeTarefas.escolherTarefa(andarAtual);
	}

}
