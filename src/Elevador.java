import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Patrícia S. Ghiraldelli e Carlos Eduardo S. Martins
 *
 */
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
	
	/**
	 * Construtor da classe Elevador, responsável por instanciar suas variáveis locais.
	 * @param id identificador do elevador
	 * @param andarDeInicio andar de início do elevador
	 * @param monitor instância única do monitor (é necessário que seja a mesma instância para todas
	 * as threads (elevadores) em execução.
	 * 
	 */
	public Elevador(int id, int andarDeInicio, MonitorDeTarefas monitor) 
	{
		this.id =id;
		this.andarDeInicio = andarDeInicio;
		this.andarAtual = andarDeInicio;
		this.monitor = monitor;
		requisicoes= new ArrayList<Integer>();
	}
	
	/**
	 * Método que pede tarefas ao monitor (através do método escolheTarefas)até que uma tarefa venha como null (ocorreu um erro na escolha ou acabaram as tarefas),
	 * processa tais tarefas e finaliza-as.
	 * 
	 * @see processaTarefa
	 * @see finalizaTarefa
	 * @see escolheTarefas
	 * 
	 */
	public void run()
	{
		System.out.println("Elevador "+id+" começou no andar "+andarDeInicio);
		
		while(true)
		{
			System.out.println("Elevador "+id+" vai escolher");
			tarefaElevador = monitor.escolherTarefa(this);
					
				
			if(tarefaElevador == null){
				System.out.println("Elevador "+id+" morreu");
				break;
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
	
	/**
	 * Método responsável por fazer o elevador atender a todas as requisições, movendo-se de um andar para o outro.
	 */
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
