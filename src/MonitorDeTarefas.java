import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Essa classe funciona como um monitor, contendo todos os métodos com acessos às variáveis 
 * compartilhadas pelos elevadores (threads).
 * 
 * @author Patrícia S. Ghiraldelli e Carlos Eduardo S. Martisns
 */

public class MonitorDeTarefas 
{
	String [] 		  tarefasBrutas;
	
	ArrayList<Tarefa> listaDeTarefas;
	
	private final int DESCE = 0;
	private final int SOBE = 1;
	
	private boolean criandoTarefas;
	
	/**
	 * Método responsável por iniciar o monitor, instanciando variáveis e recebendo o arquivo de tarefas.
	 * 
	 * @param tarefas é o vetor de tarefas já lidas no arquivo de entrada 
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
	 * Método responsável por instanciar os objetos Tarefa com as informações do arquivo lido.
	 * A cada tarefa criada é dado um notify para acordar possíveis elevadores que estejam aguardando
	 * a criação de tarefas.
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
	 * Método responsável por instanciar o objeto Tarefa e tratá-los, excluindo repetições e ordenando corretamente.
	 * 
	 * @param tarefaBruta É a string referente a uma linha lida no arquivo de entrada, portanto uma posição do 
	 * vetor de tarefas recebido no  método iniciaMonitor
	 * @param tarefa É o objeto criado Tarefa que receberá as informações da tarefaBruta
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
	 * Método responsável por ordenar as requisições de uma tarefa de acordo com o seu sentido 
	 * (crescente caso o sentido seja subindo, decrescente caso o sentido seja descendo).
	 * Percorre o vetor de requisições comparando uma posição com a sua posterior e testando a condição dada 
	 * pelo método trocarPosicao.
	 *  
	 * @param separado vetor de string em que cada posição corresponde a uma requisição
	 * @param sentido
	 * 
	 * @see MonitorDeTarefas#trocarPosicao(int, int, int, String[]) trocarPosicao
	 */
	public void ordenarRequisicoes(String[] separado, int sentido)
	{
		String aux = "";
		//percorre o vetor (na parte das requisicoes) comparando a posição com a sua posterior
		for(int i =3; i<(separado.length-1); i++){
			for(int j = i+1; j<separado.length;j++){
				//testa as condições para trocar os elementos de lugar
				if(trocarPosicao(i, j, sentido, separado) ){
					aux = separado[i];
					separado[i] = separado[j];
					separado[j] = aux;
				}
			}
		}
	}
	
	/**
	 * Método que, caso o sentido seja de subida, verifica se o valor inteiro da requisição avaliada é maior que o 
	 * da sua posterior, e então retorna true. Caso o sentido seja de descida e o valor inteiro da posição avaliada
	 * seja menor que o da sua posterior, então retorna true. Caso contrário retorna false.
	 * 
	 * @param i posição sendo avaliada 
	 * @param j posição posterior a avaliada
	 * @param sentido sentido da tarefa (sobe ou desce)
	 * @param separado vetor de string contendo em cada posição uma requisição
	 * 
	 * @return boolean que diz se é ou não necessário trocar as posições (avaliada e sua posterior)
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
	 * Método responsável por inserir os valores inteiros das requisições numa lista de inteiros, não permitindo repetições de valores, e
	 * também por setar o valor da requisição bruta da tarefa antes de convertê-la para inteiro. 
	 * Antes de adicionar um inteiro na lista de requisições é verificado se já existe um igual. Caso exista o mesmo não é adicionado.
	 * 
	 * @param separado vetor de string que possui, em cada posição, uma requisição
	 * @param requisicoes lista de inteiros a ser preenchida com os valores inteiros das strings de requisições
	 * @param tarefa tarefa que está sendo criada
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
	 * Método responsável por escolher um elevador a partir de uma tarefa. Com o andar de início da tarefa percorremos a lista de elevadores esperando para comparar com o 
	 * andar em que o elevador se encontra. Caso o andar de início da tarefa seja igual ao do elevador, esse será o eleito. Caso contrário, será escolhido o melhor elevador
	 * a partir da diferença entre o andar de início da tarefa e o andar atual do elevador. O mais próximo será escolhido. 
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
	 * Método responsável por escolher a melhor tarefa para um elevador. A partir do andar em que o elevador se encontra, o comparamos com o andar de início da tarefa.
	 * Caso sejam iguais, essa tarefa será escolhida. Caso contrário, escolheremos a tarefa cuja diferença entre o andar de início da tarefa e o andar atual do elevador seja menor.
	 * Ou seja, a tarefa cujo andar de início esteja mais perto do elevador.
	 * 
	 * @param elevador 
	 * 
	 * @return tarefa que esteja mais próxima do elevador
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
	 * Método responsável por monitor a escolha de tarefa. Caso a lista de tarefa esteja vazia e a criação das tarefas já tenha
	 * acabado, é retornado null para que a thread termine sua execução. Caso a lista esteja vazia, mas a criação de tarefas esteja
	 * em andamento, a thread é bloqueada. Caso haja menos tarefas do que elevador é chamado o método tarefaEscolheElevador. Caso contrário
	 * é chamado o elevadorEscolheTarefa.
	 * 
	 * @param elevador
	 * 
	 * @return Null, caso haja erro ou as tarefas já tenham acabado. Tarefa com a tarefa mais adequada à situação.
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
	 * Método responsável por notificar as threads que estejam bloqueadas que o elevador em questão finalizou sua tarefa.
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
	 * Método responsável por escrever no arquivo de log
	 * 
	 * @param string string de log a ser escrita no arquivo
	 * @param idElevador id do elevador que está gerando o log
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
