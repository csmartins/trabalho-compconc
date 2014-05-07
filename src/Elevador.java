import java.util.ArrayList;
import java.util.List;


public class Elevador extends Thread{	
	
	
	int 		  id;
	int 	      andarDeInicio;
	Tarefa   	  tarefaElevador;
	Tarefa 		  tarefaEscolhida;
	int 		  andarDePartida;
	int 		  sentido;
	int 		  numRequisicoes;
	List<Integer> requisicoes; 
	Integer		  andarAtual;
	
	public Elevador(int id, int andarDeInicio) 
	{
		this.id =id;
		this.andarDeInicio = andarDeInicio;
		this.andarAtual = andarDeInicio;
		requisicoes= new ArrayList<Integer>();
	}
	
	public void run()
	{
		System.out.println("Thread "+id+" começou");
		tarefaEscolhida = escolherTarefa(); 
	}

	public Tarefa escolherTarefa()
	{
		synchronized (this) 
		{
			tarefaElevador = MonitorDeTarefas.escolherTarefa(andarAtual);
		}
		//código para escolher melhor tarefa
		//pensar em como excluir a tarefa esscolhida do vetor
		return tarefaElevador;
	}
	
}
