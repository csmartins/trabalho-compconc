import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Essa classe funciona como um monitor, contendo todos os m�todos com acessos �s vari�veis 
 * compartilhadas pelos elevadores (threads).
 * 
 * @author Patr�cia S. Ghiraldelli e Carlos Eduardo S. Martisns
 */

public class MonitorDeTarefas 
{
	String [] 		  tarefasBrutas;
	
	ArrayList<Tarefa> listaDeTarefas;
	
	private final int DESCE = 0;
	private final int SOBE = 1;
	
	private boolean criandoTarefas;
	
	/**
	 * M�todo respons�vel por iniciar o monitor, instanciando vari�veis e recebendo o arquivo de tarefas.
	 * 
	 * @param tarefas � o vetor de tarefas j� lidas no arquivo de entrada 
	 * 
	 * @see MonitorDeTarefas#criarTarefas() criarTarefas
	 */
	public void iniciarMonitor(String[] tarefas)
	{
		this.listaDeTarefas = new ArrayList<Tarefa>();
		this.tarefasBrutas = tarefas;
		this.criarTarefas();
		
	}
	
	/**
	 * M�todo respons�vel por instanciar os objetos Tarefa com as informa��es do arquivo lido.
	 * A cada tarefa criada � dado um notify para acordar poss�veis elevadores que estejam aguardando
	 * a cria��o de tarefas.
	 * 
	 * @see MonitorDeTarefas#tratarTarefas(String, Tarefa) tratarTarefas
	 */
	public void criarTarefas() 
	{
		criandoTarefas = true;
		for(int i = 0; i < tarefasBrutas.length; i++)
		{
			synchronized (this) 
			{
				System.out.println("Criando tarefa " + i);
				Tarefa tarefa = new Tarefa(i);
				tratarTarefas(tarefasBrutas[i], tarefa);
				listaDeTarefas.add(tarefa);
				
				this.notifyAll();
			}
		}
		this.criandoTarefas = false;
	}

	/**
	 * M�todo respons�vel por instanciar o objeto Tarefa e trat�-los, excluindo repeti��es e ordenando corretamente.
	 * 
	 * @param tarefaBruta � a string referente a uma linha lida no arquivo de entrada, portanto uma posi��o do 
	 * vetor de tarefas recebido no  m�todo iniciaMonitor
	 * @param tarefa � o objeto criado Tarefa que receber� as informa��es da tarefaBruta
	 * 
	 * @see MonitorDeTarefas#ordenarRequisicoes(String[], int) ordenarRequisicoes
	 * @see MonitorDeTarefas#excluirRepeticoes(String[], List, Tarefa) excluirRepeticoes
	 */
	public void tratarTarefas(String tarefaBruta, Tarefa tarefa)
	{
		String [] separado = tarefaBruta.split(" ");
		tarefa.setAndarDeInicio(Integer.parseInt(separado[0]));
		tarefa.setSentido(Integer.parseInt(separado[1]));
		tarefa.setNumeroRequisicoes(Integer.parseInt(separado[2]));
		
		List<Integer> requisicoes = new ArrayList<Integer>();
		
		ordenarRequisicoes(separado, tarefa.getSentido());
		excluirRepeticoes(separado, requisicoes, tarefa);
		
		tarefa.setRequisicoes(requisicoes);
		
	}

	/**
	 * M�todo respons�vel por ordenar as requisi��es de uma tarefa de acordo com o seu sentido 
	 * (crescente caso o sentido seja subindo, decrescente caso o sentido seja descendo).
	 * Percorre o vetor de requisi��es comparando uma posi��o com a sua posterior e testando a condi��o dada 
	 * pelo m�todo trocarPosicao.
	 *  
	 * @param separado vetor de string em que cada posi��o corresponde a uma requisi��o
	 * @param sentido
	 * 
	 * @see MonitorDeTarefas#trocarPosicao(int, int, int, String[]) trocarPosicao
	 */
	public void ordenarRequisicoes(String[] separado, int sentido)
	{
		String aux = "";
		//percorre o vetor (na parte das requisicoes) comparando a posi��o com a sua posterior
		for(int i =3; i<(separado.length-1); i++){
			for(int j = i+1; j<separado.length;j++){
				//testa as condi��es para trocar os elementos de lugar
				if(trocarPosicao(i, j, sentido, separado) ){
					aux = separado[i];
					separado[i] = separado[j];
					separado[j] = aux;
				}
			}
		}
	}
	
