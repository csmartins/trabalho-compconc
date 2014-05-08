import java.util.ArrayList;
import java.util.List;


public class MonitorDeTarefas 
{
	String [] 		  tarefasBrutas;
	
	ArrayList<Tarefa> listaDeTarefas; 
	
	private final int DESCE = 0;
	private final int SOBE = 1;

	
	public MonitorDeTarefas(String [] tarefas)
	{
		this.listaDeTarefas = new ArrayList<Tarefa>();
		this.tarefasBrutas = tarefas;
		this.criarTarefas();
		System.out.println("Quantidade de tarefas: "+listaDeTarefas.size());
		for(int i =0 ; i < listaDeTarefas.size(); i++){
			System.out.println("Tarefa "+listaDeTarefas.get(i).getIdTarefa());
			System.out.println("Sentido da tarefa: "+listaDeTarefas.get(i).getSentido());
			List<Integer> reqtemp = listaDeTarefas.get(i).getRequisicoes();
			for(int j =0; j<reqtemp.size(); j++){
				System.out.println(" "+reqtemp.get(j)+" ");
			}
		}
	}

	private void tratarTarefas(String tarefaBruta, Tarefa tarefa)
	{
		String [] separado = tarefaBruta.split(" ");
		tarefa.setTarefaBruta(tarefaBruta);
		tarefa.setAndarDeInicio(Integer.parseInt(separado[0]));
		tarefa.setSentido(Integer.parseInt(separado[1]));
		tarefa.setNumeroRequisicoes(Integer.parseInt(separado[2]));
		
		List<Integer> requisicoes = new ArrayList<Integer>();
		excluirRepeticoes(separado, requisicoes);
		ordenarRequisicoes(requisicoes, tarefa.getSentido());
		tarefa.setRequisicoes(requisicoes);
		
	}
	private void criarTarefas() 
	{
		for(int i = 0; i < tarefasBrutas.length; i++)
		{
			Tarefa tarefa = new Tarefa(i);
			tratarTarefas(tarefasBrutas[i], tarefa);
			//comitando qq coisa
			listaDeTarefas.add(tarefa);
		}
		
	}
	
	public void excluirRepeticoes(String[] separado, List<Integer> requisicoes)
	{
			for(int i =3; i< separado.length; i++){
			if(!requisicoes.contains(Integer.decode(separado[i]))){
				requisicoes.add(Integer.decode(separado[i]));
			}
		}	
	}
	
	public void ordenarRequisicoes(List<Integer> requisicoes, int sentido)
	{
		Integer aux = 0;
		//percorre o vetor comparando a posição com a sua posterior
		for(int i =0; i<(requisicoes.size()-1); i++){
			for(int j = i+1; j<requisicoes.size();j++){
				//testa as condições para trocar os elementos de lugar
				if(trocarPosicao(i, j, sentido, requisicoes) ){
					aux = requisicoes.get(i);
					requisicoes.set(i, requisicoes.get(j));
					requisicoes.set(j, aux);
				}
			}
		}
	}
	
	public boolean trocarPosicao(int i,int j, int sentido, List<Integer> requisicoes)
	{
		if (sentido == SOBE){
			if(requisicoes.get(i) >requisicoes.get(j))	
				return true;
			else
				return false;
		}
		else{
			if(requisicoes.get(i) <requisicoes.get(j))
				return true;
			else
				return false;
		}
	}

	public static synchronized Tarefa escolherTarefa(Integer andarAtual) {
		// lógica para escolher melhor tarefa
		return null;
	}
	
	public synchronized void finalizaTarefa(Tarefa f){
		listaDeTarefas.remove(f.getIdTarefa());
	}
}
