package com.sistemaempresa.reportes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.sistemaempresa.config.DatabaseConnection;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@WebServlet(
  name = "ReporteServlet",
  description = "Servlet que administra la lógica necesaria para JasperReports",
  urlPatterns = {"/jasper-report"}
)
public class ReporteServlet extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String[] reportesDisponibles = {"inventario", "ventas", "compras", "empleados", "clientes"}; // Listado de reportes disponibles
    String tipoReporte = request.getParameter("tipo");
    String plantillaReporte = null; // plantilla.jrxml
    String tituloReporte = null; // Reporte - Sistema Empresa
    String nombreArchivo = null; // reporte.pdf
    SimpleDateFormat formatoFechaReporte = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
    SimpleDateFormat formatoFechaTitulo = new SimpleDateFormat("dd/MM/yyyy");
    Connection conn = null;
    Date now = new Date(); // Obtener fecha

    if (Arrays.stream(reportesDisponibles).anyMatch(tipoReporte::equals)) {
      System.out.println("=== INICIANDO REPORTE ===");
      System.out.println("Tipo: " + tipoReporte);
      String fechaReporte = formatoFechaReporte.format(now);

      // 1. Determinar qué reporte generar
      plantillaReporte = tipoReporte + ".jrxml";
      nombreArchivo = "reporte_" + tipoReporte + "_" + fechaReporte + ".pdf";
      tituloReporte = "REPORTE DE " + tipoReporte.toUpperCase() + " AL " + formatoFechaTitulo.format(now);

      try {
      // 2. Obtener conexión
      conn = DatabaseConnection.getConnection();
      System.out.println("Conexión BD: " + (conn != null ? "OK" : "ERROR"));

      if (conn == null) {
        response.getWriter().println("Error: No hay conexión a BD");

        return;
      }

      // 3. Cargar el reporte
      InputStream reportStream = getClass().getResourceAsStream(plantillaReporte);

      System.out.println("Report stream: " + (reportStream != null ? "OK" : "NULL"));

      if (reportStream == null) {
        response.setContentType("text/html");
        response.getWriter().println("<h3>Error: Archivo JRXML no encontrado</h3>");
        response.getWriter().println("<p>Buscando en: " + getClass().getPackage().getName() + "/" + plantillaReporte + "</p>");
        return;
      }

      // 4. Parámetros
      HashMap<String, Object> parametros = new HashMap<>();
      parametros.put("TITULO_REPORTE", tituloReporte);

      // 5. Compilar el reporte JRXML a JasperReport
      System.out.println("Compilando JRXML...");
      JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
      System.out.println("JRXML compilado OK");

      // 6. Generar reporte
      System.out.println("Generando JasperPrint...");
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conn);
      System.out.println("JasperPrint generado OK");

      // 7. Configurar respuesta HTML
      response.setContentType("application/pdf");
      response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

      // 8. Exportar a PDF
      OutputStream out = response.getOutputStream();
      JasperExportManager.exportReportToPdfStream(jasperPrint, out);
      out.flush();

      System.out.println("=== REPORTE GENERADO EXITOSAMENTE ===");

    } catch (Exception e) {
        System.out.println("=== ERROR EN REPORTE ===");
        e.printStackTrace();

        response.setContentType("text/html");
        response.getWriter().println("<h3>Error generando reporte:</h3>");
        response.getWriter().println("<pre>");
        e.printStackTrace(response.getWriter());
        response.getWriter().println("</pre>");
    } finally {
      if (conn != null) {
        try { conn.close(); } catch (Exception e) {}
      }
    }

    } else {
      System.out.println("** REPORTE NO DISPONIBLE **");
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
}