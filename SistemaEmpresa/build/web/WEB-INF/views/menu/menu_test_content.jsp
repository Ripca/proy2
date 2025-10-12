<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.MenuItem" %>
<%@ page import="java.util.List" %>

<%
    List<MenuItem> menuItems = (List<MenuItem>) request.getAttribute("menuItems");
%>

<div class="container-fluid">
    <div class="row">
        <div class="col-12">
            <div class="page-header">
                <h1><i class="fas fa-sitemap"></i> Menú Dinámico - Estructura de Árbol</h1>
                <p class="text-muted">Visualización de la estructura jerárquica del menú dinámico</p>
            </div>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="mb-0">
                                <i class="fas fa-tree"></i> Estructura del Menú
                            </h5>
                        </div>
                        <div class="card-body">
                            <%
                                if (menuItems != null && !menuItems.isEmpty()) {
                            %>
                                <div class="menu-tree">
                                    <%
                                        for (MenuItem item : menuItems) {
                                    %>
                                        <div class="tree-item level-1">
                                            <i class="<%= item.getIcono() %>"></i>
                                            <strong><%= item.getTitulo() %></strong>
                                            <% if (item.getUrl() != null && !item.getUrl().trim().isEmpty()) { %>
                                                <small class="text-muted">(<%= item.getUrl() %>)</small>
                                            <% } %>
                                            
                                            <% if (item.tieneHijos()) { %>
                                                <div class="tree-children">
                                                    <%
                                                        for (MenuItem hijo : item.getHijos()) {
                                                    %>
                                                        <div class="tree-item level-2">
                                                            <i class="<%= hijo.getIcono() %>"></i>
                                                            <%= hijo.getTitulo() %>
                                                            <% if (hijo.getUrl() != null && !hijo.getUrl().trim().isEmpty()) { %>
                                                                <small class="text-muted">(<%= hijo.getUrl() %>)</small>
                                                            <% } %>
                                                            
                                                            <% if (hijo.tieneHijos()) { %>
                                                                <div class="tree-children">
                                                                    <%
                                                                        for (MenuItem nieto : hijo.getHijos()) {
                                                                    %>
                                                                        <div class="tree-item level-3">
                                                                            <i class="<%= nieto.getIcono() %>"></i>
                                                                            <%= nieto.getTitulo() %>
                                                                            <% if (nieto.getUrl() != null && !nieto.getUrl().trim().isEmpty()) { %>
                                                                                <small class="text-muted">(<%= nieto.getUrl() %>)</small>
                                                                            <% } %>
                                                                        </div>
                                                                    <%
                                                                        }
                                                                    %>
                                                                </div>
                                                            <% } %>
                                                        </div>
                                                    <%
                                                        }
                                                    %>
                                                </div>
                                            <% } %>
                                        </div>
                                    <%
                                        }
                                    %>
                                </div>
                            <%
                                } else {
                            %>
                                <div class="alert alert-warning">
                                    <i class="fas fa-exclamation-triangle"></i>
                                    No se encontraron elementos del menú en la base de datos.
                                </div>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="mb-0">
                                <i class="fas fa-info-circle"></i> Información del Sistema
                            </h5>
                        </div>
                        <div class="card-body">
                            <h6>Características del Menú Dinámico:</h6>
                            <ul class="list-unstyled">
                                <li><i class="fas fa-check text-success"></i> Estructura jerárquica de 3 niveles</li>
                                <li><i class="fas fa-check text-success"></i> Almacenado en base de datos</li>
                                <li><i class="fas fa-check text-success"></i> Ordenamiento automático</li>
                                <li><i class="fas fa-check text-success"></i> Iconos personalizables</li>
                                <li><i class="fas fa-check text-success"></i> URLs dinámicas</li>
                                <li><i class="fas fa-check text-success"></i> Estado activo/inactivo</li>
                            </ul>
                            
                            <hr>
                            
                            <h6>Estructura Implementada:</h6>
                            <div class="structure-example">
                                <div class="text-primary">1. Productos</div>
                                <div class="ms-3 text-secondary">1.1. Marcas</div>
                                <div class="ms-3 text-secondary">1.2. Gestión de Productos</div>
                                
                                <div class="text-primary mt-2">2. Ventas</div>
                                <div class="ms-3 text-secondary">2.1. Clientes</div>
                                <div class="ms-3 text-secondary">2.2. Empleados</div>
                                <div class="ms-5 text-muted">2.2.1. Puestos</div>
                                <div class="ms-5 text-muted">2.2.2. Gestión de Empleados</div>
                                <div class="ms-3 text-secondary">2.3. Gestión de Ventas</div>
                                
                                <div class="text-primary mt-2">3. Compras</div>
                                <div class="ms-3 text-secondary">3.1. Proveedores</div>
                                <div class="ms-3 text-secondary">3.2. Gestión de Compras</div>
                                
                                <div class="text-primary mt-2">4. Reportes</div>
                                <div class="ms-3 text-secondary">4.1. Reporte de Ventas</div>
                                <div class="ms-3 text-secondary">4.2. Reporte de Compras</div>
                                <div class="ms-3 text-secondary">4.3. Reporte de Inventario</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
.menu-tree {
    font-family: 'Courier New', monospace;
    font-size: 0.9rem;
}

.tree-item {
    margin: 0.25rem 0;
    padding: 0.25rem;
    border-radius: 0.25rem;
}

.tree-item.level-1 {
    background-color: #e3f2fd;
    border-left: 4px solid #2196f3;
    font-weight: bold;
}

.tree-item.level-2 {
    background-color: #f3e5f5;
    border-left: 4px solid #9c27b0;
    margin-left: 1rem;
}

.tree-item.level-3 {
    background-color: #e8f5e8;
    border-left: 4px solid #4caf50;
    margin-left: 2rem;
}

.tree-children {
    margin-top: 0.5rem;
}

.structure-example {
    font-family: 'Courier New', monospace;
    font-size: 0.85rem;
    background-color: #f8f9fa;
    padding: 1rem;
    border-radius: 0.375rem;
    border: 1px solid #dee2e6;
}
</style>
