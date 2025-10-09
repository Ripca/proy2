<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.sistemaempresa.models.Marca"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema Empresa - <%= request.getAttribute("marca") != null ? "Editar" : "Nueva" %> Marca</title>
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
                <a class="nav-link" href="MarcaServlet">
                    <i class="fas fa-tags"></i> Marcas
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
                    Marca marca = (Marca) request.getAttribute("marca");
                    boolean esEdicion = marca != null;
                %>
                
                <!-- Header -->
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h1>
                        <i class="fas fa-tags"></i> 
                        <%= esEdicion ? "Editar Marca" : "Nueva Marca" %>
                    </h1>
                    <a href="MarcaServlet" class="btn btn-secondary">
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
                            Datos de la Marca
                        </h5>
                    </div>
                    <div class="card-body">
                        <form method="POST" action="MarcaServlet" id="marcaForm">
                            <input type="hidden" name="action" value="<%= esEdicion ? "update" : "save" %>">
                            <% if (esEdicion) { %>
                                <input type="hidden" name="idMarca" value="<%= marca.getIdMarca() %>">
                            <% } %>
                            
                            <div class="mb-3">
                                <label for="marca" class="form-label">
                                    <i class="fas fa-tag"></i> Nombre de la Marca *
                                </label>
                                <input type="text" 
                                       class="form-control" 
                                       id="marca" 
                                       name="marca" 
                                       value="<%= esEdicion ? marca.getMarca() : "" %>"
                                       required 
                                       maxlength="45"
                                       placeholder="Ingrese el nombre de la marca">
                                <div class="form-text">Máximo 45 caracteres</div>
                            </div>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="MarcaServlet" class="btn btn-secondary me-md-2">
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
        document.getElementById('marcaForm').addEventListener('submit', function(e) {
            const marca = document.getElementById('marca').value.trim();
            
            if (marca === '') {
                e.preventDefault();
                alert('Por favor ingrese el nombre de la marca');
                document.getElementById('marca').focus();
                return false;
            }
            
            if (marca.length > 45) {
                e.preventDefault();
                alert('El nombre de la marca no puede exceder 45 caracteres');
                document.getElementById('marca').focus();
                return false;
            }
        });

        // Enfocar el primer campo al cargar
        document.addEventListener('DOMContentLoaded', function() {
            document.getElementById('marca').focus();
        });
    </script>
</body>
</html>
