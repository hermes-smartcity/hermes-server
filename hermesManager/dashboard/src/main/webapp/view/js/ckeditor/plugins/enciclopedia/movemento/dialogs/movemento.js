CKEDITOR.dialog.add( 'movementoDialog', function( editor ) {
    return {
        title: editor.lang.movemento.titulo,
        minWidth: 200,
        minHeight: 100,
        contents: [
            {
                id: 'tab-movemento',
                label: editor.lang.movemento.titulo,
                elements: [
                    {
                        type: 'text',
                        id: 'engadirElemento',
                        className: 'engadirElemento engadirMovemento',
                        label: editor.lang.movemento.nome,
                        validate: function () {
                        	var id = $("#idMovemento").val();
                        	var nome = this.getValue();
                        	if(id == null || id == undefined || id == ''){
                        		$(".error").remove();
                        		if(nome == null || nome == undefined || nome == '')
                        			$(".engadirMovemento input").first().after("<span class='error'>"+editor.lang.movemento.erro_obrigatorio+"</span>");
                        		else
                        			$(".engadirMovemento input").first().after("<span class='error'>"+editor.lang.movemento.erro_non_existe+"</span>");
                        		return false;
                        	}
                        	
                        	$(".error").remove();
                        	return true;
                        },
                        onLoad : function( element ) {
                        	
                        	$(".engadirMovemento input").first()
                        		.after("<img class='eliminar' style='display:none;' src='"+CKEDITOR.plugins.getPath("enciclopedia/movemento/images")+"eliminar.png'/>");
                        	$(".engadirMovemento input").first().after("<input type='hidden' id='nomeMovemento'/>");
                        	$(".engadirMovemento input").first().after("<input type='hidden' id='idMovemento'/>");
                        	
                        	$(".engadirMovemento .eliminar").click(function(){
    							$(".engadirMovemento input").first().val("");
    							$("#nomeMovemento").val("");
    							$("#idMovemento").val("");
    							$(".engadirMovemento .eliminar").hide();
    						});
                        	
                        	$(".engadirMovemento input").first().keydown(function(){
                        		$("#nomeMovemento").val("");
    							$("#idMovemento").val("");
    							$(".engadirMovemento .eliminar").hide();
                        	});
                        	
                        	$(".engadirMovemento input").first().autocomplete({
                				source: function( request, response ) {
                					$.getJSON(CKEDITOR.config.baseHref+"usuarioXestor/literatura/movemento/json/nomesMovementos", {term: request.term}, function(movementos) {
                						response(
                								$.map(movementos, function(movemento) {
                									return {idMovemento: movemento.idMovemento, value: movemento.nome};
                								}
                						));
                					});
                				},
                				minLength: 2,
                				select: function(event, ui) {
                					$(".engadirMovemento input").first().val(ui.item.value);
        							$("#nomeMovemento").val(ui.item.value);
        							$("#idMovemento").val(ui.item.idMovemento);
        							$(".engadirMovemento .eliminar").show();
            						event.preventDefault();
                				}
                			});
                        },
                        onHide : function( element ) {
                        	$(".engadirMovemento input").first().val("");
							$("#nomeMovemento").val("");
							$("#idMovemento").val("");
							$(".engadirMovemento .eliminar").hide();
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
                            
                            $(".engadirMovemento input").first().val(nome);
							$("#nomeMovemento").val(nome);
							$("#idMovemento").val(id);
							$(".engadirMovemento .eliminar").show();
							$(".error").remove();
                        },
                        commit: function( element ) {
                        	
                        	var id = $("#idMovemento").val();
                        	
                        	if(id != null && id != undefined && id != ''){
                        		
                        		if(element.getText() == null || element.getText() == undefined || element.getText() == ''){
                        			element.setText($("#nomeMovemento").val());
                        		}
                        		
                        		element.setAttribute( 'title', $("#nomeMovemento").val());
                        		element.setAttribute( 'class', 'entidade movemento ' + 'movemento_'+$("#idMovemento").val());
                        	}
                        	else{
                        		element.remove(true);
                        	}
                        }
                    },
                    {
                        type: 'checkbox',
                        id: 'entidadeConFicha',
                        label: editor.lang.movemento.ten_ficha,
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

            if ( !element || element.getName() != 'span' || !element.hasClass('movemento')) {
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
            	$(".engadirMovemento input").first().val(selection.getSelectedText());
            	$(".engadirMovemento input").first().trigger( "keydown" );
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