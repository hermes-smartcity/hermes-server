<%@ attribute name="paxinable" required="true" type="java.lang.Object"%>
<%@ attribute name="posicion" required="false"%>
<%@ attribute name="amosarNumeroResultados" required="false" type="java.lang.Boolean"%>
<%@ attribute name="formId" required="false" %>

<%-- url: url a invocar, sen facer c:url --%>
<%-- posición indica a posición onde se situa pode tomar os valores arriba, abaixo, ambas. Por defecto abaixo --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.enxenio.es/tags/cityDriver" prefix="cityDriver"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<c:if test="${!empty paxinable.elementos}">
	<c:if test="${empty amosarNumeroResultados}">
		<c:set var="amosarNumeroResultados" value="false"/>
	</c:if>
	
	<c:if test="${amosarNumeroResultados or paxinable.paxinasTotais gt 1}">
		<c:if test="${empty posicion or (posicion != 'arriba' and posicion != 'abaixo' and posicion != 'ambos') }">
			<c:set var="posicion" value="ambos"/>
		</c:if>
			
		<c:if test="${posicion eq 'arriba' or posicion eq 'ambos'}">			
		<div class="paxinacion">
			<c:if test="${amosarNumeroResultados}">
				<div class="numeroResultados">
					<c:if test="${!empty paxinable.elementos}">
						<fmt:message key="comun.paxinable.titulo">
							<fmt:param>${paxinable.indiceInicial + 1}</fmt:param>
							<fmt:param>${paxinable.indiceInicial + fn:length(paxinable.elementos)}</fmt:param>
							<fmt:param>${paxinable.numeroElementos}</fmt:param>
						</fmt:message>
					</c:if>
				</div>
			</c:if>
			
			<c:if test="${posicion eq 'arriba' or posicion eq 'ambos'}">
				<cityDriver:numerosPaxina paxinable="${paxinable}" formId="${formId}"/>
			</c:if>
		</div>
		</c:if>
	</c:if>
</c:if>

<jsp:doBody/>

<c:if test="${!empty paxinable.elementos}">
	<c:if test="${amosarNumeroResultados or paxinable.paxinasTotais gt 1}">
		<c:if test="${posicion eq 'abaixo' or posicion eq 'ambos'}">
			<div class="paxinacion">
				<cityDriver:numerosPaxina paxinable="${paxinable}" formId="${formId}"/>
			</div>
		</c:if>
	</c:if>
</c:if>
