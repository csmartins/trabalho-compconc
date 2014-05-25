import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Patr�cia S. Ghiraldelli e Carlos Eduardo S. Martins
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
	 * Construtor da classe Elevador, respons�vel por instanciar suas vari�veis locais.
	 * @param id identificador do elevador
	 * @param andarDeInicio andar de in�cio do elevador
	 * @param monitor inst�ncia �nica do monitor (� necess�rio que seja a mesma inst�ncia para todas
	 * as threads (elevadores) em execu��o.
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
	 * M�todo que pede tarefas ao monitor (atrav�s do m�todo escolheTarefas)at� que uma tarefa venha como null (ocorreu um erro na escolha ou acabaram as tarefas),
	 * processa tais tarefas e finaliza-as.
	 * 
	 * @see processaTarefa
	 * @see finalizaTarefa
	 * @see escolheTarefas
	 * 
	 */
	public void run()
	{
		System.out.println("Elevador "+id+" come�ou no andar "+andarDeInicio);
		
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
	 * M�todo respons�vel por fazer o elevador atender a todas as requisi��es, movendo-se de um andar para o outro.
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
