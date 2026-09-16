<!-- Debe mostrar:

* Título del catálogo.
* Identificación del usuario autenticado.
* Opciones correspondientes al rol.
* Campo de búsqueda.
* Filtros.
* Listado de libros.
* Título.
* Autor.
* ISBN.
* Editorial.
* Año de publicación.
* Categoría.
* Cantidad disponible.

Para ESTUDIANTE debe mostrar las acciones:

* Ver detalles.
* Solicitar libro.

Para PERSONAL debe mostrar las acciones:

* Ver detalles.
* Agregar libro.
* Editar libro.
* Eliminar libro.

Cómo debe mostrarlo:

* Los libros deben mostrarse de forma clara y ordenada.
* La información principal debe estar visible en el catálogo.
* Las acciones deben estar asociadas a cada libro.
* Los detalles deben mostrarse mediante un modal.
* Las operaciones de solicitar, agregar y editar deben utilizar modales.
* La eliminación debe utilizar un modal de confirmación.
* Los resultados deben organizarse de acuerdo con la búsqueda y los filtros.

Con qué debe mostrarlo:

* HTML5.
* CSS3.
* Bootstrap 5 para tarjetas, botones, formularios, filtros y modales.
* JSTL para representar la información recibida desde el servidor. -->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <div class="row g-4" id="catalogoLibrosContainer">
            <!-- Encabezado de la Sección -->
            <div class="col-12">
                <div
                    class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 pb-3 border-bottom">
                    <div>
                        <h2 class="h3 fw-bold text-dark mb-1">
                            <i class="fa-solid fa-book-open text-primary me-2"></i>Catálogo de Libros
                        </h2>
                        <p class="text-muted small mb-0">
                            Explore y consulte el catálogo bibliográfico de la Universidad UNTEC.
                            <c:if test="${not empty sessionScope.nombreUsuario}">
                                Sesión activa como: <strong class="text-dark">
                                    <c:out value="${sessionScope.nombreUsuario}" />
                                </strong>
                                (<span class="badge bg-secondary">
                                    <c:out value="${sessionScope.rol}" />
                                </span>)
                            </c:if>
                        </p>
                    </div>

                    <!-- Acciones según el ROL: Botón Agregar Libro solo para PERSONAL -->
                    <c:if test="${sessionScope.rol == 'PERSONAL'}">
                        <div>
                            <button type="button"
                                class="btn btn-primary fw-semibold shadow-sm d-flex align-items-center gap-2"
                                data-bs-toggle="modal" data-bs-target="#modalAgregarLibro">
                                <i class="fa-solid fa-plus-circle"></i>
                                <span>Agregar Libro</span>
                            </button>
                        </div>
                    </c:if>
                </div>
            </div>

            <!-- Barra de Búsqueda y Filtros -->
            <div class="col-12">
                <div class="card shadow-sm border-0">
                    <div class="card-body p-3 p-md-4">
                        <form action="${pageContext.request.contextPath}/libros" method="GET"
                            class="row g-3 align-items-end" id="formFiltroLibros">
                            <!-- Campo de búsqueda por Título / Autor / ISBN -->
                            <div class="col-12 col-md-5">
                                <label for="buscar" class="form-label small fw-semibold text-secondary">
                                    <i class="fa-solid fa-magnifying-glass me-1"></i> Búsqueda por Título
                                </label>
                                <input type="text" class="form-control" id="buscar" name="titulo"
                                    value="<c:out value='${not empty titulo ? titulo : param.titulo}'/>"
                                    placeholder="Ej: Programación, Cálculo, Redes...">
                            </div>

                            <!-- Filtro por Categoría -->
                            <div class="col-12 col-md-4">
                                <label for="categoria" class="form-label small fw-semibold text-secondary">
                                    <i class="fa-solid fa-layer-group me-1"></i> Categoría
                                </label>
                                <select class="form-select" id="categoria" name="categoria">
                                    <option value="">Todas las categorías</option>
                                    <option value="Informática" ${param.categoria=='Informática' ||
                                        categoria=='Informática' ? 'selected' : '' }>Informática</option>
                                    <option value="Matemáticas" ${param.categoria=='Matemáticas' ||
                                        categoria=='Matemáticas' ? 'selected' : '' }>Matemáticas</option>
                                    <option value="Literatura" ${param.categoria=='Literatura' ||
                                        categoria=='Literatura' ? 'selected' : '' }>Literatura</option>
                                    <option value="Historia" ${param.categoria=='Historia' || categoria=='Historia'
                                        ? 'selected' : '' }>Historia</option>
                                    <option value="Ciencias" ${param.categoria=='Ciencias' || categoria=='Ciencias'
                                        ? 'selected' : '' }>Ciencias</option>
                                    <option value="Administración" ${param.categoria=='Administración' ||
                                        categoria=='Administración' ? 'selected' : '' }>Administración</option>
                                </select>
                            </div>

                            <!-- Botones de Acción de Filtro -->
                            <div class="col-12 col-md-3 d-flex gap-2">
                                <button type="submit" class="btn btn-primary w-100 fw-semibold">
                                    <i class="fa-solid fa-filter me-1"></i> Filtrar
                                </button>
                                <a href="${pageContext.request.contextPath}/libros"
                                    class="btn btn-outline-secondary w-auto" title="Limpiar Filtros">
                                    <i class="fa-solid fa-rotate-left"></i>
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Listado de Libros (Grid Cards con información clara y ordenada) -->
            <div class="col-12">
                <c:choose>
                    <c:when test="${not empty paginacion && not empty paginacion.registros}">
                        <div class="row row-cols-1 row-cols-sm-2 row-cols-lg-3 row-cols-xl-4 g-4">
                            <c:forEach var="libro" items="${paginacion.registros}">
                                <div class="col">
                                    <div class="card h-100 shadow-sm border-0 transition-hover position-relative">
                                        <!-- Badge de Disponibilidad -->
                                        <div class="position-absolute top-0 end-0 m-3">
                                            <c:choose>
                                                <c:when test="${libro.cantidadDisponible > 0}">
                                                    <span class="badge bg-success shadow-sm">
                                                        <i
                                                            class="fa-solid fa-check me-1"></i>${libro.cantidadDisponible}
                                                        disponibles
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-danger shadow-sm">
                                                        <i class="fa-solid fa-xmark me-1"></i>Agotado
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <div class="card-body p-4 d-flex flex-column">
                                            <div class="mb-2">
                                                <span
                                                    class="badge bg-light text-primary border small text-uppercase fw-semibold">
                                                    <c:out
                                                        value="${not empty libro.categoria ? libro.categoria : 'General'}" />
                                                </span>
                                            </div>
                                            <h5 class="card-title fw-bold text-dark text-truncate mb-1"
                                                title="<c:out value='${libro.titulo}'/>">
                                                <c:out value="${libro.titulo}" />
                                            </h5>
                                            <p class="card-text text-secondary small mb-3">
                                                <i class="fa-solid fa-pen-nib me-1 text-muted"></i>
                                                <c:out value="${libro.autor}" />
                                            </p>

                                            <!-- Datos Clave del Libro -->
                                            <div class="mt-auto bg-light p-2 rounded small text-muted mb-3">
                                                <div class="d-flex justify-content-between mb-1">
                                                    <span><strong>ISBN:</strong></span>
                                                    <span class="text-dark">
                                                        <c:out value="${libro.isbn}" />
                                                    </span>
                                                </div>
                                                <div class="d-flex justify-content-between mb-1">
                                                    <span><strong>Editorial:</strong></span>
                                                    <span class="text-dark">
                                                        <c:out
                                                            value="${not empty libro.editorial ? libro.editorial : 'N/A'}" />
                                                    </span>
                                                </div>
                                                <div class="d-flex justify-content-between">
                                                    <span><strong>Año:</strong></span>
                                                    <span class="text-dark">
                                                        <c:out
                                                            value="${not empty libro.anioPublicacion ? libro.anioPublicacion : 'N/A'}" />
                                                    </span>
                                                </div>
                                            </div>

                                            <!-- Acciones según el rol -->
                                            <div class="d-flex flex-wrap gap-2 pt-2 border-top mt-2">
                                                <!-- Acción: Ver Detalles (Común a Estudiante y Personal) -->
                                                <button type="button"
                                                    class="btn btn-outline-secondary btn-sm flex-fill btn-ver-detalles"
                                                    data-id="${libro.id}" data-titulo="<c:out value='${libro.titulo}'/>"
                                                    data-autor="<c:out value='${libro.autor}'/>"
                                                    data-isbn="<c:out value='${libro.isbn}'/>"
                                                    data-editorial="<c:out value='${libro.editorial}'/>"
                                                    data-anio="<c:out value='${libro.anioPublicacion}'/>"
                                                    data-categoria="<c:out value='${libro.categoria}'/>"
                                                    data-cantidad="${libro.cantidad}"
                                                    data-disponible="${libro.cantidadDisponible}">
                                                    <i class="fa-solid fa-eye me-1"></i> Ver Detalles
                                                </button>

                                                <!-- Acciones para ESTUDIANTE: Solicitar libro -->
                                                <c:if test="${sessionScope.rol == 'ESTUDIANTE'}">
                                                    <c:choose>
                                                        <c:when test="${libro.cantidadDisponible > 0}">
                                                            <button type="button"
                                                                class="btn btn-primary btn-sm flex-fill btn-solicitar-libro"
                                                                data-id="${libro.id}"
                                                                data-titulo="<c:out value='${libro.titulo}'/>">
                                                                <i class="fa-solid fa-hand-holding-heart me-1"></i>
                                                                Solicitar
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <button type="button"
                                                                class="btn btn-secondary btn-sm flex-fill" disabled>
                                                                No Disponible
                                                            </button>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:if>

                                                <!-- Acciones para PERSONAL: Editar y Eliminar -->
                                                <c:if test="${sessionScope.rol == 'PERSONAL'}">
                                                    <button type="button"
                                                        class="btn btn-outline-primary btn-sm btn-editar-libro"
                                                        title="Editar Libro" data-id="${libro.id}"
                                                        data-titulo="<c:out value='${libro.titulo}'/>"
                                                        data-autor="<c:out value='${libro.autor}'/>"
                                                        data-isbn="<c:out value='${libro.isbn}'/>"
                                                        data-editorial="<c:out value='${libro.editorial}'/>"
                                                        data-anio="<c:out value='${libro.anioPublicacion}'/>"
                                                        data-categoria="<c:out value='${libro.categoria}'/>"
                                                        data-cantidad="${libro.cantidad}"
                                                        data-disponible="${libro.cantidadDisponible}">
                                                        <i class="fa-solid fa-pencil"></i>
                                                    </button>
                                                    <button type="button"
                                                        class="btn btn-outline-danger btn-sm btn-eliminar-libro"
                                                        title="Eliminar Libro" data-id="${libro.id}"
                                                        data-titulo="<c:out value='${libro.titulo}'/>">
                                                        <i class="fa-solid fa-trash-can"></i>
                                                    </button>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <!-- Paginación con Paginacion model -->
                        <c:if test="${paginacion.totalPaginas > 1}">
                            <nav class="d-flex justify-content-center mt-5" aria-label="Navegación de páginas">
                                <ul class="pagination shadow-sm">
                                    <li class="page-item ${!paginacion.tienePaginaAnterior ? 'disabled' : ''}">
                                        <a class="page-link"
                                            href="${pageContext.request.contextPath}/libros?pagina=${paginacion.paginaActual - 1}&titulo=${param.titulo}&categoria=${param.categoria}">
                                            <i class="fa-solid fa-chevron-left me-1"></i> Anterior
                                        </a>
                                    </li>

                                    <c:forEach var="i" begin="1" end="${paginacion.totalPaginas}">
                                        <li class="page-item ${paginacion.paginaActual == i ? 'active' : ''}">
                                            <a class="page-link"
                                                href="${pageContext.request.contextPath}/libros?pagina=${i}&titulo=${param.titulo}&categoria=${param.categoria}">
                                                ${i}
                                            </a>
                                        </li>
                                    </c:forEach>

                                    <li class="page-item ${!paginacion.tienePaginaSiguiente ? 'disabled' : ''}">
                                        <a class="page-link"
                                            href="${pageContext.request.contextPath}/libros?pagina=${paginacion.paginaActual + 1}&titulo=${param.titulo}&categoria=${param.categoria}">
                                            Siguiente <i class="fa-solid fa-chevron-right ms-1"></i>
                                        </a>
                                    </li>
                                </ul>
                            </nav>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <div class="card shadow-sm border-0 p-5 text-center my-4">
                            <div class="text-muted mb-3">
                                <i class="fa-solid fa-book-bookmark display-4"></i>
                            </div>
                            <h5 class="fw-bold text-dark">No se encontraron libros</h5>
                            <p class="text-muted small mb-3">Intente ajustar los términos de búsqueda o filtros
                                seleccionados.</p>
                            <div>
                                <a href="${pageContext.request.contextPath}/libros"
                                    class="btn btn-outline-primary btn-sm">
                                    <i class="fa-solid fa-rotate-left me-1"></i> Restablecer Catálogo
                                </a>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- ======================================================== -->
        <!-- MODALES DEL CATÁLOGO                                      -->
        <!-- ======================================================== -->

        <!-- Modal 1: Ver Detalles del Libro -->
        <div class="modal fade" id="modalVerDetalles" tabindex="-1" aria-labelledby="modalDetallesLabel"
            aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg">
                    <div class="modal-header bg-light">
                        <h5 class="modal-title fw-bold" id="modalDetallesLabel">
                            <i class="fa-solid fa-circle-info text-primary me-2"></i>Detalles del Libro
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="text-center mb-4">
                            <div class="bg-light d-inline-flex p-3 rounded-circle text-primary mb-2">
                                <i class="fa-solid fa-book fs-1"></i>
                            </div>
                            <h4 class="fw-bold text-dark mb-1" id="detalleTitulo">-</h4>
                            <p class="text-muted mb-0" id="detalleAutor">-</p>
                        </div>

                        <div class="list-group list-group-flush border rounded">
                            <div class="list-group-item d-flex justify-content-between align-items-center">
                                <span class="text-muted"><i class="fa-solid fa-barcode me-2"></i>ISBN:</span>
                                <strong id="detalleIsbn">-</strong>
                            </div>
                            <div class="list-group-item d-flex justify-content-between align-items-center">
                                <span class="text-muted"><i class="fa-solid fa-building me-2"></i>Editorial:</span>
                                <span id="detalleEditorial">-</span>
                            </div>
                            <div class="list-group-item d-flex justify-content-between align-items-center">
                                <span class="text-muted"><i class="fa-solid fa-calendar me-2"></i>Año de
                                    Publicación:</span>
                                <span id="detalleAnio">-</span>
                            </div>
                            <div class="list-group-item d-flex justify-content-between align-items-center">
                                <span class="text-muted"><i class="fa-solid fa-tag me-2"></i>Categoría:</span>
                                <span class="badge bg-primary" id="detalleCategoria">-</span>
                            </div>
                            <div class="list-group-item d-flex justify-content-between align-items-center">
                                <span class="text-muted"><i class="fa-solid fa-boxes-stacked me-2"></i>Total
                                    Ejemplares:</span>
                                <span id="detalleCantidad">-</span>
                            </div>
                            <div class="list-group-item d-flex justify-content-between align-items-center">
                                <span class="text-muted"><i class="fa-solid fa-check-circle me-2"></i>Disponibles para
                                    Préstamo:</span>
                                <strong class="text-success" id="detalleDisponible">-</strong>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer bg-light">
                        <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal 2: Solicitar Libro (ESTUDIANTE) -->
        <c:if test="${sessionScope.rol == 'ESTUDIANTE'}">
            <div class="modal fade" id="modalSolicitarLibro" tabindex="-1" aria-labelledby="modalSolicitarLabel"
                aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content border-0 shadow-lg">
                        <div class="modal-header bg-primary text-white">
                            <h5 class="modal-title fw-bold" id="modalSolicitarLabel">
                                <i class="fa-solid fa-hand-holding-heart me-2"></i>Confirmar Solicitud de Préstamo
                            </h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                aria-label="Cerrar"></button>
                        </div>
                        <form action="${pageContext.request.contextPath}/libros" method="POST">
                            <input type="hidden" name="accion" value="solicitar">
                            <input type="hidden" name="libroId" id="solicitarLibroId">
                            <div class="modal-body p-4 text-center">
                                <p class="mb-3">¿Desea solicitar en préstamo el siguiente ejemplar?</p>
                                <div class="p-3 bg-light rounded border mb-3">
                                    <h5 class="fw-bold text-dark mb-0" id="solicitarLibroTitulo"></h5>
                                </div>
                                <div class="alert alert-info py-2 px-3 small text-start mb-0">
                                    <i class="fa-solid fa-circle-info me-1"></i> El préstamo tendrá una duración de 5
                                    días hábiles a partir de la entrega.
                                </div>
                            </div>
                            <div class="modal-footer bg-light">
                                <button type="button" class="btn btn-outline-secondary btn-sm"
                                    data-bs-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-primary btn-sm fw-semibold">
                                    <i class="fa-solid fa-check me-1"></i> Confirmar Solicitud
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Modal 3: Agregar Libro (PERSONAL) -->
        <c:if test="${sessionScope.rol == 'PERSONAL'}">
            <div class="modal fade" id="modalAgregarLibro" tabindex="-1" aria-labelledby="modalAgregarLabel"
                aria-hidden="true">
                <div class="modal-dialog modal-lg modal-dialog-centered">
                    <div class="modal-content border-0 shadow-lg">
                        <div class="modal-header bg-primary text-white">
                            <h5 class="modal-title fw-bold" id="modalAgregarLabel">
                                <i class="fa-solid fa-circle-plus me-2"></i>Registrar Nuevo Libro
                            </h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                aria-label="Cerrar"></button>
                        </div>
                        <form action="${pageContext.request.contextPath}/libros" method="POST" id="formAgregarLibro">
                            <input type="hidden" name="accion" value="agregar">
                            <div class="modal-body p-4">
                                <div class="row g-3">
                                    <div class="col-12 col-md-8">
                                        <label class="form-label small fw-semibold">Título del Libro *</label>
                                        <input type="text" class="form-control" name="titulo" required
                                            placeholder="Ej: Clean Code">
                                    </div>
                                    <div class="col-12 col-md-4">
                                        <label class="form-label small fw-semibold">ISBN *</label>
                                        <input type="text" class="form-control" name="isbn" required
                                            placeholder="Ej: 9780132350884">
                                    </div>
                                    <div class="col-12 col-md-6">
                                        <label class="form-label small fw-semibold">Autor *</label>
                                        <input type="text" class="form-control" name="autor" required
                                            placeholder="Ej: Robert C. Martin">
                                    </div>
                                    <div class="col-12 col-md-6">
                                        <label class="form-label small fw-semibold">Editorial</label>
                                        <input type="text" class="form-control" name="editorial"
                                            placeholder="Ej: Prentice Hall">
                                    </div>
                                    <div class="col-12 col-md-4">
                                        <label class="form-label small fw-semibold">Año de Publicación</label>
                                        <input type="date" class="form-control" name="anioPublicacion">
                                    </div>
                                    <div class="col-12 col-md-4">
                                        <label class="form-label small fw-semibold">Categoría</label>
                                        <input type="text" class="form-control" name="categoria"
                                            placeholder="Ej: Informática">
                                    </div>
                                    <div class="col-6 col-md-2">
                                        <label class="form-label small fw-semibold">Cantidad Total *</label>
                                        <input type="number" class="form-control" name="cantidad" min="1" value="1"
                                            required>
                                    </div>
                                    <div class="col-6 col-md-2">
                                        <label class="form-label small fw-semibold">Disponibles *</label>
                                        <input type="number" class="form-control" name="cantidadDisponible" min="0"
                                            value="1" required>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer bg-light">
                                <button type="button" class="btn btn-outline-secondary btn-sm"
                                    data-bs-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-primary btn-sm fw-semibold">
                                    <i class="fa-solid fa-floppy-disk me-1"></i> Guardar Libro
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Modal 4: Editar Libro (PERSONAL) -->
            <div class="modal fade" id="modalEditarLibro" tabindex="-1" aria-labelledby="modalEditarLabel"
                aria-hidden="true">
                <div class="modal-dialog modal-lg modal-dialog-centered">
                    <div class="modal-content border-0 shadow-lg">
                        <div class="modal-header bg-primary text-white">
                            <h5 class="modal-title fw-bold" id="modalEditarLabel">
                                <i class="fa-solid fa-pencil me-2"></i>Editar Información del Libro
                            </h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                aria-label="Cerrar"></button>
                        </div>
                        <form action="${pageContext.request.contextPath}/libros" method="POST" id="formEditarLibro">
                            <input type="hidden" name="accion" value="editar">
                            <input type="hidden" name="libroId" id="editLibroId">
                            <div class="modal-body p-4">
                                <div class="row g-3">
                                    <div class="col-12 col-md-8">
                                        <label class="form-label small fw-semibold">Título del Libro *</label>
                                        <input type="text" class="form-control" name="titulo" id="editTitulo" required>
                                    </div>
                                    <div class="col-12 col-md-4">
                                        <label class="form-label small fw-semibold">ISBN *</label>
                                        <input type="text" class="form-control" name="isbn" id="editIsbn" required>
                                    </div>
                                    <div class="col-12 col-md-6">
                                        <label class="form-label small fw-semibold">Autor *</label>
                                        <input type="text" class="form-control" name="autor" id="editAutor" required>
                                    </div>
                                    <div class="col-12 col-md-6">
                                        <label class="form-label small fw-semibold">Editorial</label>
                                        <input type="text" class="form-control" name="editorial" id="editEditorial">
                                    </div>
                                    <div class="col-12 col-md-4">
                                        <label class="form-label small fw-semibold">Año de Publicación</label>
                                        <input type="date" class="form-control" name="anioPublicacion" id="editAnio">
                                    </div>
                                    <div class="col-12 col-md-4">
                                        <label class="form-label small fw-semibold">Categoría</label>
                                        <input type="text" class="form-control" name="categoria" id="editCategoria">
                                    </div>
                                    <div class="col-6 col-md-2">
                                        <label class="form-label small fw-semibold">Cantidad Total *</label>
                                        <input type="number" class="form-control" name="cantidad" id="editCantidad"
                                            min="1" required>
                                    </div>
                                    <div class="col-6 col-md-2">
                                        <label class="form-label small fw-semibold">Disponibles *</label>
                                        <input type="number" class="form-control" name="cantidadDisponible"
                                            id="editDisponible" min="0" required>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer bg-light">
                                <button type="button" class="btn btn-outline-secondary btn-sm"
                                    data-bs-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-primary btn-sm fw-semibold">
                                    <i class="fa-solid fa-floppy-disk me-1"></i> Actualizar Cambios
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Modal 5: Eliminar Libro (PERSONAL) - Modal de Confirmación -->
            <div class="modal fade" id="modalEliminarLibro" tabindex="-1" aria-labelledby="modalEliminarLabel"
                aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content border-0 shadow-lg">
                        <div class="modal-header bg-danger text-white">
                            <h5 class="modal-title fw-bold" id="modalEliminarLabel">
                                <i class="fa-solid fa-triangle-exclamation me-2"></i>Confirmar Eliminación
                            </h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                aria-label="Cerrar"></button>
                        </div>
                        <form action="${pageContext.request.contextPath}/libros" method="POST">
                            <input type="hidden" name="accion" value="eliminar">
                            <input type="hidden" name="libroId" id="eliminarLibroId">
                            <div class="modal-body p-4 text-center">
                                <div class="text-danger mb-3">
                                    <i class="fa-solid fa-trash-can display-4"></i>
                                </div>
                                <h6 class="fw-bold mb-2">¿Está seguro de eliminar este libro del catálogo?</h6>
                                <p class="text-muted small mb-0" id="eliminarLibroTitulo"></p>
                            </div>
                            <div class="modal-footer bg-light">
                                <button type="button" class="btn btn-outline-secondary btn-sm"
                                    data-bs-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-danger btn-sm fw-semibold">
                                    <i class="fa-solid fa-trash-can me-1"></i> Eliminar Definitivamente
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Script ES6 para inicializar los modales dinámicamente -->
        <script>
            document.addEventListener('DOMContentLoaded', () => {
                // Modal Ver Detalles
                document.querySelectorAll('.btn-ver-detalles').forEach(btn => {
                    btn.addEventListener('click', () => {
                        const d = btn.dataset;
                        document.getElementById('detalleTitulo').textContent = d.titulo || '-';
                        document.getElementById('detalleAutor').textContent = d.autor || '-';
                        document.getElementById('detalleIsbn').textContent = d.isbn || '-';
                        document.getElementById('detalleEditorial').textContent = d.editorial || '-';
                        document.getElementById('detalleAnio').textContent = d.anio || '-';
                        document.getElementById('detalleCategoria').textContent = d.categoria || 'General';
                        document.getElementById('detalleCantidad').textContent = d.cantidad || '0';
                        document.getElementById('detalleDisponible').textContent = d.disponible || '0';

                        const modal = new bootstrap.Modal(document.getElementById('modalVerDetalles'));
                        modal.show();
                    });
                });

                // Modal Solicitar Libro
                document.querySelectorAll('.btn-solicitar-libro').forEach(btn => {
                    btn.addEventListener('click', () => {
                        const d = btn.dataset;
                        document.getElementById('solicitarLibroId').value = d.id;
                        document.getElementById('solicitarLibroTitulo').textContent = d.titulo;
                        const modal = new bootstrap.Modal(document.getElementById('modalSolicitarLibro'));
                        modal.show();
                    });
                });

                // Modal Editar Libro
                document.querySelectorAll('.btn-editar-libro').forEach(btn => {
                    btn.addEventListener('click', () => {
                        const d = btn.dataset;
                        document.getElementById('editLibroId').value = d.id;
                        document.getElementById('editTitulo').value = d.titulo || '';
                        document.getElementById('editAutor').value = d.autor || '';
                        document.getElementById('editIsbn').value = d.isbn || '';
                        document.getElementById('editEditorial').value = d.editorial || '';
                        document.getElementById('editAnio').value = d.anio || '';
                        document.getElementById('editCategoria').value = d.categoria || '';
                        document.getElementById('editCantidad').value = d.cantidad || '1';
                        document.getElementById('editDisponible').value = d.disponible || '1';

                        const modal = new bootstrap.Modal(document.getElementById('modalEditarLibro'));
                        modal.show();
                    });
                });

                // Modal Eliminar Libro
                document.querySelectorAll('.btn-eliminar-libro').forEach(btn => {
                    btn.addEventListener('click', () => {
                        const d = btn.dataset;
                        document.getElementById('eliminarLibroId').value = d.id;
                        document.getElementById('eliminarLibroTitulo').textContent = d.titulo;
                        const modal = new bootstrap.Modal(document.getElementById('modalEliminarLibro'));
                        modal.show();
                    });
                });
            });
        </script>