CKEDITOR.dialog.add( 'organismoDialog', function( editor ) {
    return {
        title: editor.lang.organismo.titulo,
        minWidth: 200,
        minHeight: 100,
        contents: [
            {
                id: 'tab-organismo',
                label: editor.lang.organismo.titulo,
                elements: [
                    {
                        type: 'text',
                        id: 'engadirElemento',
                        className: 'engadirElemento engadirOrganismo',
                        label: editor.lang.organismo.nome,
                        validate: function () {
                        	var id = $("#idOrganismo").val();
                        	var nome = this.getValue();
                        	if(id == null || id == undefined || id == ''){
                        		$(".error").remove();
                        		if(nome == null || nome == undefined || nome == '')
                        			$(".engadirOrganismo input").first().after("<span class='error'>"+editor.lang.organismo.erro_obrigatorio+"</span>");
                        		else
                        			$(".engadirOrganismo input").first().after("<span class='error'>"+editor.lang.organismo.erro_non_existe+"</span>");
                        		return false;
                        	}
                        	
                        	$(".error").remove();
                        	return true;
                        },
                        onLoad : function( element ) {
                        	
                        	$(".engadirOrganismo input").first()
                        		.after("<img class='eliminar' style='display:none;' src='"+CKEDITOR.plugins.getPath("enciclopedia/organismo/images")+"eliminar.png'/>");
                        	$(".engadirOrganismo input").first().after("<input type='hidden' id='nomeOrganismo'/>");
                        	$(".engadirOrganismo input").first().after("<input type='hidden' id='idOrganismo'/>");
                        	
                        	$(".engadirOrganismo .eliminar").click(function(){
    							$(".engadirOrganismo input").first().val("");
    							$("#nomeOrganismo").val("");
    							$("#idOrganismo").val("");
    							$(".engadirOrganismo .eliminar").hide();
    						});
                        	
                        	$(".engadirOrganismo input").first().keydown(function(){
                        		$("#nomeOrganismo").val("");
    							$("#idOrganismo").val("");
    							$(".engadirOrganismo .eliminar").hide();
                        	});
                        	
                        	$(".engadirOrganismo input").first().autocomplete({
                				source: function( request, response ) {
                					$.getJSON(CKEDITOR.config.baseHref+"usuarioXestor/literatura/organismo/json/nomesOrganismos", {term: request.term}, function(organismos) {
                						response(
                								$.map(organismos, function(organismo) {
                									return {idOrganismo: organismo.idOrganismo, value: organismo.nome};
                								}
                						));
                					});
                				},
                				minLength: 2,
                				select: function(event, ui) {
                					$(".engadirOrganismo input").first().val(ui.item.value);
        							$("#nomeOrganismo").val(ui.item.value);
        							$("#idOrganismo").val(ui.item.idOrganismo);
        							$(".engadirOrganismo .eliminar").show();
            						event.preventDefault();
                				}
                			});
                        },
                        onHide : function( element ) {
                        	$(".engadirOrganismo input").first().val("");
							$("#nomeOrganismo").val("");
							$("#idOrganismo").val("");
							$(".engadirOrganismo .eliminar").hide();
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
                            
                            $(".engadirOrganismo input").first().val(nome);
							$("#nomeOrganismo").val(nome);
							$("#idOrganismo").val(id);
							$(".engadirOrganismo .eliminar").show();
							$(".error").remove();
                        },
                        commit: function( element ) {
                        	
                        	var id = $("#idOrganismo").val();
                        	
                        	if(id != null && id != undefined && id != ''){
                        		
                        		if(element.getText() == null || element.getText() == undefined || element.getText() == ''){
                        			element.setText($("#nomeOrganismo").val());
                        		}
                        		
                        		element.setAttribute( 'title', $("#nomeOrganismo").val());
                        		element.setAttribute( 'class', 'entidade organismo ' + 'organismo_'+$("#idOrganismo").val());
                        	}
                        	else{
                        		element.remove(true);
                        	}
                        }
                    },
                    {
                        type: 'checkbox',
                        id: 'entidadeConFicha',
                        label: editor.lang.organismo.ten_ficha,
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

            if ( !element || element.getName() != 'span' || !element.hasClass('organismo')) {
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
            	$(".engadirOrganismo input").first().val(selection.getSelectedText());
            	$(".engadirOrganismo input").first().trigger( "keydown" );
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