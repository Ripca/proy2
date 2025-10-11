<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Marca" %>

<%
    Marca marca = (Marca) request.getAttribute("marca");
    boolean esEdicion = marca != null;
    String titulo = esEdicion ? "Editar Marca" : "Nueva Marca";
    String action = esEdicion ? "update" : "save";
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0"><%= titulo %></h2>
        <p class="text-muted mb-0">Complete la información de la marca</p>
    </div>
    <a href="MarcaServlet" class="btn btn-secondary">
        <i class="fas fa-arrow-left"></i> Volver a la Lista
    </a>
</div>

<!-- Formulario -->
<div class="row justify-content-center">
    <div class="col-lg-6">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">
                    <i class="fas fa-tag"></i> Información de la Marca
                </h5>
            </div>
            <div class="card-body">
                <form action="MarcaServlet" method="post" id="formMarca">
                    <input type="hidden" name="action" value="<%= action %>">
                    <% if (esEdicion) { %>
                        <input type="hidden" name="idMarca" value="<%= marca.getIdMarca() %>">
                    <% } %>

                    <div class="mb-3">
                        <label for="marca" class="form-label">
                            Nombre de la Marca <span class="text-danger">*</span>
                        </label>
                        <input type="text" class="form-control" id="marca" name="marca" 
                               value="<%= esEdicion ? marca.getMarca() : "" %>" 
                               required maxlength="50" placeholder="Ej: Samsung, Apple, Nike...">
                        <div class="form-text">Ingrese el nombre de la marca</div>
                    </div>



                    <!-- Botones -->
                    <div class="d-flex justify-content-end gap-2 mt-4">
                        <a href="MarcaServlet" class="btn btn-secondary">
                            <i class="fas fa-times"></i> Cancelar
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> <%= esEdicion ? "Actualizar" : "Guardar" %>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    // Validación del formulario
    document.getElementById('formMarca').addEventListener('submit', function(e) {
        const marca = document.getElementById('marca').value.trim();
        
        if (!marca) {
            e.preventDefault();
            showAlert('error', 'Error', 'El nombre de la marca es obligatorio.');
            return false;
        }
        
        if (marca.length < 2) {
            e.preventDefault();
            showAlert('error', 'Error', 'El nombre de la marca debe tener al menos 2 caracteres.');
            return false;
        }
    });
    
    // Auto-focus en el primer campo
    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('marca').focus();
    });
</script>