	/**
	 * M�todo que, caso o sentido seja de subida, verifica se o valor inteiro da requisi��o avaliada � maior que o 
	 * da sua posterior, e ent�o retorna true. Caso o sentido seja de descida e o valor inteiro da posi��o avaliada
	 * seja menor que o da sua posterior, ent�o retorna true. Caso contr�rio retorna false.
	 * 
	 * @param i posi��o sendo avaliada 
	 * @param j posi��o posterior a avaliada
	 * @param sentido sentido da tarefa (sobe ou desce)
	 * @param separado vetor de string contendo em cada posi��o uma requisi��o
	 * 
	 * @return boolean que diz se � ou n�o necess�rio trocar as posi��es (avaliada e sua posterior)
	 */
	public boolean trocarPosicao(int i,int j, int sentido, String[] separado)
	{
		Integer sepi = Integer.decode(separado[i]);
		Integer sepj = Integer.decode(separado[j]);
		if (sentido == SOBE){
			if( sepi > sepj )	
				return true;
			else
				return false;
		}
		else{
			if(sepi < sepj)
				return true;
			else
				return false;
		}
	}

	/**
	 * M�todo respons�vel por inserir os valores inteiros das requisi��es numa lista de inteiros, n�o permitindo repeti��es de valores, e
	 * tamb�m por setar o valor da requisi��o bruta da tarefa antes de convert�-la para inteiro. 
	 * Antes de adicionar um inteiro na lista de requisi��es � verificado se j� existe um igual. Caso exista o mesmo n�o � adicionado.
	 * 
	 * @param separado vetor de string que possui, em cada posi��o, uma requisi��o
	 * @param requisicoes lista de inteiros a ser preenchida com os valores inteiros das strings de requisi��es
	 * @param tarefa tarefa que est� sendo criada
	 */
	public void excluirRepeticoes(String[] separado, List<Integer> requisicoes, Tarefa tarefa)
	{
			tarefa.setRequisicaoBruta(new ArrayList<Integer>());
			for(int i =3; i< separado.length; i++)
			{
				Integer elemento = Integer.decode(separado[i]);
				tarefa.getRequisicaoBruta().add(elemento); 
				if(!requisicoes.contains(elemento))
				{
					requisicoes.add(elemento);
				}
		}	
	}

	/**
	 * M�todo respons�vel por escolher um elevador a partir de uma tarefa. Com o andar de in�cio da tarefa percorremos a lista de elevadores esperando para comparar com o 
	 * andar em que o elevador se encontra. Caso o andar de in�cio da tarefa seja igual ao do elevador, esse ser� o eleito. Caso contr�rio, ser� escolhido o melhor elevador
	 * a partir da diferen�a entre o andar de in�cio da tarefa e o andar atual do elevador. O mais pr�ximo ser� escolhido. 
	 * 
	 * @param elevadoresEsperando lista de elevadores bloqueados devido ao fato de nenhuma tarefa ter sido criada ainda 
	 * 
	 * @return inteiro com o id do elevador escolhido
	 */
	public int tarefaEscolheElevador(List<Elevador> elevadoresEsperando)
	{
		int idElevadorEscolhido = 0;
		int distancia = 0;
		int aux;
		
		for(Elevador e: elevadoresEsperando){
			if(listaDeTarefas.get(0).getAndarDeInicio() == e.id){
				idElevadorEscolhido = e.id;
				break;
			}
			else{
				aux= distancia;
				distancia = Math.abs(e.andarAtual - listaDeTarefas.get(0).getAndarDeInicio());
				if(aux < distancia) distancia = aux;
				else idElevadorEscolhido =  (int) e.getId();
			}
		}
		
		return idElevadorEscolhido;
	}
	
	/**
	 * M�todo respons�vel por escolher a melhor tarefa para um elevador. A partir do andar em que o elevador se encontra, o comparamos com o andar de in�cio da tarefa.
	 * Caso sejam iguais, essa tarefa ser� escolhida. Caso contr�rio, escolheremos a tarefa cuja diferen�a entre o andar de in�cio da tarefa e o andar atual do elevador seja menor.
	 * Ou seja, a tarefa cujo andar de in�cio esteja mais perto do elevador.
	 * 
	 * @param elevador 
	 * 
	 * @return tarefa que esteja mais pr�xima do elevador
	 * 
	 * @throws IOException 
	 * 
	 * @see MonitorDeTarefas#escreverNoArquivo(String, int) escreverNoArquivo
	 */
	public Tarefa elevadorEscolheTarefa(Elevador elevador) throws IOException
	{
		Tarefa candidataTarefa = new Tarefa(40);
		int andarTarefa;
		int menorDistancia = 0;
		int distanciaAtual;
		
		for(int tarefa = 0; tarefa < listaDeTarefas.size(); tarefa++)
		{
			andarTarefa = listaDeTarefas.get(tarefa).getAndarDeInicio();
			
			if(andarTarefa == elevador.andarAtual)
			{
				candidataTarefa = listaDeTarefas.get(tarefa);
				listaDeTarefas.remove(candidataTarefa);
									
				System.out.println("Elevador "+elevador.id+" escolheu a tarefa "+candidataTarefa.getIdTarefa());
				escreverNoArquivo("Elevador "+elevador.id+" escolheu a tarefa "+candidataTarefa.getIdTarefa(), elevador.id);
				
				return candidataTarefa;
			}
			
			else
			{
				distanciaAtual = Math.abs(elevador.andarAtual - listaDeTarefas.get(tarefa).getAndarDeInicio());
				if(tarefa == 0)
				{
					candidataTarefa = listaDeTarefas.get(0);
					menorDistancia = distanciaAtual;
				}
				else
				{
					if(menorDistancia > distanciaAtual)
					{							
						candidataTarefa = listaDeTarefas.get(tarefa);
						menorDistancia = distanciaAtual;
					}
				}
			}		
		}
		
		System.out.println("Elevador "+elevador.id+" escolheu a tarefa "+candidataTarefa.getIdTarefa());
		escreverNoArquivo("Elevador "+elevador.id+" escolheu a tarefa "+candidataTarefa.getIdTarefa(), elevador.id);
		
		listaDeTarefas.remove(candidataTarefa);
		return candidataTarefa;

	}
	
