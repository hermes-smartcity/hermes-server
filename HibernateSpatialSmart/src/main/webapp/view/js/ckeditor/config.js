/**
 * @license Copyright (c) 2003-2014, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here.
	// For the complete reference:
	// http://docs.ckeditor.com/#!/api/CKEDITOR.config
	
	config.language = 'gl';

	config.indentOffset = 10;
	
	// Extra plugins
//	CKEDITOR.plugins.addExternal('autor', 'plugins/enciclopedia/autor/');
//	CKEDITOR.plugins.addExternal('obra', 'plugins/enciclopedia/obra/');
//	CKEDITOR.plugins.addExternal('organismo', 'plugins/enciclopedia/organismo/');
//	CKEDITOR.plugins.addExternal('publicacion', 'plugins/enciclopedia/publicacion/');
//	CKEDITOR.plugins.addExternal('evento', 'plugins/enciclopedia/evento/');
//	CKEDITOR.plugins.addExternal('movemento', 'plugins/enciclopedia/movemento/');
//	CKEDITOR.plugins.addExternal('imaxe', 'plugins/enciclopedia/imaxe/');
//	CKEDITOR.plugins.addExternal('audio', 'plugins/enciclopedia/audio/');
//	CKEDITOR.plugins.addExternal('video', 'plugins/enciclopedia/video/');
//	CKEDITOR.plugins.addExternal('definicion', 'plugins/enciclopedia/definicion/');
	config.extraPlugins = 'indent,indentlist,indentblock,panel,floatpanel,menu,contextmenu,maximize,sourcedialog,pastefromword,pastetext,specialchar,removeformat,horizontalrule,clipboard,table,blockquote,format,justify,find,liststyle';
	
	// Make dialogs simpler.
	config.removePlugins = 'image';
	config.removeDialogTabs = 'link:advanced';
	
	// Remove some buttons, provided by the standard plugins, which we don't
	// need to have in the Standard(s) toolbar.
	config.removeButtons = 'Subscript,Superscript';

	// Se the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre';
	
	// Permitir elementos span con todos sus atributos
	config.allowedContent = true;
	
	config.toolbar_editor =
		[
			{ name: 'document', items : [ 'Undo','Redo' ] },
			{ name: 'basicstyles', items : [ 'Format','Bold','Italic','Underline','Strike', '-', 'RemoveFormat' ] },
			{ name: 'paragraph', items : ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote'] },
			{ name: 'tables', items : [ 'Table','HorizontalRule','SpecialChar'] },
			{ name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord' ] },
			{ name: 'editing', items : [ 'Find','Replace'] },
			'/',
			{ name: 'links', items : [ 'Link','Unlink','Anchor' ] },
			{ name: 'others', items : ['Definicion', '-', 'Autor', 'Obra', 'Organismo', 'Publicacion', 'Evento', 'Movemento', '-', 'Imaxe', 'Audio', 'Video']},
			{ name: 'tools', items : [ 'Maximize', 'Sourcedialog' ] }
		];
};