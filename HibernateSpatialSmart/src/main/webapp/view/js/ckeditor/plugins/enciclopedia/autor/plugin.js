CKEDITOR.plugins.add( 'autor', {
	lang: ['gl'],
    init: function( editor ) {
        
        var eliminarAutor = new CKEDITOR.command( editor, {
            exec: function( editor ) {
            	var selection = editor.getSelection();
                var element = selection.getStartElement();

                if ( element ){
                    element = element.getAscendant( 'span', true );
                }
                if ( element && element.getName() == 'span' ) {
                	element.remove(true);
                	var command = editor.getCommand( 'autor' );
                	command.setState( CKEDITOR.TRISTATE_OFF );
                }
            }
        } );
        
        editor.addCommand( 'autor', new CKEDITOR.dialogCommand( 'autorDialog' ) );
        editor.addCommand( 'eliminarAutor', eliminarAutor );
        
        editor.ui.addButton( 'Autor', {
            label: editor.lang.autor.icono,
            command: 'autor',
            toolbar: 'others',
            icon: this.path + 'images/autor.png'
        });
        
        function seleccion(editor, element) {
			
			if ( editor.readOnly ){
				return;
			}
			
			var command = editor.getCommand( 'autor' );
			
			if ( element ){
				var parents = jQuery(element.$).parents('*').andSelf();
				if (parents.filter(".autor").size() > 0){
					command.setState( CKEDITOR.TRISTATE_ON );
					return;
				}
			}
			
			command.setState( CKEDITOR.TRISTATE_OFF );
		}
        
		if ( editor.contextMenu ){
			editor.addMenuGroup( 'autorGroup' );
			editor.addMenuItem( 'autorItem',{
				label : editor.lang.autor.editar,
				icon : this.path + 'images/autor.png',
				command : 'autor',
				group : 'autorGroup'
			});
			editor.addMenuItem( 'eliminarAutorItem',{
				label : editor.lang.autor.eliminar,
				icon : this.path + 'images/eliminar.png',
				command : 'eliminarAutor',
				group : 'autorGroup'
			});
			editor.contextMenu.addListener( function( element ){
				if ( element ){
					var parents = jQuery(element.$).parents('*').andSelf().filter(".autor");
					if (parents.size() == 0){
						element = null;
					}else{
						element = new CKEDITOR.dom.element(parents.get(0));
					}
				}
				if ( element && !element.isReadOnly() && !element.data( 'cke-realelement' ))
		 			return { autorItem : CKEDITOR.TRISTATE_OFF, eliminarAutorItem: CKEDITOR.TRISTATE_OFF};
		 		return null;
			});
		}
        
		editor.on( 'selectionChange', function( evt ) {
			seleccion(editor, evt.data.path.lastElement);
		});
		editor.on('doubleclick', function(evt){
			seleccion(editor, evt.data.element);
		});
		
        CKEDITOR.dialog.add( 'autorDialog', this.path + 'dialogs/autor.js' );
    }
});