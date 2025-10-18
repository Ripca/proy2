<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.*" %>

<%
    Venta venta = (Venta) request.getAttribute("venta");
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    boolean esEdicion = venta != null;
    String titulo = esEdicion ? "Editar Venta" : "Nueva Venta";
    String action = esEdicion ? "update" : "save";
%>

<!-- MODAL CLIENTES -->
<div class="modal fade effect-scale" id="modalClientes">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content modal-content-demo">
            <div class="modal-header">
                <h4 class="modal-title"><i class="fas fa-user"></i>&nbsp;Consultas de clientes</h4>
                <button type="button" aria-label="Close" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body text-start">
                <table id="grvClientes" class="table table-bordered table-striped table-condensed table-sm">
                    <thead class="table-head">
                        <tr>
                            <th>Código</th>
                            <th>NIT</th>
                            <th>Cliente</th>
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
                            <th>Precio unitario</th>
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

<!-- MODAL MEDIOS DE PAGO -->
<div class="modal fade effect-slide-in-right" id="modalMedioPago">
    <div class="modal-dialog modal-dialog-centered text-center modal-lg" role="document">
        <div class="modal-content modal-content-demo">
            <div class="modal-header">
                <h4 class="modal-title">Medios de pago</h4>
                <button type="button" aria-label="Close" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body text-start">
                <div class="table-responsive mb-3">
                    <table id="tbl_medio_pago" class="table-sm">
                        <thead style="display: none;">
                            <tr>
                                <th>&nbsp;</th>
                                <th>Monto</th>
                                <th>Referencia</th>
                                <th>Autorización</th>
                                <th>Banco</th>
                                <th>Fecha</th>
                                <th>&nbsp;</th>
                            </tr>
                        </thead>
                        <tfoot>
                            <tr>
                                <td colspan="2">
                                    <div class="input-group input-group-sm">
                                        <span class="input-group-text"><b>Total</b></span>
                                        <span class="input-group-text lbl-info-moneda"></span>
                                        <input id="lbl_info_saldo_ingresado" class="form-control amount total" disabled="disabled" type="number" value="" />
                                    </div>
                                </td>
                                <td colspan="2">
                                    <div class="input-group input-group-sm">
                                        <span class="input-group-text"><b>Total a pagar</b></span>
                                        <span class="input-group-text lbl-info-moneda"></span>
                                        <input id="lbl_info_saldo_pagar" class="form-control amount total" disabled="disabled" type="number" value="" />
                                    </div>
                                </td>
                                <td colspan="2">
                                    <div class="input-group input-group-sm">
                                        <span class="input-group-text"><b>Pendiente</b></span>
                                        <span class="input-group-text lbl-info-moneda"></span>
                                        <input id="lbl_info_saldo_pendiente" class="form-control amount" disabled="disabled" type="number" placeholder="0.00" value="0.00" />
                                        <span class="input-group-text" id="span_info_saldo_pendiente" style="min-width: 42px; min-height: 34px;">
                                            <i class="fas fa-exclamation-triangle text-warning"></i>
                                        </span>
                                    </div>
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
                <div id="alerta-saldo-a-favor" class="alert alert-warning d-flex align-items-center d-none mb-1" role="alert">
                    <span><i class="fas fa-exclamation-circle me-2"></i></span>
                    <div>
                        El monto ingresado excede el total a pagar. El saldo restante de <strong id="lbl_info_saldo_a_favor"></strong>&nbsp;quedará a favor para futuras compras.
                    </div>
                </div>
                <div class="alert alert-info d-flex align-items-center" role="alert">
                    <span><i class="fas fa-bullhorn me-2"></i></span>
                    <div>
                        ¡Próximamente! Cuentas por cobrar y financiamiento estarán disponibles.
                    </div>
                </div>
            </div>
            <div class="modal-footer d-flex justify-content-end">
                <button id="btnGuardarMediosPago" type="button" class="btn btn-primary">Guardar</button>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
            </div>
        </div>
    </div>
</div>

