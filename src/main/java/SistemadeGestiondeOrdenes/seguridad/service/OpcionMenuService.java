package SistemadeGestiondeOrdenes.seguridad.service;

import SistemadeGestiondeOrdenes.seguridad.persistence.entities.OpcionMenu;

import java.util.List;

public interface OpcionMenuService {
    List<OpcionMenu> obtenerOpcionesMenu(String username);
}
