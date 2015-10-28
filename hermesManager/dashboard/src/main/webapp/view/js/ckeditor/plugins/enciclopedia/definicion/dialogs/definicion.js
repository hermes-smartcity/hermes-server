// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.dialog.html#.add
CKEDITOR.dialog
		.add(
				'definicionDialog',
				function(editor) {
					return {
						// Basic properties of the dialog window: title, minimum
						// size.
						// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.dialog.dialogDefinition.html
						title : editor.lang.definicion.titulo_dialog,
						minWidth : 400,
						minHeight : 200,
						// Dialog window contents.
						// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.dialog.definition.content.html
						contents : [ {
							// Definition of the Basic Settings dialog window
							// tab (page) with its id, label and contents.
							// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.dialog.contentDefinition.html
							id : 'tab1',
							label : editor.lang.definicion.titulo_tab,
							elements : [ {
								// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.ui.dialog.textInput.html
								type : 'textarea',
								id : 'definicion',
								label : editor.lang.definicion.label,
								validate : function() {

									CKEDITOR.instances[this.getInputElement()
											.getId()].updateElement();

									var pass = true;
									pass = CKEDITOR.dialog.validate.notEmpty(
											editor.lang.definicion.validacion)(
											this.getValue());
									if (pass !== true) {
										return pass;
									}
								},
								setup : function(element) {
									var titulo = jQuery(element.$).attr("alt");
									if (titulo && titulo.length != 0) {
										this.setValue(titulo);
									}

									var ed = CKEDITOR.instances[this
											.getInputElement().getId()];
									if (ed) {
										ed.destroy(true);
									}

									ed = CKEDITOR.replace(this
											.getInputElement().$, {
										toolbar : [
							   						{ name: 'basicstyles', items : [ 'Bold','Italic','Underline','-','RemoveFormat' ] }
							   					],
										height : 150,
										width : 400,
										removePlugins : 'elementspath',
										resize_enabled : false,
										autoParagraph : false,
										enterMode : CKEDITOR.ENTER_BR
									});

									CKEDITOR.instances[ed.name].on('blur',
											function(d) {
												d.editor.updateElement();
											});
								},
								// Function to be run when the commitContent
								// method of the parent dialog window is called.
								// Set the element's text content to the value
								// of this field.
								// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.dom.element.html#setText
								// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.dom.element.html#getValue
								commit : function(element) {
									var $element = jQuery(element.$);
									$element.attr("alt",this.getValue());
								}
							} ]
						} ],
						// This method is invoked once a dialog window is
						// loaded.
						onShow : function() {
							// Get the element selected in the editor.
							// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.editor.html#getSelection
							var sel = editor.getSelection(),
							// Assigning the element in which the selection
							// starts to a variable.
							// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.dom.selection.html#getStartElement
							element = sel.getStartElement();

							if (element) {
								var parents = jQuery(element.$).parents('*')
										.andSelf().filter(".definicion");
								if (parents.size() == 0) {
									element = null;
								} else {
									element = new CKEDITOR.dom.element(parents
											.get(0));
								}
							}
							// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.dom.document.html#createElement
							if (!element || element.data('cke-realelement')) {
								element = editor.document.createElement('span')
										.setAttribute('class', 'definicion');
								var text = sel.getSelectedText();
								if (text && text.length > 0) {
									element.appendText(sel.getSelectedText());
								} else {
									element
											.appendText(editor.lang.definicion.placeholder);
								}

								this.insertMode = true;
							} else
								this.insertMode = false;

							this.element = element;

							// Invoke the setup functions of the element.
							this.setupContent(this.element);
						},
						// This method is invoked once a user closes the dialog
						// window, accepting the changes.
						// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.dialog.dialogDefinition.html#onOk
						onOk : function() {
							// A dialog window object.
							// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.dialog.html
							var dialog = this, nuevoelemento = this.element;

							// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.editor.html#insertElement
							if (this.insertMode) {
								editor.insertElement(nuevoelemento);
							}

							// Populate the element with values entered by the
							// user (invoke commit functions).
							this.commitContent(nuevoelemento);
						}
					};
				});