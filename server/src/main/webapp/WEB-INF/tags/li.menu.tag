<%@tag pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@attribute name="href" fragment="false" required="false"%>
<%@attribute name="modal" fragment="false" required="false"%>
<%@attribute name="icon" fragment="false" required="false"%>
<%@attribute name="title" fragment="false" required="false"%>
<%@attribute name="cls" fragment="false" required="false"%>
<jsp:doBody var="bodyText"></jsp:doBody>
<c:choose>
	<c:when test="${not empty bodyText}">
		<c:choose>
			<c:when test="${not empty title}">
				<c:choose>
					<c:when test="${not empty icon}">
						<li class="dropdown ${cls}"><a href="#"
							class="dropdown-toggle" data-toggle="dropdown"><i
								class="${icon}"></i><span class="mr-5 ml-3">${title}</span><span
								class="caret"></span> </a>
							<ul class="dropdown-menu" role="menu">${bodyText}
							</ul></li>
					</c:when>
					<c:otherwise>
						<li class="dropdown ${cls}"><a href="#"
							class="dropdown-toggle" data-toggle="dropdown"><span
								class="mr-5">${title}</span><span class="caret"></span> </a>
							<ul class="dropdown-menu" role="menu">${bodyText}
							</ul></li>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${not empty icon}">
						<li class="dropdown ${cls}"><a href="#"
							class="dropdown-toggle" data-toggle="dropdown"><i
								class="${icon} mr-5"></i><span class="caret"></span> </a>
							<ul class="dropdown-menu" role="menu">${bodyText}
							</ul></li>
					</c:when>
					<c:otherwise>
						<li class="dropdown ${cls}"><a href="#"
							class="dropdown-toggle" data-toggle="dropdown"><span
								class="caret"></span> </a>
							<ul class="dropdown-menu" role="menu">${bodyText}
							</ul></li>
					</c:otherwise>
				</c:choose>


			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${not empty title}">
				<c:choose>
					<c:when test="${not empty modal}">
						<c:choose>
							<c:when test="${not empty icon}">
								<li class="${cls}"><a href="${modal}" data-remote="false"
									data-toggle="modal" data-target="#modal"><i
										class="${icon} mr-5"></i><span>${title}</span></a></li>
							</c:when>
							<c:otherwise>
								<li class="${cls}"><a href="${modal}" data-remote="false"
									data-toggle="modal" data-target="#modal"><span>${title}</span></a></li>
							</c:otherwise>
						</c:choose>


					</c:when>
					<c:when test="${not empty href}">
						<c:choose>
							<c:when test="${not empty icon}">
								<li class="${cls}"><a href="${href}"><i
										class="${icon} mr-5"></i><span>${title}</span></a></li>
							</c:when>
							<c:otherwise>
								<li class="${cls}"><a href="${href}"><span>${title}</span></a></li>
							</c:otherwise>
						</c:choose>


					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${not empty icon}">
								<li class="${cls}"><a href="#"><i class="${icon} mr-5"></i><span>${title}</span></a></li>
							</c:when>
							<c:otherwise>
								<li class="${cls}"><a href="#"><span>${title}</span></a></li>
							</c:otherwise>
						</c:choose>

					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${not empty modal}">
						<c:choose>
							<c:when test="${not empty icon}">
								<li class="${cls}"><a href="${modal}" data-remote="false"
									data-toggle="modal" data-target="#modal"><i class="${icon}"></i></a></li>
							</c:when>
							<c:otherwise>
								<li class="${cls}"><a href="${modal}" data-remote="false"
									data-toggle="modal" data-target="#modal"></a></li>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:when test="${not empty href}">
						<c:choose>
							<c:when test="${not empty icon}">
								<li class="${cls}"><a href="${href}"><i class="${icon}"></i></a></li>
							</c:when>
							<c:otherwise>
								<li class="${cls}"><a href="${href}"></a></li>

							</c:otherwise>
						</c:choose>

					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${not empty icon}">
								<li class="${cls}"><a href="#"></a></li>
							</c:when>
							<c:otherwise>
								<li class="${cls}"><a href="#"></a></li>

							</c:otherwise>
						</c:choose>

					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>
