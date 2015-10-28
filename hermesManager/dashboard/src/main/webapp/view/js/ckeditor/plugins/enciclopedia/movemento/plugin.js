CKEDITOR.plugins.add( 'movemento', {
	lang: ['gl'],
    init: function( editor ) {
        
        var eliminarMovemento = new CKEDITOR.command( editor, {
            exec: function( editor ) {
            	var selection = editor.getSelection();
                var element = selection.getStartElement();

                if ( element ){
                    element = element.getAscendant( 'span', true );
                }
                if ( element && element.getName() == 'span' ) {
                	element.remove(true);
                	var command = editor.getCommand( 'movemento' );
                	command.setState( CKEDITOR.TRISTATE_OFF );
                }
            }
        } );
        
        editor.addCommand( 'movemento', new CKEDITOR.dialogCommand( 'movementoDialog' ) );
        editor.addCommand( 'eliminarMovemento', eliminarMovemento );
        
        editor.ui.addButton( 'Movemento', {
            label: editor.lang.movemento.icono,
            command: 'movemento',
            toolbar: 'others',
            icon: this.path + 'images/movemento.png'
        });
        
        function seleccion(editor, element) {
			
			if ( editor.readOnly ){
				return;
			}
			
			var command = editor.getCommand( 'movemento' );
			
			if ( element ){
				var parents = jQuery(element.$).parents('*').andSelf();
				if (parents.filter(".movemento").size() > 0){
					command.setState( CKEDITOR.TRISTATE_ON );
					return;
				}
			}
			
			command.setState( CKEDITOR.TRISTATE_OFF );
		}
        
		if ( editor.contextMenu ){
			editor.addMenuGroup( 'movementoGroup' );
			editor.addMenuItem( 'movementoItem',{
				label : editor.lang.movemento.editar,
				icon : this.path + 'images/movemento.png',
				command : 'movemento',
				group : 'movementoGroup'
			});
			editor.addMenuItem( 'eliminarMovementoItem',{
				label : editor.lang.movemento.eliminar,
				icon : this.path + 'images/eliminar.png',
				command : 'eliminarMovemento',
				group : 'movementoGroup'
			});
			editor.contextMenu.addListener( function( element ){
				if ( element ){
					var parents = jQuery(element.$).parents('*').andSelf().filter(".movemento");
					if (parents.size() == 0){
						element = null;
					}else{
						element = new CKEDITOR.dom.element(parents.get(0));
					}
				}
				if ( element && !element.isReadOnly() && !element.data( 'cke-realelement' ))
		 			return { movementoItem : CKEDITOR.TRISTATE_OFF, eliminarMovementoItem: CKEDITOR.TRISTATE_OFF};
		 		return null;
			});
		}
        
		editor.on( 'selectionChange', function( evt ) {
			seleccion(editor, evt.data.path.lastElement);
		});
		editor.on('doubleclick', function(evt){
			seleccion(editor, evt.data.element);
		});
		
        CKEDITOR.dialog.add( 'movementoDialog', this.path + 'dialogs/movemento.js' );
    }
});