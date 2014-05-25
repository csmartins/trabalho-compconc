import java.io.IOException;
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
	 * 
	 * @param id identificador do elevador
	 * @param andarDeInicio andar de in�cio do elevador
	 * @param monitor inst�ncia �nica do monitor (� necess�rio que seja a mesma inst�ncia para todas
	 * as threads (elevadores) em execu��o.
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
	 * @see Elevador#processarTarefa() processarTarefa
	 * @see Elevador#imprimirStatusTarefa(List) imprimirStatusTarefa
	 * @see MonitorDeTarefas#finalizarTarefa(int, int) finalizarTarefa
	 * @see MonitorDeTarefas#escolherTarefa(Elevador) escolherTarefas
	 * @see MonitorDeTarefas#escreverNoArquivo(String, int) escreverNoArquivo 
	 */
	public void run()
	{
		try {
			System.out.println("Elevador "+id+" come�ou no andar "+andarDeInicio);
			monitor.escreverNoArquivo("Elevador "+id+" come�ou no andar "+andarDeInicio, id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true)
		{
			try {
				System.out.println("Elevador "+id+" vai escolher");
				monitor.escreverNoArquivo("Elevador "+id+" vai escolher", id);
				tarefaElevador = monitor.escolherTarefa(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
					
				
			if(tarefaElevador == null){
				try {
					System.out.println("Elevador "+id+" morreu");
					monitor.escreverNoArquivo("Elevador "+id+" morreu", id);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			
			this.idTarefaElevador = tarefaElevador.getIdTarefa();
			this.andarDeInicio = tarefaElevador.getAndarDeInicio();
			this.sentido = tarefaElevador.getSentido();
			this.requisicoes = tarefaElevador.getRequisicoes();
			
			imprimirStatusTarefa(tarefaElevador.getRequisicaoBruta());
			
			processarTarefa();
			
			try {
				monitor.finalizarTarefa(id, tarefaElevador.getIdTarefa());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * M�todo respons�vel por imprimir e escrever no arquivo de log o status da tarefa passada como argumento.
	 * 
	 * @param requisicao lista com os andares requisitados
	 * 
	 * @see MonitorDeTarefas#escreverNoArquivo(String, int) escreverNoArquivo
	 */
	public void imprimirStatusTarefa(List<Integer> requisicao)
	{
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
		try {
			System.out.println("Elevador " + id+ ": " +status);
			monitor.escreverNoArquivo("Elevador " + id+ ": " +status, id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * M�todo respons�vel por fazer o elevador atender a todas as requisi��es, movendo-se de um andar para o outro.
	 * 
	 * @see Elevador#imprimirStatusTarefa(List) imprimirStatusTarefa
	 */
	public void processarTarefa()
	{
		andarAtual = andarDeInicio;
		imprimirStatusTarefa(requisicoes);
		
		while(requisicoes.size()>0){
			Integer andando = requisicoes.get(0);
			andarAtual = andando;
			requisicoes.remove(0);
			imprimirStatusTarefa(requisicoes);
		}
		
		
	}

}
