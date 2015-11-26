package es.udc.lbd.hermes.model.util.dao;

import java.util.List;

public class BloqueElementos<E> {

	private List<E> elementos;
	private Long numeroElementos;
	private int elementosPaxina;
	private int paxina;
	private boolean mais;

	public BloqueElementos(List<E> elementos, Long numeroElementos,
			int elementosPaxina, int paxina, boolean mais) {
		this.elementos = elementos;
		this.numeroElementos = numeroElementos;
		this.elementosPaxina = elementosPaxina;
		this.paxina = paxina;
		this.mais = mais;
	}

	public BloqueElementos(List<E> elementos, int elementosPaxina, int paxina,
			boolean mais) {
		this(elementos, null, elementosPaxina, paxina, mais);
	}

	public List<E> getElementos() {
		return elementos;
	}

	public Long getNumeroElementos() {
		return numeroElementos;
	}

	public int getPaxina() {
		return paxina;
	}

	public boolean isMais() {
		return mais;
	}

	public long getTotalPaxinas() {
		long total = numeroElementos / elementosPaxina;
		if (numeroElementos % elementosPaxina != 0) {
			total += 1;
		}
		return total;
	}
	
	public Integer getIndiceInicial(){
		return (paxina - 1) * elementosPaxina;
	}

	/**
	 * ObtÃ©n o nÃºmero de pÃ¡xinas que hai en BD
	 * @return
	 */
	public Integer getPaxinasTotais(){
		return new Double(Math.round((new Double(numeroElementos) / new Double(elementosPaxina)) + 0.4999)).intValue();
	}
}
