package es.enxenio.smart.model.util;

public class Parella<T, R> {

	private T primeiro;
	private R segundo;

	public Parella() {
		super();
	}
	
	public Parella(T primeiro, R segundo) {
		super();
		this.primeiro = primeiro;
		this.segundo = segundo;
	}

	public T getPrimeiro() {
		return primeiro;
	}

	public void setPrimeiro(T primeiro) {
		this.primeiro = primeiro;
	}

	public R getSegundo() {
		return segundo;
	}

	public void setSegundo(R segundo) {
		this.segundo = segundo;
	}
	
}

