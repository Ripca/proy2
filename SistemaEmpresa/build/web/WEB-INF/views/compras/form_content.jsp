<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.*" %>

<%
    Compra compra = (Compra) request.getAttribute("compra");
    List<Proveedor> proveedores = (List<Proveedor>) request.getAttribute("proveedores");
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    boolean esEdicion = compra != null;
    String titulo = esEdicion ? "Editar Compra" : "Nueva Compra";
    String action = esEdicion ? "update" : "save";
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0"><%= titulo %></h2>
        <p class="text-muted mb-0">Complete la información de la compra</p>
    </div>
    <a href="CompraServlet" class="btn btn-secondary">
        <i class="fas fa-arrow-left"></i> Volver a la Lista
    </a>
</div>

<div class="card">
    <div class="card-body">
        <form action="CompraServlet" method="post" id="formCompra">
            <input type="hidden" name="action" value="<%= action %>">
            <% if (esEdicion) { %>
                <input type="hidden" name="idCompra" value="<%= compra.getIdCompra() %>">
            <% } %>

            <!-- Información de la Compra -->
            <div class="row mb-4">
                <div class="col-md-4">
                    <label for="noFactura" class="form-label">No. Factura <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="noFactura" name="noFactura"
                           value="<%= esEdicion ? String.valueOf(compra.getNoOrdenCompra()) : "" %>" required>
                </div>
                <div class="col-md-4">
                    <label for="fecha" class="form-label">Fecha <span class="text-danger">*</span></label>
                    <input type="date" class="form-control" id="fecha" name="fecha"
                           value="<%= esEdicion && compra.getFechaOrden() != null ? compra.getFechaOrden().toString() : "" %>" required>
                </div>
                <div class="col-md-4">
                    <label for="idProveedor" class="form-label">Proveedor</label>
                    <div class="input-group mb-2">
                        <input type="text" class="form-control" id="buscarProveedor"
                               placeholder="Buscar proveedor..." onkeyup="buscarProveedores()">
                        <button type="button" class="btn btn-outline-success" onclick="abrirModalNuevoProveedor()">
                            <i class="fas fa-plus"></i>
                        </button>
                    </div>
                    <select class="form-select" id="idProveedor" name="idProveedor">
                        <option value="">Proveedor General</option>
                        <%
                            if (proveedores != null) {
                                for (Proveedor proveedor : proveedores) {
                                    boolean selected = esEdicion && compra.getIdProveedor() == proveedor.getIdProveedor();
                        %>
                        <option value="<%= proveedor.getIdProveedor() %>" <%= selected ? "selected" : "" %>>
                            <%= proveedor.getProveedor() %>
                        </option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
            </div>

            <!-- Detalle de Productos -->
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">Detalle de Productos</h5>
                    <button type="button" class="btn btn-success btn-sm" onclick="agregarProducto()">
                        <i class="fas fa-plus"></i> Agregar Producto
                    </button>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" id="tablaProductos">
                            <thead class="table-dark">
                                <tr>
                                    <th>Producto</th>
                                    <th>Cantidad</th>
                                    <th>Precio Costo</th>
                                    <th>Subtotal</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody id="detalleProductos">
                                <%
                                    if (esEdicion && compra.getDetalles() != null && !compra.getDetalles().isEmpty()) {
                                        for (CompraDetalle detalle : compra.getDetalles()) {
                                %>
                                <tr>
                                    <td>
                                        <select class="form-select producto-select" name="idProducto[]" required onchange="actualizarPrecio(this)">
                                            <option value="">Seleccionar producto...</option>
                                            <%
                                                if (productos != null) {
                                                    for (Producto producto : productos) {
                                                        boolean selectedProd = detalle.getIdProducto() == producto.getIdProducto();
                                            %>
                                            <option value="<%= producto.getIdProducto() %>" 
                                                    data-precio="<%= producto.getPrecioCosto() != null ? producto.getPrecioCosto().doubleValue() : 0.0 %>"
                                                    <%= selectedProd ? "selected" : "" %>>
                                                <%= producto.getProducto() %> - Q.<%= String.format("%.2f", producto.getPrecioCosto() != null ? producto.getPrecioCosto().doubleValue() : 0.0) %>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="number" class="form-control cantidad-input" name="cantidad[]" 
                                               value="<%= detalle.getCantidad() %>" min="1" required onchange="calcularSubtotal(this)">
                                    </td>
                                    <td>
                                        <input type="number" class="form-control precio-input" name="precio[]"
                                               value="<%= detalle.getPrecioCostoUnitario() %>" step="0.01" min="0" required onchange="calcularSubtotal(this)">
                                    </td>
                                    <td>
                                        <input type="number" class="form-control subtotal-input" readonly
                                               value="<%= detalle.getSubtotal() %>">
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-danger btn-sm" onclick="eliminarFila(this)">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </td>
                                </tr>
                                <%
                                        }
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Total -->
            <div class="row mb-4">
                <div class="col-md-8"></div>
                <div class="col-md-4">
                    <div class="card bg-light">
                        <div class="card-body">
                            <h5 class="card-title">Total de la Compra</h5>
                            <h3 class="text-warning">Q. <span id="totalCompra">0.00</span></h3>
                            <input type="hidden" name="total" id="totalInput" value="0">
                        </div>
                    </div>
                </div>
            </div>

            <!-- Botones -->
            <div class="d-flex justify-content-end gap-2">
                <a href="CompraServlet" class="btn btn-secondary">
                    <i class="fas fa-times"></i> Cancelar
                </a>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> <%= esEdicion ? "Actualizar" : "Guardar" %> Compra
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    let contadorFilas = 0;

    function agregarProducto() {
        const tbody = document.getElementById('detalleProductos');
        const nuevaFila = document.createElement('tr');

        nuevaFila.innerHTML = `
            <td>
                <div class="input-group">
                    <select class="form-select producto-select" name="idProducto[]" required onchange="actualizarPrecio(this)">
                        <option value="">Seleccionar producto...</option>
                        <%
                            if (productos != null) {
                                for (Producto producto : productos) {
                        %>
                        <option value="<%= producto.getIdProducto() %>" data-precio="<%= producto.getPrecioCosto() != null ? producto.getPrecioCosto().doubleValue() : 0.0 %>">
                            <%= producto.getProducto() %> - Q.<%= String.format("%.2f", producto.getPrecioCosto() != null ? producto.getPrecioCosto().doubleValue() : 0.0) %>
                        </option>
                        <%
                                }
                            }
                        %>
                    </select>
                    <button type="button" class="btn btn-outline-primary" onclick="abrirModalProductosCompra(this)">
                        <i class="fas fa-search"></i>
                    </button>
                </div>
            </td>
            <td>
                <input type="number" class="form-control cantidad-input" name="cantidad[]" value="1" min="1" required onchange="calcularSubtotal(this)">
            </td>
            <td>
                <input type="number" class="form-control precio-input" name="precio[]" step="0.01" min="0" required onchange="calcularSubtotal(this)">
            </td>
            <td>
                <input type="number" class="form-control subtotal-input" readonly value="0.00">
            </td>
            <td>
                <button type="button" class="btn btn-danger btn-sm" onclick="eliminarFila(this)">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;

        tbody.appendChild(nuevaFila);
        contadorFilas++;
    }

    function eliminarFila(boton) {
        const fila = boton.closest('tr');
        fila.remove();
        calcularTotal();
    }

    function actualizarPrecio(select) {
        const fila = select.closest('tr');
        const precioInput = fila.querySelector('.precio-input');
        const selectedOption = select.options[select.selectedIndex];

        if (selectedOption.value) {
            const precio = selectedOption.getAttribute('data-precio');
            precioInput.value = precio;
            calcularSubtotal(precioInput);
        } else {
            precioInput.value = '';
            calcularSubtotal(precioInput);
        }
    }

    function calcularSubtotal(input) {
        const fila = input.closest('tr');
        const cantidad = parseFloat(fila.querySelector('.cantidad-input').value) || 0;
        const precio = parseFloat(fila.querySelector('.precio-input').value) || 0;
        const subtotal = cantidad * precio;

        fila.querySelector('.subtotal-input').value = subtotal.toFixed(2);
        calcularTotal();
    }

    function calcularTotal() {
        let total = 0;
        const subtotales = document.querySelectorAll('.subtotal-input');

        subtotales.forEach(function(subtotal) {
            total += parseFloat(subtotal.value) || 0;
        });

        document.getElementById('totalCompra').textContent = total.toFixed(2);
        document.getElementById('totalInput').value = total.toFixed(2);
    }

    // Validación del formulario
    document.getElementById('formCompra').addEventListener('submit', function(e) {
        const filas = document.querySelectorAll('#detalleProductos tr');

        if (filas.length === 0) {
            e.preventDefault();
            showAlert('error', 'Error', 'Debe agregar al menos un producto a la compra.');
            return false;
        }

        let hayProductosValidos = false;
        filas.forEach(function(fila) {
            const producto = fila.querySelector('.producto-select').value;
            const cantidad = fila.querySelector('.cantidad-input').value;
            const precio = fila.querySelector('.precio-input').value;

            if (producto && cantidad && precio) {
                hayProductosValidos = true;
            }
        });

        if (!hayProductosValidos) {
            e.preventDefault();
            showAlert('error', 'Error', 'Debe completar la información de al menos un producto.');
            return false;
        }
    });

    // Calcular total inicial si hay productos
    document.addEventListener('DOMContentLoaded', function() {
        calcularTotal();

        // Si no hay productos, agregar uno por defecto
        const filas = document.querySelectorAll('#detalleProductos tr');
        if (filas.length === 0) {
            agregarProducto();
        }
    });

    // Modal para seleccionar productos en compras
    let selectProductoActualCompra = null;

    function abrirModalProductosCompra(button) {
        // Guardar referencia al select que abrió el modal
        selectProductoActualCompra = button.parentElement.querySelector('.producto-select');

        const modal = `
            <div class="modal fade" id="modalProductosCompra" tabindex="-1">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Seleccionar Producto para Compra</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <input type="text" class="form-control" id="buscarProductoModalCompra" placeholder="Buscar producto...">
                            </div>
                            <div class="table-responsive">
                                <table class="table table-hover" id="tablaProductosCompra">
                                    <thead>
                                        <tr>
                                            <th>Producto</th>
                                            <th>Marca</th>
                                            <th>Precio Costo</th>
                                            <th>Existencia</th>
                                            <th>Acción</th>
                                        </tr>
                                    </thead>
                                    <tbody id="bodyProductosCompra">
                                        <!-- Se llena dinámicamente -->
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;

        // Agregar modal al DOM si no existe
        if (!document.getElementById('modalProductosCompra')) {
            document.body.insertAdjacentHTML('beforeend', modal);
        }

        // Cargar productos
        cargarProductosModalCompra();

        // Mostrar modal
        const modalElement = new bootstrap.Modal(document.getElementById('modalProductosCompra'));
        modalElement.show();
    }

    function cargarProductosModalCompra(termino = '') {
        const tbody = document.getElementById('bodyProductosCompra');
        tbody.innerHTML = '<tr><td colspan="5" class="text-center">Cargando...</td></tr>';

        fetch(`BusquedaServlet?tipo=productos&termino=${encodeURIComponent(termino)}`)
            .then(response => response.json())
            .then(data => {
                tbody.innerHTML = '';

                if (Array.isArray(data)) {
                    data.forEach(producto => {
                        // Para compras usamos precio costo
                        const precioCosto = producto.precioCosto || producto.precio;
                        const fila = `
                            <tr>
                                <td>${producto.nombre}</td>
                                <td>${producto.marca || 'Sin marca'}</td>
                                <td>Q. ${precioCosto.toFixed(2)}</td>
                                <td>${producto.existencia}</td>
                                <td>
                                    <button type="button" class="btn btn-sm btn-primary"
                                            onclick="seleccionarProductoCompra(${producto.id}, '${producto.nombre}', ${precioCosto})">
                                        Seleccionar
                                    </button>
                                </td>
                            </tr>
                        `;
                        tbody.insertAdjacentHTML('beforeend', fila);
                    });
                } else if (data.error) {
                    tbody.innerHTML = `<tr><td colspan="5" class="text-center text-danger">${data.error}</td></tr>`;
                } else {
                    tbody.innerHTML = '<tr><td colspan="5" class="text-center">No se encontraron productos</td></tr>';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                tbody.innerHTML = '<tr><td colspan="5" class="text-center text-danger">Error al cargar productos</td></tr>';
            });
    }

    function seleccionarProductoCompra(id, nombre, precio) {
        if (selectProductoActualCompra) {
            // Limpiar opciones existentes
            selectProductoActualCompra.innerHTML = '<option value="">Seleccionar producto...</option>';

            // Agregar el producto seleccionado
            const option = document.createElement('option');
            option.value = id;
            option.textContent = `${nombre} - Q.${precio.toFixed(2)}`;
            option.setAttribute('data-precio', precio);
            option.selected = true;
            selectProductoActualCompra.appendChild(option);

            // Actualizar precio
            actualizarPrecio(selectProductoActualCompra);
        }

        // Cerrar modal
        bootstrap.Modal.getInstance(document.getElementById('modalProductosCompra')).hide();
    }

    // Búsqueda en modal de productos para compras
    document.addEventListener('input', function(e) {
        if (e.target && e.target.id === 'buscarProductoModalCompra') {
            const termino = e.target.value;
            setTimeout(() => {
                if (e.target.value === termino) {
                    cargarProductosModalCompra(termino);
                }
            }, 500);
        }
    });

    // Búsqueda de proveedores
    let timeoutProveedores;
    function buscarProveedores() {
        clearTimeout(timeoutProveedores);
        timeoutProveedores = setTimeout(() => {
            const termino = document.getElementById('buscarProveedor').value;
            const select = document.getElementById('idProveedor');

            if (termino.length < 2) {
                return;
            }

            fetch(`BusquedaServlet?tipo=proveedores&termino=${encodeURIComponent(termino)}`)
                .then(response => response.json())
                .then(proveedores => {
                    select.innerHTML = '<option value="">Seleccionar proveedor...</option>';
                    proveedores.forEach(proveedor => {
                        const option = document.createElement('option');
                        option.value = proveedor.id;
                        option.textContent = `${proveedor.nombre} - ${proveedor.nit}`;
                        select.appendChild(option);
                    });
                })
                .catch(error => {
                    console.error('Error al buscar proveedores:', error);
                });
        }, 500);
    }

    function abrirModalNuevoProveedor() {
        // Implementar modal para nuevo proveedor si es necesario
        showAlert('info', 'Información', 'Funcionalidad de nuevo proveedor pendiente de implementar.');
    }
</script>
