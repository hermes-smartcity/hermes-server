
function reordenarSubapartado(urlSubapartado, idSubapartado, idApartado, idFilaPadre){
		$.ajax({
			type: 'GET',
			dataType : 'html',
			url : urlSubapartado, //supongo que subir o bajar apartado
			data : {'apartado': idApartado, 'subapartado': idSubapartado},		
			success : function(data) { 
				
				$( ".elementoSubapartadoApartado_"+idApartado).remove();
				$(data).insertAfter($("#"+idFilaPadre));
				expandirApartado(idApartado);						

			},
			error : function() { 
			}
		});

};



function eliminarSubapartado(mensaxeEliminar,urlSubapartado, idSubapartado, idApartado, idFilaPadre){
	if (confirm(mensaxeEliminar)) {
	$.ajax({
		type: 'GET',
		dataType : 'html',
		url : urlSubapartado, //supongo que subir o bajar apartado
		data : {'apartado': idApartado, 'subapartado': idSubapartado},		
		success : function(data) { 
			$( "#elementoSubapartado_"+idSubapartado).remove();
		},
		error : function() { 
		}
	});
	}
};
