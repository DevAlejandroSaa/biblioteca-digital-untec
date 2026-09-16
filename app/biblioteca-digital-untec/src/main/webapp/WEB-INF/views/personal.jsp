<!-- Debe mostrar:

* Identificación del usuario personal.
* Solicitudes.
* Préstamos activos.
* Préstamos atrasados.
* Historial de préstamos y devoluciones.

Solicitudes:

* Estudiante.
* Libro.
* Fecha de solicitud.
* Estado.
* Acción para registrar entrega.

También debe permitir:

* Registrar una solicitud realizada presencialmente por un estudiante.

Préstamos activos:

* Estudiante.
* Libro.
* Fecha del préstamo.
* Fecha límite.
* Estado.
* Acción para registrar devolución.

Préstamos atrasados:

* Estudiante.
* Libro.
* Fecha del préstamo.
* Fecha límite.
* Estado.
* Indicador de atraso.

Historial:

* Estudiante.
* Libro.
* Fecha del préstamo.
* Fecha límite.
* Fecha de devolución.
* Estado.
* Resultado de la devolución.

Cómo debe mostrarlo:

* Información organizada en secciones claramente diferenciadas.
* Solicitudes, préstamos activos, atrasados e historial mediante tablas o estructuras equivalentes.
* Los estados deben diferenciarse visualmente.
* El registro de solicitud presencial debe realizarse mediante un modal.
* El registro de entrega debe realizarse mediante un modal.
* El registro de devolución debe realizarse mediante un modal.
* Las confirmaciones de operaciones deben mostrarse mediante modales cuando corresponda.

Con qué debe mostrarlo:

