import java.util.List;

public class Tarefa 
{
	private String 		  tarefaBruta;

	private Integer 	  andarDeInicio;
	private Integer 	  sentido;
	private Integer 	  numeroRequisicoes;

	private final int 	  DESCE = 0;
	private final int 	  SOBE = 1;

	private List<Integer> requisicoes;

	private int 		  idTarefa;

	public Tarefa(int id) {
		this.idTarefa = id;
	}

	public String getTarefaBruta() {
		return tarefaBruta;
	}

	public void setTarefaBruta(String tarefaBruta) {
		this.tarefaBruta = tarefaBruta;
	}

	public Integer getAndarDeInicio() {
		return andarDeInicio;
	}

	public void setAndarDeInicio(Integer andarDeInicio) {
		this.andarDeInicio = andarDeInicio;
	}

	public Integer getSentido() {
		return sentido;
	}

	public void setSentido(Integer sentido) {
		this.sentido = sentido;
	}

	public Integer getNumeroRequisicoes() {
		return numeroRequisicoes;
	}

	public void setNumeroRequisicoes(Integer numeroRequisicoes) {
		this.numeroRequisicoes = numeroRequisicoes;
	}

	public List<Integer> getRequisicoes() {
		return requisicoes;
	}

	public void setRequisicoes(List<Integer> requisicoes) {
		this.requisicoes = requisicoes;
	}

	public int getIdTarefa() {
		return idTarefa;
	}

	public void setIdTarefa(int idTarefa) {
		this.idTarefa = idTarefa;
	}
	
	
}
