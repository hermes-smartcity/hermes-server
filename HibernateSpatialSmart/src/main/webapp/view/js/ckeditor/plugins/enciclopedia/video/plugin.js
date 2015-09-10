CKEDITOR.plugins.add( 'video', {
	lang: ['gl'],
    init: function( editor ) {
        
        var eliminarVideo = new CKEDITOR.command( editor, {
            exec: function( editor ) {
            	var selection = editor.getSelection();
                var element = selection.getStartElement();

                if ( element ){
                    element = element.getAscendant( 'img', true );
                }
                if ( element && element.getName() == 'img' && element.hasClass('cke_video') ) {
                	element.remove(true);
                	var command = editor.getCommand( 'video' );
                	command.setState( CKEDITOR.TRISTATE_OFF );
                }
            }
        } );
        
        editor.addCommand( 'video', new CKEDITOR.dialogCommand( 'videoDialog' ) );
        editor.addCommand( 'eliminarVideo', eliminarVideo );
        
        editor.ui.addButton( 'Video', {
            label: editor.lang.video.icono,
            command: 'video',
            toolbar: 'others',
            icon: this.path + 'images/video.png'
        });
        
        function seleccion(editor, element) {
			
			if ( editor.readOnly ){
				return;
			}
			
			var command = editor.getCommand( 'video' );
			
			if ( element ){
				var parents = jQuery(element.$).parents('*').andSelf();
				if (parents.filter(".cke_video").size() > 0){
					command.setState( CKEDITOR.TRISTATE_ON );
					return;
				}
			}
			
			command.setState( CKEDITOR.TRISTATE_OFF );
		}
        
		if ( editor.contextMenu ){
			editor.addMenuGroup( 'videoGroup' );
			editor.addMenuItem( 'videoItem',{
				label : editor.lang.video.editar,
				icon : this.path + 'images/video.png',
				command : 'video',
				group : 'videoGroup'
			});
			editor.addMenuItem( 'eliminarVideoItem',{
				label : editor.lang.video.eliminar,
				icon : this.path + 'images/eliminar.png',
				command : 'eliminarVideo',
				group : 'videoGroup'
			});
			editor.contextMenu.addListener( function( element ){
				if ( element ){
					var parents = jQuery(element.$).parents('*').andSelf().filter(".cke_video");
					if (parents.size() == 0){
						element = null;
					}else{
						element = new CKEDITOR.dom.element(parents.get(0));
					}
				}
				if ( element && element.hasClass("cke_video"))
		 			return { videoItem : CKEDITOR.TRISTATE_OFF, eliminarVideoItem: CKEDITOR.TRISTATE_OFF};
		 		return null;
			});
		}
        
		editor.on( 'selectionChange', function( evt ) {
			seleccion(editor, evt.data.path.lastElement);
		});
		editor.on('doubleclick', function(evt){
			seleccion(editor, evt.data.element);
		});
		
        CKEDITOR.dialog.add( 'videoDialog', this.path + 'dialogs/video.js' );
    },
	afterInit : function(editor) {

		var dataProcessor = editor.dataProcessor, htmlFilter = dataProcessor
				&& dataProcessor.htmlFilter, dataFilter = dataProcessor
				&& dataProcessor.dataFilter;

		var contexto = this;
		
		dataFilter
				.addRules({

					elements : {
						$ : function(realElement) {
							
							if (realElement.name == 'video'
									&& realElement.attributes.class
									&& realElement.attributes.class
											.indexOf('video') != -1) {
								var fakeElement = editor
										.createFakeParserElement(realElement,
												'cke_video', 'video', false);
								
								fakeElement.attributes['src'] = CKEDITOR.getUrl( contexto.path + 'images/placeholder.png' );
								fakeElement.attributes["width"] = editor.lang.video.ancho;
					        	fakeElement.attributes["height"] = editor.lang.video.alto;
								
					        	if(realElement.attributes.title != null && realElement.attributes.title != undefined
										&& realElement.attributes.title != ""){
									var title = realElement.attributes.title;
									fakeElement.attributes['title'] = title;
									fakeElement.attributes['alt'] = title;
								}
								if(realElement.attributes.poster != null && realElement.attributes.poster != undefined
										&& realElement.attributes.poster != ""){
									var src = realElement.attributes.poster;
									fakeElement.attributes['style'] = "background-image:url(" + src + ");";
								}
								if(realElement.attributes.class
										.indexOf("videoLinea")!=-1){
									fakeElement.attributes['class'] = fakeElement.attributes['class'] + " cke_video_linea";
								}
								if(realElement.attributes.class
										.indexOf("videoEsquerda")!=-1){
									fakeElement.attributes['class'] = fakeElement.attributes['class'] + " cke_video_esquerda";
								}
								if(realElement.attributes.class
										.indexOf("videoDereita")!=-1){
									fakeElement.attributes['class'] = fakeElement.attributes['class'] + " cke_video_dereita";
								}	
								if(realElement.attributes.class
										.indexOf("videoCentro")!=-1){
									fakeElement.attributes['class'] = fakeElement.attributes['class'] + " cke_video_centro";
								}									
								if(realElement.attributes.class
										.indexOf("conPe")!=-1){
									fakeElement.attributes['class'] = fakeElement.attributes['class'] + " conPe";
								}	
								return fakeElement;
							}
						}
					}

				});
	}
});