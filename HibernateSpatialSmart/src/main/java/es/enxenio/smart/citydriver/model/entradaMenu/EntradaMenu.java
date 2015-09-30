package es.enxenio.smart.citydriver.model.entradaMenu;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.LineString;

import es.enxenio.smart.citydriver.model.menu.Menu;
import es.enxenio.smart.citydriver.model.usuario.Usuario;
import es.enxenio.smart.citydriver.model.util.jackson.CustomGeometrySerializer;
import es.enxenio.smart.citydriver.model.util.jackson.CustomMultiLineStringDeserializer;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "entradaMenu_id_seq")
@Table(name = "entradaMenu")
@SuppressWarnings("serial")
public class EntradaMenu implements Serializable{

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
        private Long id;

		private String texto;
		
		private String url;
		
		private Integer orden;
		
		@LazyCollection(LazyCollectionOption.FALSE)
		@OneToMany( mappedBy = "entradaMenuPadre", cascade={CascadeType.REMOVE, CascadeType.PERSIST})
		@OrderBy("orden")
		private List<EntradaMenu> entradasMenu = new ArrayList<EntradaMenu>();
        
		
		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "idEntradaMenuPadre")
		private EntradaMenu entradaMenuPadre;
		
		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "idMenu")
		private Menu menu;
		
        @Version
    	@SuppressWarnings("unused")
    	private long version;
        
        public EntradaMenu() {
        }
       
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

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

		public List<EntradaMenu> getEntradasMenu() {
			return entradasMenu;
		}

		public void setEntradasMenu(List<EntradaMenu> entradasMenu) {
			this.entradasMenu = entradasMenu;
		}
		
		public EntradaMenu getEntradaMenuPadre() {
			return entradaMenuPadre;
		}

		public void setEntradaMenuPadre(EntradaMenu entradaMenuPadre) {
			this.entradaMenuPadre = entradaMenuPadre;
		}

		public Menu getMenu() {
			return menu;
		}

		public void setMenu(Menu menu) {
			this.menu = menu;
		}

		public long getVersion() {
			return version;
		}

		public void setVersion(long version) {
			this.version = version;
		}
}