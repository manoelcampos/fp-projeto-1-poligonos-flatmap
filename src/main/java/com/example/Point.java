package com.example;

import java.awt.geom.Point2D;

/**
 * Representa um ponto com coordenadas x e y.
 * Um record imutável com distância acumulada.
 *
 * @param x        coordenada horizontal
 * @param y        coordenada vertical
 * @param distance distância acumulada até este ponto (0 se ponto inicial)
 */
public record Point(double x, double y, double distance) {
    /**
     * Cria um ponto com coordenadas x e y, distância 0.
     */
    public Point(final double x, final double y) {
        this(x, y, 0);
    }

    /**
     * Cria um ponto com as coordenadas do p2 e acumula distância somando
     * a distância anterior (p1.distance) com a distância de p1 para p2.
     *
     */
    public Point(final Point p1, final Point p2) {
        this(p2.x, p2.y, p1.distance + Point2D.distance(p1.x, p1.y, p2.x, p2.y));
    }
}