<!-- HEADER -->
<div class="page-header">
    <div class="add-item d-flex">
        <div class="page-title">
            <h4><i class="fas fa-shopping-cart me-2"></i><%= titulo %></h4>
            <h6><%= titulo %></h6>
        </div>
    </div>
    <div class="page-btn">
        <a href="VentaServlet" class="btn btn-added color">
            <i class="fas fa-inbox me-2"></i>Listado de ventas
        </a>
    </div>
</div>

<!-- FACTURACION -->
<div class="row ventas">
    <div class="col-md-12">
        <div class="card">
            <div class="card-body">
                <form action="VentaServlet" method="post" id="formVenta">
                    <input type="hidden" name="action" value="<%= action %>">
                    <input type="hidden" name="idCliente" id="hiddenIdCliente" value="">
                    <% if (esEdicion) { %>
                        <input type="hidden" name="idVenta" value="<%= venta.getIdVenta() %>">
                    <% } %>

                    <div class="row">
                        <!-- SECCION DE CLIENTE -->
                        <div class="col-md-8">
                            <div class="form-horizontal">
                                <!-- NIT CLIENTE -->
                                <div class="mb-1 row">
                                    <div class="add-newplus">
                                        <span id="lblIdCliente" hidden=""></span>
                                        <label for="txtNitCliente" class="form-label">Cliente:</label>
                                        <a href="#!" id="btnBuscarCliente">
                                            <i class="fas fa-search plus-down-add"></i>
                                            <span>Buscar</span>
                                        </a>
                                    </div>
                                    <div class="col-12">
                                        <div class="row g-0">
                                            <div class="col-4 input-h-control">
                                                <div class="input-group input-group-sm">
                                                    <span class="input-group-text">
                                                        <i class="fas fa-user"></i>
                                                    </span>
                                                    <input id="txtNitCliente" name="txtNitCliente" type="text" aria-label="NIT" placeholder="NIT" class="form-control no-right-border">
                                                </div>
                                            </div>
                                            <div class="col-8">
                                                <input id="txtNombreCliente" type="text" aria-label="Nombre completo" placeholder="Nombre completo" class="form-control no-left-border input-h-control" disabled="disabled">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- SECCION DE FACTURA -->
                        <div class="col-md-4">
                            <div class="form-horizontal">
                                <!-- NUMERO DE FACTURA -->
                                <div class="mb-1 row">
                                    <div class="col-sm-12">
                                        <label class="form-label">No. Factura:</label>
                                        <div class="input-group input-group-sm">
                                            <span class="input-group-text">
                                                <i class="fas fa-file-invoice"></i>
                                            </span>
                                            <input id="txtNoFactura" name="noFactura" class="form-control" type="text"
                                                   value="<%= esEdicion ? venta.getNoFactura() : "" %>" required>
                                        </div>
                                    </div>
                                </div>

                                <!-- SERIE -->
                                <div class="mb-1 row">
                                    <div class="col-sm-12">
                                        <label class="form-label">Serie:</label>
                                        <div class="input-group input-group-sm">
                                            <span class="input-group-text">
                                                <i class="fas fa-barcode"></i>
                                            </span>
                                            <input id="txtSerie" name="serie" class="form-control" type="text"
                                                   value="<%= esEdicion ? venta.getSerie() : "" %>" required>
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
                                                   value="<%= esEdicion && venta.getFechaFactura() != null ? venta.getFechaFactura().toString() : "" %>" required>
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

                    <!-- PRODUCTOS VENTA -->
                    <div class="col-md-12">
                        <div class="table-responsive">
                            <table id="grvProductosCompra" class="table table-striped table-bordered table-sm">
                                <thead class="table-head">
                                    <tr>
                                        <th>Id Producto</th>
                                        <th>Código</th>
                                        <th>Artículo</th>
                                        <th>Cantidad</th>
                                        <th>P. venta</th>
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

                            <div class="d-grid btn-block mt-1 mb-2">
                                <a href="#!"><b>Medio de pago:</b></a>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="rb_forma_pago" id="rb_mp_efectivo" checked="">
                                    <label class="form-check-label" for="rb_mp_efectivo">
                                        Efectivo
                                    </label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="rb_forma_pago" id="rb_mp_otros" data-modal="1">
                                    <label class="form-check-label" for="rb_mp_otros">
                                        Otros
                                    </label>
                                </div>
                                <div id="div-progress-medio-pago" class="progress progress-xs">
                                    <div id="progress-medio-pago" class="progress-bar bg-primary" role="progressbar" style="width: 52%" aria-valuenow="52" aria-valuemin="0" aria-valuemax="100"></div>
                                </div>
                                <a id="btnMedioPago" class="btn btn-secondary mt-2">Medios de pagos</a>
                            </div>

                            <div class="btn-row d-sm-flex align-items-center justify-content-between mb-4">
                                <button id="btnEmitirVenta" type="submit" class="btn btn-success btn-icon flex-fill">
                                    <span class="me-1 d-flex align-items-center">
                                        <i class="fas fa-check"></i>
                                    </span>
                                    Emitir
                                </button>
                                <button id="btnGuardarVenta" type="button" class="btn btn-info btn-icon flex-fill">
                                    <span class="me-1 d-flex align-items-center">
                                        <i class="fas fa-save"></i>
                                    </span>
                                    Guardar Cotización
                                </button>
                                <button id="btnCancelarVenta" type="button" class="btn btn-danger btn-icon flex-fill">
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
    var tbl_medio_pago,
        g_info_factura = {
            id_referencia: null,
            medios_pago: [],
            id_moneda: 1,
            moneda: 'Q.'
        };

    function getIconAndClass(diferencia) {
        let iconName, colorClass;
        if (diferencia === 0) {
            iconName = 'check-circle';
            colorClass = 'text-success';
        } else if (diferencia > 0) {
            iconName = 'exclamation-triangle';
            colorClass = 'text-warning';
        } else {
            iconName = 'exclamation-circle';
            colorClass = 'text-danger';
        }
        return { iconName, colorClass };
    }

    function fnAgregarProductoCompra(p_producto, p_cantidad = 1) {
        const table = $('#grvProductosCompra').DataTable();
        const info_producto = fnBuscarProductoCompra(p_producto.id_producto);
        const producto = info_producto.producto;
        const stock_actual = p_producto.stock_actual ?? p_producto.existencia;
        let _stock_insuficiente = false;

        if (producto) {
            if (stock_actual >= (producto.cantidad + p_cantidad)) {
                if (info_producto.index !== -1) {
                    const descuento = producto.precio_venta < producto.precio_unitario
                        ? producto.precio_unitario - producto.precio_venta
                        : 0;
                    producto.cantidad += p_cantidad;
                    producto.precio_total = producto.cantidad * producto.precio_venta;
                    producto.descuento = producto.cantidad * descuento;

                    table.row(info_producto.index).data(producto).draw();
                }
            } else {
                _stock_insuficiente = true;
            }
        } else {
            if (p_cantidad <= stock_actual) {
                table.row.add({
                    id_producto: p_producto.id_producto,
                    id_moneda: p_producto.id_moneda || 1,
                    codigo: p_producto.codigo,
                    nombre: p_producto.nombre || p_producto.producto,
                    cantidad: p_cantidad,
                    moneda: p_producto.moneda || 'Q.',
                    precio_venta: p_producto.precio_unitario || p_producto.precio_venta,
                    precio_total: p_cantidad * (p_producto.precio_unitario || p_producto.precio_venta),
                    descripcion: p_producto.descripcion,
                    categoria: p_producto.categoria,
                    estado: p_producto.estado,
                    existencia: stock_actual,
                    precio_unitario: p_producto.precio_unitario || p_producto.precio_venta,
                    precio_minimo: p_producto.min_descuento || 0,
                    descuento: 0,
                    img_producto: p_producto.img_producto
                }).draw();
            } else {
                _stock_insuficiente = true;
            }
        }

        if (_stock_insuficiente) {
            alert('No hay suficiente stock para agregar el producto.');
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

        g_info_factura.id_moneda = data.id_moneda;
        g_info_factura.moneda = data.moneda;

        const totalMediosPago = g_info_factura.medios_pago.reduce((sum, medio) => sum + medio.monto, 0);
        validaPorcentajeMedioPago(totalMediosPago, data.mto_total);
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
        if (data.precio_venta < data.precio_unitario) {
            descuento = data.precio_unitario - data.precio_venta;
        }

        data.cantidad = cantidad;
        data.precio_total = data.cantidad * data.precio_venta;
        data.descuento = data.cantidad * descuento;

        $(row).find(".tbl_lbl_precio_total").html((data.moneda + ' ' + data.precio_total.toFixed(2)));
        $(row).find(".tbl_lbl_descuento").html((data.moneda + ' ' + data.descuento.toFixed(2)));

        fnUpdateTotales();
    }

    function fnLimpiarCamposCliente() {
        $("#lblIdCliente").html("");
        $("#txtNitCliente").val("");
        $("#txtNombreCliente").val("");
    }

    function fnLimpiarCamposFacturacion() {
        $("#txtCodigoBarraProducto").val("");
        $('#grvProductosCompra').dataTable().fnClearTable();
        $("#rb_mp_efectivo").prop('checked', true);

        fnLimpiarCamposCliente();
        fnUpdateTotales();
    }

    function seleccionarCliente(data, enableFields = false) {
        fnLimpiarCamposCliente();

        $("#lblIdCliente").html(data.idCliente || data.id_cliente);
        $("#hiddenIdCliente").val(data.idCliente || data.id_cliente);
        $("#txtNitCliente").val(data.nit || data.NIT);
        $("#txtNombreCliente").val([data.nombres, data.apellidos].filter(Boolean).join(' '));

        $("#txtNombreCliente").attr("disabled", !enableFields);
    }

    function fnBuscarCliente(pNumeroNIT) {
        fnLimpiarCamposCliente();

        if (pNumeroNIT != "") {
            pNumeroNIT = pNumeroNIT.toUpperCase();

            if (pNumeroNIT != "C/F" && pNumeroNIT != "CF") {
                $.ajax({
                    url: 'ClienteServlet',
                    type: 'GET',
                    data: { action: 'buscarPorNit', nit: pNumeroNIT },
                    dataType: 'json',
                    success: function(result) {
                        if (result && result.id_cliente) {
                            seleccionarCliente(result);
                            $("#txtCodigoBarraProducto").focus();
                        } else {
                            if (confirm('Cliente no encontrado. ¿Desea crear uno nuevo?')) {
                                // Abrir modal de nuevo cliente
                                $("#modalClientes").modal("show");
                            }
                        }
                    },
                    error: function() {
                        alert('Error al buscar el cliente');
                    }
                });
            } else {
                seleccionarCliente({
                    id_cliente: -1,
                    nit: "C/F",
                    nombres: 'CONSUMIDOR',
                    apellidos: 'FINAL',
                    direccion: 'Ciudad'
                });

                $("#txtCodigoBarraProducto").focus();
            }
        } else {
            alert("Por favor ingrese el número de NIT del cliente");
        }
    }

    function validaPorcentajeMedioPago(saldo_ingresado, saldo_a_pagar) {
        let porcentaje_progreso = Math.max(0, (saldo_ingresado / saldo_a_pagar) * 100);
        porcentaje_progreso = porcentaje_progreso.toFixed(2);

        $("#progress-medio-pago").attr({
            "style": `width: ${porcentaje_progreso}%`,
            "aria-valuenow": porcentaje_progreso
        });
    }

    function toggleMedioPago() {
        const isEfectivoChecked = $("#rb_mp_efectivo").is(":checked");
        let status_medios = $("#rb_mp_otros").data("modal");

        $("#btnMedioPago").toggle(!isEfectivoChecked);

        if (!isEfectivoChecked) {
            if (status_medios === "1") {
                $("#btnMedioPago").click();
            }
            $("#rb_mp_otros").data("modal", "1");
            $("#div-progress-medio-pago").show();
            sumSubTotalesMedioPago();
        } else {
            g_info_factura.medios_pago = [{
                "id_medio_pago": "1",
                "codigo": "EFE",
                "descripcion": "Efectivo",
                "monto": 0
            }];

            $("#div-progress-medio-pago").hide();
            $("#progress-medio-pago").attr({
                "style": "width: 100%",
                "aria-valuenow": "100"
            });
        }
    }

    function sumSubTotalesMedioPago() {
        const info_producto = fnResumenCompra();
        const data_medios_pago = tbl_medio_pago ? tbl_medio_pago.rows().data() : [];

        let saldo_ingresado = 0;
        const saldo_a_pagar = info_producto.mto_total;
        let saldo_pendiente = 0;
        let diferencia_pago = 0;

        if (data_medios_pago.length) {
            data_medios_pago.each(row => {
                saldo_ingresado += parseFloat(row.monto) || 0;
            });
        }

        saldo_pendiente = saldo_a_pagar - saldo_ingresado;
        diferencia_pago = saldo_ingresado - saldo_a_pagar;

        const { iconName, colorClass } = getIconAndClass(diferencia_pago);

        $("#span_info_saldo_pendiente").html(`<i class="fas fa-${iconName} ${colorClass}"></i>`);

        if (saldo_ingresado > saldo_a_pagar) {
            $("#alerta-saldo-a-favor").removeClass("d-none");
            $("#lbl_info_saldo_a_favor").html(`${info_producto.moneda} ${(saldo_ingresado - saldo_a_pagar).toFixed(2)}`);
        } else {
            $("#alerta-saldo-a-favor").addClass("d-none");
            $("#lbl_info_saldo_a_favor").html("");
        }

        $("#lbl_info_saldo_ingresado").val(saldo_ingresado.toFixed(2));
        $("#lbl_info_saldo_pendiente").val(saldo_pendiente.toFixed(2));

        validaPorcentajeMedioPago(saldo_ingresado, saldo_a_pagar);
    }

    $(document).ready(function () {
        // Inicializar DataTable para productos
        $("#grvProductosCompra").DataTable({
            "language": {
                "emptyTable": "No hay datos disponibles en la tabla",
                "zeroRecords": "No se encontraron registros coincidentes",
                "loadingRecords": "Cargando...",
                "processing": "Procesando...",
                "search": ' ',
                "searchPlaceholder": "Search",
                "info": " ",
                "paginate": {
                    next: ' <i class="fa fa-angle-right"></i>',
                    previous: '<i class="fa fa-angle-left"></i> '
                },
            },
            searching: false,
            ordering: false,
            info: false,
            paging: false,
            columnDefs: [
                {
                    targets: [0],
                    visible: false
                },
                {
                    targets: [1, 3],
                    className: 'text-center'
                },
                {
                    targets: [2],
                    className: 'productimgname'
                },
                {
                    targets: [3, 4, 5, 6],
                    className: 'ammounts'
                },
                {
                    targets: [7],
                    className: 'text-center options-tables'
                }
            ],
            columns: [
                { data: "id_producto" },
                { data: "codigo" },
                {
                    data: function (item) {
                        let img_producto = item.img_producto || 'assets/img/products/icon.png';
                        return `<div class="view-product me-2">
                                    <a href="#!">
                                        <img src="${img_producto}" alt="" style="width: 40px; height: 40px;">
                                    </a>
                                </div>
                                <a href="#!" class="view-info-product">${item.nombre}</a>`;
                    }
                },
                {
                    data: function (item) {
                        return `<div class="product-quantity">
                                    <span class="quantity-btn"><i class="fas fa-minus-circle feather-search"></i></span>
                                    <input type="text" class="quntity-input tbl_txt_cantidad" value="${item.cantidad}" data-val="true" type="number" min="1" data-previous-value="${item.cantidad}">
                                    <span class="quantity-btn">+<i class="fas fa-plus-circle plus-circle"></i></span>
                                </div>`;
                    }
                },
                {
                    data: "precio_venta",
                    render: function (data, type, row, meta) {
                        return `<span class="tbl_txt_precio">${row.moneda} ${data.toFixed(2)}</span>`;
                    }
                },
                {
                    data: "descuento",
                    render: function (data, type, row, meta) {
                        return `<span class="tbl_lbl_descuento">${row.moneda} ${data.toFixed(2)}</span>`;
                    }
                },
                {
                    data: "precio_total",
                    render: function (data, type, row, meta) {
                        return `<span class="tbl_lbl_precio_total">${row.moneda} ${data.toFixed(2)}</span>`;
                    }
                },
                {
                    data: function (item) {
                        return '<button class="btn btn-danger btn-sm tbl_btn_delete" type="button" role="button" title="Eliminar"><span class="fa fa-trash"></span></button>';
                    }
                }
            ],
            rowCallback: function (row, data) {
                var isManualChange = false;

                $(row).find(".quantity-btn").off("click").on("click", function () {
                    var $button = $(this);
                    var $inputCantidad = $button.closest('.product-quantity').find("input.quntity-input");

                    var cantidadActual = parseInt($inputCantidad.val()) || 0;
                    var nuevaCantidad = cantidadActual;

                    if ($button.text().includes("+")) {
                        if (cantidadActual < data.existencia) {
                            nuevaCantidad = cantidadActual + 1;
                        }
                    } else {
                        if (cantidadActual > 1) {
                            nuevaCantidad = cantidadActual - 1;
                        }
                    }

                    isManualChange = true;
                    $inputCantidad.val(nuevaCantidad);
                    isManualChange = false;

                    fnActualizarDatosCantidad(row, data, nuevaCantidad);
                });

                $(row).find('.tbl_txt_cantidad').off('blur').on('blur', function () {
                    var cantidad_ingresada = parseInt($(this).val()) || 0;
                    var valor_anterior = $(this).data('previous-value') || 1;

                    if (isNaN(cantidad_ingresada) || cantidad_ingresada <= 0) {
                        $(this).val(1);
                        data.cantidad = 1;
                    } else if (cantidad_ingresada > data.existencia) {
                        $(this).val(valor_anterior);
                        data.cantidad = valor_anterior;
                    } else {
                        data.cantidad = cantidad_ingresada;
                    }

                    fnActualizarDatosCantidad(row, data, data.cantidad);
                    $(this).data('previous-value', data.cantidad);
                });

                $(row).find(".tbl_btn_delete").on("click", function () {
                    var table = $("#grvProductosCompra").DataTable();
                    table.row($(this).parents('tr')).remove().draw();
                    fnUpdateTotales();
                });
            }
        });

        // Eventos
        $("#txtCodigoBarraProducto").on("keypress", function(e) {
            if (e.which === 13) { // Enter
                fnValidaCodigoBarra();
            }
        });

        $("#txtNitCliente").on("keypress", function(e) {
            if (e.which === 13) { // Enter
                fnBuscarCliente($(this).val());
            }
        });

        $("#btnBuscarCliente").on("click", function() {
            $("#modalClientes").modal("show");
        });

        $("#btnBuscarProducto").on("click", function() {
            $("#modalProductos").modal("show");
        });

        $("#btnMedioPago").on("click", function() {
            $("#modalMedioPago").modal("show");
        });

        $("input[name='rb_forma_pago']").on("change", function() {
            toggleMedioPago();
        });

        $("#btnCancelarVenta").on("click", function() {
            if (confirm("¿Está seguro de cancelar la venta?")) {
                fnLimpiarCamposFacturacion();
            }
        });

        // Evento submit del formulario
        $("#formVenta").on("submit", function(e) {
            e.preventDefault();

            // Validar que haya cliente
            if (!$("#hiddenIdCliente").val()) {
                alert("Debe seleccionar un cliente");
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
                    precioUnitario: data.precio_venta
                });
            });

            // Agregar campos ocultos con los datos de productos
            productos.forEach((prod, index) => {
                $("<input>").attr({
                    type: "hidden",
                    name: "idProducto",
                    value: prod.idProducto
                }).appendTo("#formVenta");

                $("<input>").attr({
                    type: "hidden",
                    name: "cantidad",
                    value: prod.cantidad
                }).appendTo("#formVenta");

                $("<input>").attr({
                    type: "hidden",
                    name: "precioUnitario",
                    value: prod.precioUnitario
                }).appendTo("#formVenta");
            });

            // Enviar formulario
            this.submit();
        });

        // Inicializar totales
        fnUpdateTotales();
        toggleMedioPago();

        // Establecer fecha actual
        const today = new Date().toISOString().split('T')[0];
        $("#txtFecha").val(today);
    });

</script>
