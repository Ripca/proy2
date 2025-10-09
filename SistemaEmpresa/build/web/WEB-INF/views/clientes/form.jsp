<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.sistemaempresa.models.Cliente"%>
<%@page import="com.sistemaempresa.utils.JSTLAlternative"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema Empresa - <%= request.getAttribute("cliente") != null ? "Editar" : "Nuevo" %> Cliente</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="DashboardServlet">
                <i class="fas fa-building"></i> Sistema Empresa
            </a>
            <div class="navbar-nav ms-auto">
                <a class="nav-link" href="DashboardServlet">
                    <i class="fas fa-home"></i> Dashboard
                </a>
                <a class="nav-link" href="ClienteServlet">
                    <i class="fas fa-users"></i> Clientes
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
                <!-- Header -->
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h1>
                        <i class="fas fa-user${cliente != null ? '-edit' : '-plus'}"></i> 
                        ${cliente != null ? 'Editar' : 'Nuevo'} Cliente
                    </h1>
                    <a href="ClienteServlet" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Volver
                    </a>
                </div>

                <!-- Mensajes -->
                <c:if test="${param.error != null}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle"></i> ${param.error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Formulario -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-edit"></i> Datos del Cliente
                        </h5>
                    </div>
                    <div class="card-body">
                        <form method="post" action="ClienteServlet" novalidate>
                            <input type="hidden" name="action" value="${cliente != null ? 'update' : 'save'}">
                            <c:if test="${cliente != null}">
                                <input type="hidden" name="idCliente" value="${cliente.idCliente}">
                            </c:if>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="nombres" class="form-label">
                                        <i class="fas fa-user"></i> Nombres *
                                    </label>
                                    <input type="text" class="form-control" id="nombres" name="nombres" 
                                           value="${cliente.nombres}" required maxlength="60">
                                    <div class="invalid-feedback">
                                        Por favor ingrese los nombres.
                                    </div>
                                </div>

                                <div class="col-md-6 mb-3">
                                    <label for="apellidos" class="form-label">
                                        <i class="fas fa-user"></i> Apellidos *
                                    </label>
                                    <input type="text" class="form-control" id="apellidos" name="apellidos" 
                                           value="${cliente.apellidos}" required maxlength="60">
                                    <div class="invalid-feedback">
                                        Por favor ingrese los apellidos.
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="nit" class="form-label">
                                        <i class="fas fa-id-card"></i> NIT
                                    </label>
                                    <input type="text" class="form-control" id="nit" name="nit" 
                                           value="${cliente.nit}" maxlength="12" 
                                           placeholder="Ej: 12345678901">
                                </div>

                                <div class="col-md-6 mb-3">
                                    <label for="genero" class="form-label">
                                        <i class="fas fa-venus-mars"></i> Género *
                                    </label>
                                    <select class="form-select" id="genero" name="genero" required>
                                        <option value="">Seleccione...</option>
                                        <option value="M" ${cliente.genero ? 'selected' : ''}>Masculino</option>
                                        <option value="F" ${!cliente.genero && cliente != null ? 'selected' : ''}>Femenino</option>
                                    </select>
                                    <div class="invalid-feedback">
                                        Por favor seleccione el género.
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="telefono" class="form-label">
                                        <i class="fas fa-phone"></i> Teléfono
                                    </label>
                                    <input type="tel" class="form-control" id="telefono" name="telefono" 
                                           value="${cliente.telefono}" maxlength="25" 
                                           placeholder="Ej: 12345678">
                                </div>

                                <div class="col-md-6 mb-3">
                                    <label for="correoElectronico" class="form-label">
                                        <i class="fas fa-envelope"></i> Correo Electrónico
                                    </label>
                                    <input type="email" class="form-control" id="correoElectronico" name="correoElectronico" 
                                           value="${cliente.correoElectronico}" maxlength="45" 
                                           placeholder="ejemplo@correo.com">
                                    <div class="invalid-feedback">
                                        Por favor ingrese un correo válido.
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-12">
                                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                        <a href="ClienteServlet" class="btn btn-secondary me-md-2">
                                            <i class="fas fa-times"></i> Cancelar
                                        </a>
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-save"></i> ${cliente != null ? 'Actualizar' : 'Guardar'}
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <c:if test="${cliente != null}">
                    <div class="card mt-3">
                        <div class="card-header">
                            <h6 class="mb-0">
                                <i class="fas fa-info-circle"></i> Información Adicional
                            </h6>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <strong>ID Cliente:</strong> ${cliente.idCliente}
                                </div>
                                <div class="col-md-6">
                                    <strong>Fecha de Registro:</strong> 
                                    <fmt:formatDate value="${cliente.fechaIngreso}" pattern="dd/MM/yyyy HH:mm"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validación del formulario
        (function() {
            'use strict';
            window.addEventListener('load', function() {
                var forms = document.getElementsByClassName('needs-validation');
                var validation = Array.prototype.filter.call(forms, function(form) {
                    form.addEventListener('submit', function(event) {
                        if (form.checkValidity() === false) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        form.classList.add('was-validated');
                    }, false);
                });
            }, false);
        })();

        // Validación de correo electrónico
        document.getElementById('correoElectronico').addEventListener('input', function() {
            var email = this.value;
            var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            
            if (email && !emailRegex.test(email)) {
                this.setCustomValidity('Por favor ingrese un correo válido');
            } else {
                this.setCustomValidity('');
            }
        });
    </script>
</body>
</html>
