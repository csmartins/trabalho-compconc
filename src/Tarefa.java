import java.util.List;

/**
 * 
 * Classe responsável por armazenar informações da tarefa.
 * @author Patrícia S. Ghiraldelli e Carlos Eduardo S. Martins
 *
 */
public class Tarefa 
{
	private Integer 	  andarDeInicio;
	private Integer 	  sentido;
	private Integer 	  numeroRequisicoes;

	private final int 	  DESCE = 0;
	private final int 	  SOBE = 1;

	private List<Integer> requisicoes;
	private List<Integer> requisicaoBruta;

	private int 		  idTarefa;

	public Tarefa(int id) {
		this.idTarefa = id;
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

	public List<Integer> getRequisicaoBruta() {
		return requisicaoBruta;
	}

	public void setRequisicaoBruta(List<Integer> requisicaoBruta) {
		this.requisicaoBruta = requisicaoBruta;
	}	
}
