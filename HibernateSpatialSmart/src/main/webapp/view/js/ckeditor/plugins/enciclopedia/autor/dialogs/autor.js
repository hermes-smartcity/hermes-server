CKEDITOR.dialog.add( 'autorDialog', function( editor ) {
    return {
        title: editor.lang.autor.titulo,
        minWidth: 200,
        minHeight: 100,
        contents: [
            {
                id: 'tab-autor',
                label: editor.lang.autor.titulo,
                elements: [
                    {
                        type: 'text',
                        id: 'engadirElemento',
                        className: 'engadirElemento engadirAutor',
                        label: editor.lang.autor.nome,
                        validate: function () {
                        	var id = $("#idAutor").val();
                        	var nome = this.getValue();
                        	if(id == null || id == undefined || id == ''){
                        		$(".error").remove();
                        		if(nome == null || nome == undefined || nome == '')
                        			$(".engadirAutor input").first().after("<span class='error'>"+editor.lang.autor.erro_obrigatorio+"</span>");
                        		else
                        			$(".engadirAutor input").first().after("<span class='error'>"+editor.lang.autor.erro_non_existe+"</span>");
                        		return false;
                        	}
                        	
                        	$(".error").remove();
                        	return true;
                        },
                        onLoad : function( element ) {
                        	
                        	$(".engadirAutor input").first()
                        		.after("<img class='eliminar' style='display:none;' src='"+CKEDITOR.plugins.getPath("enciclopedia/autor/images")+"eliminar.png'/>");
                        	$(".engadirAutor input").first().after("<input type='hidden' id='nomeAutor'/>");
                        	$(".engadirAutor input").first().after("<input type='hidden' id='idAutor'/>");
                        	
                        	$(".engadirAutor .eliminar").click(function(){
    							$(".engadirAutor input").first().val("");
    							$("#nomeAutor").val("");
    							$("#idAutor").val("");
    							$(".engadirAutor .eliminar").hide();
    						});
                        	
                        	$(".engadirAutor input").first().keydown(function(){
                        		$("#nomeAutor").val("");
    							$("#idAutor").val("");
    							$(".engadirAutor .eliminar").hide();
                        	});
                        	
                        	$(".engadirAutor input").first().autocomplete({
                				source: function( request, response ) {
                					$.getJSON(CKEDITOR.config.baseHref+"usuarioXestor/literatura/autor/json/nomesAutores", {term: request.term}, function(autores) {
                						response(
                								$.map(autores, function(autor) {
                									return {idAutor: autor.idAutor, value: autor.pseudonimo};
                								}
                						));
                					});
                				},
                				minLength: 2,
                				select: function(event, ui) {
                					$(".engadirAutor input").first().val(ui.item.value);
        							$("#nomeAutor").val(ui.item.value);
        							$("#idAutor").val(ui.item.idAutor);
        							$(".engadirAutor .eliminar").show();
            						event.preventDefault();
                				}
                			});
                        },
                        onHide : function( element ) {
                        	$(".engadirAutor input").first().val("");
							$("#nomeAutor").val("");
							$("#idAutor").val("");
							$(".engadirAutor .eliminar").hide();
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
                            
                            $(".engadirAutor input").first().val(nome);
							$("#nomeAutor").val(nome);
							$("#idAutor").val(id);
							$(".engadirAutor .eliminar").show();
							$(".error").remove();
                        },
                        commit: function( element ) {
                        	
                        	var id = $("#idAutor").val();
                        	
                        	if(id != null && id != undefined && id != ''){
                        		
                        		if(element.getText() == null || element.getText() == undefined || element.getText() == ''){
                        			element.setText($("#nomeAutor").val());
                        		}
                        		
                        		element.setAttribute( 'title', $("#nomeAutor").val());
                        		element.setAttribute( 'class', 'entidade autor ' + 'autor_'+$("#idAutor").val());
                        	}
                        	else{
                        		element.remove(true);
                        	}
                        }
                    },
                    {
                        type: 'checkbox',
                        id: 'entidadeConFicha',
                        label: editor.lang.autor.ten_ficha,
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

            if ( !element || element.getName() != 'span' || !element.hasClass('autor')) {
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
            	$(".engadirAutor input").first().val(selection.getSelectedText());
            	$(".engadirAutor input").first().trigger( "keydown" );
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