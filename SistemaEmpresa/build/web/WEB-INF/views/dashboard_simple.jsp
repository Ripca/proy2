<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.CarruselImagen" %>

<%
    List<CarruselImagen> imagenesCarrusel = (List<CarruselImagen>) request.getAttribute("imagenesCarrusel");

    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", "Dashboard");
    request.setAttribute("pageIcon", "fas fa-tachometer-alt");
%>
<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/dashboard_content.jsp" />
</jsp:include>

