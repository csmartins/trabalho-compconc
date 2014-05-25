import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	
	static int	 	 numAndares;
	static int 	 	 numElevadores;
	static int   	 numMaxUsuarios;
	static int[] 	 andaresDeInicio; //salvos na ordem dos elevadores
	static int   	 numTarefas;
	static String[]  tarefas;
	
	static MonitorDeTarefas central;

	/**
	 * Método responsável por ler e armazenar cada linha do arquivo de entrada em um vetor de strings, ou em variáveis
	 * de tipos relacionados a seus campos. Inteiro para número de andares, número de elevador e número máximo de usuários.
	 * 
	 * @param caminhoArq
	 */
	public static void lerArquivo(String caminhoArq){
		
		String[] separados = null;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(caminhoArq));
			String str;
			
			while (in.ready()) {
				//leitura da primeira linha do arquivo
				str = in.readLine();
				separados = str.split(" ");
				numAndares = Integer.parseInt(separados[0]);
				numElevadores = Integer.parseInt(separados[1]);
				numMaxUsuarios = Integer.parseInt(separados[2]);
				
				//leitura da segunda linha do arquivo
				str = in.readLine();
				separados = str.split(" ");
				andaresDeInicio = new int[separados.length];
				for(int i=0; i<separados.length; i++){
					andaresDeInicio[i] = Integer.parseInt(separados[i]);
				}
				
				//leitura da terceira linha do arquivo
				str = in.readLine();
				separados = str.split(" ");
				numTarefas = Integer.parseInt(separados[0]);
				
				//leitura das demais linhas do arquivo, salvando as tarefas
				tarefas = new String[numTarefas];
				for(int j =0; j<numTarefas; j++){
					str = in.readLine();
					tarefas[j] = str;
				}
			}
			
			in.close();
			} catch (IOException e) {
				System.out.println("Erro na leitura do arquivo");
			}

	}
	
	/**
	 * 
	 * Método responsável por chamar a leitura do arquivo, criar as threads, iniciá-las, iniciar a criação de tarefas e,
	 * por fim, esperar pelo fim de todas as threads para finalizar o programa.
	 * 
	 * @see Main#lerArquivo(String) lerArquivo
	 * @see Main#criarArquivosDeSaida(int) criarArquivosDeSaida
	 * @see MonitorDeTarefas#iniciarMonitor(String[]) iniciarMonitor
	 * @see MonitorDeTarefas#escreverNoArquivo(String, int) escreverNoArquivo
	 * 
	 * @param args
	 * 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
		
		/*lê arquivos e salva nas variáveis as infos necessárias*/
		lerArquivo("entrada.txt");
		
		criarArquivosDeSaida(numElevadores);
		
		central = new MonitorDeTarefas();

		/*criação das threads*/
		Thread[] threads = new Thread[numElevadores];
		
	      for (int i=0; i<threads.length; i++) 
	      {
	         central.escreverNoArquivo("--Cria a thread " + i, i);
	         System.out.println("--Cria a thread " + i);
	         threads[i] = new Elevador(i, andaresDeInicio[i], central);
	      }
	      
	      for (int i=0; i<threads.length; i++) 
	      {
	          threads[i].start();
	      }
	      
	      central.iniciarMonitor(tarefas);
	      
	      for (int i=0; i<threads.length; i++) 
	      {
	         try { threads[i].join(); } catch (InterruptedException e) { return; }
	      }
	      System.out.println("A main terminou");
	}

	/**
	 * Método responsável por criar os arquivos de log para cada elevador. O método também cria uma pasta para armazenar
	 * esses arquivos de log. Se a pasta log já existir por conta de uma execução anterior, o método apaga-a e cria uma nova.
	 * 
	 * @param numElevadores número de elevadores executando
	 * 
	 * @throws IOException
	 */
	public static void criarArquivosDeSaida(int numElevadores) throws IOException  
	{
		File saida = new File("log");
		
		if(saida.exists())
		{
			for(File logs: saida.listFiles())
				logs.delete();
			
			saida.delete();
		}
		
		saida.mkdir();
		
		for(int i = 0; i < numElevadores; i++)
		{
			File saidaElevador = new File("log/logExecucaoElevador "+i+".txt");
			saidaElevador.createNewFile();			
		}
		
	}

}
