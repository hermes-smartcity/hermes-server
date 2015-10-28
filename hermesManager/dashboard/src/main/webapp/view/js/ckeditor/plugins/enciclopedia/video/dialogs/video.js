CKEDITOR.dialog.add( 'videoDialog', function( editor ) {
    return {
        title: editor.lang.video.titulo,
        minWidth: 200,
        minHeight: 100,
        contents: [
            {
                id: 'tab-video',
                label: editor.lang.video.titulo,
                elements: [
                    {
                        type: 'text',
                        id: 'engadirElemento',
                        className: 'engadirElemento engadirVideo',
                        label: editor.lang.video.nome,
                        validate: function () {
                        	var id = $("#idVideo").val();
                        	var nome = this.getValue();
                        	if(id == null || id == undefined || id == ''){
                        		$(".error").remove();
                        		if(nome == null || nome == undefined || nome == '')
                        			$(".engadirVideo input").first().after("<span class='error'>"+editor.lang.video.erro_obrigatorio+"</span>");
                        		else
                        			$(".engadirVideo input").first().after("<span class='error'>"+editor.lang.video.erro_non_existe+"</span>");
                        		return false;
                        	}
                        	
                        	$(".error").remove();
                        	return true;
                        },
                        onLoad : function( element ) {
                        	
                        	$(".engadirVideo input").first()
                        		.after("<img class='eliminar' style='display:none;' src='"+CKEDITOR.plugins.getPath("enciclopedia/video/images")+"eliminar.png'/>");
                        	$(".engadirVideo input").first().after("<input type='hidden' id='nomeVideo'/>");
                        	$(".engadirVideo input").first().after("<input type='hidden' id='idVideo'/>");
                        	$(".engadirVideo input").first().after("<input type='hidden' id='mp4Video'/>");
                        	$(".engadirVideo input").first().after("<input type='hidden' id='webmVideo'/>");
                        	$(".engadirVideo input").first().after("<input type='hidden' id='posterVideo'/>");
                        	
                        	$(".engadirVideo .eliminar").click(function(){
    							$(".engadirVideo input").first().val("");
    							$("#nomeVideo").val("");
    							$("#idVideo").val("");
    							$("#mp4Video").val("");
    							$("#webmVideo").val("");
    							$("#posterVideo").val("");
    							$(".engadirVideo .eliminar").hide();
    						});
                        	
                        	$(".engadirVideo input").first().keydown(function(){
                        		$("#nomeVideo").val("");
    							$("#idVideo").val("");
    							$("#mp4Video").val("");
    							$("#webmVideo").val("");
    							$("#posterVideo").val("");
    							$(".engadirVideo .eliminar").hide();
                        	});
                        	
                        	var rutaElementosMultimedia = "ficheiros/elemsMultimedia/";
                        	
                        	$(".engadirVideo input").first().autocomplete({
                				source: function( request, response ) {
                					$.getJSON(CKEDITOR.config.baseHref+"usuarioXestor/literatura/elemMultimedia/json/listarDatosVideo", {term: request.term}, function(videos) {
                						response(
                								$.map(videos, function(video) {
                									return {idVideo: video.idElemMultimedia, value: video.tituloFormateado, poster: video.ficheiroCaptura, 
                												mp4: video.ficheiroMP4, webm: video.ficheiroWebM};
                								}
                						));
                					});
                				},
                				minLength: 2,
                				select: function(event, ui) {
                					var src = rutaElementosMultimedia+ ui.item.idVideo + "/";
                					$(".engadirVideo input").first().val(ui.item.value);
        							$("#nomeVideo").val(ui.item.value);
        							$("#idVideo").val(ui.item.idVideo);
        							$("#mp4Video").val(src+ui.item.mp4);
        							$("#webmVideo").val(src+ui.item.webm);
        							$("#posterVideo").val(src+ui.item.poster);
        							$(".engadirVideo .eliminar").show();
            						event.preventDefault();
                				}
                			});
                        },
                        onHide : function( element ) {
                        	$(".engadirVideo input").first().val("");
							$("#nomeVideo").val("");
							$("#idVideo").val("");
							$("#mp4Video").val("");
							$("#webmVideo").val("");
							$("#posterVideo").val("");
							$(".engadirVideo .eliminar").hide();
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
                            var webm = element.getChild(1).getAttribute("src");
                            var poster = element.getAttribute('poster');
                            
                            $(".engadirVideo input").first().val(nome);
							$("#nomeVideo").val(nome);
							$("#idVideo").val(id);
							$("#mp4Video").val(mp3);
							$("#webmVideo").val(webm);
							$("#posterVideo").val(poster);
							$(".engadirVideo .eliminar").show();
							$(".error").remove();
                        },
                        commit: function( element ) {
                        	
                        	var id = $("#idVideo").val();
                        	
                        	if(id != null && id != undefined && id != ''){
                        		var sourceMP4 = element.getChild(0);
                        		var sourceWebM = element.getChild(1);
                        		sourceMP4.setAttribute("src",$("#mp4Video").val());
                        		sourceWebM.setAttribute("src",$("#webmVideo").val());
                        		sourceMP4.setAttribute("data-cke-saved-src",$("#mp4Video").val());
                        		sourceWebM.setAttribute("data-cke-saved-src",$("#webmVideo").val());
                        		element.setAttribute( 'title', $("#nomeVideo").val());
                        		element.setAttribute( 'poster', $("#posterVideo").val());
                        		element.setAttribute( 'class', 'entidade video ' + 'video_'+$("#idVideo").val());
                        	}
                        	else{
                        		element.remove(true);
                        	}
                        }
                    },
                    {
                        type: 'checkbox',
                        id: 'multimediaConPe',
                        label: editor.lang.video.pe,
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
						id: 'videoAlign',
						requiredContent: 'video{style}',
						type: 'select',
						widths: [ '35%', '65%' ],
						style: 'width:auto',
						label: editor.lang.video.posicion,
						'default': 'videoLinea',
						items: [
							[ editor.lang.video.posicion_linha, 'videoLinea' ],
							[ editor.lang.video.posicion_esquerda, 'videoEsquerda' ],
							[ editor.lang.video.posicion_dereita, 'videoDereita' ],
							[ editor.lang.video.posicion_centro, 'videoCentro' ],							
						],
						setup: function( element ) {
							
							var css = element.getAttribute("class");
							var actual = "";
							if(css.indexOf("videoLinea")!=-1)
								actual = "videoLinea";
							if(css.indexOf("videoEsquerda")!=-1)
								actual = "videoEsquerda";
							if(css.indexOf("videoDereita")!=-1)
								actual = "videoDereita";
							if(css.indexOf("videoCentro")!=-1)
								actual = "videoCentro";							
							if(actual != "")
								this.setValue(actual);
						},
						commit: function(element) {
							
							var css = element.getAttribute("class");
							css.replace("videoLinea","");
							css.replace("videoEsquerda","");
							css.replace("videoDereita","");
							css.replace("videoCentro","");							
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

            if ( !element || element.getName() != 'img' || !element.hasClass("cke_video")) {
                element = editor.document.createElement( 'video' );
                element.setAttribute("controls", "");
                element.setAttribute("poster", "");
                var sourceMP4 = editor.document.createElement( 'source' );
                sourceMP4.setAttribute("type", "video/mp4");
                var sourceWebM = editor.document.createElement( 'source' );
                sourceWebM.setAttribute("type", "video/webm")
                element.append(sourceMP4);
                element.append(sourceWebM);
                element.appendText(editor.lang.video.html5_non_soportado);
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
        	var fakeElement = editor.createFakeElement(span, 'cke_video', 'video', false);
        	fakeElement.setAttribute("src", CKEDITOR.plugins.getPath("enciclopedia/video/images" )+"placeholder.png");
        	fakeElement.setAttribute("width", editor.lang.video.ancho);
        	fakeElement.setAttribute("height", editor.lang.video.alto);
        	
        	if(span.getAttribute("title")!=null && span.getAttribute("title")!=undefined && span.getAttribute("title")!=""){
        		var title = span.getAttribute("title");
        		fakeElement.setAttribute("alt", title);
            	fakeElement.setAttribute("title", title);
        	}
        	if(span.getAttribute("poster")!=null && span.getAttribute("poster")!=undefined && span.getAttribute("poster")!=""){
        		var src = span.getAttribute("poster");
        		fakeElement.setAttribute("style", "background-image:url(" + src + ");");
        	}
            if(span.hasClass("videoLinea")){
				fakeElement.setAttribute('class', fakeElement.getAttribute('class') + " cke_video_linea");
			}
			if(span.hasClass("videoEsquerda")){
				fakeElement.setAttribute('class', fakeElement.getAttribute('class') + " cke_video_esquerda");
			}
			if(span.hasClass("videoDereita")){
				fakeElement.setAttribute('class', fakeElement.getAttribute('class') + " cke_video_dereita");
			}	
			if(span.hasClass("videoCentro")){
				fakeElement.setAttribute('class', fakeElement.getAttribute('class') + " cke_video_centro");
			}				
			if(span.hasClass("conPe")){
				fakeElement.setAttribute('class', fakeElement.getAttribute('class') + " conPe");
			}
			editor.insertElement( fakeElement );
        }
    };
});