CKEDITOR.plugins.add( 'imaxe', {
	lang: ['gl'],
    init: function( editor ) {
        
        var eliminarImaxe = new CKEDITOR.command( editor, {
            exec: function( editor ) {
            	var selection = editor.getSelection();
                var element = selection.getStartElement();

                if ( element ){
                    element = element.getAscendant( 'img', true );
                }
                if ( element && element.getName() == 'img' && element.hasClass('imaxe')) {
                	element.remove(true);
                	var command = editor.getCommand( 'imaxe' );
                	command.setState( CKEDITOR.TRISTATE_OFF );
                }
            }
        } );
        
        editor.addCommand( 'imaxe', new CKEDITOR.dialogCommand( 'imaxeDialog' ) );
        editor.addCommand( 'eliminarImaxe', eliminarImaxe );
        
        editor.ui.addButton( 'Imaxe', {
            label: editor.lang.imaxe.icono,
            command: 'imaxe',
            toolbar: 'others',
            icon: this.path + 'images/imaxe.png'
        });
        
        function seleccion(editor, element) {
			
			if ( editor.readOnly ){
				return;
			}
			
			var command = editor.getCommand( 'imaxe' );
			
			if ( element ){
				var parents = jQuery(element.$).parents('*').andSelf();
				if (parents.filter(".imaxe").size() > 0){
					command.setState( CKEDITOR.TRISTATE_ON );
					return;
				}
			}
			
			command.setState( CKEDITOR.TRISTATE_OFF );
		}
        
		if ( editor.contextMenu ){
			editor.addMenuGroup( 'imaxeGroup' );
			editor.addMenuItem( 'imaxeItem',{
				label : editor.lang.imaxe.editar,
				icon : this.path + 'images/imaxe.png',
				command : 'imaxe',
				group : 'imaxeGroup'
			});
			editor.addMenuItem( 'eliminarImaxeItem',{
				label : editor.lang.imaxe.eliminar,
				icon : this.path + 'images/eliminar.png',
				command : 'eliminarImaxe',
				group : 'imaxeGroup'
			});
			editor.contextMenu.addListener( function( element ){
				if ( element ){
					var parents = jQuery(element.$).parents('*').andSelf().filter(".imaxe");
					if (parents.size() == 0){
						element = null;
					}else{
						element = new CKEDITOR.dom.element(parents.get(0));
					}
				}
				if ( element && !element.isReadOnly() && !element.data( 'cke-realelement' ))
		 			return { imaxeItem : CKEDITOR.TRISTATE_OFF, eliminarImaxeItem: CKEDITOR.TRISTATE_OFF};
		 		return null;
			});
		}
        
		editor.on( 'selectionChange', function( evt ) {
			seleccion(editor, evt.data.path.lastElement);
		});
		editor.on('doubleclick', function(evt){
			seleccion(editor, evt.data.element);
		});
		
        CKEDITOR.dialog.add( 'imaxeDialog', this.path + 'dialogs/imaxe.js' );
    }
});