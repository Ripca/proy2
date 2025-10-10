<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Marca" %>

<%
    List<Marca> marcas = (List<Marca>) request.getAttribute("marcas");
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0">Gestión de Marcas</h2>
        <p class="text-muted mb-0">Administra las marcas de productos</p>
    </div>
    <a href="MarcaServlet?action=new" class="btn btn-primary">
        <i class="fas fa-plus"></i> Nueva Marca
    </a>
</div>

<!-- Formulario de búsqueda -->
<div class="card mb-4">
    <div class="card-body">
        <form method="get" action="MarcaServlet" class="row g-3">
            <input type="hidden" name="action" value="search">
            <div class="col-md-8">
                <input type="text" class="form-control" name="termino" 
                       placeholder="Buscar por nombre de marca..." 
                       value="<%= request.getAttribute("termino") != null ? request.getAttribute("termino") : "" %>">
            </div>
            <div class="col-md-4">
                <button type="submit" class="btn btn-outline-primary me-2">
                    <i class="fas fa-search"></i> Buscar
                </button>
                <a href="MarcaServlet" class="btn btn-outline-secondary">
                    <i class="fas fa-times"></i> Limpiar
                </a>
            </div>
        </form>
    </div>
</div>

<!-- Tabla de marcas -->
<div class="card">
    <div class="card-header">
        <h5 class="mb-0">
            <i class="fas fa-list"></i> Lista de Marcas
            <span class="badge bg-primary ms-2">
                <%= marcas != null ? marcas.size() : 0 %> registros
            </span>
        </h5>
    </div>
    <div class="card-body">
        <%
            if (marcas != null && !marcas.isEmpty()) {
        %>
        <div class="table-responsive">
            <table class="table table-striped table-hover data-table">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Marca</th>
                        <th>Fecha Creación</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (Marca marca : marcas) {
                    %>
                    <tr>
                        <td><%= marca.getIdMarca() %></td>
                        <td>
                            <strong><%= marca.getMarca() %></strong>
                        </td>
                        <td><%= marca.getFechaIngreso() != null ? marca.getFechaIngreso().toString() : "N/A" %></td>
                        <td>
                            <div class="btn-group" role="group">
                                <a href="MarcaServlet?action=edit&id=<%= marca.getIdMarca() %>" 
                                   class="btn btn-warning btn-sm" title="Editar">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <button type="button" class="btn btn-danger btn-sm" 
                                        onclick="confirmDelete('MarcaServlet?action=delete&id=<%= marca.getIdMarca() %>', 
                                        '¿Está seguro que desea eliminar la marca <%= marca.getMarca() %>?')" 
                                        title="Eliminar">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
        <%
            } else {
        %>
        <div class="text-center py-5">
            <i class="fas fa-tags fa-3x text-muted mb-3"></i>
            <h5 class="text-muted">No hay marcas registradas</h5>
            <p class="text-muted">Haz clic en "Nueva Marca" para agregar la primera marca.</p>
            <a href="MarcaServlet?action=new" class="btn btn-primary">
                <i class="fas fa-plus"></i> Crear Primera Marca
            </a>
        </div>
        <%
            }
        %>
    </div>
</div>
