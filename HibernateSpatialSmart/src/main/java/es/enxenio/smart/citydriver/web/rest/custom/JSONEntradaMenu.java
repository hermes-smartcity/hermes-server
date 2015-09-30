package es.enxenio.smart.citydriver.web.rest.custom;

import java.util.List;

import es.enxenio.smart.citydriver.model.entradaMenu.EntradaMenu;

public class JSONEntradaMenu {
	private String texto;
	private String url;
	private Integer orden;
	//TODO "Chapucilla momentánea" para identar las entradas del menú
	private String identacion;
	private List<JSONEntradaMenu>entradasMenu;
	//TODO falta entradaMenuPadre y Menu padre?¿

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public List<JSONEntradaMenu> getEntradasMenu() {
		return entradasMenu;
	}

	public void setEntradasMenu(List<JSONEntradaMenu> entradasMenu) {
		this.entradasMenu = entradasMenu;
	}

	public String getIdentacion() {
		return identacion;
	}

	public void setIdentacion(String identacion) {
		this.identacion = identacion;
	}

	@Override
	public String toString() {
		return "JSONEntradaMenu [texto=" + texto + ", url="
				+ url+ ", orden=" + orden + ", entradasMenu=" + entradasMenu + "]";
	}

}
