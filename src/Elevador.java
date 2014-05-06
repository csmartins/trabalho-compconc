import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Elevador extends Thread{	
	
	final int     DESCE 			= 0;
	final int     SOBE 			    = 1;
	
	String        tarefaEscolhida;
	
	int 	      id;
	int 	      andarDeInicio;	
	int    	      andarDePartida;
	int  	   	  sentido;
	int 	   	  numRequisicoes;
	//TODO quando andar para um andar este campo deve ser atualizado
	int 		  andarAtual;
	
	List<Integer> requisicoes 		= new ArrayList<Integer>();	
	
	public Elevador(int id, int andarDeInicio) 
	{
		this.id = id;
		this.andarDeInicio = andarDeInicio;
	}
	
	public void run()
	{
		System.out.println("Thread "+id+" começou");
		tarefaEscolhida = pegarTarefa(); 
		trataTarefas();
	}

	public String pegarTarefa()
	{
		
		String        tarefaElevador;
		
		synchronized (this) 
		{
			tarefaElevador = Main.escolherTarefa(andarAtual);
		}
		//código para escolher melhor tarefa
		//pensar em como excluir a tarefa esscolhida do vetor
		return tarefaElevador;
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
				//FIXME extrair as condições desse if para métodos, deixar mais legivel
				if(isElevadorSubindoEAndaresTrocados(i, j) || isElevadorDescendoEAndaresTrocados(i, j) )
				{
					aux = requisicoes.get(i);
					requisicoes.set(i, requisicoes.get(j));
					requisicoes.set(j, aux);
					
				}
			}
		}
	}

	private boolean isElevadorDescendoEAndaresTrocados(int i, int j) 
	{
		return (requisicoes.get(i) < requisicoes.get(j)) && sentido == DESCE;
	}

	private boolean isElevadorSubindoEAndaresTrocados(int i, int j) 
	{
		return (requisicoes.get(i) > requisicoes.get(j)) && sentido == SOBE;
	}
	
}
