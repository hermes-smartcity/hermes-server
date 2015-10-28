CKEDITOR.dialog.add( 'audioDialog', function( editor ) {
    return {
        title: editor.lang.audio.titulo,
        minWidth: 200,
        minHeight: 100,
        contents: [
            {
                id: 'tab-audio',
                label: editor.lang.audio.titulo,
                elements: [
                    {
                        type: 'text',
                        id: 'engadirElemento',
                        className: 'engadirElemento engadirAudio',
                        label: editor.lang.audio.nome,
                        validate: function () {
                        	var id = $("#idAudio").val();
                        	var nome = this.getValue();
                        	if(id == null || id == undefined || id == ''){
                        		$(".error").remove();
                        		if(nome == null || nome == undefined || nome == '')
                        			$(".engadirAudio input").first().after("<span class='error'>"+editor.lang.audio.erro_obrigatorio+"</span>");
                        		else
                        			$(".engadirAudio input").first().after("<span class='error'>"+editor.lang.audio.erro_non_existe+"</span>");
                        		return false;
                        	}
                        	
                        	$(".error").remove();
                        	return true;
                        },
                        onLoad : function( element ) {
                        	
                        	$(".engadirAudio input").first()
                        		.after("<img class='eliminar' style='display:none;' src='"+CKEDITOR.plugins.getPath("enciclopedia/audio/images")+"eliminar.png'/>");
                        	$(".engadirAudio input").first().after("<input type='hidden' id='nomeAudio'/>");
                        	$(".engadirAudio input").first().after("<input type='hidden' id='idAudio'/>");
                        	$(".engadirAudio input").first().after("<input type='hidden' id='mp3Audio'/>");
                        	$(".engadirAudio input").first().after("<input type='hidden' id='oggAudio'/>");
                        	
                        	$(".engadirAudio .eliminar").click(function(){
    							$(".engadirAudio input").first().val("");
    							$("#nomeAudio").val("");
    							$("#idAudio").val("");
    							$("#mp3Audio").val("");
    							$("#oggAudio").val("");
    							$(".engadirAudio .eliminar").hide();
    						});
                        	
                        	$(".engadirAudio input").first().keydown(function(){
                        		$("#nomeAudio").val("");
    							$("#idAudio").val("");
    							$("#mp3Audio").val("");
    							$("#oggAudio").val("");
    							$(".engadirAudio .eliminar").hide();
                        	});
                        	
                        	var rutaElementosMultimedia = "ficheiros/elemsMultimedia/";
                        	
                        	$(".engadirAudio input").first().autocomplete({
                				source: function( request, response ) {
                					$.getJSON(CKEDITOR.config.baseHref+"usuarioXestor/literatura/elemMultimedia/json/listarDatosAudio", {term: request.term}, function(audios) {
                						response(
                								$.map(audios, function(audio) {
                									return {idAudio: audio.idElemMultimedia, value: audio.tituloFormateado, mp3: audio.archivoMP3, ogg: audio.archivoOGG};
                								}
                						));
                					});
                				},
                				minLength: 2,
                				select: function(event, ui) {
                					var src = rutaElementosMultimedia+ ui.item.idAudio + "/";
                					$(".engadirAudio input").first().val(ui.item.value);
        							$("#nomeAudio").val(ui.item.value);
        							$("#idAudio").val(ui.item.idAudio);
        							$("#mp3Audio").val(src+ui.item.mp3);
        							$("#oggAudio").val(src+ui.item.ogg);
        							$(".engadirAudio .eliminar").show();
            						event.preventDefault();
                				}
                			});
                        },
                        onHide : function( element ) {
                        	$(".engadirAudio input").first().val("");
							$("#nomeAudio").val("");
							$("#idAudio").val("");
							$("#mp3Audio").val("");
							$("#oggAudio").val("");
							$(".engadirAudio .eliminar").hide();
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
                            var mp3 = element.getChild(0).getAttribute("src");
                            var ogg = element.getChild(1).getAttribute("src");
                            
                            $(".engadirAudio input").first().val(nome);
							$("#nomeAudio").val(nome);
							$("#idAudio").val(id);
							$("#mp3Audio").val(mp3);
							$("#oggAudio").val(ogg);
							$(".engadirAudio .eliminar").show();
							$(".error").remove();
                        },
                        commit: function( element ) {
                        	
                        	var id = $("#idAudio").val();
                        	
                        	if(id != null && id != undefined && id != ''){
                        		var sourceMP3 = element.getChild(0);
                        		var sourceOGG = element.getChild(1);
                        		sourceMP3.setAttribute("src",$("#mp3Audio").val());
                        		sourceOGG.setAttribute("src",$("#oggAudio").val());
                        		sourceMP3.setAttribute("data-cke-saved-src",$("#mp3Audio").val());
                        		sourceOGG.setAttribute("data-cke-saved-src",$("#oggAudio").val());
                        		element.setAttribute( 'title', $("#nomeAudio").val());
                        		element.setAttribute( 'class', 'entidade audio ' + 'audio_'+$("#idAudio").val());
                        	}
                        	else{
                        		element.remove(true);
                        	}
                        }
                    },
                    {
                        type: 'checkbox',
                        id: 'multimediaConPe',
                        label: editor.lang.audio.pe,
                        default: false,
                        commit: function( element ) {
                        	
                        	if(this.getValue()){
                        		var clases = element.getAttribute( 'class');
                        		element.setAttribute( 'class', clases + ' conPe');
                        	}
                        },
                        setup: function( element ) {
                        	
                            var identificador = element.hasClass("conPe");
                            if(identificador == true){
                            	this.setValue(true);
                            }
                            else{
                            	this.setValue(false);
                            }
                        }
                    },
                    {
						id: 'audioAlign',
						requiredContent: 'audio{style}',
						type: 'select',
						widths: [ '35%', '65%' ],
						style: 'width:auto',
						label: editor.lang.audio.posicion,
						'default': 'audioLinea',
						items: [
							[ editor.lang.audio.posicion_linha, 'audioLinea' ],
							[ editor.lang.audio.posicion_esquerda, 'audioEsquerda' ],
							[ editor.lang.audio.posicion_dereita, 'audioDereita' ],
							[ editor.lang.audio.posicion_centro, 'audioCentro' ]							
						],
						setup: function( element ) {
							
							var css = element.getAttribute("class");
							var actual = "";
							if(css.indexOf("audioLinea")!=-1)
								actual = "audioLinea";
							if(css.indexOf("audioEsquerda")!=-1)
								actual = "audioEsquerda";
							if(css.indexOf("audioDereita")!=-1)
								actual = "audioDereita";
							if(css.indexOf("audioCentro")!=-1)
								actual = "audioCentro";							
							if(actual != "")
								this.setValue(actual);
						},
						commit: function(element) {
							
							var css = element.getAttribute("class");
							css.replace("audioLinea","");
							css.replace("audioEsquerda","");
							css.replace("audioDereita","");
							css.replace("audioCentro","");							
							css = css + " " + this.getValue();
							element.setAttribute("class", css);
						}
					}
                ]
            },
        ],
        onShow: function() {
            var selection = editor.getSelection();
            var element = selection.getStartElement();

            if ( element )
                element = element.getAscendant( 'img', true );

            if ( !element || element.getName() != 'img' || !element.hasClass("cke_audio")) {
                element = editor.document.createElement( 'audio' );
                element.setAttribute("controls", "");
                var sourceMP3 = editor.document.createElement( 'source' );
                sourceMP3.setAttribute("type", "audio/mpeg");
                var sourceOGG = editor.document.createElement( 'source' );
                sourceOGG.setAttribute("type", "audio/ogg")
                element.append(sourceMP3);
                element.append(sourceOGG);
                element.appendText(editor.lang.audio.html5_non_soportado);
                this.insertMode = true;
            }
            else
                this.insertMode = false;

            this.element = element;
            
            if ( !this.insertMode )
                this.setupContent( editor.restoreRealElement(this.element) );
        },
        onOk: function() {
            var dialog = this;
            var span = null;
            
            if(!this.insertMode){
            	span = editor.restoreRealElement(this.element);
            }
            else{
            	span = this.element;
            }
            
            this.commitContent( span );
        	var fakeElement = editor.createFakeElement(span, 'cke_audio', 'audio', false);
        	fakeElement.setAttribute("src", CKEDITOR.plugins.getPath("enciclopedia/audio/images" )+"placeholder.png");
        	
        	if(span.getAttribute("title")!=null && span.getAttribute("title")!=undefined && span.getAttribute("title")!=""){
        		var title = span.getAttribute("title");
        		fakeElement.setAttribute("alt", title);
            	fakeElement.setAttribute("title", title);
        	}
            if(span.hasClass("audioLinea")){
				fakeElement.setAttribute('class', fakeElement.getAttribute('class') + " cke_audio_linea");
			}
			if(span.hasClass("audioEsquerda")){
				fakeElement.setAttribute('class', fakeElement.getAttribute('class') + " cke_audio_esquerda");
			}
			if(span.hasClass("audioDereita")){
				fakeElement.setAttribute('class', fakeElement.getAttribute('class') + " cke_audio_dereita");
			}	
			if(span.hasClass("audioCentro")){
				fakeElement.setAttribute('class', fakeElement.getAttribute('class') + " cke_audio_centro");
			}				
			if(span.hasClass("conPe")){
				fakeElement.setAttribute('class', fakeElement.getAttribute('class') + " conPe");
			}
			editor.insertElement( fakeElement );
        }
    };
});