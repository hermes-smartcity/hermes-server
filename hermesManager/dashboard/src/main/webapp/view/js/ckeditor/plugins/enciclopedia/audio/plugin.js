CKEDITOR.plugins.add( 'audio', {
	lang: ['gl'],
    init: function( editor ) {
        
        var eliminarAudio = new CKEDITOR.command( editor, {
            exec: function( editor ) {
            	var selection = editor.getSelection();
                var element = selection.getStartElement();

                if ( element ){
                    element = element.getAscendant( 'img', true );
                }
                if ( element && element.getName() == 'img' && element.hasClass('cke_audio') ) {
                	element.remove(true);
                	var command = editor.getCommand( 'audio' );
                	command.setState( CKEDITOR.TRISTATE_OFF );
                }
            }
        } );
        
        editor.addCommand( 'audio', new CKEDITOR.dialogCommand( 'audioDialog' ) );
        editor.addCommand( 'eliminarAudio', eliminarAudio );
        
        editor.ui.addButton( 'Audio', {
            label: editor.lang.audio.icono,
            command: 'audio',
            toolbar: 'others',
            icon: this.path + 'images/audio.png'
        });
        
        function seleccion(editor, element) {
			
			if ( editor.readOnly ){
				return;
			}
			
			var command = editor.getCommand( 'audio' );
			
			if ( element ){
				var parents = jQuery(element.$).parents('*').andSelf();
				if (parents.filter(".cke_audio").size() > 0){
					command.setState( CKEDITOR.TRISTATE_ON );
					return;
				}
			}
			
			command.setState( CKEDITOR.TRISTATE_OFF );
		}
        
		if ( editor.contextMenu ){
			editor.addMenuGroup( 'audioGroup' );
			editor.addMenuItem( 'audioItem',{
				label : editor.lang.audio.editar,
				icon : this.path + 'images/audio.png',
				command : 'audio',
				group : 'audioGroup'
			});
			editor.addMenuItem( 'eliminarAudioItem',{
				label : editor.lang.audio.eliminar,
				icon : this.path + 'images/eliminar.png',
				command : 'eliminarAudio',
				group : 'audioGroup'
			});
			editor.contextMenu.addListener( function( element ){
				if ( element ){
					var parents = jQuery(element.$).parents('*').andSelf().filter(".cke_audio");
					if (parents.size() == 0){
						element = null;
					}else{
						element = new CKEDITOR.dom.element(parents.get(0));
					}
				}
				if ( element && element.hasClass("cke_audio"))
		 			return { audioItem : CKEDITOR.TRISTATE_OFF, eliminarAudioItem: CKEDITOR.TRISTATE_OFF};
		 		return null;
			});
		}
        
		editor.on( 'selectionChange', function( evt ) {
			seleccion(editor, evt.data.path.lastElement);
		});
		editor.on('doubleclick', function(evt){
			seleccion(editor, evt.data.element);
		});
		
        CKEDITOR.dialog.add( 'audioDialog', this.path + 'dialogs/audio.js' );
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
							
							if (realElement.name == 'audio'
									&& realElement.attributes.class
									&& realElement.attributes.class
											.indexOf('audio') != -1) {
								var fakeElement = editor
										.createFakeParserElement(realElement,
												'cke_audio', 'audio', false);
								
								fakeElement.attributes['src'] = CKEDITOR.getUrl( contexto.path + 'images/placeholder.png' );
								
								if(realElement.attributes.title != null && realElement.attributes.title != undefined
										&& realElement.attributes.title != ""){
									var title = realElement.attributes.title;
									fakeElement.attributes['title'] = title;
									fakeElement.attributes['alt'] = title;
								}
								if(realElement.attributes.class
										.indexOf("audioLinea")!=-1){
									fakeElement.attributes['class'] = fakeElement.attributes['class'] + " cke_audio_linea";
								}
								if(realElement.attributes.class
										.indexOf("audioEsquerda")!=-1){
									fakeElement.attributes['class'] = fakeElement.attributes['class'] + " cke_audio_esquerda";
								}
								if(realElement.attributes.class
										.indexOf("audioDereita")!=-1){
									fakeElement.attributes['class'] = fakeElement.attributes['class'] + " cke_audio_dereita";
								}	
								if(realElement.attributes.class
										.indexOf("audioCentro")!=-1){
									fakeElement.attributes['class'] = fakeElement.attributes['class'] + " cke_audio_centro";
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