* HTML5.
* CSS3.
* Bootstrap 5 para tablas, botones, badges, formularios y modales.
* JSTL para representar la información recibida desde el servidor. -->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <div class="row g-4" id="panelPersonalContainer">
            <!-- Encabezado con Identificación y Botón de Acción Principal -->
            <div class="col-12">
                <div
                    class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 pb-3 border-bottom">
                    <div>
                        <h2 class="h3 fw-bold text-dark mb-1">
                            <i class="fa-solid fa-clipboard-user text-primary me-2"></i>Gestión de Préstamos y
                            Solicitudes
                        </h2>
                        <p class="text-muted small mb-0">
                            Personal a cargo: <strong class="text-dark">
                                <c:out value="${sessionScope.nombreUsuario}" />
                            </strong>
                            (<span class="badge bg-secondary">
                                <c:out value="${sessionScope.rol}" />
                            </span>)
                            — Administre entregas, devoluciones, mora y solicitudes presenciales.
                        </p>
                    </div>

                    <!-- Botón para Registrar Solicitud Presencial (Modal) -->
                    <div>
                        <button type="button"
                            class="btn btn-primary fw-semibold shadow-sm d-flex align-items-center gap-2"
                            data-bs-toggle="modal" data-bs-target="#modalSolicitudPresencial">
                            <i class="fa-solid fa-user-pen"></i>
                            <span>Nueva Solicitud Presencial</span>
                        </button>
                    </div>
                </div>
            </div>

            <!-- Navegación por Tabs / Pestañas (Bootstrap 5) -->
            <div class="col-12">
                <ul class="nav nav-tabs border-bottom mb-4" id="personalTab" role="tablist">
                    <!-- TAB 1: Solicitudes Pendientes -->
                    <li class="nav-item" role="presentation">
                        <button class="nav-link active d-flex align-items-center gap-2" id="solicitudes-tab"
                            data-bs-toggle="tab" data-bs-target="#solicitudes-pane" type="button" role="tab"
                            aria-controls="solicitudes-pane" aria-selected="true">
                            <i class="fa-solid fa-bell text-warning"></i>
                            <span>Solicitudes</span>
                            <c:if test="${not empty solicitudes && not empty solicitudes.registros}">
                                <span
                                    class="badge bg-warning text-dark rounded-pill">${solicitudes.totalRegistros}</span>
                            </c:if>
                        </button>
                    </li>

                    <!-- TAB 2: Préstamos Activos -->
                    <li class="nav-item" role="presentation">
                        <button class="nav-link d-flex align-items-center gap-2" id="activos-tab" data-bs-toggle="tab"
                            data-bs-target="#activos-pane" type="button" role="tab" aria-controls="activos-pane"
                            aria-selected="false">
                            <i class="fa-solid fa-book-bookmark text-primary"></i>
                            <span>Préstamos Activos</span>
                            <c:if test="${not empty prestamosActivos && not empty prestamosActivos.registros}">
                                <span class="badge bg-primary rounded-pill">${prestamosActivos.totalRegistros}</span>
                            </c:if>
                        </button>
                    </li>

                    <!-- TAB 3: Préstamos Atrasados -->
                    <li class="nav-item" role="presentation">
                        <button class="nav-link d-flex align-items-center gap-2" id="atrasados-tab" data-bs-toggle="tab"
                            data-bs-target="#atrasados-pane" type="button" role="tab" aria-controls="atrasados-pane"
                            aria-selected="false">
                            <i class="fa-solid fa-triangle-exclamation text-danger"></i>
                            <span>Préstamos Atrasados</span>
                            <c:if test="${not empty prestamosAtrasados && not empty prestamosAtrasados.registros}">
                                <span class="badge bg-danger rounded-pill">${prestamosAtrasados.totalRegistros}</span>
                            </c:if>
                        </button>
                    </li>

                    <!-- TAB 4: Historial de Préstamos y Devoluciones -->
                    <li class="nav-item" role="presentation">
                        <button class="nav-link d-flex align-items-center gap-2" id="historial-tab" data-bs-toggle="tab"
                            data-bs-target="#historial-pane" type="button" role="tab" aria-controls="historial-pane"
                            aria-selected="false">
                            <i class="fa-solid fa-clock-rotate-left text-secondary"></i>
                            <span>Historial</span>
                        </button>
                    </li>
                </ul>

                <!-- Contenedor de Pestañas -->
                <div class="tab-content" id="personalTabContent">

                    <!-- TAB PANE 1: Solicitudes (Acción para Registrar Entrega) -->
                    <div class="tab-pane fade show active" id="solicitudes-pane" role="tabpanel"
                        aria-labelledby="solicitudes-tab" tabindex="0">
                        <div class="card border-0 shadow-sm">
                            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                                <h5 class="card-title fw-bold text-dark mb-0">
                                    <i class="fa-solid fa-hourglass-half me-2 text-warning"></i>Solicitudes Pendientes
                                    de Entrega
                                </h5>
                            </div>
                            <div class="card-body p-0">
                                <c:choose>
                                    <c:when test="${not empty solicitudes && not empty solicitudes.registros}">
                                        <div class="table-responsive">
                                            <table class="table table-hover align-middle mb-0">
                                                <thead class="table-light text-secondary small text-uppercase">
                                                    <tr>
                                                        <th scope="col" class="ps-4">Estudiante</th>
                                                        <th scope="col">Libro</th>
                                                        <th scope="col">Fecha Solicitud</th>
                                                        <th scope="col" class="text-center">Estado</th>
                                                        <th scope="col" class="text-end pe-4">Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="s" items="${solicitudes.registros}">
                                                        <tr>
                                                            <td class="ps-4">
                                                                <div class="d-flex align-items-center gap-2">
                                                                    <div
                                                                        class="bg-light p-2 rounded-circle text-primary">
                                                                        <i class="fa-solid fa-user-graduate"></i>
                                                                    </div>
                                                                    <div>
                                                                        <strong class="d-block text-dark">
                                                                            <c:out
                                                                                value="${not empty s.nombreEstudiante ? (s.nombreEstudiante + ' ' + s.apellidoEstudiante) : s.username}" />
                                                                        </strong>
                                                                        <span class="text-muted small">
                                                                            <c:out value="${s.emailEstudiante}" />
                                                                        </span>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                            <td>
                                                                <span class="fw-semibold text-dark d-block">
                                                                    <c:out value="${s.tituloLibro}" />
                                                                </span>
                                                                <span class="text-muted small">
                                                                    <c:out value="${s.autorLibro}" />
                                                                </span>
                                                            </td>
                                                            <td class="text-secondary small">
                                                                <i class="fa-regular fa-calendar me-1"></i>
                                                                <c:out value="${s.fechaPrestamo}" />
                                                            </td>
                                                            <td class="text-center">
                                                                <span class="badge bg-warning text-dark px-3 py-2">
                                                                    <i class="fa-solid fa-clock me-1"></i>
                                                                    <c:out value="${s.estado}" />
                                                                </span>
                                                            </td>
                                                            <td class="text-end pe-4">
                                                                <button type="button"
                                                                    class="btn btn-success btn-sm fw-semibold btn-registrar-entrega"
                                                                    data-id="${s.id}"
                                                                    data-estudiante="<c:out value='${not empty s.nombreEstudiante ? (s.nombreEstudiante + " " + s.apellidoEstudiante) : s.username}'/>"
                                                                    data-libro="<c:out value='${s.tituloLibro}'/>">
                                                                    <i class="fa-solid fa-hand-holding me-1"></i>
                                                                    Registrar Entrega
                                                                </button>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="p-5 text-center text-muted">
                                            <i class="fa-solid fa-check-circle display-6 mb-2 text-success"></i>
                                            <p class="mb-0">No hay solicitudes pendientes de despacho en este momento.
                                            </p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <!-- TAB PANE 2: Préstamos Activos (Acción para Registrar Devolución) -->
                    <div class="tab-pane fade" id="activos-pane" role="tabpanel" aria-labelledby="activos-tab"
                        tabindex="0">
                        <div class="card border-0 shadow-sm">
                            <div class="card-header bg-white py-3">
                                <h5 class="card-title fw-bold text-dark mb-0">
                                    <i class="fa-solid fa-book-open-reader me-2 text-primary"></i>Libros Actualmente en
                                    Préstamo
                                </h5>
                            </div>
                            <div class="card-body p-0">
                                <c:choose>
                                    <c:when
                                        test="${not empty prestamosActivos && not empty prestamosActivos.registros}">
                                        <div class="table-responsive">
                                            <table class="table table-hover align-middle mb-0">
                                                <thead class="table-light text-secondary small text-uppercase">
                                                    <tr>
                                                        <th scope="col" class="ps-4">Estudiante</th>
                                                        <th scope="col">Libro</th>
                                                        <th scope="col">Fecha Préstamo</th>
                                                        <th scope="col">Fecha Límite</th>
                                                        <th scope="col" class="text-center">Estado</th>
                                                        <th scope="col" class="text-end pe-4">Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="p" items="${prestamosActivos.registros}">
                                                        <tr>
                                                            <td class="ps-4">
                                                                <strong class="d-block text-dark">
                                                                    <c:out
                                                                        value="${not empty p.nombreEstudiante ? (p.nombreEstudiante + ' ' + p.apellidoEstudiante) : p.username}" />
                                                                </strong>
                                                                <span class="text-muted small">
                                                                    <c:out value="${p.emailEstudiante}" />
                                                                </span>
                                                            </td>
                                                            <td>
                                                                <span class="fw-semibold text-dark d-block">
                                                                    <c:out value="${p.tituloLibro}" />
                                                                </span>
                                                                <span class="text-muted small">
                                                                    <c:out value="${p.autorLibro}" />
                                                                </span>
                                                            </td>
                                                            <td class="text-secondary small">
                                                                <c:out value="${p.fechaPrestamo}" />
                                                            </td>
                                                            <td class="text-secondary small">
                                                                <span class="badge bg-light text-dark border">
                                                                    <i class="fa-solid fa-calendar-day me-1"></i>
                                                                    <c:out value="${p.fechaLimite}" />
                                                                </span>
                                                            </td>
                                                            <td class="text-center">
                                                                <span class="badge bg-primary px-3 py-2">
                                                                    <c:out value="${p.estado}" />
                                                                </span>
                                                            </td>
                                                            <td class="text-end pe-4">
                                                                <button type="button"
                                                                    class="btn btn-primary btn-sm fw-semibold btn-registrar-devolucion"
                                                                    data-id="${p.id}"
                                                                    data-estudiante="<c:out value='${not empty p.nombreEstudiante ? (p.nombreEstudiante + " " + p.apellidoEstudiante) : p.username}'/>"
                                                                    data-libro="<c:out value='${p.tituloLibro}'/>"
                                                                    data-limite="<c:out value='${p.fechaLimite}'/>">
                                                                    <i class="fa-solid fa-arrow-rotate-left me-1"></i>
                                                                    Registrar Devolución
                                                                </button>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="p-5 text-center text-muted">
                                            <i class="fa-solid fa-books display-6 mb-2"></i>
                                            <p class="mb-0">No hay libros actualmente prestados.</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <!-- TAB PANE 3: Préstamos Atrasados (Indicador de Atraso + Acción Devolución) -->
                    <div class="tab-pane fade" id="atrasados-pane" role="tabpanel" aria-labelledby="atrasados-tab"
                        tabindex="0">
                        <div class="card border-0 shadow-sm border-danger">
                            <div class="card-header bg-danger text-white py-3">
                                <h5 class="card-title fw-bold mb-0">
                                    <i class="fa-solid fa-triangle-exclamation me-2"></i>Préstamos con Plazo Vencido
                                    (Atrasados)
                                </h5>
                            </div>
                            <div class="card-body p-0">
                                <c:choose>
                                    <c:when
                                        test="${not empty prestamosAtrasados && not empty prestamosAtrasados.registros}">
                                        <div class="table-responsive">
                                            <table class="table table-hover align-middle mb-0">
                                                <thead class="table-light text-secondary small text-uppercase">
                                                    <tr>
                                                        <th scope="col" class="ps-4">Estudiante</th>
                                                        <th scope="col">Libro</th>
                                                        <th scope="col">Fecha Préstamo</th>
                                                        <th scope="col">Fecha Límite</th>
                                                        <th scope="col" class="text-center">Indicador de Atraso</th>
                                                        <th scope="col" class="text-end pe-4">Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="a" items="${prestamosAtrasados.registros}">
                                                        <tr class="table-danger-subtle">
                                                            <td class="ps-4">
                                                                <strong class="d-block text-dark">
                                                                    <c:out
                                                                        value="${not empty a.nombreEstudiante ? (a.nombreEstudiante + ' ' + a.apellidoEstudiante) : a.username}" />
                                                                </strong>
                                                                <span class="text-muted small">
                                                                    <c:out value="${a.emailEstudiante}" />
                                                                </span>
                                                            </td>
                                                            <td>
                                                                <span class="fw-semibold text-dark d-block">
                                                                    <c:out value="${a.tituloLibro}" />
                                                                </span>
                                                                <span class="text-muted small">
                                                                    <c:out value="${a.autorLibro}" />
                                                                </span>
                                                            </td>
                                                            <td class="text-secondary small">
                                                                <c:out value="${a.fechaPrestamo}" />
                                                            </td>
                                                            <td class="text-danger fw-semibold small">
                                                                <i class="fa-solid fa-calendar-xmark me-1"></i>
                                                                <c:out value="${a.fechaLimite}" />
                                                            </td>
                                                            <td class="text-center">
                                                                <span
                                                                    class="badge bg-danger text-white px-3 py-2 shadow-sm">
                                                                    <i
                                                                        class="fa-solid fa-triangle-exclamation me-1"></i>PLAZO
                                                                    VENCIDO
                                                                </span>
                                                            </td>
                                                            <td class="text-end pe-4">
                                                                <button type="button"
                                                                    class="btn btn-danger btn-sm fw-semibold btn-registrar-devolucion"
                                                                    data-id="${a.id}"
                                                                    data-estudiante="<c:out value='${not empty a.nombreEstudiante ? (a.nombreEstudiante + " " + a.apellidoEstudiante) : a.username}'/>"
                                                                    data-libro="<c:out value='${a.tituloLibro}'/>"
                                                                    data-limite="<c:out value='${a.fechaLimite}'/>">
                                                                    <i class="fa-solid fa-arrow-rotate-left me-1"></i>
                                                                    Registrar Devolución
                                                                </button>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="p-5 text-center text-muted">
                                            <i class="fa-solid fa-circle-check display-6 mb-2 text-success"></i>
                                            <p class="mb-0">¡Excelente! No existen préstamos con atraso actualmente.</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <!-- TAB PANE 4: Historial General -->
                    <div class="tab-pane fade" id="historial-pane" role="tabpanel" aria-labelledby="historial-tab"
                        tabindex="0">
                        <div class="card border-0 shadow-sm">
                            <div class="card-header bg-white py-3">
                                <h5 class="card-title fw-bold text-dark mb-0">
                                    <i class="fa-solid fa-timeline me-2 text-secondary"></i>Historial de Devoluciones y
                                    Préstamos Finalizados
                                </h5>
                            </div>
                            <div class="card-body p-0">
                                <c:choose>
                                    <c:when test="${not empty historial && not empty historial.registros}">
                                        <div class="table-responsive">
                                            <table class="table table-hover align-middle mb-0">
                                                <thead class="table-light text-secondary small text-uppercase">
                                                    <tr>
                                                        <th scope="col" class="ps-4">Estudiante</th>
                                                        <th scope="col">Libro</th>
                                                        <th scope="col">Fecha Préstamo</th>
                                                        <th scope="col">Fecha Límite</th>
                                                        <th scope="col">Fecha Devolución</th>
                                                        <th scope="col" class="text-center">Estado</th>
                                                        <th scope="col" class="text-center">Resultado</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="h" items="${historial.registros}">
                                                        <tr>
                                                            <td class="ps-4">
                                                                <strong class="text-dark d-block">
                                                                    <c:out
                                                                        value="${not empty h.nombreEstudiante ? (h.nombreEstudiante + ' ' + h.apellidoEstudiante) : h.username}" />
                                                                </strong>
                                                                <span class="text-muted small">
                                                                    <c:out value="${h.emailEstudiante}" />
                                                                </span>
                                                            </td>
                                                            <td>
                                                                <span class="fw-semibold text-dark d-block">
                                                                    <c:out value="${h.tituloLibro}" />
                                                                </span>
                                                                <span class="text-muted small">
                                                                    <c:out value="${h.autorLibro}" />
                                                                </span>
                                                            </td>
                                                            <td class="text-secondary small">
                                                                <c:out value="${h.fechaPrestamo}" />
                                                            </td>
                                                            <td class="text-secondary small">
                                                                <c:out value="${h.fechaLimite}" />
                                                            </td>
                                                            <td class="text-secondary small">
                                                                <c:out
                                                                    value="${not empty h.fechaDevolucion ? h.fechaDevolucion : '-'}" />
                                                            </td>
                                                            <td class="text-center">
                                                                <span
                                                                    class="badge ${h.estado == 'DEVUELTO' ? 'bg-success' : 'bg-secondary'}">
                                                                    <c:out value="${h.estado}" />
                                                                </span>
                                                            </td>
                                                            <td class="text-center">
                                                                <c:choose>
                                                                    <c:when
                                                                        test="${not empty h.fechaDevolucion && h.fechaDevolucion le h.fechaLimite}">
                                                                        <span
                                                                            class="badge bg-success-subtle text-success border border-success-subtle px-2 py-1">
                                                                            <i class="fa-solid fa-check me-1"></i>A
                                                                            Tiempo (+puntos)
                                                                        </span>
                                                                    </c:when>
                                                                    <c:when
                                                                        test="${not empty h.fechaDevolucion && h.fechaDevolucion gt h.fechaLimite}">
                                                                        <span
                                                                            class="badge bg-danger-subtle text-danger border border-danger-subtle px-2 py-1">
                                                                            <i
                                                                                class="fa-solid fa-triangle-exclamation me-1"></i>Atrasado
                                                                            (-puntos)
                                                                        </span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span
                                                                            class="badge bg-light text-muted border">En
                                                                            Curso</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="p-5 text-center text-muted">
                                            <i class="fa-solid fa-folder-open display-6 mb-2"></i>
                                            <p class="mb-0">No se registran préstamos en el historial aún.</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- ======================================================== -->
        <!-- MODALES DE GESTIÓN PARA PERSONAL                          -->
        <!-- ======================================================== -->

        <!-- Modal 1: Registrar Entrega / Préstamo Presencial en Mesón -->
        <div class="modal fade" id="modalSolicitudPresencial" tabindex="-1" aria-labelledby="modalPresencialLabel"
            aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered modal-lg">
                <div class="modal-content border-0 shadow-lg">
                    <div class="modal-header bg-warning text-dark">
                        <h5 class="modal-title fw-bold" id="modalPresencialLabel">
                            <i class="fa-solid fa-hand-holding-hand me-2"></i>Registrar Entrega Presencial en Mesón
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <form action="${pageContext.request.contextPath}/personal" method="POST"
                        id="formSolicitudPresencial">
                        <input type="hidden" name="accion" value="registrarSolicitudPresencial">
                        <div class="modal-body p-4">
                            <div
                                class="alert alert-light border d-flex align-items-center py-2 px-3 mb-3 small text-muted">
                                <i class="fa-solid fa-circle-info text-primary fs-5 me-2"></i>
                                <div>Para estudiantes que solicitan en persona en el mesón, ingrese los datos del alumno
                                    y el ejemplar a entregar.</div>
                            </div>

                            <h6 class="fw-bold text-dark border-bottom pb-2 mb-3">
                                <i class="fa-solid fa-id-card me-1 text-primary"></i> 1. Datos del Estudiante
                            </h6>
                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <label for="presencialRut" class="form-label small fw-semibold">RUT / Identificación
                                        del Estudiante *</label>
                                    <input type="text" class="form-control" id="presencialRut" name="rutEstudiante"
                                        placeholder="Ej: 20.123.456-7" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="presencialNombre" class="form-label small fw-semibold">Nombre y
                                        Apellidos del Estudiante *</label>
                                    <input type="text" class="form-control" id="presencialNombre"
                                        name="nombreEstudiante" placeholder="Ej: Matías González" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="presencialEmail" class="form-label small fw-semibold">Correo Electrónico
                                        *</label>
                                    <input type="email" class="form-control" id="presencialEmail" name="emailEstudiante"
                                        placeholder="estudiante@untec.cl" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="presencialTelefono" class="form-label small fw-semibold">Teléfono de
                                        Contacto</label>
                                    <input type="text" class="form-control" id="presencialTelefono"
                                        name="telefonoEstudiante" placeholder="+56 9 8765 4321">
                                </div>
                            </div>

                            <h6 class="fw-bold text-dark border-bottom pb-2 mb-3">
                                <i class="fa-solid fa-book me-1 text-primary"></i> 2. Ejemplar a Entregar
                            </h6>
                            <div class="mb-3">
                                <label for="presencialLibroId" class="form-label small fw-semibold">Seleccionar Libro
                                    del Catálogo con Stock *</label>
                                <c:choose>
                                    <c:when test="${not empty libros && not empty libros.registros}">
                                        <select class="form-select" id="presencialLibroId" name="libroId" required>
                                            <option value="">-- Seleccione un libro disponible --</option>
                                            <c:forEach var="lib" items="${libros.registros}">
                                                <option value="${lib.id}" ${lib.cantidadDisponible <=0 ? 'disabled' : ''
                                                    }>
                                                    <c:out value="${lib.titulo}" /> (
                                                    <c:out value="${lib.cantidadDisponible}" /> disponibles)
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="number" class="form-control" id="presencialLibroId" name="libroId"
                                            placeholder="Ingrese el ID del libro" required min="1">
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="mb-3">
                                <label for="presencialObservaciones" class="form-label small fw-semibold">Observaciones
                                    de Entrega</label>
                                <input type="text" class="form-control" id="presencialObservaciones"
                                    name="observaciones"
                                    placeholder="Ej: Ejemplar entregado físicamente en mesón conforme al estudiante.">
                            </div>
                        </div>
                        <div class="modal-footer bg-light">
                            <button type="button" class="btn btn-outline-secondary btn-sm"
                                data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-warning btn-sm fw-bold px-3">
                                <i class="fa-solid fa-check me-1"></i> Realizar Entrega Presencial
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Modal 2: Registrar Entrega de Libro -->
        <div class="modal fade" id="modalRegistrarEntrega" tabindex="-1" aria-labelledby="modalEntregaLabel"
            aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title fw-bold" id="modalEntregaLabel">
                            <i class="fa-solid fa-hand-holding me-2"></i>Confirmar Entrega de Ejemplar
                        </h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                            aria-label="Cerrar"></button>
                    </div>
                    <form action="${pageContext.request.contextPath}/personal" method="POST">
                        <input type="hidden" name="accion" value="registrarEntrega">
                        <input type="hidden" name="prestamoId" id="entregaPrestamoId">
                        <div class="modal-body p-4 text-center">
                            <p class="mb-2">¿Confirma que se entrega físicamente el libro al estudiante?</p>
                            <div class="p-3 bg-light rounded border mb-3 text-start">
                                <div class="mb-1"><strong>Estudiante:</strong> <span id="entregaEstudiante"></span>
                                </div>
                                <div><strong>Libro:</strong> <span id="entregaLibro"></span></div>
                            </div>
                            <div class="alert alert-success py-2 px-3 small text-start mb-0">
                                <i class="fa-solid fa-circle-check me-1"></i> El estado cambiará a
                                <strong>PRESTADO</strong> y se computarán 5 días de plazo.
                            </div>
                        </div>
                        <div class="modal-footer bg-light">
                            <button type="button" class="btn btn-outline-secondary btn-sm"
                                data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-success btn-sm fw-semibold">
                                <i class="fa-solid fa-check me-1"></i> Confirmar Entrega
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Modal 3: Registrar Devolución de Libro -->
        <div class="modal fade" id="modalRegistrarDevolucion" tabindex="-1" aria-labelledby="modalDevolucionLabel"
            aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title fw-bold" id="modalDevolucionLabel">
                            <i class="fa-solid fa-arrow-rotate-left me-2"></i>Registrar Devolución en Mesón
                        </h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                            aria-label="Cerrar"></button>
                    </div>
                    <form action="${pageContext.request.contextPath}/personal" method="POST">
                        <input type="hidden" name="accion" value="registrarDevolucion">
                        <input type="hidden" name="prestamoId" id="devolucionPrestamoId">
                        <div class="modal-body p-4">
                            <div class="p-3 bg-light rounded border mb-3">
                                <div class="mb-1"><strong>Estudiante:</strong> <span id="devolucionEstudiante"></span>
                                </div>
                                <div class="mb-1"><strong>Libro:</strong> <span id="devolucionLibro"></span></div>
                                <div><strong>Fecha Límite Pactada:</strong> <span id="devolucionLimite"
                                        class="text-danger fw-semibold"></span></div>
                            </div>

                            <div class="mb-3">
                                <label for="fechaDevolucionInput" class="form-label small fw-semibold">Fecha Efectiva de
                                    Recepción *</label>
                                <input type="date" class="form-control" id="fechaDevolucionInput" name="fechaDevolucion"
                                    required>
                                <div class="form-text small">Por defecto la fecha del día de hoy. El sistema calculará
                                    automáticamente la bonificación o penalización de puntos.</div>
                            </div>
                        </div>
                        <div class="modal-footer bg-light">
                            <button type="button" class="btn btn-outline-secondary btn-sm"
                                data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-primary btn-sm fw-semibold">
                                <i class="fa-solid fa-check me-1"></i> Completar Devolución
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Script ES6 para enlazar los modales -->
        <script>
            document.addEventListener('DOMContentLoaded', () => {
                // Modal Entrega
                document.querySelectorAll('.btn-registrar-entrega').forEach(btn => {
                    btn.addEventListener('click', () => {
                        const d = btn.dataset;
                        document.getElementById('entregaPrestamoId').value = d.id;
                        document.getElementById('entregaEstudiante').textContent = d.estudiante || '-';
                        document.getElementById('entregaLibro').textContent = d.libro || '-';

                        const modal = new bootstrap.Modal(document.getElementById('modalRegistrarEntrega'));
                        modal.show();
                    });
                });

                // Modal Devolución
                document.querySelectorAll('.btn-registrar-devolucion').forEach(btn => {
                    btn.addEventListener('click', () => {
                        const d = btn.dataset;
                        document.getElementById('devolucionPrestamoId').value = d.id;
                        document.getElementById('devolucionEstudiante').textContent = d.estudiante || '-';
                        document.getElementById('devolucionLibro').textContent = d.libro || '-';
                        document.getElementById('devolucionLimite').textContent = d.limite || '-';

                        // Fecha actual en formato YYYY-MM-DD
                        const today = new Date().toISOString().split('T')[0];
                        document.getElementById('fechaDevolucionInput').value = today;

                        const modal = new bootstrap.Modal(document.getElementById('modalRegistrarDevolucion'));
                        modal.show();
                    });
                });
            });
        </script>