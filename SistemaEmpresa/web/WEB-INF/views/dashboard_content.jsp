<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.CarruselImagen" %>

<%
    List<CarruselImagen> imagenesCarrusel = (List<CarruselImagen>) request.getAttribute("imagenesCarrusel");
%>

<!-- Resumen de Estadísticas -->
<div class="row mb-4">
    <div class="col-xl-3 col-md-6 mb-4">
        <div class="card border-left-primary shadow h-100 py-2 card-stats">
            <div class="card-body">
                <div class="row no-gutters align-items-center">
                    <div class="col mr-2">
                        <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                            Clientes
                        </div>
                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                            <%= request.getAttribute("totalClientes") != null ? request.getAttribute("totalClientes") : 0 %>
                        </div>
                    </div>
                    <div class="col-auto">
                        <i class="fas fa-users fa-2x text-gray-300"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="col-xl-3 col-md-6 mb-4">
        <div class="card border-left-success shadow h-100 py-2 card-stats">
            <div class="card-body">
                <div class="row no-gutters align-items-center">
                    <div class="col mr-2">
                        <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                            Productos
                        </div>
                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                            <%= request.getAttribute("totalProductos") != null ? request.getAttribute("totalProductos") : 0 %>
                        </div>
                    </div>
                    <div class="col-auto">
                        <i class="fas fa-box fa-2x text-gray-300"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="col-xl-3 col-md-6 mb-4">
        <div class="card border-left-info shadow h-100 py-2 card-stats">
            <div class="card-body">
                <div class="row no-gutters align-items-center">
                    <div class="col mr-2">
                        <div class="text-xs font-weight-bold text-info text-uppercase mb-1">
                            Empleados
                        </div>
                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                            <%= request.getAttribute("totalEmpleados") != null ? request.getAttribute("totalEmpleados") : 0 %>
                        </div>
                    </div>
                    <div class="col-auto">
                        <i class="fas fa-user-tie fa-2x text-gray-300"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="col-xl-3 col-md-6 mb-4">
        <div class="card border-left-warning shadow h-100 py-2 card-stats">
            <div class="card-body">
                <div class="row no-gutters align-items-center">
                    <div class="col mr-2">
                        <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                            Proveedores
                        </div>
                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                            <%= request.getAttribute("totalProveedores") != null ? request.getAttribute("totalProveedores") : 0 %>
                        </div>
                    </div>
                    <div class="col-auto">
                        <i class="fas fa-truck fa-2x text-gray-300"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>



<!-- Carrusel de Imágenes -->
<%
    if (imagenesCarrusel != null && !imagenesCarrusel.isEmpty()) {
%>
<div class="row">
    <div class="col-12">
        <div class="card shadow">
            <div class="card-header py-3">
                <h6 class="m-0 font-weight-bold text-primary">
                    <i class="fas fa-images"></i> Galería
                </h6>
            </div>
            <div class="card-body p-0">
                <div id="carouselDashboard" class="carousel slide">
                    <div class="carousel-indicators">
                        <%
                            for (int i = 0; i < imagenesCarrusel.size(); i++) {
                        %>
                        <button type="button" data-bs-target="#carouselDashboard" data-bs-slide-to="<%= i %>" 
                                <%= i == 0 ? "class=\"active\"" : "" %>></button>
                        <%
                            }
                        %>
                    </div>
                    <div class="carousel-inner">
                        <%
                            for (int i = 0; i < imagenesCarrusel.size(); i++) {
                                CarruselImagen imagen = imagenesCarrusel.get(i);
                        %>
                        <div class="carousel-item <%= i == 0 ? "active" : "" %>">
                            <img src="<%= imagen.getUrlImagen() %>" class="d-block w-100" alt="<%= imagen.getTitulo() %>" style="height: 400px; object-fit: cover;">
                            <div class="carousel-caption d-none d-md-block">
                                <h5><%= imagen.getTitulo() %></h5>
                                <% if (imagen.tieneDescripcion()) { %>
                                    <p><%= imagen.getDescripcion() %></p>
                                <% } %>
                            </div>
                        </div>
                        <%
                            }
                        %>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#carouselDashboard" data-bs-slide="prev">
                        <span class="carousel-control-prev-icon"></span>
                        <span class="visually-hidden">Anterior</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#carouselDashboard" data-bs-slide="next">
                        <span class="carousel-control-next-icon"></span>
                        <span class="visually-hidden">Siguiente</span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<%
    }
%>

<style>
    .border-left-primary {
        border-left: 0.25rem solid #4e73df !important;
    }
    
    .border-left-success {
        border-left: 0.25rem solid #1cc88a !important;
    }
    
    .border-left-info {
        border-left: 0.25rem solid #36b9cc !important;
    }
    
    .border-left-warning {
        border-left: 0.25rem solid #f6c23e !important;
    }
    
    .card-stats {
        transition: transform 0.2s ease-in-out;
    }
    
    .card-stats:hover {
        transform: translateY(-5px);
    }
    
    .text-gray-300 {
        color: #dddfeb !important;
    }
    
    .text-gray-800 {
        color: #5a5c69 !important;
    }
</style>

<script>
    // Carousel manual (sin auto-play para evitar animaciones automáticas)
    document.addEventListener('DOMContentLoaded', function() {
        var carousel = document.getElementById('carouselDashboard');
        if (carousel) {
            var bsCarousel = new bootstrap.Carousel(carousel, {
                interval: false, // Desactivar auto-play
                wrap: true
            });
        }
    });
</script>
