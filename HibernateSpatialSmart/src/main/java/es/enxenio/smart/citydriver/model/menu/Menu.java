package es.enxenio.smart.citydriver.model.menu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.LineString;

import es.enxenio.smart.citydriver.model.entradaMenu.EntradaMenu;
import es.enxenio.smart.citydriver.model.usuario.Usuario;
import es.enxenio.smart.citydriver.model.util.jackson.CustomGeometrySerializer;
import es.enxenio.smart.citydriver.model.util.jackson.CustomMultiLineStringDeserializer;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "menu_id_seq")
@Table(name = "menu")
@SuppressWarnings("serial")
public class Menu implements Serializable{

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
        private Long id;
		
		private String nombre;		
	
		@LazyCollection(LazyCollectionOption.FALSE)
		@OneToMany( mappedBy = "menu", cascade={CascadeType.REMOVE, CascadeType.PERSIST})
		@OrderBy("orden")
		private List<EntradaMenu> entradasMenu = new ArrayList<EntradaMenu>();
		
        @Version
    	@SuppressWarnings("unused")
    	private long version;
        
        public Menu() {
        }
       
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public List<EntradaMenu> getEntradasMenu() {
			return entradasMenu;
		}

		public void setEntradasMenu(List<EntradaMenu> entradasMenu) {
			this.entradasMenu = entradasMenu;
		}

		public long getVersion() {
			return version;
		}

		public void setVersion(long version) {
			this.version = version;
		}
	}