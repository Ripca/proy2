<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.MenuItem" %>
<%@ page import="java.util.List" %>

<%
    List<MenuItem> menuItems = (List<MenuItem>) request.getAttribute("menuItems");
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", "Menú Dinámico - Prueba");
    request.setAttribute("pageIcon", "fas fa-sitemap");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/menu/menu_test_content.jsp" />
</jsp:include>