	/**
	 * M�todo respons�vel por monitor a escolha de tarefa. Caso a lista de tarefa esteja vazia e a cria��o das tarefas j� tenha
	 * acabado, � retornado null para que a thread termine sua execu��o. Caso a lista esteja vazia, mas a cria��o de tarefas esteja
	 * em andamento, a thread � bloqueada. Caso haja menos tarefas do que elevador � chamado o m�todo tarefaEscolheElevador. Caso contr�rio
	 * � chamado o elevadorEscolheTarefa.
	 * 
	 * @param elevador
	 * 
	 * @return Null, caso haja erro ou as tarefas j� tenham acabado. Tarefa com a tarefa mais adequada � situa��o.
	 * 
	 * @throws IOException 
	 * 
	 * @see MonitorDeTarefas#tarefaEscolheElevador(List) tarefaEscolheElevador
	 * @see MonitorDeTarefas#elevadorEscolheTarefa(Elevador) elevadorEscolheTarefa
	 * @see MonitorDeTarefas#escreverNoArquivo(String, int) escreverNoArquivo
	 */
	public synchronized Tarefa escolherTarefa(Elevador elevador) throws IOException 
	{		
		Tarefa candidataTarefa = null;
		List <Elevador> elevadoresEsperando = new ArrayList<Elevador>();
		int idElevadorEscolhido;
		
		try
		{
			if(listaDeTarefas.isEmpty() && this.criandoTarefas == false)
			{
				return null;
			}
			while(listaDeTarefas.isEmpty() && this.criandoTarefas == true)
			{
				elevadoresEsperando.add(elevador);
				wait();
			}
			
			while((listaDeTarefas.size() < elevadoresEsperando.size()) && this.criandoTarefas)
			{
				idElevadorEscolhido = tarefaEscolheElevador(elevadoresEsperando);
				if(elevador.getId() != idElevadorEscolhido){
					wait();
				}
				else{
					candidataTarefa = listaDeTarefas.get(0);
					elevadoresEsperando.remove(elevador);
					return candidataTarefa;
				}
			}
			
			candidataTarefa = elevadorEscolheTarefa(elevador);
			
			return candidataTarefa;
			
		} catch(Exception e){
			System.out.println("Deu erro");
			escreverNoArquivo("Deu erro", elevador.id);
		}
		return null;
	}
	
	/**
	 * M�todo respons�vel por notificar as threads que estejam bloqueadas que o elevador em quest�o finalizou sua tarefa.
	 * 
	 * @param id id do elevador que executou tal tarefa
	 * @param idTarefa id da tarefa que acabou de ser executada
	 * 
	 * @throws IOException
	 * 
	 * @see MonitorDeTarefas#escreverNoArquivo(String, int) escreverNoArquivo 
	 */
	public synchronized void finalizarTarefa(int id, int idTarefa) throws IOException
	{
		System.out.println("Elevador "+id+" finalizou a tarefa "+idTarefa);
		escreverNoArquivo("Elevador "+id+" finalizou a tarefa "+idTarefa, id);
		this.notify();
	}
	
	/**
	 * M�todo respons�vel por escrever no arquivo de log
	 * 
	 * @param string string de log a ser escrita no arquivo
	 * @param idElevador id do elevador que est� gerando o log
	 * 
	 * @throws IOException
	 */
	public synchronized void escreverNoArquivo(String string, int idElevador) throws IOException 
	{
		
		File diretorioDeSaidas = new File("log");
		File [] saidas = diretorioDeSaidas.listFiles();
		
		for(File file : saidas)
		{
			if(file.getName().equals("logExecucaoElevador "+idElevador+".txt"))
			{			
				FileWriter fileWriter = new FileWriter(file, true);
				BufferedWriter buffWriter = new BufferedWriter(fileWriter);
				
				buffWriter.write(string+"\n");
				
				buffWriter.close();
				
				fileWriter.close();
			}
		}
	}
}
