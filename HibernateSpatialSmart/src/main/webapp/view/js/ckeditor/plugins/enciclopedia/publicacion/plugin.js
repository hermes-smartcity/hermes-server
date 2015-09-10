CKEDITOR.plugins.add( 'publicacion', {
	lang: ['gl'],
    init: function( editor ) {
        
        var eliminarPublicacion = new CKEDITOR.command( editor, {
            exec: function( editor ) {
            	var selection = editor.getSelection();
                var element = selection.getStartElement();

                if ( element ){
                    element = element.getAscendant( 'span', true );
                }
                if ( element && element.getName() == 'span' ) {
                	element.remove(true);
                	var command = editor.getCommand( 'publicacion' );
                	command.setState( CKEDITOR.TRISTATE_OFF );
                }
            }
        } );
        
        editor.addCommand( 'publicacion', new CKEDITOR.dialogCommand( 'publicacionDialog' ) );
        editor.addCommand( 'eliminarPublicacion', eliminarPublicacion );
        
        editor.ui.addButton( 'Publicacion', {
            label: editor.lang.publicacion.icono,
            command: 'publicacion',
            toolbar: 'others',
            icon: this.path + 'images/publicacion.png'
        });
        
        function seleccion(editor, element) {
			
			if ( editor.readOnly ){
				return;
			}
			
			var command = editor.getCommand( 'publicacion' );
			
			if ( element ){
				var parents = jQuery(element.$).parents('*').andSelf();
				if (parents.filter(".publicacion").size() > 0){
					command.setState( CKEDITOR.TRISTATE_ON );
					return;
				}
			}
			
			command.setState( CKEDITOR.TRISTATE_OFF );
		}
        
		if ( editor.contextMenu ){
			editor.addMenuGroup( 'publicacionGroup' );
			editor.addMenuItem( 'publicacionItem',{
				label : editor.lang.publicacion.editar,
				icon : this.path + 'images/publicacion.png',
				command : 'publicacion',
				group : 'publicacionGroup'
			});
			editor.addMenuItem( 'eliminarPublicacionItem',{
				label : editor.lang.publicacion.eliminar,
				icon : this.path + 'images/eliminar.png',
				command : 'eliminarPublicacion',
				group : 'publicacionGroup'
			});
			editor.contextMenu.addListener( function( element ){
				if ( element ){
					var parents = jQuery(element.$).parents('*').andSelf().filter(".publicacion");
					if (parents.size() == 0){
						element = null;
					}else{
						element = new CKEDITOR.dom.element(parents.get(0));
					}
				}
				if ( element && !element.isReadOnly() && !element.data( 'cke-realelement' ))
		 			return { publicacionItem : CKEDITOR.TRISTATE_OFF, eliminarPublicacionItem: CKEDITOR.TRISTATE_OFF};
		 		return null;
			});
		}
        
		editor.on( 'selectionChange', function( evt ) {
			seleccion(editor, evt.data.path.lastElement);
		});
		editor.on('doubleclick', function(evt){
			seleccion(editor, evt.data.element);
		});
		
        CKEDITOR.dialog.add( 'publicacionDialog', this.path + 'dialogs/publicacion.js' );
    }
});