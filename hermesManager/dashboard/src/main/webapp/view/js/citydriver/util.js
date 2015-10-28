
/* Listas */

var ListModel = function(elementos) {
	this._elementos = elementos;
	this._indice = -1;
	
	this.elementoEngadido = new Evento(this);
	this.elementoActualizado = new Evento(this);
	this.elementoEliminado = new Evento(this);
	this.indiceCambiado = new Evento(this);
}

ListModel.prototype = {
	getElementos: function() {
		return [].concat(this._elementos);
	},
	addElemento: function(elemento) {
		this._elementos.push(elemento);
		this.elementoEngadido.notificar({elemento: elemento});
	},
	setElemento: function(indice, elemento) {
		this._elementos[indice] = elemento;
		this.elementoActualizado.notificar({elemento: elemento});
	},
	eliminarElemento: function(indice) {
		var elemento = this._elementos[indice];
		this._elementos.splice(indice, 1);
		this.elementoEliminado.notificar({elemento: elemento});
	},
	getIndice: function() {
		return this._indice;
	},
	setIndice: function(indice) {
		var anterior = this._indice;
		this._indice = indice;
		this.indiceCambiado.notificar({anterior: anterior});
	},
	ordenar: function(comparacion) {
		this._elementos.sort(comparacion);
	}
};

var Evento = function(orixe) {
	this._orixe = orixe;
	this._observadores = [];
};

Evento.prototype = {
	engadirObservador: function(observador) {
		this._observadores.push(observador);
	},
	notificar: function(args) {
		for (var i = 0; i < this._observadores.length; i++) {
			this._observadores[i](this._orixe, args);
		}
	}
};

var ListView = function (model, lista, inputs) {
	this._model = model;
	this._lista = lista;
	this._inputs = inputs;
	
	var _this = this;
	
	_this.actualizar();

	this._model.elementoEngadido.engadirObservador(function() {
		_this.actualizar();
	});
	
	this._model.elementoActualizado.engadirObservador(function() {
		_this.actualizar();
	});
	
	this._model.elementoEliminado.engadirObservador(function() {
		_this.actualizar();
	});
	
	this._model.indiceCambiado.engadirObservador(function(orixe, evento) {
		_this.indiceCambiado(orixe, evento);
	});
	
	this._lista.change(function (e) {
		_this._model.setIndice(e.target.selectedIndex);
	});
};

var ULView = function(model, ul, inputs) {
	this._model = model;
	this._ul = ul;
	this._inputs = inputs;
	
	var _this = this;
	
	_this.actualizar();

	this._model.elementoEngadido.engadirObservador(function() {
		_this.actualizar();
	});
	
	this._model.elementoEliminado.engadirObservador(function() {
		_this.actualizar();
	});
	
};

/* Varios */

function limpar() {
	for (var i = 0; i < arguments.length; i++) {
		$(arguments[i]).val("");
	}
}

function eliminarValidacions(seccion) {
	seccion.find(".error").hide();
}

function visible(elemento) {
	$(elemento).show();
}

function invisible(elemento) {
	$(elemento).hide();
}

// Devolve o valor dun parámetro da url pasada como param
function getParameterByName(url, param) {
    param = param.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + param + "=([^&#]*)"),
        results = regex.exec(url);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}