package es.enxenio.smart.citydriver.web.rest.custom;

import java.util.List;

import es.enxenio.smart.citydriver.model.entradaMenu.EntradaMenu;

public class JSONEntradaMenu {
	private Long id;
	private String texto;
	private String url;
	private Integer orden;
	private List<JSONEntradaMenu>entradasMenu;
	private Long version;

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

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "JSONEntradaMenu [texto=" + texto + ", url="
				+ url+ ", orden=" + orden + ", entradasMenu=" + entradasMenu + "]";
	}

}
