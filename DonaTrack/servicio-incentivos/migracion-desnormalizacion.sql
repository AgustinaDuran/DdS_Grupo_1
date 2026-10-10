-- Opcional. Sólo si el esquema de incentivos ya tenía datos antes de la entrega 5.
-- Hibernate (ddl-auto=update) agrega las columnas nuevas, pero las filas viejas quedan en NULL.
-- La aplicación las recalcula sola al leerlas, pero la query de posición en el ranking
-- (countDonantesConMasDonaciones) compara total_donaciones en la base, así que conviene completarlas.

UPDATE donaciones_registradas d
SET d.cantidad_bienes = (SELECT COUNT(*) FROM donacion_registrada_bienes b WHERE b.donacion_id = d.id),
    d.periodo = DATE_FORMAT(d.fecha_ingreso, '%Y-%m')
WHERE d.cantidad_bienes IS NULL OR d.periodo IS NULL;

UPDATE donantes_incentivos di
SET di.total_donaciones = (SELECT COUNT(*) FROM donaciones_registradas d WHERE d.donante_id = di.nombre_usuario),
    di.impacto_acumulado = (SELECT COALESCE(SUM(d.cantidad_bienes), 0) FROM donaciones_registradas d WHERE d.donante_id = di.nombre_usuario)
WHERE di.total_donaciones IS NULL OR di.impacto_acumulado IS NULL;