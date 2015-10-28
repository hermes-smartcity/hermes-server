CKEDITOR.plugins.add( 'evento', {
	lang: ['gl'],
    init: function( editor ) {
        
        var eliminarEvento = new CKEDITOR.command( editor, {
            exec: function( editor ) {
            	var selection = editor.getSelection();
                var element = selection.getStartElement();

                if ( element ){
                    element = element.getAscendant( 'span', true );
                }
                if ( element && element.getName() == 'span' ) {
                	element.remove(true);
                	var command = editor.getCommand( 'evento' );
                	command.setState( CKEDITOR.TRISTATE_OFF );
                }
            }
        } );
        
        editor.addCommand( 'evento', new CKEDITOR.dialogCommand( 'eventoDialog' ) );
        editor.addCommand( 'eliminarEvento', eliminarEvento );
        
        editor.ui.addButton( 'Evento', {
            label: editor.lang.evento.icono,
            command: 'evento',
            toolbar: 'others',
            icon: this.path + 'images/evento.png'
        });
        
        function seleccion(editor, element) {
			
			if ( editor.readOnly ){
				return;
			}
			
			var command = editor.getCommand( 'evento' );
			
			if ( element ){
				var parents = jQuery(element.$).parents('*').andSelf();
				if (parents.filter(".evento").size() > 0){
					command.setState( CKEDITOR.TRISTATE_ON );
					return;
				}
			}
			
			command.setState( CKEDITOR.TRISTATE_OFF );
		}
        
		if ( editor.contextMenu ){
			editor.addMenuGroup( 'eventoGroup' );
			editor.addMenuItem( 'eventoItem',{
				label : editor.lang.evento.editar,
				icon : this.path + 'images/evento.png',
				command : 'evento',
				group : 'eventoGroup'
			});
			editor.addMenuItem( 'eliminarEventoItem',{
				label : editor.lang.evento.eliminar,
				icon : this.path + 'images/eliminar.png',
				command : 'eliminarEvento',
				group : 'eventoGroup'
			});
			editor.contextMenu.addListener( function( element ){
				if ( element ){
					var parents = jQuery(element.$).parents('*').andSelf().filter(".evento");
					if (parents.size() == 0){
						element = null;
					}else{
						element = new CKEDITOR.dom.element(parents.get(0));
					}
				}
				if ( element && !element.isReadOnly() && !element.data( 'cke-realelement' ))
		 			return { eventoItem : CKEDITOR.TRISTATE_OFF, eliminarEventoItem: CKEDITOR.TRISTATE_OFF};
		 		return null;
			});
		}
        
		editor.on( 'selectionChange', function( evt ) {
			seleccion(editor, evt.data.path.lastElement);
		});
		editor.on('doubleclick', function(evt){
			seleccion(editor, evt.data.element);
		});
		
        CKEDITOR.dialog.add( 'eventoDialog', this.path + 'dialogs/evento.js' );
    }
});