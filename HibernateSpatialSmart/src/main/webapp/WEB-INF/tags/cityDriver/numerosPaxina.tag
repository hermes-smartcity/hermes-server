<%@ attribute name="paxinable" required="true" type="java.lang.Object"%>
<%@ attribute name="formId" required="false" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%-- A sección cos  números de páxina soamente se pintan se hai máis de unha páxina --%>

<c:url var="previousImg" value="/view/imaxes/admin/previous.png" />
<c:url var="nextImg" value="/view/imaxes/admin/next.png" />
	<c:url var="urlPaxinaBase" value="">
		<c:forEach var="parametro" items="${param}">
			<c:if test="${parametro.key != 'paxina'}">
				<c:param name="${parametro.key}" value="${parametro.value}"/>
			</c:if>
		</c:forEach>
	</c:url>
	<c:choose>
	<c:when test="${paxinable.paxinasTotais gt 1}">
		<div class="numerosPaxina">
			<span style="display: inline-block; float: left; width: 55px;"><fmt:message key="comun.paxinas"/>:&nbsp;</span>
						<!--  primera pagina 
						<c:if test="${paxinable.paxina gt 1}">						
							<c:url var="urlPaxina" value="${urlPaxinaBase}">
								<c:param name="paxina" value="1"/>
							</c:url>
							<c:set var="urlPaxina" value="${urlPaxina}"/>
							<span class="paxina"><a href="${fn:escapeXml(urlPaxina)}"><c:out value="1"/></a></span>							
						</c:if>		-->				
						<!-- pagina  anterior  -->
			<span class="bloqueAnteriorPaxinaSeguinte">
						<c:choose>
						<c:when test="${paxinable.paxina gt 2 or paxinable.paxina eq 2}">

								
								<c:url var="urlPaxina" value="${urlPaxinaBase}">
									<c:param name="paxina" value="${paxinable.paxina-1}"/>
								</c:url>
								<c:set var="urlPaxina" value="${urlPaxina}"/>
								<span class="paxina imaxePrevious"><a href="${fn:escapeXml(urlPaxina)}"><img src="${previousImg}"/></a></span>
						</c:when>
						<c:otherwise>
								<span class="paxina paxinaIconoVacio"> &#160; </span>
														
						</c:otherwise>
						</c:choose>
						<!-- pagina  actual -->
						<div class="selectPaxina">
						<select id="selectPaxinacion">	
							<c:forEach var="i" begin="1" end="${paxinable.paxinasTotais}">
								<c:choose>
								<c:when test="${i eq paxinable.paxina}">
								<option value="${i}" selected="selected">${i}</option>
								</c:when>
								<c:otherwise>
								<option value="${i}">${i}</option>
								</c:otherwise>
								</c:choose>
							</c:forEach>	
						</select>
							</div>
						<!-- pagina  actual 										
						<span class="paxinaActual"><c:out value="${paxinable.paxina}"/></span>
						 -->	
						<!-- pagina  seguinte  -->
						<c:choose>
						<c:when test="${paxinable.paxina lt paxinable.paxinasTotais}">
								<c:url var="urlPaxina" value="${urlPaxinaBase}">
									<c:param name="paxina" value="${paxinable.paxina+1}"/>
								</c:url>
								<c:set var="urlPaxina" value="${urlPaxina}"/>
								<span class="paxina imaxeNext"><a href="${fn:escapeXml(urlPaxina)}"><img src="${nextImg}"/></a></span>
						
						</c:when>
						<c:otherwise>
								<span class="paxina paxinaIconoVacio"> &#160; </span>
														
						</c:otherwise>
						</c:choose>
				</span>
						<!--  ultima pagina 
						<c:if test="${paxinable.paxina ne paxinable.paxinasTotais}">						
							<c:url var="urlPaxina" value="${urlPaxinaBase}">
								<c:param name="paxina" value="${paxinable.paxinasTotais}"/>
							</c:url>
							<c:set var="urlPaxina" value="${urlPaxina}"/>
							<span class="paxina"><a href="${fn:escapeXml(urlPaxina)}"><c:out value="${paxinable.paxinasTotais}"/></a></span>								
						</c:if>-->
		</div>
	</c:when>
	<c:otherwise>
		<div class="numerosPaxina">&nbsp;</div>
	</c:otherwise>
	</c:choose>
	
	<c:url var="urlPaxinaBaseSelect" value="${urlPaxinaBase}"/>


	<script type="text/javascript">
	var urlBaseParams = '${urlPaxinaBaseSelect}';
	$("#selectPaxinacion").change(function () {
		if(urlBaseParams.length>1)
			location.href = urlBaseParams+"&paxina="+ $(this).val();
		else location.href = urlBaseParams+"?paxina="+ $(this).val();
	});
	</script>
	
