import java.util.ArrayList;
import java.util.List;


public class MonitorDeTarefas 
{
	String [] 		  tarefasBrutas;
	
	ArrayList<Tarefa> listaDeTarefas; 
	
	private final int DESCE = 0;
	private final int SOBE = 1;
	
	private int count;
	
	public MonitorDeTarefas(String [] tarefas)
	{
		this.listaDeTarefas = new ArrayList<Tarefa>();
		this.tarefasBrutas = tarefas;
		this.criarTarefas();
		
		this.count= 0;
	}
	
	
	private void criarTarefas() 
	{
		for(int i = 0; i < tarefasBrutas.length; i++)
		{
			Tarefa tarefa = new Tarefa(i);
			tratarTarefas(tarefasBrutas[i], tarefa);
			listaDeTarefas.add(tarefa);
		}
		
	}

	private void tratarTarefas(String tarefaBruta, Tarefa tarefa)
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

	public void excluirRepeticoes(String[] separado, List<Integer> requisicoes, Tarefa tarefa)
	{
			tarefa.setRequisicaoBruta(new ArrayList<Integer>());
			for(int i =3; i< separado.length; i++){
				Integer elemento = Integer.decode(separado[i]);
				tarefa.getRequisicaoBruta().add(elemento); //seta o vetor da string bruta antes de remover repeticao
				if(!requisicoes.contains(elemento)){
					requisicoes.add(elemento);
				}
		}	
	}


	public synchronized Tarefa escolherTarefa(Integer andarAtual, int idElevador) 
	{		
		Tarefa candidataTarefa = null;
		try{
			
			if(listaDeTarefas.size() == 0){
				return null;
			}
			
			while(count > 0){
				System.out.println("Elevador "+idElevador+" bloqueado");
				wait();
			}

			++count;
			int andarTarefa;
			int menorDistancia = 0;
			int distanciaAtual;
			candidataTarefa = new Tarefa(40);
			
			for(int tarefa = 0; tarefa < listaDeTarefas.size(); tarefa++)
			{
				andarTarefa = listaDeTarefas.get(tarefa).getAndarDeInicio();
				
				if(andarTarefa == andarAtual)
				{
					candidataTarefa = listaDeTarefas.get(tarefa);
					listaDeTarefas.remove(candidataTarefa);
					System.out.println("Elevador "+idElevador+" escolheu a tarefa "+candidataTarefa.getIdTarefa());
					--count;
					return candidataTarefa;
				}
				
				else{
					distanciaAtual = Math.abs(andarAtual - listaDeTarefas.get(tarefa).getAndarDeInicio());
					if(tarefa == 0){
						candidataTarefa = listaDeTarefas.get(0);
						menorDistancia = distanciaAtual;
					}
					else{
						if(menorDistancia > distanciaAtual)
							candidataTarefa = listaDeTarefas.get(tarefa);
							menorDistancia = distanciaAtual;
					}
				}		
			}
			
			System.out.println("Elevador "+idElevador+" escolheu a tarefa "+candidataTarefa.getIdTarefa());
			
			listaDeTarefas.remove(candidataTarefa);
			
			--count;
			
			return candidataTarefa;
			
		} catch(Exception e){
			System.out.println("Deu erro");
		}
		return null;
	}
	
	public synchronized void finalizaTarefa(int id, int idTarefa)
	{
		System.out.println("Elevador "+id+" finalizou a tarefa "+idTarefa);
		notify();
	}
}
