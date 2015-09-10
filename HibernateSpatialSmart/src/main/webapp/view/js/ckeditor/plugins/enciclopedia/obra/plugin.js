CKEDITOR.plugins.add( 'obra', {
	lang: ['gl'],
    init: function( editor ) {
        
        var eliminarObra = new CKEDITOR.command( editor, {
            exec: function( editor ) {
            	var selection = editor.getSelection();
                var element = selection.getStartElement();

                if ( element ){
                    element = element.getAscendant( 'span', true );
                }
                if ( element && element.getName() == 'span' ) {
                	element.remove(true);
                	var command = editor.getCommand( 'obra' );
                	command.setState( CKEDITOR.TRISTATE_OFF );
                }
            }
        } );
        
        editor.addCommand( 'obra', new CKEDITOR.dialogCommand( 'obraDialog' ) );
        editor.addCommand( 'eliminarObra', eliminarObra );
        
        editor.ui.addButton( 'Obra', {
            label: editor.lang.obra.icono,
            command: 'obra',
            toolbar: 'others',
            icon: this.path + 'images/obra.png'
        });
        
        function seleccion(editor, element) {
			
			if ( editor.readOnly ){
				return;
			}
			
			var command = editor.getCommand( 'obra' );
			
			if ( element ){
				var parents = jQuery(element.$).parents('*').andSelf();
				if (parents.filter(".obra").size() > 0){
					command.setState( CKEDITOR.TRISTATE_ON );
					return;
				}
			}
			
			command.setState( CKEDITOR.TRISTATE_OFF );
		}
        
		if ( editor.contextMenu ){
			editor.addMenuGroup( 'obraGroup' );
			editor.addMenuItem( 'obraItem',{
				label : editor.lang.obra.editar,
				icon : this.path + 'images/obra.png',
				command : 'obra',
				group : 'obraGroup'
			});
			editor.addMenuItem( 'eliminarObraItem',{
				label : editor.lang.obra.eliminar,
				icon : this.path + 'images/eliminar.png',
				command : 'eliminarObra',
				group : 'obraGroup'
			});
			editor.contextMenu.addListener( function( element ){
				if ( element ){
					var parents = jQuery(element.$).parents('*').andSelf().filter(".obra");
					if (parents.size() == 0){
						element = null;
					}else{
						element = new CKEDITOR.dom.element(parents.get(0));
					}
				}
				if ( element && !element.isReadOnly() && !element.data( 'cke-realelement' ))
		 			return { obraItem : CKEDITOR.TRISTATE_OFF, eliminarObraItem: CKEDITOR.TRISTATE_OFF};
		 		return null;
			});
		}
        
		editor.on( 'selectionChange', function( evt ) {
			seleccion(editor, evt.data.path.lastElement);
		});
		editor.on('doubleclick', function(evt){
			seleccion(editor, evt.data.element);
		});
		
        CKEDITOR.dialog.add( 'obraDialog', this.path + 'dialogs/obra.js' );
    }
});