<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Puesto" %>

<%
    Puesto puesto = (Puesto) request.getAttribute("puesto");
    boolean esEdicion = puesto != null;
    String titulo = esEdicion ? "Editar Puesto" : "Nuevo Puesto";
    String action = esEdicion ? "update" : "save";
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0"><%= titulo %></h2>
        <p class="text-muted mb-0">Complete la información del puesto</p>
    </div>
    <a href="PuestoServlet" class="btn btn-secondary">
        <i class="fas fa-arrow-left"></i> Volver a la Lista
    </a>
</div>

<!-- Formulario -->
<div class="row justify-content-center">
    <div class="col-lg-6">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">
                    <i class="fas fa-briefcase"></i> Información del Puesto
                </h5>
            </div>
            <div class="card-body">
                <form action="PuestoServlet" method="post" id="formPuesto">
                    <input type="hidden" name="action" value="<%= action %>">
                    <% if (esEdicion) { %>
                        <input type="hidden" name="idPuesto" value="<%= puesto.getIdPuesto() %>">
                    <% } %>

                    <div class="mb-3">
                        <label for="puesto" class="form-label">
                            Nombre del Puesto <span class="text-danger">*</span>
                        </label>
                        <input type="text" class="form-control" id="puesto" name="puesto" 
                               value="<%= esEdicion ? puesto.getPuesto() : "" %>" 
                               required maxlength="50" placeholder="Ej: Gerente, Contador, Vendedor...">
                        <div class="form-text">Ingrese el nombre del puesto de trabajo</div>
                    </div>



                    <!-- Botones -->
                    <div class="d-flex justify-content-end gap-2 mt-4">
                        <a href="PuestoServlet" class="btn btn-secondary">
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
    document.getElementById('formPuesto').addEventListener('submit', function(e) {
        const puesto = document.getElementById('puesto').value.trim();
        
        if (!puesto) {
            e.preventDefault();
            showAlert('error', 'Error', 'El nombre del puesto es obligatorio.');
            return false;
        }
        
        if (puesto.length < 2) {
            e.preventDefault();
            showAlert('error', 'Error', 'El nombre del puesto debe tener al menos 2 caracteres.');
            return false;
        }
    });
    
    // Auto-focus en el primer campo
    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('puesto').focus();
    });
</script>
