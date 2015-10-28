CKEDITOR.dialog.add( 'publicacionDialog', function( editor ) {
    return {
        title: editor.lang.publicacion.titulo,
        minWidth: 200,
        minHeight: 100,
        contents: [
            {
                id: 'tab-publicacion',
                label: editor.lang.publicacion.titulo,
                elements: [
                    {
                        type: 'text',
                        id: 'engadirElemento',
                        className: 'engadirElemento engadirPublicacion',
                        label: editor.lang.publicacion.nome,
                        validate: function () {
                        	var id = $("#idPublicacion").val();
                        	var nome = this.getValue();
                        	if(id == null || id == undefined || id == ''){
                        		$(".error").remove();
                        		if(nome == null || nome == undefined || nome == '')
                        			$(".engadirPublicacion input").first().after("<span class='error'>"+editor.lang.publicacion.erro_obrigatorio+"</span>");
                        		else
                        			$(".engadirPublicacion input").first().after("<span class='error'>"+editor.lang.publicacion.erro_non_existe+"</span>");
                        		return false;
                        	}
                        	
                        	$(".error").remove();
                        	return true;
                        },
                        onLoad : function( element ) {
                        	
                        	$(".engadirPublicacion input").first()
                        		.after("<img class='eliminar' style='display:none;' src='"+CKEDITOR.plugins.getPath("enciclopedia/publicacion/images")+"eliminar.png'/>");
                        	$(".engadirPublicacion input").first().after("<input type='hidden' id='nomePublicacion'/>");
                        	$(".engadirPublicacion input").first().after("<input type='hidden' id='idPublicacion'/>");
                        	
                        	$(".engadirPublicacion .eliminar").click(function(){
    							$(".engadirPublicacion input").first().val("");
    							$("#nomePublicacion").val("");
    							$("#idPublicacion").val("");
    							$(".engadirPublicacion .eliminar").hide();
    						});
                        	
                        	$(".engadirPublicacion input").first().keydown(function(){
                        		$("#nomePublicacion").val("");
    							$("#idPublicacion").val("");
    							$(".engadirPublicacion .eliminar").hide();
                        	});
                        	
                        	$(".engadirPublicacion input").first().autocomplete({
                				source: function( request, response ) {
                					$.getJSON(CKEDITOR.config.baseHref+"usuarioXestor/literatura/publicacion/json/titulosPublicacions", {term: request.term}, function(publicacions) {
                						response(
                								$.map(publicacions, function(publicacion) {
                									return {idPublicacion: publicacion.idPubli, value: publicacion.titulo};
                								}
                						));
                					});
                				},
                				minLength: 2,
                				select: function(event, ui) {
                					$(".engadirPublicacion input").first().val(ui.item.value);
        							$("#nomePublicacion").val(ui.item.value);
        							$("#idPublicacion").val(ui.item.idPublicacion);
        							$(".engadirPublicacion .eliminar").show();
            						event.preventDefault();
                				}
                			});
                        },
                        onHide : function( element ) {
                        	$(".engadirPublicacion input").first().val("");
							$("#nomePublicacion").val("");
							$("#idPublicacion").val("");
							$(".engadirPublicacion .eliminar").hide();
							$(".error").remove();
                        },
                        setup: function( element ) {
                        	
                            var identificador = element.getAttribute('class');
                            var parametros = identificador.split(" ");
                            var id = "";
                            for(p in parametros){
                            	var actual = parametros[p];
                            	if (actual.indexOf("_") != -1){
                            		id = actual.split("_")[1];
                            	}
                            }
                            var nome = element.getAttribute('title');
                            
                            $(".engadirPublicacion input").first().val(nome);
							$("#nomePublicacion").val(nome);
							$("#idPublicacion").val(id);
							$(".engadirPublicacion .eliminar").show();
							$(".error").remove();
                        },
                        commit: function( element ) {
                        	
                        	var id = $("#idPublicacion").val();
                        	
                        	if(id != null && id != undefined && id != ''){
                        		
                        		if(element.getText() == null || element.getText() == undefined || element.getText() == ''){
                        			element.setText($("#nomePublicacion").val());
                        		}
                        		
                        		element.setAttribute( 'title', $("#nomePublicacion").val());
                        		element.setAttribute( 'class', 'entidade publicacion ' + 'publicacion_'+$("#idPublicacion").val());
                        	}
                        	else{
                        		element.remove(true);
                        	}
                        }
                    },
                    {
                        type: 'checkbox',
                        id: 'entidadeConFicha',
                        label: editor.lang.publicacion.ten_ficha,
                        default: false,
                        commit: function( element ) {
                        	
                        	if(this.getValue()){
                        		var clases = element.getAttribute( 'class');
                        		element.setAttribute( 'class', clases + ' conFicha');
                        	}
                        },
                        setup: function( element ) {
                        	
                            var identificador = element.hasClass("conFicha");
                            if(identificador == true){
                            	this.setValue(true);
                            }
                            else{
                            	this.setValue(false);
                            }
                        }
                    }
                ]
            },
        ],
        onShow: function() {
            var selection = editor.getSelection();
            var element = selection.getStartElement();

            if ( element )
                element = element.getAscendant( 'span', true );

            if ( !element || element.getName() != 'span' || !element.hasClass('publicacion')) {
                element = editor.document.createElement( 'span' );
                element.setText(selection.getSelectedText());
                this.insertMode = true;
            }
            else
                this.insertMode = false;

            this.element = element;
            if ( !this.insertMode )
                this.setupContent( this.element );
            else{
            	$(".engadirPublicacion input").first().val(selection.getSelectedText());
            	$(".engadirPublicacion input").first().trigger( "keydown" );
        	}               
        },
        onOk: function() {
            var dialog = this;
            var span = this.element;
            this.commitContent( span );

            if ( this.insertMode )
                editor.insertElement( span );
        }
    };
});