CKEDITOR.plugins.add( 'organismo', {
	lang: ['gl'],
    init: function( editor ) {
        
        var eliminarOrganismo = new CKEDITOR.command( editor, {
            exec: function( editor ) {
            	var selection = editor.getSelection();
                var element = selection.getStartElement();

                if ( element ){
                    element = element.getAscendant( 'span', true );
                }
                if ( element && element.getName() == 'span' ) {
                	element.remove(true);
                	var command = editor.getCommand( 'organismo' );
                	command.setState( CKEDITOR.TRISTATE_OFF );
                }
            }
        } );
        
        editor.addCommand( 'organismo', new CKEDITOR.dialogCommand( 'organismoDialog' ) );
        editor.addCommand( 'eliminarOrganismo', eliminarOrganismo );
        
        editor.ui.addButton( 'Organismo', {
            label: editor.lang.organismo.icono,
            command: 'organismo',
            toolbar: 'others',
            icon: this.path + 'images/organismo.png'
        });
        
        function seleccion(editor, element) {
			
			if ( editor.readOnly ){
				return;
			}
			
			var command = editor.getCommand( 'organismo' );
			
			if ( element ){
				var parents = jQuery(element.$).parents('*').andSelf();
				if (parents.filter(".organismo").size() > 0){
					command.setState( CKEDITOR.TRISTATE_ON );
					return;
				}
			}
			
			command.setState( CKEDITOR.TRISTATE_OFF );
		}
        
		if ( editor.contextMenu ){
			editor.addMenuGroup( 'organismoGroup' );
			editor.addMenuItem( 'organismoItem',{
				label : editor.lang.organismo.editar,
				icon : this.path + 'images/organismo.png',
				command : 'organismo',
				group : 'organismoGroup'
			});
			editor.addMenuItem( 'eliminarOrganismoItem',{
				label : editor.lang.organismo.eliminar,
				icon : this.path + 'images/eliminar.png',
				command : 'eliminarOrganismo',
				group : 'organismoGroup'
			});
			editor.contextMenu.addListener( function( element ){
				if ( element ){
					var parents = jQuery(element.$).parents('*').andSelf().filter(".organismo");
					if (parents.size() == 0){
						element = null;
					}else{
						element = new CKEDITOR.dom.element(parents.get(0));
					}
				}
				if ( element && !element.isReadOnly() && !element.data( 'cke-realelement' ))
		 			return { organismoItem : CKEDITOR.TRISTATE_OFF, eliminarOrganismoItem: CKEDITOR.TRISTATE_OFF};
		 		return null;
			});
		}
        
		editor.on( 'selectionChange', function( evt ) {
			seleccion(editor, evt.data.path.lastElement);
		});
		editor.on('doubleclick', function(evt){
			seleccion(editor, evt.data.element);
		});
		
        CKEDITOR.dialog.add( 'organismoDialog', this.path + 'dialogs/organismo.js' );
    }
});