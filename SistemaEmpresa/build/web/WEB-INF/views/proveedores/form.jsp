<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.sistemaempresa.models.Proveedor"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema Empresa - <%= request.getAttribute("proveedor") != null ? "Editar" : "Nuevo" %> Proveedor</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <!-- Navegación -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="DashboardServlet">
                <i class="fas fa-building"></i> Sistema Empresa
            </a>
            <div class="navbar-nav ms-auto">
                <a class="nav-link" href="DashboardServlet">
                    <i class="fas fa-home"></i> Dashboard
                </a>
                <a class="nav-link" href="ProveedorServlet">
                    <i class="fas fa-truck"></i> Proveedores
                </a>
                <a class="nav-link" href="LoginServlet?action=logout">
                    <i class="fas fa-sign-out-alt"></i> Cerrar Sesión
                </a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <%
                    Proveedor proveedor = (Proveedor) request.getAttribute("proveedor");
                    boolean esEdicion = proveedor != null;
                %>
                
                <!-- Header -->
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h1>
                        <i class="fas fa-truck"></i> 
                        <%= esEdicion ? "Editar Proveedor" : "Nuevo Proveedor" %>
                    </h1>
                    <a href="ProveedorServlet" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Volver
                    </a>
                </div>

                <!-- Mensajes -->
                <% if (request.getParameter("error") != null) { %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle"></i> <%= request.getParameter("error") %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                <% } %>

                <!-- Formulario -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-<%= esEdicion ? "edit" : "plus" %>"></i>
                            Datos del Proveedor
                        </h5>
                    </div>
                    <div class="card-body">
                        <form method="POST" action="ProveedorServlet" id="proveedorForm">
                            <input type="hidden" name="action" value="<%= esEdicion ? "update" : "save" %>">
                            <% if (esEdicion) { %>
                                <input type="hidden" name="idProveedor" value="<%= proveedor.getIdProveedor() %>">
                            <% } %>
                            
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="proveedor" class="form-label">
                                            <i class="fas fa-building"></i> Nombre del Proveedor *
                                        </label>
                                        <input type="text" 
                                               class="form-control" 
                                               id="proveedor" 
                                               name="proveedor" 
                                               value="<%= esEdicion ? proveedor.getProveedor() : "" %>"
                                               required 
                                               maxlength="60"
                                               placeholder="Ingrese el nombre del proveedor">
                                    </div>
                                </div>
                                
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="nit" class="form-label">
                                            <i class="fas fa-id-card"></i> NIT *
                                        </label>
                                        <input type="text" 
                                               class="form-control" 
                                               id="nit" 
                                               name="nit" 
                                               value="<%= esEdicion ? proveedor.getNit() : "" %>"
                                               required 
                                               maxlength="13"
                                               placeholder="Ingrese el NIT">
                                    </div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="direccion" class="form-label">
                                    <i class="fas fa-map-marker-alt"></i> Dirección
                                </label>
                                <textarea class="form-control" 
                                          id="direccion" 
                                          name="direccion" 
                                          rows="3"
                                          maxlength="100"
                                          placeholder="Dirección del proveedor"><%= esEdicion && proveedor.getDireccion() != null ? proveedor.getDireccion() : "" %></textarea>
                            </div>

                            <div class="mb-3">
                                <label for="telefono" class="form-label">
                                    <i class="fas fa-phone"></i> Teléfono
                                </label>
                                <input type="tel" 
                                       class="form-control" 
                                       id="telefono" 
                                       name="telefono" 
                                       value="<%= esEdicion && proveedor.getTelefono() != null ? proveedor.getTelefono() : "" %>"
                                       maxlength="25"
                                       placeholder="Número de teléfono">
                            </div>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="ProveedorServlet" class="btn btn-secondary me-md-2">
                                    <i class="fas fa-times"></i> Cancelar
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save"></i> 
                                    <%= esEdicion ? "Actualizar" : "Guardar" %>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validación del formulario
        document.getElementById('proveedorForm').addEventListener('submit', function(e) {
            const proveedor = document.getElementById('proveedor').value.trim();
            const nit = document.getElementById('nit').value.trim();
            
            if (proveedor === '') {
                e.preventDefault();
                alert('Por favor ingrese el nombre del proveedor');
                document.getElementById('proveedor').focus();
                return false;
            }
            
            if (nit === '') {
                e.preventDefault();
                alert('Por favor ingrese el NIT');
                document.getElementById('nit').focus();
                return false;
            }
            
            // Validación básica de NIT (solo números y guiones)
            if (!/^[0-9\-]+$/.test(nit)) {
                e.preventDefault();
                alert('El NIT solo puede contener números y guiones');
                document.getElementById('nit').focus();
                return false;
            }
        });

        // Enfocar el primer campo al cargar
        document.addEventListener('DOMContentLoaded', function() {
            document.getElementById('proveedor').focus();
        });
    </script>
</body>
</html>
