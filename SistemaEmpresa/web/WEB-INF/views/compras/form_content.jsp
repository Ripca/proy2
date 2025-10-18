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

<!-- MODAL PROVEEDORES -->
<div class="modal fade effect-scale" id="modalProveedores">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content modal-content-demo">
            <div class="modal-header">
                <h4 class="modal-title"><i class="fas fa-truck"></i>&nbsp;Consultas de proveedores</h4>
                <button type="button" aria-label="Close" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body text-start">
                <table id="grvProveedores" class="table table-bordered table-striped table-condensed table-sm">
                    <thead class="table-head">
                        <tr>
                            <th>Código</th>
                            <th>NIT</th>
                            <th>Proveedor</th>
                            <th>Teléfono</th>
                            <th>Correo</th>
                            <th>Dirección</th>
                            <th>&nbsp;</th>
                        </tr>
                    </thead>
                </table>
            </div>
            <div class="modal-footer d-flex justify-content-end">
                <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cerrar</button>
            </div>
        </div>
    </div>
</div>

<!-- MODAL PRODUCTOS -->
<div class="modal fade effect-scale" id="modalProductos">
    <div class="modal-dialog modal-fullscreen" role="document">
        <div class="modal-content modal-content-demo">
            <div class="modal-header">
                <h4 class="modal-title">Productos</h4>
                <button type="button" aria-label="Close" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body text-start">
                <table id="grvProductosConsulta" class="table table-bordered table-hover table-striped table-condensed table-sm">
                    <thead class="table-head">
                        <tr>
                            <th>Código</th>
                            <th>Producto</th>
                            <th>Precio costo</th>
                            <th>Marca</th>
                            <th>Modelo</th>
                            <th>Año</th>
                            <th>Descripción</th>
                            <th>Existencia</th>
                            <th>En canasta</th>
                            <th>&nbsp;</th>
                        </tr>
                    </thead>
                </table>
            </div>
            <div class="modal-footer d-flex justify-content-end">
                <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cerrar</button>
            </div>
        </div>
    </div>
</div>

<!-- HEADER -->
<div class="page-header">
    <div class="add-item d-flex">
        <div class="page-title">
            <h4><i class="fas fa-shopping-bag me-2"></i><%= titulo %></h4>
            <h6><%= titulo %></h6>
        </div>
    </div>
    <div class="page-btn">
        <a href="CompraServlet" class="btn btn-added color">
            <i class="fas fa-inbox me-2"></i>Listado de compras
        </a>
    </div>
</div>

