import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Elevador extends Thread{	
	
	final int     DESCE 			= 0;
	final int     SOBE 			    = 1;
	int 	      id;
	int 	      andarDeInicio;
	String []     tarefasElevador;
	String        tarefaEscolhida;
	int    	      andarDePartida;
	int  	   	  sentido;
	int 	   	  numRequisicoes;
	List<Integer> requisicoes 		= new ArrayList<Integer>();	
	
	public Elevador(int id, int andarDeInicio) 
	{
		this.id = id;
		this.andarDeInicio = andarDeInicio;
	}
	
	public void run()
	{
		System.out.println("Thread "+id+" começou");
		tarefaEscolhida = escolheTarefas(); 
		trataTarefas();
	}

	public String escolheTarefas()
	{
		synchronized (this) 
		{
			tarefasElevador = Main.tarefas;
		}
		//código para escolher melhor tarefa
		//pensar em como excluir a tarefa esscolhida do vetor
		return tarefasElevador[0];
	}

	public void trataTarefas()
	{
		String [] separado = tarefaEscolhida.split(" ");
		andarDePartida= Integer.parseInt(separado[0]);
		sentido = Integer.parseInt(separado[1]);
		numRequisicoes = Integer.parseInt(separado[2]);
		
		excluiRepeticoes(separado);
		ordenaRequisicoes();
	}
	
	public void excluiRepeticoes(String[] separado)
	{
		Set <Integer> auxiliar = new HashSet<Integer>(); //o set não permite repetições
		for(int i=3; i<separado.length; i++)
		{
			auxiliar.add(Integer.decode(separado[i]));
		}
		// copiamos o set para uma lista, pois é melhor para manusear
		requisicoes.addAll(auxiliar);
			
	}
	
	public void ordenaRequisicoes()
	{
		Integer aux = 0;
		//percorre o vetor comparando a posição com a sua posterior
		for(int i =0; i<(requisicoes.size()-1); i++)
		{
			for(int j = i+1; j<requisicoes.size();j++)
			{
				//testa as condições para trocar os elementos de lugar
				if(((requisicoes.get(i) >requisicoes.get(j)) && sentido == SOBE) || ((requisicoes.get(i) <requisicoes.get(j)) && sentido == DESCE ) ){
					aux = requisicoes.get(i);
					requisicoes.set(i, requisicoes.get(j));
					requisicoes.set(j, aux);
					
				}
			}
		}
	}
	
}
