CKEDITOR.plugins.add( 'definicion',
{
	lang: ['gl'],
	init: function( editor )
	{
		var iconPath = this.path + 'images/definicion.png';

		var eliminarDefinicion = new CKEDITOR.command( editor, {
            exec: function( editor ) {
            	var selection = editor.getSelection();
                var element = selection.getStartElement();

                if ( element ){
                    element = element.getAscendant( 'span', true );
                }
                if ( element && element.getName() == 'span' && element.hasClass('definicion') ) {
                	element.remove(true);
                	var command = editor.getCommand( 'definicionDialog' );
                	command.setState( CKEDITOR.TRISTATE_OFF );
                }
            }
        } );
		
		editor.addCommand( 'definicionDialog',new CKEDITOR.dialogCommand( 'definicionDialog' ) );
		editor.addCommand( 'eliminarDefinicion', eliminarDefinicion );
		
		editor.ui.addButton( 'Definicion',
		{
			label: editor.lang.definicion.add_button,
			command: 'definicionDialog',
			icon: iconPath
		} );
		
		// Add context menu support.
		if ( editor.contextMenu )
		{
			editor.addMenuGroup( 'definicionGroup' );
			editor.addMenuItem( 'definicionItem',
			{
				label : editor.lang.definicion.edit_button,
				icon : iconPath,
				command : 'definicionDialog',
				group : 'definicionGroup'
			});
			editor.addMenuItem( 'eliminarDefinicionItem',{
				label : editor.lang.definicion.eliminar,
				icon : this.path + 'images/eliminar.png',
				command : 'eliminarDefinicion',
				group : 'definicionGroup'
			});
			editor.contextMenu.addListener( function( element )
			{
				// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.dom.node.html#getAscendant
				if ( element ){
					var parents = jQuery(element.$).parents('*').andSelf().filter(".definicion");
					if (parents.size() == 0){
						element = null;
					}else{
						element = new CKEDITOR.dom.element(parents.get(0));
					}
				}

				// Return a context menu object in an enabled, but not active state.
				// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.html#.TRISTATE_OFF
				if ( element && !element.isReadOnly() && !element.data( 'cke-realelement' ))
		 			return { definicionItem : CKEDITOR.TRISTATE_OFF, eliminarDefinicionItem: CKEDITOR.TRISTATE_OFF };
				// Return nothing if the conditions are not met.
		 		return null;
			});
		}
		
		function seleccion(editor, element) {
			
			if ( editor.readOnly ){
				return;
			}

			/*
			 * Despite our initial hope, document.queryCommandEnabled() does not work
			 * for this in Firefox. So we must detect the state by element paths.
			 */
			var command = editor.getCommand( 'definicionDialog' );
			
			if ( element ){
				var parents = jQuery(element.$).parents('*').andSelf();
				if (parents.filter(".definicion").size() > 0){
					command.setState( CKEDITOR.TRISTATE_ON );
					return;
				}
			}
			
			command.setState( CKEDITOR.TRISTATE_OFF );
		}
		
		editor.on( 'selectionChange', function( evt ) {
			seleccion(editor, evt.data.path.lastElement);
		});
		editor.on('doubleclick', function(evt){
			seleccion(editor, evt.data.element);
		});
		CKEDITOR.dialog.add( 'definicionDialog', this.path + 'dialogs/definicion.js' );
	}
} );