<!-- FACTURACION -->
<div class="row compras">
    <div class="col-md-12">
        <div class="card">
            <div class="card-body">
                <form action="CompraServlet" method="post" id="formCompra">
                    <input type="hidden" name="action" value="<%= action %>">
                    <input type="hidden" name="idProveedor" id="hiddenIdProveedor" value="">
                    <% if (esEdicion) { %>
                        <input type="hidden" name="idCompra" value="<%= compra.getIdCompra() %>">
                    <% } %>

                    <div class="row">
                        <!-- SECCION DE PROVEEDOR -->
                        <div class="col-md-8">
                            <div class="form-horizontal">
                                <!-- NIT PROVEEDOR -->
                                <div class="mb-1 row">
                                    <div class="add-newplus">
                                        <span id="lblIdProveedor" hidden=""></span>
                                        <label for="txtNitProveedor" class="form-label">Proveedor:</label>
                                        <a href="#!" id="btnBuscarProveedor">
                                            <i class="fas fa-search plus-down-add"></i>
                                            <span>Buscar</span>
                                        </a>
                                    </div>
                                    <div class="col-12">
                                        <div class="row g-0">
                                            <div class="col-4 input-h-control">
                                                <div class="input-group input-group-sm">
                                                    <span class="input-group-text">
                                                        <i class="fas fa-truck"></i>
                                                    </span>
                                                    <input id="txtNitProveedor" name="txtNitProveedor" type="text" aria-label="NIT" placeholder="NIT" class="form-control no-right-border">
                                                </div>
                                            </div>
                                            <div class="col-8">
                                                <input id="txtNombreProveedor" type="text" aria-label="Nombre completo" placeholder="Nombre completo" class="form-control no-left-border input-h-control" disabled="disabled">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- SECCION DE COMPRA -->
                        <div class="col-md-4">
                            <div class="form-horizontal">
                                <!-- NUMERO DE ORDEN -->
                                <div class="mb-1 row">
                                    <div class="col-sm-12">
                                        <label class="form-label">No. Orden:</label>
                                        <div class="input-group input-group-sm">
                                            <span class="input-group-text">
                                                <i class="fas fa-file-invoice"></i>
                                            </span>
                                            <input id="txtNoOrden" name="noOrden" class="form-control" type="text"
                                                   value="<%= esEdicion ? String.valueOf(compra.getNoOrdenCompra()) : "" %>" required>
                                        </div>
                                    </div>
                                </div>

                                <!-- FECHA -->
                                <div class="mb-1 row">
                                    <div class="col-sm-12">
                                        <label class="form-label">Fecha:</label>
                                        <div class="input-group input-group-sm">
                                            <span class="input-group-text">
                                                <i class="fas fa-calendar"></i>
                                            </span>
                                            <input id="txtFecha" name="fecha" class="form-control" type="date"
                                                   value="<%= esEdicion && compra.getFechaOrden() != null ? compra.getFechaOrden().toString() : "" %>" required>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <hr />

                    <div class="row">
                        <span class="card-title" style="float: left; width: auto !important;">
                            <i class="fas fa-box"></i>&nbsp;Conceptos
                        </span>

                        <!-- CODIGO DE BARRA -->
                        <div class="col-lg-4 col-sm-12 ms-auto">
                            <div class="add-newplus">
                                <label class="form-label" for="txtCodigoBarraProducto">&nbsp;</label>
                                <a href="#!" id="btnBuscarProducto">
                                    <i class="fas fa-search plus-down-add"></i>
                                    <span>Consultar</span>
                                </a>
                            </div>
                            <div class="input-blocks">
                                <div class="input-groupicon select-code">
                                    <input id="txtCodigoBarraProducto" name="txtCodigoBarraProducto" class="barcode-search form-control" type="text" placeholder="Código de producto" style="padding: 10px;">
                                    <div class="addonset">
                                        <img src="assets/img/barcode-scanner.gif" alt="img" style="height: 38px;">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- PRODUCTOS COMPRA -->
                    <div class="col-md-12">
                        <div class="table-responsive">
                            <table id="grvProductosCompra" class="table table-striped table-bordered table-sm">
                                <thead class="table-head">
                                    <tr>
                                        <th>Id Producto</th>
                                        <th>Código</th>
                                        <th>Artículo</th>
                                        <th>Cantidad</th>
                                        <th>P. costo</th>
                                        <th>Descuento</th>
                                        <th width="200">Subtotal</th>
                                        <th>&nbsp;</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12 col-md-6 col-lg-4 ms-md-auto mt-2">
                            <div class="total-order w-100 max-widthauto m-auto mb-1">
                                <ul>
                                    <li>
                                        <h4>SubTotal</h4>
                                        <h5><span class="lbl-info-moneda">Q.</span>&nbsp;<span class="lbl-info-subtotal">0.00</span></h5>
                                    </li>
                                    <li>
                                        <h4>Descuento</h4>
                                        <h5><span class="lbl-info-moneda">Q.</span>&nbsp;<span class="lbl-info-descuento">0.00</span></h5>
                                    </li>
                                    <li class="total">
                                        <h4>Total a pagar</h4>
                                        <h4><span class="lbl-info-moneda">Q.</span>&nbsp;<span class="lbl-info-total">0.00</span></h4>
                                    </li>
                                </ul>
                            </div>

                            <div class="btn-row d-sm-flex align-items-center justify-content-between mb-4">
                                <button id="btnGuardarCompra" type="submit" class="btn btn-success btn-icon flex-fill">
                                    <span class="me-1 d-flex align-items-center">
                                        <i class="fas fa-check"></i>
                                    </span>
                                    Guardar
                                </button>
                                <button id="btnCancelarCompra" type="button" class="btn btn-danger btn-icon flex-fill">
                                    <span class="me-1 d-flex align-items-center">
                                        <i class="fas fa-trash-alt"></i>
                                    </span>
                                    Cancelar
                                </button>
                            </div>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var g_info_compra = {
        id_referencia: null,
        id_moneda: 1,
        moneda: 'Q.'
    };

    function fnAgregarProductoCompra(p_producto, p_cantidad = 1) {
        const table = $('#grvProductosCompra').DataTable();
        const info_producto = fnBuscarProductoCompra(p_producto.id_producto);
        const producto = info_producto.producto;
        const stock_actual = p_producto.stock_actual ?? p_producto.existencia;
        let _stock_insuficiente = false;

        if (producto) {
            if (info_producto.index !== -1) {
                const descuento = producto.precio_costo < producto.precio_unitario
                    ? producto.precio_unitario - producto.precio_costo
                    : 0;
                producto.cantidad += p_cantidad;
                producto.precio_total = producto.cantidad * producto.precio_costo;
                producto.descuento = producto.cantidad * descuento;

                table.row(info_producto.index).data(producto).draw();
            }
        } else {
            table.row.add({
                id_producto: p_producto.id_producto,
                id_moneda: p_producto.id_moneda || 1,
                codigo: p_producto.codigo,
                nombre: p_producto.nombre || p_producto.producto,
                cantidad: p_cantidad,
                moneda: p_producto.moneda || 'Q.',
                precio_costo: p_producto.precio_costo || p_producto.precio_unitario,
                precio_total: p_cantidad * (p_producto.precio_costo || p_producto.precio_unitario),
                descripcion: p_producto.descripcion,
                categoria: p_producto.categoria,
                estado: p_producto.estado,
                existencia: stock_actual,
                precio_unitario: p_producto.precio_costo || p_producto.precio_unitario,
                precio_minimo: p_producto.min_descuento || 0,
                descuento: 0,
                img_producto: p_producto.img_producto
            }).draw();
        }

        fnUpdateTotales();

        setTimeout(function () {
            table.columns.adjust().draw();
        }, 50);
    }

    function fnBuscarProductoCompra(p_id_producto, p_tipo_busqueda = "id") {
        const table = $('#grvProductosCompra').DataTable().rows().data().toArray();
        const index = table.findIndex(row => {
            if (p_tipo_busqueda === "id") {
                return row.id_producto === p_id_producto;
            } else if (p_tipo_busqueda === "codigo") {
                return row.codigo === p_id_producto;
            }
        });

        return { producto: index !== -1 ? table[index] : null, index };
    }

    function fnResumenCompra() {
        const table = $('#grvProductosCompra').DataTable();
        let moneda = 'Q.',
            id_moneda = 1,
            mto_subtotal = 0,
            mto_recargo = 0,
            mto_descuento = 0,
            mto_total = 0;
        let productos = [];

        table.rows().every(function () {
            const data = this.data();

            mto_subtotal += parseFloat(data.precio_total) || 0;
            mto_recargo += parseFloat(data.recargo || 0) || 0;
            mto_descuento += parseFloat(data.descuento || 0) || 0;

            if (!moneda) {
                moneda = data.moneda;
                id_moneda = data.id_moneda;
            }
            productos.push(data);
        });

        mto_total = mto_subtotal - mto_descuento + mto_recargo;

        return {
            moneda,
            mto_subtotal,
            mto_recargo,
            mto_descuento,
            mto_total,
            id_moneda,
            productos
        };
    }

    function fnUpdateTotales() {
        let data = fnResumenCompra();

        $(".lbl-info-moneda").html(`${data.moneda}`);
        $(".lbl-info-subtotal").html(data.mto_subtotal.toFixed(2));
        $(".lbl-info-recargo").html(data.mto_recargo.toFixed(2));
        $(".lbl-info-descuento").html(data.mto_descuento.toFixed(2));
        $(".lbl-info-total").html(data.mto_total.toFixed(2));

        g_info_compra.id_moneda = data.id_moneda;
        g_info_compra.moneda = data.moneda;
    }

    function fnValidaCodigoBarra() {
        let codigo_producto = $("#txtCodigoBarraProducto").val().trim();
        let cantidad = 1;

        if (codigo_producto.includes('*')) {
            let partes = codigo_producto.split('*');
            if (partes.length === 2 && !isNaN(partes[0]) && !isNaN(partes[1])) {
                cantidad = parseInt(partes[0]);
                codigo_producto = partes[1];
            }
        }

        if (codigo_producto) {
            let info_producto = fnBuscarProductoCompra(codigo_producto, "codigo");
            let producto = info_producto.producto;

            if (!producto) {
                // Buscar producto por código en el servidor
                $.ajax({
                    url: 'ProductoServlet',
                    type: 'GET',
                    data: { action: 'buscarPorCodigo', codigo: codigo_producto },
                    dataType: 'json',
                    success: function(result) {
                        if (result && result.id_producto) {
                            fnAgregarProductoCompra(result, cantidad);
                        } else {
                            alert('Producto no encontrado');
                        }
                    },
                    error: function() {
                        alert('Error al buscar el producto');
                    }
                });
            } else {
                fnAgregarProductoCompra(producto, cantidad);
            }
            $("#txtCodigoBarraProducto").val("");
        }
    }

    function fnActualizarDatosCantidad(row, data, cantidad) {
        let descuento = 0;
        if (data.precio_costo < data.precio_unitario) {
            descuento = data.precio_unitario - data.precio_costo;
        }

        data.cantidad = cantidad;
        data.precio_total = data.cantidad * data.precio_costo;
        data.descuento = data.cantidad * descuento;

        $(row).find(".tbl_lbl_precio_total").html((data.moneda + ' ' + data.precio_total.toFixed(2)));
        $(row).find(".tbl_lbl_descuento").html((data.moneda + ' ' + data.descuento.toFixed(2)));

        fnUpdateTotales();
    }

    function fnLimpiarCamposProveedor() {
        $("#lblIdProveedor").html("");
        $("#txtNitProveedor").val("");
        $("#txtNombreProveedor").val("");
    }

    function fnLimpiarCamposCompra() {
        $("#txtCodigoBarraProducto").val("");
        $('#grvProductosCompra').dataTable().fnClearTable();

        fnLimpiarCamposProveedor();
        fnUpdateTotales();
    }

    function seleccionarProveedor(data, enableFields = false) {
        fnLimpiarCamposProveedor();

        $("#lblIdProveedor").html(data.idProveedor || data.id_proveedor);
        $("#hiddenIdProveedor").val(data.idProveedor || data.id_proveedor);
        $("#txtNitProveedor").val(data.nit);
        $("#txtNombreProveedor").val(data.proveedor);

        $("#txtNombreProveedor").attr("disabled", !enableFields);
    }

    function fnBuscarProveedor(pNumeroNIT) {
        fnLimpiarCamposProveedor();

        if (pNumeroNIT != "") {
            pNumeroNIT = pNumeroNIT.toUpperCase();

            $.ajax({
                url: 'ProveedorServlet',
                type: 'GET',
                data: { action: 'buscarPorNit', nit: pNumeroNIT },
                dataType: 'json',
                success: function(result) {
                    if (result && result.id_proveedor) {
                        seleccionarProveedor(result);
                        $("#txtCodigoBarraProducto").focus();
                    } else {
                        if (confirm('Proveedor no encontrado. ¿Desea crear uno nuevo?')) {
                            // Abrir modal de nuevo proveedor
                            $("#modalProveedores").modal("show");
                        }
                    }
                },
                error: function() {
                    alert('Error al buscar el proveedor');
                }
            });
        } else {
            alert("Por favor ingrese el número de NIT del proveedor");
        }
    }

    // Inicialización de DataTable para productos de compra
    $(document).ready(function() {
        $('#grvProductosCompra').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.10.24/i18n/Spanish.json"
            },
            "pageLength": 10,
            "lengthChange": false,
            "searching": false,
            "ordering": false,
            "info": false,
            "paging": false,
            "columns": [
                { "data": "id_producto", "visible": false },
                { "data": "codigo" },
                {
                    "data": "nombre",
                    "render": function(data, type, row) {
                        return `<div class="productimgname">
                                    <a href="javascript:void(0);" class="product-img stock-img">
                                        <img src="${row.img_producto || 'assets/img/product/noimage.png'}" alt="product">
                                    </a>
                                    <a href="javascript:void(0);">${data}</a>
                                </div>`;
                    }
                },
              {
    "data": "cantidad",
    "render": function (data, type, row, meta) {
        return `
            <div class="product-quantity">
                <span class="quantity-btn" onclick="fnCambiarCantidad(${meta.row}, -1)">-</span>
                <input type="text" class="qty" value="${data}" readonly>
                <span class="quantity-btn" onclick="fnCambiarCantidad(${meta.row}, 1)">+</span>
            </div>
        `;
    }
},

                {
                    "data": "precio_costo",
                    "render": function(data, type, row) {
                        return `<span class="tbl_lbl_precio_costo">${row.moneda} ${data.toFixed(2)}</span>`;
                    }
                },
                {
                    "data": "descuento",
                    "render": function(data, type, row) {
                        return `<span class="tbl_lbl_descuento">${row.moneda} ${data.toFixed(2)}</span>`;
                    }
                },
                {
                    "data": "precio_total",
                    "render": function(data, type, row) {
                        return `<span class="tbl_lbl_precio_total">${row.moneda} ${data.toFixed(2)}</span>`;
                    }
                },
                {
                    "data": null,
                    "render": function(data, type, row) {
                        return `<a class="delete-set" href="javascript:void(0);" onclick="$('#grvProductosCompra').DataTable().row($(this).closest('tr')).remove().draw(); fnUpdateTotales();">
                                    <img src="assets/img/icons/delete.svg" alt="svg">
                                </a>`;
                    }
                }
            ],
            "rowCallback": function(row, data) {
                $(row).attr('data-id-producto', data.id_producto);
            }
        });

        // Event handlers
        $("#txtCodigoBarraProducto").on("keypress", function(e) {
            if (e.which === 13) {
                fnValidaCodigoBarra();
            }
        });

        $("#btnBuscarProducto").on("click", function() {
            $("#modalProductos").modal("show");
        });

        $("#btnBuscarProveedor").on("click", function() {
            $("#modalProveedores").modal("show");
        });

        $("#txtNitProveedor").on("keypress", function(e) {
            if (e.which === 13) {
                fnBuscarProveedor($(this).val());
            }
        });

        $("#btnCancelarCompra").on("click", function() {
            if (confirm("¿Está seguro de cancelar la compra?")) {
                fnLimpiarCamposCompra();
            }
        });

        // Evento submit del formulario
        $("#formCompra").on("submit", function(e) {
            e.preventDefault();

            // Validar que haya proveedor
            if (!$("#hiddenIdProveedor").val()) {
                alert("Debe seleccionar un proveedor");
                return false;
            }

            // Validar que haya productos
            const table = $('#grvProductosCompra').DataTable();
            if (table.rows().count() === 0) {
                alert("Debe agregar al menos un producto");
                return false;
            }

            // Serializar datos de la tabla
            const productos = [];
            table.rows().every(function() {
                const data = this.data();
                productos.push({
                    idProducto: data.id_producto,
                    cantidad: data.cantidad,
                    precioCostoUnitario: data.precio_costo
                });
            });

            // Agregar campos ocultos con los datos de productos
            productos.forEach((prod, index) => {
                $("<input>").attr({
                    type: "hidden",
                    name: "idProducto",
                    value: prod.idProducto
                }).appendTo("#formCompra");

                $("<input>").attr({
                    type: "hidden",
                    name: "cantidad",
                    value: prod.cantidad
                }).appendTo("#formCompra");

                $("<input>").attr({
                    type: "hidden",
                    name: "precioCostoUnitario",
                    value: prod.precioCostoUnitario
                }).appendTo("#formCompra");
            });

            // Enviar formulario
            this.submit();
        });

        // Inicializar totales
        fnUpdateTotales();
    });
</script>
