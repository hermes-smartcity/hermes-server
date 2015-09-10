
function reordenarApartado(urlApartado, idApartado, idCapitulo, idFilaPadre){
		$.ajax({
			type: 'GET',
			dataType : 'html',
			url : urlApartado, //supongo que subir o bajar apartado
			data : {'capitulo': idCapitulo, 'apartado': idApartado},		
			success : function(data) { 
				// [Leti 20/10/2014] Cambiado, xa que por defecto amosamos todo expandido. Se por defecto se amosa contraído
				// recuperar a versión anterior do cvs				
				var apartadosAContraer = [];
				$( ".elementoApartadoCapitulo_"+idCapitulo+ " a.expandirApartado").each(function(){
					if ($(this).is(":visible")){
						var idContraido = $(this).attr("id");
						var idApartado = idContraido.substring(17, idContraido.length);
						apartadosAContraer.push(idApartado);
					}
				});
				
				$( ".elementoApartadoCapitulo_"+idCapitulo).remove();
				$( ".elementoSubapartadoCapitulo_"+idCapitulo).remove();
		
				$(data).insertAfter($("#"+idFilaPadre));
				//contraerCapitulo(idCapitulo);						
				for (i=0; i<apartadosAContraer.length; i++){
					contraerApartado(apartadosAContraer[i]);
				}
				
			},
			error : function() { 
			}
		});

};

function eliminarApartado(mensaxeEliminar, urlApartado, idApartado, idCapitulo, idFilaPadre){
	if (confirm(mensaxeEliminar)) {
		$.ajax({
			type: 'GET',
			dataType : 'html',
			url : urlApartado, //supongo que subir o bajar apartado
			data : { 'apartado': idApartado},		
			success : function(data) { 
				var apartadosAExpandir = [];
				$( ".elementoApartadoCapitulo_"+idCapitulo+ " a.contraerApartado").each(function(){
					if ($(this).is(":visible")){
						var idExpandido = $(this).attr("id");
						var idApartado = idExpandido.substring(17, idExpandido.length);
						apartadosAExpandir.push(idApartado);
					}
				});
				
				$("#elementoApartado_" + idApartado).remove();
				$(".elementoApartadoSubapartado_" + idApartado).remove();
				$(".elementoSubapartadoApartado_" + idApartado).remove();
				if($("#elementoApartadoCapitulo_" + idCapitulo).length==0){
					$("#expandirCapitulo_" + idCapitulo).hide();
					$("#contraerCapitulo_" + idCapitulo).hide();
				}	
				
			},
			error : function() { 
			}
		});
	}
};



