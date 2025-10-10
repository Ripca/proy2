#pragma once
#include <iostream>
#include <mysql.h>
#include <string>
#include "ConexionBD.h"

using namespace std;

class Empleado {
    // Atributos
private:
    int idEmpleado = 0;
    string nombres;
    string apellidos;
    string direccion;
    string telefono;
    string DPI;
    bool genero = false;
    string fecha_nacimiento;
    short idPuesto = 0;
    string fecha_inicio_labores;
    string fecha_ingreso;

    // Constructor
public:
    Empleado() {}
    Empleado(int id, string nom, string ape, string dir, string tel, string dpi, bool gen, string fn, short idP, string fil, string fi) {
        idEmpleado = id;
        nombres = nom;
        apellidos = ape;
        direccion = dir;
        telefono = tel;
        DPI = dpi;
        genero = gen;
        fecha_nacimiento = fn;
        idPuesto = idP;
        fecha_inicio_labores = fil;
        fecha_ingreso = fi;
    }

    // Métodos get y set
    void setIdEmpleado(int id) { idEmpleado = id; }
    void setNombres(string nom) { nombres = nom; }
    void setApellidos(string ape) { apellidos = ape; }
    void setDireccion(string dir) { direccion = dir; }
    void setTelefono(string tel) { telefono = tel; }
    void setDPI(string dpi) { DPI = dpi; }
    void setGenero(bool gen) { genero = gen; }
    void setFechaNacimiento(string fn) { fecha_nacimiento = fn; }
    void setIdPuesto(short idP) { idPuesto = idP; }
    void setFechaInicioLabores(string fil) { fecha_inicio_labores = fil; }
    void setFechaIngreso(string fi) { fecha_ingreso = fi; }

    int getIdEmpleado() { return idEmpleado; }
    string getNombres() { return nombres; }
    string getApellidos() { return apellidos; }
    string getDireccion() { return direccion; }
    string getTelefono() { return telefono; }
    string getDPI() { return DPI; }
    bool getGenero() { return genero; }
    string getFechaNacimiento() { return fecha_nacimiento; }
    short getIdPuesto() { return idPuesto; }
    string getFechaInicioLabores() { return fecha_inicio_labores; }
    string getFechaIngreso() { return fecha_ingreso; }

    // Métodos CRUD
    void crear() {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        cn.abrir_conexion();
        if (cn.getConector()) {
            string gen = genero ? "1" : "0";
            string idP = to_string(idPuesto);

            // Modificamos la consulta para usar AUTO_INCREMENT
            string consulta = "INSERT INTO Empleados(nombres, apellidos, direccion, telefono, DPI, genero, fecha_nacimiento, idPuesto, fecha_inicio_labores, fecha_ingreso) VALUES ('" + nombres + "', '" + apellidos + "', '" + direccion + "', '" + telefono + "', '" + DPI + "', " + gen + ", '" + fecha_nacimiento + "', " + idP + ", '" + fecha_inicio_labores + "', '" + fecha_ingreso + "');";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Ingreso de Empleado Exitoso..." << endl;
            }
            else {
                cout << "xxx Error al ingresar información xxx" << endl;
                cout << consulta << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        cn.cerrar_conexion();
    }

    void leer() {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        MYSQL_ROW fila;
        MYSQL_RES* resultado;
        cn.abrir_conexion();
        if (cn.getConector()) {
            string consulta = "SELECT e.idEmpleado, e.nombres, e.apellidos, e.direccion, e.telefono, e.DPI, IF(e.genero=1,'Masculino','Femenino') as genero, e.fecha_nacimiento, p.puesto, e.fecha_inicio_labores, e.fecha_ingreso FROM Empleados AS e INNER JOIN Puestos AS p ON e.idPuesto = p.idPuesto;";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                resultado = mysql_store_result(cn.getConector());
                cout << "---------------------------------------- EMPLEADOS ----------------------------------------" << endl;
                cout << "ID | NOMBRES | APELLIDOS | DIRECCIÓN | TELÉFONO | DPI | GÉNERO | NACIMIENTO | PUESTO | INICIO LABORES | INGRESO" << endl;
                cout << "--------------------------------------------------------------------------------------" << endl;
                while (fila = mysql_fetch_row(resultado)) {
                    cout << fila[0] << " | " << fila[1] << " | " << fila[2] << " | " << fila[3] << " | " << fila[4] << " | " << fila[5] << " | " << fila[6] << " | " << fila[7] << " | " << fila[8] << " | " << fila[9] << " | " << fila[10] << endl;
                }
                cout << "--------------------------------------------------------------------------------------" << endl;
            }
            else {
                cout << "xxx Error al consultar información xxx" << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        cn.cerrar_conexion();
    }

    void actualizar() {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        cn.abrir_conexion();
        if (cn.getConector()) {
            string id = to_string(idEmpleado);
            string gen = genero ? "1" : "0";
            string idP = to_string(idPuesto);

            string consulta = "UPDATE Empleados SET nombres = '" + nombres + "', apellidos = '" + apellidos + "', direccion = '" + direccion + "', telefono = '" + telefono + "', DPI = '" + DPI + "', genero = " + gen + ", fecha_nacimiento = '" + fecha_nacimiento + "', idPuesto = " + idP + ", fecha_inicio_labores = '" + fecha_inicio_labores + "', fecha_ingreso = '" + fecha_ingreso + "' WHERE idEmpleado = " + id + ";";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Actualización de Empleado Exitosa..." << endl;
            }
            else {
                cout << "xxx Error al actualizar información xxx" << endl;
                cout << consulta << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        cn.cerrar_conexion();
    }

    void eliminar() {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        cn.abrir_conexion();
        if (cn.getConector()) {
            string id = to_string(idEmpleado);
            string consulta = "DELETE FROM Empleados WHERE idEmpleado = " + id + ";";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Eliminación de Empleado Exitosa..." << endl;
            }
            else {
                cout << "xxx Error al eliminar información xxx" << endl;
                cout << "Puede que este registro esté siendo utilizado en otra tabla" << endl;
                cout << consulta << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        cn.cerrar_conexion();
    }
};
