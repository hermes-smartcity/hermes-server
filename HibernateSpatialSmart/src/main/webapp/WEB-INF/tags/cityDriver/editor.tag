<%@ attribute name="columnas" required="false"%>
<%@ attribute name="filas" required="false"%>
<%@ attribute name="alto" required="false"%>
<%@ attribute name="ancho" required="false"%>
<%@ attribute name="toolbar" required="false"%>
<%@ attribute name="disabletoolbar" type="java.lang.Boolean" required="false"%>
<%@ attribute name="campo" required="true"%>
<%@ attribute name="campoId" required="true"%>
<%@ attribute name="etiqueta" required="true"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<c:if test="${empty columnas}">
	<c:set var="columnas" value="200"/>
</c:if>

<c:if test="${empty filas}">
	<c:set var="filas" value="20"/>
</c:if>

<c:if test="${empty alto}">
	<c:set var="alto" value="400"/>
</c:if>

<c:if test="${empty ancho}">
	<c:set var="ancho" value="930"/>
</c:if>

<c:if test="${empty toolbar}">
	<c:set var="toolbar" value="editor"/>
</c:if>

<c:if test="${empty disabletoolbar}">
	<c:set var="disabletoolbar" value="false"/>
</c:if>

<form:label path="${campo}">
	<fmt:message key="${etiqueta}" />
</form:label> 
<form:textarea cols="${columnas}" rows="${filas}" id="${campoId}" path="${campo}"/>

<script type="text/javascript">

if(typeof $ !== 'undefined'){
	$(document).ready(function(){

			var editorTextarea = $("#${campoId}");
	
			$("form").submit(function(){
				var value = CKEDITOR.instances.${campoId}.getData();
				editorTextarea.html(value);
				return true;
			});
					
			CKEDITOR.config.baseHref = "${pageContext.request.contextPath}/";
			
			CKEDITOR.replace( "${campoId}", 
				{
		        height: ${alto},
				width: ${ancho},
				toolbar: '${toolbar}',
				toolbarCanCollapse : !${disabletoolbar}
		        }
			);
			
			<c:if test="${fn:contains(clase, 'campoErroneo')}">
			CKEDITOR.instances.${campoId}.on("instanceReady", function(event){
				CKEDITOR.instances.${campoId}.container.addClass('campoErroneo');
			});
			</c:if>
			
	});	
}
</script>			