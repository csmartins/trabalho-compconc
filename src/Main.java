import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Main {
	
	static int 		numAndares;
	static int 		numElevadores;
	static int 		numMaxUsuarios;
	static int[] 	andaresDeInicio; //salvos na ordem dos elevadores
	static int 		numTarefas;
	static String[] tarefas;

	public static void leArquivo(String caminhoArq)
	{
		
		String[] separados = null;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(caminhoArq));
			String str;
			
			while (in.ready()) 
			{
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
			} 
		catch (IOException e) 
		{
				System.out.println("Erro na leitura do arquivo");
		}

	}
	
	public static void main(String[] args) 
	{
		
		/*lê arquivos e salva nas variáveis as infos necessárias*/
		leArquivo("C:/Users/Patrícia/Desktop/teste.txt");
		
		/*criação das threads*/
		Thread[] threads = new Thread[numElevadores];
		
	      for (int i=0; i<threads.length; i++) 
	      {
	         System.out.println("--Cria a thread " + i);
	         threads[i] = new Elevador(i, andaresDeInicio[i]);
	      }
	      
	      for (int i=0; i<threads.length; i++) 
	      {
	          threads[i].start();
	      }

	      for (int i=0; i<threads.length; i++) 
	      {
	          try { threads[i].join(); } catch (InterruptedException e) { return; }
	      }
	       System.out.println("A main terminou");
	}

}
