CKEDITOR.dialog.add( 'eventoDialog', function( editor ) {
    return {
        title: editor.lang.evento.titulo,
        minWidth: 200,
        minHeight: 100,
        contents: [
            {
                id: 'tab-evento',
                label: editor.lang.evento.titulo,
                elements: [
                    {
                        type: 'text',
                        id: 'engadirElemento',
                        className: 'engadirElemento engadirEvento',
                        label: editor.lang.evento.nome,
                        validate: function () {
                        	var id = $("#idEvento").val();
                        	var nome = this.getValue();
                        	if(id == null || id == undefined || id == ''){
                        		$(".error").remove();
                        		if(nome == null || nome == undefined || nome == '')
                        			$(".engadirEvento input").first().after("<span class='error'>"+editor.lang.evento.erro_obrigatorio+"</span>");
                        		else
                        			$(".engadirEvento input").first().after("<span class='error'>"+editor.lang.evento.erro_non_existe+"</span>");
                        		return false;
                        	}
                        	
                        	$(".error").remove();
                        	return true;
                        },
                        onLoad : function( element ) {
                        	
                        	$(".engadirEvento input").first()
                        		.after("<img class='eliminar' style='display:none;' src='"+CKEDITOR.plugins.getPath("enciclopedia/evento/images")+"eliminar.png'/>");
                        	$(".engadirEvento input").first().after("<input type='hidden' id='nomeEvento'/>");
                        	$(".engadirEvento input").first().after("<input type='hidden' id='idEvento'/>");
                        	
                        	$(".engadirEvento .eliminar").click(function(){
    							$(".engadirEvento input").first().val("");
    							$("#nomeEvento").val("");
    							$("#idEvento").val("");
    							$(".engadirEvento .eliminar").hide();
    						});
                        	
                        	$(".engadirEvento input").first().keydown(function(){
                        		$("#nomeEvento").val("");
    							$("#idEvento").val("");
    							$(".engadirEvento .eliminar").hide();
                        	});
                        	
                        	$(".engadirEvento input").first().autocomplete({
                				source: function( request, response ) {
                					$.getJSON(CKEDITOR.config.baseHref+"usuarioXestor/literatura/evento/json/nomesEventos", {term: request.term}, function(eventos) {
                						response(
                								$.map(eventos, function(evento) {
                									return {idEvento: evento.idEvento, value: evento.nome};
                								}
                						));
                					});
                				},
                				minLength: 2,
                				select: function(event, ui) {
                					$(".engadirEvento input").first().val(ui.item.value);
        							$("#nomeEvento").val(ui.item.value);
        							$("#idEvento").val(ui.item.idEvento);
        							$(".engadirEvento .eliminar").show();
            						event.preventDefault();
                				}
                			});
                        },
                        onHide : function( element ) {
                        	$(".engadirEvento input").first().val("");
							$("#nomeEvento").val("");
							$("#idEvento").val("");
							$(".engadirEvento .eliminar").hide();
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
                            
                            $(".engadirEvento input").first().val(nome);
							$("#nomeEvento").val(nome);
							$("#idEvento").val(id);
							$(".engadirEvento .eliminar").show();
							$(".error").remove();
                        },
                        commit: function( element ) {
                        	
                        	var id = $("#idEvento").val();
                        	
                        	if(id != null && id != undefined && id != ''){
                        		
                        		if(element.getText() == null || element.getText() == undefined || element.getText() == ''){
                        			element.setText($("#nomeEvento").val());
                        		}
                        		
                        		element.setAttribute( 'title', $("#nomeEvento").val());
                        		element.setAttribute( 'class', 'entidade evento ' + 'evento_'+$("#idEvento").val());
                        	}
                        	else{
                        		element.remove(true);
                        	}
                        }
                    },
                    {
                        type: 'checkbox',
                        id: 'entidadeConFicha',
                        label: editor.lang.evento.ten_ficha,
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

            if ( !element || element.getName() != 'span' || !element.hasClass('evento')) {
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
            	$(".engadirEvento input").first().val(selection.getSelectedText());
            	$(".engadirEvento input").first().trigger( "keydown" );
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