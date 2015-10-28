CKEDITOR.dialog.add( 'obraDialog', function( editor ) {
    return {
        title: editor.lang.obra.titulo,
        minWidth: 200,
        minHeight: 100,
        contents: [
            {
                id: 'tab-obra',
                label: editor.lang.obra.titulo,
                elements: [
                    {
                        type: 'text',
                        id: 'engadirElemento',
                        className: 'engadirElemento engadirObra',
                        label: editor.lang.obra.nome,
                        validate: function () {
                        	var id = $("#idObra").val();
                        	var nome = this.getValue();
                        	if(id == null || id == undefined || id == ''){
                        		$(".error").remove();
                        		if(nome == null || nome == undefined || nome == '')
                        			$(".engadirObra input").first().after("<span class='error'>"+editor.lang.obra.erro_obrigatorio+"</span>");
                        		else
                        			$(".engadirObra input").first().after("<span class='error'>"+editor.lang.obra.erro_non_existe+"</span>");
                        		return false;
                        	}
                        	
                        	$(".error").remove();
                        	return true;
                        },
                        onLoad : function( element ) {
                        	
                        	$(".engadirObra input").first()
                        		.after("<img class='eliminar' style='display:none;' src='"+CKEDITOR.plugins.getPath("enciclopedia/obra/images")+"eliminar.png'/>");
                        	$(".engadirObra input").first().after("<input type='hidden' id='nomeObra'/>");
                        	$(".engadirObra input").first().after("<input type='hidden' id='idObra'/>");
                        	
                        	$(".engadirObra .eliminar").click(function(){
    							$(".engadirObra input").first().val("");
    							$("#nomeObra").val("");
    							$("#idObra").val("");
    							$(".engadirObra .eliminar").hide();
    						});
                        	
                        	$(".engadirObra input").first().keydown(function(){
                        		$("#nomeObra").val("");
    							$("#idObra").val("");
    							$(".engadirObra .eliminar").hide();
                        	});
                        	
                        	$(".engadirObra input").first().autocomplete({
                				source: function( request, response ) {
                					$.getJSON(CKEDITOR.config.baseHref+"usuarioXestor/literatura/obra/json/titulosobras", {term: request.term}, function(obras) {
                						response(
                								$.map(obras, function(obra) {
                									return {idObra: obra.idObra, value: obra.titulo};
                								}
                						));
                					});
                				},
                				minLength: 2,
                				select: function(event, ui) {
                					$(".engadirObra input").first().val(ui.item.value);
        							$("#nomeObra").val(ui.item.value);
        							$("#idObra").val(ui.item.idObra);
        							$(".engadirObra .eliminar").show();
            						event.preventDefault();
                				}
                			});
                        },
                        onHide : function( element ) {
                        	$(".engadirObra input").first().val("");
							$("#nomeObra").val("");
							$("#idObra").val("");
							$(".engadirObra .eliminar").hide();
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
                            
                            $(".engadirObra input").first().val(nome);
							$("#nomeObra").val(nome);
							$("#idObra").val(id);
							$(".engadirObra .eliminar").show();
							$(".error").remove();
                        },
                        commit: function( element ) {
                        	
                        	var id = $("#idObra").val();
                        	
                        	if(id != null && id != undefined && id != ''){
                        		
                        		if(element.getText() == null || element.getText() == undefined || element.getText() == ''){
                        			element.setText($("#nomeObra").val());
                        		}
                        		
                        		element.setAttribute( 'title', $("#nomeObra").val());
                        		element.setAttribute( 'class', 'entidade obra ' + 'obra_'+$("#idObra").val());
                        	}
                        	else{
                        		element.remove(true);
                        	}
                        }
                    },
                    {
                        type: 'checkbox',
                        id: 'entidadeConFicha',
                        label: editor.lang.obra.ten_ficha,
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

            if ( !element || element.getName() != 'span' || !element.hasClass('obra')) {
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
            	$(".engadirObra input").first().val(selection.getSelectedText());
            	$(".engadirObra input").first().trigger( "keydown" );
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